package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;

public class SongTableTag extends UIComponentTag {

    // Associate the renderer and component type.
    public String getComponentType() {
        return "com.rkuo.WebApps.RockBandAnalyzerWeb.SongTable";
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
    }

    public void release() {
        super.release();
    }
}
