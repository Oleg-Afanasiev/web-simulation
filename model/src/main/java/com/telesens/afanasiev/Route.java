package com.telesens.afanasiev;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by oleg on 1/22/16.
 */
public interface Route<T extends Identity> extends Identity {
    String getNumber();
    String getDescription();
    BigDecimal getCost();
    T getFirstNode();
    T getLastNode();
    Collection<Arc<T>> getSequenceArcs();
    Collection<T> getSequenceNodes();

    void setNumber(String number);
    void setDescription(String description);
    void setCost(BigDecimal cost);
    void setFirstNode(T firstNode);

    void setSequenceArcs(Collection<Arc<T>> arcs);
}
