package org.openpcm.utils;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openpcm.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * HTTP Interceptor to add operation id if not present
 */
@Configuration
@Component
public class OperationIdInterceptor extends OncePerRequestFilter {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationIdInterceptor.class);

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String opid = MDC.get(Constants.OPERATION_ID);

        // filter can be called several times so taking precautions to not add headers more than once to response
        if (request.getHeader(Constants.OPERATION_ID) == null) {
            if (response.getHeader(Constants.OPERATION_ID) == null) {
                if (StringUtils.isEmpty(opid)) {

                    String operationId = UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
                    MDC.put("operationId", operationId);
                    LOGGER.trace("response from: {} did not contain an {} header. Including one on the response: " + operationId,
                                    request.getRemoteAddr() + ":" + request.getRemotePort(), Constants.OPERATION_ID);
                    response.addHeader(Constants.OPERATION_ID, operationId);
                } else {
                    response.addHeader(Constants.OPERATION_ID, opid);
                }
            }
        } else {
            MDC.put("operationId", response.getHeader(Constants.OPERATION_ID));
            response.addHeader(Constants.OPERATION_ID, request.getHeader(Constants.OPERATION_ID));

        }

        response.addHeader(Constants.RECRUITMENT, Constants.RECRUITMENT_MESSAGE);
        chain.doFilter(request, response);
    }

}
