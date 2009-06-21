package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.google.apphosting.api.DeadlineExceededException;
import com.rkuo.RockBand.JTidyDomScraper;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CMF;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandDotComSong;
import com.rkuo.util.RKTimer;
import org.apache.wicket.util.time.Duration;

import java.util.List;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScrapeSongList extends BaseAppEngineTaskList {

    public ScrapeSongList() {
        _tasks.add(new ScrapeDotComSongListSubtask());
        _tasks.add(new DropMidiTitlesSubtask());
        _tasks.add(new TryWriteSongListSubtask());
        _tasks.add(new ScrapeSongDetailsSubtask());
        _interval = Duration.ONE_DAY.getMilliseconds();
        setId( this.getClass().getName() );
        return;
    }

    public class ScrapeDotComSongListSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(ScrapeDotComSongListSubtask.class.getName());

        public ScrapeDotComSongListSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove(CacheKeys.ScrapedMidiTitles);
            return;
        }

        public Boolean Execute() {

            List<String> midiTitles;

            midiTitles = JTidyDomScraper.ScrapeSongs("http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=");
            if( midiTitles == null ) {
                log.log(Level.INFO, String.format("ScrapeSongs failed."));
                return false;
            }

            CMF.put(CacheKeys.ScrapedMidiTitles, midiTitles);
            log.log(Level.INFO, String.format("Scraped %d songs into memcache.", midiTitles.size()));
            return true;
        }
    }

    public class DropMidiTitlesSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(DropMidiTitlesSubtask.class.getName());

        public DropMidiTitlesSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        public Boolean Execute() {

            log.log(Level.INFO, "Attempting to drop RockBandDotComSong.");
            DataAccess.DropTable("RockBandDotComSong");

            log.log(Level.INFO, "Successfully dropped RockBandDotComSong.");
            return true;
        }
    }

    public class TryWriteSongListSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(TryWriteSongListSubtask.class.getName());

        public TryWriteSongListSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove(CacheKeys.LoadedMidiTitlesCount);
            CMF.remove(CacheKeys.ScrapedMidiTitles);
            return;
        }

        public Boolean Execute() {

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
            RKTimer t;

            t = new RKTimer();
            t.Start();

            midiTitles = (List<String>)CMF.get(CacheKeys.ScrapedMidiTitles);
            if( midiTitles == null ) {
                log.log(Level.INFO, String.format("%s is not cached. Nothing to do.", CacheKeys.ScrapedMidiTitles));
                return false;
            }

            while( true ) {

                String midiTitle;
                Long loadedCount;

                loadedCount = (Long)CMF.get(CacheKeys.LoadedMidiTitlesCount);
                if( loadedCount == null ) {
                    loadedCount = 0L;
                }

                if( midiTitles.size() == 0 ) {
                    break;
                }

                midiTitle = midiTitles.get(0);

                DataAccess.WriteDotComSong(midiTitle);
                midiTitles.remove(0);
                CMF.put(CacheKeys.LoadedMidiTitlesCount, loadedCount + 1L);
                CMF.put(CacheKeys.ScrapedMidiTitles, midiTitles);
            }

            return true;
        }
    }

    public class ScrapeSongDetailsSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(ScrapeSongDetailsSubtask.class.getName());

        public ScrapeSongDetailsSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove(CacheKeys.ScrapedSongDetailsCount);
            return;
        }

        public Boolean Execute() {

            RKTimer t;

            t = new RKTimer();
            t.Start();

            while( true ) {
                SortedMap<String, String> properties;
                RockBandDotComSong s;
                Long    scrapedSongDetailsCount;
                Long    now;

                now = System.currentTimeMillis();

                scrapedSongDetailsCount = (Long)CMF.get(CacheKeys.ScrapedSongDetailsCount);
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
                    CMF.put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
                    DataAccess.DotComSongSetAttempted(s.getId(), now);
                    continue;
                }

                if( properties == null ) {
                log.log(Level.INFO, String.format("ScrapeSongDetail failed on %s.", s.getMidiTitle()));
                    CMF.put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
                    DataAccess.DotComSongSetAttempted(s.getId(), now);
                    continue;
                }

                DataAccess.UpdateDotComSong(s.getMidiTitle(), now, properties);
                DataAccess.MergeDotComSong( System.currentTimeMillis(), s.getMidiTitle());
                CMF.put(CacheKeys.ScrapedSongDetailsCount, scrapedSongDetailsCount + 1L);
                DataAccess.DotComSongSetAttempted(s.getId(), now);
            }

            DataAccess.SetLastUpdated();
            return true;
        }
    }
}
