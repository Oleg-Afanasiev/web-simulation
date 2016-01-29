package com.telesens.afanasiev.servlets.edit.map.passport;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.SearchEngineImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by oleg on 1/28/16.
 */
@WebServlet (name="MapPassportServlet", urlPatterns = "/map/passport", loadOnStartup = 0)
public class MapPassportServlet extends PersistServlet {
    String msgError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);
        Map map = getMap(req);
        List<Station> nodes = getNodes(req, map);

        req.setAttribute("map", map);
        req.setAttribute("nodes", nodes);
        req.setAttribute("msgError", msgError);
        req.getRequestDispatcher("/jsp/map/passport/mapPassport.jsp").forward(req, resp);
    }

    @Override
    protected  void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);

        Map map = getMap(req);
        List<Station> nodes = getNodes(req, map);

        Collection<Station> searchRes = search(req, map);

        req.setAttribute("map", map);
        req.setAttribute("nodes", nodes);
        req.setAttribute("searchRes", searchRes);
        req.setAttribute("msgError", msgError);

        req.getRequestDispatcher("/jsp/map/passport/mapPassport.jsp").forward(req, resp);
    }

    private Map getMap(HttpServletRequest req) {
        long mapId;

        try {
            mapId = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read map from params");
            return null;
        }

        Map map;
        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            MapDAO mapDAO = daoFactory.getMapDAO();
            map = mapDAO.getById(mapId);
        } catch(DaoException exc) {
            logger.debug("Can't get 'map' with specified ID. id = " + mapId);
            return null;
        }

        return map;
    }

    private List<Station> getNodes(HttpServletRequest req, Map map) {
        List<Station> nodes = new ArrayList<>();

        if (map != null) {
            nodes.addAll(map.getAllStations());
        }

        Collections.sort(nodes, (o1, o2)->(o1.getName().compareTo(o2.getName())));
        return nodes;
    }

    private Collection<Station> search (HttpServletRequest req, Map map) {
        long fromNodeId;
        long toNodeId;

        try {
            fromNodeId = Long.parseLong(req.getParameter("fromNode"));
            toNodeId = Long.parseLong(req.getParameter("toNode"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read node parameters");
            msgError = "Can't read node parameters";
            return null;
        }

        if (fromNodeId == 0 || toNodeId == 0) {
            msgError = "Select stations";
            return null;
        }

        if (fromNodeId == toNodeId) {
            msgError = "Stations should be differ";
            return null;
        }

        SearchEngine<Station> sr = new SearchEngineImpl<>();
        Collection<Station> nodeSeq =  map.searchFastestPath(sr, fromNodeId, toNodeId);
        req.setAttribute("time", sr.getTotalDuration());
        return nodeSeq;
    }

    private void rewriteParams(HttpServletRequest req) {
        msgError = "";
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("nodeFromId", req.getParameter("fromNode"));
        req.setAttribute("nodeToId", req.getParameter("toNode"));
    }
}
