package com.telesens.afanasiev.servlets.net;

import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oleg on 1/19/16.
 */
@WebServlet(name="NetListServlet", urlPatterns = "/net/list", loadOnStartup = 0)
public class NetListServlet extends PersistServlet {
    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/jsp/net/netList.jsp").forward(req, resp);
    }

    @Override
    protected  void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }
}
