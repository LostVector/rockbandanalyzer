package com.rkuo.RockBand;

import com.rkuo.util.Misc;

import java.io.StringReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.lobobrowser.html.parser.HtmlParser;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 12, 2009
 * Time: 12:21:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CobraScraper {


    public static List<String> ScrapeSongs(String scrapeUrl) {

        String response;
        StringReader sReader;
        ArrayList<String> midiTitles;

        midiTitles = new ArrayList<String>();

        response = Misc.GetXml(scrapeUrl);

        sReader = new StringReader(response);

//        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
        // In this case we will use a standard XML document
        // as opposed to Cobra's HTML DOM implementation.

        Document doc;
        HtmlParser parser;

        doc = Misc.NewDocument();
        if( doc == null ) {
            return null;
        }

        // Here is where we use Cobra's HTML parser.
        UserAgentContext uacontext = new SimpleUserAgentContext();
        parser = new HtmlParser(uacontext, doc);

        try {
            parser.parse(sReader);
        }
        catch( IOException ioex ) {
            return null;
        }
        catch( SAXException saxex ) {
            return null;
        }

        // Now we use XPath to locate "a" elements that are
        // descendents of any "html" element.
        XPath xpath;
        NodeList nodeList;

        xpath = XPathFactory.newInstance().newXPath();

        try {
            nodeList = (NodeList)xpath.evaluate("html//tr/td/a", doc, XPathConstants.NODESET);
//            nodeList = (NodeList) xpath.evaluate("html//a", doc, XPathConstants.NODESET);
        }
        catch( XPathExpressionException xpex ) {
            return null;
        }

        int length = nodeList.getLength();
        for( int i = 0; i < length; i++ ) {
            Element element;
            String  sHrefPrefix;
            String  sHref;
            String  midiTitle;

            sHrefPrefix = "/songs/";

            element = (Element)nodeList.item(i);
            sHref = element.getAttribute("href");
            if( sHref.startsWith(sHrefPrefix) == false ) {
                continue;
            }

            midiTitle = sHref.substring( sHrefPrefix.length() );
            midiTitles.add( midiTitle );
        }

        return midiTitles;
    }

    public static SortedMap<String,String> ScrapeSongDetail(String scrapeUrl) {

        SortedMap<String,String>    properties;
        Document doc;
        String  sValue;

        properties = new TreeMap<String,String>();

        doc = GetToXmlDocument( scrapeUrl );

        sValue = GetSongTitle( doc );
        properties.put( "title", sValue );

        sValue = GetArtist( doc );
        properties.put( "artist", sValue );

        sValue = GetAlbum( doc );
        properties.put( "album", sValue );

        sValue = GetReleaseYear( doc );
        properties.put( "release_year", sValue );

        sValue = GetGenre( doc );
        properties.put( "genre", sValue );

        sValue = GetLocation( doc );
        properties.put( "location", sValue );

        sValue = GetReleased( doc );
        properties.put( "rb_release_date", sValue );

        sValue = GetGuitarDifficulty( doc );
        properties.put( "guitar_difficulty", sValue );
        sValue = GetDrumsDifficulty( doc );
        properties.put( "drums_difficulty", sValue );
        sValue = GetVocalsDifficulty( doc );
        properties.put( "vocals_difficulty", sValue );
        sValue = GetBassDifficulty( doc );
        properties.put( "bass_difficulty", sValue );
        sValue = GetBandDifficulty( doc );
        properties.put( "band_difficulty", sValue );

        sValue = GetCover( doc );
        properties.put( "cover", sValue );

        return properties;
    }

    public static String GetSongTitle( Document doc ) {
        String sValue = XPathQuery( doc, "html//div[@class='middle']/h1" );
        sValue = sValue.trim();
        return sValue;
    }

    public static String GetArtist( Document doc ) {
        String  byPrefix;
        String  coverSuffix;
        String  sValue;

        byPrefix = "by ";
        coverSuffix = "(Cover version)";

        sValue = XPathQuery( doc, "html//div[@class='middle']/h1/em" );
        if( sValue.startsWith(byPrefix) == true ) {
            sValue = sValue.substring( byPrefix.length() );
        }
        if( sValue.endsWith(coverSuffix) == true ) {
            sValue = sValue.substring(0, sValue.length() - coverSuffix.length() );
            sValue = sValue.trim();
        }

        return sValue; 
    }

    public static String GetCover( Document doc ) {
        String  coverSuffix;
        String  sValue;

        coverSuffix = "(Cover version)";

        sValue = XPathQuery( doc, "html//div[@class='middle']/h1/em" );
        if( sValue.endsWith(coverSuffix) == true ) {
            return Boolean.TRUE.toString();
        }

        return Boolean.FALSE.toString();
    }

    public static String GetAlbum( Document doc ) {
        return XPathQuery( doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire']/span/strong" );
    }

    public static String GetReleaseYear( Document doc ) {
        return XPathQuery( doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire bumpdown']/span/strong" );
    }

    public static String GetGenre( Document doc ) {
        return XPathQuery( doc, 1, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire bumpdown']/span/strong" );
    }

    public static String GetLocation( Document doc ) {
        return XPathQuery( doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='right-songinfo']/p[@class='questionnaire']/span/strong" );
    }

    public static String GetReleased( Document doc ) {
        return XPathQuery( doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='right-songinfo']/p[@class='questionnaire bumpdown']/span/strong" );
    }

    public static String GetGuitarDifficulty( Document doc ) {
        String  sValue;

        sValue = XPathQueryAttribute( doc, 0, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire']/span" );
        return ClassNameToString( sValue );
    }

    public static String GetVocalsDifficulty( Document doc ) {
        String  sValue;

        sValue = XPathQueryAttribute( doc, 0, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span" );
        return ClassNameToString( sValue );
    }

    public static String GetDrumsDifficulty( Document doc ) {
        String  sValue;

        sValue = XPathQueryAttribute( doc, 1, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span" );
        return ClassNameToString( sValue );
    }

    public static String GetBassDifficulty( Document doc ) {
        String  sValue;

        sValue = XPathQueryAttribute( doc, 2, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span" );
        return ClassNameToString( sValue );
    }

    public static String GetBandDifficulty( Document doc ) {
        String  sValue;

        sValue = XPathQueryAttribute( doc, 3, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span" );
        return ClassNameToString( sValue );
    }

    public static String ClassNameToString( String className ) {
        if( className.compareTo("dots-zero") == 0 ) {
            return "0";
        }

        if( className.compareTo("dots-one") == 0 ) {
            return "1";
        }

        if( className.compareTo("dots-two") == 0 ) {
            return "2";
        }

        if( className.compareTo("dots-three") == 0 ) {
            return "3";
        }

        if( className.compareTo("dots-four") == 0 ) {
            return "4";
        }

        if( className.compareTo("dots-five") == 0 ) {
            return "5";
        }

        if( className.compareTo("dots-devils") == 0 ) {
            return "6";
        }

        return null;
    }

    public static String XPathQuery( Document doc, String query ) {
        return XPathQuery( doc, 0, query );
    }

    public static String XPathQuery( Document doc, Integer nodeIndex, String query ) {

        XPath xpath;
        NodeList nodes;
        Element el;

        xpath = XPathFactory.newInstance().newXPath();

        try {
            nodes = (NodeList)xpath.evaluate(query, doc, XPathConstants.NODESET);
        }
        catch( XPathExpressionException xpex ) {
            return null;
        }

        el = (Element)nodes.item(nodeIndex);
        return el.getFirstChild().getNodeValue();
    }

    public static String XPathQueryAttribute( Document doc, Integer nodeIndex, String attributeName, String query ) {

        XPath xpath;
        NodeList nodes;
        Element el;

        xpath = XPathFactory.newInstance().newXPath();

        try {
            nodes = (NodeList)xpath.evaluate(query, doc, XPathConstants.NODESET);
        }
        catch( XPathExpressionException xpex ) {
            return null;
        }

        el = (Element)nodes.item(nodeIndex);
        return el.getAttribute( attributeName );
    }

    public static Document GetToXmlDocument( String targetUrl ) {

        String response;
        StringReader sReader;
        Document doc;
        HtmlParser parser;

        response = Misc.GetXml( targetUrl );
        if( response == null ) {
            return null;
        }

        sReader = new StringReader(response);

//        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
        // In this case we will use a standard XML document
        // as opposed to Cobra's HTML DOM implementation.

        doc = Misc.NewDocument();
        if( doc == null ) {
            return null;
        }

        // Here is where we use Cobra's HTML parser.
        UserAgentContext uacontext = new SimpleUserAgentContext();
        parser = new HtmlParser(uacontext, doc);

        try {
            parser.parse(sReader);
        }
        catch( IOException ioex ) {
            return null;
        }
        catch( SAXException saxex ) {
            return null;
        }

        return doc;
    }

    public static void DumpNodes( NodeList nodes ) {

        for( int i = 0; i < nodes.getLength(); i++ ) {
            Element element;

            element = (Element)nodes.item(i);
            System.out.format( element.getFirstChild().getNodeValue() + "\n" );
            System.out.format( element.getTextContent() + "\n" );
        }

        return;
    }
}
