package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.Station;
import com.telesens.afanasiev.StationDAO;;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.StationImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by oleg on 1/20/16.
 */
public class StationDAOImpl extends GenericDAO<Station> implements StationDAO {
    private static final String queryGetById =
            "SELECT station_id, name " +
                    "FROM bts.station " +
                    "WHERE station_id = ? ;";

    private static final String queryGetRange =
            "SELECT * FROM bts.station ORDER BY station_id LIMIT ? offset ? ;";

    private static final String queryInsert =
            "INSERT INTO bts.station (name) " +
                    "VALUES (?) RETURNING station_id;";

    private static final String queryUpdate =
            "UPDATE bts.station " +
                    "SET (name) = " +
                    "(?) " +
                    "WHERE station_id = ? ;";

    private static final String queryDelete =
            "DELETE FROM bts.station WHERE station_id = ? ;";

    @Override
    protected String getQueryById() {
        return queryGetById;
    }

    @Override
    protected String getQueryGetRange() {
        return queryGetRange;
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
    protected void setParamsForInsertOrUpdate(PreparedStatement statement, Station entity)
            throws SQLException {

        Long id = entity.getId();
        statement.setString(1, entity.getName());

        if (id != 0) // in case update of entity
            statement.setLong(2, entity.getId());
    }

    @Override
    protected void saveRelations(Station entity) throws SQLException {}

    @Override
    protected Station createFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {

        Station station = new StationImpl();
        DaoUtils.setPrivateField(station, "id", rs.getLong("station_id"));
        station.setName(rs.getString("name"));

        return station;
    }
}
