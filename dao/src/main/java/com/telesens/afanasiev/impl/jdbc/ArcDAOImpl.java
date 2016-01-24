package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.ArcImpl;
import com.telesens.afanasiev.impl.DaoUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by oleg on 1/20/16.
 */
public class ArcDAOImpl extends GenericDAO<Arc<Station>> implements ArcDAO {
    private static final String queryInsert =
            "INSERT INTO bts.arc (node_left_id, node_right_id, duration) " +
            "VALUES (?, ?, ?) RETURNING arc_id; ";

    private static final String queryUpdate =
            "UPDATE bts.arc SET (node_left_id, node_right_id, duration) = " +
            "(?, ?, ?) " +
            "WHERE arc_id = ?;";

    private static final String queryGetById =
            "SELECT arc_id, node_left_id, node_right_id, duration " +
            "FROM bts.arc " +
            "WHERE arc_id = ?; ";

    private static final String queryDelete =
            "DELETE FROM bts.arc " +
            "WHERE arc_id = ? ;";

    private static final String queryGetRange =
            "SELECT * FROM bts.arc ORDER BY arc_id LIMIT ? offset ? ; ";

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
    protected Arc<Station> createFromResultSet(ResultSet rs)
        throws SQLException, IllegalAccessException, NoSuchFieldException {

        Arc<Station> arc = new ArcImpl<>();
        int nodeLeftId = rs.getInt("node_left_id");
        int nodeRightId = rs.getInt("node_right_id");
        Station nodeLeft;
        Station nodeRight;

        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();

        nodeLeft = stationDAO.getById(nodeLeftId);
        nodeRight = stationDAO.getById(nodeRightId);

        DaoUtils.setPrivateField(arc, "id", rs.getLong("arc_id"));
        arc.setNodeLeft(nodeLeft);
        arc.setNodeRight(nodeRight);
        arc.setDuration(rs.getInt("duration"));

        return arc;
    }

    @Override
    protected void setParamsForInsertOrUpdate(PreparedStatement statement, Arc<Station> entity)
            throws SQLException {

        Long id = entity.getId();
        statement.setLong(1, entity.getNodeLeft().getId());
        statement.setLong(2, entity.getNodeRight().getId());
        statement.setInt(3, entity.getDuration());

        if (id != 0) // in case update of entity
            statement.setLong(4, entity.getId());
    }

    @Override
    protected void saveRelations(Arc<Station> arc)
        throws SQLException {
        DaoManager daoManager = DaoManager.getInstance();
        StationDAO stationDAO = daoManager.getStationDAO();
        stationDAO.insertOrUpdate(arc.getNodeLeft());
        stationDAO.insertOrUpdate(arc.getNodeRight());
    }
}
