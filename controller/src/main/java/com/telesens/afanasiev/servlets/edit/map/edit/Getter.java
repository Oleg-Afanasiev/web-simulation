package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class Getter {
    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);

    public Map getMapById(String paramId) {
        long mapId;

        try {
            mapId = Long.parseLong(paramId);
        } catch(NumberFormatException exc) {
            logger.debug("Can't read map ID from parameters");
            return null;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        Map map;

        try {
            map = mapDAO.getById(mapId);
        } catch(DaoException exc) {
            logger.debug("Can't get 'map' with specified ID. id = " + mapId);
            return null;
        }

        return map;
    }

    public List<RoutePair<Station>> getRoutePairs() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
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

    public List<Route<Station>> getNotPairedRoutes() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
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

    public Collection<Route<Station>> getNotPairedNotImMapRoutes(long mapId) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        Collection<Route<Station>> listRoutes;
        try {
            listRoutes = routeDAO.getRangeNotPairedNotInMap(0, 1000, mapId);
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'NotPairedRoutes'");
            return new ArrayList<>();
        }

        return listRoutes;
    }

    public Collection<RoutePair<Station>> getRoutePairsNotInMap(long mapId) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        Collection<RoutePair<Station>> listPairs;
        try {
            listPairs = routeDAO.getRangePairNotInMap(0, 1000, mapId);
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'PairNotInMap'");
            return new ArrayList<>();
        }

        return listPairs;
    }

    public List<Station> getAllNodes() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        List<Station> stations;
        try {
            stations = new ArrayList<>(
                    stationDAO.getRange(0, 1000));
        } catch(DaoException exc) {
            logger.debug("Can't getRange 'Stations'");
            return new ArrayList<>();
        }

        Collections.sort(stations, (o1, o2) -> (o1.getName().compareToIgnoreCase(o2.getName())));

        return stations;
    }

    /**
     * Read from parameters already selected arcs for route
     *
     * @param req
     * @return
     */
    public Collection<Arc<Station>> getArcSequencesForRoute(HttpServletRequest req) {

        List<Arc<Station>> arcSeq = new ArrayList<>();
        Arc<Station> arc = null;

        int index = 0;

        while (req.getParameter("arcSequencesForRoute" + index) != null) {
            arc = getArcByParam(req.getParameter("arcSequencesForRoute" + index));
            if (arc != null)
                arcSeq.add(arc);

            index++;
        }

        if (req.getParameter("attachArc") != null) {
            arc = getArcByParam(req.getParameter("arcId"));
            if (arc != null)
                arcSeq.add(arc);
        }
        else if (req.getParameter("undockArc") != null) {
            if (arcSeq.size() > 0)
                arcSeq.remove(arcSeq.size()-1);
            else
                return null; // hard code
        }

        return arcSeq;
    }

    public List<Arc<Station>> getNextArcsForRoute(long lastNodeId, Collection<Arc<Station>> arcSeq) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArcDAO arcDAO = daoFactory.getArcDAO();
        List<Arc<Station>> arcs;

        try {
            arcs = new ArrayList<>(
                    arcDAO.getRange(0, 1000)
            );
        } catch(DaoException exc) {
            exc.printStackTrace();
            return new ArrayList<>();
        }

        Arc<Station> arc;
        Iterator<Arc<Station>> iterator = arcs.iterator();
        while (iterator.hasNext()) {
            arc = iterator.next();
            if (arc.getNodeLeft().getId() != lastNodeId && arc.getNodeRight().getId() != lastNodeId) {
                iterator.remove();
            }
            else {
                for (Arc<Station> arcInSeq : arcSeq) {
                    if (arc.getId() == arcInSeq.getId()) {

                        iterator.remove();
                        break;
                    }
                }
            }
        }
        Collections.sort(arcs, (o1, o2) -> (o1.getNodeLeft().getName().compareTo(o2.getNodeLeft().getName())));

        return arcs;
    }

    public List<Station> getNodeSequencesForRoute(HttpServletRequest req, Collection<Arc<Station>> arcSeq) {
        List<Station> nodeSeq = new ArrayList<>();
        Long firstNodeId;
        Station node = null;

        if (arcSeq == null && req.getParameter("undockArc") != null)
            return nodeSeq;

        try {
            firstNodeId = Long.parseLong(req.getParameter("selectFirstNodeId"));
        } catch(NumberFormatException exc) {
            logger.debug("Can't read 'firstNodeId' from request");
            return nodeSeq;
        }

        try {
            if (firstNodeId > 0) {
                DaoFactory daoFactory = DaoFactory.getInstance();
                StationDAO stationDAO = daoFactory.getStationDAO();
                node = stationDAO.getById(firstNodeId);
                nodeSeq.add(node);
            }
        } catch (DaoException exc) {
            logger.debug("Can't get 'first node' with specified ID from db. id = " + firstNodeId);
            return nodeSeq;
        }

        for (Arc<Station> arc : arcSeq) {
            node = arc.getOppositeNode(node);
            if (node != null)
                nodeSeq.add(node);
        }

        return nodeSeq;
    }

    private Arc<Station> getArcByParam(String paramId) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArcDAO arcDAO = daoFactory.getArcDAO();
        Arc<Station> arc = null;
        long arcId = 0;
        try {
            arcId = Long.parseLong(paramId);
        } catch(NumberFormatException exc) {
            logger.debug("Can't read attached arc id");
            return null;
        }

        try {
            if (arcId > 0) {
                arc = arcDAO.getById(arcId);
            }
        } catch(DaoException exc) {
            logger.debug("Can't get arc with specified ID. id = " + arcId);
            return null;
        }

        return arc;
    }
}
