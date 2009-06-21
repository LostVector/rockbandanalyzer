package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.SongTablePanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongFilter;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexPage extends SidebarPage {

    private SongTablePanel panelRecentlyReleased;

    public IndexPage() {

        panelRecentlyReleased = new SongTablePanel("panelRecentlyReleased");
        panelRecentlyReleased.setRenderType( "filter" );
        panelRecentlyReleased.setFilterType( RockBandSongFilter.RecentlyReleased );
        add( panelRecentlyReleased );
        return;
    }
}
