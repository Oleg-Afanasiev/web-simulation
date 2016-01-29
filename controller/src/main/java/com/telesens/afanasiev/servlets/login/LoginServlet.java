package com.telesens.afanasiev.servlets.login;

import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.User;
import com.telesens.afanasiev.UserDAO;
import com.telesens.afanasiev.servlets.PersistServlet;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Authentication Servlet
 *
 * @author Oleg  Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class LoginServlet extends PersistServlet {
    protected static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.setAttribute("sameUser", getSameUser());

        doLoginAction(req, resp);
    }

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("sameUser", getSameUser());
        HttpSession session = req.getSession(false);
        if (session != null)
            session.invalidate();

        req.getRequestDispatcher("/jsp/login/login.jsp").forward(req, resp);
    }


    private void doLoginAction(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        RequestDispatcher requestDispatcher;

        String login = req.getParameter("input-login");
        String password = req.getParameter("input-password");
        String remember = req.getParameter("remember");

        req.setAttribute("login", login);
        req.setAttribute("password", password);
        req.setAttribute("remember", remember);

        if (login == null || password == null) {
            req.setAttribute("errorMessage", "Empty login / password!");
            requestDispatcher = req.getRequestDispatcher("/jsp/login/login.jsp");

        } else if (!login.matches("[A-Za-z_0-9@.]{2,30}$") ||
                !password.matches("[A-Za-z_0-9]{4,16}")) {
            req.setAttribute("errorMessage", "Wrong login / password");
            requestDispatcher = req.getRequestDispatcher("/jsp/login/login.jsp");
        } else {
            HttpSession session = req.getSession();
            User user = getUser(login, password);

            if (user == null) {
                logger.info("Unsuccessful attempt login action");
                req.setAttribute("errorMessage", "Wrong login / password!");
                requestDispatcher = req.getRequestDispatcher("/jsp/login/login.jsp");
            } else {
                logger.info("login user: " + user.getUserName());
                session.setAttribute("activeUser", user);
                session.setMaxInactiveInterval(30 * 60);
                req.setAttribute("loginMessage", user.getUserName() + " - you have been successfully logged in!");

                saveUserToCookie(req, resp, user);

                req.getRequestDispatcher("/jsp/index.jsp").forward(req, resp);

                return;
            }
        }

        requestDispatcher.forward(req, resp);
    }

    private User getUser(String login, String password) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        List<User> users = (List<User>) userDAO.getRange(0, 100);

        for (User u: users) {
            if (    (u.getEmail().equals(login) || u.getUserName().equals(login)) &&
                    u.getPassword().equals(password)) {
                return u;
            }
        }

        return null;
    }

    private User getSameUser() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        List<User> users = (List<User>)userDAO.getRange(0, 1);

        if (users.size() > 0)
            return users.get(0);
        else
            return null;
    }

    private void saveUserToCookie(HttpServletRequest req, HttpServletResponse resp, User user) {
        String remember = req.getParameter("remember");
        if (remember != null) {
            Cookie c = new Cookie("userId", user.getId() + "");
            c.setMaxAge(24 * 60 * 60);
            resp.addCookie(c);
        }
    }
}
