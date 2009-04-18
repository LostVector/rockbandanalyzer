package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.DrumChart;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 29, 2008
 * Time: 10:22:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPlayerState {

    public long Now;
    public long OverdriveMeter;
    public long OverdriveReadyTick;
//    public long OverdriveActive;
    public long Score;
    public int NoteStreak;

    public int SoloNotesHit;

    // if non-max, overdrive was activated on this tick...Used to detect notes coming too soon after a fill
    public long OverdriveStartTick;

    // Used to detect potential back-end squeezes
    public long OverdriveFinishedTick;

    // if non-zero, overdrive is active and will run out on this tick
    public long OverdriveEndTick;

    // If non-max, overdrive is ready and fills will become active on this tick
    public long FillsActiveTick;

    // if non-max, denotes the most recent end tick of a fill. Used to determine when notes should be skipped
    // because they come too soon after a fill
    public long LastPotentialActivationTick;

    // Activation branch nodes
    public int                 Depth;
    public RockBandPlayerState NodeActivate;
    public RockBandPlayerState NodeSkip;

    // Stores the path we are currently following. Only used when walking multiple paths
    public ArrayList<PathNode> CurrentPath;
    public boolean NonOptimal;
    
    // This is all state associated with following a specified path. Does this belong here?
    public ArrayList<Integer> SuggestedPath;
    public int SkipCounter;
    public int SkipIndex;

    public ArrayList<String> Warnings;

    public RockBandPlayerState() {
        Now = 0;
        OverdriveMeter = 0;
        OverdriveReadyTick = 0;
        NoteStreak = 0;
        Score = 0;

        SoloNotesHit = 0;

        SkipCounter = 0;
        
        OverdriveStartTick = Long.MAX_VALUE;
        OverdriveFinishedTick = Long.MAX_VALUE;
        OverdriveEndTick = 0;
        FillsActiveTick = Long.MAX_VALUE;
        LastPotentialActivationTick = Long.MAX_VALUE;

        CurrentPath = new ArrayList<PathNode>();
        NonOptimal = false;
        
        SuggestedPath = new ArrayList<Integer>();
        SkipCounter = 0;
        SkipIndex = 0;

        Depth = 0;
        NodeActivate = null;
        NodeSkip = null;

        Warnings = new ArrayList<String>();

        return;
    }

    public RockBandPlayerState clone() {

        RockBandPlayerState newState;

        newState = new RockBandPlayerState();

        newState.Now = Now;
        newState.OverdriveMeter = OverdriveMeter;
        newState.OverdriveReadyTick = OverdriveReadyTick;
        newState.NoteStreak = NoteStreak;
        newState.Score = Score;

        newState.SoloNotesHit = 0;

        newState.SkipCounter = SkipCounter;

        newState.OverdriveEndTick = OverdriveEndTick;
        newState.FillsActiveTick = FillsActiveTick;
        newState.LastPotentialActivationTick = LastPotentialActivationTick;

        newState.CurrentPath = new ArrayList<PathNode>(CurrentPath);
        newState.SuggestedPath = new ArrayList<Integer>(SuggestedPath);
        newState.SkipCounter = SkipCounter;
        newState.SkipIndex = SkipIndex;

        newState.Warnings = new ArrayList<String>(Warnings);
        
        return newState;
    }

    public void CreateChildNodes() {
        NodeActivate = this.clone();
        NodeSkip = this.clone();

        PathNode    activateNode, skipNode;

        activateNode = new PathNode();
        activateNode.Activate = true;
        activateNode.Squeeze = null;

        skipNode = new PathNode();
        skipNode.Activate = false;

        NodeActivate.CurrentPath.add( activateNode );
        NodeSkip.CurrentPath.add( skipNode );
        return;
    }

    // Are we in a fill?
    // Fills don't show up if we are in overdrive
    // Fills don't show up unless we have enough overdriveMeter
    // Fills don't show up until a certain amount of time has passed after attaining the
    // mininum required amount of overdriveMeter
    public boolean AreFillsActive() {

        boolean fillsActive;

        fillsActive = false;
        if( (OverdriveEndTick == 0) && (Now >= FillsActiveTick) ) {
            fillsActive = true;
        }

        return fillsActive;
    }

    public boolean IsOverdriveFinished() {

        boolean isOverdriveFinished;

        isOverdriveFinished = false;
        if( (OverdriveEndTick != 0) && (OverdriveEndTick < Now) ) {
            isOverdriveFinished = true;
        }

        return isOverdriveFinished;
    }

    public void ActivateOverdrive( DrumChart dc ) {

        long    overdriveBeats;

        // set up overdrive to be active
        overdriveBeats = OverdriveMeter / RockBandConstants.OverdrivePerBeat;

        OverdriveStartTick = Now;
        OverdriveEndTick = dc.GetOverdriveEnd( Now, overdriveBeats );

        // Tiny amount of slack to cover backend squeezing...may want to revisit this approach
        OverdriveEndTick += RockBandConstants.BackEndSqueezeTicks;

        FillsActiveTick = Long.MAX_VALUE;

//        System.out.format( "T%d: Overdrive activated, ends at %d\n", Now, OverdriveEndTick );
        return;
    }

    public void DeactivateOverdrive() {

        OverdriveStartTick = Long.MAX_VALUE;
        OverdriveFinishedTick = OverdriveEndTick;
        OverdriveEndTick = 0;
        OverdriveMeter = 0;
        return;
    }

    public void AddWarning( String s ) {

        Warnings.add( s );
        return;
    }
}
