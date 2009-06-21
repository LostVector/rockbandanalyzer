package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jun 18, 2009
 * Time: 4:11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    public static boolean IsDebug() {
        String value;

        value = System.getProperty("debug");
        if( value != null ) {
            if( Boolean.parseBoolean(value) == true ) {
                return true;
            }
        }

        return false;
    }
}
