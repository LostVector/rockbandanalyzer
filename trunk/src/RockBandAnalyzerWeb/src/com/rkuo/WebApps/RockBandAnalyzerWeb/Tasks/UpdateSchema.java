package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CMF;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CacheKeys;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;

public class UpdateSchema extends BaseAppEngineTaskList {

    public UpdateSchema() {
        _tasks.add( new UpdateSchemaTask() );
        setId( this.getClass().getName() );
        return;
    }

    public class UpdateSchemaTask extends BaseAppEngineTask  {
        public UpdateSchemaTask() {
            setId( this.getClass().getName() );
            return;
        }

        @Override
        public void Reset() {
            super.Reset();
            CMF.remove( CacheKeys.NextUpdateSchemaEntityId );
            CMF.remove( CacheKeys.UpdatedSchemaCount );
            return;
        }

        public Boolean Execute() {

            Long nextId;

            // It's fine if this returns null
            nextId = (Long)CMF.get(CacheKeys.NextUpdateSchemaEntityId);

            while( true ) {
                Long processedCount;
                Long newNextId;

                processedCount = (Long)CMF.get(CacheKeys.UpdatedSchemaCount);
                if( processedCount == null ) {
                    processedCount = 0L;
                }

                newNextId = DataAccess.UpdateSchema(nextId);
                if( newNextId == null ) {
                    break;
                }

                nextId = newNextId;
                CMF.put(CacheKeys.NextUpdateSchemaEntityId, nextId);
                CMF.put(CacheKeys.UpdatedSchemaCount, processedCount + 1L);
            }

            return true;
        }
    }
}
