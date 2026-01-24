package com.tcs.utx.digiframe.exception;

public class UserDefinedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6609436492985846378L;
	/** The message. */
	private final String message;

	/**
	 * Instantiates a new user defined exception.
	 */
	public UserDefinedException() {
		super();
		this.message = null;
	}

	/**
	 * Instantiates a new user defined exception.
	 *
	 * @param message the message
	 */
	public UserDefinedException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * Instantiates a new user defined exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public UserDefinedException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	/**
	 * Instantiates a new user defined exception.
	 *
	 * @param cause the cause
	 */
	public UserDefinedException(Throwable cause) {
		super(cause);
		this.message = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return this.message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return this.message;
	}
}