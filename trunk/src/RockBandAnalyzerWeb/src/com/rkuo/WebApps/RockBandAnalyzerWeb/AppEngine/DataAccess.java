package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.rkuo.RockBand.*;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.PMF;
import com.google.appengine.api.datastore.*;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 25, 2009
 * Time: 11:01:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataAccess {

    public static RockBandDotComSong GetDotComSongForScraping(Long currentTime) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<RockBandDotComSong> rbdcSongs;
        RockBandDotComSong rbdcSong;
        Long msDay;

        msDay = 24L * 60L * 60L * 1000L;

        rbdcSong = null;

        try {

            Query query;

            query = pm.newQuery(RockBandDotComSong.class);
            query.setFilter("lastAttempted < lastAttemptedParam");
            query.declareParameters("Long lastAttemptedParam");
            query.setRange(0, 1);

            try {
                rbdcSongs = (List<RockBandDotComSong>)query.execute(currentTime - msDay);
            }
            finally {
                query.closeAll();
            }

            if( rbdcSongs.size() > 0 ) {
                rbdcSong = rbdcSongs.get(0);
            }
        }
        finally {
            pm.close();
        }

        return rbdcSong;
    }

    public static void FlagDotComSongAsAttempted( Long id, Long currentTime ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandDotComSong dcSong;
//      Key k;

//      k = KeyFactory.createKey(RockBandSongRaw.class.getSimpleName(), id);
        try {
            dcSong = (RockBandDotComSong)pm.getObjectById(RockBandDotComSong.class, id);
            dcSong.setLastAttempted(currentTime);
            pm.makePersistent(dcSong);
        }
        finally {
            pm.close();
        }

        return;
    }

    public static RockBandSongRaw GetRawSongForReprocessing() {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        List<RockBandSongRaw> rawSongs;
        RockBandSongRaw rawSong;

        rawSong = null;

        try {

            Query query;

            query = pm.newQuery(RockBandSongRaw.class);
            query.setFilter("needsReprocessing == needsReprocessingParam");
            query.declareParameters("Boolean needsReprocessingParam");
            query.setRange(0, 1);

            try {
                rawSongs = (List<RockBandSongRaw>)query.execute(true);
            }
            finally {
                query.closeAll();
            }

            if( rawSongs.size() > 0 ) {
                rawSong = rawSongs.get(0);
            }
        }
        finally {
            pm.close();
        }

        return rawSong;
    }

    public static Boolean UpdateDotComSong(String midiTitle, Long now, SortedMap<String, String> props) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandDotComSong rbdcSong;
        String sValue;
        Integer nValue;
        SimpleDateFormat sdf;

        Query query;

        query = pm.newQuery(RockBandDotComSong.class);
        query.setFilter("midiTitle == midiTitleParam");
        query.declareParameters("String midiTitleParam");
        query.setUnique(true);

        try {
            rbdcSong = (RockBandDotComSong)query.execute(midiTitle);

            rbdcSong.setTitle(props.get("title"));
            rbdcSong.setArtist(props.get("artist"));
            rbdcSong.setAlbum(props.get("album"));

            sValue = props.get("release_year");
            nValue = Integer.parseInt(sValue);
            nValue -= 1900;

            Calendar cal;
            cal = new GregorianCalendar(nValue + 1900, 0, 1, 0, 0);
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            rbdcSong.setDateReleased(cal.getTime());

            rbdcSong.setGenre(props.get("genre"));

            sValue = props.get("location");
            if( sValue.compareToIgnoreCase("Rock Band") == 0 ) {
                rbdcSong.setLocation(RockBandLocation.RockBandOne);
            }
            else if( sValue.compareToIgnoreCase("Rock Band 2") == 0 ) {
                rbdcSong.setLocation(RockBandLocation.RockBandTwo);
            }
            else if( sValue.compareToIgnoreCase("Downloadable Content") == 0 ) {
                rbdcSong.setLocation(RockBandLocation.Downloaded);
            }
            else if( sValue.compareToIgnoreCase("Track Pack") == 0 ) {
                rbdcSong.setLocation(RockBandLocation.TrackPack);
            }
            else {
                // maybe throw an exception
                rbdcSong.setLocation(RockBandLocation.Unknown);
            }

            sdf = new SimpleDateFormat("M/d/yy");
            sValue = props.get("rb_release_date");

            try {
                Date rbDateReleased;

                rbDateReleased = sdf.parse(sValue);
                cal = new GregorianCalendar(rbDateReleased.getYear() + 1900, rbDateReleased.getMonth(), rbDateReleased.getDate(), 0, 0);
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                rbDateReleased = cal.getTime();
                rbdcSong.setDatePublished(rbDateReleased);
            }
            catch( ParseException pex ) {
                return false;
            }

            RockBandInstrumentDifficulty rbid;

            rbid = RockBandInstrumentDifficulty.ToEnum(Integer.parseInt(props.get("guitar_difficulty")));
            rbdcSong.setGuitarDifficulty(rbid);
            rbid = RockBandInstrumentDifficulty.ToEnum(Integer.parseInt(props.get("drums_difficulty")));
            rbdcSong.setDrumsDifficulty(rbid);
            rbid = RockBandInstrumentDifficulty.ToEnum(Integer.parseInt(props.get("vocals_difficulty")));
            rbdcSong.setVocalsDifficulty(rbid);
            rbid = RockBandInstrumentDifficulty.ToEnum(Integer.parseInt(props.get("bass_difficulty")));
            rbdcSong.setBassDifficulty(rbid);
            rbid = RockBandInstrumentDifficulty.ToEnum(Integer.parseInt(props.get("band_difficulty")));
            rbdcSong.setBandDifficulty(rbid);

            sValue = props.get("cover");
            rbdcSong.setCover(Boolean.parseBoolean(sValue));

            rbdcSong.setLastUpdated(now);

            pm.makePersistent(rbdcSong);
        }
        finally {
            query.closeAll();
            pm.close();
        }

        return true;
    }

    public static Long WriteDotComSong(RockBandDotComSong rbSong) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            pm.makePersistent(rbSong);
        }
        finally {
            pm.close();
        }

        return rbSong.getId();
    }


    // http://jgeewax.wordpress.com/2008/09/23/prefix-searches-in-google-app-engine/
    @SuppressWarnings("unchecked")
    public static boolean GenerateSuggestEntries() {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<RockBandSong> songs;
        SortedMap<String, List<Long>> suggestEntries;

        suggestEntries = new TreeMap<String, List<Long>>();

        songs = GetSongs(RockBandAdvancedSort.Song);
        for( RockBandSong song : songs ) {
            String[] words;

            // TODO: may want to strip certain characters here...
            words = song.getAssociated().getTitle().toLowerCase().split("");
            for( String word : words ) {
                List<Long> songIds;

                if( suggestEntries.containsKey(word) == true ) {
                    songIds = suggestEntries.get(word);
                }
                else {
                    songIds = new ArrayList<Long>();
                }
                songIds.add(song.getId());
                suggestEntries.put(word, songIds);
            }
        }

        for( Map.Entry<String, List<Long>> entry : suggestEntries.entrySet() ) {

            SuggestEntry suggestEntry;

            suggestEntry = new SuggestEntry();
            suggestEntry.setSuggestKeyword(entry.getKey());
            suggestEntry.setSongIds(entry.getValue());

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
    public static List<RockBandSong> GetSongsBySuggestion(String prefix) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query;
        List<RockBandSong> songs;
        String prefixLowerParam, prefixUpperParam;


        songs = null;


        query = pm.newQuery(RockBandSong.class);
        query.setOrdering("_title asc");
        query.setFilter("prefix >= prefixLowerParam && prefix < prefixUpperParam");
        query.declareParameters("String prefixParam, String prefixUpperParam");

        prefixLowerParam = prefix;
        prefixUpperParam = prefix + "\ufffd";

        try {
            songs = (List<RockBandSong>)query.execute(prefixLowerParam, prefixUpperParam);
        }
        finally {
            query.closeAll();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<Long> GetRawSongIds() {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;
        List<RockBandSongRaw> rawSongs;
        List<Long> songIds;

        songIds = new ArrayList<Long>();

        query = pm.newQuery(RockBandSongRaw.class);
        query.setOrdering("id asc");

        try {
            rawSongs = (List<RockBandSongRaw>)query.execute();
        }
        finally {
            query.closeAll();
        }

        for( RockBandSongRaw rawSong : rawSongs ) {
            songIds.add(rawSong.getId());
        }

        return songIds;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSong> GetSongs(RockBandAdvancedSort rbas) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query;
        List<RockBandSong> songs;

        query = pm.newQuery(RockBandSong.class);
        if( rbas == RockBandAdvancedSort.DifficultyGuitar ) {
            query.setOrdering("associated.guitarDifficulty asc");
        }
        else if( rbas == RockBandAdvancedSort.DifficultyBass ) {
            query.setOrdering("associated.bassDifficulty asc");
        }
        else if( rbas == RockBandAdvancedSort.DifficultyDrums ) {
            query.setOrdering("associated.drumsDifficulty asc");
        }
        else if( rbas == RockBandAdvancedSort.DifficultyVocals ) {
            query.setOrdering("associated.vocalsDifficulty asc");
        }
        else if( rbas == RockBandAdvancedSort.DifficultyBand ) {
            query.setOrdering("associated.bandDifficulty asc");
        }
        else if( rbas == RockBandAdvancedSort.Song ) {
            query.setOrdering("associated.title asc");
        }
        else if( rbas == RockBandAdvancedSort.Band ) {
            query.setOrdering("associated.artist asc");
        }
        else if( rbas == RockBandAdvancedSort.Genre ) {
            query.setOrdering("associated.genre asc");
        }
        else if( rbas == RockBandAdvancedSort.Decade ) {
            query.setOrdering("associated.dateReleased asc");
        }
        else if( rbas == RockBandAdvancedSort.Location ) {
            query.setOrdering("associated.location asc");
        }
        else if( rbas == RockBandAdvancedSort.Duration ) {
            query.setOrdering("generated.microseconds asc");
        }
        else if( rbas == RockBandAdvancedSort.Notes ) {
            query.setOrdering("generated.Notes asc");
        }
        else if( rbas == RockBandAdvancedSort.MaxScore ) {
            query.setOrdering("generated.maxScore asc");
        }
        else if( rbas == RockBandAdvancedSort.RBReleaseDate ) {
            query.setOrdering("associated.datePublished asc");
        }

        try {
            songs = (List<RockBandSong>)query.execute();
        }
        finally {
            query.closeAll();
            pm.close();
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
            pm.close();
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
            pm.close();
        }

        return songs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandSong> GetSongsByFilter(RockBandSongFilter rbsf) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        Query query;
        List<RockBandSong> songs;
        Object filterParam;

        songs = null;

        query = pm.newQuery(RockBandSong.class);
//        query.setOrdering("id asc");

        if( rbsf == RockBandSongFilter.Glitched ) {
            query.setFilter("generated.glitched == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else if( rbsf == RockBandSongFilter.NormalOptimal ) {
            query.setFilter("generated.optimalFillDelay == filterParam");
            query.declareParameters("Integer filterParam");
            filterParam = RockBandOptimalFillDelay.ToInteger(RockBandOptimalFillDelay.Normal);
        }
        else if( rbsf == RockBandSongFilter.BreakneckOptimal ) {
            query.setFilter("generated.optimalFillDelay == filterParam");
            query.declareParameters("Integer filterParam");
            filterParam = RockBandOptimalFillDelay.ToInteger(RockBandOptimalFillDelay.Breakneck);
        }
        else if( rbsf == RockBandSongFilter.Solos ) {
            query.setOrdering("generated.Solos asc");
            query.setFilter("generated.Solos > filterParam");
            query.declareParameters("Long filterParam");
            filterParam = 0;
        }
        else if( rbsf == RockBandSongFilter.GoldImpossible ) {
            query.setFilter("generated.goldStarrable == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = false;
        }
        else if( rbsf == RockBandSongFilter.BigRockEnding ) {
            query.setFilter("generated.BigRockEnding == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else if( rbsf == RockBandSongFilter.Cover ) {
            query.setFilter("associated.isCover == filterParam");
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else if( rbsf == RockBandSongFilter.RecentlyReleased ) {
            query.setOrdering("associated.datePublished desc");
            query.setRange(0, 10);

            // dummy parameters
            query.declareParameters("Boolean filterParam");
            filterParam = true;
        }
        else {
            return null;
        }

        try {
            songs = (List<RockBandSong>)query.execute(filterParam);
        }
        finally {
            query.closeAll();
            pm.close();
        }

        return songs;
    }

    public static RockBandSong GetSongByMidiTitle(String midiTitle) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSong rbSong;
        Query query;

        pm.setDetachAllOnCommit(true);

        query = pm.newQuery(RockBandSong.class);
        query.setFilter("generated.midiTitle == midiTitleParam");
        query.declareParameters("String midiTitleParam");
        query.setUnique(true);

        try {
            rbSong = (RockBandSong)query.execute(midiTitle);
        }
        finally {
            query.closeAll();
            pm.close();
        }

        return rbSong;
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
            pm.close();
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

    public static Long WriteSong(RockBandSong rbSong) {

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
        try {
            pm.getFetchPlan().setGroup("full");
            rbSong = (RockBandSongRaw)pm.getObjectById(RockBandSongRaw.class, id);
        }
        finally {
            pm.close();
        }

        return rbSong;
    }

    public static void FlagRawSongAsProcessed(long id) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSongRaw rawSong;

        try {
            rawSong = (RockBandSongRaw)pm.getObjectById(RockBandSongRaw.class, id);
            rawSong.setNeedsReprocessing(false);
            pm.makePersistent(rawSong);
        }
        finally {
            pm.close();
        }

        return;
    }

    public static RockBandSong GetSongById(Long id) {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        RockBandSong rbSong;
        Query query;

        pm.setDetachAllOnCommit(true);

        query = pm.newQuery(RockBandSong.class);
        query.setFilter("id == idParam");
        query.declareParameters("String idParam");
        query.setUnique(true);

        try {
            rbSong = (RockBandSong)query.execute("rbs" + id.toString());
        }
        finally {
            query.closeAll();
            pm.close();
        }

        return rbSong;
    }

    // some sort of bug with the below code returning a blank object

    /*
       public static RockBandSong GetSongById(long id) {

           PersistenceManager pm = PMF.get().getPersistenceManager();
           RockBandSong rbSong;


           try {
               String stringId;
               stringId = String.format("%s%d", RockBandSong.idPrefix, id);
               rbSong = (RockBandSong)pm.getObjectById(RockBandSong.class, stringId);
           }
           finally {
               pm.close();
           }

           return rbSong;
       }
    */
    public static boolean TryWritingSong(RockBandSongRaw raw, RockBandSong song) {

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

                song.setId(raw.getId());
                pm.makePersistent(song);
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

    public static SortedMap<String, Long> GetArtistDistribution() {

        SortedMap<String, Long> artistDistribution;
        List<RockBandSong> songs;

        artistDistribution = new TreeMap<String, Long>();
        songs = GetSongs(RockBandAdvancedSort.Song);

        for( RockBandSong s : songs ) {
            String artist;
            Long artistCount;

            artist = s.getAssociated().getGenre();
            if( artist == null ) {
                artist = "Unknown";
            }

            if( artist.length() == 0 ) {
                artist = "Unknown";
            }

            if( artistDistribution.containsKey(artist) == true ) {
                artistCount = artistDistribution.get(artist);
                artistDistribution.put(artist, artistCount + 1L);
            }
            else {
                artistDistribution.put(artist, 1L);
            }
        }

        return artistDistribution;
    }

    public static SortedMap<String, Long> GetGenreDistribution() {

        SortedMap<String, Long> genreDistribution;
        List<RockBandSong> songs;

        genreDistribution = new TreeMap<String, Long>();
        songs = GetSongs(RockBandAdvancedSort.Song);

        for( RockBandSong s : songs ) {
            String genre;
            Long genreCount;

            genre = s.getAssociated().getGenre();
            if( genre == null ) {
                genre = "Unknown";
            }

            if( genre.length() == 0 ) {
                genre = "Unknown";
            }

            if( genreDistribution.containsKey(genre) == true ) {
                genreCount = genreDistribution.get(genre);
                genreDistribution.put(genre, genreCount + 1L);
            }
            else {
                genreDistribution.put(genre, 1L);
            }
        }

        return genreDistribution;
    }

    public static SortedMap<RockBandInstrumentDifficulty, Long> GetDifficultyDistribution(RockBandInstrumentDifficultyCategory rbidc) {

        SortedMap<RockBandInstrumentDifficulty, Long> difficultyDistribution;
        List<RockBandSong> songs;

        difficultyDistribution = new TreeMap<RockBandInstrumentDifficulty, Long>();
        songs = GetSongs(RockBandAdvancedSort.Song);

        for( RockBandSong s : songs ) {
            RockBandInstrumentDifficulty difficulty;
            Long difficultyCount;

            difficulty = s.getAssociated().getDifficulty(rbidc);

            if( difficultyDistribution.containsKey(difficulty) == true ) {
                difficultyCount = difficultyDistribution.get(difficulty);
                difficultyDistribution.put(difficulty, difficultyCount + 1L);
            }
            else {
                difficultyDistribution.put(difficulty, 1L);
            }
        }

        return difficultyDistribution;
    }

    public static boolean ProcessSong(RockBandSong song, DrumsFullAnalysis dfa) {

        RockBandSong.RBGenerated g;
        String midiTitle;

        // Have noticed that some midi files have a "1" appended to their real identifier
        // We strip this before putting it in the db
        midiTitle = dfa.dba.MidiTitle;
        if( midiTitle.endsWith("1") == true ) {
            midiTitle = midiTitle.substring(0, midiTitle.length() - 1);
        }

        g = song.getGenerated();

        g.setMidiTitle(midiTitle);

        g.setMicroseconds(dfa.dba.Microseconds);
        g.setNotes(dfa.dba.Notes);
        g.setSolos(dfa.dba.Solos);
        g.setBigRockEnding(dfa.dba.BigRockEnding);

        g.setGlitched(false);
        if( dfa.dba.GlitchedChords.size() > 0 ) {
            g.setGlitched(true);
        }

        if( dfa.RB2PathNormalOptimal.Score > dfa.RB2PathBreakneckOptimal.Score ) {
            g.setOptimalFillDelay(RockBandOptimalFillDelay.Normal);
        }
        else if( dfa.RB2PathBreakneckOptimal.Score > dfa.RB2PathNormalOptimal.Score ) {
            g.setOptimalFillDelay(RockBandOptimalFillDelay.Breakneck);
        }
        else {
            g.setOptimalFillDelay(RockBandOptimalFillDelay.None);
        }

        g.setGoldStarrable(true);
        if( dfa.RB2PathOverallOptimal.Score < dfa.dba.StarCutoffGold ) {
            g.setGoldStarrable(false);
        }

        g.setUnmultipliedScore(dfa.dba.UnmultipliedScore);
        g.setMultipliedScore(dfa.dba.MultipliedScore);

        g.setOverdrivePhrases(dfa.dba.OverdrivePhrases);
        g.setMaximumMultiplierNotReachedWarning(dfa.dba.MaximumMultiplierNotReachedWarning);

        g.setStarCutoffOne(dfa.dba.StarCutoffOne);
        g.setStarCutoffTwo(dfa.dba.StarCutoffTwo);
        g.setStarCutoffThree(dfa.dba.StarCutoffThree);
        g.setStarCutoffFour(dfa.dba.StarCutoffFour);
        g.setStarCutoffFive(dfa.dba.StarCutoffFive);
        g.setStarCutoffGold(dfa.dba.StarCutoffGold);
        g.setStarCutoffGoldOld(dfa.dba.StarCutoffGoldOld);

        g.setMaxScore(dfa.RB2PathOverallOptimal.Score);
        g.setNormalOptimalScore(dfa.RB2PathNormalOptimal.Score);
        g.setBreakneckOptimalScore(dfa.RB2PathBreakneckOptimal.Score);

        return true;
    }

    public static Boolean DropTable(String entityName) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        while( true ) {
            com.google.appengine.api.datastore.Query q;
            ArrayList<Key> keys;
            List<Entity> entities;

            keys = new ArrayList<Key>();

            q = new com.google.appengine.api.datastore.Query(entityName);
            entities = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(32));
            if( entities == null ) {
                break;
            }

            if( entities.size() == 0 ) {
                break;
            }

            for( Entity e : entities ) {
                keys.add(e.getKey());
            }

            datastore.delete(keys);
        }
/*
//        for( Entity taskEntity : datastore.prepare(q).asIterable(FetchOptions.Builder.withLimit(100)) ) {
//            keys.add(taskEntity.getKey());
//        }

        // This might be slow, but probably not a big deal for now
        // There is a limit of 500 deletes on any single call, so we have to do this.
//        for( Key key : keys ) {
//            datastore.delete(key);
//        }

        for( int x=0; x < keys.size(); x += 500 ) {
            List<Key>  subKeys;
            int             lastIndex;

            lastIndex = x + 500;
            if( lastIndex > keys.size()) {
                lastIndex = keys.size();
            }

            subKeys = keys.subList(x, lastIndex);
            datastore.delete( subKeys );
        }
 */
        return true;
    }

    public static Boolean FlagRawSongsForReprocessing() {


        while( true ) {
            PersistenceManager pm = PMF.get().getPersistenceManager();

            try {

                Query query;
                List<RockBandSongRaw> songs;
                Object filterParam;

                songs = null;

                query = pm.newQuery(RockBandSongRaw.class);
                query.setFilter("needsReprocessing == filterParam");
                query.setRange(0, 32);
                query.declareParameters("Boolean filterParam");
                filterParam = false;

                try {
                    songs = (List<RockBandSongRaw>)query.execute(filterParam);
                }
                finally {
                    query.closeAll();
                }

                if( songs == null ) {
                    break;
                }

                if( songs.size() == 0 ) {
                    break;
                }

                for( RockBandSongRaw s : songs ) {
                    s.setNeedsReprocessing(true);
                    pm.makePersistent(s);
                }
            }
            finally {
                pm.close();
            }
        }

        return true;
    }

    public static Boolean WriteDotComSong( String midiTitle ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            RockBandDotComSong rbdcSong;
            Query query;

            query = pm.newQuery(RockBandDotComSong.class);
            query.setFilter("midiTitle == midiTitleParam");
            query.declareParameters("String midiTitleParam");
            query.setUnique(true);

            try {
                rbdcSong = (RockBandDotComSong)query.execute(midiTitle);
            }
            finally {
                query.closeAll();
            }

            if( rbdcSong != null ) {
                return false;
            }

            rbdcSong = new RockBandDotComSong();
            rbdcSong.setMidiTitle(midiTitle);

            pm.makePersistent(rbdcSong);
        }
        finally {
            pm.close();
        }

        return true;
    }

    public static boolean UpdateDotComSong(RockBandDotComSong rbdcSong) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            RockBandDotComSong rbdcExistingSong;

            Query query;

            query = pm.newQuery(RockBandDotComSong.class);
            query.setFilter("midiTitle == midiTitleParam");
            query.declareParameters("String midiTitleParam");
            query.setUnique(true);

            try {
                rbdcExistingSong = (RockBandDotComSong)query.execute(rbdcSong.getMidiTitle());
            }
            finally {
                query.closeAll();
            }

            rbdcSong.setId(rbdcExistingSong.getId());
            pm.makePersistent(rbdcSong);
        }
        finally {
            pm.close();
        }

        return true;
    }

    public static void UpdateSong( Long id, DrumsFullAnalysis dfa ) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            RockBandSong    song;
            Query           query;

            query = pm.newQuery(RockBandSong.class);
            query.setFilter("id == idParam");
            query.declareParameters("String idParam");
            query.setUnique(true);

            try {
                song = (RockBandSong)query.execute("rbs" + id.toString());
            }
            finally {
                query.closeAll();
            }

            if( song == null ) {
                song = new RockBandSong();
                song.setId( id );
            }

            DataAccess.ProcessSong( song, dfa );
            pm.makePersistent( song );
        }
        finally {
            pm.close();
        }

        return;
    }

    public static boolean MergeDotComSong(String midiTitle) {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        try {
            RockBandDotComSong dcSong;
            RockBandSong song;

            Query query;

            query = pm.newQuery(RockBandDotComSong.class);
            query.setFilter("midiTitle == midiTitleParam");
            query.declareParameters("String midiTitleParam");
            query.setUnique(true);

            try {
                dcSong = (RockBandDotComSong)query.execute(midiTitle);
            }
            finally {
                query.closeAll();
            }

            query = pm.newQuery(RockBandSong.class);
            query.setFilter("generated.midiTitle == midiTitleParam");
            query.declareParameters("String midiTitleParam");
            query.setUnique(true);

            try {
                song = (RockBandSong)query.execute(midiTitle);
            }
            finally {
                query.closeAll();
            }

            if( dcSong != null && song != null ) {
                song.getAssociated().setArtist(dcSong.getArtist());
                song.getAssociated().setAlbum(dcSong.getAlbum());
                song.getAssociated().setTitle(dcSong.getTitle());
                song.getAssociated().setGenre(dcSong.getGenre());
                song.getAssociated().setLocation(dcSong.getLocation());
                song.getAssociated().setDateReleased(dcSong.getDateReleased());
                song.getAssociated().setRBReleaseDate(dcSong.getDatePublished());
                song.getAssociated().setCover(dcSong.getCover());

                song.getAssociated().setGuitarDifficulty(dcSong.getDifficulty(RockBandInstrumentDifficultyCategory.Guitar));
                song.getAssociated().setDrumsDifficulty(dcSong.getDifficulty(RockBandInstrumentDifficultyCategory.Drums));
                song.getAssociated().setVocalsDifficulty(dcSong.getDifficulty(RockBandInstrumentDifficultyCategory.Vocals));
                song.getAssociated().setBassDifficulty(dcSong.getDifficulty(RockBandInstrumentDifficultyCategory.Bass));
                song.getAssociated().setBandDifficulty(dcSong.getDifficulty(RockBandInstrumentDifficultyCategory.Band));

                pm.makePersistent(song);
            }
        }
        finally {
            pm.close();
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static List<String> GetMissingSongs() {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<String> missingSongs;

        missingSongs = new ArrayList<String>();

        try {
            Query query;
            List<RockBandDotComSong> dcSongs;

            query = pm.newQuery(RockBandDotComSong.class);

            try {
                dcSongs = (List<RockBandDotComSong>)query.execute();
            }
            finally {
                query.closeAll();
            }

            for( RockBandDotComSong dcSong : dcSongs ) {

                RockBandSong rbSong;

                query = pm.newQuery(RockBandSong.class);
                query.setFilter("generated.midiTitle == midiTitleParam");
                query.declareParameters("String midiTitleParam");
                query.setUnique(true);

                try {
                    rbSong = (RockBandSong)query.execute(dcSong.getMidiTitle());
                }
                finally {
                    query.closeAll();
                }

                if( rbSong == null ) {
                    missingSongs.add(dcSong.getMidiTitle());
                }
            }
        }
        finally {
            pm.close();
        }

        return missingSongs;
    }

    @SuppressWarnings("unchecked")
    public static List<RockBandDotComSong> GetDotComSongs() {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Query query;
        List<RockBandDotComSong> songs;

        query = pm.newQuery(RockBandDotComSong.class);
        query.setOrdering("id asc");

        try {
            songs = (List<RockBandDotComSong>)query.execute();
        }
        finally {
            query.closeAll();
            pm.close();
        }

        return songs;
    }


    public static SortedMap<Date, Long> GetReleaseDateDistribution() {

        SortedMap<Date, Long> dateDistribution;
        List<RockBandSong> songs;

        dateDistribution = new TreeMap<Date, Long>();

        songs = GetSongs(RockBandAdvancedSort.RBReleaseDate);

        for( RockBandSong s : songs ) {
            Date songDate;
            Long dateCount;

            songDate = s.getAssociated().getRBReleaseDate();

            if( dateDistribution.containsKey(songDate) == true ) {
                dateCount = dateDistribution.get(songDate);
                dateDistribution.put(songDate, dateCount + 1L);
            }
            else {
                dateDistribution.put(songDate, 1L);
            }
        }

        return dateDistribution;
    }

/*
    @SuppressWarnings("unchecked")
    public static List<RockBandDotComSong> GetDotComSongs() {

        PersistenceManager pm = PMF.get().getPersistenceManager();
        Extent extent;
        ArrayList<RockBandDotComSong> songs;

        songs = new ArrayList<RockBandDotComSong>();
        extent = pm.getExtent(RockBandDotComSong.class, false);
        for( Object e : extent ) {
            RockBandDotComSong s = (RockBandDotComSong)e;
            songs.add( s );
        }

        extent.closeAll();

        return songs;
    }
 */
}
