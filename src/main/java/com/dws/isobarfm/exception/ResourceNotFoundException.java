package com.dws.isobarfm.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceType, String id) {
        super(resourceType + " not found with id: " + id);
    }
}
