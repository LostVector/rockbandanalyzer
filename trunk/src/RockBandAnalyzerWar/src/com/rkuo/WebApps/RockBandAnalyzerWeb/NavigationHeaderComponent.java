package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponentBase;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 12:23:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class NavigationHeaderComponent extends UIComponentBase {
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer;
//        HttpServletRequest request;

        writer = context.getResponseWriter();
//        request = (HttpServletRequest) context.getExternalContext().getRequest();

        writer.startElement( "table", this );
        writer.writeAttribute( "width", "960px", null );
        writer.startElement( "tr", this );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/", null );
        writer.writeText( "Home", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/browse.jsf", null );
        writer.writeText( "Browse songs", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/submit.jsf", null );
        writer.writeText( "Submit a new song", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.startElement( "a", null );
        writer.writeAttribute( "href", "/source.jsf", null );
        writer.writeText( "Source code", null );
        writer.endElement( "a" );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "center", null );
        writer.writeText( "Search", null );
        writer.startElement( "input", null );
        writer.writeAttribute( "id", "searchInputText", null );
        writer.writeAttribute( "type", "text", null );
        writer.writeAttribute( "value", "Search...", null );
        writer.writeAttribute( "onselect", "onSearchSelect()", null );
        writer.endElement( "input" );
        writer.endElement( "td" );

        writer.endElement( "tr" );
        writer.endElement( "table" );

        writer.startElement( "hr", this );
        writer.endElement( "hr" );
        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }
}
