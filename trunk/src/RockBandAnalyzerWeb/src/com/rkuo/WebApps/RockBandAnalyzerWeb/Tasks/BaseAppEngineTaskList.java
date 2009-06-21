package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.CMF;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 22, 2009
 * Time: 12:40:02 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseAppEngineTaskList implements IAppEngineTaskList {

    protected final String _taskListIdPrefix = "AETaskList_";
    protected String _taskListId;
    protected ArrayList<IAppEngineTask> _tasks;
    protected Long  _interval;

    public BaseAppEngineTaskList() {
        _tasks = new ArrayList<IAppEngineTask>();
        _taskListId = "";
        _interval = 0L;
        return;
    }

    public Integer getTaskCount() {
        return _tasks.size();
    }

    public IAppEngineTask getTask( Integer i ) {
        return _tasks.get(i);
    }

    public void Reset() {

        for( IAppEngineTask t : _tasks ) {
            t.Reset();
        }

        if( _interval == 0 ) {
            CMF.put( _taskListId, true );
        }
        else {
            CMF.remove( _taskListId );
        }

        return;
    }

    public Boolean ShouldRun() {

        Boolean br;

        if( _interval == 0 ) {
            br = CMF.containsKey( _taskListId );
            if( br == false ) {
                return false;
            }
        }
        else {
            Long lastClearTime;
            Long now;

            now = System.currentTimeMillis();

            lastClearTime = (Long)CMF.get( _taskListId );
            if( lastClearTime != null ) {
                if( now - lastClearTime < _interval ) {
                    return false;
                }
            }
        }

        return true;
    }

    protected void setId( String id ) {
        _taskListId = _taskListIdPrefix + id;
        return;
    }

    protected String getTaskListId() {
        return _taskListId;
    }

    public Boolean Finish() {
        if( _interval == 0 ) {
            CMF.remove( _taskListId );
        }
        else {
            CMF.put( _taskListId, System.currentTimeMillis() );
        }
        
        return true;
    }
}
