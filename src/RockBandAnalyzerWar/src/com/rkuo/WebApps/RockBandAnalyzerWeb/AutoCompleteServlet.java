package com.rkuo.WebApps.RockBandAnalyzerWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: rkuo
 * Date: Apr 24, 2009
 * Time: 11:59:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoCompleteServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
        String targetId = request.getParameter("id");
        if (targetId != null) targetId = targetId.trim().toLowerCase();
        Iterator it = employees.keySet().iterator();
        while (it.hasNext()) {
            EmployeeBean e = (EmployeeBean) employees.get(
                    (String) it.next());
            if ((targetId != null) &&
                    (e.getFirstName().toLowerCase().startsWith(targetId) ||
                            e.getLastName().toLowerCase().startsWith(targetId))
                    && !targetId.equals("")) {
                sb.append("<employee>");
                sb.append("<id>" + e.getId() + "</id>");
                sb.append("<firstName>" + e.getFirstName() +
                        "</firstName>");
                sb.append("<lastName>" + e.getLastName() +
                        "</lastName>");
                sb.append("</employee>");
                namesAdded = true;
            }
        }
        if (namesAdded) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<employees>" +
                    sb.toString() + "</employees>");
        }
        else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
 */
        return;
    }
}
