package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import java.util.*;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.GVizColumnChartPanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.GVizAnnotatedTimelinePanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.GVizDataPoint;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandInstrumentDifficulty;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatsPage extends BasePage {

    protected GVizColumnChartPanel ccpGenreDistribution;

    protected GVizColumnChartPanel ccpGuitarDifficultyDistribution;
    protected GVizColumnChartPanel ccpBassDifficultyDistribution;
    protected GVizColumnChartPanel ccpDrumsDifficultyDistribution;
    protected GVizColumnChartPanel ccpVocalsDifficultyDistribution;
    protected GVizColumnChartPanel ccpBandDifficultyDistribution;

    protected GVizAnnotatedTimelinePanel gvatpRBReleaseDateTimeline;

    public StatsPage() {


        ccpGenreDistribution = new GVizColumnChartPanel("gvccpGenreDistribution");
        add( ccpGenreDistribution );

        ccpGuitarDifficultyDistribution = new GVizColumnChartPanel("gvccpGuitarDifficultyDistribution");
        add( ccpGuitarDifficultyDistribution );
        ccpBassDifficultyDistribution = new GVizColumnChartPanel("gvccpBassDifficultyDistribution");
        add( ccpBassDifficultyDistribution );
        ccpDrumsDifficultyDistribution = new GVizColumnChartPanel("gvccpDrumsDifficultyDistribution");
        add( ccpDrumsDifficultyDistribution );
        ccpVocalsDifficultyDistribution = new GVizColumnChartPanel("gvccpVocalsDifficultyDistribution");
        add( ccpVocalsDifficultyDistribution );
        ccpBandDifficultyDistribution = new GVizColumnChartPanel("gvccpBandDifficultyDistribution");
        add( ccpBandDifficultyDistribution );

        gvatpRBReleaseDateTimeline = new GVizAnnotatedTimelinePanel("gvatpRBReleaseDateTimeline");
        add(gvatpRBReleaseDateTimeline);

        RenderGenreDistribution( ccpGenreDistribution );

        RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory.Guitar, ccpGuitarDifficultyDistribution );
        RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory.Bass, ccpBassDifficultyDistribution );
        RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory.Drums, ccpDrumsDifficultyDistribution );
        RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory.Vocals, ccpVocalsDifficultyDistribution );
        RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory.Band, ccpBandDifficultyDistribution );

        RenderRBReleaseDateTimeline(gvatpRBReleaseDateTimeline);
        return;
    }

    protected static void RenderGenreDistribution( GVizColumnChartPanel ccpGenreDistribution ) {

        SortedMap<String,Long> genreDistribution;

        genreDistribution = DataAccess.GetGenreDistribution();

        ccpGenreDistribution.setCategory( "Genre" );

        List<String>    lineLabels;
        List<String>    columnLabels;
        List<Long>      data;
        Map<String,String> options;

        lineLabels = new ArrayList<String>();
        lineLabels.add( "Number of songs" );
        ccpGenreDistribution.setLineLabels( lineLabels );

        columnLabels = new ArrayList<String>();
        for( String genre : genreDistribution.keySet() ) {
            columnLabels.add( genre );
        }
        ccpGenreDistribution.setColumnLabels( columnLabels );

        options = new HashMap<String,String>();
        options.put( "width", "560" );
        options.put( "height", "300" );
        options.put( "is3D", "true" );
        options.put( "legend", "'none'" );
        options.put( "title", "'Distribution of songs by genre'" );
//        options.put( "title", "genreDistribution" );
        ccpGenreDistribution.setOptions( options );

        data = new ArrayList<Long>();
        for( Long genreCount : genreDistribution.values() ) {
            data.add( genreCount );
        }
        ccpGenreDistribution.setData( data );

        return;
    }

    protected static void RenderDifficultyDistribution( RockBandInstrumentDifficultyCategory rbidc, GVizColumnChartPanel ccp ) {

        SortedMap<RockBandInstrumentDifficulty,Long> difficultyDistribution;

        difficultyDistribution = DataAccess.GetDifficultyDistribution( rbidc );

        ccp.setCategory( "Difficulty" );

        List<String>    lineLabels;
        List<String>    columnLabels;
        List<Long>      data;
        Map<String,String> options;

        lineLabels = new ArrayList<String>();
        lineLabels.add( "Number of songs" );
        ccp.setLineLabels( lineLabels );

        columnLabels = new ArrayList<String>();
        for( RockBandInstrumentDifficulty difficulty : difficultyDistribution.keySet() ) {
            columnLabels.add( difficulty.name() );
        }
        ccp.setColumnLabels( columnLabels );

        options = new HashMap<String,String>();
        options.put( "width", "560" );
        options.put( "height", "300" );
        options.put( "is3D", "true" );
        options.put( "legend", "'none'" );
        options.put( "title", String.format("'Distribution of songs by difficulty for %s'", rbidc.name()) );
//        options.put( "title", "genreDistribution" );
        ccp.setOptions( options );

        data = new ArrayList<Long>();
        for( Long difficultyCount : difficultyDistribution.values() ) {
            data.add( difficultyCount );
        }
        ccp.setData( data );

        return;
    }

    protected static void RenderRBReleaseDateTimeline( GVizAnnotatedTimelinePanel atp ) {

        SortedMap<Date,Long> dateDistribution;

        dateDistribution = DataAccess.GetRBReleaseDateDistribution();

//        atp.setCategory( "Difficulty" );

        List<String>    lineLabels;
        List<Date>    columnLabels;
        List<GVizDataPoint>      data;
        Map<String,String> options;

        atp.setWidth( 700 );
        atp.setHeight( 300 );

        lineLabels = new ArrayList<String>();
        lineLabels.add( "released" );
        atp.setLineLabels( lineLabels );

        columnLabels = new ArrayList<Date>();
        for( Date d : dateDistribution.keySet() ) {
            columnLabels.add( d );
        }
        atp.setColumnLabels( columnLabels );

        options = new HashMap<String,String>();
        options.put( "displayAnnotations", "true" );
        atp.setOptions( options );

        data = new ArrayList<GVizDataPoint>();
        for( Long songCount : dateDistribution.values() ) {
            GVizDataPoint    dp;

            dp = new GVizDataPoint();
            dp.setValue( songCount );
//            dp.setAnnotation( String.format("%d songs", songCount) );
//            dp.setExtendedAnnotation( String.format("%d songs extended", songCount) );

            data.add( dp );
        }

        atp.setData( data );

        return;
    }

}
