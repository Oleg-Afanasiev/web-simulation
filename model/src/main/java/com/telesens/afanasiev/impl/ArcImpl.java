package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Arc;
import com.telesens.afanasiev.Identity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@NoArgsConstructor
@Data
public class ArcImpl<T extends Identity> extends IdentityImpl implements Arc<T>, Identity {
    private static final long serialVersionUID = 1L;
    private T nodeLeft;
    private T nodeRight;
    private int duration;

    @Override
    public void setNodeLeft(T node) {
        if (node == null)
            throw new IllegalArgumentException("Trying to assign null node");

        if (nodeRight != null && nodeRight.getId() == node.getId())
            throw new IllegalArgumentException("Trying to assign equals nodes into arc");

        nodeLeft = node;
    }

    @Override
    public void setNodeRight(T node) {
        if (node == null)
            throw new IllegalArgumentException("Trying to assign null node");

        if (nodeLeft != null && nodeLeft.getId() == node.getId())
            throw new IllegalArgumentException("Trying to assign equals nodes into arc");

        nodeRight = node;
    }

    @Override
    public boolean containsNode(T node) {
        if (node == null || nodeLeft == null || nodeRight == null)
            return false;

        if (node.equals(nodeLeft))
            return true;

        if (node.equals(nodeRight))
            return true;

        return false;
    }

    @Override
    public boolean containsByIdOnly(T node) {
        if (node == null || nodeLeft == null || nodeRight == null)
            return false;

        if (node.getId() == nodeLeft.getId())
            return true;

        if (node.getId() == nodeRight.getId())
            return true;

        return false;
    }

    @Override
    public T getOppositeNode(T node) {
        if (node.getId() == nodeLeft.getId())
            return nodeRight;

        if (node.getId() == nodeRight.getId())
            return nodeLeft;

        return null;
    }

    @Override
    public String toString() {
        return "ArcImpl{" +
                "id=" + super.getId() +
                ", nodeLeft='" + nodeLeft + '\'' +
                ", nodeRight='" + nodeRight + '\'' +
                ", duration ='" + duration + '\'' +
                '}';
    }
}
