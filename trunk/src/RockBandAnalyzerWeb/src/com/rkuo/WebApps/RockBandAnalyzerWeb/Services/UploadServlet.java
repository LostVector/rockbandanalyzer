package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import java.io.*;
import java.util.logging.Logger;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.rkuo.RockBand.*;
import com.rkuo.RockBand.Simulators.DrumsBaselineAnalysis;
import com.rkuo.RockBand.Simulators.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongEmbedded;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongGenerated;
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
        RockBandSongEmbedded    embedded;
        RockBandSongGenerated   generated;
        boolean br;

        response.setContentType("text/plain");

        doc = getXml( request );
        if( doc == null ) {
            return;
        }

        rawSong = getRawSong( doc );
        if( rawSong == null ) {
            return;
        }

        RockBandAnalyzerParams rbap;
        DrumsFullAnalysis dfa;

        rbap = new RockBandAnalyzerParams();
        dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(rawSong.getFile()), rbap);
        if( dfa == null ) {
            return;
        }

        embedded = new RockBandSongEmbedded();
        ReadEmbeddedData(doc, embedded);
        embedded.setMidiTitle( dfa.dba.MidiTitle );

        generated = new RockBandSongGenerated();
        generated.setMicroseconds( dfa.dba.Microseconds );
        generated.setNotes( dfa.dba.Notes );
        generated.setSolos( dfa.dba.Solos );
        generated.setBigRockEnding( dfa.dba.BigRockEnding );

        generated.setGlitched( false );
        if( dfa.dba.GlitchedChords.size() > 0 ) {
            generated.setGlitched( true );
        }

        generated.setBreakneckOptimal( false );
        if( dfa.RB2PathOverallOptimal.FillDelay != RockBandConstants.FillDelayRB2Expert ) {
            generated.setBreakneckOptimal( true );
        }

        generated.setGoldStarrable( true );
        if( dfa.RB2PathOverallOptimal.Score < dfa.dba.StarCutoffGold ) {
            generated.setGoldStarrable( false );
        }

        generated.setMaxScore( dfa.RB2PathOverallOptimal.Score );

        br = DataAccess.TryWritingSong( rawSong, embedded, generated );
        if( br == false ) {
            response.getWriter().format("%s already exists in the database.", rawSong.getOriginalFileName());
            return;
        }
        
        response.getWriter().format("%s has been added to the database.", rawSong.getOriginalFileName());
        return;
    } // doPost

    public String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch( TransformerException ex ) {
            ex.printStackTrace();
            return null;
        }
    }

    protected void ReadEmbeddedData(Document doc, RockBandSongEmbedded embedded) {

        String sValue;

        sValue = TryReadingEmbeddedValue(doc, "SongName");
        if( sValue != null ) {
            embedded.setTitle(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Artist");
        if( sValue != null ) {
            embedded.setArtist(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Album");
        if( sValue != null ) {
            embedded.setAlbum(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "Year");
        if( sValue != null ) {
            Date dateReleased;
            GregorianCalendar cal;

            cal = new GregorianCalendar();
            cal.set(Integer.parseInt(sValue), 1, 1);
            dateReleased = cal.getTime();
            embedded.setDateReleased(dateReleased);
        }

        sValue = TryReadingEmbeddedValue(doc, "Genre");
        if( sValue != null ) {
            embedded.setGenre(sValue);
        }

        sValue = TryReadingEmbeddedValue(doc, "GuitarDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.values()[Integer.parseInt(sValue)];
            embedded.setGuitarDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "BassDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.values()[Integer.parseInt(sValue)];
            embedded.setBassDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "DrumsDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.values()[Integer.parseInt(sValue)];
            embedded.setDrumsDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "VocalsDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.values()[Integer.parseInt(sValue)];
            embedded.setVocalsDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "BandDifficulty");
        if( sValue != null ) {
            RockBandInstrumentDifficulty    rbid;

            rbid = RockBandInstrumentDifficulty.values()[Integer.parseInt(sValue)];
            embedded.setBandDifficulty( rbid );
        }

        sValue = TryReadingEmbeddedValue(doc, "Location");
        if( sValue != null ) {
            if( sValue.compareTo("rb1") == 0 ) {
                embedded.setLocation(RockBandLocation.RockBandOne);
            }
            else if( sValue.compareTo("rb2") == 0 ) {
                embedded.setLocation(RockBandLocation.RockBandTwo);
            }
            else if( sValue.compareTo("rbdlc") == 0 ) {
                embedded.setLocation(RockBandLocation.Downloaded);
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