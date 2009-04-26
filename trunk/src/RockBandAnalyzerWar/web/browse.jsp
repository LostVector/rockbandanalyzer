<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://rockbandanalyzer.appspot.com/jsfcomponents" prefix="x" %>

<html>
<head>
    <title>Browse songs</title>
    <link rel="STYLESHEET" type="text/css" href="/main.css" />    
</head>
<body>
<f:view>
    <x:loginHeader/>
    <x:navHeader/>
    <x:SongTable/>

<ul id="tabs">
<li class="current"><a href="/tor/1960277">General information</a></li>
<li><a href="/det/1960277">Details</a></li>
<li><a href="/com/1960277">Comments (0) + Thanks (1)</a></li>
<li><a href="/rep/1960277">Report a problem</a></li>
</ul>
    
    <!--
    <h:dataTable id="table1" value="#{shoppingCartBean.items}" var="item">
        <f:facet name="header">
            <h:outputText value="Your Shopping Cart"/>
        </f:facet>
        <h:column>
            <f:facet name="header">
                <h:outputText value="Item Description"/>
            </f:facet>
            <h:outputText value="#{item.description}"/>
        </h:column>
        <h:column>
            <f:facet name="header">
                <h:outputText value="Price"/>
            </f:facet>
            <h:outputText value="#{item.price}"/>
        </h:column>
        <f:facet name="footer">
            <h:outputText value="Total: #{shoppingCartBean.total}"/>
        </f:facet>
    </h:dataTable>
    -->
    <x:Footer/>
</f:view>
</body>
</html>
