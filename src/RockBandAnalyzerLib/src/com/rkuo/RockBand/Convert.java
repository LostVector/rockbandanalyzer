package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.Chord;
import com.rkuo.RockBand.Primitives.Note;
import com.rkuo.RockBand.Primitives.TickRange;
import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.Midi.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 26, 2008
 * Time: 2:31:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Convert {

    public static ArrayList<RockBandPath> DepthToRockBandPaths( RockBandPlayerState state, long fillDelay ) {

        ArrayList<RockBandPath> paths;
        ArrayList<RockBandPlayerState>  terminalStates;

        paths = new ArrayList<RockBandPath>();
        terminalStates = new ArrayList<RockBandPlayerState>();

        WalkActivationTree( state, terminalStates );

        Collections.sort( terminalStates, new RockBandPlayerStateScoreComparator() );

        for( RockBandPlayerState terminalState : terminalStates ) {
            RockBandPath    path;

            path = Convert.ToRockBandPath( terminalState, fillDelay );
            paths.add( path );
        }

        return paths;
    }

    private static void WalkActivationTree( RockBandPlayerState state, ArrayList<RockBandPlayerState> terminalStates ) {

        if( (state.NodeActivate == null) && (state.NodeSkip == null) ) {
            terminalStates.add( state );
            return;
        }

        if( state.NodeActivate != null ) {
            WalkActivationTree( state.NodeActivate, terminalStates );
        }

        if( state.NodeSkip != null ) {
            WalkActivationTree( state.NodeSkip, terminalStates );
        }

        return;
    }

    public static ArrayList<RockBandPath> BreadthToRockBandPaths( ArrayList<RockBandPlayerState> uniqueStates, long fillDelay ) {

        ArrayList<RockBandPath> paths;

        paths = new ArrayList<RockBandPath>();

        Collections.sort( uniqueStates, new RockBandPlayerStateScoreComparator() );

        for( RockBandPlayerState terminalState : uniqueStates ) {
            RockBandPath    path;

            path = Convert.ToRockBandPath( terminalState, fillDelay );
            paths.add( path );
        }

        return paths;
    }

    public static RockBandPath ToRockBandPath( RockBandPlayerState rbps, long fillDelay ) {

        RockBandPath    path;

        path = new RockBandPath();

        path.Path = rbps.CurrentPath;
        path.NoteStreak = rbps.NoteStreak;
        path.Score = rbps.Score;
        path.FillDelay = fillDelay;

        return path;
    }

    public static DrumChart ToDrumChart( RockBandChartDifficulty rbd, Sequence s ) {

        DrumChart dc;
        Track       tempoTrack;
        Track       beatTrack;
        Track       guitarTrack;
        Track       drumTrack;
        boolean     br;

        dc = new DrumChart();

        dc.setResolution( s.getResolution() );
        dc.setMicroseconds( s.getMicrosecondLength() );

//        DumpTrack( s.getTracks()[3] );

        tempoTrack = s.getTracks()[RockBandMidi.TrackTempo];
        if( tempoTrack == null ) {
            return null;
        }

        br = LoadTempoTrack( dc, tempoTrack );
        if( br == false ) {
            return null;
        }

        dc.setSongTitle( RockBandMidi.GetTrackName( tempoTrack ) );

        // We always need to load the BEAT track for OD duration calculations
        beatTrack = RockBandMidi.GetTrack( s, RockBandMidi.TrackNameBeat );
//        if( beatTrack == null ) {
//        beatTrack = s.getTracks()[RockBandMidi.TrackBeat];
//        }

        if( beatTrack == null ) {
            System.out.format( "Warning: Couldn't find \"BEAT\" track.\n" );
            return null;
        }
        else {
            br = LoadBeatTrack( dc, beatTrack );
            if( br == false ) {
                return null;
            }
        }

        // We need to search all the tracks to find the one for our chosen instrument
        // I've seen this named as PART DRUM and PART DRUMS...so gonna stop looking by track name
        drumTrack = RockBandMidi.GetTrack( s, RockBandMidi.TrackNameDrums );
        if( drumTrack == null ) {
            System.out.format( "Warning: Couldn't find \"PART DRUMS\" track.\n" );
            return null;
        }

        // Now parse the entire track and pull out the notes for our chosen difficulty
        br = LoadDrumTrack( dc, drumTrack, rbd );
        if( br == false ) {
            return null;
        }

        // We need to parse the guitar track to figure out if there's a big rock ending
        guitarTrack = RockBandMidi.GetTrack( s, RockBandMidi.TrackNameGuitar );
        if( guitarTrack == null ) {
            System.out.format( "Warning: Couldn't find \"PART GUITAR\" track.\n" );
            return null;
        }

        SortedMap<Long, Long>       fillPhrases;
        Set<Map.Entry<Long, Long>>  fillPhrasesSet;

        TickRange   bigRockEnding;

 //       DumpTrack( guitarTrack );
        bigRockEnding = GetBigRockEnding( guitarTrack );

        // Set up the fill data we need to track throughout the song
        fillPhrases = dc.getFillPhrases();
        fillPhrasesSet = fillPhrases.entrySet();

        if( bigRockEnding != null ) {
            Long    fillEnd;

            dc.setBigRockEnding( bigRockEnding );
            fillEnd = dc.getFillPhrases().get( bigRockEnding.Start );
            assert( fillEnd != null );
            assert( fillEnd == bigRockEnding.Start );
            dc.getFillPhrases().remove( bigRockEnding.Start );

            MarkBigRockEndingChords( dc.getBigRockEnding(), dc.getChords() );
        }

        // Mark each chord in an overdrive phrase
        if( dc.getBigRockEnding() != null ) {
        }

        // Mark each chord in an solo phrase
        MarkSoloChords( dc.getSoloPhrases(), dc.getChords() );

        // Figure out what the last chord of each solo phrase is
        MarkLastSoloChords( dc.getSoloPhrases(), dc.getChords() );

        // Mark each chord in an overdrive phrase
        MarkOverdriveChords( dc.getOverdrivePhrases(), dc.getChords() );

        // Figure out what the last chord of each overdrive phrase is
        MarkLastOverdriveChords( dc.getOverdrivePhrases(), dc.getChords() );

        // Also mark the last chord of each overdrive phrase when overlapped by fills
        MarkSecondaryLastOverdriveChords( dc.getFillPhrases(), dc.getChords() );

        // Also mark the last chord of each overdrive phrase when overlapped by fills
        MarkPotentialSqueezes( dc.getFillPhrases(), dc.getChords() );

        // Make a shallow copy of the chord ArrayList from the drum chart,
        // and then insert new chords representing fill activation points so that we can simulate properly
        dc.ChordsAndActivations = new ArrayList<Chord>( dc.getChords() );
        for( Map.Entry<Long, Long> fp : fillPhrasesSet ) {

            Chord   c;

            c = new Chord( fp.getValue() );
            c.IsActivation = true;
            dc.ChordsAndActivations.add( c );
        }

        Collections.sort( dc.ChordsAndActivations );

        return dc;
    }
/*
    private static Track GetTrackByIndex( Sequence s, int nIndex ) {

        Track[] tracks;

        tracks = s.getTracks();
        return tracks[nIndex];
    }
 */

    // It seems like fill sections are demarcated by five simultaneous drum fill notes.
    // Currently, I only look at DrumsFillRed since i don't see a reason to check the other fill notes.
    private static int FilterNote( RockBandChartDifficulty rbd, byte rawNote ) {

        int nNote;

        nNote = (int)rawNote;

        if( rbd == RockBandChartDifficulty.Easy ) {
            if( (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasyRed) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasyYellow) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasyBlue) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasyGreen) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasyOrange) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsEasySoloMarker)
                ) {
                return nNote;
            }
        }

        if( rbd == RockBandChartDifficulty.Medium ) {
            if( (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumRed) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumYellow) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumBlue) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumGreen) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumOrange) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsMediumSoloMarker)
                ) {
                return nNote;
            }
        }

        if( rbd == RockBandChartDifficulty.Hard ) {
            if( (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardRed) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardYellow) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardBlue) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardGreen) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardOrange) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsHardSoloMarker)
                ) {
                return nNote;
            }
        }

        if( rbd == RockBandChartDifficulty.Expert ) {
            if( (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertRed) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertYellow) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertBlue) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertGreen) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertOrange) ||
                    (nNote == com.rkuo.RockBand.RockBandMidi.DrumsExpertSoloMarker)
                ) {
                return nNote;
            }
        }

        if( nNote == com.rkuo.RockBand.RockBandMidi.DrumsFillRed ) {
//              (nNote == com.rkuo.RockBand.RockBandMidi.DrumsFillYellow) ||
//                (nNote == com.rkuo.RockBand.RockBandMidi.DrumsFillBlue) ||
//                (nNote == com.rkuo.RockBand.RockBandMidi.DrumsFillGreen) ||
//                (nNote == com.rkuo.RockBand.RockBandMidi.DrumsFillOrange) ||
            return nNote;
        }

        if( nNote == com.rkuo.RockBand.RockBandMidi.OverdriveMarker ) {
            return nNote;
        }

        return 0;
    }

    private static boolean LoadTempoTrack( DrumChart dc, Track tempoTrack ) {

        for( int x=0; x < tempoTrack.size(); x++ ) {
            MidiEvent   me;
            MidiMessage mm;
            long        currentTick;
            byte[]      midiMessage;
            short       statusMessage;

            me = tempoTrack.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            midiMessage = mm.getMessage();

            statusMessage = (short) (midiMessage[0] & 0xf0);

            if( statusMessage == 0xf0 ) {
                int     messageCode;
                int     messageLength;

//                System.out.format( "T%d: ", currentTick );
//                System.out.format( "SYSEX - " );

                messageCode = (int)midiMessage[1];
                if( messageCode == 0x51 ) {
//                    System.out.format( "Set Tempo" );

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

                        tempo = ToInt( tempBytes );

                        dc.AddTempo( currentTick, tempo );
//                        bpm = 60000000 / tempo;
//                        System.out.format( " to %d us per 1/4 note (%d bpm)", tempo, bpm );
                    }
                }
            }
        }

        return true;
    }
/*
    private static boolean GenerateBeatTrack( DrumChart dc, long totalTicks ) {

            Chord   lastChord;

            lastChord = null;

            for( int x=0; x < beatTrack.size(); x++ ) {
                MidiEvent   me;
                MidiMessage mm;
                long        currentTick;
                byte[]      rawMessage;
                short       statusMessage;
//            String      sMessage;

                me = beatTrack.get( x );
                currentTick = me.getTick();
                mm = me.getMessage();
                rawMessage = mm.getMessage();

                statusMessage = (short) (rawMessage[0] & 0xf0);

                if( statusMessage == RockBandMidi.NoteOff ) {
                    // do nothing for now
                }

                if( statusMessage == RockBandMidi.NoteOn ) {

                    Note    n;

                    int nNote;
                    int nVelocity;

                    // If this note isn't interesting to us, continue the loop
//                nNote = FilterNote( rawMessage[1] );
//                if( nNote == 0 ) {
//                    continue;
//                }

                    nNote = (int)rawMessage[1];
                    n = new Note( nNote );

                    nVelocity = (int)rawMessage[2];
                    if( nVelocity > 0 ) {

//                    System.out.format( "BEAT %d, %d, %d, %d\n", currentTick, statusMessage, nNote, nVelocity );

                        if( (lastChord != null) && (lastChord.getTick() == currentTick) ) {
                            lastChord.AddNote( n );
                        }
                        else {
                            Chord   c;

                            c = new Chord( currentTick );
                            c.AddNote( n );
                            dc.AddBeat( c );
                            lastChord = c;
                        }

//                    RockBandMidi.PrintMidiMessage( currentTick, rawMessage );
                    }
                }
            }

            return true;
        }
  */
    private static boolean LoadBeatTrack( DrumChart dc, Track beatTrack ) {

        Chord   lastChord;

        lastChord = null;

        for( int x=0; x < beatTrack.size(); x++ ) {
            MidiEvent   me;
            MidiMessage mm;
            long        currentTick;
            byte[]      rawMessage;
            short       statusMessage;
//            String      sMessage;

            me = beatTrack.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            rawMessage = mm.getMessage();

            statusMessage = (short) (rawMessage[0] & 0xf0);

            if( statusMessage == RockBandMidi.NoteOff ) {
                // do nothing for now
            }

            if( statusMessage == RockBandMidi.NoteOn ) {

                Note    n;

                int nNote;
                int nVelocity;

                // If this note isn't interesting to us, continue the loop
//                nNote = FilterNote( rawMessage[1] );
//                if( nNote == 0 ) {
//                    continue;
//                }

                nNote = (int)rawMessage[1];
                n = new Note( nNote );

                nVelocity = (int)rawMessage[2];
                if( nVelocity > 0 ) {

//                    System.out.format( "BEAT %d, %d, %d, %d\n", currentTick, statusMessage, nNote, nVelocity );

                    if( (lastChord != null) && (lastChord.getTick() == currentTick) ) {
                        lastChord.AddNote( n );
                    }
                    else {
                        Chord   c;

                        c = new Chord( currentTick );
                        c.AddNote( n );
                        dc.AddBeat( c );
                        lastChord = c;
                    }

//                    RockBandMidi.PrintMidiMessage( currentTick, rawMessage );
                }
            }
        }

        return true;
    }

    private static boolean LoadDrumTrack( DrumChart dc, Track drumTrack, RockBandChartDifficulty rbd ) {

        Chord   lastChord;
        long    overdriveStartTick;
        long    fillStartTick;
        long    soloStartTick;

        lastChord = null;
        overdriveStartTick = 0;
        fillStartTick = 0;
        soloStartTick = 0;

        for( int x=0; x < drumTrack.size(); x++ ) {
            MidiEvent   me;
            MidiMessage mm;
            long        currentTick;
            byte[]      rawMessage;
            short       statusMessage;
            String      sMessage;

            me = drumTrack.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            rawMessage = mm.getMessage();

            statusMessage = (short) (rawMessage[0] & 0xf0);

//            if( currentTick == 115080 ) {
//                String dummy = "hello";
//            }

            if( (statusMessage == RockBandMidi.NoteOn) || (statusMessage == RockBandMidi.NoteOff) ) {

                Note    n;

                int nNote;
                int nVelocity;

//                nNote = (int)rawMessage[1];
//                nVelocity = (int)rawMessage[2];
//                System.out.printf( "RAW %d, %d, %d\n", currentTick, nNote, nVelocity );

                // If this note isn't interesting to us, continue the loop
                nNote = FilterNote( rbd, rawMessage[1] );
                if( nNote == 0 ) {
                    continue;
                }

                // There are two ways of switching off a note and apparently both ways can be used
                // in these mid files.  We redirect the NoteOff message to the zero velocity method.
                nVelocity = (int)rawMessage[2];
                if( statusMessage == RockBandMidi.NoteOff ) {
                    nVelocity = 0;
                }

                n = new Note( nNote );
                if( n.isOverdriveMarker() == true ) {
                    if( nVelocity > 0 ) {
                        assert( overdriveStartTick == 0 );
                        overdriveStartTick = currentTick;
                    }
                    else {
                        assert( overdriveStartTick > 0 );
                        dc.AddOverdrivePhrase( overdriveStartTick, currentTick );
                        overdriveStartTick = 0;
                    }

//                  PrintMidiMessage( currentTick, rawMessage );
                    continue;
                }
                else if( n.isFillMarker() == true ) {
                    if( nVelocity > 0 ) {
                        assert( fillStartTick == 0 );
                        fillStartTick = currentTick;
                    }
                    else {
                        assert( fillStartTick > 0 );
                        dc.AddFillPhrase( fillStartTick, currentTick );
                        fillStartTick = 0;
                    }

//                    RockBandMidi.PrintMidiMessage( currentTick, rawMessage );
                    continue;
                }
                else if( n.isSoloMarker(rbd) == true ) {
                    if( nVelocity > 0 ) {
                        assert( soloStartTick == 0 );
                        soloStartTick = currentTick;
                    }
                    else {
                        assert( soloStartTick > 0 );
                        dc.AddSoloPhrase( soloStartTick, currentTick );
                        soloStartTick = 0;
                    }

//                    RockBandMidi.PrintMidiMessage( currentTick, rawMessage );
                    continue;
                }
                else {
                    if( nVelocity > 0 ) {

                        if( (lastChord != null) && (lastChord.getTick() == currentTick) ) {
                            lastChord.AddNote( n );
                        }
                        else {
                            Chord   c;

                            c = new Chord( currentTick );
                            c.AddNote( n );
                            dc.AddChord( c );
                            lastChord = c;
                        }

//                      PrintMidiMessage( currentTick, rawMessage );
                    }
                }
            }
            else if( statusMessage == 0xf0 ) {
//              PrintMidiMessage( currentTick, rawMessage );
            }
        }

        return true;
    }


    private static TickRange GetBigRockEnding( Track guitarTrack ) {

        TickRange   bigRockEnding;

        long    breStartTick, breEndTick;

        bigRockEnding = null;
        breStartTick = 0;
        breEndTick = 0;

        for( int x=0; x < guitarTrack.size(); x++ ) {

            MidiEvent   me;
            MidiMessage mm;
            long        currentTick;
            byte[]      rawMessage;
            short       statusMessage;

            me = guitarTrack.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            rawMessage = mm.getMessage();

            statusMessage = (short) (rawMessage[0] & 0xf0);

            if( statusMessage == 0x90 ) {

                Note    n;

                int nNote;
                int nVelocity;

                nNote = (int)rawMessage[1];
                n = new Note( nNote );

                nVelocity = (int)rawMessage[2];

                if( n.isFillMarker() == true ) {
                    if( nVelocity > 0 ) {
                        assert( breStartTick == 0 );
                        breStartTick = currentTick;
                    }
                    else {
                        assert( breStartTick > 0 );
                        assert( breEndTick == 0 );
                        breEndTick = currentTick;
                    }
                }
            }
        }

        if( (breStartTick > 0) && (breEndTick > 0) ) {
            bigRockEnding = new TickRange();
            bigRockEnding.Start = breStartTick;
            bigRockEnding.End = breEndTick;
        }

        return bigRockEnding;
    }

    private static void DumpTrack( Track track ) {

        for( int x=0; x < track.size(); x++ ) {
            MidiEvent   me;
            MidiMessage mm;
            long        currentTick;
            byte[]      rawMessage;

            me = track.get( x );
            currentTick = me.getTick();
            mm = me.getMessage();
            rawMessage = mm.getMessage();

            RockBandMidi.PrintMidiMessage( currentTick, rawMessage );
        }

        return;
    }


    private static void MarkBigRockEndingChords( TickRange bigRockEnding, ArrayList<Chord> chords ) {

        for( Chord c : chords ) {
            if( c.getTick() >= bigRockEnding.Start ) {
                if( c.getTick() < bigRockEnding.End ) {
                    c.BigRockEnding = true;
                }
                else {
                    c.BigRockEndingActivation = true;
                }
            }
        }

        return;
    }

    private static void MarkSoloChords( SortedMap<Long, Long> soloPhrases, ArrayList<Chord> chords ) {

        int soloIndex;

        Set<Map.Entry<Long, Long>> soloPhrasesSet;

        soloIndex = 0;
        soloPhrasesSet = soloPhrases.entrySet();

        for( Map.Entry<Long, Long> sdp : soloPhrasesSet ) {

            soloIndex++;

            for( Chord c : chords ) {
                if( c.getTick() >= sdp.getKey() ) {
                    if( c.getTick() < sdp.getValue() ) {
                        c.SoloPhrase = soloIndex;
                    }
                }
            }
        }

        return;
    }

    private static void MarkLastSoloChords( SortedMap<Long, Long> soloPhrases, ArrayList<Chord> chords ) {

        Set<Map.Entry<Long, Long>>  soloPhrasesSet;

        soloPhrasesSet = soloPhrases.entrySet();

        if( chords.size() == 0 ) {
            return;
        }

        // Notes are inclusive at the start of the range, but not at the end.
        // If the OD range ends at 1500 and a chord comes at 1500, the chord is not part of the OD phrase
        for( Map.Entry<Long, Long> sdp : soloPhrasesSet ) {
            Chord lastChord;

            lastChord = null;
            for( Chord c : chords ) {
                if( lastChord != null ) {
                    if( lastChord.getTick() >= sdp.getKey() ) {
                        if( lastChord.getTick() < sdp.getValue() ) {
                            if( c.getTick() >= sdp.getValue() ) {
                                lastChord.LastNoteInSoloPhrase = true;
                                break;
                            }
                        }
                    }
                }

                lastChord = c;
            }

            if( lastChord == null ) {
                break;
            }

            // Gotta check the last chord too...this'll never happen, but still...
            if( lastChord.LastNoteInSoloPhrase == false ) {
                Chord   c;

                c = chords.get( chords.size() - 1 );
                if( c.getTick() >= sdp.getKey() ) {
                    if( c.getTick() <= sdp.getValue() ) {
                        c.LastNoteInSoloPhrase = true;
                    }
                }
            }
        }

        return;
    }

    private static void MarkOverdriveChords( SortedMap<Long, Long> overdrivePhrases, ArrayList<Chord> chords ) {

        int overdriveIndex;

        Set<Map.Entry<Long, Long>> overdrivePhrasesSet;

        overdriveIndex = 0;
        overdrivePhrasesSet = overdrivePhrases.entrySet();

        for( Map.Entry<Long, Long> odp : overdrivePhrasesSet ) {

            overdriveIndex++;

            for( Chord c : chords ) {
                if( c.getTick() >= odp.getKey() ) {
                    if( c.getTick() < odp.getValue() ) {
                        c.OverdrivePhrase = overdriveIndex;
                    }
                }
            }
        }

        return;
    }

    private static void MarkLastOverdriveChords( SortedMap<Long, Long> overdrivePhrases, ArrayList<Chord> chords ) {

        Set<Map.Entry<Long, Long>>  overdrivePhrasesSet;

        overdrivePhrasesSet = overdrivePhrases.entrySet();

        if( chords.size() == 0 ) {
            return;
        }

        // Notes are inclusive at the start of the range, but not at the end.
        // If the OD range ends at 1500 and a chord comes at 1500, the chord is not part of the OD phrase
        for( Map.Entry<Long, Long> odp : overdrivePhrasesSet ) {
            Chord lastChord;

            lastChord = null;
            for( Chord c : chords ) {
                if( lastChord != null ) {
                    if( lastChord.getTick() >= odp.getKey() ) {
                        if( lastChord.getTick() < odp.getValue() ) {
                            if( c.getTick() >= odp.getValue() ) {
                                lastChord.LastNoteInOverdrivePhrase = true;
                                break;
                            }
                        }
                    }
                }

                lastChord = c;
            }

            if( lastChord == null ) {
                break;
            }
            
            // Gotta check the last chord too...this'll never happen, but still...
            if( lastChord.LastNoteInOverdrivePhrase == false ) {
                Chord   c;

                c = chords.get( chords.size() - 1 );
                if( c.getTick() >= odp.getKey() ) {
                    if( c.getTick() <= odp.getValue() ) {
                        c.LastNoteInOverdrivePhrase = true;
                    }
                }
            }
        }

        return;
    }

    private static void MarkPotentialSqueezes( SortedMap<Long, Long> fillPhrases, ArrayList<Chord> chords ) {

        Set<Map.Entry<Long, Long>>  fillPhrasesSet;

        fillPhrasesSet = fillPhrases.entrySet();


            // This looping is inefficient...come back and clean this up later
            for( Map.Entry<Long, Long> fp : fillPhrasesSet ) {
                ArrayList<Chord>    squeezes;
                Chord               squeeze;

                squeezes = new ArrayList<Chord>();

                for( Chord c : chords ) {
                    if( Math.abs( c.getTick() - fp.getValue() ) <= 10 ) {
                        squeezes.add( c );
                    }
                }

                if( squeezes.size() == 0 ) {
                    continue;
                }

                Collections.sort( squeezes );

                squeeze = squeezes.get( squeezes.size() - 1 );
                squeeze.PotentialSqueeze = true;
            }

        return;
    }

    private static void MarkSecondaryLastOverdriveChords( SortedMap<Long, Long> fillPhrases, ArrayList<Chord> chords ) {

        Chord                       lastChord;
        Set<Map.Entry<Long, Long>>  fillPhrasesSet;

        fillPhrasesSet = fillPhrases.entrySet();

        lastChord = null;
        for( Chord c : chords ) {

            if( lastChord == null ) {
                lastChord = c;
                continue;
            }

            if( c.OverdrivePhrase == 0 ) {
                lastChord = c;
                continue;
            }

            // This looping is inefficient...come back and clean this up later
            for( Map.Entry<Long, Long> fp : fillPhrasesSet ) {
                if( c.getTick() >= fp.getKey() ) {
                    if( c.getTick() <= fp.getValue() ) {
                        if( lastChord.getTick() < fp.getKey() ) {
                            if( lastChord.OverdrivePhrase == c.OverdrivePhrase ) {
                                lastChord.SecondaryLastNoteInOverdrivePhrase = true;
                            }
                        }
                    }
                }
            }

            lastChord = c;
        }

        return;
    }

    public static int ToShort( byte[] b ) {
        return ToShort( b, 0 );
    }

    public static short ToShort( byte[] b, int offset ) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }

        return value;
    }

    /**
     * Convert the byte array to an int.
     *
     * @param b The byte array
     * @return The integer
     */
    public static int ToInt( byte[] b ) {
        return ToInt( b, 0 );
    }

    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @param b The byte array
     * @param offset The array offset
     * @return The integer
     */
    public static int ToInt( byte[] b, int offset ) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }

        return value;
    }
}
