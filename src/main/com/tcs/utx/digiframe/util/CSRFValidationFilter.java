package com.tcs.utx.digiframe.util;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import jakarta.servlet.http.HttpSession;

public class CSRFValidationFilter implements Filter {
    public static final String TARGET_ORIGIN_JVM_PARAM_NAME = "TARGET_ORIGIN";
    private static final String CSRF_TOKEN_NAME = "x-csrf-token";
    private static final Logger LOG = LoggerFactory.getLogger(CSRFValidationFilter.class);
    private List<URL> targetOrigins;
    private final SecureRandom secureRandom = new SecureRandom();
    public static final RequestMatcher DEFAULT_CSRF_MATCHER = new DefaultRequiresCsrfMatcher();

    // Safe HTTP methods that do not require CSRF validation
    private static final Set<String> SAFE_METHODS = new HashSet<>(Arrays.asList("GET", "HEAD", "OPTIONS", "TRACE"));

    // Exact exempt paths that skip CSRF validation entirely
    private static final Set<String> EXEMPT_PATHS = new HashSet<>(Arrays.asList(
        "/BugHuntr/api/dologin",
        "/BugHuntr/api/logoutApp"
    ));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpReq = (HttpServletRequest) request;
    	HttpServletResponse httpResp = (HttpServletResponse) response;
    	String requestURI = httpReq.getRequestURI();
    	String httpMethod = httpReq.getMethod();
    	String ipAddress = getSourceIP(httpReq);
    	LOG.info(ipAddress + " " + httpReq.getServerPort()+ " "  + requestURI + " " + httpMethod);

        // Check exempt paths using exact match
        if (isExemptPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // For the /menu endpoint, generate and set the CSRF token
        if (requestURI.equals("/BugHuntr/api/menu")) {
            HttpSession session = httpReq.getSession(false);
            if (session == null) {
                chain.doFilter(request, response);
                return;
            }
            HttpServletResponseWrapper httpRespWrapper = new HttpServletResponseWrapper(httpResp);
            if (session.getAttribute(CSRF_TOKEN_NAME) == null) {
                String trueToken = this.generateToken();
                session.setAttribute(CSRF_TOKEN_NAME, trueToken);
            }
            StringBuilder sb = new StringBuilder(2048);
            sb.append(this.determineCookieName(httpReq)).append("=").append((String) session.getAttribute(CSRF_TOKEN_NAME)).append("; Path=/; Secure; SameSite=Strict");
            httpRespWrapper.addHeader("Set-Cookie", sb.toString());
            httpRespWrapper.setHeader(CSRF_TOKEN_NAME, (String) session.getAttribute(CSRF_TOKEN_NAME));
            chain.doFilter(request, httpRespWrapper);
            return;
        }

        // Safe HTTP methods (GET, HEAD, OPTIONS, TRACE) do not need CSRF validation
        if (SAFE_METHODS.contains(httpMethod.toUpperCase())) {
            chain.doFilter(request, response);
            return;
        }

        // For state-changing methods (POST, PUT, DELETE, PATCH), enforce CSRF token validation
        // Validate using the request HEADER (set by JavaScript), not the cookie (auto-sent by browser)
        String tokenFromHeader = httpReq.getHeader(CSRF_TOKEN_NAME);

        if (this.isBlank(tokenFromHeader)) {
            LOG.info("CSRFValidationFilter: CSRF header absent or empty for state-changing request!");
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.getWriter().write(sendResponseText(httpResp, "Unauthorized"));
            return;
        }

        HttpSession session = httpReq.getSession(false);
        if (session == null) {
            LOG.warn("CSRFValidationFilter: No session found for state-changing request");
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.getWriter().write(sendResponseText(httpResp, "Unauthorized"));
            return;
        }

        String tokenFromSession = (String) session.getAttribute(CSRF_TOKEN_NAME);
        if (tokenFromSession == null) {
            LOG.warn("CSRFValidationFilter: No CSRF token in session for state-changing request");
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.getWriter().write(sendResponseText(httpResp, "Unauthorized"));
            return;
        }

        if (!tokenFromSession.equals(tokenFromHeader)) {
            LOG.warn("CSRFValidationFilter: Token from request header does not match session token!");
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.getWriter().write(sendResponseText(httpResp, "ACESS_DENIED"));
            return;
        }

        // Token is valid - rotate it for single-use protection
        String newToken = this.generateToken();
        session.setAttribute(CSRF_TOKEN_NAME, newToken);

        HttpServletResponseWrapper httpRespWrapper = new HttpServletResponseWrapper(httpResp);
        StringBuilder sb = new StringBuilder(2048);
        sb.append(this.determineCookieName(httpReq)).append("=").append(newToken).append("; Path=/; Secure; SameSite=Strict");
        httpRespWrapper.addHeader("Set-Cookie", sb.toString());
        httpRespWrapper.setHeader(CSRF_TOKEN_NAME, newToken);

        chain.doFilter(request, httpRespWrapper);
    }

    /**
     * Check if the request path is exempt from CSRF validation using exact path matching.
     */
    private boolean isExemptPath(String requestURI) {
        for (String exemptPath : EXEMPT_PATHS) {
            if (requestURI.equals(exemptPath)) {
                return true;
            }
        }
        return false;
    }

    @Override    public void init(FilterConfig filterConfig) throws ServletException {
        String originProp = System.getProperty(TARGET_ORIGIN_JVM_PARAM_NAME, "");
        List<String> urls = originProp.isEmpty() ? List.of() : List.of(originProp.split(","));
            this.targetOrigins =new ArrayList<>();
            urls.forEach(url -> {
                try {
                    this.targetOrigins.add(new URL(url));
                } catch (MalformedURLException e) {
                     LOG.error("Cannot init the filter !", e.getMessage());
                }
            });
        LOG.info("CSRFValidationFilter: Filter init, set expected target origin to '{}'.", this.targetOrigins);
    }
    /**     * {@inheritDoc}     */    @Override    public void destroy() {
        LOG.info("CSRFValidationFilter: Filter shutdown");
    }
    /**     * Check if a string is null or empty (including containing only spaces)     *     * @param s Source string     * @return TRUE if source string is null or empty (including containing only spaces)     */    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
    /**     * Generate a new CSRF token     *     * @return The token a string     */    private String generateToken() {
        byte[] buffer = new byte[50];
        this.secureRandom.nextBytes(buffer);
        return HexFormat.of().withUpperCase().formatHex(buffer);
    }
    /**     * Determine the name of the CSRF cookie for the targeted backend service     *     * @param httpRequest Source HTTP request     * @return The name of the cookie as a string     */    private String determineCookieName(HttpServletRequest httpRequest) {
        return CSRF_TOKEN_NAME;
    }
    private String sendResponseText(HttpServletResponse response, String message) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("text", message);
        } catch (JSONException e) {
            LOG.error("JSON exception in "+message,e.getMessage());
        }
        return jsonObj.toString();
    }
    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher    {
        private static final String whitelistProp = System.getProperty("CSRF_WHITELIST", "");
        private List<String> methods = whitelistProp.isEmpty() ? List.of() : List.of(whitelistProp.split(","));
      private final Set<String> allowedMethods = new HashSet<String>(methods);
      public boolean matches(HttpServletRequest request)
      {
        return !this.allowedMethods.contains(request.getMethod());
      }
    }

    private String getSourceIP(HttpServletRequest request) {
		String sourceIP = "";
		String xForwardedFor = request.getHeader("X-FORWARDED-FOR");
		if (xForwardedFor != null) {
			String[] xforwardedIPs = xForwardedFor.split(",");
			sourceIP = xforwardedIPs[0].replaceAll("[^0-9.:]", "");
		} else {
			sourceIP = request.getServerName();
		}
		return sourceIP;
	}
}
