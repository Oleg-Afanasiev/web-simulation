package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.Station;
import com.telesens.afanasiev.StationDAO;
import com.telesens.afanasiev.impl.StationImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class ActStation implements Action {

    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);
    @Getter
    private String msgError;
    @Getter
    private final String nameError = "stationError";

    @Override
    public boolean doSave(HttpServletRequest req) {
        String name = req.getParameter("stationName");

        if (!isValidName(name))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        Station station = new StationImpl();
        station.setName(name);

        try {
            stationDAO.insertOrUpdate(station);
        } catch (DaoException exc) {
            msgError = "Station wasn't save";
            logger.debug("Can't save new 'station'");
            return false;
        }

        return true;
    }


    @Override
    public void clearParams(HttpServletRequest req) {
        req.setAttribute("stationName", null);
    }

    private boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            msgError = "Please input 'Name' of station ";
            return false;
        }

        return true;
    }
}
