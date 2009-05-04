package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;

import java.util.List;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongEmbedded;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrowsePage extends BasePage {

    ExternalLink    elDifficulty;
    ExternalLink    elSong;
    ExternalLink    elBand;
    ExternalLink    elGenre;
    ExternalLink    elDecade;
    ExternalLink    elLocation;

    public BrowsePage() {

//        lnkDifficulty = new Link( "lnkDifficulty", new )

        elDifficulty = new ExternalLink( "elDifficulty", "/browse?sort=difficulty", "Difficulty" );
        elSong = new ExternalLink( "elSong", "/browse?sort=song", "Song" );
        elBand = new ExternalLink( "elBand", "/browse?sort=band", "Band" );
        elGenre = new ExternalLink( "elGenre", "/browse?sort=genre", "Genre" );
        elDecade = new ExternalLink( "elDecade", "/browse?sort=decade", "Decade" );
        elLocation = new ExternalLink( "elLocation", "/browse?sort=location", "Location" );

        add( elDifficulty );
        add( elSong );
        add( elBand );
        add( elGenre );
        add( elDecade );
        add( elLocation );

        List<RockBandSongEmbedded> songs;
        RepeatingView      rptvSongs;

        rptvSongs = new RepeatingView("rptvSongs");

        songs = DataAccess.GetSongs();
        if( songs == null ) {
            return;
        }

        for( RockBandSongEmbedded s : songs ) {
            WebMarkupContainer  item;
            ExternalLink        elMidiName;

            item = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( item );

//            item.add( new Label("midiName", s.getMidiTitle()) );

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), s.getTitle() );
            item.add( elMidiName );
        }

        add( rptvSongs );
        return;
    }

}
