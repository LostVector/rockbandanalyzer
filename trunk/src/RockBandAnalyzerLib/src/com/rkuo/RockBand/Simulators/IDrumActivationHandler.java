package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.RockBandPlayerState;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 11, 2008
 * Time: 12:56:54 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IDrumActivationHandler {

    // Called when the simulator encounters an activation note at the end of a fill
    public abstract void HandlePotentialActivation(DrumChart dc, RockBandPlayerState rbps);

    // Called when the simulator encounters an activation point but no fill is present
    // In other words, the fill isn't really there and there is no activation note, but
    // we still use these points as branch points for the simulation
    public abstract void HandleNonActivation(RockBandPlayerState rbps);
}
