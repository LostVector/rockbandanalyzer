package com.rkuo.WebApps.RockBandAnalyzerWeb;

import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandSong {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private User    _uploader;

    @Persistent
    private String _artist;

    @Persistent
    private String _title;

    // when the song was released (in traditional channels)
    @Persistent
    private Date _dateReleased;

    // when the song was released on Rock Band
    @Persistent
    private Date _datePublished;

    @Persistent
    private String  _originalFileName;

    @Persistent
    private byte[]  _file;

    @Persistent
    private byte[]  _md5;

    public RockBandSong(User uploader, String originalFileName, byte[] file) {
        _uploader = uploader;
        _originalFileName = originalFileName;
        _file = file.clone();
        _md5 = GetMD5( _file );
/*
        _artist = artist;
        _title = title;
        _dateReleased = dateReleased;
        _dateReleased = datePublished;
 */
        return;
    }

    public Long getId() {
        return id;
    }

    public User getUploader() {
        return _uploader;
    }

    public String getArtist() {
        return _artist;
    }

    public String getTitle() {
        return _title;
    }

    public Date getDateReleased() {
        return _dateReleased;
    }

    public Date getDatePublished() {
        return _datePublished;
    }

    public String getOriginalFileName() {
        return _originalFileName;
    }

    public byte[] getFile() {
        return _file;
    }

    public byte[] getMD5() {
        return _md5;
    }

    public void setUploader(User uploader) {
        _uploader = uploader;
        return;
    }

    public void setArtist( String artist ) {
        _artist = artist;
        return;
    }

    public void setTitle( String title ) {
        _title = title;
        return;
    }

    public void setDateReleased( Date dateReleased ) {
        _dateReleased = dateReleased;
        return;
    }

    public void setDatePublished( Date datePublished ) {
        _datePublished = datePublished;
        return;
    }

    public void setOriginalFileName( String originalFileName ) {
        _originalFileName = originalFileName;
        return;
    }

    public void setFile( byte[] file ) {
        _file = file.clone();
        _md5 = GetMD5( _file );
        return;
    }

    private static byte[] GetMD5( byte[] file ) {

        byte[] md5hash;

        md5hash = new byte[32];

        try {
            MessageDigest md;

            md = MessageDigest.getInstance("MD5");
            md.update( file );

            md5hash = md.digest();
        }
        catch( NoSuchAlgorithmException nsaex ) {
            // do nothing
        }

        return md5hash;
    }
}