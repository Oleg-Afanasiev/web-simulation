package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RoutePairImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class ActPair implements Action {
    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);
    @Getter
    private String msgError;
    @Getter
    private final String nameError = "pairError";

    @Override
    public boolean doSave(HttpServletRequest req) {
        long routeForwardId;
        long routeBackId;

        try {
            routeForwardId = Long.parseLong(req.getParameter("routeForward"));
            routeBackId = Long.parseLong(req.getParameter("routeBack"));
        } catch(NumberFormatException exc) {
            msgError = "Can't create pair. Select values";
            logger.debug("Can't read IDs for pair from parameters");

            return false;
        }

        if (!isValidPair(routeForwardId, routeBackId))
            return false;

        try {
            DaoFactory daoFactory = DaoFactory.getInstance();
            RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
            RouteDAO routeDAO = daoFactory.getRouteDAO();

            Route<Station> routeForw = routeDAO.getById(routeForwardId);
            Route<Station> routeBack = routeDAO.getById(routeBackId);

            RoutePair<Station> routePair = new RoutePairImpl<>(routeForw, routeBack);
            routePairDAO.insert(routePair);

        } catch(DaoException | IllegalArgumentException exc) {
            msgError = "Can't create pair";
            logger.debug("Pair wasn't created");
            return false;
        }

        return true;
    }

    @Override
    public void clearParams(HttpServletRequest req) {

        req.setAttribute("notPairedBackRouteId", req.getParameter("routeBack"));
        req.setAttribute("notPairedForwRouteId", req.getParameter("routeForward"));

        req.setAttribute("circRouteId", req.getParameter("circRouteId"));
        req.setAttribute("pairForwardRouteId", req.getParameter("routePairs"));
    }

    private boolean isValidPair(long routeForwardId, long routeBackId) {
        if (routeForwardId == 0 || routeBackId == 0){
            msgError = "Please select routes";
            return false;
        }

        if (routeForwardId == routeBackId) {
            msgError = "Pair wasn't saved. Two pairs should be differ";
            return false;
        }

        return true;
    }
}
