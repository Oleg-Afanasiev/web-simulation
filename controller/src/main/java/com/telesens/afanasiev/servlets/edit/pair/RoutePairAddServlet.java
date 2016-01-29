package com.telesens.afanasiev.servlets.edit.pair;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RoutePairImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet(name="RoutePairAddServlet", urlPatterns = "/pair/add", loadOnStartup = 0)
public class RoutePairAddServlet extends PersistServlet {

    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Collection<Route<Station>> routes = getRoutes();
        req.setAttribute("routes", routes);
        req.getRequestDispatcher("/jsp/pair/routePairAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (addSuccess(req)) {
            resp.sendRedirect("/pair/list");
        } else {
            rewriteParams(req);
            Collection<Route<Station>> routes = getRoutes();
            req.setAttribute("routes", routes);
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);
            req.getRequestDispatcher("/jsp/pair/routePairAdd.jsp").forward(req, resp);
        }
    }

    private Collection<Route<Station>> getRoutes() {
        Collection<Route<Station>> routes;

        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();

        try {
            routes = routeDAO.getRangeNotPaired(0, 1000);
        } catch(DaoException exc) {
            logger.debug("Can't get routes from db");
            return new ArrayList<>();
        }

        return routes;
    }

    private boolean addSuccess(HttpServletRequest req) {
        long routeForwId;
        long routeBackId;

        try {
            routeForwId = Long.parseLong(req.getParameter("routeForwId"));
            routeBackId = Long.parseLong(req.getParameter("routeBackId"));
        } catch (NumberFormatException exc) {
            logger.debug("Can't add 'Pair' of route: incorrect id parameters");
            nameError = "createFiasco";
            msgError = "Can't insert 'RoutePair'";
            return false;
        }

        if (!isValidParams(routeForwId, routeBackId))
            return false;

        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            RouteDAO routeDAO = daoFactory.getRouteDAO();
            RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();

            Route<Station> routeForw = routeDAO.getById(routeForwId);
            Route<Station> routeBack = routeDAO.getById(routeBackId);
            RoutePair<Station> pair = new RoutePairImpl<>(routeForw, routeBack);
            routePairDAO.insert(pair);
        } catch(DaoException exc) {
            nameError = "createFiasco";
            msgError = "Can't insert 'RoutePair'";
            logger.debug("Can't insert 'RoutePair'");
            return false;
        }

        return true;
    }

    private boolean isValidParams(long routeForwId, long routeBackId) {
        if (routeForwId == 0) {
            nameError = "nullRouteForw";
            msgError = "Please select 'Route forward'";
            return false;
        }

        if (routeBackId == 0) {
            nameError = "nullRouteBack";
            msgError = "Please select 'Route back'";
            return false;
        }

        if (routeForwId == routeBackId) {
            nameError = "equalNodes";
            msgError = "'Route forward' and 'Route forward' should be differ";
            return false;
        }

        return true;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("routeForwId", req.getParameter("routeForwId"));
        req.setAttribute("routeBackId", req.getParameter("routeBackId"));
    }
}
