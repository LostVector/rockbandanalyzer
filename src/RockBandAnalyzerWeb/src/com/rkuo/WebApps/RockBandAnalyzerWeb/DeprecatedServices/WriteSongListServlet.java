package com.rkuo.WebApps.RockBandAnalyzerWeb.DeprecatedServices;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Services.BaseServlet;
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


public class WriteSongListServlet extends BaseServlet {

    private static final Logger log = Logger.getLogger(WriteSongListServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        List<String>    midiTitles;
        List<String>    writeRange;
        RKTimer t;

        t = new RKTimer();
        t.Start();

        midiTitles = (List<String>)getCache().get( CacheKeys.ScrapedMidiTitles );
        if( midiTitles == null ) {
            log.log( Level.INFO, String.format( "%s is not cached. Nothing to do.", CacheKeys.ScrapedMidiTitles ) );
            return;
        }

        if( midiTitles.size() >= 100 ) {
            writeRange = midiTitles.subList( 0, 100 );
        }
        else {
            writeRange = midiTitles.subList( 0, midiTitles.size() );
        }

        for( String midiTitle : writeRange ) {
            DataAccess.WriteDotComSong( midiTitle );
        }

        int writeCount = writeRange.size();
        for( int x=0; x < writeCount; x++ ) {
            midiTitles.remove(0);
        }

        if( midiTitles.size() == 0 ) {
            getCache().remove( CacheKeys.ScrapedMidiTitles );
        }
        else {
            getCache().put( CacheKeys.ScrapedMidiTitles, midiTitles );
        }

        log.log( Level.INFO, String.format( "Wrote %d titles in %d ms", writeCount, t.Stop() ) );
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
