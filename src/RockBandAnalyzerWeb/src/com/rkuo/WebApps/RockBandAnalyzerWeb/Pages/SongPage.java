package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.PageParameters;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;
import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.RockBandPrint;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;

import java.util.zip.GZIPInputStream;
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

    public SongPage( PageParameters params ) {

        super();

        DrumsBaselineData   dbd;
        long                id;
        
//        add(new Label("lblHelloWorld", new Model("Hello, World")));

        lblMidiTitle = new Label("lblMidiTitle","");
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

        id = params.getLong( "id" );

        dbd = GetDBDById( id );

        SetDBDValues( dbd );
        return;
    }

    protected void SetDBDValues( DrumsBaselineData dbd ) {

        String  sValue;

        sValue = dbd.SongTitle;
        lblMidiTitle.setDefaultModel( new Model<String>(sValue) );

        sValue = RockBandPrint.MicrosecondsToString( dbd.Microseconds );
        lblDurationValue.setDefaultModel( new Model<String>(sValue) );

        sValue = String.format("%d", dbd.Notes );
        lblNotesValue.setDefaultModel( new Model<String>(sValue) );

        sValue = String.format("%d", dbd.Chords );
        lblChordsValue.setDefaultModel( new Model<String>(sValue) );

        sValue = String.format("%d", dbd.StarCutoffOne );
        lblOneStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffTwo );
        lblTwoStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffThree );
        lblThreeStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffFour );
        lblFourStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffFive );
        lblFiveStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffGold );
        lblGoldStarValue.setDefaultModel( new Model<String>(sValue) );
        sValue = String.format("%d", dbd.StarCutoffGoldOld );
        lblGoldStarOldValue.setDefaultModel( new Model<String>(sValue) );

        return;
    }

    protected DrumsBaselineData GetDBDById( long id ) {
        RockBandAnalyzerParams rbap;
        String                  sId;
        GZIPInputStream gzIn;
        ByteArrayInputStream baIn;
        ByteArrayOutputStream baOut;
        RockBandSong rbSong;
        DrumsBaselineData       dbd;

        rbSong = DataAccess.GetSongById( id );

        baIn = new ByteArrayInputStream( rbSong.getFile() );
        baOut = new ByteArrayOutputStream();

        try {
            gzIn = new GZIPInputStream( baIn );

            for( int c = gzIn.read(); c != -1; c = gzIn.read() ) {
                baOut.write( c );
            }

            baOut.close();
        }
        catch( IOException ioex ) {
            return null;
        }

        rbap = new RockBandAnalyzerParams();
        dbd = RockBandAnalyzer.AnalyzeStream( null, new ByteArrayInputStream(baOut.toByteArray()), rbap );

        return dbd;
    }
}
