package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WicketEventReference;
import org.apache.wicket.ajax.WicketAjaxReference;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 4, 2009
 * Time: 10:57:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class GVizHeaderContributorImpl implements IHeaderContributor {

    private String  _vizPackage;

    // google api url - no key required for charts
    private static final String GOOGLE_API_URL = "http://www.google.com/jsapi";

    // here the visualization package is hardcoded to make the code cleaner and clearer
    protected static final String GOOGLE_LOAD_VISUALIZATION = "google.load(\"visualization\", \"1\", {packages: ['linechart']});";

    public GVizHeaderContributorImpl( String vizPackage ) {
        _vizPackage = vizPackage;
        return;
    }

    public void renderHead(IHeaderResponse response) {

        String  loadVizJS;

        response.renderJavascriptReference(GOOGLE_API_URL);

        loadVizJS = String.format( "google.load(\"visualization\", \"1\", {packages: ['%s']});", _vizPackage );
        response.renderJavascript( loadVizJS, _vizPackage );

        response.renderJavascriptReference(WicketEventReference.INSTANCE);
        response.renderJavascriptReference(WicketAjaxReference.INSTANCE);
        response.renderJavascriptReference("/js/wicket-gviz.js");
//                response.renderJavascriptReference(WICKET_VISUALIZATION_JS);
    }
}
