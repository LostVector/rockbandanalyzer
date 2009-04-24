<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://rockbandanalyzer.appspot.com/jsfcomponents" prefix="x" %>

<html>
  <head>
   <title>Browse songs</title>
  </head>
  <body>
     <f:view>
        <x:loginHeader />
        <x:navHeader />
        <table>
            <tr>
                <td>Difficulty</td>
                <td>Alphabetic by Song</td>
                <td>Alphabetic by Band</td>
                <td>Decade</td>
                <td>Location</td>
            </tr>
        </table>
<!--         
        <h:dataTable id="table1"value="#{shoppingCartBean.items}" var="item">
          <f:facet name="header">
            <h:outputText value="Your Shopping Cart" />
          </f:facet>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Item Description" />
            </f:facet>
            <h:outputText value="#{item.description}" />
          </h:column>
          <h:column>
            <f:facet name="header">
              <h:outputText value="Price" />
            </f:facet>
            <h:outputText value="#{item.price}"/>
          </h:column>
          <f:facet name="footer">
            <h:outputText value="Total: #{shoppingCartBean.total}" />
          </f:facet>
        </h:dataTable>
  -->
       <jsp:include page="footer.jsp" />  
     </f:view>
 </body>
</html>
