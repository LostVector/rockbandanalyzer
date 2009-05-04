package com.rkuo.Executables;

import com.rkuo.RockBand.*;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;

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

        if( clp.GetBoolean("upload") == true ) {
            UploadFiles( fileNames, clp.GetString("uploadUrl") );
        }
        else {
            ProcessFiles( fileNames, rbap );
        }

        return;
    }

    private static void UploadFiles( String[] fileNames, String uploadUrl ) {
        long    succeeded, failed;

        succeeded = 0;
        failed = 0;

        for( String fileName2 : fileNames ) {
            File    f2;

            f2 = new File( fileName2 );

            try {
                String  songXml;
                boolean br;

                songXml = SerializeSong( f2 );
                br = UploadFile( songXml, uploadUrl );
                if( br == true ) {
                    System.out.format( "UploadFile succeeded: %s\n", fileName2 );
                    succeeded++;
                }
                else {
                    System.out.format( "UploadFile failed: %s\n", fileName2 );
                    failed++;
                }
            }
            catch( Exception ex ) {
                System.out.format( "Exception while processing %s\n", fileName2 );
                failed++;
            }
        }

        System.out.format( "Succeeded: %d, Failed: %d\n", succeeded, failed );
        return;
    }

    private static boolean UploadFile( String songXml, String uploadUrl ) {

        HttpURLConnection   conn;
        java.net.URL netUrl;

        try {
            netUrl = new java.net.URL( uploadUrl );
        }
        catch( MalformedURLException muex ) {
            return false;
        }

        try {
            conn = (HttpURLConnection)netUrl.openConnection();
        }
        catch( IOException ioex ) {
            return false;
        }

        try {
            conn.setRequestMethod("POST");
        }
        catch( ProtocolException pex ) {
            return false;
        }
        
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        DataOutputStream out;
        int              i;

        conn.setRequestProperty( "Content-type", "text/xml" );

        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes( songXml );
            out.flush();
            out.close();
        }
        catch( IOException ioex ) {
            return false;
        }

        BufferedReader reader;
        String response;
        boolean exists;

        exists = false;

        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            response = reader.readLine();
            while( null != response ) {
//                System.out.println(response);
                if( response.contains("exists") == true ) {
                    exists = true;
                }

                response = reader.readLine();
            }
        }
        catch( IOException ioex ) {
            return false;
        }

/*
       try {
           fis = new FileInputStream( fIn );

           System.out.format( "Processing %s.\n", fIn.getAbsolutePath() );
           RockBandAnalyzer.AnalyzeStream( new PrintWriter(System.out, true), fis, rbap );
       }
       catch( FileNotFoundException fnfex ) {
           System.out.format( "Could not open %s.\n", fIn.getName() );
           return false;
       }
*/

        if( exists == true ) {
            return false;
        }

        return true;
    }

    private static String SerializeSong( File fIn ) {

        String      xmlString;

        XMLOutputFactory xof;
        javax.xml.stream.XMLStreamWriter    w;
        StringWriter        sWriter;
        String  b64FileData;

        FileInputStream fInStream;
        byte fileBytes[];

        try {
            fInStream = new FileInputStream( fIn );
        }
        catch( FileNotFoundException fnfex ) {
            return null;
        }

        fileBytes = new byte[(int)fIn.length()];

        try {
            fInStream.read( fileBytes );
            fInStream.close();
        }
         catch( IOException ioex ) {
             return null;
         }

        b64FileData = Base64.encodeBytes( fileBytes );

        sWriter = new StringWriter();
        xof = XMLOutputFactory.newInstance();

        try {
            w = xof.createXMLStreamWriter(sWriter);
        }
        catch( XMLStreamException xsex ) {
            return null;
        }

        try {
            w.writeStartDocument("ISO-8859-1", "1.0");
            w.writeStartElement("RockBandOriginalSong");

            w.writeStartElement("OriginalFileName");
            w.writeCharacters(fIn.getName());
            w.writeEndElement();

            w.writeStartElement("FileData");
            w.writeCharacters(b64FileData);
            w.writeEndElement();

            String                  directoryName;
            File                    fSongIni;
            String                  iniFileName;

            directoryName = fIn.getParent();
            iniFileName = directoryName + "\\song.ini";
            fSongIni = new File( iniFileName );

            if( fSongIni.exists() == true ) {
                java.util.Properties    props;
                java.io.FileInputStream fis;

                props = new java.util.Properties();

                try {
                    fis = new java.io.FileInputStream( fSongIni );
                    props.load( fis );
                    SerializeEmbeddedSongData( w, props );
                }
                catch( FileNotFoundException fnfex ) {
                    // nothing
                }
                catch( IOException ioex ) {
                    // nothing
                }
            }


            w.writeEndElement();
            w.writeEndDocument();
        }
        catch( XMLStreamException xsex ) {
            return null;
        }

        return sWriter.toString();
    }

    private static void SerializeEmbeddedSongData( XMLStreamWriter w, Properties props ) throws XMLStreamException {

        String  sValue;

        TryWritingProperty( w, props, "SongName", "name" );
        TryWritingProperty( w, props, "Artist", "artist" );
        TryWritingProperty( w, props, "Album", "album" );
        TryWritingProperty( w, props, "Year", "year" );
        TryWritingProperty( w, props, "Genre", "genre" );
        TryWritingProperty( w, props, "GuitarDifficulty", "diff_guitar" );
        TryWritingProperty( w, props, "BassDifficulty", "diff_bass" );
        TryWritingProperty( w, props, "DrumsDifficulty", "diff_drums" );
        TryWritingProperty( w, props, "VocalsDifficulty", "diff_vocals" );
        TryWritingProperty( w, props, "BandDifficulty", "diff_band" );

        sValue = props.getProperty( "icon" );
        if( sValue != null ) {
            w.writeStartElement("Location");
            w.writeCharacters(sValue);
            w.writeEndElement();
        }

        return;
    }

    private static void TryWritingProperty( XMLStreamWriter w, Properties props, String sXmlName, String sPropName ) throws XMLStreamException {

        String  sValue;

        sValue = props.getProperty( sPropName );
        if( sValue != null ) {
            w.writeStartElement(sXmlName);
            w.writeCharacters(sValue);
            w.writeEndElement();
        }

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
