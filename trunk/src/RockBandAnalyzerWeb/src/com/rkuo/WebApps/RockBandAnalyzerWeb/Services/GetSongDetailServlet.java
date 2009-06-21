package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class GetSongDetailServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder   sb;
        RockBandSong        s;
        String              sId;
        Long                id;

        sId = request.getParameter("id");
        if( sId == null ) {
            response.sendError( 400, "The 'id' parameter is required.");
            return;
        }

        id = Long.parseLong( sId );
        s = DataAccess.GetSongById(id );
        if( s == null ) {
            response.sendError( 400, String.format("The id '%d' was not found.", id) );
            return;
        }

        sb = new StringBuilder();

        response.setContentType( "text/xml" );
        response.setCharacterEncoding( "UTF-8" );

        // Not a fan of writing my own xml, but Google hasn't whitelisted the XML serializers in Java
        sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append( "<song>\n");

//        SimpleDateFormat sdf;

//        sdf = new SimpleDateFormat("m/d/yyyy");
        Date d;
        String  sValue;

        WriteProperty( sb, "id", s.getId().toString() );        
        WriteProperty( sb, "last_updated", s.getLastUpdated().toString() );
        WriteProperty( sb, "title", s.getAssociated().getTitle() );
        WriteProperty( sb, "artist", s.getAssociated().getArtist() );
        WriteProperty( sb, "album", s.getAssociated().getAlbum() );

//        sdf = new SimpleDateFormat("yyyy");
        d = s.getAssociated().getDateReleased();
        sValue = String.format( "%d", d.getYear()+1900  );
        WriteProperty( sb, "release_year", sValue );
        
        WriteProperty( sb, "genre", s.getAssociated().getGenre() );

//        sdf = new SimpleDateFormat("m/d/yyyy");
        d = s.getAssociated().getRBReleaseDate();
        sValue = String.format( "%d/%02d/%02d", d.getYear()+1900, d.getMonth()+1, d.getDate() );
        WriteProperty( sb, "rb_release_date", sValue );

        WriteProperty( sb, "location", RockBandLocation.ToInteger(s.getAssociated().getLocation()).toString() );
        WriteProperty( sb, "cover", s.getAssociated().getCover().toString() );
        WriteProperty( sb, "available_rb1", s.getAssociated().getAvailableInRB1().toString() );
        WriteProperty( sb, "available_rb2", s.getAssociated().getAvailableInRB2().toString() );

        RockBandInstrumentDifficulty rbid;

        rbid = s.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Band);
        WriteProperty( sb, "difficulty_band", RockBandInstrumentDifficulty.ToInteger(rbid).toString() );
        rbid = s.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Guitar);
        WriteProperty( sb, "difficulty_guitar", RockBandInstrumentDifficulty.ToInteger(rbid).toString() );
        rbid = s.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Drums);
        WriteProperty( sb, "difficulty_drums", RockBandInstrumentDifficulty.ToInteger(rbid).toString() );
        rbid = s.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Vocals);
        WriteProperty( sb, "difficulty_vocals", RockBandInstrumentDifficulty.ToInteger(rbid).toString() );
        rbid = s.getAssociated().getDifficulty(RockBandInstrumentDifficultyCategory.Bass);
        WriteProperty( sb, "difficulty_bass", RockBandInstrumentDifficulty.ToInteger(rbid).toString() );

        WriteProperty( sb, "star_cutoff_one", s.getGenerated().getStarCutoffOne().toString() );
        WriteProperty( sb, "star_cutoff_two", s.getGenerated().getStarCutoffTwo().toString() );
        WriteProperty( sb, "star_cutoff_three", s.getGenerated().getStarCutoffThree().toString() );
        WriteProperty( sb, "star_cutoff_four", s.getGenerated().getStarCutoffFour().toString() );
        WriteProperty( sb, "star_cutoff_five", s.getGenerated().getStarCutoffFive().toString() );
        WriteProperty( sb, "star_cutoff_gold", s.getGenerated().getStarCutoffGold().toString() );
        WriteProperty( sb, "star_cutoff_gold_old", s.getGenerated().getStarCutoffGoldOld().toString() );
        WriteProperty( sb, "maximum_score", s.getGenerated().getMaxScore().toString() );

        WriteProperty( sb, "midi_title", s.getGenerated().getMidiTitle() );
        WriteProperty( sb, "duration", s.getGenerated().getMicroseconds().toString() );
        WriteProperty( sb, "notes", s.getGenerated().getNotes().toString() );
        WriteProperty( sb, "chords", s.getGenerated().getChords().toString() );

        WriteProperty( sb, "solos", String.format("%d", s.getGenerated().getSolos()) );
        WriteProperty( sb, "fills", String.format("%d", s.getGenerated().getFills()) );
        WriteProperty( sb, "overdrive_phrases", String.format("%d", s.getGenerated().getOverdrivePhrases()) );
        WriteProperty( sb, "big_rock_ending", Boolean.valueOf(s.getGenerated().isBigRockEnding()).toString() );
        WriteProperty( sb, "unmultiplied_score", s.getGenerated().getChords().toString() );
        WriteProperty( sb, "multiplied_score", s.getGenerated().getChords().toString() );
        WriteProperty( sb, "unmultiplied_score_with_bre_notes", s.getGenerated().getChords().toString() );
        WriteProperty( sb, "maximum_multiplier_not_possible", s.getGenerated().getChords().toString() );
        WriteProperty( sb, "optimal_fill_delay", s.getGenerated().getChords().toString() );
        WriteProperty( sb, "glitched", Boolean.valueOf(s.getGenerated().isGlitched()).toString() );
        WriteProperty( sb, "gold_starrable", Boolean.valueOf(s.getGenerated().isGoldStarrable()).toString() );
        WriteProperty( sb, "normal_optimal_score", s.getGenerated().getNormalOptimalScore().toString() );
        WriteProperty( sb, "breakneck_optimal_score", s.getGenerated().getBreakneckOptimalScore().toString() );

        sb.append( "</song>\n");
        response.getWriter().format( sb.toString() );
        return;
    } // doGet

    protected void WriteProperty( StringBuilder sb, String name, String value ) {

        String  sEscaped;

        sEscaped = value;
        sEscaped = sEscaped.replaceAll("&", "&amp;");
        sEscaped = sEscaped.replaceAll("<", "&lt;");
        sEscaped = sEscaped.replaceAll(">", "&gt;");

        sb.append( String.format("<%s>", name) );
        sb.append( sEscaped );
        sb.append( String.format("</%s>", name) );
        return;
    }
}
