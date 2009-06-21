package com.rkuo.WebApps.RockBandAnalyzerWeb.DeprecatedServices;

import com.rkuo.RockBand.JTidyDomScraper;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Services.BaseServlet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ScrapeSongDetailServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(ScrapeSongDetailServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        SortedMap<String,String> properties;
        String                   midiTitle;

        Long    now;

        now = System.currentTimeMillis();
/*
        RockBandDotComSong  rbdcScrapeSong;

        rbdcScrapeSong = DataAccess.GetDotComSongForScraping( now );
        if( rbdcScrapeSong == null ) {
            // There are no more attempts to make, so we're going to note that in the cache
            this.getCache().put( CacheKeys.LastSongDetailScrapePass, now );
            log.log( Level.INFO, String.format("Finished scraping song details. Noting it in memcache.") );
            return;
        }
 */

        midiTitle = req.getParameter( "title" );

        try {
            properties = JTidyDomScraper.ScrapeSongDetail( "http://www.rockband.com/songs/" + midiTitle );
        }
        catch( Exception ex ) {
            log.log( Level.WARNING, String.format( "ScrapeSongDetail exceptioned on %s.", midiTitle ) );
            return;
        }

        if( properties == null ) {
            log.log( Level.INFO, String.format( "ScrapeSongDetail failed on %s.", midiTitle ) );
            return;
        }

        DataAccess.UpdateDotComSong( midiTitle, now, properties );
        DataAccess.MergeDotComSong( System.currentTimeMillis(), midiTitle );
        DataAccess.SetLastUpdated();
        log.log( Level.INFO, String.format( "Updated %s", midiTitle ) );
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
