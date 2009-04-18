package com.rkuo.RockBand;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 11, 2008
 * Time: 1:24:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandConstants {

    public static final long    OverdriveMeterQuarter = 16384;
    public static final long    OverdriveMeterHalf = 32768;
    public static final long    OverdriveMeterMax = 65536;

    // Each quarter bar gives 8 beats of overdrive
    public static final long    OverdrivePerBeat = 2048;

    // The simulator decides that any chord within this number of ticks of an activation tick
    // is a potential front end squeeze
    public static final long    FrontEndSqueezeTicks = 10;

    // The simulator decides that any chord within this number of ticks of the end of an overdrive
    // is a potential back end squeeze
    public static final long    BackEndSqueezeTicks = 10;

    // The simulator decides that any chord within this number of ticks of the end of an active fill will not
    // be displayed or counted
    public static final long    FillDeadZoneTicks = 10;

    // The base number of points you get from a successful note hit, without any multiplier
    public static final long    BaseNoteScore = 25;

    // These multipliers are applied to the baseline score to determine the
    // score above which the specified stars can be obtained.
    public static final double    DrumsStarMultiplierOne = 1;
    public static final double    DrumsStarMultiplierTwo = 1;
    public static final double    DrumsStarMultiplierThree = 1;
    public static final double    DrumsStarMultiplierFour = 1.85;
    public static final double    DrumsStarMultiplierFive = 3.079;
    public static final double    DrumsStarMultiplierGold = 4.29;

    // The gold multiplier was adjusted downwards just before Rock Band 2 was released.
    // Also, pre-patch, notes hidden under the BRE used to be factored into the calculation of stars
    // Post-patch, these notes are no longer counted
    public static final double    DrumsStarMultiplierGoldOld = 4.44005;

    // If two chords are this many ticks close together (or closer)
    // they will be flagged as potential glitches
    // In Next To You, it's 30 ticks.
    // In Panic Attack, it's 40 ticks.
    // In New Wave, it's 3 ticks!
    public static final long      PotentialGlitchTicks = 40;

    // Denotes the window within which a fill is deemed to be "controllable"
    // by hitting the last note of the overdrive phrase before it early or late
    public static final long      FillControlThreshold = 10;

    // Currently suggested that this is around 2.43 seconds, or 2430 ms
    public static final long    FillDelayRB1 = 2430;

    // The fill delay values changed in RB2, and in addition
    // the delays are tied to the scroll speeds of each difficulty
//    public static final long    FillDelayRB2Easy = 1170;
//    public static final long    FillDelayRB2Medium = 1170;
//    public static final long    FillDelayRB2Hard = 1170;
    public static final long    FillDelayRB2Expert = 1170;

    // the multiplier of the normal scroll speed when breakneck speed is applied
    // In RB2, applying breakneck speed causes the notes to scroll 50% faster
    // meaning fill delay becomes 2/3rds of normal
    // In conjunction with the default fill delay
    public static final long    RB2BreakneckSpeedNumerator = 2;
    public static final long    RB2BreakneckSpeedDenominator = 3;

    public static final long    MaxMultiplier = 4;
    public static final long    SoloPerfectMultiplier = 4;
}
