package com.telesens.afanasiev;

/**
 * Created by oleg on 1/20/16.
 */
public interface Arc<T extends Identity> extends Identity {
    T getNodeLeft();
    T getNodeRight();
    int getDuration();

    void setNodeLeft(T nodeLeft);
    void setNodeRight(T nodeRight);
    void setDuration(int duration);

    boolean containsNode(T node);
    T getOppositeNode(T node);
}
