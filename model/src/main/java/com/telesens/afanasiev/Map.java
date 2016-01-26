package com.telesens.afanasiev;

import java.util.Collection;

/**
 * Created by oleg on 1/16/16.
 */
public interface Map extends Identity{
    String getName();
    String getDescribe();

    Collection<Route<Station>> getCircularRoutes();
    Collection<RoutePair<Station>> getPairsRoutes();

    void setName(String name);
    void setDescribe(String descr);

    void registerCircularRoute(Route<Station> circularRoute);
    void registerSimpleRoute(Route<Station> routeForward, Route<Station> routeBack);
    void registerSimpleRoute(RoutePair<Station> pair);
}
