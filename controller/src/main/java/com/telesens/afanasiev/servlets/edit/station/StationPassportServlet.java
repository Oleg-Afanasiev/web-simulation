package com.telesens.afanasiev.servlets.edit.station;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet(name="StationPassportServlet", urlPatterns = "/station/passport", loadOnStartup = 0)
public class StationPassportServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Station node = getNode(req);
        Collection<Route<Station>> routes = getRoutes(req);
        req.setAttribute("node", node);
        req.setAttribute("routes", routes);

        req.getRequestDispatcher("/jsp/station/stationPassport.jsp").forward(req, resp);
    }

    private Station getNode(HttpServletRequest req) {
        long nodeId;

        try {
            nodeId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read node id from params");
            return null;
        }

        Station node;

        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            StationDAO nodeDAO = daoFactory.getStationDAO();

            node = nodeDAO.getById(nodeId);
        } catch(DaoException exc) {
            logger.debug("Can't get node with specified ID. id = " + nodeId);
            return null;
        }

        return node;
    }

    private Collection<Route<Station>> getRoutes(HttpServletRequest req) {
        long mapId;
        long nodeId;

        try {
            mapId = Long.parseLong(req.getParameter("mapid"));
            nodeId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read map id from params");
            return null;
        }

        Map map;
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MapDAO mapDAO = daoFactory.getMapDAO();

            map = mapDAO.getById(mapId);
        } catch(DaoException exc) {
            logger.debug("Can't get map with specified ID. id = " + mapId);
            return null;
        }

        return map.getRoutesAcrossNode(nodeId);
    }
}
