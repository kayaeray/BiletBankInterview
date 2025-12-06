package com.flightproviderconsumer.service;


import com.flightproviderconsumer.client.providera.AvailabilitySearchProviderA;
import com.flightproviderconsumer.client.providera.AvailabilitySearchResponseProviderA;
import com.flightproviderconsumer.client.providera.FlightProviderA;
import com.flightproviderconsumer.client.providera.SearchRequestProviderA;
import com.flightproviderconsumer.client.providerb.AvailabilitySearchProviderB;
import com.flightproviderconsumer.client.providerb.AvailabilitySearchResponseProviderB;
import com.flightproviderconsumer.client.providerb.FlightProviderB;
import com.flightproviderconsumer.client.providerb.SearchRequestProviderB;
import com.flightproviderconsumer.dto.SearchRequest;
import com.flightproviderconsumer.exceptions.FlightProviderBusinessException;
import com.flightproviderconsumer.exceptions.FlightProviderUnavailableException;
import jakarta.xml.ws.soap.SOAPFaultException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class FlightAggregatorService {


    private final WebServiceTemplate webServiceClient;
    @Value("http://localhost:9001/flightprovidera")
    private String providerAEndpointUrl;
    @Value("http://localhost:9002/flightproviderb")
    private String providerBEndpointUrl;

    public FlightAggregatorService(Jaxb2Marshaller marshaller) {
        this.webServiceClient = new WebServiceTemplate(marshaller);
    }


    public List<FlightProviderA> searchFlightsFromProviderA(SearchRequest searchRequest) {
        try {

            AvailabilitySearchProviderA searchProviderA = new AvailabilitySearchProviderA();
            SearchRequestProviderA requestProviderA = new SearchRequestProviderA();

            requestProviderA.setOrigin(searchRequest.getOrigin());
            requestProviderA.setDestination(searchRequest.getDestination());
            requestProviderA.setDepartureDate(searchRequest.getDate());

            searchProviderA.setArg0(requestProviderA);

            Object responseObj = webServiceClient.marshalSendAndReceive(providerAEndpointUrl, searchProviderA);

            AvailabilitySearchResponseProviderA response = (AvailabilitySearchResponseProviderA) responseObj;

            if (response != null && response.getReturn() != null && !response.getReturn().isHasError()) {
                return response.getReturn().getFlightOptions();
            }
            return Collections.emptyList();
        } catch (SOAPFaultException ex) {
            throw new FlightProviderBusinessException("ProviderA", ex.getMessage());
        } catch (Exception ex) {
            throw new FlightProviderUnavailableException("ProviderA");
        }
    }

    public List<FlightProviderB> searchFlightsFromProviderB(SearchRequest searchRequest) {
        try {

            AvailabilitySearchProviderB searchProviderB = new AvailabilitySearchProviderB();
            SearchRequestProviderB requestProviderB = new SearchRequestProviderB();

            requestProviderB.setDeparture(searchRequest.getOrigin());
            requestProviderB.setArrival(searchRequest.getDestination());
            requestProviderB.setDepartureDate(searchRequest.getDate());

            searchProviderB.setArg0(requestProviderB);

            Object responseObj = webServiceClient.marshalSendAndReceive(providerBEndpointUrl, searchProviderB);

            AvailabilitySearchResponseProviderB response = (AvailabilitySearchResponseProviderB) responseObj;

            if (response != null && response.getReturn() != null && !response.getReturn().isHasError()) {
                return response.getReturn().getFlightOptions();
            }
            return Collections.emptyList();
        } catch (SOAPFaultException ex) {
            throw new FlightProviderBusinessException("ProviderB", ex.getMessage());
        } catch (Exception ex) {
            throw new FlightProviderUnavailableException("ProviderB");
        }
    }

}