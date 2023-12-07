package org.generalka.storage;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3881335702567833226L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
