package com.telesens.afanasiev;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public interface RoutePair<T extends Identity> {
    Route<T> getForwardRoute();
    Route<T> getBackRoute();

    void setForwardRoute(Route<T> forwardRoute);
    void setBackRoute(Route<T> backRoute);
}
