package com.telesens.afanasiev.servlets.edit.route;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RouteImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet (name="RouteAddServlet", urlPatterns = "/route/add", loadOnStartup = 0)
public class RouteAddServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Station> nodes = getAllStations();
        req.setAttribute("nodes", nodes);
        req.setAttribute("countSeqNodes", 0);

        req.getRequestDispatcher("/jsp/route/routeAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getParameter("save") != null) {
            if (createRouteSuccess((req))) {
                resp.sendRedirect("/route/list");
            } else {
                doReturn(req, resp);
            }
            return ;
        }

        doReturn(req, resp);
    }

    private void doReturn(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        rewriteParams(req);

        List<Station> nodes =  getAllStations();
        Collection<Arc<Station>> arcSeq = getArcSequence(req);
        List<Station> nodeSeq = getNodeSequence(req, arcSeq);
        Long lastNodeId = nodeSeq.size() > 0 ? nodeSeq.get(nodeSeq.size()-1).getId() : 0;
        List<Arc<Station>> arcs =  getAllArcs(lastNodeId, arcSeq);
        Station firstNode = nodeSeq.size() > 0 ? nodeSeq.get(0) : null;
        Station lastNode = nodeSeq.size() > 1 ? nodeSeq.get(nodeSeq.size() - 1) : null;

        req.setAttribute("nodes", nodes);
        req.setAttribute("arcs", arcs);
        req.setAttribute("arcSeq", arcSeq);
        req.setAttribute("nodeSeq", nodeSeq);
        req.setAttribute("firstNode", firstNode);
        req.setAttribute("firstNodeId", firstNode == null ? "0" : firstNode.getId());
        req.setAttribute("lastNode", lastNode);
        req.setAttribute("countSeqNodes", nodeSeq.size());
        req.setAttribute("msgError", msgError);
        req.setAttribute("nameError", nameError);
        req.getRequestDispatcher("/jsp/route/routeAdd.jsp").forward(req, resp);
    }

    private List<Arc<Station>> getAllArcs(long lastNodeId, Collection<Arc<Station>> arcSeq) {
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

    private List<Station> getAllStations() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        List<Station> stations;

        try {
            stations = new ArrayList<>(
                    stationDAO.getRange(0, 1000)
            );
        } catch(DaoException exc) {
            exc.printStackTrace();
            return new ArrayList<>();
        }

        Collections.sort(stations, (o1, o2) -> (o1.getName().compareTo(o2.getName())));

        return stations;
    }

    private List<Arc<Station>> getArcSequence(HttpServletRequest req) {
        List<Arc<Station>> arcSeq = new ArrayList<>();
        Arc<Station> arc = null;

        int index = 0;

        while (req.getParameter("arcSeq" + index) != null) {
            arc = getArcByParam(req.getParameter("arcSeq" + index));
            if (arc != null)
                arcSeq.add(arc);

            index++;
        }

        if (req.getParameter("attachArc") != null) {
            arc = getArcByParam(req.getParameter("arcId"));
            if (arc != null)
                arcSeq.add(arc);
        }
        else if (req.getParameter("undock") != null) {
            if (arcSeq.size() > 0)
                arcSeq.remove(arcSeq.size()-1);
            else
                return null; // hard code
        }

        return arcSeq;
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

    private List<Station> getNodeSequence(HttpServletRequest req, Collection<Arc<Station>> arcSeq) {
        List<Station> nodeSeq = new ArrayList<>();
        Long firstNodeId;
        Station node = null;

        if (arcSeq == null && req.getParameter("undock") != null)
            return nodeSeq;

        try {
            firstNodeId = Long.parseLong(req.getParameter("firstNodeId"));
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

    private boolean createRouteSuccess(HttpServletRequest req) {

        String number = req.getParameter("number");
        String costParam = req.getParameter("cost");
        String firstNodeIdParam = req.getParameter("firstNodeId");
        String arcIdParam = req.getParameter("arcId");
        String description = req.getParameter("describe");

        if (!isNumberValid(number))
            return false;

        if(!isCostValid(costParam)){
            return false;
        }

        Long firstNodeId;
        BigDecimal cost;

        try {
            firstNodeId = Long.parseLong(firstNodeIdParam);
            cost = new BigDecimal(costParam);
        } catch (NumberFormatException exc) {
            nameError = "createFiasco";
            msgError = "Can't add new route";
            exc.printStackTrace();
            return false;
        }

        if (!firstNodeValid(firstNodeId))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        Collection<Arc<Station>> arcSeq = getArcSequence(req);

        if (arcSeq.size() == 0) {
            nameError = "createFiasco";
            msgError = "Route must have as least one arc";
            return false;
        }

        try {
            Station firstNode = stationDAO.getById(firstNodeId);

            Route<Station> route = new RouteImpl<>();
            route.setNumber(number);
            route.setCost(cost);
            route.setDescription(description);
            route.setFirstNode(firstNode);
            route.setSequenceArcs(arcSeq);

            routeDAO.insertOrUpdate(route);

        } catch(DaoException exc) {
            logger.debug("Unsuccessful attempt to create new route");
            exc.printStackTrace();
            nameError = "createFiasco";
            msgError = "Can't create new route";
            return false;
        }

        return true;
    }

    private boolean isNumberValid(String number) {
        if (number == null || number.isEmpty()) {
            nameError = "nullNumber";
            msgError = "Please, input 'number' value";
            return false;
        }

        return true;
    }

    private boolean isCostValid(String costParam) {
        if (costParam == null)
            return false;
        BigDecimal bd;
        try {
            bd = new BigDecimal(costParam);
        } catch(NumberFormatException exc) {
            nameError = "costFmtError";
            msgError = "Incorrect 'cost' format";
            return false;
        }

        if (bd.compareTo(BigDecimal.ZERO) < 0) {
            nameError = "costNegativeError";
            msgError = "'Cost' value should be positive";
            return false;
        }

        return true;
    }

    private boolean firstNodeValid(Long firstNodeId) {
        if (firstNodeId <= 0) {
            nameError = "nullFirstNode";
            msgError = "Please, select first node";
            return false;
        }
        return true;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("number", req.getParameter("number"));
        req.setAttribute("cost", req.getParameter("cost"));
        req.setAttribute("firstNodeId", req.getParameter("firstNodeId"));
        req.setAttribute("arcId", req.getParameter("arcId"));
        req.setAttribute("describe", req.getParameter("describe"));
    }
}
