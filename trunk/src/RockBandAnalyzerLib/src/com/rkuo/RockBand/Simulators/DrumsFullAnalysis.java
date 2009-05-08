package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.RockBandPath;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 5, 2009
 * Time: 12:14:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class DrumsFullAnalysis {

    public DrumsBaselineAnalysis    dba;

    public RockBandPath RB2PathImmediate;
    public RockBandPath RB2PathNormalOptimal;
    public RockBandPath RB2PathBreakneckOptimal;
    public RockBandPath RB2PathOverallOptimal;

    public DrumsFullAnalysis() {

        RB2PathImmediate = new RockBandPath();
        RB2PathNormalOptimal = new RockBandPath();
        RB2PathBreakneckOptimal = new RockBandPath();
        RB2PathOverallOptimal = new RockBandPath();

        dba = new DrumsBaselineAnalysis();
        return;
    }
}
