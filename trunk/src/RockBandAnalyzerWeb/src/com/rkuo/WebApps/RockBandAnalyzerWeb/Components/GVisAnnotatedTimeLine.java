package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.behavior.HeaderContributor;

/**
See http://code.google.com/apis/visualization
 */
public class GVisAnnotatedTimeLine extends Panel {

    private final WebMarkupContainer visualization;

    private final String url;

    public GVisAnnotatedTimeLine(final String id, final String url) {
        super(id);
        add(new GVisAnnotatedTimeLineHeaderContributor());
        add(new HeaderContributor(new IHeaderContributor() {
            public void renderHead(IHeaderResponse response) {
                response.renderOnDomReadyJavascript(getJSinit());
            }
        }));
        this.url = url;
        visualization = new WebMarkupContainer("visualization");
        visualization.setOutputMarkupId(true);
        add(visualization);
    }

    /**
     * Generates the JavaScript used to instantiate this Visualization as an JavaScript
     * class on the client side.
     *
     * @return The generated JavaScript
     */
    private String getJSinit() {
        StringBuffer js = new StringBuffer("new WicketVisualization('"
                + visualization.getMarkupId() + "', '" + url + "');\n");
        js.append(getJSDoDraw());
        return js.toString();
    }

    /**
     * Convenience method for generating a JavaScript call on this Visualization with
     * the given invocation.
     *
     * @param invocation The JavaScript call to invoke on this Visualization
     * @return The generated JavaScript.
     */
    private String getJSinvoke(String invocation) {
        return "Wicket.visualizations['" + visualization.getMarkupId() + "']."
                + invocation + ";";
    }

    private String getJSDoDraw() {
        return getJSinvoke("doDraw()");
    }
}