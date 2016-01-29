package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@NoArgsConstructor
public class IdentityImpl implements Identity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Override
    public long getId() {
        return id == null ? 0 : id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identity identity = (Identity) o;

        return id == identity.getId();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
