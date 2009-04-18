package com.rkuo.RockBand.Primitives;

import com.rkuo.RockBand.Primitives.Chord;
import com.rkuo.RockBand.Primitives.TickRange;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 25, 2008
 * Time: 12:13:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class DrumChart {

    private long _resolution;
    private TreeMap<Long, Long>     _tempo;
    private ArrayList<Chord>        _chords;
    private ArrayList<Chord>        _beats;
    private TreeMap<Long, Long>     _soloPhrases;
    private TreeMap<Long, Long>     _overdrivePhrases;
    private TreeMap<Long, Long>     _fillPhrases;

    private TickRange               _range;

    public ArrayList<Chord>        ChordsAndActivations;

    public DrumChart() {
        _tempo = new TreeMap<Long, Long>();
        _chords = new ArrayList<Chord>();
        _beats = new ArrayList<Chord>();
        _soloPhrases = new TreeMap<Long, Long>();
        _overdrivePhrases = new TreeMap<Long, Long>();
        _fillPhrases = new TreeMap<Long, Long>();
        _resolution = 0;

        _range = new TickRange();
        _range.Start = 0;
        _range.End = 0;

        ChordsAndActivations = new ArrayList<Chord>();
        return;
    }

    public long getResolution() {
        return _resolution;
    }

    public void setResolution( long resolution ) {
        _resolution = resolution;
        return;
    }

    public ArrayList<Chord> getChords() {
        return _chords;
    }

    public ArrayList<Chord> getBeats() {
        return _beats;
    }

    public SortedMap<Long, Long> getTempo() {
        return _tempo;
    }

    public SortedMap<Long, Long> getSoloPhrases() {
        return _soloPhrases;
    }

    public SortedMap<Long, Long> getOverdrivePhrases() {
        return _overdrivePhrases;
    }

    public SortedMap<Long, Long> getFillPhrases() {
        return _fillPhrases;
    }

    public void AddTempo( long tick, long tempo ) {
        _tempo.put( tick, tempo );
        return;
    }

    public void AddChord( Chord c ) {
        _chords.add( c );
        return;
    }

    public void AddBeat( Chord c ) {
        _beats.add( c );
        return;
    }

    public void AddSoloPhrase( long startTick, long endTick ) {
        _soloPhrases.put( startTick, endTick );
        return;
    }

    public void AddOverdrivePhrase( long startTick, long endTick ) {
        _overdrivePhrases.put( startTick, endTick );
        return;
    }

    public void AddFillPhrase( long startTick, long endTick ) {
        _fillPhrases.put( startTick, endTick );
        return;
    }
    
    public void setBigRockEnding( TickRange range ) {
        _range = range;
        return;
    }

    public TickRange getBigRockEnding() {
        return _range;
    }

    public long GetOverdriveEnd( long overdriveStart, long overdriveBeats ) {

        long                beatCounter;
        float               fractionalBeat;
        Chord               lastBeat;
        long                overdriveEnd;

        beatCounter = 0;
        fractionalBeat = 0;
        lastBeat = null;
        overdriveEnd = 0;

        for( Chord b : _beats ) {

            if( overdriveStart < b.getTick() ) {

                if( lastBeat == null ) {
                    break;
                }

                if( beatCounter == 0 ) {

                    assert( overdriveStart == lastBeat.getTick() );
                    if( overdriveStart != lastBeat.getTick() ) {
                        float   beatNumerator, beatDenominator;

                        beatNumerator = (long)(b.getTick() - overdriveStart);
                        beatDenominator = (long)(b.getTick() - lastBeat.getTick());
                        fractionalBeat =  beatNumerator / beatDenominator;
                    }
                }

                beatCounter++;
            }

            if( beatCounter >= overdriveBeats ) {
                if( fractionalBeat != 0 ) {
                    overdriveEnd = b.getTick() + (long)( (1.0f - fractionalBeat) * (b.getTick() - lastBeat.getTick()) );
                }
                else {
                    overdriveEnd = b.getTick();
                }
                break;
            }

            lastBeat = b;
        }

        assert( overdriveEnd > 0 );
        return overdriveEnd;
    }
}
