package com.rkuo.RockBand;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 26, 2009
 * Time: 3:00:35 PM
 * To change this template use File | Settings | File Templates.
 */
public enum RockBandInstrumentDifficulty {
    Warmup,
    Apprentice,
    Solid,
    Moderate,
    Challenging,
    Nightmare,
    Impossible,
    Unknown;

    public static RockBandInstrumentDifficulty ToEnum( Long value ) {

        if( value == 0 ) {
            return Warmup;
        }
        else if( value == 1 ) {
            return Apprentice;
        }
        else if( value == 2 ) {
            return Solid;
        }
        else if( value == 3 ) {
            return Moderate;
        }
        else if( value == 4 ) {
            return Challenging;
        }
        else if( value == 5 ) {
            return Nightmare;
        }
        else if( value == 6 ) {
            return Impossible;
        }

        return Unknown;
    }
}
