package com.telesens.afanasiev.servlets.edit.station;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.Station;
import com.telesens.afanasiev.StationDAO;
import com.telesens.afanasiev.impl.StationImpl;
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
@WebServlet(name="StationAddServlet", urlPatterns = "/station/add", loadOnStartup = 0)
public class StationAddServlet extends PersistServlet {
    private String msgError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/jsp/station/stationAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (createStationSuccess(req))
            resp.sendRedirect("/station/list");
        else {
            req.setAttribute("msgError", msgError);
            req.getRequestDispatcher("/jsp/station/stationAdd.jsp").forward(req, resp);
        }

    }

    private boolean createStationSuccess(HttpServletRequest req) {
        String name = req.getParameter("stationName").trim();
        req.setAttribute("name", name);

        if (!validName(name))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        Station station = new StationImpl();
        station.setName(name);

        try {
            stationDAO.insertOrUpdate(station);
        } catch(DaoException exc) {
            logger.debug("Unsuccessful attempt to create station: " + station);
            exc.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean validName(String name) {
        if (name == null || name.isEmpty()) {
            msgError = "Incorrect 'Name'";
            return false;
        }

        return true;
    }
}
