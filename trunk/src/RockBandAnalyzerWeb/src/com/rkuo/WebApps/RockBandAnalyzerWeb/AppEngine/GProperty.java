package com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine;

import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class GProperty {

    @PrimaryKey
    private String id;

    @Persistent
    private String value;

    public GProperty() {
        value = "";
        return;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
}
