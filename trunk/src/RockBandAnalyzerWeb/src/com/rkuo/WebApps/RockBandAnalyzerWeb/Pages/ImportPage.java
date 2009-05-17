package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.PageParameters;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.RBCSVUploadForm;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 11, 2009
 * Time: 12:53:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImportPage extends BasePage {

    protected RBCSVUploadForm formImport;

    public ImportPage(final PageParameters parameters) {

        formImport = new RBCSVUploadForm("formImport");
        add( formImport );
        return;
    }

}
