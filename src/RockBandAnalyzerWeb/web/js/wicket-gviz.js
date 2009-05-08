Wicket.visualizations = {};

function WicketGViz(id) {

    Wicket.visualizations[id] = this;

    //wicket component id
    this.id = id;

    // url based query based on the chart rendering, this is standard for all the chart types
//    this.query = new google.visualization.Query(url);

    // a bunch of hardcoded chart options
//    this.options = { width : 850, height : 400, lineSize : 4, pointSize : 6 };

    this.drawLineChart = function( data, options ) {
        var self = this;
        var container = document.getElementById(self.id);
        var chart = new google.visualization.LineChart(container);
        chart.draw( data, options );
        return;
    }

    this.drawColumnChart = function( data, options ) {
        var self = this;
        var container = document.getElementById(self.id);
        var chart = new google.visualization.ColumnChart(container);
        chart.draw( data, options );
        return;
    }
}