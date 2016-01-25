package com.telesens.afanasiev.servlets.map;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.MapImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oleg on 1/25/16.
 */
@WebServlet (name="MapAddServlet", urlPatterns = "/map/add", loadOnStartup = 0)
public class MapAddServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Route<Station>> routes = getAllRoutes();
        req.setAttribute("routes", routes);

        req.getRequestDispatcher("/jsp/map/mapAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (createMapSuccess(req))
            resp.sendRedirect("/map/list");
        else {
            List<Route<Station>> routes =  getAllRoutes();
            rewriteParams(req);
            req.setAttribute("nodes", routes);
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);
            req.getRequestDispatcher("/jsp/map/mapAdd.jsp").forward(req, resp);
        }
    }

    private boolean createMapSuccess(HttpServletRequest req) {
        String name = req.getParameter("name");
        String describe = req.getParameter("describe");

        if (!isNameValid(name))
            return false;

        DaoManager daoManager = DaoManager.getInstance();
        MapDAO mapDAO = daoManager.getMapDAO();

        try {
            Map map = new MapImpl();
            map.setName(name);
            map.setDescribe(describe);
            mapDAO.insertOrUpdate(map);
        } catch(DaoException exc) {
            logger.debug("Unsuccessful attempt to create new map");
            exc.printStackTrace();
            nameError = "createFiasco";
            msgError = "Can't create new arc";
            return false;
        }

        return true;
    }

    private boolean isNameValid(String name) {
        if (name == null || name.isEmpty()) {
            nameError = "nullNameError";
            msgError = "Please input 'Name'";
            return false;
        }

        return true;
    }

    private List<Route<Station>> getAllRoutes() {
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        List<Route<Station>> routes;

        try {
            routes = new ArrayList<>(
                    routeDAO.getRange(0, 1000)
            );
        } catch(DaoException exc) {
            exc.printStackTrace();
            return new ArrayList<>();
        }

        Collections.sort(routes, (o1, o2) -> (o1.getNumber().compareTo(o2.getNumber())));

        return routes;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("name", req.getParameter("name"));
        req.setAttribute("describe", req.getParameter("describe"));
    }
}
