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

public class LoginHeaderPanel extends Panel {

    public LoginHeaderPanel(String id) {
        super(id);

        UserService userService;
        User user;

        String realUri;
        String greeting;
        String loginURL;
        String loginCaption;

        userService = UserServiceFactory.getUserService();
        user = userService.getCurrentUser();

        ServletWebRequest servletWebRequest;
        HttpServletRequest req;

        servletWebRequest = (ServletWebRequest) getRequest();
        req = servletWebRequest.getHttpServletRequest();

        realUri = req.getRequestURL().toString();

        if (user != null) {
            greeting = "Hello, " + user.getNickname();
            loginURL = userService.createLogoutURL(realUri);
            loginCaption = "Logout";
        }
        else {
            greeting = "Hello!";
            loginURL = userService.createLoginURL(realUri);
            loginCaption = "Login";
        }

        add( new Label("lblUsername", greeting) );
        add( new ExternalLink("lnkLogin", loginURL, loginCaption) );
        return;
    }
}