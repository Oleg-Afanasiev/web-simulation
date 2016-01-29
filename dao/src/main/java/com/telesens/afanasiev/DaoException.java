package com.telesens.afanasiev;

/**
 *
 * @author  Oleg Afanasiev <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable e) {
        super(message, e);
    }

    public DaoException(String message) {
        super(message);
    }
}
