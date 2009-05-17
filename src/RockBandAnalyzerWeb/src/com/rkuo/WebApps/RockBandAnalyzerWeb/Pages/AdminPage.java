package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.model.PropertyModel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 16, 2009
 * Time: 10:37:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminPage extends BasePage {

    private ReprocessForm formReprocess;
    private ScrapeForm formScrape;

    public AdminPage() {

        formReprocess = new ReprocessForm("formReprocess");
        formScrape = new ScrapeForm("formScrape");

        add(formReprocess);
        add(formScrape);
        return;
    }

    public final class ReprocessForm extends Form {

        private Label lblReprocessStatus;

        public ReprocessForm(final String componentName) {
            super(componentName);

            PropertyModel<String> reprocessStatusModel;

            reprocessStatusModel = new PropertyModel<String>(this, "reprocessStatus");

            lblReprocessStatus = new Label("lblReprocessStatus", reprocessStatusModel);
            add( lblReprocessStatus );

            lblReprocessStatus.add( new AjaxSelfUpdatingTimerBehavior(Duration.seconds(20)) );
            return;
        }

        public final void onSubmit() {
            getCache().put(CacheKeys.FlagRawSongsForReprocessing, true);
            return;
        }

        public String getReprocessStatus() {

            Long    reprocessedCount;

            if( getCache().containsKey(CacheKeys.FlagRawSongsForReprocessing) == true ) {
                return "Flagging songs for reprocessing...";
            }

            if( getCache().containsKey(CacheKeys.ReprocessRawSongs) == true ) {
                if( getCache().containsKey(CacheKeys.ReprocessedSongCount) == false ) {
                    return "Beginning to reprocess raw songs...";
                }
            }

            reprocessedCount = (Long)getCache().get(CacheKeys.ReprocessedSongCount);
            if( reprocessedCount != null ) {
                return String.format( "Processed %d raw songs so far...", reprocessedCount );
            }

            return "No reprocessing being done at this time.";
        }
    }

    public final class ScrapeForm extends Form {

        private Label lblScrapeStatus;

        public ScrapeForm(final String componentName) {
            super(componentName);

            PropertyModel<String> scrapeStatusModel;

            scrapeStatusModel = new PropertyModel<String>(this, "scrapeStatus");

            lblScrapeStatus = new Label("lblScrapeStatus", scrapeStatusModel);
            add( lblScrapeStatus );

            lblScrapeStatus.add( new AjaxSelfUpdatingTimerBehavior(Duration.seconds(20)) );
            return;
        }

        public final void onSubmit() {
            getCache().remove( CacheKeys.LastSongDetailScrapePass );
            getCache().put( CacheKeys.ScrapeDotComSongList, true );

            return;
        }

        public String getScrapeStatus() {

            Long    scrapedCount;
            Long    lastPass;

            if( getCache().containsKey(CacheKeys.ScrapeDotComSongList) == true ) {
                return "Scraping song list from rockband.com...";
            }

            if( getCache().containsKey(CacheKeys.DropMidiTitles) == true ) {
                return "Dropping old DotComSong table...";
            }

            if( getCache().containsKey(CacheKeys.ScrapedMidiTitles) == true ) {
                Long    loadedCount;
                loadedCount = (Long)getCache().get(CacheKeys.LoadedMidiTitlesCount);
                if( loadedCount == null ) {
                    return "Beginning to load DotComSong table from memcache to datastore...";
                }
                else {
                    return String.format( "Loaded %d DotComSongs from memcache to datastore", loadedCount );                 
                }
            }

            scrapedCount = (Long)getCache().get(CacheKeys.ScrapedSongDetailsCount);
            if( scrapedCount != null ) {
                return String.format( "Scraped %d raw songs so far...", scrapedCount );
            }

            lastPass = (Long)getCache().get(CacheKeys.LastSongDetailScrapePass);
            if( lastPass != null ) {
                Long    now;
                now = System.currentTimeMillis();
                return String.format( "%d ms have elapsed since the last scrape pass...", now - lastPass );
            }

            return "No scraping being done at this time.";
        }
    }
}
