package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.PageParameters;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.model.Model;

import java.util.List;
import java.util.GregorianCalendar;
import java.util.Calendar;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import com.rkuo.RockBand.RockBandSort;

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

    ExternalLink    elDuration;
    ExternalLink    elNotes;
    ExternalLink    elMaxScore;

    ExternalLink    elGlitched;
    ExternalLink    elBigRockEnding;
    ExternalLink    elSolos;
    ExternalLink    elBreakneckOptimal;
    ExternalLink    elGoldImpossible;

    public BrowsePage( PageParameters params ) {

//        lnkDifficulty = new Link( "lnkDifficulty", new )

        elDifficulty = new ExternalLink( "elDifficulty", "/browse?sort=difficulty", "Difficulty" );
        elSong = new ExternalLink( "elSong", "/browse?sort=song", "Song" );
        elBand = new ExternalLink( "elBand", "/browse?sort=band", "Band" );
        elGenre = new ExternalLink( "elGenre", "/browse?sort=genre", "Genre" );
        elDecade = new ExternalLink( "elDecade", "/browse?sort=decade", "Decade" );
        elLocation = new ExternalLink( "elLocation", "/browse?sort=location", "Location" );

        elDuration = new ExternalLink( "elDuration", "/browse?sort=duration", "Duration" );
        elNotes = new ExternalLink( "elNotes", "/browse?sort=notes", "Notes" );
        elMaxScore = new ExternalLink( "elMaxScore", "/browse?sort=maxscore", "Max Score" );

        elGlitched = new ExternalLink( "elGlitched", "/browse?filter=glitched", "Glitched" );
        elBigRockEnding = new ExternalLink( "elBigRockEnding", "/browse?filter=bigrockending", "Big Rock Ending" );
        elSolos = new ExternalLink( "elSolos", "/browse?filter=solos", "Solo" );
        elBreakneckOptimal = new ExternalLink( "elBreakneckOptimal", "/browse?filter=breakneckoptimal", "Breakneck Optimal" );
        elGoldImpossible = new ExternalLink( "elGoldImpossible", "/browse?filter=goldimpossible", "Gold Impossible" );

        add( elDifficulty );
        add( elSong );
        add( elBand );
        add( elGenre );
        add( elDecade );
        add( elLocation );

        add( elGlitched );
        add( elDuration );
        add( elBigRockEnding );
        add( elNotes );
        add( elMaxScore );
        add( elSolos );
        add( elBreakneckOptimal );
        add( elGoldImpossible );

        if( params.containsKey("sort") == true ) {
            String sortParam;

            sortParam = params.getString("sort");

            if( sortParam.compareToIgnoreCase("song") == 0 ) {
                RenderSortedSongs( RockBandSort.Song );
            }
            else if( sortParam.compareToIgnoreCase("band") == 0 ) {
                RenderSortedSongs( RockBandSort.Band );
            }
            else if( sortParam.compareToIgnoreCase("genre") == 0 ) {
                RenderSortedSongs( RockBandSort.Genre );
            }
            else if( sortParam.compareToIgnoreCase("decade") == 0 ) {
                RenderSortedSongs( RockBandSort.Decade );
            }
            else if( sortParam.compareToIgnoreCase("location") == 0 ) {
                RenderSortedSongs( RockBandSort.Location );
            }
            else if( sortParam.compareToIgnoreCase("difficultyguitar") == 0 ) {
                RenderSortedSongs( RockBandSort.DifficultyGuitar );
            }
            else if( sortParam.compareToIgnoreCase("difficultybass") == 0 ) {
                RenderSortedSongs( RockBandSort.DifficultyBass );
            }
            else if( sortParam.compareToIgnoreCase("difficultydrums") == 0 ) {
                RenderSortedSongs( RockBandSort.DifficultyDrums );
            }
            else if( sortParam.compareToIgnoreCase("difficultyvocals") == 0 ) {
                RenderSortedSongs( RockBandSort.DifficultyVocals );
            }
            else if( sortParam.compareToIgnoreCase("difficultyband") == 0 ) {
                RenderSortedSongs( RockBandSort.DifficultyBand );
            }
            else {
                RenderSortedSongs( RockBandSort.Song );
            }
        }
        else if( params.containsKey("filter") == true ) {
            String filterParam;

            filterParam = params.getString("filter");
            if( filterParam.compareToIgnoreCase("glitched") == 0 ) {
                RenderFilteredSongs( RockBandSongFilter.Glitched );
            }
            else if( filterParam.compareToIgnoreCase("breakneckoptimal") == 0 ) {
                RenderFilteredSongs( RockBandSongFilter.BreakneckOptimal );
            }
            else if( filterParam.compareToIgnoreCase("solos") == 0 ) {
                RenderFilteredSongs( RockBandSongFilter.Solos );
            }
            else if( filterParam.compareToIgnoreCase("goldimpossible") == 0 ) {
                RenderFilteredSongs( RockBandSongFilter.GoldImpossible );
            }
            else if( filterParam.compareToIgnoreCase("bigrockending") == 0 ) {
                RenderFilteredSongs( RockBandSongFilter.BigRockEnding );
            }
            else {
                RenderSortedSongs( RockBandSort.Song );
            }
        }
        else {
            RenderSortedSongs( RockBandSort.Song );
        }

        return;
    }

    protected void RenderSortedSongs( RockBandSort rbs ) {

        List<RockBandSongEmbedded> songs;
        RepeatingView      rptvSongs;
        String              sLastCategory;

        sLastCategory = "";

        rptvSongs = new RepeatingView("rptvSongs");
        add( rptvSongs );

        songs = DataAccess.GetSongs( rbs );
        if( songs == null ) {
            return;
        }

        for( RockBandSongEmbedded s : songs ) {
            WebMarkupContainer  trItem;
            WebMarkupContainer  tdItem;
            ExternalLink        elMidiName;
            String              songTitle;

            trItem = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( trItem );

            tdItem = new WebMarkupContainer( "tdItem" );
            trItem.add( tdItem );
//            item.add( new Label("midiName", s.getMidiTitle()) );

            Label lblCategory;
            String  sCategory;

            sCategory = GetSortCategory(s,rbs);

            if( sLastCategory.compareTo( sCategory ) != 0 ) {
                lblCategory = new Label( "lblCategory", sCategory );
                sLastCategory = sCategory;
                tdItem.add( lblCategory );
                elMidiName = new ExternalLink( "elMidiName", "/song?id=1", "" );
                elMidiName.setVisible( false );
                tdItem.add( elMidiName );

                tdItem.add( new SimpleAttributeModifier("class","category") ); 

                trItem = new WebMarkupContainer( rptvSongs.newChildId() );
                rptvSongs.add( trItem );

                tdItem = new WebMarkupContainer( "tdItem" );
                trItem.add( tdItem );
            }

            lblCategory = new Label( "lblCategory" );
            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            if( s.getTitle().length() == 0 ) {
                songTitle = "(MIDI) - " + s.getMidiTitle();
            }
            else {
                songTitle = s.getTitle();
            }

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), songTitle );
            tdItem.add( elMidiName );
        }

        return;
    }

    protected String GetSortCategory( RockBandSongEmbedded s, RockBandSort rbs ) {

        String  sCategory;

        if( rbs == RockBandSort.Song ) {
            if( s.getTitle() == null ) {
                sCategory = "NULL";
            }
            else if( s.getTitle().length() == 0 ) {
                sCategory = "NULL";
            }
            else {
                sCategory = s.getTitle().substring(0, 1);
            }
        }
        else if( rbs == RockBandSort.Band ) {
            sCategory = s.getArtist();
        }
        else if( rbs == RockBandSort.Genre ) {
            sCategory = s.getGenre();
        }
        else if( rbs == RockBandSort.Decade ) {
            GregorianCalendar cal;
            int decade;

            cal = new GregorianCalendar();
            cal.setTime( s.getDateReleased() );
            decade = cal.get( Calendar.YEAR );
            decade = decade - (decade % 10);

            sCategory = String.format( "%d", decade );
        }
        else if( rbs == RockBandSort.Location ) {
            sCategory = s.getLocation().name();
        }
        else if( rbs == RockBandSort.DifficultyGuitar ) {
            sCategory = s.getGuitarDifficulty().name();
        }
        else if( rbs == RockBandSort.DifficultyBass ) {
            sCategory = s.getBassDifficulty().name();
        }
        else if( rbs == RockBandSort.DifficultyDrums ) {
            sCategory = s.getDrumsDifficulty().name();
        }
        else if( rbs == RockBandSort.DifficultyVocals ) {
            sCategory = s.getVocalsDifficulty().name();
        }
        else if( rbs == RockBandSort.DifficultyBand ) {
            sCategory = s.getBandDifficulty().name();
        }
        else {
            sCategory = "NULL";
        }

        return sCategory;
    }

    protected void RenderFilteredSongs( RockBandSongFilter rbsf ) {
        List<RockBandSongGenerated> songs;
        RepeatingView      rptvSongs;

        rptvSongs = new RepeatingView("rptvSongs");

        songs = DataAccess.GetSongsByFilter( rbsf );
        if( songs == null ) {
            return;
        }

        for( RockBandSongGenerated gs : songs ) {
            WebMarkupContainer  trItem;
            WebMarkupContainer  tdItem;

            Label               lblCategory;
            ExternalLink        elMidiName;

            RockBandSongEmbedded    s;
            String                  songTitle;

            trItem = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( trItem );

            tdItem = new WebMarkupContainer( "tdItem" );
            trItem.add( tdItem );

            lblCategory = new Label( "lblCategory" );
            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            s = DataAccess.GetEmbeddedSongById( gs.getId() );
            if( s.getTitle().length() == 0 ) {
                songTitle = "(MIDI) - " + s.getMidiTitle();
            }
            else {
                songTitle = s.getTitle();
            }

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), songTitle );
            tdItem.add( elMidiName );
        }

        add( rptvSongs );
        return;
    }
}
