package com.telesens.afanasiev;

import java.util.Collection;

/**
 * Created by oleg on 1/17/16.
 */
public interface AbstractDAO<T> {
    void insertOrUpdate(T entity);
    T getById(long id);
    void delete(Long id);
    void deleteAll(Collection<? extends T> entities);

    /**
     *
     * @param from row number, min value 0
     * @param size count of rows to select shall be grater then 0
     * @return Collection of Entities
     */
    Collection<T> getRange(long from, long size);
}
