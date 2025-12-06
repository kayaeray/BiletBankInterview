package com.flightproviderconsumer.exceptions;

public class FlightProviderBusinessException extends FlightException {
    public FlightProviderBusinessException(String providerName, String reason) {
        super("Error from " + providerName + ": " + reason);
    }
}