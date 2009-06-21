package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CMF;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 23, 2009
 * Time: 3:47:49 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAppEngineTask implements IAppEngineTask {

    protected String _className;
    protected String _taskId;
    protected final String _taskIdPrefix = "AETask_";

    protected Logger getLogger() {
        return Logger.getLogger( _className );
    }

    protected void setId( String id ) {
        _taskId = _taskIdPrefix + id;
        _className = id;
        return;
    }

    public Boolean ShouldRun() {

        Boolean br;

        br = CMF.containsKey( _taskId + "_Finished" );
        if( br == true ) {
            return false;
        }

        return true;
    }

   public void Start() {
        CMF.put( _taskId, true );
        return;
    }

    public void Finish() {
        CMF.put( _taskId + "_Finished", true );
        CMF.remove( _taskId );
        getLogger().log( Level.INFO, String.format( "%s::Finish", _className ) );
        return;
    }

    public void Reset() {
        CMF.remove( _taskId + "_Finished" );
        CMF.remove( _taskId );
        return;
    }
}
