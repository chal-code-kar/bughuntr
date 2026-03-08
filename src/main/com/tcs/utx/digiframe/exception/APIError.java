
package com.tcs.utx.digiframe.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonFormat;

public class APIError {
	private HttpStatus status;
	   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	   private LocalDateTime timestamp;
	   private String message;
	   private List<APISubError> subErrors;
	   private List<FieldError> fieldErrors = new ArrayList<>();

	   private APIError() {
	       timestamp = LocalDateTime.now();
	   }

	   APIError(HttpStatus status) {
	       this();
	       this.status = status;
	   }

	   APIError(HttpStatus status, Throwable ex) {
	       this();
	       this.status = status;
	       this.message = "Unexpected error";
	   }

	   public APIError(HttpStatus status, String message, Throwable ex) {
	       this();
	       this.status = status;
	       this.message = message;
	   }

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<APISubError> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<APISubError> subErrors) {
		this.subErrors = subErrors;
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
	
	public void addFieldError(String objectName, String path, String message) {
        FieldError error = new FieldError(objectName,path, message);
        fieldErrors.add(error);
    }
	
	   
}
