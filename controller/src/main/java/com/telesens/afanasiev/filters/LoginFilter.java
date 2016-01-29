package com.telesens.afanasiev.filters;

import com.telesens.afanasiev.DaoException;
import com.telesens.afanasiev.DaoFactory;
import com.telesens.afanasiev.User;
import com.telesens.afanasiev.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class LoginFilter implements Filter {
    private Map<String, String> paramValues;
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public void init(FilterConfig fConfig)
            throws ServletException {
        paramValues = new HashMap<>();
        Enumeration<String> paramNames = fConfig.getInitParameterNames();
        String nextName;
        while (paramNames.hasMoreElements()) {
            nextName = paramNames.nextElement();
            paramValues.put(nextName, fConfig.getInitParameter(nextName));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        User user = null;

        String userId = userIdFromCookie(req);
        if (userId != null) {
            user = getUserFromDB(Long.parseLong(userId));
            if (user != null) {
                session.setAttribute("activeUser", user);
            }
        }

        user = (User)session.getAttribute("activeUser");
        String reqURI = req.getRequestURI();
        if (user == null && !isSkippedURI(reqURI) &&
                reqURI.indexOf("css") == -1 && reqURI.indexOf("jpg") == -1) {
            logger.debug("Authorization access request");

            savePrevURL(req, resp);
            resp.sendRedirect("/login");
        } else
            chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isSkippedURI(String uri) {

        String paramValue;
        for (Map.Entry<String, String> entry : paramValues.entrySet()) {
            paramValue = entry.getValue();
            if (paramValue.equals(uri))
                return true;
        }
        return false;
    }

    private String userIdFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null)
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("userId"))
                    return cookies[i].getValue();
            }

        return null;
    }

    private User getUserFromDB(long id) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        UserDAO userDAO = daoFactory.getUserDAO();
        User user = null;
        try {
            user = userDAO.getById(id);
        } catch (DaoException exc) {
            logger.debug("User with specified ID wasn't found. id = " + id);
        }

        return user;
    }

    private void savePrevURL(HttpServletRequest req, HttpServletResponse resp) {
        Cookie c = new Cookie("prevURL", req.getRequestURL().toString());
        c.setMaxAge(2 * 60);
        resp.addCookie(c);
    }
}
