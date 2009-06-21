package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.PageParameters;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.RockBandPrint;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.GVizLineChartPanel;

import java.util.*;
import java.io.ByteArrayInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongPage extends BasePage {

    protected Label               lblSongTitle;

    protected Label               lblArtist;
    protected Label               lblCoverVersion;
    protected Label               lblAlbum;
    protected Label               lblGenre;

    protected Label               lblReleaseYear;
    protected Label               lblRBDatePublished;
    protected Label               lblMidiTitle;

    protected Label               lblDurationValue;
    protected Label               lblNotesValue;
    protected Label               lblChordsValue;

    protected Label               lblOneStarValue;
    protected Label               lblTwoStarValue;
    protected Label               lblThreeStarValue;
    protected Label               lblFourStarValue;
    protected Label               lblFiveStarValue;
    protected Label               lblGoldStarValue;
    protected Label               lblGoldStarOldValue;
    protected Label               lblMaxScore;

    protected Label               lblBandDifficulty;
    protected Label               lblGuitarDifficulty;
    protected Label               lblDrumsDifficulty;
    protected Label               lblVocalsDifficulty;
    protected Label               lblBassDifficulty;

    protected ContextImage        imgBandDifficulty;
    protected ContextImage        imgGuitarDifficulty;
    protected ContextImage        imgDrumsDifficulty;
    protected ContextImage        imgVocalsDifficulty;
    protected ContextImage        imgBassDifficulty;

    protected Label               lblRB2ImmediatePath;
    protected Label               lblRB2NormalOptimalPath;
    protected Label               lblRB2BreakneckOptimalPath;

    protected GVizLineChartPanel  lcStars;

    public SongPage( PageParameters params ) {

        super();

        DrumsFullAnalysis dfa;
        long                id;
        
//        add(new Label("lblHelloWorld", new Model("Hello, World")));

        lblSongTitle = new Label("lblSongTitle","");

        lblArtist = new Label("lblArtist", "");
        lblCoverVersion = new Label("lblCoverVersion", "");
        lblAlbum = new Label("lblAlbum", "");
        lblGenre = new Label("lblGenre", "");

        lblReleaseYear = new Label("lblReleaseYear", "");
        lblRBDatePublished = new Label("lblRBDatePublished", "");
        lblMidiTitle = new Label("lblMidiTitle", "");

        lblDurationValue = new Label("lblDurationValue", "");
        lblNotesValue = new Label("lblNotesValue", "");
        lblChordsValue = new Label("lblChordsValue", "");

        lblOneStarValue = new Label("lblOneStarValue", "");
        lblTwoStarValue = new Label("lblTwoStarValue", "");
        lblThreeStarValue = new Label("lblThreeStarValue", "");
        lblFourStarValue = new Label("lblFourStarValue", "");
        lblFiveStarValue = new Label("lblFiveStarValue", "");
        lblGoldStarValue = new Label("lblGoldStarValue", "");
        lblGoldStarOldValue = new Label("lblGoldStarOldValue", "");
        lblMaxScore = new Label("lblMaxScore", "");

        lblBandDifficulty = new Label( "lblBandDifficulty", "" );
        lblGuitarDifficulty = new Label( "lblGuitarDifficulty", "" );
        lblDrumsDifficulty = new Label( "lblDrumsDifficulty", "" );
        lblVocalsDifficulty = new Label( "lblVocalsDifficulty", "" );
        lblBassDifficulty = new Label( "lblBassDifficulty", "" );

        lblRB2ImmediatePath = new Label("lblRB2ImmediatePath", "");
        lblRB2NormalOptimalPath = new Label("lblRB2NormalOptimalPath", "");
        lblRB2BreakneckOptimalPath = new Label("lblRB2BreakneckOptimalPath", "");

        add( lblSongTitle );

        add( lblArtist );
        add( lblCoverVersion );
        add( lblAlbum );
        add( lblGenre );

        add( lblReleaseYear );
        add( lblRBDatePublished );
        add( lblMidiTitle );

        add( lblDurationValue );
        add( lblNotesValue );
        add( lblChordsValue );

        add( lblOneStarValue );
        add( lblTwoStarValue );
        add( lblThreeStarValue );
        add( lblFourStarValue );
        add( lblFiveStarValue );
        add( lblGoldStarValue );
        add( lblGoldStarOldValue );
        add( lblMaxScore );

        add( lblBandDifficulty );
        add( lblGuitarDifficulty );
        add( lblDrumsDifficulty );
        add( lblVocalsDifficulty );
        add( lblBassDifficulty );

        add( lblRB2ImmediatePath );
        add( lblRB2NormalOptimalPath );
        add( lblRB2BreakneckOptimalPath );

        id = params.getLong( "id" );

        String  sValue;
        RockBandSong    song;

        song = DataAccess.GetSongById( id );

        sValue = song.getAssociated().getTitle();
        lblSongTitle.setDefaultModel( new Model<String>(sValue) );
        sValue = song.getAssociated().getArtist();
        lblArtist.setDefaultModel( new Model<String>(sValue) );
        sValue = song.getAssociated().getCover().toString();
        lblCoverVersion.setDefaultModel( new Model<String>(sValue) );        
        sValue = song.getAssociated().getAlbum();
        lblAlbum.setDefaultModel( new Model<String>(sValue) );
        sValue = song.getAssociated().getGenre();
        lblGenre.setDefaultModel( new Model<String>(sValue) );

        // I'm calling image constructors here b/c apparently set default model doesn't work for Context Image

        RockBandInstrumentDifficulty    rbid;

        rbid = song.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Band );
        lblBandDifficulty.setDefaultModel( new Model<String>(rbid.name()) );
        imgBandDifficulty = new ContextImage( "imgBandDifficulty", GetDifficultyImage(rbid) );
        imgBandDifficulty.setDefaultModel( new Model<String>(GetDifficultyImage(rbid)) );

        rbid = song.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Guitar );
        lblGuitarDifficulty.setDefaultModel( new Model<String>(rbid.name()) );
        imgGuitarDifficulty = new ContextImage( "imgGuitarDifficulty", GetDifficultyImage(rbid) );
        imgGuitarDifficulty.setDefaultModel( new Model<String>(GetDifficultyImage(rbid)) );

        rbid = song.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Drums );
        lblDrumsDifficulty.setDefaultModel( new Model<String>(rbid.name()) );
        imgDrumsDifficulty = new ContextImage( "imgDrumsDifficulty", GetDifficultyImage(rbid) );
        imgDrumsDifficulty.setDefaultModel( new Model<String>(GetDifficultyImage(rbid)) );

        rbid = song.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Vocals );
        lblVocalsDifficulty.setDefaultModel( new Model<String>(rbid.name()) );
        imgVocalsDifficulty = new ContextImage( "imgVocalsDifficulty", GetDifficultyImage(rbid) );
        imgVocalsDifficulty.setDefaultModel( new Model<String>(GetDifficultyImage(rbid)) );

        rbid = song.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Bass );
        lblBassDifficulty.setDefaultModel( new Model<String>(rbid.name()) );
        imgBassDifficulty = new ContextImage( "imgBassDifficulty", GetDifficultyImage(rbid) );
        imgBassDifficulty.setDefaultModel( new Model<String>(GetDifficultyImage(rbid)) );

        add( imgBandDifficulty );
        add( imgGuitarDifficulty );
        add( imgDrumsDifficulty );
        add( imgVocalsDifficulty );
        add( imgBassDifficulty );

        Date d;
//        Calendar    cal;

        d = song.getAssociated().getDateReleased();

        sValue = String.format( "%d", d.getYear()+1900 );
        lblReleaseYear.setDefaultModel( new Model<String>(sValue) );

        d = song.getAssociated().getRBReleaseDate();
//        cal = new GregorianCalendar( d.getYear(), d.getMonth(), d.getDate() );
        sValue = String.format( "%d/%02d/%02d", d.getYear()+1900, d.getMonth()+1, d.getDate() );
        lblRBDatePublished.setDefaultModel( new Model<String>(sValue) );

        sValue = song.getGenerated().getMidiTitle();
        lblMidiTitle.setDefaultModel( new Model<String>(sValue) );

        lcStars = new GVizLineChartPanel("gvlcpStars");
        add( lcStars );

        dfa = GetDFAById( id );

        SetDFAValues( dfa );
        return;
    }

    protected void SetDFAValues( DrumsFullAnalysis dfa ) {

        String  sValue;

        sValue = RockBandPrint.MicrosecondsToString( dfa.dba.Microseconds );
        lblDurationValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.Notes );
        lblNotesValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.Chords );
        lblChordsValue.setDefaultModel( new Model<String>(sValue) );

        sValue = String.format("%d", dfa.dba.StarCutoffOne );
        lblOneStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffTwo );
        lblTwoStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffThree );
        lblThreeStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffFour );
        lblFourStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffFive );
        lblFiveStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffGold );
        lblGoldStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.dba.StarCutoffGoldOld );
        lblGoldStarOldValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dfa.RB2PathNormalOptimal.Score );
        lblMaxScore.setDefaultModel( new Model<String>(sValue) );

        sValue = dfa.RB2PathImmediate.toString();
        lblRB2ImmediatePath.setDefaultModel( new Model<String>(sValue) );
        sValue = dfa.RB2PathNormalOptimal.toString();
        lblRB2NormalOptimalPath.setDefaultModel( new Model<String>(sValue) );
        sValue = dfa.RB2PathBreakneckOptimal.toString();
        lblRB2BreakneckOptimalPath.setDefaultModel( new Model<String>(sValue) );

        lcStars.setCategory( "Stars" );

        List<String>    lineLabels;
        List<String>    columnLabels;
        List<Long>      data;
        Map<String,String> options;

        lineLabels = new ArrayList<String>();
        lineLabels.add( "Score" );
        lcStars.setLineLabels( lineLabels );

        columnLabels = new ArrayList<String>();
        columnLabels.add( "0" );
        columnLabels.add( "1" );
        columnLabels.add( "2" );
        columnLabels.add( "3" );
        columnLabels.add( "4" );
        columnLabels.add( "5" );
        columnLabels.add( "Gold" );
        columnLabels.add( "Gold (old)" );
        columnLabels.add( "Max" );
        lcStars.setColumnLabels( columnLabels );

        options = new HashMap<String,String>();
        options.put( "width", "533" );
        options.put( "height", "300" );
        options.put( "lineSize", "3" );
        options.put( "pointSize", "5" );
        lcStars.setOptions( options );
        
        data = new ArrayList<Long>();
        data.add( 0L );
        data.add( dfa.dba.StarCutoffOne );
        data.add( dfa.dba.StarCutoffTwo );
        data.add( dfa.dba.StarCutoffThree );
        data.add( dfa.dba.StarCutoffFour );
        data.add( dfa.dba.StarCutoffFive );
        data.add( dfa.dba.StarCutoffGold );
        data.add( dfa.dba.StarCutoffGoldOld );
        data.add( dfa.RB2PathNormalOptimal.Score );
        lcStars.setData( data );
        return;
    }

    protected DrumsFullAnalysis GetDFAById( long id ) {
        RockBandAnalyzerParams rbap;
        RockBandSongRaw rbSong;
        DrumsFullAnalysis dfa;

        rbSong = DataAccess.GetRawSongById( id );

        rbap = new RockBandAnalyzerParams();
        dfa = RockBandAnalyzer.AnalyzeStream( null, new ByteArrayInputStream(rbSong.getFile()), rbap );

        return dfa;
    }

    protected String GetDifficultyImage( RockBandInstrumentDifficulty rbid ) {
        
        switch( rbid ) {
            case Warmup:
                return "/images/dots-zero.png";
            case Apprentice:
                return "/images/dots-one.png";
            case Solid:
                return "/images/dots-two.png";
            case Moderate:
                return "/images/dots-three.png";
            case Challenging:
                return "/images/dots-four.png";
            case Nightmare:
                return "/images/dots-five.png";
            case Impossible:
                return "/images/dots-devils.png";
            case NoPart:
                return "";
            case Unknown:
                return "";
        }

        return "";
    }
}
