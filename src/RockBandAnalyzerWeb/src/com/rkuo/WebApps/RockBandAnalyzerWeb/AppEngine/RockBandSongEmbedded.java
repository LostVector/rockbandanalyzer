package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.rkuo.RockBand.RockBandLocation;

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

    @Persistent
    private boolean     _isCover;

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
        _isCover = false;
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

    public int getGuitarDifficulty() {
        return _guitarDifficulty;
    }

    public int getBassDifficulty() {
        return _bassDifficulty;
    }

    public int getDrumsDifficulty() {
        return _drumsDifficulty;
    }

    public int getVocalsDifficulty() {
        return _vocalsDifficulty;
    }

    public int getBandDifficulty() {
        return _bandDifficulty;
    }

    public boolean getCover() {
        return _isCover;
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

    public void setGuitarDifficulty(int guitarDifficulty) {
        _guitarDifficulty = guitarDifficulty;
    }

    public void setBassDifficulty(int bassDifficulty) {
        _bassDifficulty = bassDifficulty;
    }

    public void setDrumsDifficulty(int drumsDifficulty) {
        _drumsDifficulty = drumsDifficulty;
    }

    public void setVocalsDifficulty(int vocalsDifficulty) {
        _vocalsDifficulty = vocalsDifficulty;
    }

    public void setBandDifficulty(int bandDifficulty) {
        _bandDifficulty = bandDifficulty;
    }

    public void setCover(boolean isCover) {
        _isCover = isCover;
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