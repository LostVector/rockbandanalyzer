package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.Simulators.IDrumActivationHandler;
import com.rkuo.RockBand.RockBandPlayerState;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 11, 2008
 * Time: 12:56:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class BreadthDrumsActivationHandler implements IDrumActivationHandler {

    public ArrayList<RockBandPlayerState> NextStates;

    public BreadthDrumsActivationHandler() {
        NextStates = new ArrayList<RockBandPlayerState>();
        return;
    }

    public void HandlePotentialActivation( DrumChart dc, RockBandPlayerState rbps ) {
        rbps.LastPotentialActivationTick = rbps.Now;

        rbps.CreateChildNodes();

        rbps.NodeActivate.ActivateOverdrive( dc );

        NextStates.add( rbps.NodeActivate );
        NextStates.add( rbps.NodeSkip );
        return;
    }

    public void HandleNonActivation( RockBandPlayerState rbps ) {
        NextStates.add( rbps );
        return;
    }
}
