package com.telesens.afanasiev.servlets;

import com.telesens.afanasiev.DaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
abstract public class PersistServlet extends HttpServlet {
    protected static final Logger logger = LoggerFactory.getLogger(PersistServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // open db connection
        DaoFactory daoFactory = DaoFactory.getInstance();

        //processRequest(req, resp);
        doGetInPersistentCtx(req, resp);

        // close db connection
        daoFactory.closeConnection();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // open db connection
        DaoFactory daoFactory = DaoFactory.getInstance();

        doPostInPersistentCtx(req, resp);

        // close db connection
        daoFactory.closeConnection();
    }

    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}

    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}
}
