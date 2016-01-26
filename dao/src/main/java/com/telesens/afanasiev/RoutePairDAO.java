package com.telesens.afanasiev;

import java.util.Collection;

/**
 * Created by oleg on 1/25/16.
 */
public interface RoutePairDAO {
    void insert(RoutePair<Station> pair);
    //void delete(Long id);
    //void deleteAll(Collection<? extends T> entities);

    /**
     *
     * @param from row number, min value 0
     * @param size count of rows to select shall be grater then 0
     * @return Collection of Entities
     */
    Collection<RoutePair<Station>> getRange(long from, long size);
}
