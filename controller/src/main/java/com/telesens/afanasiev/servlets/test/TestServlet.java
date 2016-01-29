package com.telesens.afanasiev.servlets.test;

import com.telesens.afanasiev.DaoFactory;
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
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class TestServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        out.println("<h3>response from test servlet</h3>");

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Collection<Map> maps = mapDAO.getRange(0, 10);

//        for (Map map : maps) {
//            out.println(String.format("<div>Map: '%s' - %s</div>", map.getName(), map.getDescribe()));
//        }
        req.setAttribute("maps", maps);

        req.getRequestDispatcher("/jsp/test/test.jsp").forward(req, resp);
    }

    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}
 }
