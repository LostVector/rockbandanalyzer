package com.rkuo.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.GregorianCalendar;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 9, 2009
 * Time: 2:18:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Misc {

    public static String[] ProcessDirectory(File fIn, String fileExtension) {

        ArrayList<String> fileNames;
        File[] files;

        fileNames = new ArrayList<String>();

        files = fIn.listFiles();
        for( File f : files ) {
            if( f.isDirectory() == true ) {
                String[] subFileNames;

                subFileNames = ProcessDirectory(f, fileExtension);
                Collections.addAll(fileNames, subFileNames);
            }
            else {
                String fileName;

                fileName = f.getName();
                fileName = fileName.toLowerCase();

                if( fileName.endsWith(fileExtension) == true ) {
                    fileNames.add(f.getAbsolutePath());
                }
            }
        }

        return fileNames.toArray(new String[fileNames.size()]);
    }


    public static String PostXml(String serviceUrl, String requestXml) {

        HttpURLConnection conn;
        java.net.URL netUrl;
        String responseXml;

        try {
            netUrl = new java.net.URL(serviceUrl);
        }
        catch( MalformedURLException muex ) {
            return null;
        }

        try {
            conn = (HttpURLConnection)netUrl.openConnection();
        }
        catch( IOException ioex ) {
            return null;
        }

        try {
            conn.setRequestMethod("POST");
        }
        catch( ProtocolException pex ) {
            return null;
        }

        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);

        DataOutputStream out;

        conn.setRequestProperty("Content-type", "text/xml");

        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(requestXml);
            out.flush();
            out.close();
        }
        catch( IOException ioex ) {
            return null;
        }

        InputStream is;

        try {
            is = conn.getInputStream();
        }
        catch( IOException ioex ) {
            return null;
        }

        responseXml = Misc.ToString(is);

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

        return responseXml;
    }


    public static String GetXml(String serviceUrl) {

        HttpURLConnection conn;
        java.net.URL netUrl;
        String responseXml;

        try {
            netUrl = new java.net.URL(serviceUrl);
        }
        catch( MalformedURLException muex ) {
            return null;
        }

        try {
            conn = (HttpURLConnection)netUrl.openConnection();
        }
        catch( IOException ioex ) {
            return null;
        }

        try {
            conn.setRequestMethod("GET");
        }
        catch( ProtocolException pex ) {
            return null;
        }

        conn.setDoOutput(true);

        InputStream is;

        try {
            is = conn.getInputStream();
        }
        catch( IOException ioex ) {
            return null;
        }

        responseXml = ToString(is);
        return responseXml;
    }

    public static String ToString(InputStream is) {

        StringBuilder sb;

        sb = new StringBuilder();

        try {
            BufferedReader reader;
            String line;

            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            while( true ) {
                line = reader.readLine();
                if( line == null ) {
                    break;
                }

                sb.append(line + "\n");
            }
        }
        catch( IOException ioex ) {
            return null;
        }

        return sb.toString();
    }

    public static Document NewDocument() {

        DocumentBuilder db;
        DocumentBuilderFactory dbf;

        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException pcex ) {
            return null;
        }

        return db.newDocument();
    }

    public static Document ToDocument(String sXml) {

        Document doc;
        DocumentBuilder db;
        DocumentBuilderFactory dbf;

        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException pcex ) {
            return null;
        }

        try {
            doc = db.parse(new InputSource(new StringReader(sXml)));
        }
        catch( SAXException saxex ) {
            return null;
        }
        catch( IOException ioex ) {
            return null;
        }

        return doc;
    }

    public static Document ToDocument(InputStream is) {

        Document doc;
        DocumentBuilder db;
        DocumentBuilderFactory dbf;

        dbf = DocumentBuilderFactory.newInstance();

        try {
            db = dbf.newDocumentBuilder();
        }
        catch( ParserConfigurationException pcex ) {
            return null;
        }

        try {
            doc = db.parse(is);
        }
        catch( SAXException saxex ) {
            return null;
        }
        catch( IOException ioex ) {
            return null;
        }

        return doc;
    }

    public static String ToString(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch( TransformerException ex ) {
            ex.printStackTrace();
            return null;
        }
    }
/*
    public ToCalendar( Date date  ) {
        GregorianCalendar cal;
        int decade;

        cal = new GregorianCalendar();
        cal.setTime( date );
        
    }
 */

    public static void DocToFile(Document doc, String filename) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        }
        catch( TransformerConfigurationException e ) {
        }
        catch( TransformerException e ) {
        }
    }

}
