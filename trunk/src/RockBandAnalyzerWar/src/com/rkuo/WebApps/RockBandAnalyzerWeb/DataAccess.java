package com.rkuo.WebApps.RockBandAnalyzerWeb;

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

        PersistenceManager pm;

        pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent( rbs );
        } finally {
            pm.close();
        }

        return;
    }
}
