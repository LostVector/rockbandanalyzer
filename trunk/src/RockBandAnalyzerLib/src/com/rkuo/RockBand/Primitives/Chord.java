package com.rkuo.RockBand.Primitives;

import com.rkuo.RockBand.Primitives.Note;
import com.rkuo.RockBand.NoteDisplayComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 8:53:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class Chord implements Comparable {

    private ArrayList<Note> _notes;
    private long            _tick;
    public boolean          IsActivation;

    // if greater than 0, indicates that this chord is part of an overdrive phrase
    // where the value is the index of the phrase in the song
    public int              OverdrivePhrase;

    public boolean          LastNoteInOverdrivePhrase;

    // If a phrase gets partially overlapped by a fill on the trailing end of the phrase
    // indicates that this becomes the last note of the phrase
    public boolean          SecondaryLastNoteInOverdrivePhrase;

    // if greater than 0, indicates that this chord is part of an solo phrase
    // where the value is the index of the phrase in the song
    public int              SoloPhrase;
    public boolean          LastNoteInSoloPhrase;

    public boolean          BigRockEnding;
    public boolean          BigRockEndingActivation;

    // Indicate this chord can be front-end squeezed under a fill activation
    public boolean          PotentialSqueeze;

    public Chord( long tick ) {
        _notes = new ArrayList<Note>();
        _tick = tick;
        IsActivation = false;
        OverdrivePhrase = 0;
        LastNoteInOverdrivePhrase = false;
        SecondaryLastNoteInOverdrivePhrase = false;
        SoloPhrase = 0;
        LastNoteInSoloPhrase = false;
        BigRockEnding = false;
        BigRockEndingActivation = false;
        PotentialSqueeze = false;
        return;
    }

    public void AddNote( Note n ) {
        _notes.add( n );
        Collections.sort( _notes, new NoteDisplayComparator() );
        return;
    }

    public ArrayList<Note> getNotes() {
        return _notes;
    }

    public long getTick() {
        return _tick;
    }

    public int compareTo( Object o ) {

        Chord    c2;

        if( (o instanceof Chord) == false ) {
            throw new ClassCastException("A Chord object expected.");
        }

        c2 = (Chord)o;

        if( _tick == c2.getTick() ) {
            return 0;
        }

        if( _tick < c2.getTick() ) {
            return -1;
        }

        return 1;
    }
}
