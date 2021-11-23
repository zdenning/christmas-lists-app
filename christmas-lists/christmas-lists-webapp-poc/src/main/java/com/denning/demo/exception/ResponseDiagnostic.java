package com.denning.demo.exception;

import org.springframework.http.HttpStatus;

public class ResponseDiagnostic
{
	private HttpStatus status;
	
	private Integer httpCode;
	
	private String message;

	public ResponseDiagnostic()
	{
	}

	public HttpStatus getStatus()
	{
		return status;
	}

	public void setStatus(HttpStatus status)
	{
		this.status = status;
	}

	public Integer getHttpCode()
	{
		return httpCode;
	}

	public void setHttpCode(Integer httpCode)
	{
		this.httpCode = httpCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
