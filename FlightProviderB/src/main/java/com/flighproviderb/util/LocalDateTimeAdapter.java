package com.flighproviderb.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.LocalDateTime;

/**
 * LocalDateTimeAdapter, useful to convert String to Joda Time's LocalDateTime and conversely, for use with JAXB 2.0.
 * <br>
 * Can be used when customizing XML Schema to Java Representation Binding (XJC).
 *
 * @see org.joda.time.LocalDateTime
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    @Override
    public LocalDateTime unmarshal(String v) throws Exception {
        return (v == null) ? null : LocalDateTime.parse(v);
    }

    @Override
    public String marshal(LocalDateTime v) throws Exception {
        return (v == null) ? null : v.toString();
    }
}
