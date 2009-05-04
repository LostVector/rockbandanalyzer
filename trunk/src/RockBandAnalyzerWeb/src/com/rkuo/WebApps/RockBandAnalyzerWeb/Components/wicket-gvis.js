Wicket.visualizations = {};

function WicketVisualization(id, url) {
    Wicket.visualizations[id] = this;
    //wicket component id
    this.id = id;
    // url based query based on the chart rendering, this is standart for all the chart types
    this.query = new google.visualization.Query(url);
    // a bunch of hardcoded chart options
    this.options = { width : 850, height : 400, lineSize : 4, pointSize : 6 };

    this.doDraw = function() {
        var self = this;
        var handleQueryResponse = function(response) {
            if( response.isError() ) {
                alert('Error in query: ' + response.getMessage() + ' ' + response.getDetailedMessage());
                return;
            }
            var data = response.getDataTable();
            var container = document.getElementById(self.id);
            // if you plan to support other chart types, you need to change the following
            new google.visualization.LineChart(container).draw(data, self.options);
        }
        // Send the query with a callback function.
        this.query.send(handleQueryResponse);
    }
}