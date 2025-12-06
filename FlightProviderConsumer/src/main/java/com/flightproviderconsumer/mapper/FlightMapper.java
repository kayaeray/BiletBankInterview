package com.flightproviderconsumer.mapper;


import com.flightproviderconsumer.client.providera.FlightProviderA;
import com.flightproviderconsumer.client.providerb.FlightProviderB;
import com.flightproviderconsumer.dto.UnifiedFlight;


import java.time.LocalDateTime;

public class FlightMapper {

    public static UnifiedFlight mapProviderAToDTO(FlightProviderA source) {
        UnifiedFlight dto = new UnifiedFlight();
        dto.setProviderName("ProviderA");


        dto.setFlightNumber(source.getFlightNo());
        dto.setOrigin(source.getOrigin());
        dto.setDestination(source.getDestination());
        dto.setPrice(source.getPrice());


        if (source.getDeparturedatetime() != null) {
            dto.setDeparture(LocalDateTime.parse(source.getDeparturedatetime()));
        }
        if (source.getArrivaldatetime() != null) {
            dto.setArrival(LocalDateTime.parse(source.getArrivaldatetime()));
        }

        return dto;
    }
    public static UnifiedFlight mapProviderBToDTO(FlightProviderB source) {
        UnifiedFlight dto = new UnifiedFlight();
        dto.setProviderName("ProviderB");

        dto.setFlightNumber(source.getFlightNumber());
        dto.setOrigin(source.getDeparture());
        dto.setDestination(source.getArrival());
        dto.setPrice(source.getPrice());

        if (source.getDeparturedatetime() != null) {
            dto.setDeparture(LocalDateTime.parse(source.getDeparturedatetime()));
        }
        if (source.getArrivaldatetime() != null) {
            dto.setArrival(LocalDateTime.parse(source.getArrivaldatetime()));
        }

        return dto;
    }

}