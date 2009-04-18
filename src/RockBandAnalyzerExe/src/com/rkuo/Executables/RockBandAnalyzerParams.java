package com.rkuo.Executables;

import com.rkuo.RockBand.RockBandPathingAlgorithm;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 18, 2009
 * Time: 1:08:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandAnalyzerParams {

    public String   Source;
    public boolean  PrintChart;
    public RockBandPathingAlgorithm PathingAlgorithm;
    public ArrayList<Integer>   Path;

    public RockBandAnalyzerParams() {

        Source = "";
        PrintChart = false;
        PathingAlgorithm = RockBandPathingAlgorithm.Optimal;
        Path = new ArrayList<Integer>();
        return;
    }
}
