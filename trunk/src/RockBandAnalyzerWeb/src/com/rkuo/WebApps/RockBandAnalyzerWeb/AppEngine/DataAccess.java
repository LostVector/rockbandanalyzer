package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
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

    public static List<RockBandSongEmbedded> GetSongs() {

        Query               query;
        List<RockBandSongEmbedded>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongEmbedded.class);
        query.setOrdering("_title asc");

        try {
            songs = (List<RockBandSongEmbedded>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSongRaw> GetSongsByDecade( int decade ) {

        Query               query;
        List<RockBandSongRaw>  songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSongRaw>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSongRaw> GetSongsByDifficulty( int decade ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query               query;
        List<RockBandSongRaw>  songs;

        songs = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSongRaw>) query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static List<RockBandSongRaw> GetSongsByLocation( RockBandLocation location ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query               query;
        List<RockBandSongRaw>  songs;

        songs = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");
        query.setFilter("_location == LocationParam");
        query.declareParameters("String LocationParam");

        try {
            songs = (List<RockBandSongRaw>) query.execute( location.ordinal() );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    public static boolean SongExists( String md5 ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;
        RockBandSongRaw rbSong;

        rbSong = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setFilter("_md5 == MD5Param");
        query.declareParameters("String MD5Param");
        query.setUnique( true );
//        query.setResult( "_md5" );

        try {
            rbSong = (RockBandSongRaw) query.execute( md5 );
        }
        finally {
            query.closeAll();
        }

        if( rbSong == null ) {
            return false;
        }

        return true;
    }

    public static Long WriteRaw( RockBandSongRaw rbSong ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        } finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static Long WriteEmbedded( RockBandSongEmbedded rbSong ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        } finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static Long WriteGenerated( RockBandSongGenerated rbSong ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        } finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static RockBandSongRaw GetRawSongById( long id ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSongRaw rbSong;
//      Key k;

//      k = KeyFactory.createKey(RockBandSongRaw.class.getSimpleName(), id);
        rbSong = (RockBandSongRaw)pm.getObjectById( RockBandSongRaw.class, id );

        return rbSong;
    }

    public static RockBandSongEmbedded GetEmbeddedSongById( long id ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        String  stringId;
        RockBandSongEmbedded rbSong;
        stringId = String.format( "rbe%d", id );
        rbSong = (RockBandSongEmbedded)pm.getObjectById( RockBandSongEmbedded.class, stringId );

//      Key k;
//      k = KeyFactory.createKey(RockBandSongRaw.class.getSimpleName(), id);

        return rbSong;
    }
}