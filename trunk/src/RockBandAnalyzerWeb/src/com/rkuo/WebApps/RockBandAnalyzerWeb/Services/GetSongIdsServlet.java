package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import java.io.*;
import java.util.List;
import javax.servlet.http.*;
import javax.servlet.ServletException;

import com.rkuo.RockBand.*;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;

public class GetSongIdsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder   sb;
        List<Long> songIds;

        songIds = DataAccess.GetRawSongIds();

        sb = new StringBuilder();

        response.setContentType( "text/xml" );

        // Not a fan of writing my own xml, but Google hasn't whitelisted the XML serializers in Java
        sb.append( "<?xml version=\"1.0\"?>\n");
        sb.append( "<songIds>\n");
        for( Long songId : songIds ) {
            sb.append( "<songId>" );
            sb.append( songId.toString() );
            sb.append( "</songId>\n" );
        }
        sb.append( "</songIds>\n");

        response.getWriter().format( sb.toString() );
        return;
    } // doPost
}
