package com.rkuo.WebApps.RockBandAnalyzerWeb.SecureServices;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.util.Base64;
import com.rkuo.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;

public class ReprocessServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Document    doc;
        Long        songId;
        RockBandSongRaw rawSong;
        RockBandSong song;
        RockBandAnalyzerParams rbap;
        DrumsFullAnalysis dfa;

        rbap = new RockBandAnalyzerParams();
//        song = new RockBandSong();

        doc = Misc.ToDocument( request.getInputStream() );
        if( doc == null ) {
            return;
        }

        songId = getSongId( doc );
        if( songId == null ) {
            return;
        }

        rawSong = DataAccess.GetRawSongById( songId );
        song = DataAccess.GetSongById( songId );

        dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(rawSong.getFile()), rbap);
        if( dfa == null ) {
            return;
        }

        song.setId( rawSong.getId() );
        DataAccess.ProcessSong( song, dfa );
        DataAccess.WriteSong( System.currentTimeMillis(), song );
        DataAccess.SetLastUpdated();
        return;
    } // doPost

    protected RockBandSongRaw getRawSong( Document doc ) {

        RockBandSongRaw rawSong;
        byte[] fileBytes;

        NodeList nodeList;
        Element e;

        String originalFileName;
        String b64FileData;

        nodeList = doc.getElementsByTagName("OriginalFileName");
        e = (Element)nodeList.item(0);
        originalFileName = e.getFirstChild().getNodeValue();

        nodeList = doc.getElementsByTagName("FileData");
        e = (Element)nodeList.item(0);
        b64FileData = e.getFirstChild().getNodeValue();

        try {
         fileBytes = Base64.decode(b64FileData);
        }
        catch( IOException ioex ) {
            return null;
        }

        UserService userService;
        User user;

        userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();

        rawSong = new RockBandSongRaw(user, new Date(), originalFileName, fileBytes);

        return rawSong;
    }

    protected Long getSongId( Document doc ) {

        Long    songId;

        NodeList nodeList;
        Element e;

        String sValue;

        nodeList = doc.getElementsByTagName("songId");
        e = (Element)nodeList.item(0);
        sValue = e.getFirstChild().getNodeValue();
        songId = Long.parseLong( sValue );

        return songId;
    }
}
