package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.Primitives.Chord;
import com.rkuo.RockBand.Primitives.Note;
import com.rkuo.RockBand.Simulators.DrumsSimulatorParameters;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jan 2, 2009
 * Time: 5:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPrint {

    public static void PrintDrumChart( DrumChart dc ) {

        ArrayList<Chord> chords;

        SortedMap<Long, Long> overdrivePhrases;
        SortedMap<Long, Long> fillPhrases;

        chords = dc.getChords();
        overdrivePhrases = dc.getOverdrivePhrases();
        fillPhrases = dc.getFillPhrases();

        System.out.format( "===== Chords: %d =====\n", chords.size() );
        for( Chord c : chords ) {
            ArrayList<Note> notes;
            boolean         firstNote;

            firstNote = true;

            System.out.format( "T%d: ", c.getTick() );

            notes = c.getNotes();
            for( Note n : notes ) {

                if( firstNote == true ) {
                    firstNote = false;
                }
                else {
                    System.out.format( ", " );
                }

                System.out.format( "%s", RockBandMidi.toDrumColorString(n.getNumber()) );
            }

            if( c.OverdrivePhrase > 0 ) {
                System.out.format(" - OD%d", c.OverdrivePhrase);
            }

            System.out.format( "\n");
        }

        System.out.format( "\n" );

        // Print overdrive phrases
        Set<Map.Entry<Long, Long>> overdrivePhrasesSet;

        System.out.format( "===== Overdrive Phrases: %d =====\n", overdrivePhrases.size() );

        overdrivePhrasesSet = overdrivePhrases.entrySet();
        for( Map.Entry<Long, Long> odp : overdrivePhrasesSet ) {
            System.out.format( "T%d -> T%d\n", odp.getKey(), odp.getValue() );
        }

        // Print fill phrases
        Set<Map.Entry<Long, Long>> fillPhrasesSet;

        System.out.format( "===== Fill Phrases: %d =====\n", fillPhrases.size() );

        fillPhrasesSet = fillPhrases.entrySet();
        for( Map.Entry<Long, Long> fp : fillPhrasesSet ) {
            System.out.format( "T%d -> T%d\n", fp.getKey(), fp.getValue() );
        }

        return;
    }

    public static void PrintDrumsSimulatorParameters( DrumsSimulatorParameters dsp ) {

        String  fillDelayDescription;

        fillDelayDescription = "";

        System.out.format( "===== Drums Simulator Parameters =====\n" );

        if( dsp.FillDelay == RockBandConstants.FillDelayRB1 ) {
            fillDelayDescription = "Rock Band 1";
        }
        else if( dsp.FillDelay == RockBandConstants.FillDelayRB2Expert ) {
            fillDelayDescription = "Rock Band 2, Expert Difficulty";
        }
        else {
            // do nothing
        }

        if( fillDelayDescription.length() == 0 ) {
            System.out.format( "Fill delay = %d.\n", dsp.FillDelay );
        }
        else {
            System.out.format( "Fill delay = %d. (We appear to be using the delay for %s)\n", dsp.FillDelay, fillDelayDescription);
        }

        if( dsp.OverhitOverdrivePhrases == true ) {
            System.out.format( "OverhitOverdrivePhrases = TRUE : The simulator will test paths where overdrive phrases are skipped by overhitting.\n");
        }
        else {
            System.out.format( "OverhitOverdrivePhrases = FALSE : The simulator will NOT test paths where overdrive phrases are skipped by overhitting.\n");
        }
        
        if( dsp.FrontEndSqueezes == true ) {
            System.out.format( "FrontEndSqueezes = TRUE : Front end squeezes are being accounted for when calculating potential scores.\n");
        }
        else {
            System.out.format( "FrontEndSqueezes = FALSE : Front end squeezes are NOT accounted for when calculating potential scores.\n");
        }

        if( dsp.DetectGlitches == true ) {
            System.out.format( "DetectGlitches = TRUE : Will note any potential chord glitches.\n");
        }
        else {
            System.out.format( "DetectGlitches = FALSE : Will not detect any potential chord glitches.\n");
        }

        System.out.format( "========== Drums Simulator Parameters ENDS =====\n\n" );
        return;
    }

    public static void PrintDrumsBaselineData( DrumsBaselineData dbd ) {

        String  sWarning;
        String  sBRE;

        sBRE = "FALSE";

        System.out.format( "===== Baseline data STARTS =====\n" );

        System.out.format( "Number of chords: %d\n", dbd.Chords );
        System.out.format( "Number of notes: %d\n", dbd.Notes );
        System.out.format( "Number of solos: %d\n", dbd.Solos );
        System.out.format( "Number of fills: %d\n", dbd.Fills );
        System.out.format( "Number of overdrive phrases: %d\n", dbd.OverdrivePhrases );

        if( dbd.BigRockEnding == true ) {
            sBRE = "TRUE";
        }

        System.out.format( "Has Big Rock Ending: %s\n", sBRE );
        System.out.format( "Baseline score without multiplier: %d\n", dbd.UnmultipliedScore );
        System.out.format( "Baseline score with multiplier: %d\n", dbd.MultipliedScore );

        sWarning = "FALSE";
        if( dbd.MaximumMultiplierNotReachedWarning == true ) {
            sWarning = "TRUE";
        }

        System.out.format( "Glitched chords: %d glitched chords found. (Using %d tick threshold)\n", dbd.GlitchedChords.size(), RockBandConstants.PotentialGlitchTicks );

        for( Chord c : dbd.GlitchedChords ) {
            PrintChord( c );
        }

        System.out.format( "Maximum multiplier not reached warning: %s\n", sWarning );

//        System.out.format( "1 star: %d\n", multiplierCounter );
//        System.out.format( "2 star: %d\n", multiplierCounter );
//        System.out.format( "3 star: %d\n", multiplierCounter );
        System.out.format( "4 star: %d\n", dbd.StarCutoffFour );
        System.out.format( "5 stars: %d\n", dbd.StarCutoffFive );
        System.out.format( "Gold stars: %d\n", dbd.StarCutoffGold );
        System.out.format( "Gold stars (pre-patch): %d\n", dbd.StarCutoffGoldOld );

        System.out.format( "========== Baseline data ENDS =====\n\n" );
        return;
    }

    public static void PrintPaths( ArrayList<RockBandPath> paths ) {

        System.out.format( "===== Paths =====\n");

        for( RockBandPath p : paths ) {
            PrintPath( p );
        }

        System.out.format( "===== %d paths generated. ====\n", paths.size() );
        return;
    }

    private static void PrintPath( RockBandPath path ) {

        int     skipCounter;
        boolean firstOutput;

        System.out.format( "Path: [" );

        skipCounter = 0;
        firstOutput = true;
        for( PathNode p : path.Path ) {
            if( p.Activate == false ) {
                skipCounter++;
            }
            else {
                if( firstOutput == true ) {
                    System.out.format( "%d/%s", skipCounter, FormatChordShort(p.Squeeze) );
                    firstOutput = false;
                }
                else {
                    System.out.format( ",%d/%s", skipCounter, FormatChordShort(p.Squeeze) );
                }

                skipCounter = 0;
            }
        }

        // The star notation denotes skips that never activate because we reached the end of the song
        if( skipCounter > 0 ) {
            if( firstOutput == true ) {
                System.out.format( "%d*", skipCounter );
            }
            else {
                System.out.format( ",%d*", skipCounter );
            }
        }

        System.out.format( "], Score: %d, Note Streak = %d\n", path.Score, path.NoteStreak );
        return;
    }

    private static String FormatChordShort( Chord c ) {

        String  chordString;

        if( c == null ) {
            return "NULL";
        }

        chordString = "";

        for( Note n : c.getNotes() ) {
            chordString += RockBandMidi.toShortDrumColorString( n.getNumber() );
        }

        return chordString;
    }

    private static String FormatChord( Chord c ) {

        boolean firstNote;
        String  chordString;

        firstNote = true;
        chordString = "";

        for( Note n : c.getNotes() ) {
            if( firstNote == true ) {
                firstNote = false;
            }
            else {
                chordString += String.format( ", " );
            }

            chordString += String.format( "%s", RockBandMidi.toDrumColorString(n.getNumber()) );
        }

        return chordString;
    }

    private static void PrintChord( Chord c ) {

        System.out.format( "T%d: ", c.getTick() );

        System.out.format( "%s", FormatChord(c) );

        if( c.OverdrivePhrase > 0 ) {
            System.out.format(" - OD%d", c.OverdrivePhrase);
        }

        System.out.format( "\n" );
        return;
    }

    public static void PrintChord( Chord c, long currentScore ) {

        System.out.format( "T%d: ", c.getTick() );

        System.out.format( "%s", FormatChord(c) );

        if( c.OverdrivePhrase > 0 ) {
            System.out.format(" - OD%d", c.OverdrivePhrase);
        }

        System.out.format( " - %d\n", currentScore );
        return;
    }
}
