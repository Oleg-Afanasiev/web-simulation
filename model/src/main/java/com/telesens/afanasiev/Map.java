package com.telesens.afanasiev;

import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public interface Map extends Identity{
    String getName();
    String getDescribe();

    Collection<Route<Station>> getCircularRoutes();
    Collection<Arc<Station>> getAllArcs();
    Collection<Station> getAllStations();
    Collection<RoutePair<Station>> getPairsRoutes();
    Collection<Route<Station>> getForwardRoutes();
    Collection<Route<Station>> getBackRoutes();
    Collection<Route<Station>> getRoutesAcrossNode(long nodeId);

    void setName(String name);
    void setDescribe(String descr);

    void registerCircularRoute(Route<Station> circularRoute);
    void registerSimpleRoute(Route<Station> routeForward, Route<Station> routeBack);
    void registerSimpleRoute(RoutePair<Station> pair);

    void clearCircularRoutes();
    void clearSimpleRoutes();


    Collection<Station> searchFastestPath(SearchEngine<Station> searchEngine, long nodeFromId, long nodeToId);
}
