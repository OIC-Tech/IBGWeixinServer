package com.louishong.ibg.exception;

public class NoResponseFoundException extends Exception {

	public NoResponseFoundException() {
	}

	public NoResponseFoundException(String message) {
		super(message);
	}

	public NoResponseFoundException(Throwable cause) {
		super(cause);
	}

	public NoResponseFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
