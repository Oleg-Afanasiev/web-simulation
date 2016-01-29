package com.telesens.afanasiev.impl.jdbc.relation;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.RoutePairImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class RelMapRouteDAOImpl {
    private Connection connection;
    private final String queryGetByMap =
            "SELECT map_id, route_id " +
                    "FROM bts.map_route " +
                    "WHERE map_id = ? ;";

    private final String queryGetByRoute =
            "SELECT map_id, route_id " +
                    "FROM bts.map_route " +
                    "WHERE route_id = ? ;";

    private final String queryGetNotPairedByMap =
            "SELECT route_id " +
                    "FROM bts.map_route " +
                    "WHERE map_id = ? " +
                    "AND route_id NOT IN " +
                    "(SELECT route_forw_id FROM bts.route_pair) " +
                    "AND route_id NOT IN " +
                    "(SELECT route_back_id FROM bts.route_pair) " +
                    "ORDER BY route_id ;";

    private final String queryGetPairedByMap =
            "SELECT route_forw_id, route_back_id " +
                    "FROM bts.route_pair " +
                    "WHERE route_forw_id IN " +
                    "(SELECT route_id FROM bts.map_route WHERE map_id = ? );";

    private final String queryDelete =
            "DELETE " +
                    "FROM bts.map_route " +
                    "WHERE map_id = ? ;";

    private final String queryInsert =
            "INSERT INTO bts.map_route (map_id, route_id) " +
                    "VALUES (?, ?); ";


    public RelMapRouteDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public Collection<Route<Station>> getAllByMap(long mapId) {
        Collection<Route<Station>> routes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetByMap)) {
            statement.setLong(1, mapId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                routes.add(createRouteFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return routes;
    }

    public Collection<Route<Station>> getNotPaireByMap(long mapId) {
        Collection<Route<Station>> routes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetNotPairedByMap)) {
            statement.setLong(1, mapId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                routes.add(createRouteFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return routes;
    }

    public Collection<RoutePair<Station>> getPairedByMap(long mapId) {
        Collection<RoutePair<Station>> pairs = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetPairedByMap)) {
            statement.setLong(1, mapId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                pairs.add(createPairFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return pairs;
    }

    public void insert(long mapId, long routeId)
            throws DaoException {

        try (PreparedStatement statement = connection.prepareStatement(queryInsert)) {
            statement.setLong(1, mapId);
            statement.setLong(2, routeId);
            statement.execute();
        } catch(SQLException  exc) {
            throw new DaoException("Can't find entity", exc);
        }
    }

    public void delete(long mapId)
            throws DaoException {

        try (PreparedStatement statement = connection.prepareStatement(queryDelete)) {
            statement.setLong(1, mapId);
            statement.execute();
        } catch(SQLException  exc) {
            throw new DaoException("Can't find entity", exc);
        }
    }

    private Route<Station> createRouteFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        long routeId = rs.getLong("route_id");
        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();

        return routeDAO.getById(routeId);
    }

    private RoutePair<Station> createPairFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException{

        long routeForwId = rs.getLong("route_forw_id");
        long routeBackId = rs.getLong("route_back_id");

        DaoFactory daoFactory = DaoFactory.getInstance();
        RouteDAO routeDAO = daoFactory.getRouteDAO();
        Route<Station> routeForw = routeDAO.getById(routeForwId);
        Route<Station> routeBack = routeDAO.getById(routeBackId);
        return new RoutePairImpl<>(routeForw, routeBack);
    }
}
