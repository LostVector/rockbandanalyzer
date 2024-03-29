
package com.rkuo.WebApps.RockBandAnalyzerWeb;

import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.RockBandPrint;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;

import javax.faces.component.UIComponentBase;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 12:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongDetailComponent extends UIComponentBase {

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer;
        HttpServletRequest req;
        DrumsFullAnalysis dfa;

        writer = context.getResponseWriter();
        req = (HttpServletRequest) context.getExternalContext().getRequest();

        RockBandAnalyzerParams  rbap;
        String                  sId;
        long                    id;
        GZIPInputStream gzIn;
        ByteArrayInputStream    baIn;
        ByteArrayOutputStream   baOut;
        RockBandSong            rbSong;

        sId = req.getParameter( "id" );
        id = Long.parseLong( sId );

        rbSong = DataAccess.GetSongById( id );

        baIn = new ByteArrayInputStream( rbSong.getFile() );
        baOut = new ByteArrayOutputStream();
        gzIn = new GZIPInputStream( baIn );

        for( int c = gzIn.read(); c != -1; c = gzIn.read() ) {
            baOut.write( c );
        }

        baOut.close();

        rbap = new RockBandAnalyzerParams();
        dfa = RockBandAnalyzer.AnalyzeStream( null, new ByteArrayInputStream(baOut.toByteArray()), rbap );

        WriteSongDetail( writer, dfa );
        return;
    }

    private  void WriteSongDetail( ResponseWriter w, DrumsFullAnalysis dfa ) throws IOException {

        String  outText;

        w.startElement( "table", this );
        w.writeAttribute( "width", "960px", null );

        // Copyright row
        w.startElement( "tr", this );
        w.startElement( "td", this );
        w.writeAttribute( "colspan", "2", null );
        w.writeAttribute( "align", "center", null );
        w.writeText( dfa.dba.MidiTitle, null );
        w.endElement( "td" );
        w.endElement( "tr" );

        w.startElement( "tr", this );

        // Duration
        outText = RockBandPrint.MicrosecondsToString( dfa.dba.Microseconds );
        WriteRow( w, "Duration", outText );

        // Notes
        outText = String.format( "%d", dfa.dba.Notes );
        WriteRow( w, "Notes", outText );

        // Notes
        outText = String.format( "%d", dfa.dba.Chords );
        WriteRow( w, "Chords", outText );

        // Stars
        outText = String.format( "%d", dfa.dba.StarCutoffOne );
        WriteRow( w, "1 star", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffTwo );
        WriteRow( w, "2 stars", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffThree );
        WriteRow( w, "3 stars", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffFour );
        WriteRow( w, "4 stars", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffFive );
        WriteRow( w, "5 stars", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffGold );
        WriteRow( w, "Gold stars", outText );
        outText = String.format( "%d", dfa.dba.StarCutoffGoldOld );
        WriteRow( w, "Gold stars (pre-patch)", outText );

        w.endElement( "tr" );
        w.endElement( "table" );

        return;
    }

    private void WriteRow( ResponseWriter w, String sName, String sValue ) throws IOException {

        w.startElement( "tr", this );
        WriteCell( w, sName );
        WriteCell( w, sValue );
        w.endElement( "tr" );
        return;
    }

    private void WriteCell( ResponseWriter w, String outText ) throws IOException {

        w.startElement( "td", this );
        w.writeAttribute( "align", "left", null );
        w.writeText( outText, null );
        w.endElement( "td" );
        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }
}
