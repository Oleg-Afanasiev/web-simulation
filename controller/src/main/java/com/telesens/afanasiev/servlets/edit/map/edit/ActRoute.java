package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RouteImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class ActRoute implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);
    @Getter
    private String msgError;
    @Getter
    private final String nameError = "routeError";
    private boolean wasSaved = false;

    public boolean doSave(HttpServletRequest req) {
        wasSaved = false;
        String number = req.getParameter("number");
        String describe = req.getParameter("routeDescribe");
        BigDecimal cost;

        try {
            cost = new BigDecimal(req.getParameter("cost"));
        } catch(NumberFormatException exc) {
            logger.debug("Route wasn't create");
            msgError = "Route wasn't create - incorrect 'cost'";
            return false;
        }

        if (!isValidNumber(number)) {
            return false;
        }

        if (!isValidCost(cost)) {
            return false;
        }

        String firstNodeIdParam = req.getParameter("selectFirstNodeId");
        Long firstNodeId;

        try {
            firstNodeId = Long.parseLong(firstNodeIdParam);
        } catch (NumberFormatException exc) {
            msgError = "Can't add new route";
            logger.debug("Route wasn't create");
            return false;
        }

        if (!firstNodeValid(firstNodeId))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        com.telesens.afanasiev.servlets.edit.map.edit.Getter getter = new com.telesens.afanasiev.servlets.edit.map.edit.Getter();
        Collection<Arc<Station>> arcSeq = getter.getArcSequencesForRoute(req);

        if (arcSeq.size() == 0) {
            msgError = "Route must have as least one arc";
            return false;
        }

        try {
            Station firstNode = stationDAO.getById(firstNodeId);
            Route<Station> route = new RouteImpl<>();

            route.setNumber(number);
            route.setCost(cost);
            route.setDescription(describe);
            route.setFirstNode(firstNode);
            route.setSequenceArcs(arcSeq);

            routeDAO.insertOrUpdate(route);

        } catch(DaoException exc) {
            logger.debug("Unsuccessful attempt to create new route");
            exc.printStackTrace();
            msgError = "Can't create new route";
            return false;
        }

        wasSaved = true;
        return true;
    }

    @Override
    public void clearParams(HttpServletRequest req) {
        req.setAttribute("selectFirstNodeId", 0);
        req.setAttribute("number", null);
        req.setAttribute("cost", null);
        req.setAttribute("routeDescribe", null);
        req.setAttribute("countSeqNodes", null);
    }

    public void writeData(HttpServletRequest req) {
        com.telesens.afanasiev.servlets.edit.map.edit.Getter getter = new com.telesens.afanasiev.servlets.edit.map.edit.Getter();
        Collection<Arc<Station>> arcSequencesForRoute = wasSaved ? new ArrayList<>() : getter.getArcSequencesForRoute(req);
        List<Station> nodeSequencesForRoute = wasSaved ? new ArrayList<>(): getter.getNodeSequencesForRoute(req, arcSequencesForRoute);
        Long lastNodeId = nodeSequencesForRoute.size() > 0 ? nodeSequencesForRoute.get(nodeSequencesForRoute.size()-1).getId() : 0;
        Collection<Arc<Station>> nextArcsForRoute = getter.getNextArcsForRoute(lastNodeId, arcSequencesForRoute);

        req.setAttribute("arcSequencesForRoute", arcSequencesForRoute);
        req.setAttribute("nodeSequencesForRoute", nodeSequencesForRoute);
        req.setAttribute("nextArcsForRoute", nextArcsForRoute);
        req.setAttribute("countSeqNodes", nodeSequencesForRoute.size());
    }

    public void rewriteParams(HttpServletRequest req) {
        req.setAttribute("selectFirstNodeId", req.getParameter("selectFirstNodeId"));
        req.setAttribute("number", req.getParameter("number"));
        req.setAttribute("cost", req.getParameter("cost"));
        req.setAttribute("routeDescribe", req.getParameter("routeDescribe"));
        req.setAttribute("countSeqNodes", req.getParameter("countSeqNodes"));
    }

    private boolean isValidNumber(String number) {
        if (number == null || number.isEmpty()) {
            msgError = "Please input 'Number' of route";
            return false;
        }
        return true;
    }

    private boolean isValidCost(BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            msgError = "'Cost' shouldn't be negative value";
            return false;
        }
        return true;
    }

    private boolean firstNodeValid(Long firstNodeId) {
        if (firstNodeId <= 0) {
            msgError = "Please, select first node";
            return false;
        }
        return true;
    }
}
