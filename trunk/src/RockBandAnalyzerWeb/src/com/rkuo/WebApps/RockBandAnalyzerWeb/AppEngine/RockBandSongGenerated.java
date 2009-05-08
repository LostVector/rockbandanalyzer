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

    public long getUnmultipliedScoreWithBRENotes() {
        return UnmultipliedScoreWithBRENotes;
    }

    public void setUnmultipliedScoreWithBRENotes(long unmultipliedScoreWithBRENotes) {
        UnmultipliedScoreWithBRENotes = unmultipliedScoreWithBRENotes;
    }

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

    @Persistent
    private long         MaxScore;

    @NotPersistent
    private ArrayList<Chord> GlitchedChords;

    @NotPersistent
    private String      RB2ImmediatePath;

    @NotPersistent
    private String      OptimalRB2NormalPath;

    @NotPersistent
    private String      OptimalRB2BreakneckPath;

    @NotPersistent
    private String      OptimalRB2Path;

    @Persistent
    private boolean     breakneckOptimal;

    @Persistent
    private boolean     glitched;

    @Persistent
    private boolean     goldStarrable;

    public RockBandSongGenerated() {
        microseconds = -1;
        breakneckOptimal = false;
        glitched = false;
        goldStarrable = true;
        Notes = 0;
        Chords = 0;
        Solos = 0;
        BigRockEnding = false;
        Fills = 0;
        OverdrivePhrases = 0;
        UnmultipliedScore = 0;
        UnmultipliedScoreWithBRENotes = 0;

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


    public boolean isGoldStarrable() {
        return goldStarrable;
    }

    public void setGoldStarrable(boolean goldStarrable) {
        this.goldStarrable = goldStarrable;
    }

    public boolean isGlitched() {
        return glitched;
    }

    public void setGlitched(boolean glitched) {
        this.glitched = glitched;
    }

    public boolean isBreakneckOptimal() {
        return breakneckOptimal;
    }

    public void setBreakneckOptimal(boolean breakneckOptimal) {
        this.breakneckOptimal = breakneckOptimal;
    }


    public long getFills() {
        return Fills;
    }

    public void setFills(long fills) {
        Fills = fills;
    }

    public long getOverdrivePhrases() {
        return OverdrivePhrases;
    }

    public void setOverdrivePhrases(long overdrivePhrases) {
        OverdrivePhrases = overdrivePhrases;
    }

    public long getUnmultipliedScore() {
        return UnmultipliedScore;
    }

    public void setUnmultipliedScore(long unmultipliedScore) {
        UnmultipliedScore = unmultipliedScore;
    }

    public long getMultipliedScore() {
        return MultipliedScore;
    }

    public void setMultipliedScore(long multipliedScore) {
        MultipliedScore = multipliedScore;
    }

    public long getChords() {
        return Chords;
    }

    public void setChords(long chords) {
        Chords = chords;
    }


    public long getMicroseconds() {
        return microseconds;
    }

    public void setMicroseconds(long microseconds) {
        this.microseconds = microseconds;
    }

    public long getNotes() {
        return Notes;
    }

    public void setNotes(long notes) {
        Notes = notes;
    }

    public long getSolos() {
        return Solos;
    }

    public void setSolos(long solos) {
        Solos = solos;
    }

    public boolean isBigRockEnding() {
        return BigRockEnding;
    }

    public void setBigRockEnding(boolean bigRockEnding) {
        BigRockEnding = bigRockEnding;
    }
    
    public long getMaxScore() {
        return MaxScore;
    }

    public void setMaxScore(long maxScore) {
        MaxScore = maxScore;
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
