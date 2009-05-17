package com.rkuo.RockBand;

import com.rkuo.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 12, 2009
 * Time: 12:21:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class JTidyDomScraper {

    public static List<String> ScrapeSongs(String scrapeUrl) {

        Document doc;
        ArrayList<String> midiTitles;

        midiTitles = new ArrayList<String>();

        doc = GetToXmlDocument(scrapeUrl);
        if( doc == null ) {
            return null;
        }

        NodeList    trList;

        trList = doc.getElementsByTagName( "tr" );
        for( int x=0; x < trList.getLength(); x++ ) {
            NodeList tdList;
            Element  trElement;

            trElement = (Element)trList.item(x);
            tdList = trElement.getElementsByTagName( "td" );
            for( int y=0; y < tdList.getLength(); y++ ) {
                NodeList aList;
                Element  tdElement;

                tdElement = (Element)tdList.item(y);
                aList = tdElement.getElementsByTagName( "a" );
                if( aList.getLength() > 0 ){
                    Element aElement;
                    String sHrefPrefix;
                    String sHref;
                    String midiTitle;

                    sHrefPrefix = "/songs/";

                    aElement = (Element)aList.item(0);
                    sHref = aElement.getAttribute("href");
                    if( sHref.startsWith(sHrefPrefix) == false ) {
                        continue;
                    }

                    midiTitle = sHref.substring(sHrefPrefix.length());
                    midiTitles.add(midiTitle);
                }
            }
        }

        return midiTitles;
    }

    public static SortedMap<String, String> ScrapeSongDetail(String scrapeUrl) {

        SortedMap<String, String> properties;
        Document doc;
        String sValue;

        properties = new TreeMap<String, String>();

        doc = GetToXmlDocument(scrapeUrl);
        if( doc == null ) {
            return null;
        }

        sValue = GetSongTitle(doc);
        properties.put("title", sValue);

        sValue = GetArtist(doc);
        properties.put("artist", sValue);

        sValue = GetAlbum(doc);
        properties.put("album", sValue);

        sValue = GetReleaseYear(doc);
        properties.put("release_year", sValue);

        sValue = GetGenre(doc);
        properties.put("genre", sValue);

        sValue = GetLocation(doc);
        properties.put("location", sValue);

        sValue = GetReleased(doc);
        properties.put("rb_release_date", sValue);

        sValue = GetGuitarDifficulty(doc);
        properties.put("guitar_difficulty", sValue);
        sValue = GetDrumsDifficulty(doc);
        properties.put("drums_difficulty", sValue);
        sValue = GetVocalsDifficulty(doc);
        properties.put("vocals_difficulty", sValue);
        sValue = GetBassDifficulty(doc);
        properties.put("bass_difficulty", sValue);
        sValue = GetBandDifficulty(doc);
        properties.put("band_difficulty", sValue);

        sValue = GetCover(doc);
        properties.put("cover", sValue);

        return properties;
    }

    public static String GetSongTitle(Document doc) {

        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "h1" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetArtist(Document doc) {

        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "h1" );
        classes.add( "" );
        names.add( "em" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();

        String byPrefix;
        String coverSuffix;

        byPrefix = "by ";
        coverSuffix = "(Cover version)";

        if( sValue.startsWith(byPrefix) == true ) {
            sValue = sValue.substring(byPrefix.length());
        }
        if( sValue.endsWith(coverSuffix) == true ) {
            sValue = sValue.substring(0, sValue.length() - coverSuffix.length());
            sValue = sValue.trim();
        }

        return sValue;
    }

    public static String GetCover(Document doc) {

        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "h1" );
        classes.add( "" );
        names.add( "em" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();

        String coverSuffix;

        coverSuffix = "(Cover version)";

        if( sValue.endsWith(coverSuffix) == true ) {
            return Boolean.TRUE.toString();
        }

        return Boolean.FALSE.toString();
    }

    public static String GetAlbum(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "songinfo" );
        names.add( "div" );
        classes.add( "left-songinfo" );
        names.add( "p" );
        classes.add( "questionnaire" );
        names.add( "span" );
        classes.add( "" );
        names.add( "strong" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetReleaseYear(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "songinfo" );
        names.add( "div" );
        classes.add( "left-songinfo" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "strong" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetGenre(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "songinfo" );
        names.add( "div" );
        classes.add( "left-songinfo" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "strong" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() < 2 ) {
            return "";
        }

        sValue = matches.get(1).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetLocation(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "songinfo" );
        names.add( "div" );
        classes.add( "right-songinfo" );
        names.add( "p" );
        classes.add( "questionnaire" );
        names.add( "span" );
        classes.add( "" );
        names.add( "strong" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetReleased(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "songinfo" );
        names.add( "div" );
        classes.add( "right-songinfo" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "strong" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return sValue;
    }

    public static String GetGuitarDifficulty(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "page-sidebar" );
        names.add( "p" );
        classes.add( "questionnaire" );
        names.add( "span" );
        classes.add( "" );
        names.add( "span" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return DifficultyToString(sValue);
    }

    public static String GetVocalsDifficulty(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "page-sidebar" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "span" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() == 0 ) {
            return "";
        }

        sValue = matches.get(0).getFirstChild().getNodeValue().trim();
        return DifficultyToString(sValue);
    }

    public static String GetDrumsDifficulty(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "page-sidebar" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "span" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() < 2 ) {
            return "";
        }

        sValue = matches.get(1).getFirstChild().getNodeValue().trim();
        return DifficultyToString(sValue);
    }

    public static String GetBassDifficulty(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "page-sidebar" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "span" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() < 3 ) {
            return "";
        }

        sValue = matches.get(2).getFirstChild().getNodeValue().trim();
        return DifficultyToString(sValue);
    }

    public static String GetBandDifficulty(Document doc) {
        ArrayList<String>    names;
        ArrayList<String>    classes;
        ArrayList<Element>   matches;
        String               sValue;

        names = new ArrayList<String>();
        classes = new ArrayList<String>();
        matches = new ArrayList<Element>();

        names.add( "div" );
        classes.add( "middle" );
        names.add( "div" );
        classes.add( "page-sidebar" );
        names.add( "p" );
        classes.add( "questionnaire bumpdown" );
        names.add( "span" );
        classes.add( "" );
        names.add( "span" );
        classes.add( "" );

        SimpleQuery( doc.getDocumentElement(), 0, names, classes, matches );
        if( matches.size() < 4 ) {
            return "";
        }

        sValue = matches.get(3).getFirstChild().getNodeValue().trim();
        return DifficultyToString(sValue);
    }

    public static String DifficultyToString(String desc) {
        if( desc.compareTo("No stars") == 0 ) {
            return "0";
        }

        if( desc.compareTo("One star") == 0 ) {
            return "1";
        }

        if( desc.compareTo("Two stars") == 0 ) {
            return "2";
        }

        if( desc.compareTo("Three stars") == 0 ) {
            return "3";
        }

        if( desc.compareTo("Four stars") == 0 ) {
            return "4";
        }

        if( desc.compareTo("Five stars") == 0 ) {
            return "5";
        }

        if( desc.compareTo("Devilish!") == 0 ) {
            return "6";
        }

        return null;
    }

    public static void SimpleQuery( Element root, Integer depth, List<String> names, List<String> classes, List<Element> outputMatches ) {

        NodeList    nodeList;
        ArrayList<Node>   aMatches;
        String  elName;
        String  elAttribute;

        elName = names.get( depth );
        elAttribute = classes.get( depth );
        aMatches = new ArrayList<Node>();

        if( depth == 0 ) {
            nodeList = root.getElementsByTagName( elName );
        }
        else {
            nodeList = root.getChildNodes();
        }

        for( int x=0; x < nodeList.getLength(); x++ ) {
            Node     nodeWalk;
            Element  elWalk;
            String   sAttribute;

            nodeWalk = nodeList.item(x);
            if( (nodeWalk instanceof Element) == false ) {
                continue;
            }

            elWalk = (Element)nodeWalk;

            if( elName.compareToIgnoreCase(elWalk.getTagName()) != 0 ) {
                continue;
            }

            if( elAttribute.length() > 0 ) {
                sAttribute = elWalk.getAttribute( "class" );
                if( elAttribute.compareToIgnoreCase(sAttribute) != 0 ) {
                    continue;
                }
            }

            aMatches.add( nodeList.item(x) );
        }

        for( int x=0; x < aMatches.size(); x++ ) {
            Element  elList;

            elList = (Element)aMatches.get(x);

            if( (depth + 1) == names.size() ) {
                outputMatches.add( elList );
            }
            else {
                SimpleQuery( elList, depth + 1, names, classes, outputMatches );
            }
        }

        return;
    }

    public static Document GetToXmlDocument(String targetUrl) {

        String response;
        Document doc;

        response = Misc.GetXml(targetUrl);
        if( response == null ) {
            return null;
        }

        try {
            Tidy tidy = new Tidy();
            tidy.setQuiet(true);
            tidy.setShowWarnings(false);
            tidy.setXHTML( true );
            tidy.setCharEncoding(3);
            doc = tidy.parseDOM(new ByteArrayInputStream(response.getBytes("UTF-8")), null);
        }
        catch( IOException ioex ) {
            return null;
        }

        return doc;
    }

    public static void DumpNodes(NodeList nodes) {

        for( int i = 0; i < nodes.getLength(); i++ ) {
            Element element;

            element = (Element)nodes.item(i);
            System.out.format(element.getFirstChild().getNodeValue() + "\n");
            System.out.format(element.getTextContent() + "\n");
        }

        return;
    }
}
