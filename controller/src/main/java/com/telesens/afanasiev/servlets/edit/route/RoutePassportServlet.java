package com.telesens.afanasiev.servlets.edit.route;

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
@WebServlet(name="RoutePassportServlet", urlPatterns = "/route/passport", loadOnStartup = 0)
public class RoutePassportServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Route<Station> route = getRoute(req);
        req.setAttribute("route", route);

        req.getRequestDispatcher("/jsp/route/routePassport.jsp").forward(req, resp);
    }

    private Route<Station> getRoute(HttpServletRequest req) {
        long routeId;

        try {
            routeId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read route id from params");
            return null;
        }

        Route<Station> route;
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            RouteDAO routeDAO = daoFactory.getRouteDAO();
            route = routeDAO.getById(routeId);
        } catch(DaoException exc) {
            logger.debug("Can't get route with specified ID. id = " + routeId);
            return null;
        }

        return route;
    }
}
