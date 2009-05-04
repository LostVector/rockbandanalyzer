package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.WicketAjaxReference;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WicketEventReference;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 3, 2009
 * Time: 11:26:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class GVisAnnotatedTimeLineHeaderContributor extends HeaderContributor {

// google api url - no key required for charts
private static final String GOOGLE_API_URL = "http://www.google.com/jsapi";

// here the visualization package is hardcoded to make the code cleaner and clearer
protected static final String GOOGLE_LOAD_VISUALISATION = "google.load(\"visualization\", \"1\", {packages: ['linechart']});";

// static reference to the custom javascript file that handles google visualization calls
private static final ResourceReference WICKET_VISUALISATION_JS = new JavascriptResourceReference(
    GVisAnnotatedTimeLine.class, "wicket-url-visualization.js");

public GVisAnnotatedTimeLineHeaderContributor() {
  super(new IHeaderContributor() {

    public void renderHead(IHeaderResponse response) {

      response.renderJavascriptReference(GOOGLE_API_URL);

      response.renderJavascript(GOOGLE_LOAD_VISUALISATION,
          GVisAnnotatedTimeLineHeaderContributor.class.getName());

      response
          .renderJavascriptReference(WicketEventReference.INSTANCE);
      response
          .renderJavascriptReference(WicketAjaxReference.INSTANCE);
      response.renderJavascriptReference(WICKET_VISUALISATION_JS);
    }
  });
}
}