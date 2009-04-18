package com.rkuo.Executables;

import com.rkuo.RockBand.*;
import com.rkuo.RockBand.Primitives.Chord;
import com.rkuo.RockBand.Primitives.Note;
import com.rkuo.RockBand.Primitives.DrumChart;
import com.rkuo.RockBand.Simulators.DrumsSimulator;
import com.rkuo.RockBand.Simulators.DrumsSimulatorParameters;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;

import javax.sound.midi.*;
import java.io.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Jul 26, 2008
 * Time: 11:00:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class RockBandAnalyzerExe {
    public static void main( String[] args ) {

        CommandLineParser       clp;
        RockBandAnalyzerParams  rbap;
        String[]                fileNames;
        File                    f;

        clp = new CommandLineParser();

        clp.Parse( args );

        rbap = GetRockBandAnalyzerParams( clp );
        if( rbap == null ) {
            return;
        }

        f = new File( rbap.Source );

        if( f.isDirectory() == true ) {
            fileNames = ProcessDirectory( f );
        }
        else {
            fileNames = new String[1];
            fileNames[0] = f.getAbsolutePath();
        }

        System.out.format( "Processing %d file(s).\n", fileNames.length );
        ProcessFiles( fileNames, rbap );
        return;
    }

    private static void ProcessFiles( String[] fileNames, RockBandAnalyzerParams rbap ) {
        long    succeeded, failed;

        succeeded = 0;
        failed = 0;

        for( String fileName2 : fileNames ) {
            File    f2;

            f2 = new File( fileName2 );

            try {
                ProcessFile( f2, rbap );
                succeeded++;
            }
            catch( Exception ex ) {
                System.out.format( "Exception while processing %s\n", fileName2 );
                failed++;
            }
        }

        System.out.format( "Succeeded: %d, Failed: %d\n", succeeded, failed );
        return;
    }

    private static String[] ProcessDirectory( File fIn ) {

        ArrayList<String>   fileNames;
        File[]  files;

        fileNames = new ArrayList<String>();
        
        files = fIn.listFiles();
        for( File f : files ) {
            if( f.isDirectory() == true ) {
                String[]    subFileNames;

                subFileNames = ProcessDirectory( f );
                Collections.addAll( fileNames, subFileNames );
            }
            else {
                String      fileName;

                fileName = f.getName();
                fileName = fileName.toLowerCase();

                if( fileName.endsWith(".mid") == true ) {
                    fileNames.add( f.getAbsolutePath() );
                }
            }
        }

        return fileNames.toArray( new String[fileNames.size()] );
    }

    private static boolean ProcessFile( File fIn, RockBandAnalyzerParams rbap ) {

        String      fileName;
        Sequence    s;
        DrumChart   dc;

        fileName = fIn.getName();

        System.out.format( "Processing %s.\n", fIn.getAbsolutePath() );

        try {
            s = MidiSystem.getSequence( fIn );
        }
        catch( InvalidMidiDataException imde ) {
            System.out.format( "InvalidMidiDataException\n" );
            System.out.format( "The file %s may not be a valid MIDI file.\n", fileName );
            s = null;
        }
        catch( IOException ie ) {
            System.out.format( "IOException\n" );
            System.out.format( "The file %s could not be opened. Are you sure it's there?\n", fileName );
            s = null;
        }
        catch( Exception e ) {
            System.out.format( "Exception\n" );
            s = null;
        }

        if( s == null ) {
            System.out.format( "The file %s could not be opened. Exiting.\n", fileName );
            return false;
        }

//        RockBandMidi.DumpSequence( s );
//        RockBandMidi.DumpTempoTrack( s );

        System.out.format( "Opened %s successfully.\n", fileName );

        dc = Convert.ToDrumChart( RockBandDifficulty.Expert, s );
        if( dc == null ) {
            System.out.format( "Convert.ToDrumChart returned null.\n");
            return false;
        }

        // Print stats
        if( rbap.PrintChart == true ) {
            RockBandPrint.PrintDrumChart( dc );
        }

        DrumsSimulator dg;
        DrumsSimulatorParameters    dsp;
        DrumsBaselineData           dbd;
        ArrayList<RockBandPath>     paths;
        int[]                       skipPath;

        dg = new DrumsSimulator();
        dsp = new DrumsSimulatorParameters();
        dsp.FillDelay = RockBandConstants.FillDelayRB2Expert;

        RockBandPrint.PrintDrumsSimulatorParameters( dsp );

        dbd = dg.GenerateBaselineData( dc );
        RockBandPrint.PrintDrumsBaselineData( dbd );

        skipPath = new int[5];
        skipPath[0] = 0;
        skipPath[1] = 0;
        skipPath[2] = 2;
        skipPath[3] = 0;
        skipPath[4] = 0;

        if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Predefined ) {
            paths = dg.GenerateScoreFromPath( dsp, dc, rbap.Path.toArray( new Integer[rbap.Path.size()]) );
        }
        else if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Optimal ) {
            paths = dg.GenerateOptimalPaths( dsp, dc );
        }
        else {
            paths = dg.GenerateAllPaths( dsp, dc );
        }

        RockBandPrint.PrintPaths( paths );

        return true;
    }

    private static RockBandAnalyzerParams GetRockBandAnalyzerParams( CommandLineParser clp ) {

        RockBandAnalyzerParams  rbap;

        rbap = new RockBandAnalyzerParams();

        if( clp.Contains("source") == false ) {
            return null;
        }

        rbap.Source = clp.GetString( "source" );

        if( clp.Contains("printchart") == true ) {
            rbap.PrintChart = clp.GetBoolean( "printchart" );
        }

        if( clp.Contains("pathingalgorithm") == true ) {
            String  pathingAlgorithm;

            pathingAlgorithm = clp.GetString( "printchart" );
            if( pathingAlgorithm.compareToIgnoreCase("predefined") == 0 ) {
                rbap.PathingAlgorithm = RockBandPathingAlgorithm.Predefined;
            }
            else if( pathingAlgorithm.compareToIgnoreCase("optimal") == 0 ) {
                rbap.PathingAlgorithm = RockBandPathingAlgorithm.Optimal;
            }
            else if( pathingAlgorithm.compareToIgnoreCase("full") == 0 ) {
                rbap.PathingAlgorithm = RockBandPathingAlgorithm.Full;
            }
            else {
                return null;
            }
        }

        if( rbap.PathingAlgorithm == RockBandPathingAlgorithm.Predefined ) {
            if( clp.Contains("path") == false ) {
                String  path;
                String[]  skips;

                path = clp.GetString("path");
                skips = path.split(",");

                for( String skip : skips ) {
                    rbap.Path.add( Integer.parseInt(skip) );
                }
            }
        }

        return rbap;
    }
}
