package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

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

    @Persistent
    private Blob  _file;

    @Persistent
    private String    _md5;

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

        return;
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
        return _file.getBytes();
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

    public RockBandSongEmbedded getEmbedded() {
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
