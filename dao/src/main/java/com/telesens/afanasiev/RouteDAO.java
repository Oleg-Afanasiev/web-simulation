package com.telesens.afanasiev;

import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public interface RouteDAO extends AbstractDAO<Route<Station>> {
    Collection<Route<Station>> getRangeNotPaired(long from , long size);
    Collection<RoutePair<Station>> getRangePair(long from, long size);
    Collection<Route<Station>> getRangeNotPairedNotInMap(long from, long size, long mapId);
    Collection<RoutePair<Station>> getRangePairNotInMap(long from, long size, long mapId);
}
