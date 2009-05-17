package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import java.io.*;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.servlet.http.*;
import javax.servlet.ServletException;

import com.rkuo.util.RKTimer;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Services.BaseServlet;
import com.rkuo.RockBand.JTidyDomScraper;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.google.apphosting.api.DeadlineExceededException;

public class BatchServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(BatchServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Boolean br;

        String  key;

        key = request.getParameter( "key");
        if( key == null ) {
            return;
        }
        
        if( key.compareToIgnoreCase("28cd8050-428a-11de-8a39-0800200c9a66") != 0 ) {
            return;
        }
        
        br = TryFlaggingRawSongsForReprocessing();
        if( br == true ) {
            return;
        }

        br = TryReprocessingRawSongs();
        if( br == true ) {
            return;
        }

        br = TryScrapeDotComSongList();
        if( br == true ) {
            return;
        }

        br = TryDropMidiTitles();
        if( br == true ) {
            return;
        }

        br = TryWriteSongList();
        if( br == true ) {
            return;
        }

        br = TrySongDetailScrape();
        if( br == true ) {
            return;
        }

        return;
    } // doPost

    protected static Boolean TryFlaggingRawSongsForReprocessing() {

        Boolean br;

        br = getCache().containsKey( CacheKeys.FlagRawSongsForReprocessing );
        if( br == false ) {
            return false;
        }

        br = DataAccess.FlagRawSongsForReprocessing();
        if( br == false ) {
            return false;
        }

        getCache().put( CacheKeys.ReprocessRawSongs, true );
        getCache().remove( CacheKeys.FlagRawSongsForReprocessing );
        return true;
    }

    protected static Boolean TryReprocessingRawSongs() {

        Boolean br;

        br = getCache().containsKey( CacheKeys.ReprocessRawSongs );
        if( br == false ) {
            return false;
        }

        while( true ) {
            RockBandSongRaw rawSong;
            RockBandAnalyzerParams rbap;
            DrumsFullAnalysis dfa;
            Long    reprocessedSongCount;

            rbap = new RockBandAnalyzerParams();

            reprocessedSongCount = (Long)getCache().get( CacheKeys.ReprocessedSongCount );
            if( reprocessedSongCount == null ) {
                reprocessedSongCount = 0L;
            }

            rawSong = DataAccess.GetRawSongForReprocessing();
            if( rawSong == null ) {
                break;
            }

            dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(rawSong.getFile()), rbap);
            if( dfa == null ) {
                DataAccess.FlagRawSongAsProcessed( rawSong.getId() );
                getCache().put( CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L );
                continue;
            }

            DataAccess.UpdateSong( rawSong.getId(), dfa );
//            DataAccess.WriteSong( song );

            DataAccess.FlagRawSongAsProcessed( rawSong.getId() );
            getCache().put( CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L );
        }

        getCache().remove( CacheKeys.ReprocessedSongCount );
        getCache().remove( CacheKeys.ReprocessRawSongs );
        return true;
    }

    protected static Boolean TryScrapeDotComSongList() {

        List<String>    midiTitles;
        Boolean br;

        br = getCache().containsKey( CacheKeys.ScrapeDotComSongList );
        if( br == false ) {
            return false;
        }

        midiTitles = JTidyDomScraper.ScrapeSongs( "http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=" );
        if( midiTitles == null ) {
            log.log( Level.INFO, String.format( "ScrapeSongs failed." ) );
            getCache().remove( CacheKeys.ScrapeDotComSongList );
            return false;
        }


        getCache().put( CacheKeys.ScrapedMidiTitles, midiTitles );
        getCache().put( CacheKeys.DropMidiTitles, true );
        getCache().remove( CacheKeys.ScrapeDotComSongList );
        log.log( Level.INFO, String.format( "Scraped %d songs into memcache.", midiTitles.size() ) );
        return true;
    }

    protected static Boolean TryDropMidiTitles() {
        Boolean br;

        br = getCache().containsKey( CacheKeys.DropMidiTitles );
        if( br == false ) {
            return false;
        }

        log.log( Level.INFO, "Attempting to drop RockBandDotComSong." );
        DataAccess.DropTable( "RockBandDotComSong" );

        getCache().remove( CacheKeys.DropMidiTitles );
        log.log( Level.INFO, "Successfully dropped RockBandDotComSong." );
        return true;
    }
    
    protected static Boolean TryWriteSongList() {

        Boolean br;

        br = getCache().containsKey( CacheKeys.ScrapedMidiTitles );
        if( br == false ) {
            return false;
        }
/*
        String value;
        value = System.getProperty("debug");
        if( value != null ) {
            if( Boolean.parseBoolean(value) == true ) {
                log.log( Level.INFO, String.format( "Debug build: Calling localhost writeSongList." ) );
                Misc.GetXml( "http://localhost:8080/api/writeSongList" );
            }
        }
        else {
            Misc.GetXml( "http://rockbandanalyzer.appspot.com/api/writeSongList" );
        }
*/


        List<String>    midiTitles;
        Integer         writeCount;
        RKTimer t;

        t = new RKTimer();
        t.Start();

        midiTitles = (List<String>)getCache().get( CacheKeys.ScrapedMidiTitles );
        if( midiTitles == null ) {
            log.log( Level.INFO, String.format( "%s is not cached. Nothing to do.", CacheKeys.ScrapedMidiTitles ) );
            return false;
        }

        writeCount = 0;
        while( true ) {

            String  midiTitle;
            Long    loadedCount;

            loadedCount = (Long)getCache().get( CacheKeys.LoadedMidiTitlesCount );
            if( loadedCount == null ) {
                loadedCount = 0L;
            }

            if( midiTitles.size() == 0 ) {
                break;
            }

            midiTitle = midiTitles.get( 0 );

            DataAccess.WriteDotComSong( midiTitle );
            midiTitles.remove( 0 );
            getCache().put( CacheKeys.LoadedMidiTitlesCount, loadedCount + 1L );
            getCache().put( CacheKeys.ScrapedMidiTitles, midiTitles );
            writeCount++;
        }

        getCache().remove( CacheKeys.LoadedMidiTitlesCount );
        getCache().remove( CacheKeys.ScrapedMidiTitles );
        log.log( Level.INFO, String.format( "Wrote %d titles in %d ms", writeCount, t.Stop() ) );
        return true;
    }

    protected static Boolean TrySongDetailScrape() {

        Long    msDay;
        Long    lastClearTime;
        Long    now;

        msDay = 24L * 60L * 60L * 1000L;
        now = System.currentTimeMillis();

        lastClearTime = (Long)getCache().get( CacheKeys.LastSongDetailScrapePass );
        if( lastClearTime != null ) {
            if( now - lastClearTime < msDay ) {
                // Ignoring the request ... it hasn't been a full day since the last scan
//                log.log( Level.INFO,
//                        String.format( "TrySongDetailScrape exiting early. %d ms have elapsed, %d ms should elapse in total before resuming.",
//                                now - lastClearTime, msDay)
//                );

                return false;
            }
        }

        Integer x;
        RKTimer t;

        x=0;

        t = new RKTimer();
        t.Start();

        while( true ) {
            SortedMap<String,String> properties;
            RockBandDotComSong s;
            Long    scrapedSongDetailsCount;

            now = System.currentTimeMillis();

            scrapedSongDetailsCount = (Long)getCache().get( CacheKeys.ScrapedSongDetailsCount );
            if( scrapedSongDetailsCount == null ) {
                scrapedSongDetailsCount = 0L;
            }

            s = DataAccess.GetDotComSongForScraping( now );
            if( s == null ) {
                break;
            }

            if( s.getMidiTitle().length() == 0 ) {
                log.log( Level.INFO, String.format("Zero length midiTitle encountered. Skipping.") );
                continue;
            }

            try {
                properties = JTidyDomScraper.ScrapeSongDetail( "http://www.rockband.com/songs/" + s.getMidiTitle() );
            }
            catch( DeadlineExceededException deex ) {
                throw deex;
            }
            catch( Exception ex ) {
                log.log( Level.WARNING, String.format( "ScrapeSongDetail exceptioned on %s.", s.getMidiTitle() ) );
                getCache().put( CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L );
                DataAccess.FlagDotComSongAsAttempted( s.getId(), now );
                continue;
            }

            if( properties == null ) {
                log.log( Level.INFO, String.format( "ScrapeSongDetail failed on %s.", s.getMidiTitle() ) );
                getCache().put( CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L );
                DataAccess.FlagDotComSongAsAttempted( s.getId(), now );
                continue;
            }

            DataAccess.UpdateDotComSong( s.getMidiTitle(), now, properties );
            DataAccess.MergeDotComSong( s.getMidiTitle() );
            getCache().put( CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L );
            DataAccess.FlagDotComSongAsAttempted( s.getId(), now );
        }

        // There are no more attempts to make, so we're going to note that in the cache
        log.log( Level.INFO, String.format("Finished scraping song details. Noting it in memcache.") );
        getCache().remove( CacheKeys.ScrapedSongDetailsCount );
        getCache().put( CacheKeys.LastSongDetailScrapePass, now );
        log.log( Level.INFO, String.format( "Time elapsed: %d ms, %d iterations", t.Stop(), x ) );
        return true;
    }
}
