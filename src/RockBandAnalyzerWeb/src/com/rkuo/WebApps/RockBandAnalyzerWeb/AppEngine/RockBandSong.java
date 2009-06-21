package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandLocation;

import javax.jdo.annotations.*;
import java.io.Serializable;
import java.util.Date;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class RockBandSong implements Serializable {

    @PrimaryKey
    private String id;

    @Persistent
    private Long    lastUpdated;

    @Persistent
    @Embedded
    private RBAssociated associated;

    @Persistent
    @Embedded
    private RBGenerated generated;

    @NotPersistent
    public static String idPrefix = "rbs";

    public RockBandSong() {
        lastUpdated = Long.MIN_VALUE;
        associated = new RBAssociated();
        generated = new RBGenerated();
        return;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long value) {
        lastUpdated = value;
        return;
    }

    public Long getId() {
        Long numericId;

        numericId = Long.parseLong(id.substring(idPrefix.length()));
        return numericId;
    }

    public void setId(Long id) {
        this.id = idPrefix + id.toString();
        return;
    }

    public RBAssociated getAssociated() {
        return associated;
    }

    public RBGenerated getGenerated() {
        return generated;
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

    // This is data that can be pulled directly from the midi files without complex analysis
    // Example ... durations, midi titles
    // There should generally be no need to regenerate this data when reprocessing files.
    @PersistenceCapable
    @EmbeddedOnly
    public static class RBAssociated implements Serializable {

        @Persistent
        private String artist;

        @Persistent
        private String album;

        @Persistent
        private String title;

        @Persistent
        private String genre;

        @Persistent
        private Integer location;

        @Persistent
        private Integer guitarDifficulty;

        @Persistent
        private Integer bassDifficulty;

        @Persistent
        private Integer drumsDifficulty;

        @Persistent
        private Integer vocalsDifficulty;

        @Persistent
        private Integer bandDifficulty;

        @Persistent
        private Boolean isCover;

        // when the song was released (in traditional channels)
        @Persistent
        private Date dateReleased;

        // when the song was released on Rock Band
        @Persistent
        private Date datePublished;

        @Persistent
        private Boolean availableInRB1;

        @Persistent
        private Boolean availableInRB2;

        public RBAssociated() {
            // associated ... should not change over time
            artist = "";
            title = "";
            album = "";
            genre = "";
            location = RockBandLocation.ToInteger( RockBandLocation.Downloaded );
            dateReleased = new Date(0);
            datePublished = new Date(0);
            guitarDifficulty = -1;
            bassDifficulty = -1;
            drumsDifficulty = -1;
            vocalsDifficulty = -1;
            bandDifficulty = -1;
            isCover = false;

            availableInRB1 = true;
            availableInRB2 = true;

            return;
        }

        public String getArtist() {
            return artist;
        }

        public String getAlbum() {
            return album;
        }

        public String getTitle() {
            return title;
        }

        public String getGenre() {
            return genre;
        }

        public Date getDateReleased() {
            return dateReleased;
        }

        public RockBandLocation getLocation() {
            return RockBandLocation.ToEnum( this.location );
        }

        public Date getRBReleaseDate() {
            return datePublished;
        }

        public RockBandInstrumentDifficulty getDifficulty(RockBandInstrumentDifficultyCategory rbidc) {
            if( rbidc == RockBandInstrumentDifficultyCategory.Guitar ) {
                return RockBandInstrumentDifficulty.ToEnum(this.guitarDifficulty);
            }
            else if( rbidc == RockBandInstrumentDifficultyCategory.Bass ) {
                return RockBandInstrumentDifficulty.ToEnum(this.bassDifficulty);
            }
            else if( rbidc == RockBandInstrumentDifficultyCategory.Drums ) {
                return RockBandInstrumentDifficulty.ToEnum(this.drumsDifficulty);
            }
            else if( rbidc == RockBandInstrumentDifficultyCategory.Vocals ) {
                return RockBandInstrumentDifficulty.ToEnum(this.vocalsDifficulty);
            }

            return RockBandInstrumentDifficulty.ToEnum(this.bandDifficulty);
        }

        public Boolean getCover() {
            return isCover;
        }

        public void setArtist(String artist) {
            this.artist = artist;
            return;
        }

        public void setAlbum(String album) {
            this.album = album;
            return;
        }

        public void setTitle(String title) {
            this.title = title;
            return;
        }

        public void setGenre(String genre) {
            this.genre = genre;
            return;
        }

        public void setDateReleased(Date dateReleased) {
            this.dateReleased = dateReleased;
            return;
        }

        public void setLocation(RockBandLocation location) {
            this.location = RockBandLocation.ToInteger( location );
            return;
        }

        public void setRBReleaseDate(Date datePublished) {
            this.datePublished = datePublished;
            return;
        }

        public void setGuitarDifficulty(RockBandInstrumentDifficulty guitarDifficulty) {
            this.guitarDifficulty = RockBandInstrumentDifficulty.ToInteger( guitarDifficulty );
        }

        public void setBassDifficulty(RockBandInstrumentDifficulty bassDifficulty) {
            this.bassDifficulty = RockBandInstrumentDifficulty.ToInteger( bassDifficulty );
        }

        public void setDrumsDifficulty(RockBandInstrumentDifficulty drumsDifficulty) {
            this.drumsDifficulty = RockBandInstrumentDifficulty.ToInteger( drumsDifficulty );
        }

        public void setVocalsDifficulty(RockBandInstrumentDifficulty vocalsDifficulty) {
            this.vocalsDifficulty = RockBandInstrumentDifficulty.ToInteger( vocalsDifficulty );
        }

        public void setBandDifficulty(RockBandInstrumentDifficulty bandDifficulty) {
            this.bandDifficulty = RockBandInstrumentDifficulty.ToInteger( bandDifficulty );
        }

        public void setCover(Boolean isCover) {

            this.isCover = false;
            if( isCover == true ) {
                this.isCover = true;
            }

            return;
        }

        public Boolean getAvailableInRB1() {
            return availableInRB1;
        }

        public void setAvailableInRB1(Boolean availableInRB1) {
            this.availableInRB1 = availableInRB1;
        }

        public Boolean getAvailableInRB2() {
            return availableInRB2;
        }

        public void setAvailableInRB2(Boolean availableInRB2) {
            this.availableInRB2 = availableInRB2;
        }
    }

    @PersistenceCapable
    @EmbeddedOnly
    public static class RBGenerated implements Serializable {

        // Begin non difficulty/instrument specific data
        @Persistent
        private String midiTitle;

        @Persistent
        private long microseconds;

        @Persistent
        private boolean BigRockEnding;

        // End non difficulty/instrument specific data

        // begin difficulty/instrument specific data
        @Persistent
        private long Notes;

        @Persistent
        private long Chords;

        @Persistent
        private long Solos;

        @Persistent
        private long OverdrivePhrases;

        // Every note in the song * the base note score (25). Notes hidden under a BRE are not counted.
        @Persistent
        private long UnmultipliedScore;

        @Persistent
        private long MultipliedScore;

        // Every note in the song * the base note score (25). Notes hidden under a BRE are counted.
        @Persistent
        private long UnmultipliedScoreWithBRENotes;

        @Persistent
        private boolean MaximumMultiplierNotReachedWarning;

        @Persistent
        private long StarCutoffOne;

        @Persistent
        private long StarCutoffTwo;

        @Persistent
        private long StarCutoffThree;

        @Persistent
        private long StarCutoffFour;

        @Persistent
        private long StarCutoffFive;

        @Persistent
        private long StarCutoffGold;

        @Persistent
        private long StarCutoffGoldOld;

        @Persistent
        private long maxScore;

        @Persistent
        private boolean goldStarrable;

        @Persistent
        private String OptimalRB2Path;

        // end diffculty/instrument specific data
//        @NotPersistent
//        private ArrayList<Chord> GlitchedChords;

        // begin drums specific data
        @Persistent
        private long Fills;

        @Persistent
        private Integer optimalFillDelay;

        @Persistent
        private boolean glitched;

        @Persistent
        private Long normalOptimalScore;

        @Persistent
        private Long breakneckOptimalScore;

        @Persistent
        private String ImmediateRB2NormalPath;

        @Persistent
        private String OptimalRB2NormalPath;

        @Persistent
        private String OptimalRB2BreakneckPath;
        // end drums specific data

        public RBGenerated() {

            // generated
            midiTitle = "";
            microseconds = -1;
            Notes = 0;
            Chords = 0;
            Solos = 0;
            BigRockEnding = false;
            Fills = 0;
            OverdrivePhrases = 0;
            UnmultipliedScore = 0;
            UnmultipliedScoreWithBRENotes = 0;
            optimalFillDelay = 0;
            glitched = false;
            goldStarrable = true;

            ImmediateRB2NormalPath = "";
            OptimalRB2NormalPath = "";
            OptimalRB2BreakneckPath = "";
            OptimalRB2Path = "";

            StarCutoffOne = 0;
            StarCutoffTwo = 0;
            StarCutoffThree = 0;
            StarCutoffFour = 0;
            StarCutoffFive = 0;
            StarCutoffGold = 0;
            StarCutoffGoldOld = 0;

            maxScore = 0;
            normalOptimalScore = 0L;
            breakneckOptimalScore = 0L;
            return;
        }

        public String getMidiTitle() {
            return midiTitle;
        }
        public void setMidiTitle(String midiTitle) {
            this.midiTitle = midiTitle;
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

        public Long getChords() {
            return Chords;
        }

        public void setChords(long chords) {
            Chords = chords;
        }


        public Long getMicroseconds() {
            return microseconds;
        }

        public void setMicroseconds(long microseconds) {
            this.microseconds = microseconds;
        }

        public Long getNotes() {
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

        public Long getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(long maxScore) {
            this.maxScore = maxScore;
        }

        public long getUnmultipliedScoreWithBRENotes() {
            return UnmultipliedScoreWithBRENotes;
        }

        public void setUnmultipliedScoreWithBRENotes(long unmultipliedScoreWithBRENotes) {
            UnmultipliedScoreWithBRENotes = unmultipliedScoreWithBRENotes;
        }

        public RockBandOptimalFillDelay getOptimalFillDelay() {
            return RockBandOptimalFillDelay.ToEnum(optimalFillDelay);
        }

        public void setOptimalFillDelay(RockBandOptimalFillDelay optimalFillDelay) {
            this.optimalFillDelay = RockBandOptimalFillDelay.ToInteger( optimalFillDelay );
            return;
        }

        public boolean isMaximumMultiplierNotReachedWarning() {
            return MaximumMultiplierNotReachedWarning;
        }

        public void setMaximumMultiplierNotReachedWarning(boolean maximumMultiplierNotReachedWarning) {
            MaximumMultiplierNotReachedWarning = maximumMultiplierNotReachedWarning;
        }

        public Long getStarCutoffOne() {
            return StarCutoffOne;
        }

        public void setStarCutoffOne(long starCutoffOne) {
            StarCutoffOne = starCutoffOne;
        }

        public Long getStarCutoffTwo() {
            return StarCutoffTwo;
        }

        public void setStarCutoffTwo(long starCutoffTwo) {
            StarCutoffTwo = starCutoffTwo;
        }

        public Long getStarCutoffThree() {
            return StarCutoffThree;
        }

        public void setStarCutoffThree(long starCutoffThree) {
            StarCutoffThree = starCutoffThree;
        }

        public Long getStarCutoffFour() {
            return StarCutoffFour;
        }

        public void setStarCutoffFour(long starCutoffFour) {
            StarCutoffFour = starCutoffFour;
        }

        public Long getStarCutoffFive() {
            return StarCutoffFive;
        }

        public void setStarCutoffFive(long starCutoffFive) {
            StarCutoffFive = starCutoffFive;
        }

        public Long getStarCutoffGold() {
            return StarCutoffGold;
        }

        public void setStarCutoffGold(long starCutoffGold) {
            StarCutoffGold = starCutoffGold;
        }

        public Long getStarCutoffGoldOld() {
            return StarCutoffGoldOld;
        }

        public void setStarCutoffGoldOld(long starCutoffGoldOld) {
            StarCutoffGoldOld = starCutoffGoldOld;
        }

        public Long getNormalOptimalScore() {
            return normalOptimalScore;
        }

        public void setNormalOptimalScore(Long normalOptimalScore) {
            this.normalOptimalScore = normalOptimalScore;
        }

        public Long getBreakneckOptimalScore() {
            return breakneckOptimalScore;
        }

        public void setBreakneckOptimalScore(Long breakneckOptimalScore) {
            this.breakneckOptimalScore = breakneckOptimalScore;
        }
    }
}
