package com.telesens.afanasiev;

import java.util.Collection;

/**
 * Created by oleg on 1/22/16.
 */
public interface RouteDAO extends AbstractDAO<Route<Station>> {
    Collection<Route<Station>> getRangeNotPaired(long from , long size);
    Collection<RoutePair<Station>> getRangePair(long from, long size);
    Collection<Route<Station>> getRangeNotPairedNotInMap(long from, long size, long mapId);
    Collection<RoutePair<Station>> getRangePairNotInMap(long from, long size, long mapId);
}
