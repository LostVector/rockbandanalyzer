package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.PageParameters;
import org.apache.wicket.model.Model;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongFilter;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandDotComSong;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 12, 2009
 * Time: 12:42:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrowseDotComPage extends BasePage {

    RepeatingView rptvSongs;
    Label         lblTitleData;

    ExternalLink    elAll;
    ExternalLink    elMissing;


    public BrowseDotComPage( PageParameters params ) {

        elAll = new ExternalLink( "elAll", "/browsedotcom?filter=all", "All" );
        add( elAll );

        elMissing = new ExternalLink( "elMissing", "/browsedotcom?filter=missing", "Missing" );
        add( elMissing );

        rptvSongs = new RepeatingView("rptvSongs");
        add( rptvSongs );

        lblTitleData = new Label( "lblTitleData", "" );
        add( lblTitleData );

        if( params.containsKey("filter") == true ) {
            String filterParam;

            filterParam = params.getString("filter");
            if( filterParam.compareToIgnoreCase("all") == 0 ) {
                RenderAllSongs( rptvSongs );
            }
            else if( filterParam.compareToIgnoreCase("missing") == 0 ) {
                RenderMissingSongs( rptvSongs );
            }
        }
        else {
            RenderAllSongs( rptvSongs );
        }

        return;
    }


    protected void RenderMissingSongs( RepeatingView rptvSongs ) {

        List<RockBandDotComSong> missingSongs;
        String       sValue;
        
        missingSongs = DataAccess.DotComGetMissingSongs();
        if( missingSongs == null ) {
            return;
        }

        sValue = String.format( "%d songs", missingSongs.size() );
        lblTitleData.setDefaultModel( new Model<String>(sValue));

        for( RockBandDotComSong s : missingSongs ) {
            WebMarkupContainer trItem;
            WebMarkupContainer  tdItem;
            Label lblCategory;
            ExternalLink elMidiName;

            WebMarkupContainer  tdItemData;
            Label               lblItemData;

            String                  songTitle;

            trItem = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( trItem );

            tdItem = new WebMarkupContainer( "tdItem" );
            trItem.add( tdItem );

            tdItemData = new WebMarkupContainer( "tdItemData" );
            trItem.add( tdItemData );

            lblCategory = new Label( "lblCategory" );
            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            songTitle = "(MIDI) - " + s.getMidiTitle();

            elMidiName = new ExternalLink( "elMidiName", "/dcsong?id=" + s.getMidiTitle(), songTitle );
            tdItem.add( elMidiName );

            // tdItemData
            lblItemData = new Label( "lblItemData", "" );
            tdItemData.add( lblItemData );
        }

        add( rptvSongs );
        return;
    }

    protected void RenderAllSongs( RepeatingView rptvSongs ) {

        List<RockBandDotComSong> songs;
        String          sValue;

        songs = DataAccess.GetDotComSongs();
        if( songs == null ) {
            return;
        }

        sValue = String.format( "%d songs", songs.size() );
        lblTitleData.setDefaultModel( new Model<String>(sValue));

        for( RockBandDotComSong s : songs ) {
            WebMarkupContainer trItem;
            WebMarkupContainer  tdItem;
            Label lblCategory;
            ExternalLink elMidiName;

            WebMarkupContainer  tdItemData;
            Label               lblItemData;

            String                  songTitle;

            trItem = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( trItem );

            tdItem = new WebMarkupContainer( "tdItem" );
            trItem.add( tdItem );

            tdItemData = new WebMarkupContainer( "tdItemData" );
            trItem.add( tdItemData );

            lblCategory = new Label( "lblCategory" );
            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            songTitle = String.format( "%s (%s)", s.getTitle(), s.getMidiTitle() );

            elMidiName = new ExternalLink( "elMidiName", "/dcsong?id=" + s.getMidiTitle(), songTitle );
            tdItem.add( elMidiName );

            // tdItemData
            lblItemData = new Label( "lblItemData", "" );
            tdItemData.add( lblItemData );
        }

        add( rptvSongs );
        return;
    }

}
