package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CMF;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReprocessRawSongs extends BaseAppEngineTaskList {

    private static final Logger log = Logger.getLogger(ReprocessRawSongs.class.getName());

    public ReprocessRawSongs() {
        _tasks.add(new FlagRawSongsTask());
        _tasks.add(new ReprocessRawSongsSubtask());
        setId( this.getClass().getName() );
        return;
    }

    public class FlagRawSongsTask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(FlagRawSongsTask.class.getName());

        public FlagRawSongsTask() {
            setId( this.getClass().getName() );
            return;
        }

        public Boolean Execute() {

            Boolean br;

            br = DataAccess.FlagRawSongsForReprocessing();
            if( br == false ) {
                return false;
            }

            log.log(Level.INFO, "Finished flagging songs for reprocessing.");
            return true;
        }
    }

    public class ReprocessRawSongsSubtask extends BaseAppEngineTask  {

        private final Logger log = Logger.getLogger(ReprocessRawSongsSubtask.class.getName());

        public ReprocessRawSongsSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove(CacheKeys.ReprocessedSongCount);
            return;
        }

        public Boolean Execute() {

            while( true ) {
                RockBandSongRaw rawSong;
                RockBandAnalyzerParams rbap;
                DrumsFullAnalysis dfa;
                Long reprocessedSongCount;

                rbap = new RockBandAnalyzerParams();

                reprocessedSongCount = (Long)CMF.get(CacheKeys.ReprocessedSongCount);
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
                    CMF.put(CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L);
                    continue;
                }

                DataAccess.UpdateSong( System.currentTimeMillis(), rawSong.getId(), dfa);
//            DataAccess.WriteSong( song );

                DataAccess.FlagRawSongAsProcessed(rawSong.getId());
                CMF.put(CacheKeys.ReprocessedSongCount, reprocessedSongCount + 1L);
                log.log(Level.INFO, String.format("Processed id=%d,fileName=%s", rawSong.getId(), rawSong.getOriginalFileName()));
            }

            DataAccess.SetLastUpdated();
            return true;
        }
    }
}
