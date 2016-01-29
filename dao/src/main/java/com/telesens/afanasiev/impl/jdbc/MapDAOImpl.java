package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.MapImpl;
import com.telesens.afanasiev.impl.jdbc.relation.RelMapRouteDAOImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class MapDAOImpl extends GenericDAO<Map> implements MapDAO {

    private static final String queryInsert =
            "INSERT INTO bts.map (name, describe, is_deleted) " +
                    "VALUES (?, ?, FALSE) RETURNING map_id; ";

    private static final String queryUpdate =
            "UPDATE bts.map SET (name, describe, is_deleted) = " +
                    "(?, ?, FALSE) " +
                    "WHERE map_id = ?;";

    private static final String queryGetById =
            "SELECT map_id, name, describe " +
                    "FROM bts.map " +
                    "WHERE map_id = ? " +
                    "and is_deleted = FALSE; ";

    private static final String queryDelete =
            "UPDATE bts.map " +
                    "SET (is_deleted) = (TRUE) " +
                    "WHERE map_id = ? ;";

    private static final String queryGetRange =
            "SELECT * FROM bts.map WHERE is_deleted = FALSE ORDER BY map_id LIMIT ? offset ? ; ";

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
    protected Map createFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        long mapId = rs.getLong("map_id");
        Map map = new MapImpl();

        map.setName(rs.getString("name"));
        map.setDescribe(rs.getString("describe"));
        DaoUtils.setPrivateField(map, "id", mapId);

        // get rel identities
        DaoFactory daoFactory = DaoFactory.getInstance();
        RelMapRouteDAOImpl relMapRouteDAO = daoFactory.getRelMapRouteDAO();
        Collection<Route<Station>> cirRoutes = relMapRouteDAO.getNotPaireByMap(mapId);
        Collection<RoutePair<Station>> pairs = relMapRouteDAO.getPairedByMap(mapId);

        try {
            for (Route<Station> route : cirRoutes)
                map.registerCircularRoute(route);

            for (RoutePair<Station> pair : pairs)
                map.registerSimpleRoute(pair);
        } catch(IllegalArgumentException exc) {
            logger.debug("Can't get 'map' with specified ID. id = "  + mapId);
            throw new DaoException("Error in creating 'map' with specified ID. id = " + mapId);
        }

        return map;
    }

    @Override
    protected void setParamsForInsertOrUpdate(PreparedStatement statement, Map map)
            throws SQLException {

        Long id = map.getId();
        statement.setString(1, map.getName());
        statement.setString(2, map.getDescribe());

        if (id != 0) // in case update of entity
            statement.setLong(3, map.getId());
    }

    @Override
    protected void saveRelations(Map map)
            throws SQLException {

        // rel table
        DaoFactory daoFactory = DaoFactory.getInstance();

        RelMapRouteDAOImpl relMapRouteDAO = daoFactory.getRelMapRouteDAO();
        relMapRouteDAO.delete(map.getId());

        RouteDAO routeDAO = daoFactory.getRouteDAO();

        for (Route<Station> route : map.getCircularRoutes()) {
            routeDAO.insertOrUpdate(route);
            relMapRouteDAO.insert(map.getId(), route.getId());
        }

        RoutePairDAO routePairDAO = daoFactory.getRoutePairDAO();
        for (RoutePair<Station> pair : map.getPairsRoutes()) {
            routeDAO.insertOrUpdate(pair.getForwardRoute());
            routeDAO.insertOrUpdate(pair.getBackRoute());

            relMapRouteDAO.insert(map.getId(), pair.getForwardRoute().getId());
            relMapRouteDAO.insert(map.getId(), pair.getBackRoute().getId());

            try {
                routePairDAO.getById(pair.getForwardRoute().getId(), pair.getBackRoute().getId());
            } catch(DaoException exc) {
                routePairDAO.insert(pair);
            }

        }
    }
}
