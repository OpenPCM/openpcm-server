package org.openpcm.util;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpcm.config.Constants;
import org.openpcm.utils.OperationIdInterceptor;

public class OperationIdInterceptorTest {

    private OperationIdInterceptor interceptor;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private FilterChain mockFilterChain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interceptor = new OperationIdInterceptor();
    }

    @Test
    public void test_doFilterInterval_AddsResponseHeaderWhenPresentOnRequest() throws IOException, ServletException {

        when(mockRequest.getHeader(Constants.OPERATION_ID)).thenReturn("opid1");
        when(mockResponse.getHeader(Constants.OPERATION_ID)).thenReturn(null);

        try {
            interceptor.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockResponse, times(1)).addHeader(eq(Constants.OPERATION_ID), eq("opid1"));

    }

}
