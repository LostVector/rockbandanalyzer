package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.upload.FileUploadException;
import org.apache.wicket.util.upload.FileUploadBase;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.servlet.CustomMultipartServletWebRequest;
import org.apache.wicket.WicketRuntimeException;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.CacheException;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Collections;


public class RBBaseUploadForm extends Form<Void> {

    protected static Cache getCache() {
        Cache cache;

        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
        }
        catch( CacheException e ) {
            return null;
        }

        return cache;
    }

    String message;
    protected Label lblUploadMessage;
    protected FileUploadField fileUploadField;

    public RBBaseUploadForm(String name) {
        super(name);

        PropertyModel<String> messageModel;

        message = "Submit a file to the Rock Band Analyzer.";
        messageModel = new PropertyModel<String>(this, "message");

        // set this form to multipart mode (always needed for uploads!)
        setMultiPart(true);

        lblUploadMessage = new Label("lblUploadMessage", messageModel);
//            lblUploadMessage = new Label( "lblUploadMessage", "Submit a file to the Rock Band Analyzer." );
        fileUploadField = new FileUploadField("fileInput");

        // Set maximum size to 1024K
        add(lblUploadMessage);
        add(fileUploadField);

        setMaxSize(Bytes.kilobytes(1024));
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
        WebRequest webReq;

        webReq = (WebRequest)getRequest();
        isMultipart = true;

        if( (isMultipart == true) && (webReq.isAjax() == false) ) {
            // Change the request to a multipart web request so parameters are
            // parsed out correctly
            try {
                final WebRequest multipartWebRequest;

//                    multipartWebRequest = webReq.newMultipartWebRequest(getMaxSize());
                multipartWebRequest = new CustomMultipartServletWebRequest(webReq.getHttpServletRequest(), getMaxSize());
                getRequestCycle().setRequest(multipartWebRequest);
            }
            catch( WicketRuntimeException wre ) {
                if( wre.getCause() == null || !(wre.getCause() instanceof FileUploadException) ) {
                    throw wre;
                }

                FileUploadException e = (FileUploadException)wre.getCause();
                // Create model with exception and maximum size values
                final Map<String, Object> model = new HashMap<String, Object>();
                model.put("exception", e);
                model.put("maxSize", getMaxSize());

                if( e instanceof FileUploadBase.SizeLimitExceededException ) {
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
