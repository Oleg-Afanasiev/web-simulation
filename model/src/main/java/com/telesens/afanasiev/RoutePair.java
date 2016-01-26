package com.telesens.afanasiev;

/**
 * Created by oleg on 1/25/16.
 */
public interface RoutePair<T extends Identity> {
    Route<T> getForwardRoute();
    Route<T> getBackRoute();

    void setForwardRoute(Route<T> forwardRoute);
    void setBackRoute(Route<T> backRoute);
}
