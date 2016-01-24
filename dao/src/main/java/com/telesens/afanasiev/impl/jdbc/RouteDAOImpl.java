package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.RouteImpl;
import com.telesens.afanasiev.impl.jdbc.relation.RelRouteArcDAOImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
