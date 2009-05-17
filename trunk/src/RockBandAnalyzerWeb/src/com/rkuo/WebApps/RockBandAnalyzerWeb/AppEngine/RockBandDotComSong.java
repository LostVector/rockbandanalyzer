package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 12, 2009
 * Time: 12:18:10 AM
 * To change this template use File | Settings | File Templates.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandDotComSong {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String midiTitle;

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

    // when the last attempt was to update this song
    @Persistent
    private Long lastAttempted;

    // when the last successful attempt was to update this song
    @Persistent
    private Long lastUpdated;

    public RockBandDotComSong() {
        // associated ... should not change over time
        midiTitle = "";

        artist = "";
        title = "";
        album = "";
        genre = "";
        location = RockBandLocation.ToInteger(RockBandLocation.Downloaded);
        dateReleased = new Date(0);
        datePublished = new Date(0);
        guitarDifficulty = -1;
        bassDifficulty = -1;
        drumsDifficulty = -1;
        vocalsDifficulty = -1;
        bandDifficulty = -1;
        isCover = false;

        lastAttempted = 0L;
        lastUpdated = 0L;
        return;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMidiTitle() {
        return midiTitle;
    }

    public void setMidiTitle(String midiTitle) {
        this.midiTitle = midiTitle;
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
        return RockBandLocation.ToEnum(this.location);
    }

    public Date getDatePublished() {
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
        this.location = RockBandLocation.ToInteger(location);
        return;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
        return;
    }

    public void setGuitarDifficulty(RockBandInstrumentDifficulty guitarDifficulty) {
        this.guitarDifficulty = RockBandInstrumentDifficulty.ToInteger(guitarDifficulty);
    }

    public void setBassDifficulty(RockBandInstrumentDifficulty bassDifficulty) {
        this.bassDifficulty = RockBandInstrumentDifficulty.ToInteger(bassDifficulty);
    }

    public void setDrumsDifficulty(RockBandInstrumentDifficulty drumsDifficulty) {
        this.drumsDifficulty = RockBandInstrumentDifficulty.ToInteger(drumsDifficulty);
    }

    public void setVocalsDifficulty(RockBandInstrumentDifficulty vocalsDifficulty) {
        this.vocalsDifficulty = RockBandInstrumentDifficulty.ToInteger(vocalsDifficulty);
    }

    public void setBandDifficulty(RockBandInstrumentDifficulty bandDifficulty) {
        this.bandDifficulty = RockBandInstrumentDifficulty.ToInteger(bandDifficulty);
    }

    public void setCover(Boolean isCover) {
        this.isCover = isCover;
        return;
    }

    public Long getLastAttempted() {
        return lastAttempted;
    }

    public void setLastAttempted(Long lastAttempted) {
        this.lastAttempted = lastAttempted;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
