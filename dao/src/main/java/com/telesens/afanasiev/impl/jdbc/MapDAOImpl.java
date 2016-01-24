package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.Map;
import com.telesens.afanasiev.MapDAO;
import com.telesens.afanasiev.impl.MapImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/16/16.
 */
public class MapDAOImpl implements MapDAO {
    private static final String queryGetAll = "SELECT * FROM scheme.map ORDER BY map_id";
    private Connection connection;

    public MapDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public Collection<Map> getAll() {
        Collection<Map> maps = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetAll)) {
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                maps.add(createFromResultSet(rs));
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return maps;
    }

    private Map createFromResultSet(ResultSet resultSet) throws SQLException {
        Map map = new MapImpl();

        map.setName(resultSet.getString("name"));
        map.setDescribe(resultSet.getString("describe"));

        return map;
    }
}
