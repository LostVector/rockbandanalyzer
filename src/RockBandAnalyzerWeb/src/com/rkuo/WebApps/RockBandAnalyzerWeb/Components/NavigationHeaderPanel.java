package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class NavigationHeaderPanel extends Panel {

    public NavigationHeaderPanel(String id) {
        super(id);
        return;
    }
}