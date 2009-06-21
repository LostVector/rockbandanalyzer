package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import org.apache.wicket.util.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 23, 2009
 * Time: 12:14:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class CacheEntities extends BaseAppEngineTaskList {

    private static final Logger log = Logger.getLogger(CacheEntities.class.getName());

    public CacheEntities() {
        _tasks.add( new UpdateStatsTask() );
        _interval = Duration.ONE_DAY.getMilliseconds();
        setId( this.getClass().getName() );
        return;
    }

    @Override
    public Boolean Finish() {
        super.Finish();
        log.log( Level.INFO, String.format("TryUpdatingStats completed.") );
        return true;
    }

    public class UpdateStatsTask extends BaseAppEngineTask  {

        private final Logger log = Logger.getLogger(UpdateStatsTask.class.getName());

        public UpdateStatsTask() {
            setId( this.getClass().getName() );
            return;
        }

        public Boolean Execute() {

            Long entityCount;

            entityCount = ChunkedDataAccess.Count("RockBandSong");
            if( entityCount != null ) {
                DataAccess.SetProperty(PropertyKeys.LoadedCount, entityCount.toString());
            }

            entityCount = ChunkedDataAccess.Count("RockBandDotComSong");
            if( entityCount != null ) {
                DataAccess.SetProperty(PropertyKeys.ScrapedCount, entityCount.toString());
            }

            return true;
        }
    }
}
