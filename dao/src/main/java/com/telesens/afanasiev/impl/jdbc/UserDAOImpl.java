package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.AbstractDAO;
import com.telesens.afanasiev.User;
import com.telesens.afanasiev.UserDAO;
import com.telesens.afanasiev.impl.DaoUtils;
import com.telesens.afanasiev.impl.UserImpl;
import lombok.NoArgsConstructor;

import java.sql.*;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@NoArgsConstructor
public class UserDAOImpl extends GenericDAO<User> implements UserDAO, AbstractDAO<User> {
    private static final String queryGetById =
            "SELECT user_id, username, last_name, first_name, email, created, updated, password " +
            "FROM bts.user " +
            "WHERE user_id = ? " +
            "and is_deleted = FALSE ;";

    private static final String queryGetRange =
            "SELECT * FROM bts.user WHERE is_deleted = FALSE ORDER BY user_id LIMIT ? offset ? ;";

    private static final String queryInsert =
            "INSERT INTO bts.user (username, last_name, first_name, email, created, updated, password, is_deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, FALSE) RETURNING user_id;";

    private static final String queryUpdate =
            "UPDATE bts.user " +
                    "SET (username, last_name, first_name, email, created, updated, password, is_deleted) = " +
                    "(?, ?, ?, ?, ?, ?, ?, FALSE) " +
                    "WHERE user_id = ? ;";

    private static final String queryDelete =
            "UPDATE bts.user SET (is_deleted) = (TRUE) WHERE user_id = ? ;";

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
    protected void saveRelations(User entity) throws SQLException {}


    @Override
    protected void setParamsForInsertOrUpdate(PreparedStatement statement, User entity)
        throws SQLException {

        Long id = entity.getId();
        statement.setString(1, entity.getUserName());
        statement.setString(2, entity.getLastName());
        statement.setString(3, entity.getFirstName());
        statement.setString(4, entity.getEmail());
        statement.setTimestamp(5, new Timestamp(entity.getCreated().getTime()));
        statement.setTimestamp(6, new Timestamp(entity.getUpdated().getTime()));
        statement.setString(7, entity.getPassword());
        if (id != 0) // in case update of entity
            statement.setLong(8, entity.getId());
    }

    @Override
    protected User createFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException {
        User user = new UserImpl();

        DaoUtils.setPrivateField(user, "id", rs.getLong("user_id"));
        user.setUserName(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setCreated(rs.getDate("created"));
        user.setUpdated(rs.getDate("updated"));

        return user;
    }

}
