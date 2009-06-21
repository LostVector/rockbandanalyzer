package com.rkuo.WebApps.RockBandAnalyzerWeb.SecureServices;

import com.rkuo.WebApps.RockBandAnalyzerWeb.AppEngine.DataAccess;
import com.rkuo.util.Misc;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CleanServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Document doc;
        String  entityName;

        doc = Misc.ToDocument( req.getInputStream() );
        if( doc == null ) {
            return;
        }

        entityName = getEntityName( doc );
        if( entityName == null ) {
            return;
        }

        DataAccess.DropTable( entityName );
        return;
    }

    protected String getEntityName( Document doc ) {

        String    entityName;

        NodeList nodeList;
        Element e;

        nodeList = doc.getElementsByTagName("entityName");
        e = (Element)nodeList.item(0);
        entityName = e.getFirstChild().getNodeValue();

        return entityName;
    }

}
