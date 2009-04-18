package com.rkuo.RockBand.Simulators;

import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.Simulators.IDrumActivationHandler;
import com.rkuo.RockBand.RockBandPlayerState;
import com.rkuo.RockBand.PathNode;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 11, 2008
 * Time: 12:59:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class DepthDrumsActivationHandler implements IDrumActivationHandler {

    public ArrayList<RockBandPlayerState> NextStates;

    public DepthDrumsActivationHandler() {
        NextStates = new ArrayList<RockBandPlayerState>();
        return;
    }

    public void HandlePotentialActivation( DrumChart dc, RockBandPlayerState rbps ) {
        // We are in a fill, and this is the activation point.
        rbps.LastPotentialActivationTick = rbps.Now;

        // We're brute forcing
        if( rbps.SuggestedPath.size() == 0 ) {
//                        Compare this path with other paths we've already traced

            // This isn't a "real" note, just a potential activation note
            rbps.CreateChildNodes();

            rbps.NodeActivate.ActivateOverdrive( dc );
            NextStates.add( rbps.NodeActivate );
            NextStates.add( rbps.NodeSkip );
            return;
        }

        // We're following a suggested path
        if( rbps.SkipCounter > 0 ) {
//            System.out.format( "T%d: Overdrive activation skipped\n", rbps.Now );

            // Skip
            rbps.SkipCounter--;

            PathNode skipNode;

            skipNode = new PathNode();

            rbps.CurrentPath.add( skipNode );
        }
        else {
//            System.out.format( "T%d: Overdrive activated\n", rbps.Now );

            PathNode    activateNode;

            activateNode = new PathNode();
            activateNode.Activate = true;

            // Activate!!!
            rbps.CurrentPath.add( activateNode );

            rbps.ActivateOverdrive( dc );

            // Update path rbps
            rbps.SkipIndex++;

            // Handle the convention where a lack of trailing skip numbers means no skipping
            if( rbps.SkipIndex < rbps.SuggestedPath.size() ) {
                rbps.SkipCounter = rbps.SuggestedPath.get(rbps.SkipIndex);
            }
            else {
                rbps.SkipCounter = 0;
            }
        }

        return;
    }

    public void HandleNonActivation( RockBandPlayerState rbps ) {
        return;
    }
}
