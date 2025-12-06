package com.flightproviderconsumer.service;

import com.flightproviderconsumer.client.providera.FlightProviderA;
import com.flightproviderconsumer.client.providerb.FlightProviderB;
import com.flightproviderconsumer.dto.AllFlightsResponse;
import com.flightproviderconsumer.dto.SearchRequest;
import com.flightproviderconsumer.exceptions.FlightException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FlightSearchServiceTest {

    @Mock
    private FlightAggregatorService flightIntegrationService;

    @InjectMocks
    private FlightSearchService flightSearchService;

    private SearchRequest searchRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchRequest = new SearchRequest("IST", "AMS", LocalDateTime.now().toString());
    }

    @Test
    void shouldReturnAllFlightsSuccessfully() {
        // Given
        FlightProviderA fpa = new FlightProviderA();
        fpa.setFlightNo("TK001");
        fpa.setOrigin("IST");
        fpa.setDestination("AMS");
        fpa.setDeparturedatetime(LocalDateTime.now().toString());
        fpa.setArrivaldatetime(LocalDateTime.now().plusHours(3).toString());
        fpa.setPrice(BigDecimal.valueOf(200.0));

        FlightProviderB fpb = new FlightProviderB();
        fpb.setFlightNumber("TK002");
        fpb.setDeparture("IST");
        fpb.setArrival("AMS");
        fpb.setDeparturedatetime(LocalDateTime.now().toString());
        fpb.setArrivaldatetime(LocalDateTime.now().plusHours(3).toString());
        fpb.setPrice(BigDecimal.valueOf(250.0));

        when(flightIntegrationService.searchFlightsFromProviderA(searchRequest))
                .thenReturn(List.of(fpa));

        when(flightIntegrationService.searchFlightsFromProviderB(searchRequest))
                .thenReturn(List.of(fpb));

        // When
        AllFlightsResponse result = flightSearchService.getAllFlights(searchRequest);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(2, result.getCount());
    }



    @Test
    void shouldReturnEmptyListWhenProvidersReturnNull() {
        when(flightIntegrationService.searchFlightsFromProviderA(searchRequest))
                .thenReturn(null);

        when(flightIntegrationService.searchFlightsFromProviderB(searchRequest))
                .thenReturn(null);

        AllFlightsResponse result = flightSearchService.getAllFlights(searchRequest);

        assertTrue(result.isSuccess());
        assertEquals(0, result.getCount());
    }

    @Test
    void shouldHandleProviderExceptionGracefully() {
        when(flightIntegrationService.searchFlightsFromProviderA(searchRequest))
                .thenThrow(new FlightException("Provider A Down"));

        when(flightIntegrationService.searchFlightsFromProviderB(searchRequest))
                .thenReturn(List.of());

        AllFlightsResponse result = flightSearchService.getAllFlights(searchRequest);

        assertTrue(result.isSuccess());
        assertEquals(0, result.getCount());
    }

    @Test
    void shouldPickCheapestFlightPerGroup() {
        FlightProviderA flight1 = new FlightProviderA();
                flight1.setFlightNo("TK123");
                flight1.setOrigin("IST");
                flight1.setDestination("AMS");
                flight1.setDeparturedatetime(LocalDateTime.now().toString());
                flight1.setArrivaldatetime(LocalDateTime.now().plusHours(3).toString());
                flight1.setPrice(BigDecimal.valueOf(300.0));

        FlightProviderB flight2 = new FlightProviderB();
                flight2.setFlightNumber("TK123");
                flight2.setDeparture("IST");
                flight2.setArrival("AMS");
                flight2.setDeparturedatetime(flight1.getDeparturedatetime());
                flight2.setArrivaldatetime(flight1.getArrivaldatetime());
                flight2.setPrice(BigDecimal.valueOf(180.0));

        when(flightIntegrationService.searchFlightsFromProviderA(searchRequest))
                .thenReturn(List.of(flight1));

        when(flightIntegrationService.searchFlightsFromProviderB(searchRequest))
                .thenReturn(List.of(flight2));

        // When
        AllFlightsResponse result = flightSearchService.getCheapestFlights(searchRequest);

        // Then
        assertTrue(result.isSuccess());
        assertEquals(1, result.getCount());
        assertEquals(BigDecimal.valueOf(180.0), result.getFlights().get(0).getPrice());
    }
}