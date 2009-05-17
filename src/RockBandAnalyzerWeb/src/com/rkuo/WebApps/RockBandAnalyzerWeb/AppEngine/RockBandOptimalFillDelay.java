package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 9, 2009
 * Time: 2:38:01 AM
 * To change this template use File | Settings | File Templates.
 */
public enum RockBandOptimalFillDelay {
    Unknown,
    None,
    Normal,
    Breakneck;

    public static RockBandOptimalFillDelay ToEnum( Integer value ) {

        if( value == 0 ) {
            return None;
        }
        else if( value == 1 ) {
            return Normal;
        }
        else if( value == 2 ) {
            return Breakneck;
        }

        return Unknown;
    }

    public static Integer ToInteger( RockBandOptimalFillDelay rbid ) {

        if( rbid == RockBandOptimalFillDelay.None ) {
            return 0;
        }
        if( rbid == RockBandOptimalFillDelay.Normal ) {
            return 1;
        }
        if( rbid == RockBandOptimalFillDelay.Breakneck ) {
            return 2;
        }

        return -1;
    }
}
