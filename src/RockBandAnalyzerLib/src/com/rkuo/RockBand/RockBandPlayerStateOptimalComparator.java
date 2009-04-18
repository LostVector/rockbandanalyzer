package com.rkuo.RockBand;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 1, 2008
 * Time: 11:39:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPlayerStateOptimalComparator implements Comparator<RockBandPlayerState> {
    public int compare( RockBandPlayerState rbps1, RockBandPlayerState rbps2 ) {

        if( rbps1.Depth > rbps2.Depth ) {
            return 1;
        }

        if( rbps1.Depth < rbps2.Depth ) {
            return -1;
        }

        if( rbps1.OverdriveMeter > rbps2.OverdriveMeter ) {
            return 1;
        }

        if( rbps1.OverdriveMeter < rbps2.OverdriveMeter ) {
            return -1;
        }

        if( rbps1.Score > rbps2.Score ) {
            return 1;
        }

        if( rbps1.Score < rbps2.Score ) {
            return -1;
        }

        return 0;
    }
}
