package com.rkuo.WebApps.RockBandAnalyzerWeb;

import java.io.*;
import java.util.logging.Logger;
import javax.servlet.http.*;
import javax.servlet.ServletException;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.Part;
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;

public class UploadServlet extends HttpServlet {

    private static final int MaxUploadBytes = 1024 * 1024; // 1 MB

    private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

    /*
       public void doPost(HttpServletRequest req, HttpServletResponse resp)
                   throws IOException {
           UserService userService = UserServiceFactory.getUserService();
           User user = userService.getCurrentUser();

           String content = req.getParameter("content");
           if (content == null) {
               content = "(No greeting)";
           }
           if (user != null) {
               log.info("Greeting posted by user " + user.getNickname() + ": " + content);
           } else {
               log.info("Greeting posted anonymously: " + content);
           }

           resp.sendRedirect( "/faces/upload_complete.jsp" );
           return;
       }
    */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

// custom beans from my project, not defined here
//        PersonRegistrationForm personRegistrationForm = null;
//        PortraitImage portraitImage = null;

        Part currentPart = null;
        FilePart currentFilePart = null;

//        personRegistrationForm = (PersonRegistrationForm) request.getSession().getAttribute(DsnSessionKeyConstantsIF.KEY_PERSON_FORM);
//        portraitImage = personRegistrationForm.getPortraitImage();

        try {
            MultipartParser parser = new MultipartParser(request, MaxUploadBytes);
            while( true ) {
                currentPart = parser.readNextPart();
                if( currentPart == null ) {
                    break;
                }

                if( currentPart.isFile() == true ) {
                    ByteArrayOutputStream   outputStream;

                    outputStream = new ByteArrayOutputStream();

                    currentFilePart = (FilePart)currentPart;
                    currentFilePart.writeTo( outputStream );

// portraitImage is just a bean for encapsulating image data, not defined in this posting
//                    portraitImage.setContentType(currFilePart.getContentType());
//                    portraitImage.setImageAsByteArray(outputStream.toByteArray());
//                    portraitImage.setOriginalFileName(currFilePart.getFileName());

                    RockBandAnalyzerParams  rbap;
                    DrumsBaselineData       dbd;
                    boolean                 br;

                    response.setContentType("text/plain");

                    rbap = new RockBandAnalyzerParams();
                    dbd = RockBandAnalyzer.AnalyzeStream( response.getWriter(), new ByteArrayInputStream(outputStream.toByteArray()), rbap );
                    if( dbd == null ) {
                        response.getWriter().format( "AnalyzeStream failed. The file you submitted may not be a valid Rock Band MIDI file.\n" );
                        break;
                    }

                    UserService userService;
                    User user;
                    RockBandSong song;

                    userService = UserServiceFactory.getUserService();
                    user = userService.getCurrentUser();

                    song = new RockBandSong( user, dbd.MidiTitle, currentFilePart.getFileName(), outputStream.toByteArray() );

                    song.setOriginalFileName( currentFilePart.getFileName() );
                    song.setFile( outputStream.toByteArray() );

                    br = DataAccess.SongExists( song.getMD5() );
                    if( br == false ) {
                        DataAccess.SongWrite( song );
                        break;
                    }

                    response.getWriter().format( "The song you just submitted already exists in our database.\n" );
//                    log.info( String.format("Filename: %s, Size: %d, Compressed size: %d", currentFilePart.getFileName(), outputStream.size(), baOut.size()) );
                    break;
                }
            }
        }
        catch( IOException ioe ) {
// noop
        }

//        response.sendRedirect( "/faces/upload_complete.jsp" );
        return;
    } // doPost


}