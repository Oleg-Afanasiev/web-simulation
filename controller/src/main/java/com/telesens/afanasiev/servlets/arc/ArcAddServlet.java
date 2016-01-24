package com.telesens.afanasiev.servlets.arc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.ArcImpl;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oleg on 1/21/16.
 */
@WebServlet (name="ArcAddServlet", urlPatterns = "/arc/add", loadOnStartup = 0)
public class ArcAddServlet extends PersistServlet {
    private String msgError;
    private String nameError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<Station> stations =  getAllStations();
        req.setAttribute("nodes", stations);
        req.getRequestDispatcher("/jsp/arc/arcAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (createArcSuccess(req))
            resp.sendRedirect("/arc/list");
        else {
            List<Station> stations =  getAllStations();
            rewriteParams(req);
            req.setAttribute("nodes", stations);
            req.setAttribute("msgError", msgError);
            req.setAttribute("nameError", nameError);
            req.getRequestDispatcher("/jsp/arc/arcAdd.jsp").forward(req, resp);
        }

    }

    private boolean createArcSuccess(HttpServletRequest req) {
        String durationParam = req.getParameter("duration");

        if (!isDurationValid(durationParam))
            return false;

        int duration;
        Long nodeLeftId;
        Long nodeRightId;

        try {
            nodeLeftId = Long.parseLong(req.getParameter("nodeLeftId"));
            nodeRightId = Long.parseLong(req.getParameter("nodeRightId"));
            duration = Integer.parseInt(req.getParameter("duration"));
        } catch (NumberFormatException exc) {
            nameError = "createFiasco";
            msgError = "Can't add new arc";
            exc.printStackTrace();
            return false;
        }

        if (!isNodesValid(nodeLeftId, nodeRightId))
            return false;

        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        ArcDAO arcDAO = daoManager.getArcDAO();

        try {
            Station nodeLeft = stationDAO.getById(nodeLeftId);
            Station nodeRight = stationDAO.getById(nodeRightId);
            Arc<Station> arc = new ArcImpl<>();
            arc.setNodeLeft(nodeLeft);
            arc.setNodeRight(nodeRight);
            arc.setDuration(duration);
            arcDAO.insertOrUpdate(arc);
        } catch(DaoException exc) {
            logger.debug("Unsuccessful attempt to create new arc");
            exc.printStackTrace();
            nameError = "createFiasco";
            msgError = "Can't create new arc";
            return false;
        }

        return true;
    }

    private List<Station> getAllStations() {
        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        List<Station> stations;

        try {
            stations = new ArrayList<>(
                    stationDAO.getRange(0, 1000)
            );
        } catch(DaoException exc) {
            exc.printStackTrace();
            return new ArrayList<>();
        }

        Collections.sort(stations, (o1, o2) -> (o1.getName().compareTo(o2.getName())) );

        return stations;
    }

    private boolean isNodesValid(long nodeLeftId, long nodeRightId) {
        if (nodeLeftId == 0) {
            msgError = "Please, select left node";
            nameError = "nullNodeLeft";
            return false;
        }

        if (nodeRightId == 0) {
            msgError = "Please, select right node";
            nameError = "nullNodeRight";
            return false;
        }

        if (nodeLeftId == nodeRightId) {
            msgError = "Nodes should be differ";
            nameError = "equalNodes";
            return false;
        }

        return true;
    }

    private boolean isDurationValid(String durationParam) {

        if (durationParam == null || durationParam.isEmpty()) {
            nameError = "nullDuration";
            msgError = "Please input duration value";
            return false;
        }

        int duration;

        try {
            duration = Integer.parseInt(durationParam);
        } catch (NumberFormatException exc) {
            nameError = "fmtDur";
            msgError = "Incorrect value of 'duration'";
            exc.printStackTrace();
            return false;
        }

        if (duration <= 0) {
            msgError = "Duration should be positive";
            nameError = "negativeDuration";
            return false;
        }

        return true;
    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("nodeLeftId", req.getParameter("nodeLeftId"));
        req.setAttribute("nodeRightId", req.getParameter("nodeRightId"));
        req.setAttribute("duration", req.getParameter("duration"));
    }
}
