
package com.rkuo.WebApps.RockBandAnalyzerWeb;

import com.rkuo.RockBand.RockBandSort;
import com.rkuo.RockBand.RockBandLocation;

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
        HttpServletRequest req;
        RockBandSort rbSort;

        writer = context.getResponseWriter();

        req = (HttpServletRequest) context.getExternalContext().getRequest();

        rbSort = GetSorting( req );

        WriteHeaderCells( writer, rbSort );

        if( rbSort == RockBandSort.Song ) {
            WriteBySong( writer );
        }
        else if( rbSort == RockBandSort.Band ) {
            WriteByBand( writer );
        }
        else if( rbSort == RockBandSort.Decade ) {
            WriteByDecade( writer );
        }
        else if( rbSort == RockBandSort.Location ) {
            WriteByLocation( writer );
        }
        else {
            WriteBySong( writer );
        }

        writer.startElement( "hr", this );
        writer.endElement( "hr" );

        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }

    private void WriteHeaderCells( ResponseWriter w, RockBandSort rbSort ) throws IOException {

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

    private void WriteBySong( ResponseWriter w ) throws IOException {
        List<RockBandSong> songs;

        songs = DataAccess.GetSongs();
        if( songs == null ) {
            return;
        }

        w.startElement( "table", this );
        w.writeAttribute( "width", "960px", null );

        for( RockBandSong s : songs ) {
            WriteSong( w, s );
        }

        w.endElement( "table" );
        return;
    }

    private void WriteByBand( ResponseWriter w ) throws IOException {
        return;
    }

    private void WriteByDecade( ResponseWriter w ) throws IOException {
        return;
    }

    private void WriteByLocation( ResponseWriter w ) throws IOException {

        List<RockBandSong>  songs;

        songs = DataAccess.GetSongsByLocation( RockBandLocation.Downloaded );
        WriteSongGroup( w, "Downloaded", songs );

        songs = DataAccess.GetSongsByLocation( RockBandLocation.RockBandTwo );
        WriteSongGroup( w, "Rock Band 2", songs );

        songs = DataAccess.GetSongsByLocation( RockBandLocation.RockBandOne );
        WriteSongGroup( w, "Rock Band", songs );
        return;
    }

    private void WriteByDifficulty( ResponseWriter w ) throws IOException {
        return;
    }

    private void WriteSongGroup( ResponseWriter w, String description, List<RockBandSong> songs ) throws IOException {

        String  outText;

        w.startElement( "table", this );
        w.writeAttribute( "width", "960px", null );

        w.startElement( "tr", this );

        w.startElement( "td", this );
        w.writeAttribute( "align", "left", null );
        w.writeText( description, null );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        outText = String.format( "%d songs", songs.size() );
        w.writeText( outText, null );
        w.endElement( "td" );

        w.endElement( "tr" );

        if( songs == null ) {
            WriteEmptyCategory( w );
        }
        else if( songs.size() == 0 ) {
            WriteEmptyCategory( w );
        }
        else {
            for( RockBandSong s : songs ) {
                WriteSong( w, s );
            }
        }

        w.endElement( "table" );
        return;
    }

    private void WriteEmptyCategory( ResponseWriter w ) throws IOException {

        w.startElement( "tr", this );

        w.startElement( "td", this );
        w.writeAttribute( "align", "center", null );
        w.writeAttribute( "colspan", "2", null );
        w.writeText( "No songs in this category.", null );
        w.endElement( "td" );

        w.startElement( "td", this );
        w.endElement( "td" );
        w.endElement( "tr" );

        return;
    }

    private void WriteSong( ResponseWriter w, RockBandSong s ) throws IOException {

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

        return;
    }

    private static RockBandSort GetSorting( HttpServletRequest req ) {

        RockBandSort    rbSort;
        String              sortParam;

        sortParam = req.getParameter("sort");
        if( sortParam == null ) {
            return RockBandSort.Song;
        }

        if( sortParam.compareTo("song") == 0 ) {
            rbSort = RockBandSort.Song;
        }
        else if( sortParam.compareTo("band") == 0 ) {
            rbSort = RockBandSort.Band;
        }
        else if( sortParam.compareTo("decade") == 0 ) {
            rbSort = RockBandSort.Decade;
        }
        else if( sortParam.compareTo("location") == 0 ) {
            rbSort = RockBandSort.Location;
        }
        else if( sortParam.compareTo("difficulty") == 0 ) {
            String  instrumentParam;

            instrumentParam = req.getParameter("instrument");
            if( instrumentParam.compareTo( "guitar" ) == 0 ) {
                rbSort = RockBandSort.DifficultyGuitar;
            }
            else if( instrumentParam.compareTo( "bass" ) == 0 ) {
                rbSort = RockBandSort.DifficultyBass;
            }
            else if( instrumentParam.compareTo( "drums" ) == 0 ) {
                rbSort = RockBandSort.DifficultyDrums;
            }
            else if( instrumentParam.compareTo( "vocals" ) == 0 ) {
                rbSort = RockBandSort.DifficultyVocals;
            }
            else if( instrumentParam.compareTo( "band" ) == 0 ) {
                rbSort = RockBandSort.DifficultyBand;
            }
            else {
                // Default to drums, because they rock
                rbSort = RockBandSort.DifficultyDrums;
            }
        }
        else {
            rbSort = RockBandSort.Song;
        }

        return rbSort;
    }
}
