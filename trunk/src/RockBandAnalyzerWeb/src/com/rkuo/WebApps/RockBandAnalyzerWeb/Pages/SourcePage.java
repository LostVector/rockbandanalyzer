package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 29, 2009
 * Time: 10:57:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class SourcePage extends BasePage {

    public SourcePage() {
        add(new Label("lblHelloWorld", new Model("Hello, World")));
        return;
    }
}
