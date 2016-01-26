package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RoutePairImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/25/16.
 */
public class RoutePairDAOImpl implements RoutePairDAO {
    private Connection connection;

    private final String queryInsert =
            "INSERT INTO bts.route_pair (route_forw_id, route_back_id) " +
                    "VALUES (?, ?); " +
                    "RETURNING route_forw_id; ";

    private final String queryGetRange =
            "SELECT rf.route_id, rb.route_id , rp.route_forw_id, rp.route_back_id " +
                    "FROM bts.route_pair AS rp, bts.route AS rf, bts.route AS rb " +
                    "WHERE rf.route_id = rp.route_forw_id " +
                    "AND rb.route_id = rp.route_back_id " +
                    "AND rf.is_deleted = FALSE " +
                    "AND rb.is_deleted = FALSE " +
                    "ORDER BY route_forw_id LIMIT ? offset ? ; ";

    public RoutePairDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public void insert(RoutePair<Station> pair)
            throws DaoException {

        try (PreparedStatement statement = connection.prepareStatement(queryInsert)) {
            statement.setLong(1, pair.getForwardRoute().getId());
            statement.setLong(2, pair.getBackRoute().getId());
            statement.execute();
        } catch(SQLException  exc) {
            throw new DaoException("Can't find entity", exc);
        }
    }

    public Collection<RoutePair<Station>> getRange(long from, long size)
        throws DaoException {

        if(from < 0 || size < 1) {
            throw new IllegalArgumentException("Please put positive values of arguments");
        }

        Collection<RoutePair<Station>> routePairs = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetRange)) {
            statement.setLong(1, size);
            statement.setLong(2, from);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                routePairs.add(createFromResultSet(rs));
            }
        } catch(SQLException | NumberFormatException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return routePairs;
    }

//    public void delete(long routeId)
//            throws DaoException {
//
//        try (PreparedStatement statement = connection.prepareStatement(queryDelete)) {
//            statement.setLong(1, routeId);
//            statement.execute();
//        } catch(SQLException  exc) {
//            throw new DaoException("Can't find entity", exc);
//        }
//    }

    private RoutePair<Station> createFromResultSet(ResultSet rs)
            throws SQLException, NumberFormatException, IllegalAccessException, NoSuchFieldException {

        long routeForwId = rs.getLong("route_forw_id");
        long routeBackId = rs.getLong("route_back_id");

        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();
        Route<Station> routeForw = routeDAO.getById(routeForwId);
        Route<Station> routeBack = routeDAO.getById(routeBackId);

        RoutePair<Station> routePair = new RoutePairImpl<>(routeForw, routeBack);

        return routePair;
    }
}
