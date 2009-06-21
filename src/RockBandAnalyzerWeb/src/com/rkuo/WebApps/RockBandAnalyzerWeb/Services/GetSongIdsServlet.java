package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandAdvancedSort;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetSongIdsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder   sb;
        String          sLast;
        List<RockBandSong> songs;

        sLast = request.getParameter("last");
        if( sLast == null ) {
            songs = DataAccess.GetSongs( RockBandAdvancedSort.Song );
        }
        else {
            Long            lastUpdatedTime;

            lastUpdatedTime = Long.parseLong( sLast );
            songs = DataAccess.GetSongsByLastUpdate( lastUpdatedTime );
        }

        sb = new StringBuilder();

        response.setContentType( "text/xml" );
        response.setCharacterEncoding( "UTF-8" );

        // Not a fan of writing my own xml, but Google hasn't whitelisted the XML serializers in Java
        sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append( "<songs>\n");
        for( RockBandSong s : songs ) {
            String  escapedString;

            escapedString = s.getAssociated().getTitle();
            escapedString = escapedString.replaceAll("&", "&amp;");
            escapedString = escapedString.replaceAll("<", "&lt;");
            escapedString = escapedString.replaceAll(">", "&gt;");

            sb.append( "<song>" );
            sb.append( "<songId>" );
            sb.append( s.getId() );
            sb.append( "</songId>\n" );
            sb.append( "<title>" );
            sb.append( escapedString );
            sb.append( "</title>\n" );
            sb.append( "</song>" );
        }
        sb.append( "</songs>\n");

        response.getWriter().format( sb.toString() );
        return;
    } // doGet
}
