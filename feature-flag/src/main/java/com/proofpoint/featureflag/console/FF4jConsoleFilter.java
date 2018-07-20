package com.proofpoint.featureflag.console;

import com.google.inject.Inject;
import org.eclipse.jetty.server.session.AbstractSession;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.HashedSession;
import org.eclipse.jetty.server.session.MemSession;
import org.ff4j.FF4j;
import org.ff4j.web.FF4jDispatcherServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Ascii.equalsIgnoreCase;

public class FF4jConsoleFilter implements Filter {
    @Inject
    FF4j ff4j;

    FF4jDispatcherServlet servlet;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        servlet = new FF4jDispatcherServlet();
        servlet.setFf4j(ff4j);

        // Bypass the init phase and register the necessary attributes manually
        filterConfig.getServletContext().setAttribute("FF4J", this.ff4j);
        System.out.println("Servlet has been initialized and ff4j store in session with " + this.ff4j.getFeatures().size());

        ServletRegistration.Dynamic sd = filterConfig.getServletContext().addServlet("FF4jDispatcherServlet", servlet);
        sd.addMapping("/admin/ff4j-console");
        sd.addMapping("/admin/ff4j-console/*");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
