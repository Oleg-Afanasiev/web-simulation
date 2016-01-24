package com.telesens.afanasiev.servlets.login;

import com.telesens.afanasiev.DaoManager;
import com.telesens.afanasiev.User;
import com.telesens.afanasiev.UserDAO;
import com.telesens.afanasiev.impl.UserImpl;
import com.telesens.afanasiev.servlets.PersistServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by oleg on 1/19/16.
 */
public class SignUpServlet extends PersistServlet {
    protected static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private String paramNameError;
    private String msgError;

    @Override
    protected void doGetInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/jsp/login/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPostInPersistentCtx(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (createUserSuccessful(req))
            resp.sendRedirect("/login");
        else {
            req.setAttribute("paramNameErr", paramNameError);
            req.setAttribute("msgErr", msgError);
            req.getRequestDispatcher("/jsp/login/signup.jsp").forward(req, resp);
        }
    }

    private boolean createUserSuccessful(HttpServletRequest req) {
        String userName = req.getParameter("userName").trim();
        String firstName = req.getParameter("firstName").trim();
        String lastName = req.getParameter("lastName").trim();
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password").trim();
        String passwordRepeat = req.getParameter("passwordRepeat").trim();

        req.setAttribute("userName", userName);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);
        req.setAttribute("email", email);
        req.setAttribute("password", password);
        req.setAttribute("passwordRepeat", passwordRepeat);

        if (!isValidParam(userName)) {
            paramNameError = "userNameErr";
            msgError = "Incorrect format of 'User name'";
            return false;
        }

        if (!isValidParam(firstName)) {
            paramNameError = "firstNameErr";
            msgError = "Incorrect format of 'First name'";
            return false;
        }

        if (!isValidParam(lastName)) {
            paramNameError = "lastNameErr";
            msgError = "Incorrect format of 'Last name'";
            return false;
        }

        if (!isValidParam(email)) {
            paramNameError = "emailErr";
            msgError = "Incorrect format of 'Email";
            return false;
        }

        if (!isValidPassword(password)) {
            paramNameError = "passwErr";
            msgError = "Incorrect password";
            return false;
        }

        if (!isValidPassword(passwordRepeat)) {
            paramNameError = "passwRepeatErr";
            msgError = "Incorrect password";
            return false;
        }

        if (!password.equals(passwordRepeat)) {
            paramNameError = "passwNotEqErr";
            msgError = "Not equal passwords";
            return false;
        }

        if (existUser(userName, email)) {
            paramNameError = "existUser";
            msgError = "User with same 'user name' or 'email' is already registered!";
            return false;
        }

        User user = new UserImpl();
        user.setUserName(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        UserDAO userDAO = DaoManager.getInstance().getUserDAO();
        userDAO.insertOrUpdate(user);

        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 4)
            return false;

        return true;
    }

    private boolean isValidParam(String param) {
        if (param == null || param.isEmpty())
            return false;

        return true;
    }

    private boolean existUser(String userName, String email) {
        DaoManager daoManager = DaoManager.getInstance();
        UserDAO userDAO = daoManager.getUserDAO();

        Collection<User> users = userDAO.getRange(0, 1000);
        for (User u : users) {
            if (u.getUserName().equals(userName) || u.getEmail().equals(email))
                return true;
        }

        return false;
    }
}
