package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import java.util.Collections;


public final class CMF {

    private CMF() {}

    public static Object get( Object key ) {
        return getCache().get( key );
    }

    public static Object put( Object key, Object val ) {
        return getCache().put( key, val );
    }

    public static Object remove( Object key ) {
        return getCache().remove( key );
    }

    public static Boolean containsKey( Object key ) {
        return getCache().containsKey( key );
    }

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
