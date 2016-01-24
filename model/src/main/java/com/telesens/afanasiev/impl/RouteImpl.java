package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Arc;
import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.Route;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by oleg on 1/22/16.
 */
@NoArgsConstructor
@Data
public class RouteImpl<T extends Identity> extends IdentityImpl implements Route<T>, Identity {
    private static final long serialVersionUID = 1L;

    private String number;
    private String description;
    private BigDecimal cost;
    private T firstNode;
    private Collection<Arc<T>> sequenceArcs;

    @Override
    public void setFirstNode(T node) {
        if (node == null)
            throw new IllegalArgumentException("Trying to assign null node");

        firstNode = node;
    }

    @Override
    public void setCost(BigDecimal cost) {
        if (cost.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Trying to assign negative cost");

        this.cost = cost;
    }

    @Override
    public T getLastNode() {

        if (firstNode == null || sequenceArcs == null || sequenceArcs.size() == 0)
            return null;

        T lastNode = firstNode;

        for (Arc<T> arc : sequenceArcs) {
            lastNode = arc.getOppositeNode(lastNode);
        }

        return lastNode;
    }

    @Override
    public Collection<T> getSequenceNodes() {
        Collection<T> nodes = new ArrayList<>();

        if (firstNode == null)
            return nodes;

        nodes.add(firstNode);

        if (sequenceArcs == null)
            return nodes;

        T node = firstNode;
        for (Arc<T> arc : sequenceArcs) {
            node = arc.getOppositeNode(node);
            if (node != null) {
                nodes.add(node);
            } else {
                break;
            }
        }

        return nodes;
    }

    @Override
    public void setSequenceArcs(Collection<Arc<T>> separateArcs) {
        if (firstNode == null)
            throw new IllegalArgumentException("First node hasn't been initialized");

        sequenceArcs = new ArrayList<>();
        T node = firstNode;
        Arc<T> arc;
        while (separateArcs.size() > 0) {
            arc = removeArc(separateArcs, node);
            if (arc == null)
                throw new IllegalArgumentException("Trying to assign incorrect sequence of arcs");
            else {
                sequenceArcs.add(arc);
                node = arc.getOppositeNode(node);
            }
        }
    }

    private Arc<T> removeArc(Collection<Arc<T>> separateArcs, T node) {
        Arc<T> arcRes = null;

        for (Arc<T> arc : separateArcs) {
            if (arc.containsNode(node)) {
                arcRes = arc;
                break;
            }
        }

        separateArcs.remove(arcRes);
        return arcRes;
    }

}
