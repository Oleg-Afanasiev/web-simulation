package com.telesens.afanasiev.servlets.edit.arc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet (name="ArcEditServlet", urlPatterns = "/arc/edit", loadOnStartup = 0)
public class ArcEditServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String paramId = req.getParameter("id");
        Arc<Station> arc = getArcById(paramId);
        List<Station> stations = getAllStations();

        if (arc != null) {
            req.setAttribute("id", arc.getId());
            req.setAttribute("nodeLeftId", arc.getNodeLeft().getId());
            req.setAttribute("nodeRightId", arc.getNodeRight().getId());
            req.setAttribute("duration", arc.getDuration());
            req.setAttribute("nodes", stations);
        }

        req.getRequestDispatcher("/jsp/arc/arcEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);
        if (editOrDelSuccess(req))
            resp.sendRedirect("/arc/list");
        else {
            List<Station> stations = getAllStations();
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);
            req.setAttribute("nodes", stations);
            req.getRequestDispatcher("/jsp/arc/arcEdit.jsp").forward(req, resp);
        }

    }

    private boolean editOrDelSuccess(HttpServletRequest req) {

        if (req.getParameter("delete") != null)
            return delSuccess(req);
        else
            return editSuccess(req);
    }

    private boolean editSuccess(HttpServletRequest req) {
        Long nodeLeftId;
        Long nodeRightId;
        Integer duration;

        Long id = 0L;

        try {
            id = Long.parseLong(req.getParameter("id"));
            nodeLeftId = Long.parseLong(req.getParameter("nodeLeftId"));
            nodeRightId = Long.parseLong(req.getParameter("nodeRightId"));
        } catch(NumberFormatException exc) {
            msgError = "Can't update this arc. Parameters";
            exc.printStackTrace();
            return false;
        }

        try {
            duration = Integer.parseInt(req.getParameter("duration"));
        } catch(NumberFormatException exc) {
            msgError = "Please, input correct value of duration";
            nameError = "fmtDur";
            exc.printStackTrace();
            return false;
        }

        if (!isValidNodes(nodeLeftId, nodeRightId)) {
            return false;
        }

        if (!isValidDuration(duration)) {
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        ArcDAO arcDAO = daoFactory.getArcDAO();
        StationDAO stationDAO = daoFactory.getStationDAO();

        try {
            Arc<Station> arc = arcDAO.getById(id);
            Station nodeLeft = stationDAO.getById(nodeLeftId);
            Station nodeRight = stationDAO.getById(nodeRightId);
            arc.setNodeLeft(nodeLeft);
            arc.setNodeRight(nodeRight);
            arc.setDuration(duration);
            arcDAO.insertOrUpdate(arc);
        } catch (DaoException exc) {
            logger.debug("Can't update arc by ID. id = " + id);
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean delSuccess(HttpServletRequest req) {

        Long id;

        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            msgError = "Can't delete this arc. Incorrect id";
            exc.printStackTrace();
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();

        try {
            ArcDAO arcDAO = daoFactory.getArcDAO();
            arcDAO.delete(id);
        } catch(DaoException exc) {
            //daoFactory.closeConnection();
            msgError = "Can't delete this arc";
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private Arc<Station> getArcById(String paramId) {
        Long id = 0L;

        try {
            id = Long.parseLong(paramId);
        } catch(NumberFormatException exc) {
            msgError = "Can't get arc by id. Incorrect id";
            exc.printStackTrace();
            return null;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        ArcDAO arcDAO = daoFactory.getArcDAO();
        Arc<Station> arc;
        try {
            arc = arcDAO.getById(id);
        } catch(DaoException exc) {
            msgError = "Can't get arc with specified ID. id = " + id;
            return null;
        }

        return arc;
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
            msgError = "Can't get all stations.";
            exc.printStackTrace();
            return new ArrayList<>();
        }
        return  stations;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("nodeLeftId", req.getParameter("nodeLeftId"));
        req.setAttribute("nodeRightId", req.getParameter("nodeRightId"));
        req.setAttribute("duration", req.getParameter("duration"));
    }

    private boolean isValidNodes(Long nodeLeftId, Long nodeRightId) {
        if (nodeLeftId == nodeRightId) {
            msgError = "Nodes should be differ";
            nameError = "equalNodes";
            return false;
        }
        return true;
    }

    private boolean isValidDuration(int duration) {
        if (duration <= 0) {
            msgError = "Duration should be positive";
            nameError = "negativeDuration";
            return false;
        }

        return true;
    }
}
