package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
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

    private SearchEngine<Station> searchEngine;

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

    @Override
    public void clearCircularRoutes() {
        circularRoutes.clear();
    }


    @Override
    public void clearSimpleRoutes() {
        pairsRoutes.clear();
    }

    @Override
    public Collection<Arc<Station>> getAllArcs() {
        java.util.Map<Long, Arc<Station>> mapArc = new HashMap<>();

        for (Route<Station> route : circularRoutes) {
            for (Arc<Station> arc : route.getSequenceArcs()) {
                if (mapArc.get(arc.getId()) == null)
                    mapArc.put(arc.getId(), arc);
            }
        }

        for (RoutePair<Station> pair : pairsRoutes) {
            for (Arc<Station> arc : pair.getForwardRoute().getSequenceArcs()) {
                if (mapArc.get(arc.getId()) == null)
                    mapArc.put(arc.getId(), arc);
            }

            for (Arc<Station> arc : pair.getBackRoute().getSequenceArcs()) {
                if (mapArc.get(arc.getId()) == null)
                    mapArc.put(arc.getId(), arc);
            }
        }

        return new ArrayList<>(mapArc.values());
    }

    @Override
    public Collection<Route<Station>> getForwardRoutes() {
        Collection<Route<Station>> routes = new ArrayList<>();

        for (RoutePair<Station> pair : pairsRoutes) {
            routes.add(pair.getForwardRoute());
        }
        return routes;
    }

    @Override
    public Collection<Route<Station>> getBackRoutes() {
        Collection<Route<Station>> routes = new ArrayList<>();

        for (RoutePair<Station> pair : pairsRoutes) {
            routes.add(pair.getBackRoute());
        }
        return routes;
    }

    @Override
    public Collection<Station> getAllStations() {
        java.util.Map<Long, Station> stations = new TreeMap<>();

        for (Route<Station> route : circularRoutes) {
            for (Station node : route.getSequenceNodes()) {
                if (stations.get(node.getId()) == null)
                    stations.put(node.getId(), node);
            }
        }

        for (RoutePair<Station> pair : pairsRoutes) {
            for (Station node : pair.getForwardRoute().getSequenceNodes()) {
                if (stations.get(node.getId()) == null)
                    stations.put(node.getId(), node);
            }

            for (Station node : pair.getBackRoute().getSequenceNodes()) {
                if (stations.get(node.getId()) == null)
                    stations.put(node.getId(), node);
            }
        }

        return new ArrayList<>(stations.values());
    }

    @Override
    public Collection<Route<Station>> getRoutesAcrossNode(long nodeId) {
        Collection<Route<Station>> routes = new ArrayList<>();

        for (Route<Station> route : circularRoutes) {
            for (Station station : route.getSequenceNodes()) {
                if (station.getId() == nodeId)
                    routes.add(route);
            }
        }

        for (RoutePair<Station> pair : pairsRoutes) {
            for (Station station : pair.getForwardRoute().getSequenceNodes()) {
                if (station.getId() == nodeId)
                    routes.add(pair.getForwardRoute());
            }

            for (Station station : pair.getBackRoute().getSequenceNodes()) {
                if (station.getId() == nodeId)
                    routes.add(pair.getBackRoute());
            }
        }

        return routes;
    }

    @Override
    public Collection<Station> searchFastestPath(SearchEngine<Station> searchEngine, long nodeFromId, long nodeToId) {
        Station nodeFrom = getNodeById(nodeFromId);
        Station nodeTo = getNodeById(nodeToId);

        if (nodeFrom != null && nodeTo != null)
            return searchEngine.searchFastestPath(getAllArcs(), nodeFrom, nodeTo);
        return new ArrayList<>();
    }

    private Station getNodeById(long id) {
        for (Arc<Station> arc : getAllArcs()) {
            if (arc.getNodeLeft().getId() == id)
                return arc.getNodeLeft();

            if (arc.getNodeRight().getId() == id)
                return arc.getNodeRight();
        }

        return null;
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
