package com.ticket.monolithticketmonster.infrastructure.http;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * In order to track requests across services, we need to generate a unique tracking ID for each
 * request. Filters with lower order values are executed first, We set Integer.MIN_VALUE to ensure
 * requestId filter is executed first
 */
@Component
@Order(Integer.MIN_VALUE)
public class RequestIdFilter implements Filter {

  private static final String REQUEST_ID_HEADER = "X-Request-Id";
  private static final String REQUEST_ID_KEY = "requestId";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    MDC.put(REQUEST_ID_KEY, requestId);
    httpResponse.setHeader(REQUEST_ID_HEADER, requestId);

    try {
      chain.doFilter(request, response);
    } finally {
      MDC.remove(REQUEST_ID_KEY);
    }
  }
}
