package com.rkuo.RockBand;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jan 5, 2009
 * Time: 11:15:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandDrums {
    public static long GetFillDelay( RockBandGame rbGame, RockBandDifficulty rbDifficulty, boolean breakNeckSpeed ) {

        long    fillDelay;

        fillDelay = 0;

        if( rbGame == RockBandGame.RockBandOne ) {
            fillDelay = RockBandConstants.FillDelayRB1;
        }
        else if( rbGame == RockBandGame.RockBandTwo ) {
            if( rbDifficulty == RockBandDifficulty.Expert ) {
                fillDelay = RockBandConstants.FillDelayRB2Expert;
            }
            else if( rbDifficulty == RockBandDifficulty.Hard ) {

            }
            else if( rbDifficulty == RockBandDifficulty.Medium ) {

            }
            else if( rbDifficulty == RockBandDifficulty.Easy ) {

            }
            else {
                // Unrecognized rock band difficulty
            }
        }
        else {
            // Unrecognized rock band game
        }

        if( breakNeckSpeed == true ) {
            fillDelay = fillDelay * RockBandConstants.RB2BreakneckSpeedNumerator;
            fillDelay = fillDelay / RockBandConstants.RB2BreakneckSpeedDenominator;
        }

        return fillDelay;
    }
}
