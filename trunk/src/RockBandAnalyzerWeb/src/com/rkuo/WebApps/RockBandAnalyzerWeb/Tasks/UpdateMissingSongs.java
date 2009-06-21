package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.*;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateMissingSongs extends BaseAppEngineTaskList {

    private static final Logger log = Logger.getLogger(UpdateMissingSongs.class.getName());

    public UpdateMissingSongs() {
        _tasks.add( new MemcacheMissingSongsSubtask() );
        _tasks.add( new PersistMissingSongsSubtask() );
        _interval = Duration.ONE_DAY.getMilliseconds();
        setId( this.getClass().getName() );
        return;
    }

    @Override
    public Boolean Finish() {

        super.Finish();
        log.log( Level.INFO, String.format("TryUpdatingMissingSongs completed.") );
        return true;
    }

    public class MemcacheMissingSongsSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(MemcacheMissingSongsSubtask.class.getName());

        public MemcacheMissingSongsSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove( "RockBandSong" );
            CMF.remove( "RockBandDotComSong" );
            CMF.remove( "RockBandDotComSongs_Missing" );
            return;
        }

        public Boolean Execute() {

            Hashtable<String, RockBandSong> songsMap;
            Hashtable<Long, Boolean>      missingSongsMap;
            List<RockBandDotComSong> dcSongs;
            List<RockBandSong> songs;

            missingSongsMap = new Hashtable<Long, Boolean>();

            songs = (ArrayList<RockBandSong>)CMF.get( "RockBandSong" );
            if( songs == null ) {
                return false;
            }

            dcSongs = (ArrayList<RockBandDotComSong>)CMF.get( "RockBandDotComSong" );
            if( dcSongs == null ) {
                return false;
            }

            songsMap = new Hashtable<String, RockBandSong>();

            for( RockBandSong s : songs ) {
                songsMap.put(s.getGenerated().getMidiTitle(), s);
            }

            for( RockBandDotComSong dcSong : dcSongs ) {
                if( songsMap.containsKey(dcSong.getMidiTitle()) == false ) {
                    missingSongsMap.put( dcSong.getId(), true );
                }
                else {
                    missingSongsMap.put( dcSong.getId(), false );
                }
            }

            CMF.put( "RockBandDotComSongs_Missing", missingSongsMap );
            return true;
        }
    }

    public class PersistMissingSongsSubtask extends BaseAppEngineTask {

        private final Logger log = Logger.getLogger(PersistMissingSongsSubtask.class.getName());

        public PersistMissingSongsSubtask() {
            setId( this.getClass().getName() );
            return;
        }

        public Boolean Execute() {

            Hashtable<Long, Boolean>      missingSongsMap;
            Hashtable<Long, Boolean>      missingSongsMapRemove;

            missingSongsMap = (Hashtable<Long, Boolean>)CMF.get( "RockBandDotComSongs_Missing" );
            if( missingSongsMap == null ) {
                return false;
            }

            missingSongsMapRemove = (Hashtable<Long, Boolean>)missingSongsMap.clone();

            // This doesn't need to be transactional b/c at worst
            // we'll just set the same value of an item again on the next run
            // if we exception here
            for( Long id : missingSongsMapRemove.keySet() ) {
                if( missingSongsMap.get(id) == false ) {
                    DataAccess.DotComSongSetMissing(id, true);
                }
                else {
                    DataAccess.DotComSongSetMissing(id, false);
                }

                missingSongsMap.remove( id );
                CMF.put( "RockBandDotComSongs_Missing", missingSongsMap );
            }

            return true;
        }
    }
}
