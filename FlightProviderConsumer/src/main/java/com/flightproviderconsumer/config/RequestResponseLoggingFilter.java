package com.flightproviderconsumer.config;

import com.flightproviderconsumer.entity.RequestResponseLog;
import com.flightproviderconsumer.service.RequestResponseLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private final RequestResponseLogService logService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Response body cache’lenebilir olmalı
        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request,5000);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        String traceId = UUID.randomUUID().toString(); // Request trace ID

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

        } finally {
            logRequestAndResponse(wrappedRequest, wrappedResponse, traceId);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequestAndResponse(ContentCachingRequestWrapper request,
                                       ContentCachingResponseWrapper response,
                                       String traceId) {

        String requestBody = new String(
                request.getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        String responseBody = new String(
                response.getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        RequestResponseLog logData = RequestResponseLog.builder()
                .serviceName("REST API")
                .endpoint(request.getRequestURI())
                .httpMethod(request.getMethod())
                .requestPayload(requestBody)
                .responsePayload(responseBody)
                .statusCode(response.getStatus())
                .createdAt(LocalDateTime.now())
                .errorMessage(response.getStatus() >= 400 ? responseBody : null)
                .build();

        log.info("TRACE_ID: {} | {} {} | status: {}", traceId,
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus());

        logService.log(logData);
    }
}
