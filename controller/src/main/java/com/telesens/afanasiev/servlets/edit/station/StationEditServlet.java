package com.telesens.afanasiev.servlets.edit.station;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.Station;
import com.telesens.afanasiev.StationDAO;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet(name="StationEditServlet", urlPatterns = "/station/edit", loadOnStartup = 0)
public class StationEditServlet extends PersistServlet {
    private String msgError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String paramId = req.getParameter("id");
        Station station = getStationById(paramId);

        if (station != null) {
            req.setAttribute("name", station.getName());
            req.setAttribute("id", station.getId());
        }

        req.getRequestDispatcher("/jsp/station/stationEdit.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        rewriteParams(req);
        if (editOrDelStationSuccess(req))
            resp.sendRedirect("/station/list");
        else {
            req.setAttribute("msgError", msgError);
            req.getRequestDispatcher("/jsp/station/stationEdit.jsp").forward(req, resp);
        }

    }

    private void rewriteParams(HttpServletRequest req) {
        req.setAttribute("id", req.getParameter("id"));
        req.setAttribute("name", req.getParameter("name"));
    }

    private boolean editOrDelStationSuccess(HttpServletRequest req) {

        if (req.getParameter("deleteStation") != null)
            return delStationSuccess(req);
        else
            return editStationSuccess(req);
    }

    private boolean editStationSuccess(HttpServletRequest req) {
        String name = req.getParameter("name");

        if (!isValidName(name))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();

        Long id = 0L;

        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            msgError = "Can't update this station. Incorrect id";
            exc.printStackTrace();
            return false;
        }

        try {
            Station station = stationDAO.getById(id);
            station.setName(name);
            stationDAO.insertOrUpdate(station);
        } catch (DaoException exc) {
            logger.debug("Can't update station by ID. id = " + id);
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean delStationSuccess(HttpServletRequest req) {

        Long id;

        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch(NumberFormatException exc) {
            msgError = "Can't delete this station. Incorrect id";
            exc.printStackTrace();
            return false;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();

        try {
            stationDAO.delete(id);
        } catch(DaoException exc) {
            msgError = "Can't delete this station";
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private Station getStationById(String idParam) {

        Long id;
        try {
            id = Long.parseLong(idParam);
        }  catch(NumberFormatException exc) {
            msgError = "Can't get station. Incorrect id";
            exc.printStackTrace();
            return null;
        }

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        Station station = null;
        try {
            station = stationDAO.getById(id);
        } catch (DaoException exc) {
            logger.debug("Can't get station by ID. id = " + id);
            exc.printStackTrace();
        }

        return station;
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            msgError = "Incorrect 'name'";
            return false;
        }
        return true;
    }
}
