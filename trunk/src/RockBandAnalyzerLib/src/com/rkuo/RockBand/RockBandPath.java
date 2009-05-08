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
    public long FillDelay;

    public RockBandPath() {
        Path = new ArrayList<PathNode>();
        Score = 0;
        NoteStreak = 0;
        FillDelay = RockBandConstants.FillDelayRB2Expert;
        return;
    }

    @Override
    public String toString() {
        
        StringBuilder   sb;
        int     skipCounter;
        boolean firstOutput;

        sb = new StringBuilder();

        sb.append( String.format( "Path: [" ) );

        skipCounter = 0;
        firstOutput = true;
        for( PathNode p : Path ) {
            if( p.Activate == false ) {
                skipCounter++;
            }
            else {
                if( firstOutput == true ) {
                    sb.append( String.format( "%d/%s", skipCounter, RockBandPrint.FormatChordShort(p.Squeeze) ) );
                    firstOutput = false;
                }
                else {
                    sb.append( String.format( ",%d/%s", skipCounter, RockBandPrint.FormatChordShort(p.Squeeze) ) );
                }

                skipCounter = 0;
            }
        }

        // The star notation denotes skips that never activate because we reached the end of the song
        if( skipCounter > 0 ) {
            if( firstOutput == true ) {
                sb.append( String.format( "%d*", skipCounter ) );
            }
            else {
                sb.append( String.format( ",%d*", skipCounter ) );
            }
        }

        sb.append( String.format( "], Score: %d, Note Streak = %d\n", Score, NoteStreak ) );
        return sb.toString();
    }
/*
    public String Serialize() {

        String  xmlOut;

        for( PathNode pathNode : Path ) {

        }

        return xmlOut;
    }

    public void Deserialize() {
        return;
    }
 */
}
