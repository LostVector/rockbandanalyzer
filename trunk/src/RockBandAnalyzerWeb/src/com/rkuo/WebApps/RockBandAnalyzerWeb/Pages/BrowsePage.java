package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.SimpleAttributeModifier;

import java.util.List;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.SongTablePanel;
import com.rkuo.RockBand.*;

public class BrowsePage extends BasePage {

    ExternalLink    elDifficultyGuitar;
    ExternalLink    elDifficultyBass;
    ExternalLink    elDifficultyDrums;
    ExternalLink    elDifficultyVocals;
    ExternalLink    elDifficultyBand;
    ExternalLink    elSong;
    ExternalLink    elBand;
    ExternalLink    elGenre;
    ExternalLink    elDecade;
    ExternalLink    elLocation;

    ExternalLink    elBigRockEnding;
    ExternalLink    elCover;

    ExternalLink    elDuration;
    ExternalLink    elRBReleaseDate;

    ExternalLink    elNotes;
    ExternalLink    elMaxScore;

    ExternalLink    elGlitched;
    ExternalLink    elSolos;
    ExternalLink    elNormalOptimal;
    ExternalLink    elBreakneckOptimal;
    ExternalLink    elGoldImpossible;

//    RepeatingView      rptvSongs;
    private SongTablePanel panelSongs;

    public BrowsePage( PageParameters params ) {

//        lnkDifficulty = new Link( "lnkDifficulty", new )

        elDifficultyGuitar = new ExternalLink( "elDifficultyGuitar", "/browse?sort=difficultyguitar", "Guitar" );
        elDifficultyBass = new ExternalLink( "elDifficultyBass", "/browse?sort=difficultybass", "Bass" );
        elDifficultyDrums = new ExternalLink( "elDifficultyDrums", "/browse?sort=difficultydrums", "Drums" );
        elDifficultyVocals = new ExternalLink( "elDifficultyVocals", "/browse?sort=difficultyvocals", "Vocals" );
        elDifficultyBand = new ExternalLink( "elDifficultyBand", "/browse?sort=difficultyband", "Band" );
        elSong = new ExternalLink( "elSong", "/browse?sort=song", "Song" );
        elBand = new ExternalLink( "elBand", "/browse?sort=band", "Band" );
        elGenre = new ExternalLink( "elGenre", "/browse?sort=genre", "Genre" );
        elDecade = new ExternalLink( "elDecade", "/browse?sort=decade", "Decade" );
        elLocation = new ExternalLink( "elLocation", "/browse?sort=location", "Location" );

        elBigRockEnding = new ExternalLink( "elBigRockEnding", "/browse?filter=bigrockending", "Big Rock Ending" );
        elCover = new ExternalLink( "elCover", "/browse?filter=cover", "Cover" );

        elDuration = new ExternalLink( "elDuration", "/browse?sort=duration", "Duration" );
        elRBReleaseDate = new ExternalLink( "elRBReleaseDate", "/browse?sort=rb_release_date", "Date released on Rock Band" );

        elNotes = new ExternalLink( "elNotes", "/browse?sort=notes", "Notes" );
        elMaxScore = new ExternalLink( "elMaxScore", "/browse?sort=maxscore", "Max Score" );

        elGlitched = new ExternalLink( "elGlitched", "/browse?filter=glitched", "Glitched" );
        elSolos = new ExternalLink( "elSolos", "/browse?filter=solos", "Solo" );
        elNormalOptimal = new ExternalLink( "elNormalOptimal", "/browse?filter=normaloptimal", "Normal Optimal" );
        elBreakneckOptimal = new ExternalLink( "elBreakneckOptimal", "/browse?filter=breakneckoptimal", "Breakneck Optimal" );
        elGoldImpossible = new ExternalLink( "elGoldImpossible", "/browse?filter=goldimpossible", "Gold Impossible" );

        panelSongs = new SongTablePanel("panelSongs");
//        rptvSongs = new RepeatingView("rptvSongs");

        add( elDifficultyGuitar );
        add( elDifficultyBass );
        add( elDifficultyDrums );
        add( elDifficultyVocals );
        add( elDifficultyBand );
        add( elSong );
        add( elBand );
        add( elGenre );
        add( elDecade );
        add( elLocation );

        add( elDuration );
        add( elRBReleaseDate );

        add( elBigRockEnding );
        add( elCover );

        add( elNotes );
        add( elMaxScore );

        add( elGlitched );
        add( elSolos );
        add( elNormalOptimal );
        add( elBreakneckOptimal );
        add( elGoldImpossible );

        add( panelSongs );

//        add( rptvSongs );

        if( params.containsKey("sort") == true ) {
            String sortParam;

            panelSongs.setRenderType( "sort" ) ;

            sortParam = params.getString("sort");
            if( sortParam.compareToIgnoreCase("song") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Song );
            }
            else if( sortParam.compareToIgnoreCase("band") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Band );
            }
            else if( sortParam.compareToIgnoreCase("genre") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Genre );
            }
            else if( sortParam.compareToIgnoreCase("decade") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Decade );
            }
            else if( sortParam.compareToIgnoreCase("location") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Location );
            }
            else if( sortParam.compareToIgnoreCase("difficultyguitar") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.DifficultyGuitar );
            }
            else if( sortParam.compareToIgnoreCase("difficultybass") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.DifficultyBass );
            }
            else if( sortParam.compareToIgnoreCase("difficultydrums") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.DifficultyDrums );
            }
            else if( sortParam.compareToIgnoreCase("difficultyvocals") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.DifficultyVocals );
            }
            else if( sortParam.compareToIgnoreCase("difficultyband") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.DifficultyBand );
            }
            else if( sortParam.compareToIgnoreCase("duration") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Duration );
            }
            else if( sortParam.compareToIgnoreCase("rb_release_date") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.RBReleaseDate );
            }
            else if( sortParam.compareToIgnoreCase("notes") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.Notes );
            }
            else if( sortParam.compareToIgnoreCase("maxscore") == 0 ) {
                panelSongs.setSortType( RockBandAdvancedSort.MaxScore );
            }
            else {
                panelSongs.setSortType( RockBandAdvancedSort.Song );
            }
        }
        else if( params.containsKey("filter") == true ) {
            String filterParam;

            panelSongs.setRenderType( "filter") ;

            filterParam = params.getString("filter");
            if( filterParam.compareToIgnoreCase("bigrockending") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.BigRockEnding );
            }
            else if( filterParam.compareToIgnoreCase("cover") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.Cover );
            }
            else if( filterParam.compareToIgnoreCase("glitched") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.Glitched );
            }
            else if( filterParam.compareToIgnoreCase("normaloptimal") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.NormalOptimal );
            }
            else if( filterParam.compareToIgnoreCase("breakneckoptimal") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.BreakneckOptimal );
            }
            else if( filterParam.compareToIgnoreCase("solos") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.Solos );
            }
            else if( filterParam.compareToIgnoreCase("goldimpossible") == 0 ) {
                panelSongs.setFilterType( RockBandSongFilter.GoldImpossible );
            }
            else {
                panelSongs.setFilterType( RockBandSongFilter.BigRockEnding );
            }
        }
        else {
            panelSongs.setRenderType( "sort" ) ;
            panelSongs.setSortType( RockBandAdvancedSort.Song );
        }

        return;
    }
/*
    protected void RenderSortedSongs( RockBandAdvancedSort rbs ) {

        List<RockBandSong> songs;
        String              sLastCategory;

        sLastCategory = "";

        songs = DataAccess.GetSongs( rbs );
        if( songs == null ) {
            return;
        }

        for( RockBandSong s : songs ) {
            WebMarkupContainer  trItem;

            WebMarkupContainer  tdItem;
            Label lblCategory;
            ExternalLink        elMidiName;

            WebMarkupContainer  tdItemData;
            Label               lblItemData;

            String              songTitle;

            trItem = new WebMarkupContainer( rptvSongs.newChildId() );
            rptvSongs.add( trItem );

            tdItem = new WebMarkupContainer( "tdItem" );
            trItem.add( tdItem );

            tdItemData = new WebMarkupContainer( "tdItemData" );
            trItem.add( tdItemData );

            String  sCategory;

            sCategory = GetSortCategory(s,rbs);

            if( sLastCategory.compareTo( sCategory ) != 0 ) {
                // tdItem
                lblCategory = new Label( "lblCategory", sCategory );
                sLastCategory = sCategory;
                tdItem.add( lblCategory );

                elMidiName = new ExternalLink( "elMidiName", "/song?id=1", "" );
                elMidiName.setVisible( false );
                tdItem.add( elMidiName );

                tdItem.add( new SimpleAttributeModifier("class","category") );

                // tdItemData
                lblItemData = new Label( "lblItemData", "" );
                tdItemData.add( lblItemData );

                tdItemData.add( new SimpleAttributeModifier("class","category") );

                // Create another row
                trItem = new WebMarkupContainer( rptvSongs.newChildId() );
                rptvSongs.add( trItem );

                tdItem = new WebMarkupContainer( "tdItem" );
                trItem.add( tdItem );

                tdItemData = new WebMarkupContainer( "tdItemData" );
                trItem.add( tdItemData );
            }

            // tdItem
            lblCategory = new Label( "lblCategory" );
            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            if( s.getAssociated().getTitle().length() == 0 ) {
                songTitle = "(MIDI) - " + s.getGenerated().getMidiTitle();
            }
            else {
                songTitle = s.getAssociated().getTitle();
            }

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), songTitle );
            tdItem.add( elMidiName );

            // tdItemData
            lblItemData = new Label( "lblItemData", GetSortItemData(s,rbs) );
            tdItemData.add( lblItemData );
        }

        return;
    }

    protected String GetSortCategory( RockBandSong s, RockBandAdvancedSort rbs ) {

        String  sCategory;

        if( rbs == RockBandAdvancedSort.Song ) {
            if( s.getAssociated().getTitle() == null ) {
                sCategory = "NULL";
            }
            else if( s.getAssociated().getTitle().length() == 0 ) {
                sCategory = "NULL";
            }
            else {
                Character   c;

                c = s.getAssociated().getTitle().charAt(0);
                if( Character.isDigit(c) == true ) {
                    sCategory = "123";
                }
                else if( Character.isLetter(c) == true ) {
                    sCategory = c.toString();
                    sCategory = sCategory.toUpperCase();
                }
                else {
                    sCategory = "...";
                }
            }
        }
        else if( rbs == RockBandAdvancedSort.Band ) {
            sCategory = s.getAssociated().getArtist();
        }
        else if( rbs == RockBandAdvancedSort.Genre ) {
            sCategory = s.getAssociated().getGenre();
        }
        else if( rbs == RockBandAdvancedSort.Decade ) {
            GregorianCalendar cal;
            int decade;

            cal = new GregorianCalendar();
            cal.setTime( s.getAssociated().getDateReleased() );
            decade = cal.get( Calendar.YEAR );
            decade = decade - (decade % 10);

            sCategory = String.format( "%d", decade );
        }
        else if( rbs == RockBandAdvancedSort.Location ) {
            sCategory = s.getAssociated().getLocation().name();
        }
        else if( rbs == RockBandAdvancedSort.DifficultyGuitar ) {
            sCategory = s.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Guitar ).name();
        }
        else if( rbs == RockBandAdvancedSort.DifficultyBass ) {
            sCategory = s.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Bass ).name();
        }
        else if( rbs == RockBandAdvancedSort.DifficultyDrums ) {
            sCategory = s.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Drums ).name();
        }
        else if( rbs == RockBandAdvancedSort.DifficultyVocals ) {
            sCategory = s.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Vocals ).name();
        }
        else if( rbs == RockBandAdvancedSort.DifficultyBand ) {
            sCategory = s.getAssociated().getDifficulty( RockBandInstrumentDifficultyCategory.Band ).name();
        }
        else if( rbs == RockBandAdvancedSort.Duration ) {
            Long    microseconds;
            Long    minutes;

            microseconds = s.getGenerated().getMicroseconds();
            minutes = microseconds / (60 * 1000000);

            sCategory = minutes.toString() + " min";
        }
        else if( rbs == RockBandAdvancedSort.RBReleaseDate ) {
            sCategory = "All dates";
        }
        else if( rbs == RockBandAdvancedSort.Notes ) {
            Long    notesTimes500;
            Long    noteBucket;

            notesTimes500 =  s.getGenerated().getNotes() / 500;
            noteBucket = notesTimes500 * 500;
            sCategory = noteBucket.toString() + "+";
        }
        else if( rbs == RockBandAdvancedSort.MaxScore ) {
            Long    scoreTimes50k;
            Long    scoreBucket;

            scoreTimes50k =  s.getGenerated().getMaxScore() / 50000;
            scoreBucket = scoreTimes50k * 50000;
            sCategory = scoreBucket.toString() + "+";
        }
        else {
            sCategory = "NULL";
        }

        return sCategory;
    }

    protected String GetSortItemData( RockBandSong s, RockBandAdvancedSort rbs ) {

        String  sData;

        sData = "";
        if( rbs == RockBandAdvancedSort.Song ) {
        }
        else if( rbs == RockBandAdvancedSort.Band ) {
        }
        else if( rbs == RockBandAdvancedSort.Genre ) {
        }
        else if( rbs == RockBandAdvancedSort.Decade ) {
        }
        else if( rbs == RockBandAdvancedSort.Location ) {
        }
        else if( rbs == RockBandAdvancedSort.DifficultyGuitar ) {
        }
        else if( rbs == RockBandAdvancedSort.DifficultyBass ) {
        }
        else if( rbs == RockBandAdvancedSort.DifficultyDrums ) {
        }
        else if( rbs == RockBandAdvancedSort.DifficultyVocals ) {
        }
        else if( rbs == RockBandAdvancedSort.DifficultyBand ) {
        }
        else if( rbs == RockBandAdvancedSort.Duration ) {
            sData = RockBandPrint.MicrosecondsToString( s.getGenerated().getMicroseconds() );
        }
        else if( rbs == RockBandAdvancedSort.RBReleaseDate ) {
            Date d;

            d = s.getAssociated().getRBReleaseDate();
            sData = String.format( "%d/%02d/%02d", d.getYear()+1900, d.getMonth()+1, d.getDate() );
        }
        else if( rbs == RockBandAdvancedSort.Notes ) {
            Long    noteCount;

            noteCount = s.getGenerated().getNotes();
            sData = noteCount.toString();
        }
        else if( rbs == RockBandAdvancedSort.MaxScore ) {
            Long    maxScore;

            maxScore = s.getGenerated().getMaxScore();
            sData = maxScore.toString();
        }
        else {
            sData = "";
        }

        return sData;
    }

    protected String GetFilterItemData( RockBandSong s, RockBandSongFilter rbsf ) {

        String  sData;

        sData = "";
        if( rbsf == RockBandSongFilter.BreakneckOptimal ) {
//            sData = String.format("%d -> %d (+%d)",
//                    s.getGenerated().getOptimalRB2NormalScore(),
//                    s.getGenerated().getOptimalRB2BreakneckScore(),
//                    s.getGenerated().getOptimalRB2NormalScore(),
// )
        }
        else if( rbsf == RockBandSongFilter.NormalOptimal ) {
        }
        else if( rbsf == RockBandSongFilter.GoldImpossible ) {
            sData = String.format( "Max: %d, Gold: %d", s.getGenerated().getMaxScore(), s.getGenerated().getStarCutoffGold() );
        }
        else if( rbsf == RockBandSongFilter.Solos ) {
            sData = String.format( "%d", s.getGenerated().getSolos() );
        }
        else {
            sData = "";
        }

        return sData;
    }

    protected void RenderFilteredSongs( RockBandSongFilter rbsf ) {
        List<RockBandSong> songs;

        songs = DataAccess.GetSongsByFilter( rbsf );
        if( songs == null ) {
            return;
        }

        for( RockBandSong s : songs ) {
            WebMarkupContainer  trItem;
            WebMarkupContainer  tdItem;
            Label               lblCategory;
            ExternalLink        elMidiName;

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

            if( s.getAssociated().getTitle().length() == 0 ) {
                songTitle = "(MIDI) - " + s.getGenerated().getMidiTitle();
            }
            else {
                songTitle = s.getAssociated().getTitle();
            }

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), songTitle );
            tdItem.add( elMidiName );

            // tdItemData
            lblItemData = new Label( "lblItemData", GetFilterItemData(s,rbsf) );
            tdItemData.add( lblItemData );
        }

        return;
    }
 */
}
