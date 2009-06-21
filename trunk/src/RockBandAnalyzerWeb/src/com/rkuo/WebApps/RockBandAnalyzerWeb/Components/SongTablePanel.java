package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.IModel;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongFilter;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandAdvancedSort;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandPrint;

import java.util.List;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class SongTablePanel extends Panel {

    private String          renderType;
    private RockBandAdvancedSort sortType;
    private RockBandSongFilter  filterType;
    private Label           lblTableDescription;
    private RepeatingView   rptvSongs;

    public SongTablePanel(String id) {
        super(id);

        renderType = "sort";
        sortType = RockBandAdvancedSort.Song;
        filterType = RockBandSongFilter.RecentlyReleased;

        lblTableDescription = new Label("lblTableDescription");
        add( lblTableDescription );

        rptvSongs = new RepeatingView("rptvSongs");
        add( rptvSongs );

        return;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }

    public RockBandAdvancedSort getSortType() {
        return sortType;
    }

    public void setSortType(RockBandAdvancedSort sortType) {
        this.sortType = sortType;
    }

    public RockBandSongFilter getFilterType() {
        return filterType;
    }

    public void setFilterType(RockBandSongFilter filterType) {
        this.filterType = filterType;
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();    //To change body of overridden methods use File | Settings | File Templates.

        String  sDescription;

        if( renderType.compareToIgnoreCase("sort") == 0 ) {
            RenderSortedSongs( sortType );
            sDescription = GetTableDescription( sortType );
        }
        else {
            RenderFilteredSongs( filterType );
            sDescription = GetTableDescription( filterType );
        }

        lblTableDescription.setDefaultModel( new Model<String>(sDescription) );
        return;
    }

    protected String GetTableDescription( RockBandAdvancedSort rbas ) {
        String  sDescription;

        switch( rbas ) {
            case DifficultyBand:
                sDescription = "Band Difficulty";
                break;
            case DifficultyGuitar:
                sDescription = "Guitar Difficulty";
                break;
            case DifficultyDrums:
                sDescription = "Drums Difficulty";
                break;
            case DifficultyVocals:
                sDescription = "Vocals Difficulty";
                break;
            case DifficultyBass:
                sDescription = "Bass Difficulty";
                break;
            case Song:
                sDescription = "Alphabetic by Song";
                break;
            case Band:
                sDescription = "Alphabetic by Band";
                break;
            case Genre:
                sDescription = "Genre";
                break;
            case Decade:
                sDescription = "Decade";
                break;
            case Location:
                sDescription = "Location";
                break;
            case Notes:
                sDescription = "By number of notes (does not include notes hidden under Big Rock Endings)";
                break;
            case MaxScore:
                sDescription = "By maximum possible score (either Normal or Breakneck scrolling speeds). Does not include points from Big Rock Endings.";
                break;
            default:
                sDescription = "";
                break;
        }

        return sDescription;
    }


    protected String GetTableDescription( RockBandSongFilter rbsf ) {
        String  sDescription;

        switch( rbsf ) {
            case BigRockEnding:
                sDescription = "Songs with Big Rock Endings";
                break;
            case Cover:
                sDescription = "Songs that are Covers";
                break;
            case Glitched:
                sDescription = "Songs that are glitched (notes detected unusually close together) Note: Not confident this stats is correct yet.";
                break;
            case Solos:
                sDescription = "Songs with solos";
                break;
            case NormalOptimal:
                sDescription = "Songs where the optimal score for Normal Speed is higher than the optimal score for Breakneck Speed";
                break;
            case BreakneckOptimal:
                sDescription = "Songs where the optimal score for Breakneck Speed is higher than the optimal score for Normal Speed";
                break;
            case GoldImpossible:
                sDescription = "Songs where it is impossible to get gold stars";
                break;
            case RecentlyReleased:
                sDescription = "Recently released songs";
                break;
            default:
                sDescription = "";
                break;
        }

        return sDescription;
    }

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

    protected void RenderFilteredSongs( RockBandSongFilter rbsf ) {
        List<RockBandSong> songs;

        songs = DataAccess.GetSongsByFilter( rbsf );
        if( songs == null ) {
            return;
        }

        for( RockBandSong s : songs ) {
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


            if( s.getAssociated().getTitle().length() == 0 ) {
                songTitle = "(MIDI) - " + s.getGenerated().getMidiTitle();
            }
            else {
                songTitle = s.getAssociated().getTitle();
            }

            lblCategory = new Label( "lblCategory", " by " + s.getAssociated().getArtist());
//            lblCategory.setVisible( false );
            tdItem.add( lblCategory );

            elMidiName = new ExternalLink( "elMidiName", "/song?id=" + s.getId(), songTitle );
            tdItem.add( elMidiName );

            // tdItemData
            lblItemData = new Label( "lblItemData", GetFilterItemData(s,rbsf) );
            tdItemData.add( lblItemData );
        }

        return;
    }

    protected String GetFilterItemData( RockBandSong s, RockBandSongFilter rbsf ) {

        String  sData;

        sData = "";
        if( rbsf == RockBandSongFilter.BreakneckOptimal ) {
            sData = String.format("%d -> %d (+%d)",
                    s.getGenerated().getNormalOptimalScore(),
                    s.getGenerated().getBreakneckOptimalScore(),
                    s.getGenerated().getBreakneckOptimalScore() - s.getGenerated().getNormalOptimalScore() );
        }
        else if( rbsf == RockBandSongFilter.NormalOptimal ) {
            sData = String.format("%d -> %d (+%d)",
                    s.getGenerated().getBreakneckOptimalScore(),
                    s.getGenerated().getNormalOptimalScore(),
                    s.getGenerated().getNormalOptimalScore() - s.getGenerated().getBreakneckOptimalScore() );
        }
        else if( rbsf == RockBandSongFilter.GoldImpossible ) {
            sData = String.format( "Max: %d, Gold: %d", s.getGenerated().getMaxScore(), s.getGenerated().getStarCutoffGold() );
        }
        else if( rbsf == RockBandSongFilter.Solos ) {
            sData = String.format( "%d", s.getGenerated().getSolos() );
        }
        else if( rbsf == RockBandSongFilter.RecentlyReleased ) {
            Date d;

            d = s.getAssociated().getRBReleaseDate();
            sData = String.format( "%d/%02d/%02d", d.getYear()+1900, d.getMonth()+1, d.getDate() );
        }
        else {
            sData = "";
        }

        return sData;
    }
}
