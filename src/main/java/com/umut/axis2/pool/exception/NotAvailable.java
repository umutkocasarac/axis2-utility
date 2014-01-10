package com.umut.axis2.pool.exception;

/**
 * 
 * @author umut.kocasarac
 *
 */
public class NotAvailable extends RuntimeException{

	private static final long serialVersionUID = -7471839519221331026L;
	
	public NotAvailable() {
		super();
	}

	public NotAvailable(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAvailable(String message) {
		super(message);
	}

	public NotAvailable(Throwable cause) {
		super(cause);
	}

	

}
