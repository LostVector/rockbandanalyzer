package com.rkuo.RockBand.Tests;

import com.rkuo.RockBand.Simulators.DrumsSimulator;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 27, 2008
 * Time: 8:08:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrumsScoreSimulatorTester {

    public boolean Test() {

        DrumsSimulator dg;
        String          fileName;
        ArrayList<Integer> skipNotation;
        long            expectedScore;
        long            expectedStreak;

        skipNotation = new ArrayList<Integer>();

        dg = new DrumsSimulator();

        fileName = "blooddoll";
        expectedScore = 92975;
        expectedStreak = 792;
        skipNotation.clear();
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
//        dg.GeneratePathedScore( fileName, skipNotation.toArray(Type(int[])) );

        // This one might be sketchy b/c of Big Rock Ending
        fileName = "cantletgo";
        expectedScore = 160375;
        expectedStreak = 1344;
        skipNotation.clear();
        skipNotation.add( 1 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );

        fileName = "ballroomblitz";
        expectedScore = 232875;
        expectedStreak = 1707;
        skipNotation.clear();
        skipNotation.add( 1 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );

        fileName = "29fingers";
        expectedScore = 93875;
        expectedStreak = 769;
        skipNotation.clear();
        skipNotation.add( 1 );
        skipNotation.add( 1 );
        skipNotation.add( 0 );

        fileName = "pleasure";
        expectedScore = 144475;
        expectedStreak = 1138;
        skipNotation.clear();
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 2 );
        skipNotation.add( 0 );

        fileName = "orangecrush";
        expectedStreak = 1934;
        expectedScore = 233375;
        skipNotation.clear();
        skipNotation.add( 2 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 0 );

        fileName = "maps";
        expectedScore = 194975;
        expectedStreak = 1621;
        skipNotation.clear();
        skipNotation.add( 0 );
        skipNotation.add( 0 );
        skipNotation.add( 1 );
        return true;
    }
}
