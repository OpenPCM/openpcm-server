package org.openpcm.config;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * HTTP Interceptor to add operation id if not present
 */
@Configuration
@Component
public class OperationIdInterceptor extends OncePerRequestFilter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationIdInterceptor.class);

    /** The operation id header name. */
    private static String OPERATION_ID_HEADER_NAME = "operationId";

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // filter can be called several times so taking precautions to not add headers more than once to response
        if (request.getHeader(OPERATION_ID_HEADER_NAME) == null) {
            if (response.getHeader(OPERATION_ID_HEADER_NAME) == null) {
                String operationId = UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
                MDC.put("operationId", operationId);
                LOGGER.trace("response from: {} did not contain an {} header. Including one on the response: " + operationId,
                                request.getRemoteAddr() + ":" + request.getRemotePort(), OPERATION_ID_HEADER_NAME);
                response.addHeader(OPERATION_ID_HEADER_NAME, operationId);
            }
        } else {
            if (response.getHeader(OPERATION_ID_HEADER_NAME) == null) {
                MDC.put("operationId", response.getHeader(OPERATION_ID_HEADER_NAME));
                response.addHeader(OPERATION_ID_HEADER_NAME, request.getHeader(OPERATION_ID_HEADER_NAME));
            }
        }

        if (response.getHeader("X-Recruitment") == null) {
            response.addHeader("X-Recruitment", "Support OpenPCM at https://github.com/gsugambit/openpcm");
        }
        chain.doFilter(request, response);
    }

}
