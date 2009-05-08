package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.rkuo.RockBand.RockBandLocation;
import com.rkuo.RockBand.RockBandInstrument;
import com.rkuo.RockBand.RockBandInstrumentDifficultyCategory;
import com.rkuo.RockBand.RockBandSort;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.PMF;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 25, 2009
 * Time: 11:01:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataAccess {

    // http://jgeewax.wordpress.com/2008/09/23/prefix-searches-in-google-app-engine/
    @SuppressWarnings("unchecked")
    public static boolean GenerateSuggestEntries() {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<RockBandSongEmbedded>  songs;
        SortedMap<String,List<Long>>    suggestEntries;

        suggestEntries = new TreeMap<String,List<Long>>();

        songs = GetSongs( RockBandSort.Song );
        for( RockBandSongEmbedded song : songs ) {
            String[]    words;

            // TODO: may want to strip certain characters here...
            words = song.getTitle().toLowerCase().split("");
            for( String word : words ) {
                List<Long>  songIds;

                if( suggestEntries.containsKey(word) == true ) {
                    songIds = suggestEntries.get(word);
                }
                else {
                    songIds = new ArrayList<Long>();
                }
                songIds.add( song.getId() );
                suggestEntries.put( word, songIds );
            }
        }

        for( Map.Entry<String,List<Long>> entry : suggestEntries.entrySet() ) {

            SuggestEntry    suggestEntry;

            suggestEntry = new SuggestEntry();
            suggestEntry.setSuggestKeyword( entry.getKey() );
            suggestEntry.setSongIds( entry.getValue() );

            try {
                pm.makePersistent(suggestEntry);
            }
            finally {
                pm.close();
            }
        }

        return true;
    }

    // http://jgeewax.wordpress.com/2008/09/23/prefix-searches-in-google-app-engine/
    @SuppressWarnings("unchecked")
    public static List<RockBandSongEmbedded> GetSongsBySuggestion( String prefix ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query;
        List<RockBandSongEmbedded> songs;
        String prefixLowerParam, prefixUpperParam;


        songs = null;


        query = pm.newQuery(RockBandSongEmbedded.class);
        query.setOrdering("_title asc");
        query.setFilter("prefix >= prefixLowerParam && prefix < prefixUpperParam");
        query.declareParameters("String prefixParam, String prefixUpperParam");

        prefixLowerParam = prefix;
        prefixUpperParam = prefix + "\ufffd";

        try {
            songs = (List<RockBandSongEmbedded>)query.execute( prefixLowerParam, prefixUpperParam );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongEmbedded> GetSongs( RockBandSort rbs ) {

        Query query;
        List<RockBandSongEmbedded> songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongEmbedded.class);
        if( rbs == RockBandSort.DifficultyGuitar ) {
            query.setOrdering("_guitarDifficulty asc");
        }
        else if( rbs == RockBandSort.DifficultyBass ) {
            query.setOrdering("_bassDifficulty asc");
        }
        else if( rbs == RockBandSort.DifficultyDrums ) {
            query.setOrdering("_drumsDifficulty asc");
        }
        else if( rbs == RockBandSort.DifficultyVocals ) {
            query.setOrdering("_vocalsDifficulty asc");
        }
        else if( rbs == RockBandSort.DifficultyBand ) {
            query.setOrdering("_bandDifficulty asc");
        }
        else if( rbs == RockBandSort.Song ) {
            query.setOrdering("_title asc");
        }
        else if( rbs == RockBandSort.Band ) {
            query.setOrdering("_artist asc");                        
        }
        else if( rbs == RockBandSort.Genre ) {
            query.setOrdering("_genre asc");
        }
        else if( rbs == RockBandSort.Decade ) {
            query.setOrdering("_dateReleased asc");
        }
        else if( rbs == RockBandSort.Location ) {
            query.setOrdering("_location asc");
        }

        try {
            songs = (List<RockBandSongEmbedded>)query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongRaw> GetSongsByDecade(int decade) {

        Query query;
        List<RockBandSongRaw> songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSongRaw>)query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongRaw> GetSongsByDifficulty(int decade) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;
        List<RockBandSongRaw> songs;

        songs = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");

        try {
            songs = (List<RockBandSongRaw>)query.execute();
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongRaw> GetSongsByLocation(RockBandLocation location) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query;
        List<RockBandSongRaw> songs;

        songs = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("_midiTitle asc");
        query.setFilter("_location == LocationParam");
        query.declareParameters("String LocationParam");

        try {
            songs = (List<RockBandSongRaw>)query.execute(location.ordinal());
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    // At least the last two weeks of releases
    @SuppressWarnings("unchecked")
    public static List<RockBandSongGenerated> GetSongsRecentlyReleased() {

        Query query;
        List<RockBandSongGenerated> songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongGenerated.class);
        query.setOrdering("id asc");
        query.setFilter("glitched == glitchedParam");
        query.declareParameters("Boolean glitchedParam");

        try {
            songs = (List<RockBandSongGenerated>)query.execute( true );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongGenerated> GetSongsByFilter( RockBandSongFilter rbsf ) {

        Query query;
        List<RockBandSongGenerated> songs;
        Object  filterParam;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongGenerated.class);
        query.setOrdering("id asc");

        if( rbsf == RockBandSongFilter.Glitched ) {
            query.setFilter("glitched == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else if( rbsf == RockBandSongFilter.BreakneckOptimal ) {
            query.setFilter("breakneckOptimal == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else if( rbsf == RockBandSongFilter.Solos ) {
            query.setOrdering("Solos asc");
            query.setFilter("Solos > filterParam");
            query.declareParameters("Long filterParam");
            filterParam = 0;
        }
        else if( rbsf == RockBandSongFilter.GoldImpossible ) {
            query.setFilter("goldStarrable == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = false;
        }
        else if( rbsf == RockBandSongFilter.BigRockEnding ) {
            query.setFilter("BigRockEnding == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else {
            return null;
        }

        try {
            songs = (List<RockBandSongGenerated>)query.execute( filterParam );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSongGenerated> GetSongsBreakneckOptimal() {

        Query query;
        List<RockBandSongGenerated> songs;

        PersistenceManager pm;

        songs = null;
        pm = PMF.get().getPersistenceManager();

        query = pm.newQuery(RockBandSongGenerated.class);
        query.setOrdering("id asc");
        query.setFilter("breakneckOptimal == breakneckOptimalParam");
        query.declareParameters("Boolean breakneckOptimalParam");

        try {
            songs = (List<RockBandSongGenerated>)query.execute( true );
        }
        finally {
            query.closeAll();
        }

        return songs;
    }


    public static boolean SongExists(String md5) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;
        RockBandSongRaw rbSong;

        rbSong = null;

        query = pm.newQuery(RockBandSongRaw.class);
        query.setFilter("_md5 == MD5Param");
        query.declareParameters("String MD5Param");
        query.setUnique(true);
//        query.setResult( "_md5" );

        try {
            rbSong = (RockBandSongRaw)query.execute(md5);
        }
        finally {
            query.closeAll();
        }

        if( rbSong == null ) {
            return false;
        }

        return true;
    }

    public static Long WriteRaw(RockBandSongRaw rbSong) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        }
        finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static Long WriteEmbedded(RockBandSongEmbedded rbSong) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        }
        finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static Long WriteGenerated(RockBandSongGenerated rbSong) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        }
        finally {
            pm.close();
        }

        return rbSong.getId();
    }

    public static RockBandSongRaw GetRawSongById(long id) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSongRaw rbSong;
//      Key k;

//      k = KeyFactory.createKey(RockBandSongRaw.class.getSimpleName(), id);
        rbSong = (RockBandSongRaw)pm.getObjectById(RockBandSongRaw.class, id);

        return rbSong;
    }

    public static RockBandSongEmbedded GetEmbeddedSongById(long id) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        String stringId;
        RockBandSongEmbedded rbSong;
        stringId = String.format("rbe%d", id);
        rbSong = (RockBandSongEmbedded)pm.getObjectById(RockBandSongEmbedded.class, stringId);

//      Key k;
//      k = KeyFactory.createKey(RockBandSongRaw.class.getSimpleName(), id);

        return rbSong;
    }

    public static boolean TryWritingSong(RockBandSongRaw raw, RockBandSongEmbedded embedded, RockBandSongGenerated generated) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
//        Transaction tx = pm.currentTransaction();
        boolean succeeded;


        succeeded = false;

        try {
            Query q;
            RockBandSongRaw rawQuery;

//            tx.begin();

            q = pm.newQuery(RockBandSongRaw.class);
            q.setFilter("_md5 == MD5Param");
            q.declareParameters("String MD5Param");
            q.setUnique(true);
//        query.setResult( "_md5" );

            rawQuery = (RockBandSongRaw)q.execute(raw.getMD5());
            q.closeAll();

            if( rawQuery == null ) {
                pm.makePersistent(raw);

                embedded.setId(raw.getId());
                pm.makePersistent(embedded);

                generated.setId(raw.getId());
                pm.makePersistent(generated);
                succeeded = true;
            }

//            tx.commit();
        }
        finally {
//            if( tx.isActive() ) {
//                tx.rollback();
//            }

            pm.close();
        }

        return succeeded;
    }

    public static SortedMap<String,Long> GetArtistDistribution() {

        SortedMap<String,Long>   artistDistribution;
        List<RockBandSongEmbedded>  songs;

        artistDistribution = new TreeMap<String,Long>();
        songs = GetSongs(  RockBandSort.Song );

        for( RockBandSongEmbedded s : songs ) {
            String  artist;
            Long    artistCount;

            artist = s.getGenre();
            if( artist == null ) {
                artist = "Unknown";
            }

            if( artist.length() == 0 ) {
                artist = "Unknown";
            }

            if( artistDistribution.containsKey(artist) == true ) {
                artistCount = artistDistribution.get(artist);
                artistDistribution.put( artist, artistCount+1L );
            }
            else {
                artistDistribution.put(artist, 1L);
            }
        }

        return artistDistribution;
    }

    public static SortedMap<String,Long> GetGenreDistribution() {

        SortedMap<String,Long>   genreDistribution;
        List<RockBandSongEmbedded>  songs;

        genreDistribution = new TreeMap<String,Long>();
        songs = GetSongs( RockBandSort.Song );

        for( RockBandSongEmbedded s : songs ) {
            String  genre;
            Long    genreCount;

            genre = s.getGenre();
            if( genre == null ) {
                genre = "Unknown";
            }

            if( genre.length() == 0 ) {
                genre = "Unknown";                
            }

            if( genreDistribution.containsKey(genre) == true ) {
                genreCount = genreDistribution.get(genre);
                genreDistribution.put( genre, genreCount+1L );
            }
            else {
                genreDistribution.put(genre, 1L);
            }
        }

        return genreDistribution;
    }

    public static SortedMap<Long,Long> GetDifficultyDistribution( RockBandInstrumentDifficultyCategory rbidc ) {

        SortedMap<Long,Long>   difficultyDistribution;
        List<RockBandSongEmbedded>  songs;

        difficultyDistribution = new TreeMap<Long,Long>();
        songs = GetSongs( RockBandSort.Song );

        for( RockBandSongEmbedded s : songs ) {
            Integer     difficulty;
            Long    difficultyCount;

            difficulty = s.getDifficulty( rbidc );

            if( difficultyDistribution.containsKey(difficulty.longValue()) == true ) {
                difficultyCount = difficultyDistribution.get(difficulty.longValue());
                difficultyDistribution.put( difficulty.longValue(), difficultyCount+1L );
            }
            else {
                difficultyDistribution.put( difficulty.longValue(), 1L );
            }
        }

        return difficultyDistribution;
    }
}