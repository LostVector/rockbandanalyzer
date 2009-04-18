package com.rkuo.RockBand;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 31, 2008
 * Time: 1:24:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPlayerStateScoreComparator implements Comparator<RockBandPlayerState> {
    public int compare(RockBandPlayerState rbps1, RockBandPlayerState rbps2) {
//        RockBandPlayerState rbps1 = (RockBandPlayerState) o1;
//        RockBandPlayerState rbps2 = (RockBandPlayerState) o2;

        if( rbps1.Score > rbps2.Score ) {
            return 1;
        }

        if( rbps2.Score > rbps1.Score ) {
            return -1;
        }

        return 0;
     }
}
