package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.Primitives.Chord;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jan 2, 2009
 * Time: 3:32:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrumsBaselineData {

    public String       SongTitle;
    public long         Microseconds;

    public long         Notes;
    public long         Chords;

    public long         Solos;
    public long         Fills;
    public long         OverdrivePhrases;

    // Does the song contain a big rock ending?
    public boolean      BigRockEnding;

    // Every note in the song * the base note score (25). Notes hidden under a BRE are not counted.
    public long         UnmultipliedScore;
    public long         MultipliedScore;

    // Every note in the song * the base note score (25). Notes hidden under a BRE are counted.
    public long         UnmultipliedScoreWithBRENotes;

    public ArrayList<Chord>    GlitchedChords;

    public boolean      MaximumMultiplierNotReachedWarning;

    public long         StarCutoffOne;
    public long         StarCutoffTwo;
    public long         StarCutoffThree;
    public long         StarCutoffFour;
    public long         StarCutoffFive;
    public long         StarCutoffGold;
    public long         StarCutoffGoldOld;

    public DrumsBaselineData() {
        SongTitle = "";
        Microseconds = 0;

        Notes = 0;
        Chords = 0;
        Solos = 0;
        Fills = 0;
        OverdrivePhrases = 0;

        BigRockEnding = false;

        UnmultipliedScore = 0;
        MultipliedScore = 0;

        UnmultipliedScoreWithBRENotes = 0;

        GlitchedChords = new ArrayList<Chord>();

        StarCutoffOne = 0;
        StarCutoffTwo = 0;
        StarCutoffThree = 0;
        StarCutoffFour = 0;
        StarCutoffFive = 0;
        StarCutoffGold = 0;
        StarCutoffGoldOld = 0;

        MaximumMultiplierNotReachedWarning = false;
        return;
    }
}
