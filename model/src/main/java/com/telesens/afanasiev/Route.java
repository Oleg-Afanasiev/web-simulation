package com.telesens.afanasiev;

import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
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
