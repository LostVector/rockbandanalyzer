package com.rkuo.RockBand;

import com.rkuo.Midi.*;
//import javax.sound.midi.Sequence;
//import javax.sound.midi.Track;
//import javax.sound.midi.MidiEvent;
//import javax.sound.midi.MidiMessage;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 26, 2008
 * Time: 9:06:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandMidi {

    public final static String  TrackNameGuitar = "PART GUITAR";
    public final static String  TrackNameBass = "PART BASS";
    // I've unfortunately seen tracks named as PART DRUM and PART DRUMS ... need to check for both
    public final static String  TrackNameDrum = "PART DRUM";
    public final static String  TrackNameDrums = "PART DRUMS";
    public final static String  TrackNameVocals = "PART VOCALS";
    public final static String  TrackNameBeat = "BEAT";

    public final static String  Red = "Red";
    public final static String  Yellow = "Yellow";
    public final static String  Blue = "Blue";
    public final static String  Green = "Green";
    public final static String  Orange = "Orange";

    // Used in the beat track
    // for 4/4 time, you see 12 13 13 13 12 13 13 13 etc...haven't checked other cases yet
    public final static int   BeatMajor = 12;
    public final static int   BeatMinor = 13;

    public final static int   GuitarExpertGreen = 96;
    public final static int   GuitarExpertRed = 97;
    public final static int   GuitarExpertYellow = 98;
    public final static int   GuitarExpertBlue = 99;
    public final static int   GuitarExpertOrange = 100;

    public final static int   DrumsEasyOrange = 60;
    public final static int   DrumsEasyRed = 61;
    public final static int   DrumsEasyYellow = 62;
    public final static int   DrumsEasyBlue = 63;
    public final static int   DrumsEasyGreen = 64;

    public final static int   DrumsMediumOrange = 72;
    public final static int   DrumsMediumRed = 73;
    public final static int   DrumsMediumYellow = 74;
    public final static int   DrumsMediumBlue = 75;
    public final static int   DrumsMediumGreen = 76;

    public final static int   DrumsHardOrange = 84;
    public final static int   DrumsHardRed = 85;
    public final static int   DrumsHardYellow = 86;
    public final static int   DrumsHardBlue = 87;
    public final static int   DrumsHardGreen = 88;

    public final static int   DrumsExpertOrange = 96;
    public final static int   DrumsExpertRed = 97;
    public final static int   DrumsExpertYellow = 98;
    public final static int   DrumsExpertBlue = 99;
    public final static int   DrumsExpertGreen = 100;

    public final static int   DrumsEasySoloMarker = 67;
    public final static int   DrumsMediumSoloMarker = 79;
    public final static int   DrumsHardSoloMarker = 91;
    public final static int   DrumsExpertSoloMarker = 103;

    // These color mappings may not be correct
    public final static int   DrumsFillRed = 120;
    public final static int   DrumsFillYellow = 121;
    public final static int   DrumsFillBlue = 122;
    public final static int   DrumsFillGreen = 123;
    public final static int   DrumsFillOrange = 124;

    public final static int   Tom1 = 110;
    public final static int   Tom2 = 111;
    public final static int   Tom3 = 112;

    // Vocals: five octaves worth of pitches are represented by the following range of notes, inclusive
    private final int   _vocalPitchStart = 36;
    private final int   _vocalPitchEnd = 95;
    
    public final static int TrackTempo = 0;
    public final static int TrackGuitar = 1;
    public final static int TrackBass = 2;
    public final static int TrackDrums = 3;
    public final static int TrackVocals = 4;
    public final static int TrackEvents = 5;
    public final static int TrackVenue = 6;

    // Have seen this not be 7.  could be last track?
//    public final static int TrackBeat = 7;

//    public final static int SoloMarker = 115;
    public final static int OverdriveMarker = 116;

    public final static int NoteOff = 0x80;
    public final static int NoteOn = 0x90;

    public static String toShortDrumColorString( int number ) {

        if( number == DrumsExpertRed ) {
            return "R";
        }
        else if( number == DrumsExpertYellow ) {
            return "Y";
        }
        else if( number == DrumsExpertBlue ) {
            return "B";
        }
        else if( number == DrumsExpertGreen ) {
            return "G";
        }
        else if( number == DrumsExpertOrange ) {
            return "O";
        }

        return null;
    }

    public static String toDrumColorString( int number ) {

        if( number == DrumsExpertRed ) {
            return Red;
        }
        else if( number == DrumsExpertYellow ) {
            return Yellow;
        }
        else if( number == DrumsExpertBlue ) {
            return Blue;
        }
        else if( number == DrumsExpertGreen ) {
            return Green;
        }
        else if( number == DrumsExpertOrange ) {
            return Orange;
        }

        return null;
    }

    public static int toDrumColorIndex( int number ) {

        if( number == DrumsExpertRed ) {
            return 0;
        }
        else if( number == DrumsExpertYellow ) {
            return 1;
        }
        else if( number == DrumsExpertBlue ) {
            return 2;
        }
        else if( number == DrumsExpertGreen ) {
            return 3;
        }
        else if( number == DrumsExpertOrange ) {
            return 4;
        }

        return -1;
    }

    public static void DumpSequence( Sequence s ) {

        float   divisionType;

        System.out.format( "===== Sequence information =====\n" );

        divisionType = s.getDivisionType();

        if( divisionType == Sequence.PPQ ) {
            System.out.format( "Division Type: PPQ\n");
        }
        else if( divisionType == Sequence.SMPTE_24 ) {
            System.out.format( "Division Type: SMPTE_24\n");
        }
        else if( divisionType == Sequence.SMPTE_25 ) {
            System.out.format( "Division Type: SMPTE_25\n");
        }
        else if( divisionType == Sequence.SMPTE_30 ) {
            System.out.format( "Division Type: SMPTE_30\n");
        }
        else if( divisionType == Sequence.SMPTE_30DROP ) {
            System.out.format( "Division Type: SMPTE_30DROP\n");
        }

        // for PPQ, this is usually 480, which means 480 ticks per quarter note
        System.out.format( "Resolution: %d\n", s.getResolution() );
        System.out.format( "Microsecond length: %d\n", s.getMicrosecondLength() );
        System.out.format( "Tick length: %d\n", s.getTickLength() );

        Track[] tracks;

        tracks = s.getTracks();
        for( Track t : tracks ) {

            String  trackName;

            trackName = GetTrackName( t );
            if( trackName == null ) {
                continue;
            }

            System.out.format( "Track: %s\n", trackName );
        }

        return;
    }

    public static boolean DumpTempoTrack( Sequence s ) {

        Track tempoTrack;

        System.out.format( "===== Tempo Track =====\n" );

        tempoTrack = s.getTracks()[0];
        if( tempoTrack == null ) {
            return false;
        }

        for( int x=0; x < tempoTrack.size(); x++ ) {
            MidiEvent me;
            MidiMessage mm;
            long        currentTick;
            byte[]      rawMessage;

            me = tempoTrack.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            rawMessage = mm.getMessage();

            PrintMidiMessage( currentTick, rawMessage );
        }

        return true;
    }


    public static void PrintMidiMessage( long currentTick, byte[] midiMessage ) {

        short   statusMessage;

        statusMessage = (short) (midiMessage[0] & 0xf0);

        if( statusMessage == 0x90 ) {

            String  sMessage;
            int nNote;
            int nVelocity;

            nNote = (int)midiMessage[1];
            nVelocity = (int)midiMessage[2];

            System.out.format( "T%d: ", currentTick );
            if( nVelocity > 0 ) {
                System.out.format( "NOTE ON - " );
            }
            else {
                System.out.format( "NOTE OFF - " );
            }
            System.out.format( "Note %d, ", nNote );
            System.out.format( "Velocity %d", nVelocity );
            sMessage = bytesToHex( midiMessage );
            System.out.format( " : [" + sMessage + "]");
            System.out.format( "\n" );
        }
        else if( statusMessage == 0xf0 ) {
            int     messageCode;
            int     messageLength;
            String  sMessage;

            System.out.format( "T%d: ", currentTick );
            System.out.format( "SYSEX - " );

            messageCode = (int)midiMessage[1];
            if( messageCode == 0x01 ) {
                // Text Event
                String  eventText;

                messageLength = (int)midiMessage[2];
                try {
                    eventText = new String( midiMessage, 3, messageLength, "utf-8" );
                    System.out.format( eventText );
                }
                catch( UnsupportedEncodingException uee ) {
                }
            }
            else if( messageCode == 0x03 ) {
                // Sequence/Track Name
                String  partTitle;

                messageLength = (int)midiMessage[2];
                try {
                    partTitle = new String( midiMessage, 3, messageLength, "utf-8" );
                    System.out.format( partTitle );
                }
                catch( UnsupportedEncodingException uee ) {
                }
            }
            else if( messageCode == 0x2f ) {
                // End of Track
                System.out.format( "End of Track" );
            }
            else if( messageCode == 0x51 ) {
                // Set Tempo

                System.out.format( "Set Tempo" );

                messageLength = (int)midiMessage[2];

//                assert( messageLength == 3 );
                if( messageLength == 3 ) {
                    byte[]  tempBytes;
                    int     tempo;
                    int     bpm;

                    tempBytes = new byte[4];
                    tempBytes[1] = midiMessage[3];
                    tempBytes[2] = midiMessage[4];
                    tempBytes[3] = midiMessage[5];
                    tempBytes[0] = 0;

                    tempo = Convert.ToInt( tempBytes );
                    bpm = 60000000 / tempo;
                    System.out.format( " to %d us per 1/4 note (%d bpm)", tempo, bpm );
                }
            }
            else if( messageCode == 0x58 ) {
                // Time Signature
                System.out.format( "Time Signature" );

                messageLength = (int)midiMessage[2];

                if( messageLength == 4 ) {
                    int numerator, denominator;
                    int midiClocksPerClick;
                    int n32ndNotesperMidiQuarterNote;

                    numerator = (int)midiMessage[3];
                    denominator = (int)midiMessage[4];
                    denominator = (int)Math.pow( 2, denominator );

                    midiClocksPerClick = (int)midiMessage[5];
                    n32ndNotesperMidiQuarterNote = (int)midiMessage[6];

                    System.out.format( " is %d/%d, %d MIDI clocks per click, %d 32nd notes per MIDI Quarter Note",
                            numerator, denominator, midiClocksPerClick, n32ndNotesperMidiQuarterNote );
                }
            }
            else {
                System.out.format( "Unknown" );
            }

            sMessage = bytesToHex( midiMessage );
            System.out.format( " : [" + sMessage + "]");
            System.out.format( "\n" );
        }
        else {

            String  sMessage;

            System.out.format( "UNKNOWN" );
            sMessage = bytesToHex( midiMessage );
            System.out.format( " : [" + sMessage + "]");
            System.out.format( "\n" );
        }

        return;
    }

    /**
     * Convenience method to convert a byte array to a hex string.
     *
     * @param data the byte[] to convert
     * @return String the converted byte[]
     */
    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            buf.append(byteToHex(data[i]));
        }
        return(buf.toString());
    }

    /**
     * Convenience method to convert a byte to a hex string.
     *
     * @param data the byte to convert
     * @return String the converted byte
     */
    public static String byteToHex(byte data) {
        StringBuffer buf = new StringBuffer();
        buf.append(toHexChar((data >>> 4) & 0x0F));
        buf.append(toHexChar(data & 0x0F));
        return buf.toString();
    }

    /**
     * Convenience method to convert an int to a hex char.
     *
     * @param i the int to convert
     * @return char the converted char
     */
    public static char toHexChar(int i) {
        if ((0 <= i) && (i <= 9))
            return (char) ('0' + i);
        else
            return (char) ('a' + (i-10));
    }

    public static Track GetTrack( Sequence s, String desiredTrackName ) {

        Track   returnTrack;
        Track[] tracks;

        returnTrack = null;

        tracks = s.getTracks();
        for( Track t : tracks ) {

            String  trackName;

            trackName = GetTrackName( t );
            if( trackName == null ) {
                continue;
            }

            if( trackName.compareTo( desiredTrackName ) == 0 ) {
                returnTrack = t;
                break;
            }
        }

        return returnTrack;
    }

    public static String GetTrackName( Track t ) {

        for( int x=0; x < t.size(); x++ ) {
            MidiEvent   me;
            MidiMessage mm;
            byte[]      rawMessage;
            short       statusMessage;

            me = t.get( x );

            mm = me.getMessage();
            rawMessage = mm.getMessage();

            statusMessage = (short) (rawMessage[0] & 0xf0);

            if( statusMessage == 0xf0 ) {

                int messageCode;
                int messageLength;

                messageCode = (int)rawMessage[1];
                if( messageCode == 0x03 ) {
                    String  partTitle;

                    messageLength = (int)rawMessage[2];
                    try {
                        partTitle = new String( rawMessage, 3, messageLength, "utf-8" );
                        return partTitle;
                    }
                    catch( UnsupportedEncodingException uee ) {
                        return null;
                    }
                }
            }
        }

        return null;
    }    
}
