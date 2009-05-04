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
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.Base64;
import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;
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
        DocumentBuilder db;
        DocumentBuilderFactory dbf;
        byte[] fileBytes;

        response.setContentType("text/plain");

        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException pcex ) {
            return;
        }

        try {
            doc = db.parse(request.getInputStream());
        }
        catch( SAXException saxex ) {
            return;
        }

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

        fileBytes = Base64.decode(b64FileData);

        RockBandAnalyzerParams rbap;
        DrumsBaselineData dbd;

        rbap = new RockBandAnalyzerParams();
        dbd = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(fileBytes), rbap);
        if( dbd == null ) {
            return;
        }

        UserService userService;
        User user;
        RockBandSongRaw song;
        boolean br;

        userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();

        song = new RockBandSongRaw(user, new Date(), originalFileName, fileBytes);

        RockBandSongEmbedded    embedded;
        RockBandSongGenerated   generated;

        embedded = new RockBandSongEmbedded();

        ReadEmbeddedData(doc, embedded);
        embedded.setMidiTitle( dbd.MidiTitle );

        generated = new RockBandSongGenerated();
        generated.setMicroseconds( dbd.Microseconds );

        br = DataAccess.SongExists(song.getMD5());
        if( br == false ) {
            response.getWriter().format("%s has been added to the database.", originalFileName);

            Long    id;

            id = DataAccess.WriteRaw(song);

            embedded.setId( id );
            DataAccess.WriteEmbedded( embedded );

            generated.setId( id );
            DataAccess.WriteGenerated( generated );
            return;
        }

        response.getWriter().format("%s already exists in the database.", originalFileName);
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
            embedded.setGuitarDifficulty(Integer.parseInt(sValue));
        }

        sValue = TryReadingEmbeddedValue(doc, "BassDifficulty");
        if( sValue != null ) {
            embedded.setBassDifficulty(Integer.parseInt(sValue));
        }

        sValue = TryReadingEmbeddedValue(doc, "DrumsDifficulty");
        if( sValue != null ) {
            embedded.setDrumsDifficulty(Integer.parseInt(sValue));
        }

        sValue = TryReadingEmbeddedValue(doc, "VocalsDifficulty");
        if( sValue != null ) {
            embedded.setVocalsDifficulty(Integer.parseInt(sValue));
        }

        sValue = TryReadingEmbeddedValue(doc, "BandDifficulty");
        if( sValue != null ) {
            embedded.setBandDifficulty(Integer.parseInt(sValue));
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
}