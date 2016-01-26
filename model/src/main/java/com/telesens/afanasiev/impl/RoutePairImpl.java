package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.Route;
import com.telesens.afanasiev.RoutePair;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by oleg on 1/25/16.
 */
@NoArgsConstructor
@Getter
public class RoutePairImpl<T extends Identity> implements RoutePair<T>, Serializable {
    private static final long serialVersionUID = 1L;

    private Route<T> forwardRoute;
    private Route<T> backRoute;

    public RoutePairImpl(Route<T> forwardRoute, Route<T> backRoute) {
        setForwardRoute(forwardRoute);
        setBackRoute(backRoute);
    }

    @Override
    public void setForwardRoute(Route<T> forwardRoute) {
        if (backRoute != null && forwardRoute.getId() == backRoute.getId())
            throw new IllegalArgumentException("Trying to assign equals forward and back routes in pair");

        this.forwardRoute = forwardRoute;
    }

    @Override
    public void setBackRoute(Route<T> backRoute) {
        if (forwardRoute != null && backRoute == forwardRoute)
            throw new IllegalArgumentException("Trying to assign equals forward and back routes in pair");

        this.backRoute = backRoute;
    }
}
