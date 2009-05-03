package com.rkuo.WebApps.RockBandAnalyzerWeb.Pages;

import org.apache.wicket.markup.html.WebPage;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.LoginHeaderPanel;
import com.rkuo.WebApps.RockBandAnalyzerWeb.Components.NavigationHeaderPanel;

public abstract class BasePage extends WebPage {
	public BasePage() {
//		add(new BookmarkablePageLink("page1", Page1.class));
//		add(new BookmarkablePageLink("page2", Page2.class));
//		add(new Label("footer", "This is in the footer"));
        add( new LoginHeaderPanel("lhpHeader") );
        add( new NavigationHeaderPanel("nhpHeader") );
        add( new FooterPanel("fpFooter") );
        return;
	}
}