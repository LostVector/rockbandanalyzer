package com.rkuo.WebApps.RockBandAnalyzerWeb;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Pages.IndexPage;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Pages.BrowsePage;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Pages.UploadPage;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Pages.SongPage;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:55:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandAnalyzerWebApplication extends WebApplication {

    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    protected void init() {
        super.init();

        mountBookmarkablePage("/browse", BrowsePage.class);
        mountBookmarkablePage("/upload", UploadPage.class);
        mountBookmarkablePage("/song", SongPage.class);

        getResourceSettings().setResourcePollFrequency(null);
        return;
    }

    protected ISessionStore newSessionStore() {
        //return new SecondLevelCacheSessionStore(this, new DiskPageStore());
        return new HttpSessionStore(this);
    }

    @Override
    public String getConfigurationType() {
//        return Application.DEPLOYMENT;    //To change body of overridden methods use File | Settings | File Templates.
        return Application.DEVELOPMENT;
    }
}
