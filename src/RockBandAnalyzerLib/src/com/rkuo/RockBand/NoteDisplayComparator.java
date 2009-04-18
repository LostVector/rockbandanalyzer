package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.Note;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 1, 2008
 * Time: 11:39:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class NoteDisplayComparator implements Comparator<Note> {
    public int compare( Note n1, Note n2 ) {

        int nx1, nx2;

        nx1 = RockBandMidi.toDrumColorIndex( n1.getNumber() );
        nx2 = RockBandMidi.toDrumColorIndex( n2.getNumber() );

        return nx1 - nx2;
    }
}
