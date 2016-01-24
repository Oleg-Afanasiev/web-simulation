package com.telesens.afanasiev.servlets;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoManager;
import com.telesens.afanasiev.User;
import com.telesens.afanasiev.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by oleg on 1/16/16.
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
        DaoManager daoManager = DaoManager.getInstance();

        processRequest(req, resp);
        doGetInPersistentCtx(req, resp);

        // close db connection
        daoManager.closeConnection();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // open db connection
        DaoManager daoManager = DaoManager.getInstance();

        doPostInPersistentCtx(req, resp);

        // close db connection
        daoManager.closeConnection();
    }

    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}

    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {}

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String userId = userIdFromCookie(req);
        if (userId != null) {
            User user = getUserFromDB(Long.parseLong(userId));
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("activeUser", user);
            }
        }
    }

    private String userIdFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId"))
                return cookies[i].getValue();
        }

        return null;
    }

    private User getUserFromDB(long id) {
        DaoManager daoManager = DaoManager.getInstance();
        UserDAO userDAO = daoManager.getUserDAO();
        User user = null;
        try {
            user = userDAO.getById(id);
        } catch (DaoException exc) {
            logger.debug("User with specified ID wasn't found. id = " + id);
        }

        return user;
    }
}
