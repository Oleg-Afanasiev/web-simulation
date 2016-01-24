package com.telesens.afanasiev.servlets.route;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by oleg on 1/23/16.
 */
@WebServlet(name="RouteEditServlet", urlPatterns = "/route/edit", loadOnStartup = 0)
public class RouteEditServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String paramId = req.getParameter("id");
        Route<Station> route = getRouteById(paramId);
        List<Station> nodeSeq;

        if (route != null) {
            nodeSeq = new ArrayList<>(route.getSequenceNodes());
            req.setAttribute("id", route.getId());
            req.setAttribute("number", route.getNumber());
            req.setAttribute("cost", route.getCost());
            req.setAttribute("description", route.getDescription());
            req.setAttribute("nodeSeq", nodeSeq);
            req.setAttribute("arcSeq", route.getSequenceArcs());
            req.setAttribute("countSeqNodes", nodeSeq.size());

            if (nodeSeq.size() > 0) {
                req.setAttribute("firstNode", nodeSeq.get(0));
                req.setAttribute("lastNode", nodeSeq.get(nodeSeq.size()-1));
            }
        }

        req.getRequestDispatcher("/jsp/route/routeEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);
        if (editOrDelSuccess(req))
            resp.sendRedirect("/route/list");
        else {
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);

            String paramId = req.getParameter("id");
            Route<Station> route = getRouteById(paramId);

            if (route != null) {
                List<Station> nodeSeq = new ArrayList<>(route.getSequenceNodes());
                req.setAttribute("nodeSeq", nodeSeq);
                req.setAttribute("countSeqNodes", nodeSeq.size());
                req.setAttribute("arcSeq", route.getSequenceArcs());

                if (nodeSeq.size() > 0) {
                    req.setAttribute("firstNode", nodeSeq.get(0));
                    req.setAttribute("lastNode", nodeSeq.get(nodeSeq.size()-1));
                }
            }

            req.getRequestDispatcher("/jsp/route/routeEdit.jsp").forward(req, resp);
        }
    }

    private Route<Station> getRouteById(String paramId) {
        long id;
        try {
            id = Long.parseLong(paramId);
        } catch(NumberFormatException exc) {
            logger.debug("Can't read route from parameters");
            return null;
        }
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        Route<Station> route;

        try {
            route = routeDAO.getById(id);
        } catch (DaoException exc) {
            logger.debug("Can't get route with specified ID. id = " + id);
            return null;
        }

        return route;
    }

    private boolean editOrDelSuccess(HttpServletRequest req) {
        if (req.getParameter("save") != null) {
            return editSuccess(req);
        } else {
            return deleteSuccess(req);
        }
    }

    private boolean editSuccess(HttpServletRequest req) {

        String number = req.getParameter("number");
        String description = req.getParameter("description");
        BigDecimal cost;
        long id;

        if (!isValidNumber(number)) {
            return false;
        }

        try  {
            id = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            msgError = "Can't read route id";
            nameError = "updateFiasco";
            return false;
        }

        try  {
            cost = new BigDecimal(req.getParameter("cost"));
        } catch(NumberFormatException exc) {
            msgError = "Incorrect 'Cost' format";
            nameError = "costFmtErr";
            return false;
        }

        if (!isValidCost(cost))
            return false;

        try {
            DaoManager daoManager = DaoManager.getInstance();
            RouteDAO routeDAO = daoManager.getRouteDAO();
            Collection<Arc<Station>> arcSeq = getArcSeq(req);

            Route<Station> route = routeDAO.getById(id);

            route.setNumber(number);
            route.setCost(cost);
            route.setDescription(description);
            route.setSequenceArcs(arcSeq);
            routeDAO.insertOrUpdate(route);
        } catch (DaoException exc) {
            logger.debug("Can't update route with specified ID. id = " + id);
            msgError = "Can't update route";
            nameError = "updateFiasco";
            return false;
        }

        return true;
    }

    private boolean deleteSuccess(HttpServletRequest req) {
        Long id;

        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            msgError = "Can't delete this route. Incorrect id";
            exc.printStackTrace();
            return false;
        }

        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();

        try {
            routeDAO.delete(id);
        } catch(DaoException exc) {
            msgError = "Can't delete this route";
            logger.debug("Unsuccessful attempt to delete route");
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private Collection<Arc<Station>> getArcSeq(HttpServletRequest req)
        throws DaoException {
        Collection<Arc<Station>> arcSeq = new ArrayList<>();

        int index = 0;
        String idParam;
        Arc<Station> arc;
        while (req.getParameter("arcSeq" + index) != null) {
            idParam = req.getParameter("arcSeq" + index);
            arc = getArcById(idParam);
            if (arc != null)
                arcSeq.add(arc);

            index++;
        }

        return arcSeq;
    }

    private Arc<Station> getArcById(String idParam)
        throws DaoException {
        long id;

        try {
            id = Long.parseLong(idParam);
        } catch (NumberFormatException exc) {
            logger.debug("Can't read arc id from parameters");
            throw new DaoException("Can't read id");
        }
        DaoManager daoManager = DaoManager.getInstance();
        ArcDAO arcDAO = daoManager.getArcDAO();

        Arc<Station> arc = arcDAO.getById(id);

        return arc;
    }

    private boolean isValidNumber(String number) {
        if (number == null || number.isEmpty()) {
            msgError = "Please, input number value";
            nameError = "nullNumberErr";
            return false;
        }
        return true;
    }

    private boolean isValidCost(BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            msgError = "'Cost' can't be negative. Please input correct value";
            nameError = "negativeCostErr";
            return false;
        }
        return true;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("number", req.getParameter("number"));
        req.setAttribute("cost", req.getParameter("cost"));
        req.setAttribute("description", req.getParameter("description"));
    }
}
