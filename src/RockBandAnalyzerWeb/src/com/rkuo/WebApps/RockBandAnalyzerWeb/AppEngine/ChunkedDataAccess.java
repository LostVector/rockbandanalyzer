package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import javax.jdo.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/*
All the methods here are designed to accept exceptioning out as part of the normal workflow.
 */

public class ChunkedDataAccess {

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

    public static Boolean CacheRockBandSongTable() {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        ArrayList<RockBandSong> objects;
        String entityListCacheKey;
        String nextKeyCacheKey;
        Boolean br;
        Long nextKey;

        entityListCacheKey = "RockBandSong_CacheTemp";
        nextKeyCacheKey = "RockBandSong_CacheNextKey";

        br = DataAccess.GetPropertyAsBoolean("RockBandSong_Cached");
        if( br != null ) {
            if( br == true ) {
                pm.close();
                return true;
            }
        }

        objects = (ArrayList<RockBandSong>)getCache().get(entityListCacheKey);
        if( objects == null ) {
            objects = new ArrayList<RockBandSong>();
        }

        nextKey = (Long)getCache().get(nextKeyCacheKey);
        if( nextKey == null ) {
            getCache().remove(entityListCacheKey);
            objects = new ArrayList<RockBandSong>();
        }

        try {
            while( true ) {
                List<RockBandSong> tempObjects;
                javax.jdo.Query query;
                String  idParam;

                idParam = "rbs";
                query = pm.newQuery(RockBandSong.class);
                query.declareParameters("String idParam");
                query.setRange(0, 33);
                if( nextKey != null ) {
                    query.setFilter("id >= idParam");
                    idParam += nextKey.toString();
                }

                try {
                    tempObjects = (List<RockBandSong>)query.execute(idParam);
                }
                finally {
                    query.closeAll();
                }

                if( tempObjects == null ) {
                    break;
                }

                if( tempObjects.size() == 0 ) {
                    break;
                }

                if( tempObjects.size() <= 32 ) {
                    objects.addAll(tempObjects);
                    break;
                }

                objects.addAll(tempObjects.subList(0, 32));
                nextKey = tempObjects.get(32).getId();
                getCache().put(entityListCacheKey, objects);
                getCache().put(nextKeyCacheKey, nextKey);
            }

            getCache().put("RockBandSong", objects);
        }
        finally {
            pm.close();
        }

        getCache().remove(nextKeyCacheKey);
        getCache().remove(entityListCacheKey);
        DataAccess.SetProperty("RockBandSong_Cached", "true");
        return true;
    }


    public static Boolean CacheRockBandDotComSongTable() {

        PersistenceManager pm = PMF.get().getPersistenceManager();

        ArrayList<RockBandDotComSong> objects;
        String className;
        String entityListCacheKey;
        String nextKeyCacheKey;
        Boolean br;
        Long nextKey;

        className = "RockBandDotComSong";
        entityListCacheKey = className + "_CacheTemp";
        nextKeyCacheKey = className + "_CacheNextKey";

        br = DataAccess.GetPropertyAsBoolean(className + "_Cached");
        if( br != null ) {
            if( br == true ) {
                pm.close();
                return true;
            }
        }

        objects = (ArrayList<RockBandDotComSong>)getCache().get(entityListCacheKey);
        if( objects == null ) {
            objects = new ArrayList<RockBandDotComSong>();
        }

        nextKey = (Long)getCache().get(nextKeyCacheKey);
        if( nextKey == null ) {
            getCache().remove(entityListCacheKey);
            objects = new ArrayList<RockBandDotComSong>();
        }

        try {
            while( true ) {
                List<RockBandDotComSong> tempObjects;
                javax.jdo.Query query;

                query = pm.newQuery(RockBandDotComSong.class);
                query.declareParameters("Long idParam");
                query.setRange(0, 33);
                if( nextKey != null ) {
                    query.setFilter("id >= idParam");
                }

                try {
                    tempObjects = (List<RockBandDotComSong>)query.execute(nextKey);
                }
                finally {
                    query.closeAll();
                }

                if( tempObjects == null ) {
                    break;
                }

                if( tempObjects.size() == 0 ) {
                    break;
                }

                if( tempObjects.size() <= 32 ) {
                    objects.addAll(tempObjects);
                    break;
                }

                objects.addAll(tempObjects.subList(0, 32));
                nextKey = tempObjects.get(32).getId();
                getCache().put(entityListCacheKey, objects);
                getCache().put(nextKeyCacheKey, nextKey);
            }

            getCache().put(className, objects);
        }
        finally {
            pm.close();
        }

        getCache().remove(nextKeyCacheKey);
        getCache().remove(entityListCacheKey);
        DataAccess.SetProperty(className + "_Cached", "true");
        return true;
    }

    public static Boolean CacheEntities(String entityName) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        ArrayList<Entity> entities;
        String entityListCacheKey;
        String nextKeyCacheKey;
        Boolean br;
        Key nextKey;

        entityListCacheKey = entityName + "_ShardedGetTemp";
        nextKeyCacheKey = entityName + "_ShardedGetNextKey";

        br = DataAccess.GetPropertyAsBoolean(entityName + "_Cached");
        if( br != null ) {
            if( br == true ) {
                return true;
            }
        }

        entities = (ArrayList<Entity>)getCache().get(entityListCacheKey);
        if( entities == null ) {
            entities = new ArrayList<Entity>();
        }

        nextKey = (Key)getCache().get(nextKeyCacheKey);
        if( nextKey == null ) {
            getCache().remove(entityListCacheKey);
            entities = new ArrayList<Entity>();
        }

        while( true ) {
            com.google.appengine.api.datastore.Query q;
            List<Entity> tempEntities;

            q = new com.google.appengine.api.datastore.Query(entityName);
            if( nextKey != null ) {
                q.addFilter("__key__", com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, nextKey);
            }

            tempEntities = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(33));
            if( tempEntities == null ) {
                break;
            }

            if( tempEntities.size() == 0 ) {
                break;
            }

            if( tempEntities.size() <= 32 ) {
                entities.addAll(tempEntities);
                break;
            }

            entities.addAll(tempEntities.subList(0, 32));
            nextKey = tempEntities.get(32).getKey();
            getCache().put(entityListCacheKey, entities);
            getCache().put(nextKeyCacheKey, nextKey);
        }

        getCache().put(entityName, entities);
        getCache().remove(nextKeyCacheKey);
        getCache().remove(entityListCacheKey);
        DataAccess.SetProperty(entityName + "_Cached", "true");
        return true;
    }

    // Not really expecting to time out here.
    public static Long Count(String entityName) {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Long entityCount;
        Key nextKey;

        entityCount = 0L;
        nextKey = null;

        while( true ) {
            com.google.appengine.api.datastore.Query q;
            List<Entity> entities;
//            Transaction t;

            q = new com.google.appengine.api.datastore.Query(entityName);
            if( nextKey != null ) {
                q.addFilter("__key__", com.google.appengine.api.datastore.Query.FilterOperator.GREATER_THAN_OR_EQUAL, nextKey);
            }

//            t = datastore.beginTransaction();
            entities = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(33));
            if( entities == null ) {
                break;
            }

            if( entities.size() == 0 ) {
                break;
            }

            if( entities.size() == 33 ) {
                entityCount += 32;
                nextKey = entities.get(32).getKey();
            }
            else {
                entityCount += entities.size();
                break;
            }
        }

        //      System.out.format( "TestPage took %d ms. Entity count = %d.\n", t.Stop(), entityCount );
        return entityCount;
    }

}
