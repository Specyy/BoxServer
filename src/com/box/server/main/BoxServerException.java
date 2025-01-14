package com.box.server.main;

public class BoxServerException extends Exception {

	private static final long serialVersionUID = 1L;

	public BoxServerException() {
	}

	public BoxServerException(String message) {
		super(message);
	}

	public BoxServerException(Throwable cause) {
		super(cause);
	}

	public BoxServerException(String message, Throwable cause) {
		super(message, cause);
	}

	protected BoxServerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
