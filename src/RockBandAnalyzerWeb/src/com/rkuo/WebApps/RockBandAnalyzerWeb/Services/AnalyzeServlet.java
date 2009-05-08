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

public class AnalyzeServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Document    doc;
        Long        songId;

        doc = getXml( request );
        if( doc == null ) {
            return;
        }

        songId = getSongId( doc );
        if( songId == null ) {
            return;
        }

        return;
    } // doPost

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

    protected Long getSongId( Document doc ) {

        Long    songId;

        NodeList nodeList;
        Element e;

        String sValue;

        nodeList = doc.getElementsByTagName("SongId");
        e = (Element)nodeList.item(0);
        sValue = e.getFirstChild().getNodeValue();
        songId = Long.parseLong( sValue );

        return songId;
    }
}