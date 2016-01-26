package com.telesens.afanasiev;

import com.telesens.afanasiev.impl.jdbc.*;
import com.telesens.afanasiev.impl.jdbc.relation.RelMapRouteDAOImpl;
import com.telesens.afanasiev.impl.jdbc.relation.RelRouteArcDAOImpl;
import org.apache.commons.dbcp2.BasicDataSource;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by oleg on 1/16/16.
 */
public class DaoManager {
    private static BasicDataSource connectionPool;
    private static ThreadLocal<DaoManager> daoManagerThreadLocal = new ThreadLocal<>();

    private Connection connection;
    private MapDAO mapDAO;
    private UserDAO userDAO;
    private StationDAO stationDAO;
    private ArcDAO arcDAO;
    private RouteDAO routeDAO;
    private RelRouteArcDAOImpl relRouteArcDAO;
    private RoutePairDAO routePairDAO;
    private RelMapRouteDAOImpl relMapRouteDAO;

    static {
        String username;
        String password;
        String dbUrl;
        String driver;
        String dbUrlFromEnv = System.getenv("DATABASE_URL"); // It is convenient to use this on Heroku platform

        if (dbUrlFromEnv != null) {
            try {
                URI dbUri = new URI(dbUrlFromEnv);
                username = dbUri.getUserInfo().split(":")[0];
                password = dbUri.getUserInfo().split(":")[1];
                dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
                driver = "org.postgresql.Driver";
            } catch (URISyntaxException e) {
                throw new DaoException("can't get Database URL from Env variable.", e);
            }
        } else {
            DaoProperties daoProperties = new DaoProperties();
            username = daoProperties.getProperty("user");
            password = daoProperties.getProperty("password");
            dbUrl = daoProperties.getProperty("url");
            driver = daoProperties.getProperty("driver");
        }
        connectionPool = new BasicDataSource();
        connectionPool.setUsername(username);
        connectionPool.setPassword(password);
        connectionPool.setDriverClassName(driver);
        connectionPool.setUrl(dbUrl);
        connectionPool.setInitialSize(10);
        connectionPool.setMaxTotal(20);
    }

    private DaoManager() {
        try {
            this.connection = connectionPool.getConnection();
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Can't get connection from pool.", e);
        }
    }

    public static synchronized DaoManager getInstance() {
        if (daoManagerThreadLocal.get() == null) {
            daoManagerThreadLocal.set(new DaoManager());
        }
        return daoManagerThreadLocal.get();
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        try {
            this.connection.commit();
            this.connection.close();
            daoManagerThreadLocal.remove();
        } catch (SQLException exc) {
            throw new DaoException("Cannot close connection", exc);
        }
    }

    public UserDAO getUserDAO() {
        if (this.userDAO == null) {
            this.userDAO = new UserDAOImpl();
        }
        return this.userDAO;
    }

    public MapDAO getMapDAO() {
        if (this.mapDAO == null)
            mapDAO = new MapDAOImpl();

        return mapDAO;
    }

    public StationDAO getStationDAO() {
        if (this.stationDAO == null)
            this.stationDAO = new StationDAOImpl();

        return this.stationDAO;
    }

    public ArcDAO getArcDAO() {
        if (this.arcDAO == null)
            this.arcDAO = new ArcDAOImpl();

        return this.arcDAO;
    }

    public RouteDAO getRouteDAO() {
        if (this.routeDAO == null)
            this.routeDAO = new RouteDAOImpl();

        return this.routeDAO;
    }

    public RelRouteArcDAOImpl getRelRouteArcDAO() {
        if (this.relRouteArcDAO == null)
            this.relRouteArcDAO = new RelRouteArcDAOImpl(connection);

        return this.relRouteArcDAO;
    }

    public RoutePairDAO getRoutePairDAO() {
        if (this.routePairDAO == null)
            this.routePairDAO = new RoutePairDAOImpl(connection);

        return this.routePairDAO;
    }

    public RelMapRouteDAOImpl getRelMapRouteDAO() {
        if (this.relMapRouteDAO == null)
            this.relMapRouteDAO = new RelMapRouteDAOImpl(connection);

        return this.relMapRouteDAO;
    }
}
