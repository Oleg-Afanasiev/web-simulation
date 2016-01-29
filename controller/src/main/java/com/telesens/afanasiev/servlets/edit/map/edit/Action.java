package com.telesens.afanasiev.servlets.edit.map.edit;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public interface Action extends ActError {
    boolean doSave(HttpServletRequest req);
    void clearParams(HttpServletRequest req);
}
