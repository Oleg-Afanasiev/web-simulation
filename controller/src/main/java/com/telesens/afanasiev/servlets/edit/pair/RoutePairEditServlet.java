package com.telesens.afanasiev.servlets.edit.pair;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet(name="RoutePairEditServlet", urlPatterns = "/pair/edit", loadOnStartup = 0)
public class RoutePairEditServlet extends PersistServlet {

    private String msgError;
    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        RoutePair<Station> pair = getPair(req);
        req.setAttribute("pair", pair);
        req.getRequestDispatcher("/jsp/pair/routePairEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (delSuccess(req)) {
            resp.sendRedirect("/pair/list");
        } else {
            RoutePair<Station> pair = getPair(req);
            req.setAttribute("pairs", pair);
            req.setAttribute("msgError", msgError);
            req.getRequestDispatcher("/jsp/pair/routePairEdit.jsp").forward(req, resp);
        }
    }

    private boolean delSuccess(HttpServletRequest req) {
        long forwId;
        long backId;

        try {
            forwId = Long.parseLong(req.getParameter("forwid"));
            backId = Long.parseLong(req.getParameter("backid"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read params for 'RoutePair'");
            msgError = "Can't read params for 'RoutePair'";
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();

        try {
            routePairDAO.delete(forwId, backId);
        } catch(DaoException exc) {
            logger.debug("Can't delete pair with specified Ids. route_forw_id = " + forwId + "route_back_id = " + backId);
            msgError = "Can't delete this pair";
            return false;
        }

        return true;
    }

    private RoutePair<Station> getPair(HttpServletRequest req) {
        long forwId;
        long backId;

        try {
            forwId = Long.parseLong(req.getParameter("forwid"));
            backId = Long.parseLong(req.getParameter("backid"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read params for 'RoutePair");
            return null;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
        RoutePair<Station> routePair;

        try {
            routePair = routePairDAO.getById(forwId, backId);
        } catch(DaoException exc) {
            logger.debug("Can't get pair with specified Ids. route_forw_id = " + forwId + "route_back_id = " + backId);
            return null;
        }

        return routePair;
    }
}
