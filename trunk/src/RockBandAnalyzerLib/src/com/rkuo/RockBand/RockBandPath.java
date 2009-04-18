package com.rkuo.RockBand;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jan 2, 2009
 * Time: 4:32:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandPath {
    public ArrayList<PathNode> Path;
    public long Score;
    public int NoteStreak;

    public RockBandPath() {
        Path = new ArrayList<PathNode>();
        Score = 0;
        NoteStreak = 0;
        return;
    }
}
