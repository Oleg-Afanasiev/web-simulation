package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Arc;
import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.SearchEngine;

import java.util.*;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class SearchEngineImpl<T extends Identity> implements SearchEngine<T> {

    private Collection<Arc<T>> arcs;
    private SortedMap<Integer, Collection<T>> seqResults;

    public SearchEngineImpl() {
    }

    @Override
    public Collection<T> searchFastestPath(Collection<Arc<T>> arcs, T nodeFrom, T nodeTo) {
        this.arcs = arcs;
        seqResults = new TreeMap<>();

        searchIter(new ArrayList<>(), nodeFrom, nodeTo);

        return seqResults.size() >0 ? seqResults.get(seqResults.firstKey()) : new ArrayList<>();
    }

    @Override
    public int getTotalDuration() {
        return seqResults != null && seqResults.size() > 0 ? seqResults.firstKey() : 0;
    }

    private void searchIter(Collection<T> tailNodes, T nodeFrom, T nodeTo) {
        int dur;
        tailNodes.add(nodeFrom);

        if (nodeFrom.getId() == nodeTo.getId()) {
            dur = getTotalDuration(tailNodes);
            seqResults.put(dur, tailNodes); // was found sequence
        }

        Collection<T> nextNodes = getNextNodes(tailNodes, nodeFrom);

        if (nextNodes.size() == 0) {
            return; // incorrect path
        }

        for (T node : nextNodes) {
            searchIter(new ArrayList<>(tailNodes), node, nodeTo);
        }
    }

    private Collection<T> getNextNodes(Collection<T> tailNodes, T nodeFrom) {
        Collection<T> nextNodes = new ArrayList<>();
        T oppositeNode;

        for (Arc<T> arc : arcs) {
            oppositeNode = arc.getOppositeNode(nodeFrom);
            if (oppositeNode != null)
                if (getNodeById(tailNodes, oppositeNode.getId()) == null) {
                    nextNodes.add(oppositeNode);
                }
        }

        return nextNodes;
    }

    private int getTotalDuration(Collection<T> seqNodes) {
        T prevNode = null;
        Arc<T> arc;
        int sum = 0;
        for (T node : seqNodes) {
            if (prevNode != null) {
                arc = getArcByNodes(prevNode, node);
                sum += arc.getDuration();
            }
            prevNode = node;
        }

        return sum;
    }

    private Arc<T> getArcByNodes(T node1, T node2) {
        for (Arc<T> arc : arcs) {
            if (arc.containsByIdOnly(node1) && arc.containsByIdOnly(node2)) {
                return arc;
            }
        }

        return null;
    }

    private T getNodeById(Collection<T> nodes, long id) {
        for (T node : nodes) {
            if (node.getId() == id)
                return node;
        }

        return null;
    }
}
