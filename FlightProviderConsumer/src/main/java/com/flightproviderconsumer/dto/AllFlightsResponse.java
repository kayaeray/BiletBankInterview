package com.flightproviderconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllFlightsResponse {

    private boolean success;
    private String message;
    private int count;
    private List<UnifiedFlight> flights;

    public static AllFlightsResponse success(List<UnifiedFlight> flights) {
        return new AllFlightsResponse(true, "Success", flights.size(), flights);
    }

    public static AllFlightsResponse error(String message) {
        return new AllFlightsResponse(false, message, 0, null);
    }
}