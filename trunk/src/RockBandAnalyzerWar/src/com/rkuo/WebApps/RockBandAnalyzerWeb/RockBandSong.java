package com.rkuo.WebApps.RockBandAnalyzerWeb;

import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Blob;
import com.rkuo.RockBand.RockBandLocation;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandSong {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String _midiTitle;

    @Persistent
    private User    _uploader;

    @Persistent
    private String _artist;

    @Persistent
    private String _title;

    // when the song was released (in traditional channels)
    @Persistent
    private Date _dateReleased;

    @Persistent
    private Integer _location;

    // when the song was released on Rock Band
    @Persistent
    private Date _datePublished;

    @Persistent
    private String  _originalFileName;

    @Persistent
    private Blob  _file;

    @Persistent
//    private byte[]  _md5;
    private String    _md5;

    public RockBandSong(User uploader, String midiTitle, String originalFileName, byte[] file) {
        _uploader = uploader;
        _midiTitle = midiTitle;
        _originalFileName = originalFileName;
        _file = new Blob( new byte[0] );
        _md5 = "";

        setFile( file );

        _artist = "";
        _title = "";
        _dateReleased = new Date();
        _dateReleased = new Date();
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

    public String getMidiTitle() {
        return _midiTitle;
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

    public RockBandLocation getLocation() {
        return RockBandLocation.values()[_location];
    }

    public Date getDatePublished() {
        return _datePublished;
    }

    public String getOriginalFileName() {
        return _originalFileName;
    }

    public byte[] getFile() {
        return _file.getBytes();
    }

    public String getMD5() {
        return _md5;
    }

    public void setUploader(User uploader) {
        _uploader = uploader;
        return;
    }

    public void setMidiTitle(String midiTitle) {
        this._midiTitle = midiTitle;
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

    public void setLocation(RockBandLocation location) {
        _location = location.ordinal();
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

        ByteArrayOutputStream baOut;
        GZIPOutputStream gzOut;

        try {
            baOut = new ByteArrayOutputStream();
            gzOut = new GZIPOutputStream( baOut );
            gzOut.write( file );
            gzOut.finish();

            _file = new Blob( baOut.toByteArray() );
            _md5 = GetMD5( file );
        }
        catch( IOException ioex ) {
            _file = new Blob( new byte[0] );
            _md5 = "";
        }

        return;
    }

    public void setMD5( String md5 ) {
        _md5 = md5;
        return;
    }

/*
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
 */
    private static String GetMD5( byte[] bytes ) {
        String hashword = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update( bytes );
            BigInteger hash = new BigInteger(1, md5.digest());
            hashword = hash.toString(16);

        }
        catch (NoSuchAlgorithmException nsae) {

        }
    
        return pad(hashword,32,'0');
    }

    private static String pad(String s, int length, char pad) {
        StringBuffer buffer = new StringBuffer(s);
        while (buffer.length() < length) {
            buffer.insert(0, pad);
        }
        return buffer.toString();
    }
}