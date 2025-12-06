package com.flightproviderconsumer.service;

import com.flightproviderconsumer.client.providera.FlightProviderA;
import com.flightproviderconsumer.client.providerb.FlightProviderB;
import com.flightproviderconsumer.dto.AllFlightsResponse;
import com.flightproviderconsumer.dto.SearchRequest;
import com.flightproviderconsumer.dto.UnifiedFlight;
import com.flightproviderconsumer.exceptions.FlightException;
import com.flightproviderconsumer.mapper.FlightMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FlightSearchService {

    private final FlightAggregatorService flightIntegrationService;

    public FlightSearchService(FlightAggregatorService service) {
        this.flightIntegrationService = service;
    }

    /**
     * Returns full combined list without filtering or optimization
     */
    public AllFlightsResponse getAllFlights(SearchRequest searchRequest) {
        List<UnifiedFlight> flights = fetchUnifiedFlights(searchRequest);
        return AllFlightsResponse.success(flights);
    }

    /**
     * Groups flights by key and selects cheapest option from each group
     */
    public AllFlightsResponse getCheapestFlights(SearchRequest searchRequest) {
        List<UnifiedFlight> allFlights = fetchUnifiedFlights(searchRequest);

        List<UnifiedFlight> optimized =
                allFlights.stream()
                        .collect(Collectors.groupingBy(
                                this::createFlightKey,
                                Collectors.minBy(Comparator.comparing(UnifiedFlight::getPrice))
                        ))
                        .values()
                        .stream()
                        .flatMap(Optional::stream)
                        .sorted(Comparator.comparing(UnifiedFlight::getPrice))
                        .toList();

        return AllFlightsResponse.success(optimized);
    }

    /**
     * Fetches flights from both providers and maps into unified model
     */
    private List<UnifiedFlight> fetchUnifiedFlights(SearchRequest searchRequest) {
        List<UnifiedFlight> unifiedFlights = new ArrayList<>();

        try {
            List<FlightProviderA> providerAFlights =
                    flightIntegrationService.searchFlightsFromProviderA(searchRequest);

            if (providerAFlights != null) {
                providerAFlights.forEach(f -> unifiedFlights.add(FlightMapper.mapProviderAToDTO(f)));
            }

        } catch (FlightException ex) {
            log.warn("ProviderA: {}", ex.getMessage());
        }

        try {
            List<FlightProviderB> providerBFlights =
                    flightIntegrationService.searchFlightsFromProviderB(searchRequest);

            if (providerBFlights != null) {
                providerBFlights.forEach(f -> unifiedFlights.add(FlightMapper.mapProviderBToDTO(f)));
            }

        } catch (FlightException ex) {
            log.warn("ProviderB: {}", ex.getMessage());
        }

        return unifiedFlights;
    }

    /**
     * Group key used to detect identical flights from different providers
     */
    private FlightKey createFlightKey(UnifiedFlight f) {
        return new FlightKey(
                f.getFlightNumber(),
                f.getOrigin(),
                f.getDestination(),
                f.getDeparture(),
                f.getArrival()
        );
    }

    private record FlightKey(
            String flightNumber,
            String origin,
            String destination,
            LocalDateTime departure,
            LocalDateTime arrival
    ) {}
}