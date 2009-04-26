<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://rockbandanalyzer.appspot.com/jsfcomponents" prefix="x" %>

<html>
<body>
<f:view>
    <x:loginHeader/>
    <x:navHeader/>
    Song Title <h:inputText></h:inputText><br />
    Artist<h:inputText></h:inputText><br />
    Album<h:inputText></h:inputText><br />
    Release Date<h:inputText></h:inputText><br />
    Location
    <h:selectOneRadio id="location" layout="spread" value="1">
        <f:selectItem itemValue="1" />
        <f:selectItem itemValue="2" />
        <f:selectItem itemValue="3" />
      </h:selectOneRadio><br />
    Guitar Difficulty
    <h:selectOneRadio id="buttons" layout="spread" value="Button 1">
        <f:selectItem itemValue="Button 1" />
        <f:selectItem itemValue="Button 2" />
        <f:selectItem itemValue="Button 3" />
        <f:selectItem itemValue="Button 4" />
        <f:selectItem itemValue="Button 5" />
        <f:selectItem itemValue="Button 6" />
        <f:selectItem itemValue="Button 7" />
      </h:selectOneRadio><br />
    Bass Difficulty<br />
    Drums Difficulty<br />
    Vocals Difficulty<br />
    Band Difficulty<br />
    <x:Footer />
</f:view>
</body>
</html>
