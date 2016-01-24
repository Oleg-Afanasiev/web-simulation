package com.telesens.afanasiev.servlets.arc;

import com.telesens.afanasiev.*;
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
 * Created by oleg on 1/19/16.
 */
@WebServlet (name="ArcListServlet", urlPatterns = "/arc/list", loadOnStartup = 0)
public class ArcListServlet extends PersistServlet {
    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        List<Arc<Station>> arcs = getAllArcs(sort, order);
        req.setAttribute("arcs", arcs);
        req.getRequestDispatcher("/jsp/arc/arcList.jsp").forward(req, resp);
    }

    private List<Arc<Station>> getAllArcs(String sort, String order) {
        DaoManager daoManager = DaoManager.getInstance();
        ArcDAO arcDAO = daoManager.getArcDAO();

        List<Arc<Station>> arcs = new ArrayList<>();
        try {
            arcs = new ArrayList<>(arcDAO.getRange(0, 1000));
        } catch (DaoException exc) {
            logger.debug("Can't get range stations from db");
            exc.printStackTrace();
        }
        if (sort != null && order != null)
            sortArcs(arcs, sort, order);

        return arcs;
    }

    private void sortArcs(List<Arc<Station>> arcs, String sort, String order) {

        if (sort.equals("id")) {
            if (order.equals("desc")) {
                Collections.sort(arcs, ((o1, o2) -> (Long.compare(o2.getId(), o1.getId()) )));
            } else {
                Collections.sort(arcs, ((o1, o2) -> (Long.compare(o1.getId(), o2.getId()) )));
            }
            return;
        }

        if (sort.equals("nodeleft")) {
            if (order.equals("desc")) {
                Collections.sort(arcs, ((o1, o2) -> (o2.getNodeLeft().getName().compareToIgnoreCase(o1.getNodeLeft().getName()) )));
            } else {
                Collections.sort(arcs, ((o1, o2) -> (o1.getNodeLeft().getName().compareToIgnoreCase(o2.getNodeLeft().getName()) )));
            }
            return;
        }

        if (sort.equals("noderight")) {
            if (order.equals("desc")) {
                Collections.sort(arcs, ((o1, o2) -> (o2.getNodeRight().getName().compareToIgnoreCase(o1.getNodeRight().getName()) )));
            } else {
                Collections.sort(arcs, ((o1, o2) -> (o1.getNodeRight().getName().compareToIgnoreCase(o2.getNodeRight().getName()) )));
            }
            return;
        }

        if (sort.equals("duration")) {
            if (order.equals("desc")) {
                Collections.sort(arcs, ((o1, o2) -> (Integer.compare(o2.getDuration(), o1.getDuration()) )));
            } else {
                Collections.sort(arcs, ((o1, o2) -> (Integer.compare(o1.getDuration(), o2.getDuration()) )));
            }
            return;
        }
    }
}
