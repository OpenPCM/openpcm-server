package org.openpcm.util;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpcm.constants.Constants;
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

        MDC.put(Constants.OPERATION_ID, "");
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
        verify(mockResponse, times(1)).addHeader(eq(Constants.RECRUITMENT), eq(Constants.RECRUITMENT_MESSAGE));
    }

    @Test
    public void test_doFilterInterval_AddsResponseHeaderWhenNotPresentOnRequest() throws IOException, ServletException {

        when(mockRequest.getHeader(Constants.OPERATION_ID)).thenReturn(null);
        when(mockResponse.getHeader(Constants.OPERATION_ID)).thenReturn(null);

        try {
            interceptor.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockResponse, times(1)).addHeader(eq(Constants.OPERATION_ID), anyString());
        verify(mockResponse, times(1)).addHeader(eq(Constants.RECRUITMENT), eq(Constants.RECRUITMENT_MESSAGE));
    }

    @Test
    public void test_doFilterInterval_KeepsResponseHeaderWhenNotPresentOnRequestOrMDC() throws IOException, ServletException {

        MDC.put(Constants.OPERATION_ID, null);
        when(mockRequest.getHeader(Constants.OPERATION_ID)).thenReturn(null);
        when(mockResponse.getHeader(Constants.OPERATION_ID)).thenReturn("opid1");

        try {
            interceptor.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockResponse, times(0)).addHeader(eq(Constants.OPERATION_ID), eq("opid1"));
        verify(mockResponse, times(1)).addHeader(eq(Constants.RECRUITMENT), eq(Constants.RECRUITMENT_MESSAGE));
    }

    @Test
    public void test_doFilterInterval_PutsMDCOpIdOnResponseHeaderWhenNotPresentOnRequestButMDC() throws IOException, ServletException {

        MDC.put(Constants.OPERATION_ID, "opid1");
        when(mockRequest.getHeader(Constants.OPERATION_ID)).thenReturn(null);
        when(mockResponse.getHeader(Constants.OPERATION_ID)).thenReturn(null);

        try {
            interceptor.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        verify(mockResponse, times(1)).addHeader(eq(Constants.OPERATION_ID), eq("opid1"));
        verify(mockResponse, times(1)).addHeader(eq(Constants.RECRUITMENT), eq(Constants.RECRUITMENT_MESSAGE));
    }

    @After
    public void tearDown() {
        MDC.put(Constants.OPERATION_ID, "");
    }

}
