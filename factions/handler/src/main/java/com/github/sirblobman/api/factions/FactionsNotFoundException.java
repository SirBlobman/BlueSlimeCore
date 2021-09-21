package com.github.sirblobman.api.factions;

public final class FactionsNotFoundException extends RuntimeException {
    public FactionsNotFoundException() {
        super();
    }
    
    public FactionsNotFoundException(String message) {
        super(message);
    }
    
    public FactionsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
