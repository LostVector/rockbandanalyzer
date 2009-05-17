package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

public class GVizDataPoint {

    private Long value;
    private String annotation;
    private String extendedAnnotation;

    public GVizDataPoint() {
        return;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getExtendedAnnotation() {
        return extendedAnnotation;
    }

    public void setExtendedAnnotation(String extendedAnnotation) {
        this.extendedAnnotation = extendedAnnotation;
    }
}
