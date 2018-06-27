/**
 *
 * Project: dtre
 *
 * Copyright 2018 (c) DMG27 Ltd.
 *
 */
package com.dmg27.dtre.core;

/**
 * This exception indicates that a problem with the Daily Trade Reporting Engine has been detected.
 * 
 * @author Douglas McGee (dmg27i@gmail.com)
 */
public class DtreException extends RuntimeException {
    
    public DtreException(String message) {
        super(message);
    }
    
    public DtreException(String message, Throwable cause) {
        super(message, cause);
    }
}
