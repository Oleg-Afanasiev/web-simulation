package com.telesens.afanasiev.servlets.map;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by oleg on 1/25/16.
 */
@WebServlet(name = "MapEditServlet", urlPatterns = "/map/edit", loadOnStartup = 0)
public class MapEditServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String paramId = req.getParameter("id");
        Map map = getMapById(paramId);

        if (map != null) {
            List<RoutePair<Station>> routePairs = getRoutePairs();
            List<Route<Station>> notPairedRoutes = getNotPairedRoutes();
            Collection<Route<Station>> circRoutesNotInMap = getNotPairedNotImMapRoutes(map.getId());
            Collection<RoutePair<Station>> routePairsNotInMap = getRoutePairsNotInMap(map.getId());
            Collection<Route<Station>> circRoutesInMap = map.getCircularRoutes();
            Collection<RoutePair<Station>> pairsRoutesInMap = map.getPairsRoutes();
            Collection<Station> nodes = getAllNodes();

            req.setAttribute("routePairs", routePairs);
            req.setAttribute("notPairedRoutes", notPairedRoutes);

            req.setAttribute("circRoutesNotInMap", circRoutesNotInMap);
            req.setAttribute("routePairsNotInMap", routePairsNotInMap);

            req.setAttribute("circRoutesInMap", circRoutesInMap);
            req.setAttribute("countCircRoutesInMap", circRoutesInMap.size());
            req.setAttribute("pairsRoutesInMap", pairsRoutesInMap);
            req.setAttribute("countPairsRoutesInMap", pairsRoutesInMap.size());

            req.setAttribute("nodes", nodes);

            req.setAttribute("mapId", map.getId());
            req.setAttribute("mapName", map.getName());
            req.setAttribute("mapDescribe", map.getDescribe());
        }

        req.getRequestDispatcher("/jsp/map/mapEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);
        if (editOrDelSuccess(req))
            resp.sendRedirect("/map/list");
        else {
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);
            req.getRequestDispatcher("/jsp/map/mapEdit.jsp").forward(req, resp);
        }
    }

    private  boolean editOrDelSuccess(HttpServletRequest req) {
        if (req.getParameter("delete") != null)
            return delSuccess(req);
        else
            return editSuccess(req);
    }

    private boolean editSuccess(HttpServletRequest req) {

        Long mapId = 0L;
        String mapName;
        String mapDescribe;

        try {
            mapId = Long.parseLong(req.getParameter("mapId"));
            mapName = req.getParameter("mapName");
            mapDescribe = req.getParameter("mapDescribe");
        } catch(NumberFormatException exc) {
            msgError = "Can't update map.";
            exc.printStackTrace();
            return false;
        }

        if (!isValidMapName(mapName)) {
            return false;
        }

        DaoManager daoManager = DaoManager.getInstance();
        MapDAO mapDAO = daoManager.getMapDAO();

        try {
            Map map = mapDAO.getById(mapId);
            map.setName(mapName);
            map.setDescribe(mapDescribe);
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            logger.debug("Can't update map with specified ID. id = " + mapId);
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean delSuccess(HttpServletRequest req) {

        Long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("mapId"));
        } catch(NumberFormatException exc) {
            msgError = "Can't delete this map. Incorrect id";
            exc.printStackTrace();
            return false;
        }

        DaoManager daoManager = DaoManager.getInstance();
        MapDAO mapDAO = daoManager.getMapDAO();

        try {
            mapDAO.delete(mapId);
        } catch(DaoException exc) {
            msgError = "Can't delete this map";
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean isValidMapName(String name) {
        if (name == null || name.isEmpty()) {
            nameError = "nullMapNameError";
            msgError = "Please input 'Name'";
            return false;
        }
        return true;
    }

    private Map getMapById(String paramId) {
        Long mapId;

        try {
            mapId = Long.parseLong(paramId);
        } catch(NumberFormatException exc) {
            logger.debug("Can't read map ID from parameters");
            return null;
        }

        DaoManager daoManager = DaoManager.getInstance();
        MapDAO mapDAO = daoManager.getMapDAO();
        Map map;

        try {
            map = mapDAO.getById(mapId);
        } catch(DaoException exc) {
            logger.debug("Can't get 'map' with specified ID. id = " + mapId);
            return null;
        }

        return map;
    }

    private List<RoutePair<Station>> getRoutePairs() {
        DaoManager daoManager = DaoManager.getInstance();
        RoutePairDAO routePairDAO = daoManager.getRoutePairDAO();
        List<RoutePair<Station>> listRoutePairs;
        try {
            listRoutePairs = new ArrayList<>(
                    routePairDAO.getRange(0, 1000)
            );
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'RoutePairs'");
            return new ArrayList<>();
        }

        return listRoutePairs;
    }

    private List<Route<Station>> getNotPairedRoutes() {
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        List<Route<Station>> listNotPairedRoutes;
        try {
            listNotPairedRoutes = new ArrayList<>(
                    routeDAO.getRangeNotPaired(0, 1000)
            );
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'NotPairedRoutes'");
            return new ArrayList<>();
        }

        return listNotPairedRoutes;
    }

    private Collection<Route<Station>> getNotPairedNotImMapRoutes(long mapId) {
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        Collection<Route<Station>> listRoutes;
        try {
            listRoutes = routeDAO.getRangeNotPairedNotInMap(0, 1000, mapId);
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'NotPairedRoutes'");
            return new ArrayList<>();
        }

        return listRoutes;
    }

    private Collection<RoutePair<Station>> getRoutePairsNotInMap(long mapId) {
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        Collection<RoutePair<Station>> listPairs;
        try {
            listPairs = routeDAO.getRangePairNotInMap(0, 1000, mapId);
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'PairNotInMap'");
            return new ArrayList<>();
        }

        return listPairs;
    }

    private Collection<Station> getAllNodes() {
        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        Collection<Station> stations;
        try {
            stations = stationDAO.getRange(0, 1000);
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'Stations'");
            return new ArrayList<>();
        }

        return stations;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("mapId", req.getParameter("mapId"));
        req.setAttribute("mapName", req.getParameter("mapName"));
        req.setAttribute("mapDescribe", req.getParameter("mapDescribe"));
    }
}
