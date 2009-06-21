package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 21, 2009
 * Time: 11:28:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAppEngineTaskList {

    public abstract Boolean Finish();
    public abstract Boolean ShouldRun();

    public abstract Integer getTaskCount();
    public abstract IAppEngineTask getTask( Integer idx );
    public abstract void Reset();
}
