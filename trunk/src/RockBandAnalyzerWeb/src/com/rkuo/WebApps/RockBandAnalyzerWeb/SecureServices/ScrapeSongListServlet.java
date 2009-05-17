package com.rkuo.WebApps.RockBandAnalyzerWeb.SecureServices;

import com.rkuo.WebApps.RockBandAnalyzerWeb.Services.BaseServlet;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.RockBand.JTidyDomScraper;
import com.rkuo.util.RKTimer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class ScrapeSongListServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(ScrapeSongListServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List<String>    midiTitles;
        RKTimer t;

        t = new RKTimer();
        t.Start();

        midiTitles = JTidyDomScraper.ScrapeSongs( "http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=" );
        if( midiTitles == null ) {
            log.log( Level.INFO, String.format( "ScrapeSongs failed." ) );
            return;
        }

        log.log( Level.INFO, String.format( "ScrapeSongs - time elapsed: %d ms", t.Stop() ) );

//        t.Start();
//        DataAccess.DropTable( "RockBandDotComSong" );
//        log.log( Level.INFO, String.format( "DropTable for RockBandDotComSong - time elapsed: %d ms", t.Stop() ) );

        getCache().put( CacheKeys.ScrapedMidiTitles, midiTitles );
        getCache().put( CacheKeys.DropMidiTitles, true );
        getCache().remove( CacheKeys.LastSongDetailScrapePass );
//        DataAccess.WriteDotComSongs( midiTitles );

        log.log( Level.INFO, String.format( "Cached %d songs.", midiTitles.size() ) );        
        return;
    }

    protected String getEntityName( Document doc ) {

        String    entityName;

        NodeList nodeList;
        Element e;

        nodeList = doc.getElementsByTagName("entityName");
        e = (Element)nodeList.item(0);
        entityName = e.getFirstChild().getNodeValue();

        return entityName;
    }
}
