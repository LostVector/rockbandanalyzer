package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;

public class SidebarPanel extends Panel {

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

    ExternalLink    elDuration;
    ExternalLink    elRBReleaseDate;

    ExternalLink    elBigRockEnding;
    ExternalLink    elCover;

    ExternalLink    elNotes;
    ExternalLink    elMaxScore;

    ExternalLink    elGlitched;
    ExternalLink    elSolos;
    ExternalLink    elNormalOptimal;
    ExternalLink    elBreakneckOptimal;
    ExternalLink    elGoldImpossible;

    public SidebarPanel() {
        this("panelSidebar");
        return;
    }

    public SidebarPanel(String id) {
        super(id);

        elDifficultyGuitar = new ExternalLink( "elDifficultyGuitar", "/browse?sort=difficultyguitar", "Difficulty - Guitar" );
        elDifficultyBass = new ExternalLink( "elDifficultyBass", "/browse?sort=difficultybass", "Difficulty - Bass" );
        elDifficultyDrums = new ExternalLink( "elDifficultyDrums", "/browse?sort=difficultydrums", "Difficulty - Drums" );
        elDifficultyVocals = new ExternalLink( "elDifficultyVocals", "/browse?sort=difficultyvocals", "Difficulty - Vocals" );
        elDifficultyBand = new ExternalLink( "elDifficultyBand", "/browse?sort=difficultyband", "Difficulty - Band" );
        elSong = new ExternalLink( "elSong", "/browse?sort=song", "Song" );
        elBand = new ExternalLink( "elBand", "/browse?sort=band", "Band" );
        elGenre = new ExternalLink( "elGenre", "/browse?sort=genre", "Genre" );
        elDecade = new ExternalLink( "elDecade", "/browse?sort=decade", "Decade" );
        elLocation = new ExternalLink( "elLocation", "/browse?sort=location", "Location" );

        elDuration = new ExternalLink( "elDuration", "/browse?sort=duration", "Duration" );
        elRBReleaseDate = new ExternalLink( "elRBReleaseDate", "/browse?sort=rb_release_date", "Date released on Rock Band" );

        elBigRockEnding = new ExternalLink( "elBigRockEnding", "/browse?filter=bigrockending", "Big Rock Ending" );
        elCover = new ExternalLink( "elCover", "/browse?filter=cover", "Cover" );

        elNotes = new ExternalLink( "elNotes", "/browse?sort=notes", "Notes" );
        elMaxScore = new ExternalLink( "elMaxScore", "/browse?sort=maxscore", "Max Score" );

        elGlitched = new ExternalLink( "elGlitched", "/browse?filter=glitched", "Glitched" );
        elSolos = new ExternalLink( "elSolos", "/browse?filter=solos", "Solo" );
        elNormalOptimal = new ExternalLink( "elNormalOptimal", "/browse?filter=normaloptimal", "Normal Optimal" );
        elBreakneckOptimal = new ExternalLink( "elBreakneckOptimal", "/browse?filter=breakneckoptimal", "Breakneck Optimal" );
        elGoldImpossible = new ExternalLink( "elGoldImpossible", "/browse?filter=goldimpossible", "Gold Impossible" );

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

        return;
    }

}
