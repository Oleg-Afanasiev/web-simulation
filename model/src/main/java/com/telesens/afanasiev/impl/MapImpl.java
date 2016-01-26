package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/16/16.
 */
public class MapImpl extends IdentityImpl implements Map, Identity
{
    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String describe;

    @Getter
    private Collection<Route<Station>> circularRoutes;
    @Getter
    private Collection<RoutePair<Station>> pairsRoutes;

    public MapImpl() {
        circularRoutes = new ArrayList<>();
        pairsRoutes = new ArrayList<>();
    }

    @Override
    public void registerCircularRoute(Route<Station> route) {

        if (!isRegisteredRoute(route))
            circularRoutes.add(route);
    }

    @Override
    public void registerSimpleRoute(Route<Station> routeForward, Route<Station> routeBack) {
        RoutePair<Station> pair = new RoutePairImpl<>(routeForward, routeBack);
        registerSimpleRoute(pair);
    }

    @Override
    public void registerSimpleRoute(RoutePair<Station> pair) {
        if (!isRegisteredRoute(pair.getForwardRoute()) && !isRegisteredRoute(pair.getBackRoute())) {
            pairsRoutes.add(pair);
        }
    }

    private boolean isRegisteredRoute(Route<Station> route){
        for (Route<Station> routeBuff : circularRoutes)
            if (route.getId() == routeBuff.getId())
                return true;

        for (RoutePair<Station> pair : pairsRoutes)
            if (pair.getForwardRoute().getId() == route.getId() ||
                    pair.getBackRoute().getId() == route.getId())
                return true;

        return false;
    }
}
