package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.Station;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@NoArgsConstructor
@Data
public class StationImpl extends IdentityImpl implements Station, Identity {
    private static final long serialVersionUID = 1L;
    private String name;

    @Override
    public String toString() {
        return "StationImpl{" +
                "id=" + super.getId() +
                ", name='" + name +
                '}';
    }
}
