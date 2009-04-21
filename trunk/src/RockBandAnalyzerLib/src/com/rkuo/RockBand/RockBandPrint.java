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
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jan 2, 2009
 * Time: 5:13:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPrint {

    public static void PrintDrumChart( PrintWriter psOut, DrumChart dc ) {

        ArrayList<Chord> chords;

        SortedMap<Long, Long> overdrivePhrases;
        SortedMap<Long, Long> fillPhrases;

        chords = dc.getChords();
        overdrivePhrases = dc.getOverdrivePhrases();
        fillPhrases = dc.getFillPhrases();

        psOut.format( "===== Chords: %d =====\n", chords.size() );
        for( Chord c : chords ) {
            ArrayList<Note> notes;
            boolean         firstNote;

            firstNote = true;

            psOut.format( "T%d: ", c.getTick() );

            notes = c.getNotes();
            for( Note n : notes ) {

                if( firstNote == true ) {
                    firstNote = false;
                }
                else {
                    psOut.format( ", " );
                }

                psOut.format( "%s", RockBandMidi.toDrumColorString(n.getNumber()) );
            }

            if( c.OverdrivePhrase > 0 ) {
                psOut.format(" - OD%d", c.OverdrivePhrase);
            }

            psOut.format( "\n");
        }

        psOut.format( "\n" );

        // Print overdrive phrases
        Set<Map.Entry<Long, Long>> overdrivePhrasesSet;

        psOut.format( "===== Overdrive Phrases: %d =====\n", overdrivePhrases.size() );

        overdrivePhrasesSet = overdrivePhrases.entrySet();
        for( Map.Entry<Long, Long> odp : overdrivePhrasesSet ) {
            psOut.format( "T%d -> T%d\n", odp.getKey(), odp.getValue() );
        }

        // Print fill phrases
        Set<Map.Entry<Long, Long>> fillPhrasesSet;

        psOut.format( "===== Fill Phrases: %d =====\n", fillPhrases.size() );

        fillPhrasesSet = fillPhrases.entrySet();
        for( Map.Entry<Long, Long> fp : fillPhrasesSet ) {
            psOut.format( "T%d -> T%d\n", fp.getKey(), fp.getValue() );
        }

        return;
    }

    public static void PrintAnalyzerParameters( PrintWriter psOut, RockBandAnalyzerParams rbap ) {

        psOut.format( "===== Analyzer Parameters begins =====\n" );
        if( rbap.Difficulty == RockBandDifficulty.Easy ) {
            psOut.format( "RockBandDifficulty: Easy\n");
        }
        else if( rbap.Difficulty == RockBandDifficulty.Medium ) {
            psOut.format( "RockBandDifficulty: Medium\n");
        }
        else if( rbap.Difficulty == RockBandDifficulty.Hard ) {
            psOut.format( "RockBandDifficulty: Hard\n");
        }
        else if( rbap.Difficulty == RockBandDifficulty.Expert ) {
            psOut.format( "RockBandDifficulty: Expert\n");
        }
        else {
            psOut.format( "RockBandDifficulty: Unknown\n");
        }

        if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Predefined ) {
            psOut.format( "PathingAlgorithm: Predefined\n");
        }
        else if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Optimal ) {
            psOut.format( "PathingAlgorithm: Optimal\n");
        }
        else {
            psOut.format( "PathingAlgorithm: All\n");
        }

        psOut.format( "===== Analyzer Parameters ends =====\n" );
        return;
    }

    public static void PrintDrumsSimulatorParameters( PrintWriter psOut, DrumsSimulatorParameters dsp ) {

        String  fillDelayDescription;

        fillDelayDescription = "";

        psOut.format( "===== Drums Simulator Parameters =====\n" );

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
            psOut.format( "Fill delay = %d.\n", dsp.FillDelay );
        }
        else {
            psOut.format( "Fill delay = %d. (We appear to be using the delay for %s)\n", dsp.FillDelay, fillDelayDescription);
        }

        if( dsp.OverhitOverdrivePhrases == true ) {
            psOut.format( "OverhitOverdrivePhrases = TRUE : The simulator will test paths where overdrive phrases are skipped by overhitting.\n");
        }
        else {
            psOut.format( "OverhitOverdrivePhrases = FALSE : The simulator will NOT test paths where overdrive phrases are skipped by overhitting.\n");
        }
        
        if( dsp.FrontEndSqueezes == true ) {
            psOut.format( "FrontEndSqueezes = TRUE : Front end squeezes are being accounted for when calculating potential scores.\n");
        }
        else {
            psOut.format( "FrontEndSqueezes = FALSE : Front end squeezes are NOT accounted for when calculating potential scores.\n");
        }

        if( dsp.DetectGlitches == true ) {
            psOut.format( "DetectGlitches = TRUE : Will note any potential chord glitches.\n");
        }
        else {
            psOut.format( "DetectGlitches = FALSE : Will not detect any potential chord glitches.\n");
        }

        psOut.format( "========== Drums Simulator Parameters ENDS =====\n\n" );
        return;
    }

    public static void PrintDrumsBaselineData( PrintWriter psOut, DrumsBaselineData dbd ) {

        String  sWarning;
        String  sBRE;

        sBRE = "FALSE";

        psOut.format( "===== Baseline data STARTS =====\n" );

        psOut.format( "Song title (extracted from MIDI stream): %s\n", dbd.SongTitle );

        psOut.format( "Number of chords: %d\n", dbd.Chords );
        psOut.format( "Number of notes: %d\n", dbd.Notes );
        psOut.format( "Number of solos: %d\n", dbd.Solos );
        psOut.format( "Number of fills: %d\n", dbd.Fills );
        psOut.format( "Number of overdrive phrases: %d\n", dbd.OverdrivePhrases );

        if( dbd.BigRockEnding == true ) {
            sBRE = "TRUE";
        }

        psOut.format( "Has Big Rock Ending: %s\n", sBRE );
        psOut.format( "Baseline score without multiplier: %d\n", dbd.UnmultipliedScore );
        psOut.format( "Baseline score with multiplier: %d\n", dbd.MultipliedScore );

        sWarning = "FALSE";
        if( dbd.MaximumMultiplierNotReachedWarning == true ) {
            sWarning = "TRUE";
        }

        psOut.format( "Glitched chords: %d glitched chords found. (Using %d tick threshold)\n", dbd.GlitchedChords.size(), RockBandConstants.PotentialGlitchTicks );

        for( Chord c : dbd.GlitchedChords ) {
            PrintChord( psOut, c );
        }

        psOut.format( "Maximum multiplier not reached warning: %s\n", sWarning );

//        psOut.format( "1 star: %d\n", multiplierCounter );
//        psOut.format( "2 star: %d\n", multiplierCounter );
//        psOut.format( "3 star: %d\n", multiplierCounter );
        psOut.format( "4 star: %d\n", dbd.StarCutoffFour );
        psOut.format( "5 stars: %d\n", dbd.StarCutoffFive );
        psOut.format( "Gold stars: %d\n", dbd.StarCutoffGold );
        psOut.format( "Gold stars (pre-patch): %d\n", dbd.StarCutoffGoldOld );

        psOut.format( "========== Baseline data ENDS =====\n\n" );
        return;
    }

    public static void PrintPaths( PrintWriter psOut, ArrayList<RockBandPath> paths ) {

        psOut.format( "===== Paths =====\n");

        for( RockBandPath p : paths ) {
            PrintPath( psOut, p );
        }

        psOut.format( "===== %d paths generated. ====\n", paths.size() );
        return;
    }

    private static void PrintPath( PrintWriter psOut, RockBandPath path ) {

        int     skipCounter;
        boolean firstOutput;

        psOut.format( "Path: [" );

        skipCounter = 0;
        firstOutput = true;
        for( PathNode p : path.Path ) {
            if( p.Activate == false ) {
                skipCounter++;
            }
            else {
                if( firstOutput == true ) {
                    psOut.format( "%d/%s", skipCounter, FormatChordShort(p.Squeeze) );
                    firstOutput = false;
                }
                else {
                    psOut.format( ",%d/%s", skipCounter, FormatChordShort(p.Squeeze) );
                }

                skipCounter = 0;
            }
        }

        // The star notation denotes skips that never activate because we reached the end of the song
        if( skipCounter > 0 ) {
            if( firstOutput == true ) {
                psOut.format( "%d*", skipCounter );
            }
            else {
                psOut.format( ",%d*", skipCounter );
            }
        }

        psOut.format( "], Score: %d, Note Streak = %d\n", path.Score, path.NoteStreak );
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

    private static void PrintChord( PrintWriter psOut, Chord c ) {

        psOut.format( "T%d: ", c.getTick() );

        psOut.format( "%s", FormatChord(c) );

        if( c.OverdrivePhrase > 0 ) {
            psOut.format(" - OD%d", c.OverdrivePhrase);
        }

        psOut.format( "\n" );
        return;
    }

    public static void PrintChord( PrintWriter psOut, Chord c, long currentScore ) {

        psOut.format( "T%d: ", c.getTick() );

        psOut.format( "%s", FormatChord(c) );

        if( c.OverdrivePhrase > 0 ) {
            psOut.format(" - OD%d", c.OverdrivePhrase);
        }

        psOut.format( " - %d\n", currentScore );
        return;
    }
}
