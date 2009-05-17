package com.rkuo.RockBand;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 26, 2009
 * Time: 3:10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public enum RockBandLocation {
    Unknown,
    Downloaded,
    RockBandTwo,
    RockBandOne,
    TrackPack;

    public static RockBandLocation ToEnum( Integer value ) {

        if( value == 0 ) {
            return Downloaded;
        }
        else if( value == 1 ) {
            return RockBandOne;
        }
        else if( value == 2 ) {
            return RockBandTwo;
        }
        else if( value == 16 ) {
            return TrackPack;
        }


        return Unknown;
    }

    public static Integer ToInteger( RockBandLocation rbl ) {

        if( rbl == RockBandLocation.Downloaded ) {
            return 0;
        }
        else if( rbl == RockBandLocation.RockBandOne ) {
            return 1;
        }
        else if( rbl == RockBandLocation.RockBandTwo ) {
            return 2;
        }
        else if( rbl == RockBandLocation.TrackPack ) {
            return 16;
        }

        return -1;
    }
}
