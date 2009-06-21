package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import au.com.bytecode.opencsv.CSVWriter;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandAdvancedSort;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 10, 2009
 * Time: 7:08:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportServlet extends HttpServlet {
    public void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws IOException {

        CSVWriter       csvWriter;
        StringWriter    sWriter;
        String          filename;
        List<RockBandSong>  songs;

        filename = "rockbandanalyzer_export.csv";
        sWriter = new StringWriter();
        csvWriter = new CSVWriter( sWriter );

        resp.setContentType("application/octet-stream");
        resp.setHeader( "Content-Disposition", "attachment; filename=" + filename);

        songs = DataAccess.GetSongs( RockBandAdvancedSort.Song );

        ArrayList<String>   headerEntries;

        headerEntries = new ArrayList<String>();
        headerEntries.add("midiTitle");
        headerEntries.add("title");
        headerEntries.add("artist");
        headerEntries.add("album");
        headerEntries.add("genre");
        headerEntries.add("dateReleased");
        headerEntries.add("location");
        headerEntries.add("datePublished");
        headerEntries.add("bandDifficulty");
        headerEntries.add("guitarDifficulty");
        headerEntries.add("drumsDifficulty");
        headerEntries.add("vocalsDifficulty");
        headerEntries.add("bassDifficulty");
        headerEntries.add("isCover");
        headerEntries.add("availableInRB1");
        headerEntries.add("availableInRB2");

        csvWriter.writeNext( headerEntries.toArray(new String[headerEntries.size()]) );
        for( RockBandSong song : songs ) {
            ArrayList<String> entries;
            SimpleDateFormat sdf = new SimpleDateFormat("m/d/yyyy");

            entries = new ArrayList<String>();
            entries.add( song.getGenerated().getMidiTitle() );
            entries.add( song.getAssociated().getTitle() );
            entries.add( song.getAssociated().getArtist() );
            entries.add( song.getAssociated().getAlbum() );
            entries.add( song.getAssociated().getGenre() );
            entries.add( sdf.format( song.getAssociated().getDateReleased() ) );
            entries.add( String.format("%d", RockBandLocation.ToInteger(song.getAssociated().getLocation())) );
            entries.add(sdf.format( song.getAssociated().getRBReleaseDate() ) );

            RockBandInstrumentDifficulty rbid;

            rbid = song.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Band);
            entries.add( RockBandInstrumentDifficulty.ToInteger( rbid ).toString() );
            rbid = song.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Guitar);
            entries.add( RockBandInstrumentDifficulty.ToInteger( rbid ).toString() );
            rbid = song.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Drums);
            entries.add( RockBandInstrumentDifficulty.ToInteger( rbid ).toString() );
            rbid = song.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Vocals);
            entries.add( RockBandInstrumentDifficulty.ToInteger( rbid ).toString() );
            rbid = song.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Bass);
            entries.add( RockBandInstrumentDifficulty.ToInteger( rbid ).toString() );

            entries.add( String.valueOf(song.getAssociated().getCover()) );
            entries.add( String.valueOf(song.getAssociated().getAvailableInRB1()) );
            entries.add( String.valueOf(song.getAssociated().getAvailableInRB2()) );

            csvWriter.writeNext( entries.toArray(new String[entries.size()]) );
        }

        csvWriter.close();

        resp.getWriter().format( sWriter.toString() );
        return;
    }
}
