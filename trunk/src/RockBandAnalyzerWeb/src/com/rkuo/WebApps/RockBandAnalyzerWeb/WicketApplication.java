package com.rkuo.WebApps.RockBandAnalyzerWeb;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Pages.*;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.Util;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:55:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WicketApplication extends WebApplication {

    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    protected void init() {
        super.init();

        mountBookmarkablePage("/browse", BrowsePage.class);
        mountBookmarkablePage("/upload", UploadPage.class);
        mountBookmarkablePage("/song", SongPage.class);
        mountBookmarkablePage("/about", AboutPage.class);
        mountBookmarkablePage("/stats", StatsPage.class);
        mountBookmarkablePage("/terms", TermsPage.class);
        mountBookmarkablePage("/source", SourcePage.class);
        mountBookmarkablePage("/export", ExportPage.class);
        mountBookmarkablePage("/import", ImportPage.class);
        mountBookmarkablePage("/career", CareerPage.class);
        mountBookmarkablePage("/browsedotcom", BrowseDotComPage.class);
        mountBookmarkablePage("/admin", AdminPage.class);
        mountBookmarkablePage("/test", TestPage.class);

        getResourceSettings().setResourcePollFrequency(null);
        return;
    }

    protected ISessionStore newSessionStore() {
        //return new SecondLevelCacheSessionStore(this, new DiskPageStore());
        return new HttpSessionStore(this);
    }

    @Override
    public String getConfigurationType() {

        if( Util.IsDebug() == true ) {
            return Application.DEVELOPMENT;
        }

        return Application.DEPLOYMENT;
    }
}
