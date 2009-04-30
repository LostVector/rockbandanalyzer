package com.rkuo.RockBand.Primitives;

import com.rkuo.RockBand.RockBandMidi;
import com.rkuo.RockBand.RockBandChartDifficulty;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 12:14:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Note {
    private int _number;
    public int sustain;
    public boolean hammerOn;

    public Note( int number ) {
        _number = number;
        sustain = 0;
        hammerOn = false;
        return;
    }

    public int getNumber() {
        return _number;
    }

    public boolean isSoloMarker( RockBandChartDifficulty d ) {

        int marker;

        marker = RockBandMidi.DrumsExpertSoloMarker;
        if( d == RockBandChartDifficulty.Easy ) {
            marker = RockBandMidi.DrumsEasySoloMarker;
        }
        else if( d == RockBandChartDifficulty.Medium ) {
            marker = RockBandMidi.DrumsMediumSoloMarker;
        }
        else if( d == RockBandChartDifficulty.Hard ) {
            marker = RockBandMidi.DrumsHardSoloMarker;
        }
        else {
            // must be expert
        }

        if( _number == marker ) {
            return true;
        }

        return false;
    }

    public boolean isOverdriveMarker() {

        if( _number == RockBandMidi.OverdriveMarker ) {
            return true;
        }

        return false;
    }

    public boolean isFillMarker() {

        if( _number == RockBandMidi.DrumsFillRed ) {
            return true;
        }

        if( _number == RockBandMidi.DrumsFillYellow ) {
            return true;
        }

        if( _number == RockBandMidi.DrumsFillBlue ) {
            return true;
        }

        if( _number == RockBandMidi.DrumsFillGreen ) {
            return true;
        }

        if( _number == RockBandMidi.DrumsFillOrange ) {
            return true;
        }

        return false;
    }
}
