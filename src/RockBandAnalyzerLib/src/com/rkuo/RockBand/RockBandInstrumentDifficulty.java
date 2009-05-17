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
    Unknown,
    NoPart;

    public static RockBandInstrumentDifficulty ToEnum( Integer value ) {

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
        else if( value == -2 ) {
            return NoPart;
        }

        return Unknown;
    }

    public static Integer ToInteger( RockBandInstrumentDifficulty rbid ) {

        if( rbid == RockBandInstrumentDifficulty.Warmup ) {
            return 0;
        }
        if( rbid == RockBandInstrumentDifficulty.Apprentice ) {
            return 1;
        }
        if( rbid == RockBandInstrumentDifficulty.Solid ) {
            return 2;
        }
        if( rbid == RockBandInstrumentDifficulty.Moderate ) {
            return 3;
        }
        if( rbid == RockBandInstrumentDifficulty.Challenging ) {
            return 4;
        }
        if( rbid == RockBandInstrumentDifficulty.Nightmare ) {
            return 5;
        }
        if( rbid == RockBandInstrumentDifficulty.Impossible ) {
            return 6;
        }
        if( rbid == RockBandInstrumentDifficulty.NoPart ) {
            return -2;
        }

        return -1;
    }
}
