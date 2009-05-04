package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Key;
import com.rkuo.RockBand.Primitives.Chord;

import javax.jdo.annotations.*;
import java.util.Date;
import java.util.ArrayList;

/**
Data that is not the original file or embedded within the midi, but is
 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandSongGenerated {

    @PrimaryKey
//    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//    private Key _key;
    private String    id;

    public long getMicroseconds() {
        return microseconds;
    }

    public void setMicroseconds(long microseconds) {
        this.microseconds = microseconds;
    }

    @Persistent
    private long         microseconds;

    @Persistent
    private long         Notes;

    @Persistent
    private long         Chords;

    @Persistent
    private long         Solos;

    @Persistent
    private long         Fills;

    @Persistent
    private long         OverdrivePhrases;

    // Does the song contain a big rock ending?
    @Persistent
    private boolean      BigRockEnding;

    // Every note in the song * the base note score (25). Notes hidden under a BRE are not counted.
    @Persistent
    private long         UnmultipliedScore;

    @Persistent
    private long         MultipliedScore;

    // Every note in the song * the base note score (25). Notes hidden under a BRE are counted.
    @Persistent
    private long         UnmultipliedScoreWithBRENotes;

    @Persistent
    private boolean      MaximumMultiplierNotReachedWarning;

    @Persistent
    private long         StarCutoffOne;

    @Persistent
    private long         StarCutoffTwo;

    @Persistent
    private long         StarCutoffThree;

    @Persistent
    private long         StarCutoffFour;

    @Persistent
    private long         StarCutoffFive;

    @Persistent
    private long         StarCutoffGold;

    @Persistent
    private long         StarCutoffGoldOld;

    private ArrayList<Chord> GlitchedChords;

    public RockBandSongGenerated() {
        microseconds = -1;
        return;
    }

    public Long getId() {
        Long    numericId;

        numericId = Long.parseLong( id.substring(3) );
        return numericId;
    }

    public void setId( Long id ) {
        this.id = "rbe" + id.toString();
        return;
    }

/*
    public Key getKey() {
        return _key;
    }

    public void setKey( Key key ) {
        _key = key;
        return;
    }
 */
}
