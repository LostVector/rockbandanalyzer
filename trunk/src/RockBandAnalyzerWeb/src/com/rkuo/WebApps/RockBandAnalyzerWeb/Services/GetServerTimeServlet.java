package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetServerTimeServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder       sb;
        String              name;

        name = "ServerTime";

        sb = new StringBuilder();

        response.setContentType( "text/xml" );
        response.setCharacterEncoding( "UTF-8" );

        // Not a fan of writing my own xml, but Google hasn't whitelisted the XML serializers in Java
        sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append( "<p>\n");

        sb.append( "<n>" );
        sb.append( name );
        sb.append( "</n>\n" );
        sb.append( "<v>" );
        sb.append( Long.valueOf(System.currentTimeMillis()).toString() );
        sb.append( "</v>\n" );

        sb.append( "</p>\n");

        response.getWriter().format( sb.toString() );
        return;
    } // doGet
}
