package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by oleg on 1/16/16.
 */
@NoArgsConstructor
@Data
public class MapImpl extends IdentityImpl implements Map, Identity
{
    private static final long serialVersionUID = 1L;
    private String name;
    private String describe;
}
