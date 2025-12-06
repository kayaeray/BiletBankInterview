package com.flightproviderconsumer.exceptions;

public class FlightProviderUnavailableException extends FlightException {
    public FlightProviderUnavailableException(String providerName) {
        super(providerName + " is not reachable!");
    }
}