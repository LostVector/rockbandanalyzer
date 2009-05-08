package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.servlet.CustomMultipartServletWebRequest;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileUploadException;
import org.apache.wicket.util.upload.FileUploadBase;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.basic.Label;

import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.Simulators.DrumsBaselineAnalysis;
import com.rkuo.RockBand.Simulators.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongEmbedded;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongGenerated;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 30, 2009
 * Time: 9:51:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class UploadPage extends BasePage {

    /**
     * Constructor.
     *
     * @param parameters Page parameters
     */
    public UploadPage(final PageParameters parameters) {

        // Add simple upload form, which is hooked up to its feedback panel by
        // virtue of that panel being nested in the form.
        final FileUploadForm simpleUploadForm;

        simpleUploadForm = new FileUploadForm("simpleUpload");
        add( simpleUploadForm );

        return;
    }

    /**
     * Form for uploads.
     */
    private class FileUploadForm extends Form<Void> {

        private Label           lblUploadMessage;
        private FileUploadField fileUploadField;

        private String          message;

        /**
         * Construct.
         *
         * @param name Component name
         */
        public FileUploadForm(String name) {
            super(name);

            PropertyModel<String> messageModel;

            message = "Submit a file to the Rock Band Analyzer.";
            messageModel = new PropertyModel<String>(this, "message");

            // set this form to multipart mode (always needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            fileUploadField = new FileUploadField("fileInput");
//            fileUploadField.
            lblUploadMessage = new Label( "lblUploadMessage", messageModel );

            // Set maximum size to 1024K
            add( fileUploadField );
            add( lblUploadMessage );

            setMaxSize( Bytes.kilobytes(1024) );
            return;
        }

        /**
         * @see org.apache.wicket.markup.html.form.Form#onSubmit()
         */
        @Override
        protected void onSubmit() {
            RockBandSongRaw raw;
            RockBandSongEmbedded    embedded;
            RockBandSongGenerated   generated;

            StringWriter    sWriter;
            PrintWriter printWriter;
            RockBandAnalyzerParams  rbap;
            DrumsFullAnalysis dfa;
            boolean                 br;

            sWriter = new StringWriter();
            printWriter = new PrintWriter( sWriter );
            rbap = new RockBandAnalyzerParams();
            raw = getRawSong( printWriter );
            if( raw == null ) {
                return;
            }

            dfa = RockBandAnalyzer.AnalyzeStream(null, new ByteArrayInputStream(raw.getFile()), rbap);
            if( dfa == null ) {
                return;
            }

            embedded = new RockBandSongEmbedded();
            embedded.setMidiTitle( dfa.dba.MidiTitle );

            generated = new RockBandSongGenerated();
            generated.setMicroseconds( dfa.dba.Microseconds );

            br = DataAccess.TryWritingSong( raw, embedded, generated );
            if( br == false ) {
                printWriter.format("%s already exists in the database.", raw.getOriginalFileName());
                message = sWriter.toString();
                return;
            }

            printWriter.format("%s has been added to the database.", raw.getOriginalFileName());
            message = sWriter.toString();
            return;
        }

        /**
         * Handles multi-part processing of the submitted data.
         * <p/>
         * WARNING
         * <p/>
         * If this method is overridden it can break {@link FileUploadField}s on this form
         *
         * @return false if form is multipart and upload failed
         */
        @Override
        protected boolean handleMultiPart() {

            boolean isMultipart;
            WebRequest  webReq;

            webReq = (WebRequest)getRequest();
            isMultipart = true;

            if( (isMultipart == true) && (webReq.isAjax() == false) ) {
                // Change the request to a multipart web request so parameters are
                // parsed out correctly
                try {
                    final WebRequest multipartWebRequest;

//                    multipartWebRequest = webReq.newMultipartWebRequest(getMaxSize());
                    multipartWebRequest = new CustomMultipartServletWebRequest( webReq.getHttpServletRequest(), getMaxSize() );
                    getRequestCycle().setRequest(multipartWebRequest);
                }
                catch (WicketRuntimeException wre) {
                    if (wre.getCause() == null || !(wre.getCause() instanceof FileUploadException)) {
                        throw wre;
                    }

                    FileUploadException e = (FileUploadException) wre.getCause();
                    // Create model with exception and maximum size values
                    final Map<String, Object> model = new HashMap<String, Object>();
                    model.put("exception", e);
                    model.put("maxSize", getMaxSize());

                    if (e instanceof FileUploadBase.SizeLimitExceededException) {
                        // Resource key should be <form-id>.uploadTooLarge to
                        // override default message
                        final String defaultValue = "Upload must be less than " + getMaxSize();
//                        String msg = getString(getId() + "." + UPLOAD_TOO_LARGE_RESOURCE_KEY, Model.of(model), defaultValue);
                        String msg = getString(getId() + "." + "uploadTooLarge", Model.of(model), defaultValue);

                        error(msg);
                    }
                    else {
                        // Resource key should be <form-id>.uploadFailed to override
                        // default message
                        final String defaultValue = "Upload failed: " + e.getLocalizedMessage();
//                        String msg = getString(getId() + "." + UPLOAD_FAILED_RESOURCE_KEY, Model.of(model), defaultValue);
                        String msg = getString(getId() + "." + "uploadFailed", Model.of(model), defaultValue);
                        error(msg);

//                        log.warn(msg, e);
                    }

                    // don't process the form if there is a FileUploadException
                    return false;
                }
                catch( FileUploadException fuex ) {
                    return false;
                }
            }
            return true;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @param message the message to set
         */
        public void setMessage(String message) {
            this.message = message;
        }

        protected RockBandSongRaw getRawSong( PrintWriter printWriter ) {

            RockBandSongRaw raw;
            final FileUpload upload;
            InputStream fileIn;

            UserService userService;
            User user;

            ByteArrayOutputStream   baOut;

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
                    baOut.write( c );
                }

                baOut.close();
            }
            catch( IOException ioex ) {
                printWriter.format( "IOException while reading submitted file.\n" );
                return null;
            }

            raw = new RockBandSongRaw( user, new Date(), upload.getClientFileName(), baOut.toByteArray() );

            return raw;
        }
    }
}
