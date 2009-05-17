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

import com.rkuo.RockBand.ExeHelper.RockBandAnalyzerParams;
import com.rkuo.RockBand.ExeHelper.RockBandAnalyzer;
import com.rkuo.RockBand.Primitives.DrumsFullAnalysis;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSongRaw;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.RockBandSong;
import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.RBSongUploadForm;
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

    protected RBSongUploadForm formUpload;
    /**
     * Constructor.
     *
     * @param parameters Page parameters
     */
    public UploadPage(final PageParameters parameters) {

        formUpload = new RBSongUploadForm("formUpload");
        add( formUpload );

        return;
    }

}
