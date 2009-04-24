package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.Simulators.DrumsSimulator;
import com.rkuo.RockBand.Simulators.DrumsSimulatorParameters;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;
import com.rkuo.Midi.*;
//import javax.sound.midi.*;
import com.sun.media.sound.JDK13Services;

//import javax.sound.midi.Sequence;
//import javax.sound.midi.MidiSystem;
//import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.spi.MidiFileReader;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 8:34:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandAnalyzer {

    public static boolean AnalyzeStream( PrintWriter psOut, InputStream inStream, RockBandAnalyzerParams rbap ) {

        Sequence s;
        DrumChart dc;
        
        RockBandPrint.PrintAnalyzerParameters( psOut, rbap );

        try {
            s = getSequence( inStream );
//            s = MidiSystem.getSequence( inStream );
        }
//        catch( InvalidMidiDataException imde ) {
//            psOut.format( "InvalidMidiDataException\n" );
//            psOut.format( "The stream may not be a valid MIDI file.\n" );
//            s = null;
//        }
        catch( Exception e ) {
            psOut.format( "Exception\n" );
            s = null;
        }

        if( s == null ) {
            psOut.format( "The stream could not be opened.\n");
            return false;
        }

        RockBandMidi.DumpSequence( s );
//        RockBandMidi.DumpTempoTrack( s );

        psOut.format( "Opened the stream successfully.\n" );

        dc = Convert.ToDrumChart( RockBandDifficulty.Expert, s );
//        dc = null;
        if( dc == null ) {
            psOut.format( "Convert.ToDrumChart returned null.\n");
            return false;
        }

        // Print stats
        if( rbap.PrintChart == true ) {
            RockBandPrint.PrintDrumChart( psOut, dc );
        }

        DrumsSimulator dg;
        DrumsSimulatorParameters dsp;
        DrumsBaselineData dbd;
        ArrayList<RockBandPath> paths;

        dg = new DrumsSimulator();
        dsp = new DrumsSimulatorParameters();
        dsp.FillDelay = RockBandConstants.FillDelayRB2Expert;

        RockBandPrint.PrintDrumsSimulatorParameters( psOut, dsp );

        dbd = dg.GenerateBaselineData( dc );
        RockBandPrint.PrintDrumsBaselineData( psOut, dbd );

        Integer[]   zeroPath;

        zeroPath = new Integer[1];
        zeroPath[0] = 0;

        paths = dg.GenerateScoreFromPath( dsp, dc, zeroPath );
        psOut.format( "===== Immediate activation path =====\n" );
        RockBandPrint.PrintPaths( psOut, paths );

        if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Predefined ) {
            paths = dg.GenerateScoreFromPath( dsp, dc, rbap.Path.toArray( new Integer[rbap.Path.size()]) );
        }
        else if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Optimal ) {
            paths = dg.GenerateOptimalPaths( dsp, dc );
            psOut.format( "===== Terminal paths generated by optimal search algorithm =====\n" );
        }
        else {
            paths = dg.GenerateAllPaths( dsp, dc );
        }

        RockBandPrint.PrintPaths( psOut, paths );

        return true;
    }

    private static Sequence getSequence( InputStream input ) {

        Sequence    s;
        float       divisionType;
        int         resolution;
        int         dwChunkSize;
        short       wFormatType;
        short       wNumberOfTracks;
        short       wTimeDivision;
        byte[]      header;
        String      chunkId;
        String      headerChunkId;
        String      trackChunkId;
        int         result;

        headerChunkId = "MThd";
        trackChunkId = "MTrk";

        header = new byte[14];

        try {
            result = input.read( header );
            if( result == -1 ) {
                return null;
            }
        }
        catch( IOException ioex ) {
            return null;
        }

        try {
            chunkId = new String( header, 0, 4, "utf-8" );
        }
        catch( UnsupportedEncodingException ueex ) {
            return null;
        }

        if( chunkId.compareTo(headerChunkId) != 0 ) {
            return null;
        }

        dwChunkSize = Convert.ToInt( header, 4 );
        if( dwChunkSize != 6 ) {
            return null;
        }

        wFormatType = Convert.ToShort( header, 8 );
        if( (wFormatType != 0) && (wFormatType != 1) && (wFormatType != 2) ) {
            return null;
        }

        wNumberOfTracks = Convert.ToShort( header, 10 );
        wTimeDivision = Convert.ToShort( header, 12 );

        divisionType = Sequence.PPQ;
        if( (wTimeDivision & 0x8000) == 0 ) {
            divisionType = Sequence.PPQ;
            resolution = wTimeDivision & 0x7FFF;
        }
        else {
            byte bSMPTE;

            bSMPTE = (byte)((wTimeDivision & 0x7F00) >> 8);
            if( bSMPTE == 24 ) {
                divisionType = Sequence.SMPTE_24;
            }
            else if( bSMPTE == 25 ) {
                divisionType = Sequence.SMPTE_25;
            }
            else if( bSMPTE == 29 ) {
                divisionType = Sequence.SMPTE_30DROP;
            }
            else if( bSMPTE == 30 ) {
                divisionType = Sequence.SMPTE_30;
            }

            resolution = wTimeDivision & 0x00FF;
        }

        try {
            s = new Sequence( divisionType, resolution );
        }
        catch( InvalidMidiDataException imdex ) {
            return null;
        }

        for( short x=0; x < wNumberOfTracks; x++ ) {

//            System.out.format( "Reading track %d.\n", x );

            try {
                result = input.read( header, 0, 8 );
                if( result == -1 ) {
                    return null;
                }
            }
            catch( IOException ioex ) {
                return null;
            }

            try {
                chunkId = new String( header, 0, 4, "utf-8" );
            }
            catch( UnsupportedEncodingException ueex ) {
                return null;
            }

            if( chunkId.compareTo(trackChunkId) != 0 ) {
                return null;
            }

//            dwChunkSize = Convert.ToInt( header, 4 );

            Track   t;
            long tick;
            int runningStatus;

            s.createTrack();
            t = s.getTracks()[x];

            tick = 0;
            runningStatus = 0x00;
            while( true ) {

                MidiEvent   mev;
                MidiMessage midiMsg;
                int         status;

                mev = GetMidiEvent( input, tick, runningStatus );
                if( mev == null ) {
                    break;
                }

                t.add( mev );

                midiMsg = mev.getMessage();
                status = midiMsg.getStatus();
                if( status == 0xFF ) {
                    byte[] message;
                    message = midiMsg.getMessage();
                    if( message[1] == 0x2F ) {
                        // end of track event
                        break;
                    }
                }
                else if( (status == 0xF0) || (status == 0xF7) ) {
                    // do nothing
                }
                else {
                    runningStatus = status;
                }

                tick = mev.getTick();
            }

//            System.out.format( "Finished reading track %d.\n", x );
        }

        return s;
    }

    private static MidiEvent GetMidiEvent( InputStream input, long tick, int runningStatus ) {
        MidiEvent   mev;
        MidiMessage midiMsg;
        VariableLengthValue vlfDeltaTick;

//        if( tick == 11520 ) {
//            System.out.format( "Hello!\n" );
//        }

        vlfDeltaTick = GetVLF( input );
        if( vlfDeltaTick == null ) {
            return null;
        }

        midiMsg = GetMidiMessage( input, runningStatus );
        if( midiMsg == null ) {
            return null;
        }

        mev = new MidiEvent( midiMsg, tick + vlfDeltaTick.GetLength() );

//        RockBandMidi.PrintMidiMessage( tick + vlfDeltaTick.GetLength(), midiMsg.getMessage() );
//        if( midiMsg.getStatus() == 0xFF ) {
//            byte[]  message;
//
//            message = midiMsg.getMessage();
//            if( message[1] == 0x2F ) {
//                System.out.format( "Hello!\n" );
//            }
//        }
        return mev;
    }

    private static MidiMessage GetMidiMessage( InputStream input, int runningStatus ) {

        MidiMessage midiMsg;
        int         eventCode;
        int         result;

        try {
            eventCode = input.read();
        }
        catch( IOException ioex ) {
            return null;
        }

        if( eventCode == 0xFF ) {
            MetaMessage metaMsg;
            int         messageType;
            byte[]       message;
            VariableLengthValue vlf;

            try {
                messageType = input.read();
            }
            catch( IOException ioex ) {
                return null;
            }

            vlf = GetVLF( input );
//            message = new byte[1 + vlf.GetBytes().length + vlf.GetLength()];
//            System.arraycopy( rawTrack, pos, message, 0, length + 2 );
//            message[0] = (byte)messageType;
//            System.arraycopy( vlf.GetBytes(), 0, message, 1, vlf.GetBytes().length );

            if( vlf.GetLength() > 0 ) {
                message = new byte[vlf.GetLength()];

                try {
                    result = input.read( message, 0, vlf.GetLength() );
                }
                catch( IOException ioex ) {
                    return null;
                }

                if( result == -1 ) {
                    return null;
                }
            }
            else {
                message = new byte[0];                
            }

            metaMsg = new MetaMessage();
            try {
                metaMsg.setMessage( messageType, message, message.length );
            }
            catch( InvalidMidiDataException imdex ) {
                return null;
            }

            midiMsg = (MidiMessage)metaMsg;
        }
        else if( (eventCode == 0xF0) || (eventCode == 0xF7) ) {
            SysexMessage sysexMsg;
            byte[]       message;
            int         length;
            VariableLengthValue vlf;

            vlf = GetVLF( input );
            message = new byte[1 + vlf.GetBytes().length + vlf.GetLength()];
            message[0] = (byte)eventCode;
            System.arraycopy( vlf.GetBytes(), 0, message, 1, vlf.GetBytes().length );

            if( vlf.GetLength() > 0 ) {
                try {
                    result = input.read( message, 1 + vlf.GetBytes().length, vlf.GetLength() );
                }
                catch( IOException ioex ) {
                    return null;
                }

                if( result == -1 ) {
                    return null;
                }
            }
            
            sysexMsg = new SysexMessage();
            try {
                sysexMsg.setMessage( message, message.length );
            }
            catch( InvalidMidiDataException imdex ) {
                return null;
            }

            midiMsg = (MidiMessage)sysexMsg;
        }
        else {
            ShortMessage    shortMsg;
            int             data1, data2;

            shortMsg = new ShortMessage();

            if( (eventCode & 0x80) == 0 ) {
                // running status mode
                data1 = eventCode;
                try {
                    data2 = input.read();
                }
                catch( IOException ioex ) {
                    return null;
                }
                eventCode = runningStatus;
            }
            else {
                // verbose mode
                try {
                    data1 = input.read();
                    data2 = input.read();
                }
                catch( IOException ioex ) {
                    return null;
                }
            }

            try {
                shortMsg.setMessage( eventCode, data1, data2 );
            }
            catch( InvalidMidiDataException imdex ) {
                return null;
            }

            midiMsg = (MidiMessage)shortMsg;
        }

        return midiMsg;
    }

    // VLF values cannot be more than 5 bytes long, and the value itself cannot be more than 4 bytes
    private static VariableLengthValue GetVLF( InputStream input ) {

        VariableLengthValue vlv;
        byte[]              buffer;
        byte[]              vlvBytes;
        int                 vlvByteCount;
        int value;

        buffer = new byte[5];

        vlvByteCount = 0;
        while( true ) {

            try {
                buffer[vlvByteCount] = (byte)input.read();
            }
            catch( IOException ioex ) {
                return null;
            }

            if( (buffer[vlvByteCount] & 0x80) == 0 ) {
                vlvByteCount++;
                break;
            }

            vlvByteCount++;
        }

        vlvBytes = new byte[vlvByteCount];
        System.arraycopy( buffer, 0, vlvBytes, 0, vlvByteCount );

        value = 0;
        for( int x=0; x < vlvByteCount; x++ ) {
            byte    b;
            int     shift;

            b = (byte)(vlvBytes[x] & 0x7F);
            shift = (vlvByteCount - 1 - x) * 7;
            value += b << shift;
        }

        vlv = new VariableLengthValue( value, vlvBytes );

        return vlv;
    }

    private static class VariableLengthValue {

        private int     _length;
        private byte[]  _rawBytes;
        public VariableLengthValue( int length, byte[] rawBytes ) {

            _length = length;
            _rawBytes = new byte[rawBytes.length];
            System.arraycopy( rawBytes, 0, _rawBytes, 0, rawBytes.length );
            return;
        }

        public int GetLength() {
            return _length;
        }

        public byte[] GetBytes() {
            return _rawBytes;
        }
    }
}
