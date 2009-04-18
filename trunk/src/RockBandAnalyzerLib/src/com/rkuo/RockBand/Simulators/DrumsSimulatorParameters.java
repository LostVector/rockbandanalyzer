package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.RockBandConstants;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 16, 2008
 * Time: 6:38:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrumsSimulatorParameters { 

    // if false, assumes that hitting overdrive phrases is always optimal
    // if true, does not assume the above, which will increase the range of possibilities to check
    // since we need to test skipping overdrive phrases with overhits
    // We do know for a fact that intentionally overhitting to skip the overdrive gained from a phrase
    // can sometimes lead to a higher score (see BYOB, Gimme Three Steps, etc)
    public boolean OverhitOverdrivePhrases;

    public boolean FrontEndSqueezes;
    public boolean DetectGlitches;

    public long     FillDelay;

    public DrumsSimulatorParameters() {
        OverhitOverdrivePhrases = false;
        FrontEndSqueezes = true;
        DetectGlitches = true;

        FillDelay = RockBandConstants.FillDelayRB1;
        return;
    }
}
