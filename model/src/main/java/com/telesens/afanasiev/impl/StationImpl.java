package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.Station;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 1/20/16.
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
                ", nameame='" + name +
                '}';
    }
}
