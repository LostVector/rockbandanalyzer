package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.faces.application.Application;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

public class LoginHeaderTag extends UIComponentTag {
    // Declare a bean property for the hellomsg attribute.
    public String hellomsg = null;

    // Associate the renderer and component type.
    public String getComponentType() {
        return "com.rkuo.WebApps.RockBandAnalyzerWeb.LoginHeader";
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        // set hellomsg
        if (hellomsg != null) {
            if (isValueReference(hellomsg)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(hellomsg);
                component.setValueBinding("hellomsg", vb);
            }
            else
                component.getAttributes().put("hellomsg", hellomsg);
        }
    }

    public void release() {
        super.release();
        hellomsg = null;
    }


    public void setHellomsg(String hellomsg) {
        this.hellomsg = hellomsg;
    }
}