package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.Util;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class BatchServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(BatchServlet.class.getName());

    protected ArrayList<IAppEngineTaskList> _taskLists;

    public BatchServlet() {
        super();

        _taskLists = new ArrayList<IAppEngineTaskList>();
        _taskLists.add(new UpdateSchema());
        _taskLists.add(new UpdateMissingSongs());
        _taskLists.add(new ReprocessRawSongs());
        _taskLists.add(new ScrapeSongList());
        _taskLists.add(new CacheEntities());
        return;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Boolean br;

        if( Util.IsDebug() == false ) {
            String key;

            key = request.getParameter("key");
            if( key == null ) {
                return;
            }

            if( key.compareToIgnoreCase("28cd8050-428a-11de-8a39-0800200c9a66") != 0 ) {
                return;
            }
        }

        for( IAppEngineTaskList tl : _taskLists ) {
            if( tl.ShouldRun() == false ) {
                continue;
            }

            for( int x = 0; x < tl.getTaskCount(); x++ ) {
                IAppEngineTask t;

                t = tl.getTask(x);

                if( t.ShouldRun() == false ) {
                    continue;
                }

                t.Start();

                br = t.Execute();
                if( br == false ) {
                    break;
                }

                t.Finish();
            }

            tl.Finish();
            break;
        }
/*
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

        br = TryUpdatingStats();
        if( br == true ) {
            return;
        }
 */
        return;
    } // doPost
/*
    protected static Boolean TryUpdatingSchema() {

        Long nextId;
        Boolean br;

        br = getCache().containsKey(CacheKeys.UpdateSchema);
        if( br == false ) {
            return false;
        }

        // It's fine if this returns null
        nextId = (Long)getCache().get(CacheKeys.NextUpdateSchemaEntityId);

        while( true ) {
            Long processedCount;
            Long newNextId;

            processedCount = (Long)getCache().get(CacheKeys.UpdatedSchemaCount);
            if( processedCount == null ) {
                processedCount = 0L;
            }

            newNextId = DataAccess.UpdateSchema(nextId);
            if( newNextId == null ) {
                break;
            }

            nextId = newNextId;
            getCache().put(CacheKeys.NextUpdateSchemaEntityId, nextId);
            getCache().put(CacheKeys.UpdatedSchemaCount, processedCount + 1L);
        }

        getCache().remove(CacheKeys.NextUpdateSchemaEntityId);
        getCache().remove(CacheKeys.UpdatedSchemaCount);
        getCache().remove(CacheKeys.UpdateSchema);
        log.log(Level.INFO, "Finished updating schema.");
        return true;
    }
 */
/*
    protected static Boolean TryFlaggingRawSongsForReprocessing() {

        Boolean br;

        br = getCache().containsKey(CacheKeys.FlagRawSongsForReprocessing);
        if( br == false ) {
            return false;
        }

        br = DataAccess.FlagRawSongsForReprocessing();
        if( br == false ) {
            return false;
        }

        getCache().put(CacheKeys.ReprocessRawSongs, true);
        getCache().remove(CacheKeys.FlagRawSongsForReprocessing);
        log.log(Level.INFO, "Finished flagging songs for reprocessing.");
        return true;
    }
 */
    /*
    protected static Boolean TryReprocessingRawSongs() {

        Boolean br;

        br = getCache().containsKey(CacheKeys.ReprocessRawSongs);
        if( br == false ) {
            return false;
        }

        while( true ) {
            RockBandSongRaw rawSong;
            RockBandAnalyzerParams rbap;
            DrumsFullAnalysis dfa;
            Long reprocessedSongCount;

            rbap = new RockBandAnalyzerParams();

            reprocessedSongCount = (Long)getCache().get(CacheKeys.ReprocessedSongCount);
            if( reprocessedSongCount == null ) {
                reprocessedSongCount = 0L;
            }

            rawSong = DataAccess.GetRawSongForReprocessing();
            if( rawSong == null ) {
                break;
            }

            dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(rawSong.getFile()), rbap);
            if( dfa == null ) {
                log.log(Level.WARNING, String.format("AnalyzeStream returned null. id=%d,fileName=%s", rawSong.getId(), rawSong.getOriginalFileName()));
                DataAccess.FlagRawSongAsProcessed(rawSong.getId());
                getCache().put(CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L);
                continue;
            }

            DataAccess.UpdateSong(rawSong.getId(), dfa);
//            DataAccess.WriteSong( song );

            DataAccess.FlagRawSongAsProcessed(rawSong.getId());
            getCache().put(CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L);
        }

        getCache().remove(CacheKeys.ReprocessedSongCount);
        getCache().remove(CacheKeys.ReprocessRawSongs);
        log.log(Level.INFO, "No songs left to reprocess.");
        return true;
    }

    protected static Boolean TryScrapeDotComSongList() {

        List<String> midiTitles;
        Boolean br;

        br = getCache().containsKey(CacheKeys.ScrapeDotComSongList);
        if( br == false ) {
            return false;
        }

        midiTitles = JTidyDomScraper.ScrapeSongs("http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=");
        if( midiTitles == null ) {
            log.log(Level.INFO, String.format("ScrapeSongs failed."));
            getCache().remove(CacheKeys.ScrapeDotComSongList);
            return false;
        }

        getCache().put(CacheKeys.ScrapedMidiTitles, midiTitles);
        getCache().put(CacheKeys.DropMidiTitles, true);
        getCache().remove(CacheKeys.ScrapeDotComSongList);
        log.log(Level.INFO, String.format("Scraped %d songs into memcache.", midiTitles.size()));
        return true;
    }

    protected static Boolean TryDropMidiTitles() {
        Boolean br;

        br = getCache().containsKey(CacheKeys.DropMidiTitles);
        if( br == false ) {
            return false;
        }

        log.log(Level.INFO, "Attempting to drop RockBandDotComSong.");
        DataAccess.DropTable("RockBandDotComSong");

        getCache().remove(CacheKeys.DropMidiTitles);
        log.log(Level.INFO, "Successfully dropped RockBandDotComSong.");
        return true;
    }

    protected static Boolean TryWriteSongList() {

        Boolean br;

        br = getCache().containsKey(CacheKeys.ScrapedMidiTitles);
        if( br == false ) {
            return false;
        }

//        String value;
//        value = System.getProperty("debug");
//        if( value != null ) {
//            if( Boolean.parseBoolean(value) == true ) {
//                log.log( Level.INFO, String.format( "Debug build: Calling localhost writeSongList." ) );
//                Misc.GetXml( "http://localhost:8080/api/writeSongList" );
//            }
//        }
//        else {
//            Misc.GetXml( "http://rockbandanalyzer.appspot.com/api/writeSongList" );
//        }



        List<String> midiTitles;
        Integer writeCount;
        RKTimer t;

        t = new RKTimer();
        t.Start();

        midiTitles = (List<String>)getCache().get(CacheKeys.ScrapedMidiTitles);
        if( midiTitles == null ) {
            log.log(Level.INFO, String.format("%s is not cached. Nothing to do.", CacheKeys.ScrapedMidiTitles));
            return false;
        }

        writeCount = 0;
        while( true ) {

            String midiTitle;
            Long loadedCount;

            loadedCount = (Long)getCache().get(CacheKeys.LoadedMidiTitlesCount);
            if( loadedCount == null ) {
                loadedCount = 0L;
            }

            if( midiTitles.size() == 0 ) {
                break;
            }

            midiTitle = midiTitles.get(0);

            DataAccess.WriteDotComSong(midiTitle);
            midiTitles.remove(0);
            getCache().put(CacheKeys.LoadedMidiTitlesCount, loadedCount + 1L);
            getCache().put(CacheKeys.ScrapedMidiTitles, midiTitles);
            writeCount++;
        }

        getCache().remove(CacheKeys.LoadedMidiTitlesCount);
        getCache().remove(CacheKeys.ScrapedMidiTitles);
        log.log(Level.INFO, String.format("Wrote %d titles in %d ms", writeCount, t.Stop()));
        return true;
    }

    protected static Boolean TrySongDetailScrape() {

        Long lastClearTime;
        Long now;

        Duration.ONE_DAY.getMilliseconds();

        now = System.currentTimeMillis();

        lastClearTime = (Long)getCache().get(CacheKeys.LastSongDetailScrapePass);
        if( lastClearTime != null ) {
            if( now - lastClearTime < Duration.ONE_DAY.getMilliseconds() ) {
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

        x = 0;

        t = new RKTimer();
        t.Start();

        while( true ) {
            SortedMap<String, String> properties;
            RockBandDotComSong s;
            Long scrapedSongDetailsCount;

            now = System.currentTimeMillis();

            scrapedSongDetailsCount = (Long)getCache().get(CacheKeys.ScrapedSongDetailsCount);
            if( scrapedSongDetailsCount == null ) {
                scrapedSongDetailsCount = 0L;
            }

            s = DataAccess.GetDotComSongForScraping(now);
            if( s == null ) {
                break;
            }

            if( s.getMidiTitle().length() == 0 ) {
                log.log(Level.INFO, String.format("Zero length midiTitle encountered. Skipping."));
                continue;
            }

            log.log(Level.INFO, String.format("ScrapeSongDetail on %s.", s.getMidiTitle()));

            try {
                properties = JTidyDomScraper.ScrapeSongDetail("http://www.rockband.com/songs/" + s.getMidiTitle());
            }
            catch( DeadlineExceededException deex ) {
                throw deex;
            }
            catch( Exception ex ) {
                log.log(Level.WARNING, String.format("ScrapeSongDetail exceptioned on %s.", s.getMidiTitle()));
                getCache().put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
                DataAccess.DotComSongSetAttempted(s.getId(), now);
                continue;
            }

            if( properties == null ) {
                log.log(Level.INFO, String.format("ScrapeSongDetail failed on %s.", s.getMidiTitle()));
                getCache().put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
                DataAccess.DotComSongSetAttempted(s.getId(), now);
                continue;
            }

            DataAccess.UpdateDotComSong(s.getMidiTitle(), now, properties);
            DataAccess.MergeDotComSong(s.getMidiTitle());
            getCache().put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
            DataAccess.DotComSongSetAttempted(s.getId(), now);
        }

        // There are no more attempts to make, so we're going to note that in the cache
        log.log(Level.INFO, String.format("Finished scraping song details. Noting it in memcache."));
        getCache().remove(CacheKeys.ScrapedSongDetailsCount);
        getCache().remove(CacheKeys.LastStatsUpdate);
        getCache().put(CacheKeys.LastSongDetailScrapePass, System.currentTimeMillis());
        log.log(Level.INFO, String.format("Time elapsed: %d ms, %d iterations", t.Stop(), x));
        return true;
    }

    protected static Boolean TryUpdatingStats() {


        RKTimer t = new RKTimer();

        t.Start();


        return false;
    }

    protected static Boolean TryUpdatingMissingSongs() {

        Hashtable<String, RockBandSong> songsMap;
        Long lastUpdate;
        Long now;

        now = System.currentTimeMillis();

        lastUpdate = (Long)getCache().get(CacheKeys.LastMissingSongsUpdate);
        if( lastUpdate != null ) {
            if( now - lastUpdate < Duration.ONE_DAY.getMilliseconds() ) {
                return false;
            }
        }

        DataAccess.SetProperty("RockBandDotComSong_Cached", "false");
        DataAccess.SetProperty("RockBandSong_Cached", "false");

        List<RockBandDotComSong> dcSongs;
        List<RockBandSong> songs;
        Boolean br;

        br = ChunkedDataAccess.CacheRockBandSongTable();
        br = ChunkedDataAccess.CacheRockBandDotComSongTable();

        songs = (ArrayList<RockBandSong>)getCache().get("RockBandSong");
        if( songs == null ) {
            log.log(Level.WARNING, String.format("songs == null"));
            return false;
        }

        dcSongs = (ArrayList<RockBandDotComSong>)getCache().get("RockBandDotComSong");
        if( dcSongs == null ) {
            log.log(Level.WARNING, String.format("dcSongs == null"));
            return false;
        }

        songsMap = new Hashtable<String, RockBandSong>();

        for( RockBandSong s : songs ) {
            songsMap.put(s.getGenerated().getMidiTitle(), s);
        }

        for( RockBandDotComSong dcSong : dcSongs ) {

            if( songsMap.containsKey(dcSong.getMidiTitle()) == false ) {
                DataAccess.DotComSongSetMissing(dcSong.getId(), true);
            }
            else {
                DataAccess.DotComSongSetMissing(dcSong.getId(), false);
            }
        }

        getCache().put(CacheKeys.LastMissingSongsUpdate, System.currentTimeMillis());
        log.log(Level.INFO, String.format("TryUpdatingMissingSongs completed."));
        return false;
    }
 */    
}
