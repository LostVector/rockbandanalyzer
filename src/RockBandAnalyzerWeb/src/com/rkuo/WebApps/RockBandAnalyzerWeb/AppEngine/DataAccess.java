package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Key;
import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.PMF;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 25, 2009
 * Time: 11:01:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataAccess {

    public static List<RockBandSong> GetSongs() {

        Query               query;
        List<RockBandSong>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSong.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSong>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSong> GetSongsByDecade( int decade ) {

        Query               query;
        List<RockBandSong>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSong.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSong>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSong> GetSongsByDifficulty( int decade ) {

        Query               query;
        List<RockBandSong>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSong.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSong>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSong> GetSongsByLocation( RockBandLocation location ) {

        Query               query;
        List<RockBandSong>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSong.class);
        query.setOrdering("_midiTitle asc");
        query.setFilter("_location == LocationParam");
        query.declareParameters("String LocationParam");

        try {
            songs = (List<RockBandSong>) query.execute( location.ordinal() );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static boolean SongExists( String md5 ) {

        Query query;
        RockBandSong  rbSong;

        rbSong = null;

        PersistenceManager pm;

        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSong.class);
        query.setFilter("_md5 == MD5Param");
        query.declareParameters("String MD5Param");
        query.setUnique( true );
//        query.setResult( "_md5" );

        try {
            rbSong = (RockBandSong) query.execute( md5 );
        }
        finally {
            query.closeAll();
        }

        if( rbSong == null ) {
            return false;
        }

        return true;
    }

    public static void SongWrite( RockBandSong rbs ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent( rbs );
        } finally {
            pm.close();
        }

        return;
    }

    public static RockBandSong GetSongById( long id ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSong  rbSong;
        Key k;

        k = KeyFactory.createKey(RockBandSong.class.getSimpleName(), id);
        rbSong = (RockBandSong)pm.getObjectById( RockBandSong.class, id );

        return rbSong;
    }
}