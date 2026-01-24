package com.tcs.utx.digiframe;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
  private static final Logger LOG = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
  
  public CustomAccessDeniedHandler() {
    LOG.info("Inside constructor.");
  }
  
    
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
	    LOG.debug("CustomAccessDeniedHandler | handler called");
	    response.setContentType("application/json");
	    response.setCharacterEncoding("utf-8");
	    JSONObject jsonObj = new JSONObject();
	    try {
	      jsonObj.put("text", "UNAUTHORIZED_ACCESS");
	    } catch (JSONException e) {
	      LOG.error("JSON exception in ACCESS_DENIED", (Exception)e);
	    }
	    response.getWriter().write(jsonObj.toString());
	    response.setStatus(401);
	  }
	}
