<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://rockbandanalyzer.appspot.com/jsfcomponents" prefix="x" %>

<html>
<body>
<f:view>
    <x:loginHeader/>
    <x:navHeader/>
    <p>Rock Band Analyzer is hosted as a <a href="http://code.google.com/p/rockbandanalyzer">Google Code project</a>.</p>

    <p>
  You can obtain a read only copy of the current source
  <code>
      svn checkout http://rockbandanalyzer.googlecode.com/svn/trunk/ rockbandanalyzer-read-only
  </code>
   </p>
    <p>
  The main library for Rock Band Analyzer and the command line executable wrapping the library
  are both written in Java using the 1.6 JDK.
    </p>
    <p>
  The web frontend to the library was developed with Java Server Faces 1.1 as the UI framework.  In addition,
  other components of the web frontend rely on the Google App Engine for Java SDK, since this application is hosted
  and run on <a href="http://code.google.com/appengine/">Google App Engine</a>.
    </p>
    <p>
  The project was developed with <a href="http://www.jetbrains.com/idea/">IntelliJ IDEA</a>.
    </p>
    <x:Footer />
</f:view>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Rock Band Analyzer - Source code</title></head>
  <body>

  </body>
</html>