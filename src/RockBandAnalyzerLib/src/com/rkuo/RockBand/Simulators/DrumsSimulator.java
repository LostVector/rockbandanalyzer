package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.*;
import com.rkuo.RockBand.Primitives.Chord;
import com.rkuo.RockBand.Primitives.Note;
import com.rkuo.RockBand.Primitives.DrumChart;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 12:18:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class DrumsSimulator extends BaseInstrumentSimulator {
    public DrumsSimulator() {
        return;
    }

    public DrumsBaselineData GenerateBaselineData( DrumChart dc ) {

        ArrayList<Chord>        chords;
        Chord                   lastChord;

        int                     noteStreak;
        int                     chordStreak;

        int                     noteStreakWithBRE;
        int                     chordStreakWithBRE;

        DrumsBaselineData       dbd;

        dbd = new DrumsBaselineData();

        noteStreak = 0;
        chordStreak = 0;

        noteStreakWithBRE = 0;
        chordStreakWithBRE = 0;

        lastChord = null;

        dbd.SongTitle = dc.getSongTitle();
        
        chords = dc.getChords();
        for( Chord c : chords ) {

            ArrayList<Note> notes;

//            if( c.BigRockEnding == true ) {
//                continue;
//            }

            notes = c.getNotes();
            for( Note n : notes ) {
                long noteScore;

                noteScore = RockBandConstants.BaseNoteScore * GetMultiplier( noteStreak );

                noteStreakWithBRE++;
                if( c.BigRockEnding == false ) {
                    noteStreak++;
                    dbd.MultipliedScore += noteScore;
                }
            }

            chordStreakWithBRE++;
            if( c.BigRockEnding == false ) {
                chordStreak++;
            }

            // Check for glitches
            if( lastChord != null ) {
                if( c.getTick() - lastChord.getTick() <= RockBandConstants.PotentialGlitchTicks ) {
                    dbd.GlitchedChords.add( lastChord );
                    if( dbd.GlitchedChords.contains(c) == false ) {
                        dbd.GlitchedChords.add(c);
                    }
                }
            }

            lastChord = c;
        }

        dbd.UnmultipliedScore = noteStreak * RockBandConstants.BaseNoteScore;
        dbd.UnmultipliedScoreWithBRENotes = noteStreakWithBRE * RockBandConstants.BaseNoteScore;
        dbd.Notes = noteStreak;
        dbd.Chords = chordStreak;
        dbd.Solos = dc.getSoloPhrases().size();
        dbd.Fills = dc.getFillPhrases().size();
        dbd.OverdrivePhrases = dc.getOverdrivePhrases().size();
        if( dc.getBigRockEnding().Start > 0 ) {
            dbd.BigRockEnding = true;            
        }

        if( GetMultiplier( noteStreak ) != RockBandConstants.MaxMultiplier ) {
            dbd.MaximumMultiplierNotReachedWarning = true;
        }

        double dblUnmultipliedScore;
        double dblUnmultipliedScoreWithBRENotes;

        dblUnmultipliedScore = (double)(dbd.UnmultipliedScore);
        dblUnmultipliedScoreWithBRENotes = (double)(dbd.UnmultipliedScoreWithBRENotes);

        dbd.StarCutoffFour = (long)(dblUnmultipliedScore * RockBandConstants.DrumsStarMultiplierFour);
        dbd.StarCutoffFive = (long)(dblUnmultipliedScore * RockBandConstants.DrumsStarMultiplierFive);
        dbd.StarCutoffGold = (long)(dblUnmultipliedScore * RockBandConstants.DrumsStarMultiplierGold);
        dbd.StarCutoffGoldOld = (long)(dblUnmultipliedScoreWithBRENotes * RockBandConstants.DrumsStarMultiplierGoldOld);

        return dbd;
    }

    public ArrayList<RockBandPath> GenerateScoreFromPath( DrumsSimulatorParameters dsp, DrumChart dc, Integer[] skipNotation ) {

        RockBandPlayerState         state;
        ArrayList<RockBandPath>     paths;

        state = new RockBandPlayerState();

        // Don't allow negative numbers in the skip notation
        if( dc == null ) {
            return null;
        }

        if( skipNotation == null ) {
            return null;
        }

        System.out.format( "===== Pathed score: " );
        for( int x : skipNotation ) {
            System.out.format( "%d ", x );
        }

        System.out.format( "=====\n" );

        for( Integer x : skipNotation ) {
            if( x < 0 ) {
                return null;
            }
        }

        for( Integer x : skipNotation ) {
            state.SuggestedPath.add( x );
        }

        state.SkipCounter = state.SuggestedPath.get( state.SkipIndex );

        SimulateDepth( dsp, dc, 0, state );
        paths = Convert.DepthToRockBandPaths( state );

//      System.out.format( "Pathed score: %d, Note Streak = %d\n", state.Score, state.NoteStreak );
        return paths;
    }

    public ArrayList<RockBandPath> GenerateAllPaths( DrumsSimulatorParameters dsp, DrumChart dc ) {

        RockBandPlayerState         state;
        ArrayList<RockBandPath>     paths;

        state = new RockBandPlayerState();

        // Don't allow negative numbers in the skip notation
        if( dc == null ) {
            return null;
        }

        SimulateDepth( dsp, dc, 0, state );

        paths = Convert.DepthToRockBandPaths( state );
//        PrintActivationPathsDepth( state );

        // Fix this later
        return paths;
    }


    public ArrayList<RockBandPath> GenerateOptimalPaths( DrumsSimulatorParameters dsp, DrumChart dc ) {
        ArrayList<RockBandPlayerState>  returnStates;
        ArrayList<RockBandPath>        optimalPaths;

        System.out.format( "===== Generating optimal paths =====\n" );

        returnStates = SimulateBreadth( dsp, dc );
        optimalPaths = Convert.BreadthToRockBandPaths( returnStates );

        return optimalPaths;
    }
/*
    protected void SimulateDepth( DrumChart dc, int chordStartPosition, RockBandPlayerState rbps ) {

        // iterate over the chart
        for( int x = chordStartPosition; x < dc.ChordsAndActivations.size(); x++ ) {

            ArrayList<Note> notes;
            Chord           c;
            boolean         inFill;

            c = dc.ChordsAndActivations.get(x);

            SimulateTick( c, dc, rbps, idah );

            rbps.Now = c.getTick();

            // Ignore notes that come too soon after an overdrive activation/fill
            if( rbps.OverdriveStartTick != Long.MAX_VALUE ) {
                if( rbps.Now - rbps.OverdriveStartTick <= 10 ) {
                    System.out.format( "T%d: Ignoring notes that come too soon after an activation.\n", rbps.Now );
                    continue;
                }
            }

            // Ignore notes that come too soon after a fill
            if( (rbps.AreFillsActive() == true) && (rbps.LastPotentialActivationTick != Long.MAX_VALUE) ) {
                if( rbps.Now - rbps.LastPotentialActivationTick <= 10 ) {
                    System.out.format("T%d: Ignoring notes that come too soon after a fill.\n", rbps.Now);
                    continue;
                }
            }

            if( c.BigRockEnding == true ) {
                System.out.format( "T%d: In big rock ending, chord skipped\n", rbps.Now );
                continue;
            }

            if( c.BigRockEndingActivation == true ) {
                System.out.format( "T%d: Big rock ending activation, chord skipped\n", rbps.Now );
                continue;
            }

            inFill = false;
            if( rbps.AreFillsActive() == true ) {
                // This looping is inefficient...come back and clean this up later
                for( Map.Entry<Long, Long> fp : dc.getFillPhrases().entrySet() ) {

                    // Fills can't partially appear, so we assume we're not in a fill
                    // if fill delay time intersects the fill
                    if( (rbps.Now >= fp.getKey()) && (rbps.FillsActiveTick < fp.getKey())) {
                        if( rbps.Now <= fp.getValue() ) {
                            inFill = true;
                            break;
                        }
                    }
                }
            }

            if( inFill == true ) {
                // Is this the activation note?
                if( c.IsActivation == false ) {
                    // If not, continue.
                    System.out.format( "T%d: In fill, chord skipped\n", rbps.Now );
                    continue;
                }
                else {
                    idah.HandlePotentialActivation( dc, rbps );
                }
            }

            if( c.IsActivation == true ) {
                idah.HandleNonActivation( rbps );
                continue;
            }

            // Is overdrive over?
            if( rbps.IsOverdriveFinished() == true ) {
                rbps.DeactivateOverdrive();
                System.out.format( "T%d: Overdrive deactivated at %d\n", rbps.Now, rbps.OverdriveFinishedTick );
            }

            // Enumerate each note in this chord and add up the score
            boolean         firstNote;

            firstNote = true;

            System.out.format( "T%d: ", c.getTick() );

            notes = c.getNotes();
            for( Note n : notes ) {
                int     noteScore;

                rbps.NoteStreak++;

                noteScore = _baseNoteScore * GetMultiplier( rbps.NoteStreak );

                if( rbps.Now <= rbps.OverdriveEndTick ) {
                    noteScore *= 2;
                }

                rbps.Score += noteScore;

                if( firstNote == true ) {
                    firstNote = false;
                }
                else {
                    System.out.format( ", " );
                }

                System.out.format( "%s", RockBandMidi.toDrumColorString(n.getNumber()) );
            }

            if( c.OverdrivePhrase > 0 ) {
                System.out.format( " - OD%d", c.OverdrivePhrase );
            }

            if( rbps.OverdriveEndTick != 0 ) {
                if( rbps.OverdriveEndTick - rbps.Now <= 10 ) {
                    System.out.format( " - Probable back end squeeze" );
                }
            }

            System.out.format( " - %d\n", rbps.Score );

            // Add a quarter bar of energy to the overdriveMeter
            // because we hit the last OD chord in a phrase successfully
            // I need to account for the tail end of an OD phrase being overlapped by a fill
            // in this case, the OD meter gain happens early
            // Also, is it possible for an OD phrase to span over a fill?  That would probably be an
            // error condition that I wouldn't know how to handle.

            boolean overdrivePhraseHit;

            overdrivePhraseHit = false;
            if( (rbps.AreFillsActive() == true) && (c.SecondaryLastNoteInOverdrivePhrase == true) ) {
                overdrivePhraseHit = true;
            }

            if( c.LastNoteInOverdrivePhrase == true ) {
                overdrivePhraseHit = true;
            }

            if( overdrivePhraseHit == true ) {

                long    lastOverdriveMeter;

                lastOverdriveMeter = rbps.OverdriveMeter;
                // This value is going to be wrong if we're in the middle of using overdrive.
                // However, I don't want to write code to estimate the value of the bar while it's moving
                // and it doesn't affect score calculations, so I'm leaving it as is.
                rbps.OverdriveMeter += RockBandConstants.OverdriveMeterQuarter;
                if( rbps.OverdriveMeter > RockBandConstants.OverdriveMeterMax ) {
                    // Clamp the OD meter to the max value for overdrive
                    rbps.OverdriveMeter = RockBandConstants.OverdriveMeterMax;
                }

                if( rbps.OverdriveEndTick != 0 ) {
                    // Overdrive is active, the meter will be extended
                    long overdriveBeats;

                    overdriveBeats = RockBandConstants.OverdriveMeterQuarter / RockBandConstants.OverdrivePerBeat;

                    rbps.OverdriveEndTick = dc.GetOverdriveEnd( rbps.OverdriveEndTick, overdriveBeats );
                    System.out.format( "T%d: Overdrive phrase hit under OD, overdriveMeter = %d\n", rbps.Now, rbps.OverdriveMeter );
                }
                else {

                    // If we just got enough overdrive to activate, then determine when to start showing fills.
                    // Why? The game delays a bit because we don't want a fill section to "teleport in".
                    if( (rbps.OverdriveMeter >= RockBandConstants.OverdriveMeterHalf) && (lastOverdriveMeter < RockBandConstants.OverdriveMeterHalf) ) {
                        // Don't know what the tick number should be to block fills from appearing in
                        rbps.FillsActiveTick = GetTickByAdditionalBeats( rbps.Now, _fillDelay45, dc.getBeats() );

                        // Log if there's a potential issue with a fill appearing or disappearing based on note timing
                        for( Map.Entry<Long, Long> fp : dc.getFillPhrases().entrySet() ) {
                            // Fills can't partially appear, so we assume we're not in a fill
                            // if fill delay time intersects the fill
                            if( Math.abs( rbps.FillsActiveTick - fp.getKey() ) <= 10 )  {
                                System.out.format( "T%d: Fill at %d may appear or disappear based on when this OD phrase is hit.\n", rbps.Now, fp.getKey() );
                                break;
                            }
                        }
                    }

                    System.out.format( "T%d: Overdrive phrase hit, overdriveMeter = %d\n", rbps.Now, rbps.OverdriveMeter );
                }
            }
        }

        return;
    }
 */

    protected void SimulateDepth( DrumsSimulatorParameters dsp, DrumChart dc, int chordStartPosition, RockBandPlayerState rbps ) {

        DepthDrumsActivationHandler depthHandler;

        depthHandler = new DepthDrumsActivationHandler();

        // iterate over the chart
        for( int x = chordStartPosition; x < dc.ChordsAndActivations.size(); x++ ) {

            Chord           c;

            c = dc.ChordsAndActivations.get(x);

            SimulateTick( dsp, c, dc, rbps, depthHandler );

            if( depthHandler.NextStates.size() > 0 ) {
                for( RockBandPlayerState rbps2 : depthHandler.NextStates ) {
                    SimulateDepth( dsp, dc, x+1, rbps2 );
                }

                return;
            }
        }

        return;
    }

    protected ArrayList<RockBandPlayerState> SimulateBreadth( DrumsSimulatorParameters dsp, DrumChart dc ) {

        ArrayList<RockBandPlayerState>  states;
        BreadthDrumsActivationHandler   breadthHandler;
//        ArrayList<RockBandPlayerState>  nextStates;
        int                             depth;

        states = new ArrayList<RockBandPlayerState>();
        breadthHandler = new BreadthDrumsActivationHandler();
//        nextStates = new ArrayList<RockBandPlayerState>();

        states.add( new RockBandPlayerState() );
        depth = 0;

        // iterate over the chart
        for( Chord c : dc.ChordsAndActivations ) {

            // On every activation, we have a new level of the tree
            if( c.IsActivation == true ) {
                ArrayList<RockBandPlayerState>  removeStates;

                removeStates = GetNonOptimalPaths( states );
                for( RockBandPlayerState rbps : removeStates ) {
                    states.remove( rbps );
                }

                for( RockBandPlayerState rbps : states ) {
                    rbps.Depth = depth;
                }

                depth++;
/*
                if( removeStates.size() > 0 ) {
                    ArrayList<RockBandPath> paths;
                    System.out.format( "Removing the following %d non-optimal states.\n", removeStates.size() );
                    paths = Convert.BreadthToRockBandPaths( removeStates );
                    RockBandPrint.PrintPaths( paths );
//                    PrintActivationPathsBreadth( removeStates );
                }
 */
//                System.out.format( "Now entering fill depth %d.\n", depth );
            }

//          System.out.format( "T%d: Processing %d state(s)\n", c.getTick(), states.size() );
            for( RockBandPlayerState rbps : states ) {
                SimulateTick( dsp, c, dc, rbps, breadthHandler );
            }

            // This should only happen after the last chord was an activation
            if( breadthHandler.NextStates.size() > 0 ) {
                states.clear();
                states.addAll( breadthHandler.NextStates );
                breadthHandler.NextStates.clear();
            }
        }

        return states;
    }

    private void SimulateTick( DrumsSimulatorParameters dsp, Chord c, DrumChart dc, RockBandPlayerState rbps, IDrumActivationHandler idah ) {

        rbps.Now = c.getTick();

        // Ignore notes that come too soon after an overdrive activation
        // Front end squeezes are handled in the logic for activations
        if( rbps.OverdriveStartTick != Long.MAX_VALUE ) {
            if( rbps.Now - rbps.OverdriveStartTick <= RockBandConstants.FillDeadZoneTicks ) {
//                        System.out.format("T%d: Ignoring notes that come too soon after an activation.\n", rbps.Now);
                return;
            }
        }

        // Ignore notes that come too soon after a fill
        if( (rbps.AreFillsActive() == true) && (rbps.LastPotentialActivationTick != Long.MAX_VALUE) ) {
            if( rbps.Now - rbps.LastPotentialActivationTick <= RockBandConstants.FillDeadZoneTicks ) {
//                        System.out.format("T%d: Ignoring notes that come too soon after a fill.\n", rbps.Now);
                return;
            }
        }

        if( c.BigRockEnding == true ) {
//                    System.out.format( "T%d: In big rock ending, chord skipped\n", rbps.Now );
            return;
        }

        if( c.BigRockEndingActivation == true ) {
//                    System.out.format( "T%d: Big rock ending activation, chord skipped\n", rbps.Now );
            return;
        }

        if( IsInFill( dc, rbps ) >= 0 ) {
            // Is this the activation note? If not, continue.
            if( c.IsActivation == false ) {
//                System.out.format("T%d: In fill, chord skipped\n", rbps.Now);
                return;
            }
            else {
                // Potential activation here...we are branching. Add activate and skip nodes to the next level of depth.
                idah.HandlePotentialActivation( dc, rbps );
                if( dsp.FrontEndSqueezes == true ) {
                    Chord   frontEndSqueeze;

                    frontEndSqueeze = FindFrontEndSqueeze( dc, c.getTick() );
                    if( frontEndSqueeze != null ) {
//                        System.out.format( "Found front end squeeze.\n" );
                        RockBandPlayerState    node;

                        if( rbps.SuggestedPath.size() == 0 ) {
                            node = rbps.NodeActivate;
                        }
                        else {
                            node = rbps;
                        }

                        SimulateChordHit( node, frontEndSqueeze );

                        PathNode    currentNode;

                        currentNode = node.CurrentPath.get( node.CurrentPath.size() - 1 );
                        currentNode.Squeeze = frontEndSqueeze;
                    }
                }
                
                return;
            }
        }

        // Fills are not active, so we won't be branching at this activation point. Just add ourselves
        // back to the next depth level.
        if( c.IsActivation == true ) {
            idah.HandleNonActivation( rbps );
            return;
        }

//        if( rbps.Now == 32160 ) {
//            String dummy = "";
//        }

        // Is overdrive over?
        if( rbps.IsOverdriveFinished() == true ) {
            rbps.DeactivateOverdrive();
//            System.out.format("T%d: Overdrive deactivated at %d\n", rbps.Now, rbps.OverdriveFinishedTick);
        }

        SimulateChordHit( rbps, c );

        // I'm already adding BackEndSqueezeTicks ticks to OverdriveEndTick so this logic is wrong
//        if (rbps.OverdriveEndTick != 0) {
//            if( rbps.OverdriveEndTick - rbps.Now <= RockBandConstants.BackEndSqueezeTicks ) {
//                        System.out.format( "T%d - Probable back end squeeze\n", rbps.Now );
//            }
//        }

//        RockBandPrint.PrintChord( c, rbps.Score );

        if( IsOverdrivePhraseHit( c, rbps ) == true ) {
            SimulateOverdrivePhraseHit( dc, dsp.FillDelay, rbps );
        }

        if( IsSoloPhraseHit( c, rbps ) == true ) {
            SimulateSoloPhraseHit( dc, c, rbps );
        }

        return;
    }

    void SimulateChordHit( RockBandPlayerState rbps, Chord c ) {
        // Enumerate each note in this chord and add up the score
        for( Note n : c.getNotes() ) {
            long noteScore;

            rbps.NoteStreak++;

            noteScore = RockBandConstants.BaseNoteScore * GetMultiplier(rbps.NoteStreak);

            if( rbps.Now <= rbps.OverdriveEndTick ) {
                noteScore *= 2;
            }

            rbps.Score += noteScore;
        }

        return;
    }

    boolean IsOverdrivePhraseHit( Chord c, RockBandPlayerState rbps ) {

        boolean overdrivePhraseHit;

        overdrivePhraseHit = false;
        if( (rbps.AreFillsActive() == true) && (c.SecondaryLastNoteInOverdrivePhrase == true) ) {
            overdrivePhraseHit = true;
        }

        if( c.LastNoteInOverdrivePhrase == true ) {
            overdrivePhraseHit = true;
        }

        return overdrivePhraseHit;
    }

    boolean IsSoloPhraseHit( Chord c, RockBandPlayerState rbps ) {

        boolean soloPhraseHit;

        soloPhraseHit = false;
        if( c.LastNoteInSoloPhrase == true ) {
            soloPhraseHit = true;
        }

        return soloPhraseHit;
    }

    // Add a quarter bar of energy to the overdriveMeter
    // because we hit the last OD chord in a phrase successfully
    // I need to account for the tail end of an OD phrase being overlapped by a fill
    // in this case, the OD meter gain happens early
    // Also, is it possible for an OD phrase to span over a fill?  That would probably be an
    // error condition that I wouldn't know how to handle.
    private static void SimulateOverdrivePhraseHit( DrumChart dc, long fillDelay, RockBandPlayerState rbps ) {

        long lastOverdriveMeter;

        lastOverdriveMeter = rbps.OverdriveMeter;
        // This value is going to be wrong if we're in the middle of using overdrive.
        // However, I don't want to write code to estimate the value of the bar while it's moving
        // and it doesn't affect score calculations, so I'm leaving it as is.
        rbps.OverdriveMeter += RockBandConstants.OverdriveMeterQuarter;
        if (rbps.OverdriveMeter > RockBandConstants.OverdriveMeterMax) {
            // Clamp the OD meter to the max value for overdrive
            rbps.OverdriveMeter = RockBandConstants.OverdriveMeterMax;
        }

        if (rbps.OverdriveEndTick != 0) {
            // Overdrive is active, the meter will be extended
            long overdriveBeats;

            overdriveBeats = RockBandConstants.OverdriveMeterQuarter / RockBandConstants.OverdrivePerBeat;

            rbps.OverdriveEndTick = dc.GetOverdriveEnd( rbps.OverdriveEndTick, overdriveBeats );
//                System.out.format("T%d: Overdrive phrase hit under OD, overdriveMeter = %d\n", rbps.Now, rbps.OverdriveMeter);
        } else {

            // If we just got enough overdrive to activate, then determine when to start showing fills.
            // Why? The game delays a bit because we don't want a fill section to "teleport in".
            if ((rbps.OverdriveMeter >= RockBandConstants.OverdriveMeterHalf) && (lastOverdriveMeter < RockBandConstants.OverdriveMeterHalf)) {
                // Don't know what the tick number should be to block fills from appearing in
                rbps.FillsActiveTick = GetTickByAdditionalTime( rbps.Now, fillDelay, dc );

                // Log if there's a potential issue with a fill appearing or disappearing based on note timing
                for (Map.Entry<Long, Long> fp : dc.getFillPhrases().entrySet()) {
                    // Fills can't partially appear, so we assume we're not in a fill
                    // if fill delay time intersects the fill
                    if( Math.abs(rbps.FillsActiveTick - fp.getKey()) <= RockBandConstants.FillControlThreshold ) {
                        String  warning;

                        warning = String.format( "T%d: Fill at %d may appear or disappear based on when the last note of this OD phrase is hit.\n", rbps.Now, fp.getKey() );
                        System.out.format( warning );
                        rbps.AddWarning( warning );
                        break;
                    }
                }
            }

//                        System.out.format("T%d: Overdrive phrase hit, overdriveMeter = %d\n", rbps.Now, rbps.OverdriveMeter);
        }

        return;
    }

    private static void SimulateSoloPhraseHit( DrumChart dc, Chord cIn, RockBandPlayerState rbps ) {

        int                 soloNotes;
        long                soloBonus;
        ArrayList<Chord>    chords;

        soloNotes = 0;
        soloBonus = 0;
        chords = dc.getChords();

        for( Chord c : chords ) {
            if( c.SoloPhrase == cIn.SoloPhrase ) {
                soloNotes += c.getNotes().size();
            }
        }

        soloBonus = soloNotes * RockBandConstants.BaseNoteScore * RockBandConstants.SoloPerfectMultiplier;
        rbps.Score += soloBonus;

//        System.out.format( "Solo notes hit = %d, Bonus = %d\n", soloNotes, soloBonus );
        return;
    }

    // Given a tick, looks for the last chord inside the tick range that would be considered
    // under the fill so as to simulate the squeeze
    private static Chord FindFrontEndSqueeze( DrumChart dc, long tick ) {

        Chord   squeeze;
        int     x;
        ArrayList<Chord>    chords;

        chords = dc.getChords();

        // Walk the chords backwards to find the last available squeeze
        for( x = chords.size() - 1; x >= 0; x-- ) {
            Chord   c;

            c = chords.get(x);
            if( c.IsActivation == true ) {
                continue;
            }
            
            if( Math.abs(c.getTick() - tick) < RockBandConstants.FrontEndSqueezeTicks ) {
                squeeze = c;
                return squeeze;
            }
        }

        return null;
    }

    private static int GetMultiplier( int multiplierCounter ) {

        int multiplier;

        assert( multiplierCounter > 0 );

        multiplier = 1;

        if( multiplierCounter <= 9 ) {
            multiplier = 1;
        }
        else if( multiplierCounter <= 19 ) {
            multiplier = 2;
        }
        else if( multiplierCounter <= 29 ) {
            multiplier = 3;
        }
        else if( multiplierCounter >= 30 ) {
            multiplier = 4;
        }

        return multiplier;
    }

    // Given a start tick and a number of beats, adds the beats up and returns the end tick
    // Only works if we have one or more beats (aka quarterBeats >= 4);
    private static long GetTickByAdditionalBeats( long startTick, float additionalBeats, ArrayList<Chord> beats ) {

        boolean             firstBeat;
        float               remainingBeat;
        Chord               lastBeat;
        long                outTick;

        firstBeat = false;
        remainingBeat = additionalBeats;
        lastBeat = null;
        outTick = 0;

        for( Chord b : beats ) {
            if( startTick < b.getTick() ) {
                if( lastBeat == null ) {
                    break;
                }

                if( firstBeat == false ) {
                    assert( startTick == lastBeat.getTick() );
                    if( startTick != lastBeat.getTick() ) {
                        float   beatNumerator, beatDenominator;
                        float   fractionalBeat;

                        beatNumerator = (long)(b.getTick() - startTick);
                        beatDenominator = (long)(b.getTick() - lastBeat.getTick());
                        fractionalBeat = beatNumerator / beatDenominator;
                        remainingBeat = remainingBeat - fractionalBeat;
                    }
                    else {
                        remainingBeat -= 1.0f;
                    }

                    firstBeat = true;
                }
                else {
                    remainingBeat -= 1.0f;
                    if( remainingBeat <= 0.0f ) {
                        outTick = b.getTick();
                        if( remainingBeat < 0.0f ) {
                            outTick += (long)( remainingBeat * (b.getTick() - lastBeat.getTick()) );
                        }

                        break;
                    }
                }

            }

            lastBeat = b;
        }

        System.out.format( "GetTickByAdditionalBeats added %d ticks.\n", outTick - startTick );
        assert( outTick > 0 );
        return outTick;
    }


    private static long GetTickByAdditionalTime( long startTick, long ms, DrumChart dc ) {

        Set<Map.Entry<Long, Long>>  tempoSet;
        Map.Entry<Long, Long>       lastTempo;

        long                    tempo;
        long                    elapsed;
        long                    outTick;

        tempo = 0;
        elapsed = 0;
        lastTempo = null;
        outTick = startTick;

        tempoSet = dc.getTempo().entrySet();
        for( Map.Entry<Long, Long> t : tempoSet ) {
            if( lastTempo == null ) {
                lastTempo = t;
                continue;
            }

            if( startTick < lastTempo.getKey() ) {
                lastTempo = t;
                continue;
            }

            if( startTick >= t.getKey() ) {
                lastTempo = t;
                continue;
            }

            tempo = lastTempo.getValue();
            break;
        }

        assert( tempo != 0 );
        if( tempo == 0 ) {
            return 0;
        }

        while( elapsed < (ms * 1000) ) {
            outTick++;
            elapsed += tempo / 480;
            if( dc.getTempo().containsKey(outTick) == true ) {
                tempo = dc.getTempo().get( outTick );
            }
        }

//        System.out.format( "GetTickByAdditionalTime added %d ticks.\n", outTick - startTick );
        return outTick;
    }

/*
    private static void PrintActivationPathsDepth( RockBandPlayerState state ) {

        ArrayList<RockBandPlayerState>  terminalStates;

        terminalStates = new ArrayList<RockBandPlayerState>();

        WalkActivationTree( state, terminalStates );

        Collections.sort( terminalStates, new RockBandPlayerStateScoreComparator() );
        
        for( RockBandPlayerState terminalState : terminalStates ) {
            PrintPath( terminalState );
        }

        System.out.format( "%d paths calculated.\n", terminalStates.size() );
        return;
    }
 */

/*
    private static void PrintActivationPathsBreadth( ArrayList<RockBandPlayerState> uniqueStates ) {

        Collections.sort( uniqueStates, new RockBandPlayerStateScoreComparator() );

        for( RockBandPlayerState terminalState : uniqueStates ) {
            PrintPath( terminalState );
        }

        System.out.format( "%d paths calculated.\n", uniqueStates.size() );
        return;
    }
 */

    private int IsInFill( DrumChart dc, RockBandPlayerState rbps ) {

        int returnFillIndex;

        returnFillIndex = -1;
        if( rbps.AreFillsActive() == true ) {

            int fillIndex;

            fillIndex = 0;
            for( Map.Entry<Long, Long> fp : dc.getFillPhrases().entrySet() ) {

                // Fills can't partially appear, so we assume we're not in a fill
                // if fill delay time intersects the fill
                if( (rbps.Now >= fp.getKey()) && (rbps.FillsActiveTick < fp.getKey())) {
                    if( rbps.Now <= fp.getValue() ) {
                        returnFillIndex = fillIndex;
                        break;
                    }
                }

                fillIndex++;
            }
        }

        return returnFillIndex;
    }

    private static ArrayList<RockBandPlayerState> GetNonOptimalPaths( ArrayList<RockBandPlayerState> states ) {

        ArrayList<RockBandPlayerState>  removeStates;

        Collections.sort( states, new RockBandPlayerStateOptimalComparator() );

        removeStates = new ArrayList<RockBandPlayerState>();

        for( int x=0; x+1 < states.size(); x++ ) {
            RockBandPlayerState rbps1, rbps2;

            rbps1 = states.get( x );
            rbps2 = states.get( x+1 );

            if( rbps1.OverdriveStartTick != Long.MAX_VALUE ) {
                continue;
            }

            if( rbps2.OverdriveStartTick != Long.MAX_VALUE ) {
                continue;
            }

            if( rbps1.OverdriveMeter != rbps2.OverdriveMeter ) {
                continue;
            }

            if( rbps1.Score < rbps2.Score ) {
                removeStates.add( rbps1 );
            }
        }

        return removeStates;
    }
    
/*
    private boolean Print() {

        GeneratorSettings   settings;
        ArrayList<DrumPath> paths;
        String              statsText;
        String              missesText;

        settings = new GeneratorSettings();

        paths = Generate( settings );

        if( settings.MissesAreNonOptimal == true ) {
            missesText = "Checked paths with missed overdrive phrases";
        }
        else {
            missesText = "Did not check paths with missed overdrive phrases";
        }
        
        statsText = String.format( "Examined %d paths", paths.size() );
        for( DrumPath p : paths ) {
            String  pathText;

            pathText = "Score without squeezing = %d, Score with squeezing = %d, Path = ";
        }

        return true;
    }
 */
}
