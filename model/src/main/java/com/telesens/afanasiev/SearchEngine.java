package com.telesens.afanasiev;

import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public interface SearchEngine<T extends Identity> {
    Collection<T> searchFastestPath(Collection<Arc<T>> arcs, T nodeFrom, T nodeTo);
    int getTotalDuration();
}
