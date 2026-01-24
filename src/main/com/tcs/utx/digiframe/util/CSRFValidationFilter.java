package com.tcs.utx.digiframe.util;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONException;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import com.netflix.config.DynamicStringListProperty;




public class CSRFValidationFilter implements Filter {
    public static final String TARGET_ORIGIN_JVM_PARAM_NAME = "TARGET_ORIGIN";
    private static final String CSRF_TOKEN_NAME = "x-csrf-token";
    private static final Logger LOG = LoggerFactory.getLogger(CSRFValidationFilter.class);
    private List<URL> targetOrigins;
    private final SecureRandom secureRandom = new SecureRandom();
    public static final RequestMatcher DEFAULT_CSRF_MATCHER = new DefaultRequiresCsrfMatcher();
    
    @Override    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest httpReq = (HttpServletRequest) request;
    	String url1 = httpReq.getRequestURL().toString();
    	HttpServletResponse httpResp = (HttpServletResponse) response;
    	String ipAddress = getSourceIP(httpReq);
    	LOG.info(ipAddress + " " + httpReq.getServerPort()+ " "  + httpReq.getRequestURI() + " " + httpReq.getMethod());
        String accessDeniedReason;
        if (url1.contains("/logoutApp")){
            chain.doFilter(request, response);
            return;
          }
        if (url1.contains("/dologin")) {
            chain.doFilter(request, response);
            return;
          }
        if(url1.contains("/menus")) {
        	chain.doFilter(request, response);
            return;
        }
        if(url1.contains("/help")) {
        	chain.doFilter(request, response);
            return;
        }
        if (url1.contains("/isAuthUser")) {
            chain.doFilter(request, response);
            return;
        }
        if (url1.contains("/menu")) {
            HttpSession session = httpReq.getSession(false);
            if(session==null){
              chain.doFilter(request, response);
              return;
            }
            HttpServletResponseWrapper httpRespWrapper = new HttpServletResponseWrapper(httpResp);
            LOG.info("token in session is ----- :  " + session.getAttribute(CSRF_TOKEN_NAME));
            if(session.getAttribute(CSRF_TOKEN_NAME) == null) {
                String trueToken = this.addTokenCookieAndHeader(httpReq, httpRespWrapper);
                session.setAttribute(CSRF_TOKEN_NAME, trueToken);
            }
            StringBuilder sb  = new StringBuilder(2048);
            sb.append(this.determineCookieName(httpReq)).append("=").append((String)session.getAttribute(CSRF_TOKEN_NAME)).append("; Path=").append("/").append("; HttpOnly; SameSite=Strict");
            httpRespWrapper.addHeader("Set-Cookie", sb.toString());
            httpRespWrapper.setHeader(CSRF_TOKEN_NAME, (String)session.getAttribute(CSRF_TOKEN_NAME));
            chain.doFilter(request, httpRespWrapper);
            return;
          }
        if(url1.contains("/reports")) {
        	chain.doFilter(request, response);
            return;
        }

        Cookie tokenCookie = new Cookie(CSRF_TOKEN_NAME,"");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);
        if (httpReq.getCookies() != null) {
            String csrfCookieExpectedName = this.determineCookieName(httpReq);
            tokenCookie = Arrays.stream(httpReq.getCookies()).filter(c -> c.getName().equals(csrfCookieExpectedName)).findFirst().orElse(null);
        }
        if(url1.contains("/worklists")) {
        	chain.doFilter(request, response);
            return;
        }
        if (tokenCookie == null || this.isBlank(tokenCookie.getValue())) {
            LOG.info("CSRFValidationFilter: CSRF cookie absent or value is null/empty so we provide one and return an HTTP NO_CONTENT response !");
            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResp.getWriter().write(sendResponseText(httpResp, "Unauthorized"));
            return;
        } else {    
                HttpSession session = httpReq.getSession(false);
                String tokenFromSession = (String) session.getAttribute(CSRF_TOKEN_NAME);
                if (tokenFromSession!=null && !tokenFromSession.equals(tokenCookie.getValue())) {
                    LOG.info("Tokens are not equal");
                    accessDeniedReason = "CSRFValidationFilter: Token provided via HTTP Header and via Cookie are not equals so we block the request !";
                    LOG.warn(accessDeniedReason);
	            httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                    httpResp.getWriter().write(sendResponseText(httpResp, "ACESS_DENIED")); 
                } else {
                    HttpServletResponseWrapper httpRespWrapper = new HttpServletResponseWrapper(httpResp);
                    chain.doFilter(request, httpRespWrapper);
                }
            }
    }
    @Override    public void init(FilterConfig filterConfig) throws ServletException {
        List<String> urls= new DynamicStringListProperty(TARGET_ORIGIN_JVM_PARAM_NAME,"",",").get();
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
        return DatatypeConverter.printHexBinary(buffer);
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
    /**     * Add the CSRF token cookie and header to the provided HTTP response object     *     * @param httpRequest  Source HTTP request     * @param httpResponse HTTP response object to update     */    private String addTokenCookieAndHeader(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String token = this.generateToken();
        return token;
    }
    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher    {
        private List<String> methods = new DynamicStringListProperty("CSRF_WHITELIST", "", ",").get();
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