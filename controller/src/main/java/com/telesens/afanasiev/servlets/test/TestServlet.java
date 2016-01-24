package com.telesens.afanasiev.servlets.test;

import com.telesens.afanasiev.DaoManager;
import com.telesens.afanasiev.Map;
import com.telesens.afanasiev.MapDAO;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created by oleg on 1/16/16.
 */
public class TestServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.println("<h3>response from test servlet</h3>");

        DaoManager daoManager = DaoManager.getInstance();
        MapDAO mapDAO = daoManager.getMapDAO();
        Collection<Map> maps = mapDAO.getAll();

//        for (Map map : maps) {
//            out.println(String.format("<div>Map: '%s' - %s</div>", map.getName(), map.getDescribe()));
//        }
        req.setAttribute("maps", maps);

        req.getRequestDispatcher("/jsp/test/test.jsp").forward(req, resp);
    }

    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}
 }
