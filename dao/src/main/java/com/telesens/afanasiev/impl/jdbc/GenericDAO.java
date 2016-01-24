package com.telesens.afanasiev.impl.jdbc;

import com.telesens.afanasiev.*;
import com.telesens.afanasiev.impl.DaoUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/17/16.
 */
abstract public class GenericDAO<T extends Identity> implements AbstractDAO<T> {

    protected final Logger logger = LoggerFactory.getLogger(GenericDAO.class);
    @Override
    public void insertOrUpdate(T entity) {
        DaoManager daoManager = DaoManager.getInstance();
        Connection connection = daoManager.getConnection();
        Long id = entity.getId();
        String queryInsert = getQueryInsertOrUpdate(id);

        try (PreparedStatement statement = connection.prepareStatement(queryInsert)) {
            setParamsForInsertOrUpdate(statement, entity);
            boolean hasResultSet = statement.execute();
            ResultSet rs = statement.getResultSet();

            if (hasResultSet && rs != null && rs.next()) {
                id = rs.getLong(1);
                DaoUtils.setPrivateField(entity, "id", id);
            } else {
                int updateRowsCount = statement.getUpdateCount();
                if (updateRowsCount < 1) {
                    throw new DaoException("Entity wasn't updated");
                }
            }
            saveRelations(entity);
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't save or update entity", exc);
        }
    }

    @Override
    public T getById(long id) {
        DaoManager daoManager = DaoManager.getInstance();
        Connection connection = daoManager.getConnection();
        String queryGetId = getQueryById();
        T entity = null;

        try (PreparedStatement statement = connection.prepareStatement(queryGetId)) {
            statement.setLong(1, id);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            if (!rs.next()) {
                throw new DaoException("Entity with specified ID wasn't found. Id = " + id);
            }
            entity = createFromResultSet(rs);
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return entity;
    }

    @Override
    public void delete(Long id) {
        DaoManager daoManager = DaoManager.getInstance();
        Connection connection = daoManager.getConnection();

        if (id != null) {
            try (PreparedStatement statement = connection.prepareStatement(getQueryDelete())) {
                statement.setLong(1, id);
                statement.execute();
            } catch (SQLException exc) {
                throw new DaoException("Can't delete entity", exc);
            }
        } else
            throw new DaoException("Can't delete unsaved entity (id is NULL)");
    }

    @Override
    public void deleteAll(Collection<? extends T> entities) {
        for (T entity : entities) {
            delete(entity.getId());
        }
    }

    @Override
    public Collection<T> getRange(long from, long size) {
        if(from < 0 || size < 1){
            throw new IllegalArgumentException("Please put positive values of arguments");
        }

        DaoManager daoManager = DaoManager.getInstance();
        Connection connection = daoManager.getConnection();
        String queryGetRange = getQueryGetRange();
        Collection<T> entities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(queryGetRange)) {
            statement.setLong(1, size);
            statement.setLong(2, from);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                entities.add(createFromResultSet(rs));
            }
        } catch(SQLException | IllegalAccessException | NoSuchFieldException exc) {
            throw new DaoException("Can't find entity", exc);
        }

        return entities;
    }

    abstract protected String getQueryById();
    abstract protected String getQueryGetRange();
    abstract protected String getQueryInsertOrUpdate(Long id);
    abstract protected String getQueryDelete();

    abstract protected void saveRelations(T entity) throws SQLException;

    abstract protected void setParamsForInsertOrUpdate(PreparedStatement statement, T entity)
            throws SQLException;

    abstract protected T createFromResultSet(ResultSet rs)
            throws SQLException, IllegalAccessException, NoSuchFieldException;

}
