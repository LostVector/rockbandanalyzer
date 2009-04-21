package com.rkuo.WebApps.RockBandAnalyzerWeb;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.User;

import java.util.Date;
import javax.faces.component.UIComponentBase;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 19, 2009
 * Time: 12:23:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginHeaderComponent extends UIComponentBase {

    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer;
        HttpServletRequest request;

        writer = context.getResponseWriter();
        request = (HttpServletRequest) context.getExternalContext().getRequest();

        writer.startElement( "table", this );
        writer.writeAttribute( "width", "960px", null );
        writer.startElement( "tr", this );

        writer.startElement( "td", this );
        writer.writeText( "Rock Band Analyzer", null );
        writer.endElement( "td" );

        writer.startElement( "td", this );
        writer.writeAttribute( "align", "right", null );
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        if( user != null ) {
            writer.writeText( "Hello, " + user.getNickname() + "! | ", null );
            writer.startElement( "a", null );
            writer.writeAttribute( "href", userService.createLogoutURL(request.getRequestURL().toString()), null );
            writer.writeText( "Sign out", null );
            writer.endElement( "a" );
        }
        else {
            writer.writeText( "Hello! | ", null );
            writer.startElement( "a", null );
            writer.writeAttribute( "href", userService.createLoginURL(request.getRequestURL().toString()), null );
            writer.writeText( "Sign in", null );
            writer.endElement( "a" );
        }
        writer.endElement( "td" );

        writer.endElement( "tr" );
        writer.endElement( "table" );

/*
        String hellomsg = (String) getAttributes().get("hellomsg");

        writer.startElement("h3", this);
        if (hellomsg != null)
            writer.writeText(hellomsg, "hellomsg");
        else
            writer.writeText("Hello from a custom JSF UI Component!", null);
        writer.endElement("h3");
        writer.startElement("p", this);
        writer.writeText(" Today is: " + new Date(), null);
        writer.endElement("p");
 */
        return;
    }

    public String getFamily() {
        return "HelloFamily";
    }
}
