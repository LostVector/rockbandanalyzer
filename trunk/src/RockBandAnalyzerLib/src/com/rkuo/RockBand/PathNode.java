package com.rkuo.RockBand;

import com.rkuo.RockBand.Primitives.Chord;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Aug 16, 2008
 * Time: 4:34:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PathNode {
    public boolean  Activate;
    public Chord Squeeze;

    public PathNode() {
        Activate = false;
        Squeeze = null;
        return;
    }
}
