package com.rkuo.WebApps.RockBandAnalyzerWeb.Tasks;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 22, 2009
 * Time: 12:12:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IAppEngineTask {
    public abstract Boolean ShouldRun();

    public abstract void Start();
    public abstract Boolean Execute();
    public abstract void Finish();
    public abstract void Reset();
}
