package com.example.demo.exception;

public class FileDowUpException extends RuntimeException {
	public FileDowUpException(String message) {
		super(message);
	}
	
	public FileDowUpException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
