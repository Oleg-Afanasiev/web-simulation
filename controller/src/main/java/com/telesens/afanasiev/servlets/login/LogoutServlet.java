package com.telesens.afanasiev.servlets.login;

import com.telesens.afanasiev.User;
import com.telesens.afanasiev.servlets.PersistServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@WebServlet(name="LogoutServlet", urlPatterns = {"/logout"}, loadOnStartup = 0)
public class LogoutServlet extends PersistServlet {
    protected static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doLogoutAction(req, resp);
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer);
    }

    private void doLogoutAction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            User u = (User)session.getAttribute("activeUser");

            if (u != null)
                logger.info("logout user: " + u.getUserName());
            else
                logger.info("logout action");

            session.invalidate();
        }
        removeUserFromCookie(req, resp);
        req.setAttribute("errorMessage", "You have been logged out!");
    }

    private void removeUserFromCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie[] cookies = req.getCookies();

        for (int i =0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                cookies[i].setMaxAge(0);
                cookies[i].setValue(null);
                resp.addCookie(cookies[i]);
                break;
            }
        }
    }
}
