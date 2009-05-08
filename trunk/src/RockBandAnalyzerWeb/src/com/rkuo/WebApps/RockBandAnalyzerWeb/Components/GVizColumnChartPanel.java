package com.rkuo.WebApps.RockBandAnalyzerWeb.Components;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.behavior.HeaderContributor;

import java.util.*;

/**
 * See http://code.google.com/apis/visualization
 */
public class GVizColumnChartPanel extends Panel {

    private final WebMarkupContainer visualization;

    private String  _category;
    private List<String> _lineLabels;
    private List<String> _columnLabels;
    private List<Long> _data;
    private Map<String,String>  _options;

    public GVizColumnChartPanel(final String id) {
        super(id);

        _category = "Category";

        _lineLabels = new ArrayList<String>();
        _columnLabels = new ArrayList<String>();
        _data = new ArrayList<Long>();
        _options = new HashMap<String,String>();

        add( new GVizHeaderContributor("columnchart") );
        add( new HeaderContributor(new HeaderContributorImpl()) );
        visualization = new WebMarkupContainer("visualization");
        visualization.setOutputMarkupId(true);
        add(visualization);
        return;
    }

    private class HeaderContributorImpl implements IHeaderContributor {
        public void renderHead(IHeaderResponse response) {
            StringBuffer js;

            js = new StringBuffer();
            js.append( "new WicketGViz('" + visualization.getMarkupId() + "');\n" );
            js.append( GetDataTable() );
            js.append( GetOptions( _options ) );
            js.append( "Wicket.visualizations['" + visualization.getMarkupId() + "'].drawColumnChart( data, options );" );
            response.renderOnDomReadyJavascript( js.toString() );
            return;
        }
    }

    public void setCategory( String category ) {
        _category = category;
    }

    public void setLineLabels( List<String> lineLabels ) {
        _lineLabels = lineLabels;
    }

    public void setColumnLabels( List<String> columnLabels ) {
        _columnLabels = columnLabels;
    }

    public void setData( List<Long> data ) {
        _data = data;
    }

    public void setOptions( Map<String,String> options ) {
        _options = options;
    }

    protected String GetDataTable() {

        StringBuilder   sb;

        sb = new StringBuilder();

        sb.append( "var data = new google.visualization.DataTable();" );
        sb.append( String.format( "data.addColumn('string', '%s');", _category ) );

        for( String lineLabel : _lineLabels ) {
             sb.append( String.format( "data.addColumn('number', '%s');", _category ) );
        }

        sb.append( String.format( "data.addRows(%d);", _columnLabels.size() ) );

        for( int x=0; x < _columnLabels.size(); x++ ) {
            sb.append( String.format( "data.setCell(%d, 0, '%s');", x, _columnLabels.get(x) ) );
        }

        for( int x=0; x < _lineLabels.size(); x++ ) {
            for( int y=0; y < _columnLabels.size(); y++ ) {
                sb.append( String.format( "data.setCell(%d, %d, %d);", y, x+1, _data.get(x * _columnLabels.size() + y) ) );
            }
        }

        return sb.toString();
    }

    protected static String GetOptions( Map<String,String> options ) {

        StringBuilder   sb;
        Set<Map.Entry<String, String>>  optionsSet;
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