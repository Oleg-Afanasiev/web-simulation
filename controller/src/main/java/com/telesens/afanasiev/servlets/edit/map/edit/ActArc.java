package com.telesens.afanasiev.servlets.edit.map.edit;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.ArcImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class ActArc implements Action {

    private static final Logger logger = LoggerFactory.getLogger(ActStation.class);
    @Getter
    private final String nameError = "arcError";
    @Getter
    private String msgError;

    @Override
    public boolean doSave(HttpServletRequest req) {
        long nodeLeftId;
        long nodeRightId;
        int duration;

        try {
            nodeLeftId = Long.parseLong(req.getParameter("nodeLeftId"));
            nodeRightId = Long.parseLong(req.getParameter("nodeRightId"));
            duration = Integer.parseInt(req.getParameter("duration"));
        } catch(NumberFormatException exc) {
            msgError = "Can't create arc. Fill all values";
            logger.debug("Can't read nodes from parameters");

            return false;
        }

        if (!isValidNodes(nodeLeftId, nodeRightId))
            return false;

        if (!isValidDuration(duration))
            return false;

        DaoFactory daoFactory = DaoFactory.getInstance();
        StationDAO stationDAO = daoFactory.getStationDAO();
        ArcDAO arcDAO = daoFactory.getArcDAO();

        try {
            Station nodeLeft = stationDAO.getById(nodeLeftId);
            Station nodeRight = stationDAO.getById(nodeRightId);
            Arc<Station> arc = new ArcImpl<>();
            arc.setNodeLeft(nodeLeft);
            arc.setNodeRight(nodeRight);
            arc.setDuration(duration);
            arcDAO.insertOrUpdate(arc);

        } catch(DaoException | IllegalArgumentException exc) {
            msgError = "Can't create arc";
            logger.debug("Arc wasn't create");
            return false;
        }

        return true;
    }


    @Override
    public void clearParams(HttpServletRequest req) {
        req.setAttribute("nodeLeftId", 0);
        req.setAttribute("nodeRightId", 0);
        req.setAttribute("duration", null);
    }

    private boolean isValidNodes(long nodeLeftId, long nodeRightId) {
        if (nodeLeftId == 0 || nodeRightId == 0 || nodeLeftId == nodeRightId){
            msgError = "Can't create arc. Two nodes should be differ";
            return false;
        }

        return true;
    }

    private boolean isValidDuration(int duration) {
        if (duration <= 0) {
            msgError = "Can't create arc. 'Duration' should be positive";
            return false;
        }

        return true;
    }
}
