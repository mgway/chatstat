package com.matthewgalloway.stats.exception;

public class ApiException extends RuntimeException  {
	private static final long serialVersionUID = 1L;

	public ApiException(String s) {
		super(s);
	}

	public ApiException(Throwable throwable) {
		super(throwable);
	}
}
