package com.telesens.afanasiev;

import java.util.Date;

/**
 * Created by oleg on 1/17/16.
 */
public interface User extends Identity {
    String getUserName();
    String getPassword();
    String getFirstName();
    String getLastName();
    String getEmail();
    Date getCreated();
    Date getUpdated();

    void setUserName(String userName);
    void setPassword(String password);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setEmail(String email);
    void setCreated(Date created);
    void setUpdated(Date updated);

}
