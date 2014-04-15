package com.pw.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/3/14
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpIfModifiedSinceFix implements Filter {
    private static final String IF_MODIFIED_SINCE="If-Modified-Since";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //To change body of implemented methods use File | Settings | File Templates.
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if(request.getHeader(HttpIfModifiedSinceFix.IF_MODIFIED_SINCE)!=null){
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.sendError(304);
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private class HttpIfModifedSinceWrapper extends HttpServletRequestWrapper{

        private HttpIfModifedSinceWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getHeader(String name) {
            if(HttpIfModifiedSinceFix.IF_MODIFIED_SINCE.equals(name)){
                return null;
            }
            return super.getHeader(name);    //To change body of overridden methods use File | Settings | File Templates.
        }

        @Override
        public Enumeration getHeaderNames() {
            Enumeration<?> headerNames = super.getHeaderNames();
            List<String> newHeaderNames = new ArrayList<>();
            while (headerNames.hasMoreElements()){
                if(!HttpIfModifiedSinceFix.IF_MODIFIED_SINCE.equals(headerNames.nextElement().toString())){
                    newHeaderNames.add(headerNames.nextElement().toString());
                }
            }
            return Collections.enumeration(newHeaderNames);
        }
    }
}

