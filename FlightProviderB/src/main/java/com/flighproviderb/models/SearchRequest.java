package com.flighproviderb.models;

import com.flighproviderb.util.LocalDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
public class SearchRequest {
	@XmlElement(name = "departure", required = true)
	private String departure = "";
	@XmlElement(name = "arrival", required = true)
	private String arrival = "";
	@XmlElement(name = "departureDate", required = true)
	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime departureDate;

	// Constructors
	public SearchRequest() {
	}

	public SearchRequest(String origin, String destination, LocalDateTime departureDate) {
		this.departure = origin;
		this.arrival = destination;
		this.departureDate = departureDate;
	}

	// Getters and Setters
	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String origin) {
		this.departure = origin;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String destination) {
		this.arrival = destination;
	}

	public LocalDateTime getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(LocalDateTime departureDate) {
		this.departureDate = departureDate;
	}
}
