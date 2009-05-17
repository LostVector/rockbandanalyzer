package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import java.util.Date;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import javax.jdo.annotations.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Blob;


@FetchGroup(
        name="full",
        members={ @Persistent(name="id"), @Persistent(name="_uploader"), @Persistent(name="_dateUploaded"),
                @Persistent(name="_originalFileName"), @Persistent(name="_file"), @Persistent(name="_md5"),
                @Persistent(name="needsReprocessing")
})
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RockBandSongRaw {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private User    _uploader;

    @Persistent
    private Date    _dateUploaded;

    @Persistent
    private String  _originalFileName;

//    @Persistent(serialized="true", defaultFetchGroup="true")     
    @Persistent
    private Blob  _file;

    @Persistent
    private String    _md5;

    @Persistent
    private Boolean     needsReprocessing;

//    @Persistent
//    private RockBandSongEmbedded _embedded;

//    @Persistent
//    private RockBandSongGenerated _generated;

    public RockBandSongRaw(User uploader, Date dateUploaded, String originalFileName, byte[] file) {
        _uploader = uploader;
        _dateUploaded = dateUploaded;
        _originalFileName = originalFileName;
        _file = new Blob( new byte[0] );
        _md5 = "";

//        _embedded = new RockBandSongEmbedded();
//        _generated = new RockBandSongGenerated();

        setFile( file );

        needsReprocessing = false;
        return;
    }

    public Boolean getNeedsReprocessing() {
        return needsReprocessing;
    }

    public void setNeedsReprocessing(Boolean needsReprocessing) {
        this.needsReprocessing = needsReprocessing;
    }

    public Long getId() {
        return id;
    }

    public User getUploader() {
        return _uploader;
    }

    public Date getDateUploaded() {
        return _dateUploaded;
    }

    public String getOriginalFileName() {
        return _originalFileName;
    }

    public byte[] getFile() {

        GZIPInputStream gzIn;
        ByteArrayInputStream baIn;
        ByteArrayOutputStream baOut;

        baIn = new ByteArrayInputStream( _file.getBytes() );
        baOut = new ByteArrayOutputStream();

        try {
            gzIn = new GZIPInputStream( baIn );

            for( int c = gzIn.read(); c != -1; c = gzIn.read() ) {
                baOut.write( c );
            }

            baOut.close();
        }
        catch( IOException ioex ) {
            return null;
        }

        return baOut.toByteArray();
    }

    public String getMD5() {
        return _md5;
    }

    public void setUploader(User uploader) {
        _uploader = uploader;
        return;
    }

    public void setDateUploaded(Date dateUploaded) {
        _dateUploaded = dateUploaded;
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
    public RockBandSongGenerated getGenerated() {
        return _generated;
    }

    public void setGenerated(RockBandSongGenerated generated) {
        _generated = generated;
    }

    public RockBandSongEmbedded getAssociated() {
        return _embedded;
    }

    public void setEmbedded(RockBandSongEmbedded embedded) {
        _embedded = embedded;
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
