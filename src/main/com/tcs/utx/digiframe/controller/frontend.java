package com.tcs.utx.digiframe.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class frontend {
	
	

		@RequestMapping("/Bughuntr/api/**")
		    public void blockApi() {
		        // You can also include a message if you want
		        throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "API calls not allowed on this endpoint");
		    }

	
	
	@GetMapping( "/Bughuntr/**")
	    public String forwardToIndex() {
		
	        return "forward:/index.html";
	}


	@RequestMapping(value = {"/{path:[^\\.]*}"})
    public String redirectToIndex(HttpServletRequest request) {
        String path = request.getRequestURI();
        System.out.println(path);

        if (path.startsWith("BugHuntr/api")) {
            return null;
        }
        return "forward:/index.html";
    }
	
	@GetMapping( "/")
    public String forwardToIndex1() {
	
        return "forward:/index.html";
	}
	
	

}
