
package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 12:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class FooterComponent extends UIComponentBase {

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer;
//        HttpServletRequest request;

        writer = context.getResponseWriter();
//        request = (HttpServletRequest) context.getExternalContext().getRequest();

        writer.startElement( "hr", this );
        writer.endElement( "hr" );

        writer.startElement( "table", this );
        writer.writeAttribute( "width", "960px", null );

        // Copyright row
        writer.startElement( "tr", this );
        writer.startElement( "td", this );
        writer.writeAttribute( "colspan", "2", null );
        writer.writeAttribute( "align", "center", null );
        writer.writeText( "Copyright 2009 rkuo.com", null );
        writer.endElement( "td" );
        writer.endElement( "tr" );

        writer.startElement( "tr", this );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/about.jsf", null );
        writer.writeText( "About", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/terms.jsf", null );
        writer.writeText( "Terms of use", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.endElement( "tr" );
        writer.endElement( "table" );

        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }
}
