package com.telesens.afanasiev.servlets.station;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoManager;
import com.telesens.afanasiev.Station;
import com.telesens.afanasiev.StationDAO;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by oleg on 1/19/16.
 */
@WebServlet (name="StationListServlet", urlPatterns = "/station/list", loadOnStartup = 0)
public class StationListServlet extends PersistServlet {

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        List<Station> stations = getAllStations(sort, order);
        req.setAttribute("stations", stations);
        req.getRequestDispatcher("/jsp/station/stationList.jsp").forward(req, resp);
    }

    private List<Station> getAllStations(String sort, String order) {
        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();

        List<Station> stations = new ArrayList<>();
        try {
            stations = new ArrayList<>(stationDAO.getRange(0, 1000));
        } catch (DaoException exc) {
            logger.debug("Can't get range stations from db");
            exc.printStackTrace();
        }
        if (sort != null && order != null) {
            sortStations(stations, sort, order);
        }

        return stations;
    }

    private void sortStations(List<Station> stations, String sort, String order) {

        if (sort.equals("name")) {
            if (order.equals("desc")) {
                Collections.sort(stations, ((o1, o2) -> (o2.getName().compareToIgnoreCase(o1.getName()) )));
                return;
            }

            if (order.equals("inc")) {
                Collections.sort(stations, ((o1, o2) -> (o1.getName().compareToIgnoreCase(o2.getName()) )));
                return;
            }
            return;
        }

        if (sort.equals("id")) {
            if (order.equals("desc")) {
                Collections.sort(stations, ((o1, o2) -> (Long.compare(o2.getId(), o1.getId()) )));
                return;
            }

            if (order.equals("inc")) {
                Collections.sort(stations, ((o1, o2) -> (Long.compare(o1.getId(), o2.getId()) )));
                return;
            }
            return;
        }
    }
}
