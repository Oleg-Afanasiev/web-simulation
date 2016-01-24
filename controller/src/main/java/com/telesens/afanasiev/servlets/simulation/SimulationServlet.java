package com.telesens.afanasiev.servlets.simulation;

import com.telesens.afanasiev.servlets.PersistServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oleg on 1/19/16.
 */
public class SimulationServlet extends PersistServlet {
    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/jsp/simulation/simulation.jsp").forward(req, resp);
    }

    @Override
    protected  void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }
}
