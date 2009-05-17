package com.rkuo.WebApps.RockBandAnalyzerWeb.Services;

import javax.servlet.http.HttpServlet;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import java.util.logging.Logger;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 14, 2009
 * Time: 11:02:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseServlet extends HttpServlet {

    protected static Cache getCache() {
        Cache cache;

        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        }
        catch( CacheException e ) {
            return null;
        }

        return cache;
    }
}
