package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.RouteImpl;
import com.telesens.afanasiev.impl.RoutePairImpl;
import com.telesens.afanasiev.impl.jdbc.relation.RelRouteArcDAOImpl;
import org.omg.CORBA.DATA_CONVERSION;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/22/16.
 */
public class RouteDAOImpl extends GenericDAO<Route<Station>> implements RouteDAO {
    private static final String queryInsert =
            "INSERT INTO bts.route (number, describe, cost, first_node_id, is_deleted) " +
                    "VALUES (?, ?, ?, ?, FALSE) RETURNING route_id; ";

    private static final String queryUpdate =
            "UPDATE bts.route SET (number, describe, cost, first_node_id, is_deleted) = " +
                    "(?, ?, ?, ?, FALSE) " +
                    "WHERE route_id = ?;";

    private static final String queryGetById =
            "SELECT route_id, number, describe, cost, first_node_id  " +
                    "FROM bts.route " +
                    "WHERE route_id = ? " +
                    "and is_deleted = FALSE ;";

    private static final String queryDelete =
            "UPDATE bts.route SET (is_deleted) = (TRUE) WHERE route_id = ? ;";

    private static final String queryGetRange =
            "SELECT * FROM bts.route WHERE is_deleted = FALSE ORDER BY route_id LIMIT ? offset ? ; ";

    private static final String queryGetRangeNotPaired =
            "SELECT * " +
                    "FROM bts.route " +
                    "WHERE is_deleted = (FALSE) " +
                    "AND route_id NOT IN" +
                    "(SELECT route_forw_id FROM bts.route_pair) " +
                    "AND route_id NOT IN " +
                    "(SELECT route_back_id FROM bts.route_pair) " +
                    "ORDER BY route_id " +
                    "LIMIT ? offset ? ; ";

    private static String queryGetRangeNotPairedNotInMap =
            "SELECT route_id " +
                    "FROM bts.route " +
                    "WHERE is_deleted = (FALSE) " +
                    "AND route_id NOT IN " +
                    "(SELECT route_id FROM bts.map_route WHERE map_id = ? ) " +
                    "AND route_id NOT IN " +
                    "(SELECT route_forw_id FROM bts.route_pair) " +
                    "AND route_id NOT IN " +
                    "(SELECT route_back_id FROM bts.route_pair) " +
                    "ORDER BY route_id " +
                    "LIMIT ? offset ? ; ";

    private static String queryGetRangePairedNotInMap =
            "SELECT route_forw_id, route_back_id " +
                    "FROM bts.route_pair " +
                    "WHERE route_forw_id NOT IN " +
                    "(SELECT route_id FROM bts.map_route WHERE map_id = ?) " +
                    "AND route_forw_id NOT IN " +
                    "(SELECT route_id FROM bts.route WHERE is_deleted = (TRUE))" +
                    "ORDER BY route_forw_id " +
                    "LIMIT ? offset ? ;";

    @Override
    public Collection<Route<Station>> getRangeNotPaired(long from , long size) {
        if(from < 0 || size < 1){
            throw new IllegalArgumentException("Please put positive values of arguments");
        }

        Connection connection = DaoManager.getInstance().getConnection();
        Collection<Route<Station>> routes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetRangeNotPaired)) {
            statement.setLong(1, size);
            statement.setLong(2, from);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                routes.add(createRouteFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find 'route'", exc);
        }

        return routes;
    }

    @Override
    public Collection<RoutePair<Station>> getRangePair(long from, long size) {
        DaoManager daoManager = DaoManager.getInstance();
        RoutePairDAO routePairDAO = daoManager.getRoutePairDAO();

        return routePairDAO.getRange(from, size);
    }

    @Override
    public Collection<Route<Station>> getRangeNotPairedNotInMap(long from, long size, long mapId) {

        if(from < 0 || size < 1){
            throw new IllegalArgumentException("Please put positive values of arguments");
        }

        Connection connection = DaoManager.getInstance().getConnection();
        Collection<Route<Station>> routes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetRangeNotPairedNotInMap)) {
            statement.setLong(1, mapId);
            statement.setLong(2, size);
            statement.setLong(3, from);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                routes.add(createRouteFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find 'route'", exc);
        }

        return routes;
    }

    @Override
    public Collection<RoutePair<Station>> getRangePairNotInMap(long from, long size, long mapId) {
        if(from < 0 || size < 1){
            throw new IllegalArgumentException("Please put positive values of arguments");
        }

        Connection connection = DaoManager.getInstance().getConnection();
        Collection<RoutePair<Station>> pairs = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetRangePairedNotInMap)) {
            statement.setLong(1, mapId);
            statement.setLong(2, size);
            statement.setLong(3, from);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                pairs.add(createPairFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find 'route'", exc);
        }

        return pairs;
    }

    @Override
    protected String getQueryById() {
        return queryGetById;
    }

    @Override
    protected String getQueryInsertOrUpdate(Long id) {
        return id == 0 ? queryInsert : queryUpdate;
    }

    @Override
    protected String getQueryDelete() {
        return queryDelete;
    }

    @Override
    protected String  getQueryGetRange() {
        return queryGetRange;
    }

    @Override
    protected Route<Station> createFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        Station firstNode = stationDAO.getById(rs.getLong("first_node_id"));

        Long routeId = rs.getLong("route_id");
        Route<Station> route = new RouteImpl<>();

        route.setNumber(rs.getString("number"));
        route.setDescription(rs.getString("describe"));
        route.setCost(rs.getBigDecimal("cost"));
        route.setFirstNode(firstNode);
        DaoUtils.setPrivateField(route, "id", routeId);

        RelRouteArcDAOImpl routeArcDAO = daoManager.getRelRouteArcDAO();
        Collection<Arc<Station>> relArcs = routeArcDAO.getAllByRoute(routeId);
        route.setSequenceArcs(relArcs);

        return route;
    }

    @Override
    protected void setParamsForInsertOrUpdate(PreparedStatement statement, Route<Station> route)
            throws SQLException {

        Long id = route.getId();
        statement.setString(1, route.getNumber());
        statement.setString(2, route.getDescription());
        statement.setBigDecimal(3, route.getCost());
        statement.setLong(4, route.getFirstNode().getId());

        if (id != 0) // in case update of entity
            statement.setLong(5, route.getId());
    }

    @Override
    protected void saveRelations(Route<Station> route)
        throws SQLException {

        // rel table
        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        ArcDAO arcDAO = daoManager.getArcDAO();
        RelRouteArcDAOImpl relRouteArcDAO = daoManager.getRelRouteArcDAO();

        relRouteArcDAO.delete(route.getId());
        stationDAO.insertOrUpdate(route.getFirstNode());
        for (Arc<Station> arc : route.getSequenceArcs()) {
            arcDAO.insertOrUpdate(arc);
            relRouteArcDAO.insert(route.getId(), arc.getId());
        }
    }

    private Route<Station> createRouteFromResultSet(ResultSet rs)
        throws SQLException, IllegalAccessException, NoSuchFieldException {

        long routeId = rs.getLong("route_id");
        Route route = getById(routeId);

        return route;
    }

    private RoutePair<Station> createPairFromResultSet(ResultSet rs)
        throws SQLException, IllegalAccessException, NoSuchFieldException {

        long routeForwId = rs.getLong("route_forw_id");
        long routeBackId = rs.getLong("route_back_id");

        Route<Station> routeForw = getById(routeForwId);
        Route<Station> routeBack = getById(routeBackId);

        RoutePair<Station> routePair = new RoutePairImpl<>(routeForw, routeBack);
        return routePair;
    }
}
