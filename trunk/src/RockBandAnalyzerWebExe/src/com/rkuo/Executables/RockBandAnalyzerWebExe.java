package com.rkuo.Executables;

import com.rkuo.util.CommandLineParser;
import com.rkuo.util.Misc;
import com.rkuo.util.Base64;
import com.rkuo.RockBand.CobraScraper;
import com.rkuo.RockBand.JTidyScraper;
import com.rkuo.RockBand.JTidyDomScraper;

import java.io.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class RockBandAnalyzerWebExe {

    protected static String _uploadServiceUrl = "http://rockbandanalyzer.appspot.com/api/upload";
    protected static String _songIdServiceUrl = "http://rockbandanalyzer.appspot.com/api/getSongIds";
    protected static String _reprocessServiceUrl = "http://rockbandanalyzer.appspot.com/api/reprocess";
    protected static String _cleanServiceUrl = "http://rockbandanalyzer.appspot.com/api/clean";
    protected static String _scrapeSongListServiceUrl = "http://rockbandanalyzer.appspot.com/api/scrapeSongList";
    protected static String _scrapeSongDetailServiceUrl = "http://rockbandanalyzer.appspot.com/api/scrapeSongDetail";

    public static void main(String[] args) {

        CommandLineParser clp;
        String[] fileNames;

        String uploadServiceUrl;
        String songIdServiceUrl;
        String reprocessServiceUrl;
        String cleanServiceUrl;
        String scrapeSongListServiceUrl;
        String scrapeSongDetailServiceUrl;

        String action;

        clp = new CommandLineParser();
        clp.Parse(args);

        if( clp.Contains("action") == false ) {
            return;
        }

        uploadServiceUrl = _uploadServiceUrl;
        songIdServiceUrl = _songIdServiceUrl;
        reprocessServiceUrl = _reprocessServiceUrl;
        cleanServiceUrl = _cleanServiceUrl;
        scrapeSongListServiceUrl = _scrapeSongListServiceUrl;
        scrapeSongDetailServiceUrl = _scrapeSongDetailServiceUrl;

        if( clp.Contains("local") == true ) {
            if( clp.GetBoolean("local") == true ) {
                uploadServiceUrl = "http://localhost:8080/api/upload";
                songIdServiceUrl = "http://localhost:8080/api/getSongIds";
                reprocessServiceUrl = "http://localhost:8080/api/reprocess";
                cleanServiceUrl = "http://localhost:8080/api/clean";
                scrapeSongListServiceUrl = "http://localhost:8080/api/scrapeSongList";
                scrapeSongDetailServiceUrl = "http://localhost:8080/api/scrapeSongDetail";
            }
        }

        action = clp.GetString("action");
        if( action.compareToIgnoreCase("upload") == 0 ) {

            File f;

            f = new File(clp.GetString("source"));

            if( f.isDirectory() == true ) {
                fileNames = Misc.ProcessDirectory(f, ".mid");
            }
            else {
                fileNames = new String[1];
                fileNames[0] = f.getAbsolutePath();
            }

            UploadFiles(fileNames, uploadServiceUrl);
        }
        else if( action.compareToIgnoreCase("reprocess") == 0 ) {
            List<Long> songIds;

            songIds = GetSongIds(songIdServiceUrl);
            if( songIds == null ) {
                return;
            }

            for( Long songId : songIds ) {
                ReprocessSong(songId, reprocessServiceUrl);
            }
        }
        else if( action.compareToIgnoreCase("clean") == 0 ) {
//            CleanDatastore(cleanServiceUrl, "RockBandSong");
//            CleanDatastore(cleanServiceUrl, "RockBandSongEmbedded");
//            CleanDatastore(cleanServiceUrl, "RockBandSongGenerated");
//            CleanDatastore(cleanServiceUrl, "RockBandSongRaw");
//            for( int x=0; x < 100; x++ ) {
//                CleanDatastore(cleanServiceUrl, "RockBandDotComSong");
//            }
        }
        else if( action.compareToIgnoreCase("scrape") == 0 ) {
//            JTidyScraper.ScrapeSongs( "http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=" );
            JTidyDomScraper.ScrapeSongDetail( "http://www.rockband.com/songs/limelight" );

//            List<String>    songs;
//            songs = ScrapeSongList( scrapeSongListServiceUrl );
//            ScrapeSongDetails( scrapeSongDetailServiceUrl, songs );
//            ScrapeSongs("http://www.rockband.com/music/getSearchResultsTable_Ajax?sort_on=songs.NAME&sort_order=asc&search_terms=");
//            ScrapeSongLeaderboard( );
        }

        return;
    }

    private static List<Long> GetSongIds(String serviceUrl) {

        String responseXml;
        List<Long> songIds;

        responseXml = Misc.GetXml(serviceUrl);
        if( responseXml == null ) {
            return null;
        }

        songIds = getIdsFromXml(responseXml);
        if( songIds == null ) {
            return null;
        }

        return songIds;
    }

    protected static List<Long> getIdsFromXml(String sXml) {

        ArrayList<Long> songIds;
        Document doc;
        NodeList nodeList;

        doc = Misc.ToDocument(sXml);
        songIds = new ArrayList<Long>();

        nodeList = doc.getElementsByTagName("songId");
        for( int x = 0; x < nodeList.getLength(); x++ ) {
            Element e;
            String sValue;

            e = (Element)nodeList.item(x);
            sValue = e.getFirstChild().getNodeValue();
            songIds.add(Long.parseLong(sValue));
        }

        return songIds;
    }

    private static void UploadFiles(String[] fileNames, String uploadUrl) {
        long succeeded, failed;

        succeeded = 0;
        failed = 0;

        for( String fileName2 : fileNames ) {
            File f2;

            f2 = new File(fileName2);

            try {
                String songXml;
                String responseXml;

                songXml = SerializeSong(f2);
                responseXml = Misc.PostXml(uploadUrl, songXml);
                if( responseXml == null ) {
                    System.out.format("UploadFile failed: %s\n", fileName2);
                    failed++;
                }
                else {
                    System.out.format("UploadFile succeeded: %s\n", fileName2);
                    succeeded++;
                }
            }
            catch( Exception ex ) {
                System.out.format("Exception while processing %s\n", fileName2);
                failed++;
            }
        }

        System.out.format("Succeeded: %d, Failed: %d\n", succeeded, failed);
        return;
    }

    private static String SerializeSong(File fIn) {

        XMLOutputFactory xof;
        javax.xml.stream.XMLStreamWriter w;
        StringWriter sWriter;
        String b64FileData;

        FileInputStream fInStream;
        byte fileBytes[];

        try {
            fInStream = new FileInputStream(fIn);
        }
        catch( FileNotFoundException fnfex ) {
            return null;
        }

        fileBytes = new byte[(int)fIn.length()];

        try {
            fInStream.read(fileBytes);
            fInStream.close();
        }
        catch( IOException ioex ) {
            return null;
        }

        b64FileData = Base64.encodeBytes(fileBytes);

        xof = XMLOutputFactory.newInstance();
        sWriter = new StringWriter();

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

            String directoryName;
            File fSongIni;
            String iniFileName;

            directoryName = fIn.getParent();
            iniFileName = directoryName + "\\song.ini";
            fSongIni = new File(iniFileName);

            if( fSongIni.exists() == true ) {
                java.util.Properties props;
                java.io.FileInputStream fis;

                props = new java.util.Properties();

                try {
                    fis = new java.io.FileInputStream(fSongIni);
                    props.load(fis);
                    SerializeEmbeddedSongData(w, props);
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

    private static void SerializeEmbeddedSongData(XMLStreamWriter w, Properties props) throws XMLStreamException {

        String sValue;

        TryWritingProperty(w, props, "SongName", "name");
        TryWritingProperty(w, props, "Artist", "artist");
        TryWritingProperty(w, props, "Album", "album");
        TryWritingProperty(w, props, "Year", "year");
        TryWritingProperty(w, props, "Genre", "genre");
        TryWritingProperty(w, props, "GuitarDifficulty", "diff_guitar");
        TryWritingProperty(w, props, "BassDifficulty", "diff_bass");
        TryWritingProperty(w, props, "DrumsDifficulty", "diff_drums");
        TryWritingProperty(w, props, "VocalsDifficulty", "diff_vocals");
        TryWritingProperty(w, props, "BandDifficulty", "diff_band");

        sValue = props.getProperty("icon");
        if( sValue != null ) {
            w.writeStartElement("Location");
            w.writeCharacters(sValue);
            w.writeEndElement();
        }

        return;
    }

    private static void TryWritingProperty(XMLStreamWriter w, Properties props, String sXmlName, String sPropName) throws XMLStreamException {

        String sValue;

        sValue = props.getProperty(sPropName);
        if( sValue != null ) {
            w.writeStartElement(sXmlName);
            w.writeCharacters(sValue);
            w.writeEndElement();
        }

        return;
    }

    private static void ReprocessSong(Long songId, String serviceUrl) {

        String requestXml;
        String responseXml;

        requestXml = GenerateInvocationRequest("reprocess", "songId", songId.toString());
        responseXml = Misc.PostXml(serviceUrl, requestXml);

        System.out.format("Processed songId %d.\n", songId);
        return;
    }

    private static void CleanDatastore(String serviceUrl, String entityName) {

        String requestXml;
        String responseXml;

        requestXml = GenerateInvocationRequest("clean", "entityName", entityName);
        responseXml = Misc.PostXml(serviceUrl, requestXml);

        System.out.format("Cleaned %s.\n", entityName);
        return;
    }

    protected static String GenerateInvocationRequest(String action, String name, String value) {
        Map<String, String> params;
        params = new TreeMap<String, String>();
        params.put(name, value);
        return GenerateInvocationRequest(action, params);
    }

    protected static String GenerateInvocationRequest(String action, Map<String, String> params) {

        XMLOutputFactory xof;
        javax.xml.stream.XMLStreamWriter w;
        StringWriter sWriter;

        xof = XMLOutputFactory.newInstance();
        sWriter = new StringWriter();

        try {
            w = xof.createXMLStreamWriter(sWriter);
        }
        catch( XMLStreamException xsex ) {
            return null;
        }

        try {
            w.writeStartDocument("ISO-8859-1", "1.0");
            w.writeStartElement("RockBandInvocation");

            w.writeStartElement("action");
            w.writeCharacters(action);
            w.writeEndElement();

            for( Map.Entry<String, String> entry : params.entrySet() ) {

                w.writeStartElement(entry.getKey());
                w.writeCharacters(entry.getValue());
                w.writeEndElement();
            }

            w.writeEndElement();
            w.writeEndDocument();
        }
        catch( XMLStreamException xsex ) {
            return null;
        }

        return sWriter.toString();
    }

    protected static void ScrapeSongList( String serviceUrl ) {

        String requestXml;
        String responseXml;

        requestXml = GenerateInvocationRequest("clean", "entityName", "ScrapeSongList");
        responseXml = Misc.PostXml(serviceUrl, requestXml);

        System.out.format( "Scraped song list.\n" );
        return;
    }

    protected static void ScrapeSongDetail( String songUrl ) {

        CobraScraper.ScrapeSongDetail( songUrl );
        return;
    }
/*
    protected void ScrapeSongLeaderboard( RockBandInstrument rbi, RockBandPlatform rbp, RockBand) {

        int x;

        // flip pages
        for( x=1; x <= 4000; x++ ) {

        }

//        response = Misc.PostXml( "http://www.rockband.com/leaderboards/retrieve-table", )
        return;
    }
 */    
}
