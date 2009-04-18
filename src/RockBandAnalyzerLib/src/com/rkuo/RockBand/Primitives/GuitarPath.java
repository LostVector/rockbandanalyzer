package com.rkuo.RockBand.Primitives;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 1:13:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class GuitarPath {

    // The zero based index of notes to activate on
    private ArrayList   _activationNotes;

    // The score associated with this path
    private int         _score;

    // The skip based notation of fills to activate

    public GuitarPath() {
        _activationNotes = new ArrayList();
        _score = 0;
        return;
    }
}
