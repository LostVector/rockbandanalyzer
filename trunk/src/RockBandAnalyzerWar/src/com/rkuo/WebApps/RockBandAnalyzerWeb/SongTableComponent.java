
package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 12:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongTableComponent extends UIComponentBase {

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer;
        HttpServletRequest request;
        String              sortParam;

        writer = context.getResponseWriter();

        request = (HttpServletRequest) context.getExternalContext().getRequest();
        sortParam = request.getParameter("sort");
        if( sortParam.compareTo("difficulty") == 0 ) {
            String  instrumentParam;

            instrumentParam = request.getParameter("instrument");
            if( instrumentParam.compareTo( "guitar" ) == 0 ) {

            }
            else if( instrumentParam.compareTo( "bass" ) == 0 ) {

            }
            else if( instrumentParam.compareTo( "drums" ) == 0 ) {

            }
            else if( instrumentParam.compareTo( "vocals" ) == 0 ) {

            }
            else if( instrumentParam.compareTo( "band" ) == 0 ) {

            }
            else {
                // Default to guitar
            }
        }

        writer.startElement( "hr", this );
        writer.endElement( "hr" );

        WriteHeaderCells( writer );
        WriteSongs( writer );
        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }

    private void WriteHeaderCells( ResponseWriter w ) throws IOException {

        w.startElement( "table", this );
        w.writeAttribute( "width", "960px", null );

        w.startElement( "tr", this );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=difficulty", null );
        w.writeText( "Difficulty", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=song", null );
        w.writeText( "Alphabetic By Song", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=band", null );
        w.writeText( "Alphabetic By Band", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=decade", null );
        w.writeText( "Decade", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=location", null );
        w.writeText( "Location", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.startElement( "a", null );
        w.writeAttribute( "href", "/browse.jsf?sort=rbreleasedate", null );
        w.writeText( "Release Date", null );
        w.endElement( "a" );
        w.endElement( "td" );

        w.endElement( "tr" );

        w.endElement( "table" );

        return;
    }

    private void WriteSongs( ResponseWriter w ) throws IOException {

        List<RockBandSong> songs;
        String              outText;

        songs = DataAccess.GetSongs();
        if( songs == null ) {
            return;
        }

        w.startElement( "table", this );
        w.writeAttribute( "width", "960px", null );

        w.startElement( "tr", this );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.writeText( "MIDI title", null );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        outText = String.format( "%d songs", songs.size() );
        w.writeText( outText, null );
        w.endElement( "td" );

        w.endElement( "tr" );

        for( RockBandSong s : songs ) {
            w.startElement( "tr", this );

            w.startElement( "td", this );
            w.writeAttribute( "align", "left", null );
            w.startElement( "a", null );
            w.writeAttribute( "href", "/song.jsf?id=" + s.getId().toString(), null );
            w.writeText( s.getMidiTitle(), null );
            w.endElement( "a" );
            w.endElement( "td" );

            w.startElement( "td", this );
            w.endElement( "td" );
            w.endElement( "tr" );
        }
        
        w.endElement( "table" );
    }
}
