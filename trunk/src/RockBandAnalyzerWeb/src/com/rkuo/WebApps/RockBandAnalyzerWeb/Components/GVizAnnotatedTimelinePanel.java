
package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.*;

/**
 * See http://code.google.com/apis/visualization
 */
public class GVizAnnotatedTimelinePanel extends Panel {

    private final WebMarkupContainer visualization;

//    private String  _category;
    private List<String> _lineLabels;
    private List<Date> _columnLabels;
    private List<GVizDataPoint> _data;
    private Map<String,String> _options;
    private Integer             _width;
    private Integer             _height;

    private String              styleAttribute;

    public GVizAnnotatedTimelinePanel(final String id) {
        super(id);

//        _category = "Category";
        _width = 533;
        _height = 300;

        _lineLabels = new ArrayList<String>();
        _columnLabels = new ArrayList<Date>();
        _data = new ArrayList<GVizDataPoint>();
        _options = new HashMap<String,String>();

        add( new GVizHeaderContributor("annotatedtimeline") );
        add( new HeaderContributor(new HeaderContributorImpl()) );
        visualization = new WebMarkupContainer("visualization");
        visualization.setOutputMarkupId(true);

        PropertyModel<String> styleAttributeModel;

        styleAttributeModel = new PropertyModel<String>(this, "styleAttribute");
        visualization.add( new AttributeModifier("style", styleAttributeModel) );
       
        add(visualization);
        return;
    }

    public String getStyleAttribute() {
        return String.format( "width:%dpx; height:%dpx;", _width, _height );
    }

    public Integer getWidth() {
        return _width;
    }

    public Integer getHeight() {
        return _height;
    }

    public void setWidth(Integer width) {
        _width = width;
    }

    public void setHeight(Integer height) {
        _height = height;
    }

    private class HeaderContributorImpl implements IHeaderContributor {
        public void renderHead(IHeaderResponse response) {
            StringBuffer js;

            js = new StringBuffer();
            js.append( "new WicketGViz('" + visualization.getMarkupId() + "');\n" );
            js.append( GetDataTable() );
            js.append( GetOptions( _options ) );
            js.append( "Wicket.visualizations['" + visualization.getMarkupId() + "'].drawAnnotatedTimeline( data, options );" );
            response.renderOnDomReadyJavascript( js.toString() );
            return;
        }
    }

/*
    public void setCategory( String category ) {
        _category = category;
    }
*/

    public void setLineLabels( List<String> lineLabels ) {
        _lineLabels = lineLabels;
    }

    public void setColumnLabels( List<Date> columnLabels ) {
        _columnLabels = columnLabels;
    }

    public void setData( List<GVizDataPoint> data ) {
        _data = data;
    }

    public void setOptions( Map<String,String> options ) {
        _options = options;
    }

    protected String GetDataTable() {

        StringBuilder   sb;

        sb = new StringBuilder();

        sb.append( "var data = new google.visualization.DataTable();\n" );
        sb.append( String.format( "data.addColumn('date', 'Date');\n" ) );

        for( String lineLabel : _lineLabels ) {
             sb.append( String.format( "data.addColumn('number', '%s');\n", lineLabel ) );
            sb.append( String.format( "data.addColumn('string', '%s');\n", "title" + lineLabel ) );
            sb.append( String.format( "data.addColumn('string', '%s');\n", "text" + lineLabel ) );
        }

        sb.append( String.format( "data.addRows(%d);\n", _columnLabels.size() ) );

//        for( int x=0; x < _columnLabels.size(); x++ ) {
//            sb.append( String.format( "data.setValue(%d, 0, '%s');", x, _columnLabels.get(x) ) );
//        }

        for( int x=0; x < _columnLabels.size(); x++ ) {
            Date    d;

            d = _columnLabels.get(x);
            sb.append( String.format( "data.setValue(%d, 0, new Date(%d,%d,%d));\n",
                    x, d.getYear()+1900, d.getMonth(), d.getDate() ) );
            for( int y=0; y < _lineLabels.size(); y++ ) {
                GVizDataPoint   dp;

                dp = _data.get(y * _columnLabels.size() + x);
                
                sb.append( String.format( "data.setValue(%d, %d, %d);\n", x, (y*3)+1, dp.getValue() ) );

                if( dp.getAnnotation() != null ) {
                    sb.append( String.format( "data.setValue(%d, %d, '%s');\n", x, (y*3)+2, dp.getAnnotation() ) );
                }

                if( dp.getExtendedAnnotation() != null ) {
                    sb.append( String.format( "data.setValue(%d, %d, '%s');\n", x, (y*3)+3, dp.getExtendedAnnotation() ) );
                }
            }
        }

        return sb.toString();
    }

    protected static String GetOptions( Map<String,String> options ) {

        StringBuilder   sb;
        Set<Map.Entry<String, String>> optionsSet;
        boolean firstIteration;

        optionsSet = options.entrySet();
        firstIteration = true;

        sb = new StringBuilder();
        sb.append( "var options = {" );
        for( Map.Entry<String, String> option : optionsSet ) {
            if( firstIteration == true ) {
                firstIteration = false;
            }
            else {
                sb.append( "," );
            }

            sb.append( String.format("%s : %s", option.getKey(), option.getValue() ) );
        }

        sb.append( "};" );

        return sb.toString();
    }

}
