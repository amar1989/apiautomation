package com.gojek.assignment.gojekfinalassignment.bo;

public class APIRequestBO
{
	private String url=null;
	private Enum<MethodType> methodType;
	private int statusCode;
	private String responseString="";

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Enum<MethodType> getMethodType()
	{
		return methodType;
	}

	public void setMethodType(Enum<MethodType> methodType)
	{
		this.methodType = methodType;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public String getResponseString()
	{
		return responseString;
	}

	public void setResponseString(String responseString)
	{
		this.responseString = responseString;
	}

}
