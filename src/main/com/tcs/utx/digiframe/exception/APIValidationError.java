
package com.tcs.utx.digiframe.exception;

class APIValidationError extends APISubError {
	
	private static final long serialVersionUID = -4135753730389932098L;
private String object;
   private String field;
   private transient Object rejectedValue;
   private String message;

   APIValidationError(String object, String message) {
       this.object = object;
       this.message = message;
   }

	public APIValidationError(String object, String field, Object rejectedValue, String message) {
		super();
		this.object = object;
		this.field = field;
		this.rejectedValue = rejectedValue;
		this.message = message;
	}
   
}
