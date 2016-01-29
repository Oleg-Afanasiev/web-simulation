package com.telesens.afanasiev.servlets.edit.map;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by oleg on 1/19/16.
 */
@WebServlet(name="MapListServlet", urlPatterns = "/map/list", loadOnStartup = 0)
public class MapListServlet extends PersistServlet {
    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Collection<Map> maps = getAllMaps(sort, order);
        req.setAttribute("maps", maps);
        req.getRequestDispatcher("/jsp/map/mapList.jsp").forward(req, resp);
    }

    @Override
    protected  void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }

    Collection<Map> getAllMaps(String sort, String order) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MapDAO mapDAO = daoFactory.getMapDAO();
        List<Map> maps;

        try {
            maps = new ArrayList<>(mapDAO.getRange(0, 1000));
        } catch(DaoException exc) {
            logger.debug("Can't get range map from db");
            exc.printStackTrace();
            return new ArrayList<>();
        }

        if (sort != null && order != null)
            sortRoutes(maps, sort, order);

        return maps;
    }

    private void sortRoutes(List<Map> maps, String sort, String order) {

        if (sort.equals("id")) {
            if (order.equals("desc")) {
                Collections.sort(maps, ((o1, o2) -> (Long.compare(o2.getId(), o1.getId()))));
            } else {
                Collections.sort(maps, ((o1, o2) -> (Long.compare(o1.getId(), o2.getId()) )));
            }
            return;
        }

        if (sort.equals("name")) {
            if (order.equals("desc")) {
                Collections.sort(maps, ((o1, o2) -> (o2.getName().compareToIgnoreCase(o1.getName()))));
            } else {
                Collections.sort(maps, ((o1, o2) -> (o1.getName().compareToIgnoreCase(o2.getName()))));
            }
            return;
        }
    }
}
