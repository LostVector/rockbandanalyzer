package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import java.io.*;
import java.util.logging.Logger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.rkuo.RockBand.*;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import com.rkuo.util.Base64;
import com.rkuo.util.Misc;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class UploadServlet extends HttpServlet {

    private static final int MaxUploadBytes = 1024 * 1024; // 1 MB

    private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

    // http://yeshvanthni.blogspot.com/2008/10/read-posted-xml-data-in-servlet.html
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Document doc;
        RockBandSongRaw rawSong;
        RockBandSong   song;
        RockBandAnalyzerParams rbap;
        DrumsFullAnalysis dfa;
        boolean br;

        rbap = new RockBandAnalyzerParams();
        song = new RockBandSong();

        response.setContentType("text/plain");

        doc = Misc.ToDocument( request.getInputStream() );
        if( doc == null ) {
            return;
        }

        rawSong = getRawSong( doc );
        if( rawSong == null ) {
            return;
        }

        dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(rawSong.getFile()), rbap);
        if( dfa == null ) {
            return;
        }

        ReadEmbeddedData(doc, song);

        DataAccess.ProcessSong( song, dfa );

        br = DataAccess.TryWritingSong( rawSong, song );
        if( br == false ) {
            response.getWriter().format("%s already exists in the database.", rawSong.getOriginalFileName());
            return;
        }

        RockBandSongRaw newRawSong = DataAccess.GetRawSongById( rawSong.getId() );
        response.getWriter().format("%s has been added to the database.", rawSong.getOriginalFileName());
        return;
    } // doPost

    protected void ReadEmbeddedData(Document doc, RockBandSong song) {

        String sValue;

        sValue = TryReadingEmbeddedValue(doc, "SongName");
        if( sValue != null ) {
            song.getAssociated().setTitle(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Artist");
        if( sValue != null ) {
            song.getAssociated().setArtist(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Album");
        if( sValue != null ) {
            song.getAssociated().setAlbum(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Year");
        if( sValue != null ) {
            Calendar cal;
            cal = new GregorianCalendar(Integer.parseInt(sValue), 0, 1, 0, 0);
            cal.setTimeZone( TimeZone.getTimeZone("GMT") );
            song.getAssociated().setDateReleased(cal.getTime());
        }

        sValue = TryReadingEmbeddedValue(doc, "Genre");
        if( sValue != null ) {
            song.getAssociated().setGenre(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "GuitarDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum( Integer.parseInt(sValue) );
            song.getAssociated().setGuitarDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "BassDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum( Integer.parseInt(sValue) );
            song.getAssociated().setBassDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "DrumsDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum( Integer.parseInt(sValue) );
            song.getAssociated().setDrumsDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "VocalsDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum( Integer.parseInt(sValue) );
            song.getAssociated().setVocalsDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "BandDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum( Integer.parseInt(sValue) );
            song.getAssociated().setBandDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "Location");
        if( sValue != null ) {
            if( sValue.compareTo("rb1") == 0 ) {
                song.getAssociated().setLocation(RockBandLocation.RockBandOne);
            }
            else if( sValue.compareTo("rb2") == 0 ) {
                song.getAssociated().setLocation(RockBandLocation.RockBandTwo);
            }
            else if( sValue.compareTo("rbdlc") == 0 ) {
                song.getAssociated().setLocation(RockBandLocation.Downloaded);
            }
        }

        return;
    }

    protected String TryReadingEmbeddedValue(Document doc, String sXmlName) {
        NodeList nodes;
        Element e;
        String sValue;

        sValue = null;

        nodes = doc.getElementsByTagName( sXmlName );
        if( nodes.getLength() > 0 ) {
            e = (Element)nodes.item(0);
            sValue = e.getFirstChild().getNodeValue();
        }

        return sValue;
    }

    protected Document getXml( HttpServletRequest req ) {

        Document    doc;
        DocumentBuilder db;
        DocumentBuilderFactory dbf;

        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException pcex ) {
            return null;
        }

        try {
            doc = db.parse(req.getInputStream());
        }
        catch( SAXException saxex ) {
            return null;
        }
        catch( IOException ioex ) {
            return null;
        }

        return doc;
    }

    protected RockBandSongRaw getRawSong( Document doc ) {

        RockBandSongRaw rawSong;
        byte[] fileBytes;

        NodeList nodeList;
        Element e;

        String originalFileName;
        String b64FileData;

        nodeList = doc.getElementsByTagName("OriginalFileName");
        e = (Element)nodeList.item(0);
        originalFileName = e.getFirstChild().getNodeValue();

        nodeList = doc.getElementsByTagName("FileData");
        e = (Element)nodeList.item(0);
        b64FileData = e.getFirstChild().getNodeValue();

        try {
            fileBytes = Base64.decode(b64FileData);
        }
        catch( IOException ioex ) {
            return null;
        }

        UserService userService;
        User user;

        userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();

        rawSong = new RockBandSongRaw(user, new Date(), originalFileName, fileBytes);

        return rawSong;
    }
}
