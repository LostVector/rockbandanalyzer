package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.*;
import java.util.Date;

import org.apache.wicket.markup.html.form.upload.FileUpload;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 11, 2009
 * Time: 1:02:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class RBSongUploadForm extends RBBaseUploadForm {

    public RBSongUploadForm(String name) {
        super(name);
        return;
    }

    protected RockBandSongRaw getRawSong(PrintWriter printWriter) {

        RockBandSongRaw raw;
        FileUpload upload;
        InputStream fileIn;

        UserService userService;
        User user;

        ByteArrayOutputStream baOut;

        baOut = new ByteArrayOutputStream();

        userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();

        upload = fileUploadField.getFileUpload();
        if( upload == null ) {
            return null;
        }

        try {
            fileIn = upload.getInputStream();
        }
        catch( IOException ioex ) {
            return null;
        }

        try {
            fileIn.reset();
            for( int c = fileIn.read(); c != -1; c = fileIn.read() ) {
                baOut.write(c);
            }

            baOut.close();
        }
        catch( IOException ioex ) {
            printWriter.format("IOException while reading submitted file.\n");
            return null;
        }

        raw = new RockBandSongRaw(user, new Date(), upload.getClientFileName(), baOut.toByteArray());

        return raw;
    }

    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    protected void onSubmit() {
        RockBandSongRaw raw;
        RockBandSong song;

        StringWriter sWriter;
        PrintWriter printWriter;
        RockBandAnalyzerParams rbap;
        DrumsFullAnalysis dfa;
        boolean br;

        sWriter = new StringWriter();
        printWriter = new PrintWriter(sWriter);
        rbap = new RockBandAnalyzerParams();
        raw = getRawSong(printWriter);
        if( raw == null ) {
            return;
        }

        dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(raw.getFile()), rbap);
        if( dfa == null ) {
            return;
        }

        song = new RockBandSong();
        song.getGenerated().setMidiTitle(dfa.dba.MidiTitle);
        song.getGenerated().setMicroseconds(dfa.dba.Microseconds);

        br = DataAccess.TryWritingSong(raw, song);
        if( br == false ) {
            printWriter.format("%s already exists in the database.", raw.getOriginalFileName());
            message = sWriter.toString();
            return;
        }

        printWriter.format("%s has been added to the database.", raw.getOriginalFileName());
        message = sWriter.toString();
        return;
    }

}
