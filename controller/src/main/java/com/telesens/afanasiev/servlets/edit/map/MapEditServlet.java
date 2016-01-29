package com.telesens.afanasiev.servlets.edit.map;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;
import com.telesens.afanasiev.servlets.edit.map.edit.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        writeDataParams(req);
        req.getRequestDispatcher("/jsp/map/mapEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);

        if (req.getParameter("deleteMap") != null) {
            ActMap actMap = new ActMap();
            if (actMap.doDelete(req)) {
                resp.sendRedirect("/map/list");
                return;
            } else {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            }
        }

        if (req.getParameter("saveStation") != null) {
            Action actStation = new ActStation();
            if (!actStation.doSave(req)) {
                msgError = actStation.getMsgError();
                nameError = actStation.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actStation.clearParams(req);
                req.setAttribute("saveStationSuccess", "success");
            }
        }

        if (req.getParameter("saveArc") != null) {
            Action actArc = new ActArc();
            if (!actArc.doSave(req)) {
                msgError = actArc.getMsgError();
                nameError = actArc.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actArc.clearParams(req);
                req.setAttribute("saveArcSuccess", "success");
            }
        }

        if (req.getParameter("saveRoute") != null) {
            ActRoute actRoute = new ActRoute();
            actRoute.rewriteParams(req);
            if (!actRoute.doSave(req)) {
                msgError = actRoute.getMsgError();
                nameError = actRoute.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actRoute.clearParams(req);
                req.setAttribute("saveRouteSuccess", "success");
            }
            actRoute.writeData(req);
        }
        else {
            if (req.getParameter("selectFirstNodeId") != null) {
                ActRoute actRoute = new ActRoute();
                actRoute.rewriteParams(req);
                actRoute.writeData(req);
            }
        }

        if (req.getParameter("saveRoutePair") != null) {
            Action actPair = new ActPair();
            if (!actPair.doSave(req)) {
                msgError = actPair.getMsgError();
                nameError = actPair.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actPair.clearParams(req);
                req.setAttribute("savePairSuccess", "success");
            }
        }

        if (req.getParameter("dockCircRoute") != null) {
            ActMap actMap = new ActMap();
            if (!actMap.doSaveCircRoute(req)) {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actMap.clearParams(req);
                req.setAttribute("saveMapSuccess", "success");
            }
        }

        if (req.getParameter("dockPairRoute") != null) {
            ActMap actMap = new ActMap();
            if (!actMap.doSavePairRoutes(req)) {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actMap.clearParams(req);
                req.setAttribute("saveMapSuccess", "success");
            }
        }

        if (req.getParameter("saveMap") != null) {
            Action actMap = new ActMap();
            if (!actMap.doSave(req)) {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actMap.clearParams(req);
                req.setAttribute("saveMapSuccess", "success");
            }
        }

        if (req.getParameter("undockCircRoute") != null) {
            ActMap actMap = new ActMap();
            if (!actMap.doDeleteAllCircRoutes(req)) {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actMap.clearParams(req);
                req.setAttribute("saveMapSuccess", "success");
            }
        }

        if (req.getParameter("undockPairRoute") != null) {
            ActMap actMap = new ActMap();
            if (!actMap.doDeleteAllPairs(req)) {
                msgError = actMap.getMsgError();
                nameError = actMap.getNameError();
                req.setAttribute("msgError", msgError);
                req.setAttribute("nameError", nameError);
            } else {
                actMap.clearParams(req);
                req.setAttribute("saveMapSuccess", "success");
            }
        }

        writeDataParams(req);
        req.getRequestDispatcher("/jsp/map/mapEdit.jsp").forward(req, resp);
    }

    private void rewriteParams(HttpServletRequest req) {
        // station
        req.setAttribute("stationName", req.getParameter("stationName"));

        // arc
        req.setAttribute("nodeLeftId", req.getParameter("nodeLeftId"));
        req.setAttribute("nodeRightId", req.getParameter("nodeRightId"));
        req.setAttribute("duration", req.getParameter("duration"));

        // pair
        req.setAttribute("notPairedBackRouteId", req.getParameter("routeBack"));
        req.setAttribute("notPairedForwRouteId", req.getParameter("routeForward"));

        req.setAttribute("circRouteId", req.getParameter("circRouteId"));
        req.setAttribute("pairForwardRouteId", req.getParameter("routePairs"));
    }

    private void writeDataParams(HttpServletRequest req) {
        Getter getter = new Getter();
        String paramId = req.getParameter("id");
        Map map = getter.getMapById(paramId);

        if (map != null) {
            List<RoutePair<Station>> routePairs = getter.getRoutePairs();
            List<Route<Station>> notPairedRoutes = getter.getNotPairedRoutes();
            Collection<Route<Station>> circRoutesNotInMap = getter.getNotPairedNotImMapRoutes(map.getId());
            Collection<RoutePair<Station>> routePairsNotInMap = getter.getRoutePairsNotInMap(map.getId());
            Collection<Route<Station>> circRoutesInMap = map.getCircularRoutes();
            Collection<RoutePair<Station>> pairsRoutesInMap = map.getPairsRoutes();
            Collection<Station> nodes = getter.getAllNodes();

            req.setAttribute("routePairs", routePairs);
            req.setAttribute("notPairedRoutes", notPairedRoutes);

            req.setAttribute("circRoutesNotInMap", circRoutesNotInMap);
            req.setAttribute("routePairsNotInMap", routePairsNotInMap);

            req.setAttribute("circRoutesInMap", circRoutesInMap);
            req.setAttribute("countCircRoutesInMap", circRoutesInMap.size());
            req.setAttribute("pairsRoutesInMap", pairsRoutesInMap);
            req.setAttribute("countPairsRoutesInMap", pairsRoutesInMap.size());

            req.setAttribute("nodes", nodes);

            req.setAttribute("id", map.getId());
            req.setAttribute("mapId", map.getId());
            req.setAttribute("mapName", map.getName());
            req.setAttribute("mapDescribe", map.getDescribe());
        }
    }
}
