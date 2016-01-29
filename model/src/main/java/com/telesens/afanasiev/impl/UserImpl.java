package com.telesens.afanasiev.impl;

import com.telesens.afanasiev.Identity;
import com.telesens.afanasiev.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
@NoArgsConstructor
@Data
public class UserImpl extends IdentityImpl implements User, Identity {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date created;
    private Date updated;

    @Override
    public String toString() {
        return "UserImpl{" +
                "id=" + super.getId() +
                ", userName='" + userName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", password=" + password +
                '}';
    }
}
