package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.behavior.HeaderContributor;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: May 3, 2009
 * Time: 11:26:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class GVizHeaderContributor extends HeaderContributor {

    public GVizHeaderContributor( String vizPackage ) {
        super( new GVizHeaderContributorImpl(vizPackage) );
    }
}