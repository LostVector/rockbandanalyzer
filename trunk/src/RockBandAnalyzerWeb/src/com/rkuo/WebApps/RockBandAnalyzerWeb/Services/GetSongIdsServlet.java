package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import java.io.*;
import java.util.logging.Logger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
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

public class GetSongIdsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder   sb;
        List<RockBandSongEmbedded> songs;

        songs = DataAccess.GetSongs( RockBandSort.Song );

        sb = new StringBuilder();

        response.setContentType( "text/xml" );

        // Not a fan of writing my own xml, but Google hasn't whitelisted the XML serializers in Java
        sb.append( "<?xml version=\"1.0\"?>\n");
        sb.append( "<songIds>\n");
        for( RockBandSongEmbedded song : songs ) {
            sb.append( "<songId>" );
            sb.append( song.getId().toString() );
            sb.append( "</songId>\n" );
        }
        sb.append( "</songIds>\n");
        
        return;
    } // doPost
}