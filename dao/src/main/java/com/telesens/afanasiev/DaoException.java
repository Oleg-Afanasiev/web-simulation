package com.telesens.afanasiev;

/**
 * Created by oleg on 1/16/16.
 */
public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable e) {
        super(message, e);
    }

    public DaoException(String message) {
        super(message);
    }
}
