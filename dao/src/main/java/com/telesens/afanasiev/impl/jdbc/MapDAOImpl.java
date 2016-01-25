package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.MapImpl;
import com.telesens.afanasiev.impl.RouteImpl;
import com.telesens.afanasiev.impl.jdbc.relation.RelRouteArcDAOImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/16/16.
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

        Long mapId = rs.getLong("map_id");
        Map map = new MapImpl();

        map.setName(rs.getString("name"));
        map.setDescribe(rs.getString("describe"));
        DaoUtils.setPrivateField(map, "id", mapId);

        // get rel identities

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
    protected void saveRelations(Map route)
            throws SQLException {

        // rel table
//        DaoManager daoManager = DaoManager.getInstance();
//        StationDAO stationDAO = daoManager.getStationDAO();
//        ArcDAO arcDAO = daoManager.getArcDAO();
//        RelRouteArcDAOImpl relRouteArcDAO = daoManager.getRelRouteArcDAO();
//
//        relRouteArcDAO.delete(route.getId());
//        stationDAO.insertOrUpdate(route.getFirstNode());
//        for (Arc<Station> arc : route.getSequenceArcs()) {
//            arcDAO.insertOrUpdate(arc);
//            relRouteArcDAO.insert(route.getId(), arc.getId());
//        }
    }
}
