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
public class JTidyScraper {

    public static List<String> ScrapeSongs(String scrapeUrl) {

        Document doc;
        ArrayList<String> midiTitles;

        midiTitles = new ArrayList<String>();

        doc = GetToXmlDocument(scrapeUrl);
        if( doc == null ) {
            return null;
        }

//        String sDocument = Misc.ToString( doc );
//        System.out.format( sDocument );
//        Misc.DocToFile(doc, "c:\\downloads\\scrape.xml");

        // Now we use XPath to locate "a" elements that are
        // descendents of any "html" element.

        NodeList nodeList;

        try {
            XPath xpath;

            xpath = XPathFactory.newInstance().newXPath();
            nodeList = (NodeList)xpath.evaluate("html//tr/td/a", doc, XPathConstants.NODESET);
        }
        catch( XPathExpressionException xpex ) {
            return null;
        }

        int length = nodeList.getLength();
        for( int i = 0; i < length; i++ ) {
            Element element;
            String sHrefPrefix;
            String sHref;
            String midiTitle;

            sHrefPrefix = "/songs/";

            element = (Element)nodeList.item(i);
            sHref = element.getAttribute("href");
            if( sHref.startsWith(sHrefPrefix) == false ) {
                continue;
            }

            midiTitle = sHref.substring(sHrefPrefix.length());
            midiTitles.add(midiTitle);
        }

        return midiTitles;
    }

    public static SortedMap<String, String> ScrapeSongDetail(String scrapeUrl) {

        SortedMap<String, String> properties;
        Document doc;
        String sValue;

        properties = new TreeMap<String, String>();

        doc = GetToXmlDocument(scrapeUrl);

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
        String sValue = XPathQuery(doc, "html//div[@class='middle']/h1");
        sValue = sValue.trim();
        return sValue;
    }

    public static String GetArtist(Document doc) {
        String byPrefix;
        String coverSuffix;
        String sValue;

        byPrefix = "by ";
        coverSuffix = "(Cover version)";

        sValue = XPathQuery(doc, "html//div[@class='middle']/h1/em");
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
        String coverSuffix;
        String sValue;

        coverSuffix = "(Cover version)";

        sValue = XPathQuery(doc, "html//div[@class='middle']/h1/em");
        if( sValue.endsWith(coverSuffix) == true ) {
            return Boolean.TRUE.toString();
        }

        return Boolean.FALSE.toString();
    }

    public static String GetAlbum(Document doc) {
        return XPathQuery(doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire']/span/strong");
    }

    public static String GetReleaseYear(Document doc) {
        return XPathQuery(doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire bumpdown']/span/strong");
    }

    public static String GetGenre(Document doc) {
        return XPathQuery(doc, 1, "html//div[@class='middle']/div[@class='songinfo']/div[@class='left-songinfo']/p[@class='questionnaire bumpdown']/span/strong");
    }

    public static String GetLocation(Document doc) {
        return XPathQuery(doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='right-songinfo']/p[@class='questionnaire']/span/strong");
    }

    public static String GetReleased(Document doc) {
        return XPathQuery(doc, "html//div[@class='middle']/div[@class='songinfo']/div[@class='right-songinfo']/p[@class='questionnaire bumpdown']/span/strong");
    }

    public static String GetGuitarDifficulty(Document doc) {
        String sValue;

        sValue = XPathQueryAttribute(doc, 0, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire']/span");
        return ClassNameToString(sValue);
    }

    public static String GetVocalsDifficulty(Document doc) {
        String sValue;

        sValue = XPathQueryAttribute(doc, 0, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span");
        return ClassNameToString(sValue);
    }

    public static String GetDrumsDifficulty(Document doc) {
        String sValue;

        sValue = XPathQueryAttribute(doc, 1, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span");
        return ClassNameToString(sValue);
    }

    public static String GetBassDifficulty(Document doc) {
        String sValue;

        sValue = XPathQueryAttribute(doc, 2, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span");
        return ClassNameToString(sValue);
    }

    public static String GetBandDifficulty(Document doc) {
        String sValue;

        sValue = XPathQueryAttribute(doc, 3, "class", "html//div[@class='middle']/div[@class='page-sidebar']/p[@class='questionnaire bumpdown']/span");
        return ClassNameToString(sValue);
    }

    public static String ClassNameToString(String className) {
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

    public static String XPathQuery(Document doc, String query) {
        return XPathQuery(doc, 0, query);
    }

    public static String XPathQuery(Document doc, Integer nodeIndex, String query) {

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

    public static String XPathQueryAttribute(Document doc, Integer nodeIndex, String attributeName, String query) {

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
        return el.getAttribute(attributeName);
    }

    public static Document GetToXmlDocument(String targetUrl) {

        String response;
        Document doc;

        response = Misc.GetXml(targetUrl);

        // Here is where we use Cobra's HTML parser.
        // create an instance of HtmlCleaner

        try {
            Tidy tidy = new Tidy();
            tidy.setQuiet(true);
            tidy.setShowWarnings(false);
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
