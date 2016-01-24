package com.telesens.afanasiev.impl.jdbc.relation;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.ArcImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/22/16.
 */
public class RelRouteArcDAOImpl {

    private Connection connection;
    private final String queryGetByRoute =
            "SELECT route_id, arc_id " +
                    "FROM bts.route_arc " +
                    "WHERE route_id = ? ;";

    private final String queryGetByArc =
            "SELECT route_id, arc_id " +
                    "FROM bts.route_arc " +
                    "WHERE arc_id = ? ;";

    private final String queryDelete =
            "DELETE " +
                    "FROM bts.route_arc " +
                    "WHERE route_id = ? ;";

    private final String queryInsert =
            "INSERT INTO bts.route_arc (route_id, arc_id) " +
                    "VALUES (?, ?); ";


    public RelRouteArcDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public Collection<Arc<Station>> getAllByRoute(Long routeId) {

        Collection<Arc<Station>> arcs = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetByRoute)) {
            statement.setLong(1, routeId);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                arcs.add(createArcFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return arcs;
    }

    public Collection<Route<Station>> getAllByArc(Long arcId) {
        Collection<Route<Station>> routes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetByArc)) {
            statement.setLong(1, arcId);
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

    public void insert(long routeId, long arcId)
            throws DaoException {

        try (PreparedStatement statement = connection.prepareStatement(queryInsert)) {
            statement.setLong(1, routeId);
            statement.setLong(2, arcId);
            statement.execute();
        } catch(SQLException  exc) {
            throw new DaoException("Can't find entity", exc);
        }
    }

    public void delete(long routeId)
            throws DaoException {

        try (PreparedStatement statement = connection.prepareStatement(queryDelete)) {
            statement.setLong(1, routeId);
            statement.execute();
        } catch(SQLException  exc) {
            throw new DaoException("Can't find entity", exc);
        }
    }

    private Arc<Station> createArcFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        DaoManager daoManager = DaoManager.getInstance();
        ArcDAO arcDAO = daoManager.getArcDAO();
        Long arcId = rs.getLong("arc_id");

        return arcDAO.getById(arcId);
    }

    private Route<Station> createRouteFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        Long routeId = rs.getLong("route_id");
        DaoManager daoManager = DaoManager.getInstance();
        RouteDAO routeDAO = daoManager.getRouteDAO();

        return routeDAO.getById(routeId);
    }
}
