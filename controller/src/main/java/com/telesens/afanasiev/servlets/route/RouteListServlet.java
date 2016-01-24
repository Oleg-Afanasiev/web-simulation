package com.telesens.afanasiev.servlets.route;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by oleg on 1/19/16.
 */
@WebServlet (name="RouteListServlet", urlPatterns = "/route/list", loadOnStartup = 0)
public class RouteListServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Collection<Route<Station>> routes = getAllRoutes(sort, order);
        req.setAttribute("routes", routes);
        req.getRequestDispatcher("/jsp/route/routeList.jsp").forward(req, resp);
    }

    private Collection<Route<Station>> getAllRoutes(String sort, String order) {
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        List<Route<Station>> routes;

        try {
            routes = new ArrayList<>(routeDAO.getRange(0, 1000));
        } catch(DaoException exc) {
            logger.debug("Can't get range routes from db");
            exc.printStackTrace();
            return new ArrayList<>();
        }

        if (sort != null && order != null)
            sortRoutes(routes, sort, order);

        return routes;
    }

    private void sortRoutes(List<Route<Station>> routes, String sort, String order) {

        if (sort.equals("id")) {
            if (order.equals("desc")) {
                Collections.sort(routes, ((o1, o2) -> (Long.compare(o2.getId(), o1.getId()))));
            } else {
                Collections.sort(routes, ((o1, o2) -> (Long.compare(o1.getId(), o2.getId()) )));
            }
            return;
        }

        if (sort.equals("number")) {
            if (order.equals("desc")) {
                Collections.sort(routes, ((o1, o2) -> (o2.getNumber().compareToIgnoreCase(o1.getNumber()) )));
            } else {
                Collections.sort(routes, ((o1, o2) -> (o1.getNumber().compareToIgnoreCase(o2.getNumber()) )));
            }
            return;
        }

        if (sort.equals("cost")) {
            if (order.equals("desc")) {
                Collections.sort(routes, ((o1, o2) -> (o2.getCost().compareTo(o1.getCost())) ));
            } else {
                Collections.sort(routes, ((o1, o2) -> (o1.getCost().compareTo(o2.getCost())) ));
            }
            return;
        }

        if (sort.equals("firstnode")) {
            if (order.equals("desc")) {
                Collections.sort(routes, ((o1, o2) -> (o2.getFirstNode().getName().compareTo(o1.getFirstNode().getName()) )));
            } else {
                Collections.sort(routes, ((o1, o2) -> (o1.getFirstNode().getName().compareTo(o2.getFirstNode().getName()) )));
            }
            return;
        }

        if (sort.equals("lastnode")) {
            if (order.equals("desc")) {
                Collections.sort(routes, ((o1, o2) -> (o2.getLastNode().getName().compareTo(o1.getLastNode().getName()) )));
            } else {
                Collections.sort(routes, ((o1, o2) -> (o1.getLastNode().getName().compareTo(o2.getLastNode().getName()) )));
            }
            return;
        }
    }
}
