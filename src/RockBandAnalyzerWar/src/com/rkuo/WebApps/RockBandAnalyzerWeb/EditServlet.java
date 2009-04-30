package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 26, 2009
 * Time: 4:09:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map params;

        params = request.getParameterMap();
        params.get( "" );
        return;
    }
}
