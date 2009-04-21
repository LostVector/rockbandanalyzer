package com.rkuo.Executables;

import com.rkuo.RockBand.*;

import java.io.*;
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

        FileInputStream fis;

        try {
            fis = new FileInputStream( fIn );

            System.out.format( "Processing %s.\n", fIn.getAbsolutePath() );
            RockBandAnalyzer.AnalyzeStream( new PrintWriter(System.out, true), fis, rbap );
        }
        catch( FileNotFoundException fnfex ) {
            System.out.format( "Could not open %s.\n", fIn.getName() );
            return false;
        }

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
