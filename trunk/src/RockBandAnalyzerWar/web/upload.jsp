<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://rockbandanalyzer.appspot.com/jsfcomponents" prefix="x" %>

<html>
<head>
    <title>enter your name page</title>
</head>
<body>
<f:view>
<x:loginHeader/>
<x:navHeader/>
</f:view>

<form method="post" ACTION="/api/upload" name="upload_form" ENCTYPE='multipart/form-data'>
    <input type="file" name="uploadfile">

    <p>
        <input type="submit" name="Submit" value="Submit">
        <input type="reset" name="Reset" value="Reset">
        <input type="hidden" name="action" value="upload">

</form>
</body>
</html>