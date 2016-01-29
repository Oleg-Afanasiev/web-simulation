package com.telesens.afanasiev.servlets.edit.pair;

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
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet (name="RoutePairListServlet", urlPatterns = "/pair/list", loadOnStartup = 0)
public class RoutePairListServlet extends PersistServlet {

    private String msgError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Collection<RoutePair<Station>> pairs = getAllPairRoutes(sort, order);
        req.setAttribute("pairs", pairs);
        req.getRequestDispatcher("/jsp/pair/routePairList.jsp").forward(req, resp);
    }

    private Collection<RoutePair<Station>> getAllPairRoutes(String sort, String order) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
        List<RoutePair<Station>> pairs;

        try {
            pairs = new ArrayList<>(routePairDAO.getRange(0, 1000));
        } catch(DaoException exc) {
            logger.debug("Can't get range pair of routes from db");
            exc.printStackTrace();
            return new ArrayList<>();
        }

        if (sort != null && order != null)
            sortRoutes(pairs, sort, order);

        return pairs;
    }

    private void sortRoutes(List<RoutePair<Station>> pairs, String sort, String order) {

        if (sort.equals("forw")) {
            if (order.equals("desc")) {
                Collections.sort(pairs, ((o1, o2) -> (o2.getForwardRoute().getNumber().compareTo(o1.getForwardRoute().getNumber())) ));
            } else {
                Collections.sort(pairs, ((o1, o2) -> (o1.getForwardRoute().getNumber().compareTo(o2.getForwardRoute().getNumber())) ));
            }
            return;
        }

        if (sort.equals("back")) {
            if (order.equals("desc")) {
                Collections.sort(pairs, ((o1, o2) -> (o2.getBackRoute().getNumber().compareTo(o1.getBackRoute().getNumber()) )));
            } else {
                Collections.sort(pairs, ((o1, o2) -> (o1.getBackRoute().getNumber().compareTo(o2.getBackRoute().getNumber()) )));
            }
            return;
        }
    }
}
