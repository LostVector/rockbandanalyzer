package com.rkuo.RockBand.Primitives;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 1:09:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class DrumPath implements Comparable {

    // The zero based index of fills to activate
    private ArrayList   _activationFills;

    // The score associated with this path
    private int         _score;

    // The skip based notation of fills to activate

    public DrumPath() {
        _score = 0;
        _activationFills = new ArrayList();
        return;
    }

    public int getScore() {
        return _score;
    }

    public void setScore( int score ) {
        _score = score;
    }

    public int compareTo( Object o ) throws ClassCastException {

        DrumPath    dp2;

        if( (o instanceof DrumPath) == false ) {
            throw new ClassCastException("A DrumPath object expected.");
        }

        dp2 = (DrumPath)o;
        
        return this.getScore() - dp2.getScore();
    }
}
