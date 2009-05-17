package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExportPage extends BasePage {

    ExternalLink    elExport;

    public ExportPage() {
        elExport = new ExternalLink( "elExport", "/api/export", "Download CSV" );

        add( elExport );
        return;
    }
}
