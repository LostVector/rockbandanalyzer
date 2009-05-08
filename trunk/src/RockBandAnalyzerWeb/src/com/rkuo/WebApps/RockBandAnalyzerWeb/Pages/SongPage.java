package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.PageParameters;
import com.rkuo.RockBand.Simulators.DrumsBaselineAnalysis;
import com.rkuo.RockBand.Simulators.DrumsFullAnalysis;
import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.RockBandPrint;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongEmbedded;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.GVizLineChartPanel;

import java.util.zip.GZIPInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    protected Label               lblAlbum;
    protected Label               lblGenre;

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
        lblAlbum = new Label("lblAlbum", "");
        lblGenre = new Label("lblGenre", "");

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

        lblRB2ImmediatePath = new Label("lblRB2ImmediatePath", "");
        lblRB2NormalOptimalPath = new Label("lblRB2NormalOptimalPath", "");
        lblRB2BreakneckOptimalPath = new Label("lblRB2BreakneckOptimalPath", "");

        add( lblSongTitle );

        add( lblArtist );
        add( lblAlbum );
        add( lblGenre );

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

        add( lblRB2ImmediatePath );
        add( lblRB2NormalOptimalPath );
        add( lblRB2BreakneckOptimalPath );

        id = params.getLong( "id" );

        String  sValue;
        RockBandSongRaw rawSong;
        RockBandSongEmbedded    embedded;

        embedded = DataAccess.GetEmbeddedSongById( id );

        sValue = embedded.getTitle();
        lblSongTitle.setDefaultModel( new Model<String>(sValue) );
        sValue = embedded.getArtist();
        lblArtist.setDefaultModel( new Model<String>(sValue) );
        sValue = embedded.getAlbum();
        lblAlbum.setDefaultModel( new Model<String>(sValue) );
        sValue = embedded.getGenre();
        lblGenre.setDefaultModel( new Model<String>(sValue) );

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
}
