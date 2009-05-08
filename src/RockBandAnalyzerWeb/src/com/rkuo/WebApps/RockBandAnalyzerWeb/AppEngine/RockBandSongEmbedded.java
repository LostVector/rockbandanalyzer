package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import java.util.Date;
import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Key;
import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.RockBand.RockBandInstrument;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandSongEmbedded {

    @PrimaryKey
//    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
//    private Key    _key;
    private String    id;

//    @Persistent(mappedBy = "_embedded")
//    private RockBandSongRaw rbSong;

    @Persistent
    private String  midiTitle;

    @Persistent
    private String _artist;

    @Persistent
    private String _album;
    
    @Persistent
    private String _title;

    @Persistent
    private String _genre;

    @Persistent
    private Integer _location;

    @Persistent
    private int     _guitarDifficulty;

    @Persistent
    private int     _bassDifficulty;

    @Persistent
    private int     _drumsDifficulty;

    @Persistent
    private int     _vocalsDifficulty;

    @Persistent
    private int     _bandDifficulty;

//    @Persistent
    @NotPersistent
    private int     _isCover;

    // when the song was released (in traditional channels)
    @Persistent
    private Date _dateReleased;

    // when the song was released on Rock Band
    @Persistent
    private Date _datePublished;

    public RockBandSongEmbedded() {
        midiTitle = "";
        _artist = "";
        _title = "";
        _album = "";
        _genre = "";
        _dateReleased = new Date();
        _datePublished = new Date();
        _guitarDifficulty = -1;
        _bassDifficulty = -1;
        _drumsDifficulty = -1;
        _vocalsDifficulty = -1;
        _bandDifficulty = -1;
        _isCover = 0;
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

    public RockBandSongRaw getRawSong() {
        return this.rbSong;
    }
 */

    public String getMidiTitle() {
        return midiTitle;
    }

    public String getArtist() {
        return _artist;
    }

    public String getAlbum() {
        return _album;
    }

    public String getTitle() {
        return _title;
    }

    public String getGenre() {
        return _genre;
    }

    public Date getDateReleased() {
        return _dateReleased;
    }

    public RockBandLocation getLocation() {
        return RockBandLocation.values()[_location];
    }

    public Date getDatePublished() {
        return _datePublished;
    }

    public int getDifficulty( RockBandInstrumentDifficultyCategory rbidc ) {
        if( rbidc == RockBandInstrumentDifficultyCategory.Guitar ) {
            return _guitarDifficulty;
        }
        else if( rbidc == RockBandInstrumentDifficultyCategory.Bass ) {
            return _bassDifficulty;
        }
        else if( rbidc == RockBandInstrumentDifficultyCategory.Drums ) {
            return _drumsDifficulty;
        }
        else if( rbidc == RockBandInstrumentDifficultyCategory.Vocals ) {
            return _vocalsDifficulty;
        }

        return _bandDifficulty;
    }

    public RockBandInstrumentDifficulty getGuitarDifficulty() {
        return RockBandInstrumentDifficulty.values()[_guitarDifficulty];
    }

    public RockBandInstrumentDifficulty getBassDifficulty() {
        return RockBandInstrumentDifficulty.values()[_bassDifficulty];
    }

    public RockBandInstrumentDifficulty getDrumsDifficulty() {
        return RockBandInstrumentDifficulty.values()[_drumsDifficulty];
    }

    public RockBandInstrumentDifficulty getVocalsDifficulty() {
        return RockBandInstrumentDifficulty.values()[_vocalsDifficulty];
    }

    public RockBandInstrumentDifficulty getBandDifficulty() {
        return RockBandInstrumentDifficulty.values()[_bandDifficulty];
    }

    public boolean getCover() {
        if( _isCover == 0 ) {
            return false;
        }

        return true;
    }

    public void setMidiTitle(String midiTitle) {
        this.midiTitle = midiTitle;
    }

    public void setArtist( String artist ) {
        _artist = artist;
        return;
    }

    public void setAlbum( String album ) {
        _album = album;
        return;
    }

    public void setTitle( String title ) {
        _title = title;
        return;
    }

    public void setGenre( String genre ) {
        _genre = genre;
        return;
    }

    public void setDateReleased( Date dateReleased ) {
        _dateReleased = dateReleased;
        return;
    }

    public void setLocation(RockBandLocation location) {
        _location = location.ordinal();
        return;
    }

    public void setDatePublished( Date datePublished ) {
        _datePublished = datePublished;
        return;
    }

    public void setGuitarDifficulty(RockBandInstrumentDifficulty guitarDifficulty) {
        _guitarDifficulty = guitarDifficulty.ordinal();
    }

    public void setBassDifficulty(RockBandInstrumentDifficulty bassDifficulty) {
        _bassDifficulty = bassDifficulty.ordinal();
    }

    public void setDrumsDifficulty(RockBandInstrumentDifficulty drumsDifficulty) {
        _drumsDifficulty = drumsDifficulty.ordinal();
    }

    public void setVocalsDifficulty(RockBandInstrumentDifficulty vocalsDifficulty) {
        _vocalsDifficulty = vocalsDifficulty.ordinal();
    }

    public void setBandDifficulty(RockBandInstrumentDifficulty bandDifficulty) {
        _bandDifficulty = bandDifficulty.ordinal();
    }

    public void setCover(boolean isCover) {

        _isCover = 0;
        if( isCover == true ) {
            _isCover = 1;
        }

        return;
    }

    protected boolean IsValidDifficulty( int difficulty ) {
        if( difficulty > 7 ) {
            return false;
        }

        if( difficulty < -1 ) {
            return false;
        }

        return true;
    }
}