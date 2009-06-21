package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.PropertyKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.SidebarPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 19, 2009
 * Time: 10:11:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class SidebarPage extends BasePage {

    private ArrayList<SidebarPanel> _sidebarPanels;

    private Label lblSidebarData;
    private Label lblLastStatsUpdate;
    private RepeatingView repeatSidebarPanels;

    public SidebarPage() {

        lblSidebarData = new Label( "lblSidebarData", GetSongCountString() );
        add( lblSidebarData );

        lblLastStatsUpdate = new Label( "lblLastStatsUpdate", GetLastStatsUpdateString() );
        add( lblLastStatsUpdate );

        repeatSidebarPanels = new RepeatingView("repeatSidebarPanels");
        add( repeatSidebarPanels );

        _sidebarPanels = new ArrayList<SidebarPanel>();
        return;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();    //To change body of overridden methods use File | Settings | File Templates.

        RenderSidebarPanels();
        return;
    }

    protected void RenderSidebarPanels() {
        for( SidebarPanel sp : _sidebarPanels ) {
            WebMarkupContainer  liItem;

            liItem = new WebMarkupContainer( repeatSidebarPanels.newChildId() );
            repeatSidebarPanels.add( liItem );

            liItem.add( sp );
        }
    }

    protected void AddSidebarPanel( SidebarPanel sp ) {
        _sidebarPanels.add( sp );
        return;
    }

    protected String GetLastStatsUpdateString() {
        Long    lastStatsUpdate;
        String  sValue;

        sValue = "";

        lastStatsUpdate = (Long)getCache().get( CacheKeys.LastStatsUpdate );
        if( lastStatsUpdate != null ) {
            Date d;

            d = new Date( lastStatsUpdate );
            sValue = String.format( "Site stats last updated on %s.", d.toString() );
        }

        return sValue;
    }

    protected String GetSongCountString() {

        String  sData;

        sData = "X of Y songs uploaded";

        do {
            Long    loadedCount, scrapedCount;

            loadedCount = DataAccess.GetPropertyAsLong( PropertyKeys.LoadedCount );
            if( loadedCount == null ) {
                break;
            }

            scrapedCount = DataAccess.GetPropertyAsLong( PropertyKeys.ScrapedCount );
            if( scrapedCount == null ) {
                break;
            }

            sData = String.format( "%d of %d songs uploaded", loadedCount, scrapedCount );
        }
        while( false );

        return sData;
    }

}
