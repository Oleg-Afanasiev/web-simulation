package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class ActMap implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);
    @Getter
    private String msgError;
    @Getter
    private final String nameError = "mapError";

    @Override
    public boolean doSave(HttpServletRequest req) {
        String mapName = req.getParameter("mapName");
        String description = req.getParameter("mapDescribe");
        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't update map");
            msgError = "Can't update map";
            return false;
        }

        if (!isValidName(mapName))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Map map = mapDAO.getById(mapId);
        map.setName(mapName);
        map.setDescribe(description);

        try {
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            msgError = "Can't update map";
            logger.debug("Can't update map");
            return false;
        }

        return true;
    }

    public boolean doDelete(HttpServletRequest req) {
        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't delete map");
            msgError = "Can't delete map";
            return false;
        }

        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MapDAO mapDAO = daoFactory.getMapDAO();
            mapDAO.delete(mapId);
        } catch (DaoException exc) {
            msgError = "Can't delete map";
            logger.debug("Can't delete map");
            return false;
        }

        return true;
    }

    public boolean doSaveCircRoute(HttpServletRequest req) {

        long mapId;
        long circRouteId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
            circRouteId = Long.parseLong(req.getParameter("circRouteId"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't update map");
            msgError = "Can't update map";
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        Route<Station> route = routeDAO.getById(circRouteId);
        Map map = mapDAO.getById(mapId);

        map.registerCircularRoute(route);

        try {
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            msgError = "Can't update map";
            logger.debug("Can't update map");
            return false;
        }

        return true;
    }

    public boolean doSavePairRoutes(HttpServletRequest req) {
        long mapId;
        long routeForwId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
            routeForwId = Long.parseLong(req.getParameter("routePairs"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't update map");
            msgError = "Can't update map";
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
        Route<Station> routeForw = routeDAO.getById(routeForwId);
        Route<Station> routeBack = routePairDAO.getBackByForwId(routeForwId);

        Map map = mapDAO.getById(mapId);

        map.registerSimpleRoute(routeForw, routeBack);

        try {
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            msgError = "Can't update map";
            logger.debug("Can't update map");
            return false;
        }

        return true;
    }

    public boolean doDeleteAllCircRoutes(HttpServletRequest req) {
        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't update map");
            msgError = "Can't update map";
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Map map = mapDAO.getById(mapId);

        map.clearCircularRoutes();

        try {
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            msgError = "Can't update map";
            logger.debug("Can't update map");
            return false;
        }

        return true;
    }

    public boolean doDeleteAllPairs(HttpServletRequest req) {
        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't update map");
            msgError = "Can't update map";
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Map map = mapDAO.getById(mapId);

        map.clearSimpleRoutes();

        try {
            mapDAO.insertOrUpdate(map);
        } catch (DaoException exc) {
            msgError = "Can't update map";
            logger.debug("Can't update map");
            return false;
        }

        return true;
    }

    @Override
    public void clearParams(HttpServletRequest req) {

    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            msgError = "Please input 'Name' of map ";
            return false;
        }

        return true;
    }
}
