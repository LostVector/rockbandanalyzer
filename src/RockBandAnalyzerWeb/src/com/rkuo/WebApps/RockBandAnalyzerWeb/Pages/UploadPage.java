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

import com.rkuo.RockBand.RockBandAnalyzerParams;
import com.rkuo.RockBand.RockBandAnalyzer;
import com.rkuo.RockBand.Simulators.DrumsBaselineData;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
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
            final FileUpload upload;
            InputStream fileIn;

            RockBandAnalyzerParams rbap;
            DrumsBaselineData dbd;
            UserService userService;
            User user;
            RockBandSong song;

            StringWriter    sWriter;
            PrintWriter printWriter;

            ByteArrayOutputStream   baOut;

            boolean                 br;

            sWriter = new StringWriter();
            printWriter = new PrintWriter( sWriter );
            baOut = new ByteArrayOutputStream();
            rbap = new RockBandAnalyzerParams();

            userService = UserServiceFactory.getUserService();
            user = userService.getCurrentUser();

            upload = fileUploadField.getFileUpload();
            if( upload == null ) {
                return;
            }

            try {
                fileIn = upload.getInputStream();
            }
            catch( IOException ioex ) {
                return;
            }

            dbd = RockBandAnalyzer.AnalyzeStream( printWriter, fileIn, rbap );
            if( dbd == null ) {
                printWriter.format( "AnalyzeStream failed. The file you submitted may not be a valid Rock Band MIDI file.\n" );
                message = sWriter.toString();
                return;
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
                message = sWriter.toString();
                return;
            }

            song = new RockBandSong( user, dbd.SongTitle, upload.getClientFileName(), baOut.toByteArray() );

            br = DataAccess.SongExists( song.getMD5() );
            if( br == false ) {
                DataAccess.SongWrite( song );
                printWriter.format( "Saving the song to our database.\n" );
                message = sWriter.toString();
                return;
            }

            printWriter.format( "The song you just submitted already exists in our database.\n" );
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
    }
}
