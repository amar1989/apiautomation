package com.assignment.bo;

public class ResultBO
{
	private String file1Url = "";
	private String file2Url = "";
	private boolean status;

	public String getFile1Url()
	{
		return file1Url;
	}

	public void setFile1Url(String file1Url)
	{
		this.file1Url = file1Url;
	}

	public String getFile2Url()
	{
		return file2Url;
	}

	public void setFile2Url(String file2Url)
	{
		this.file2Url = file2Url;
	}

	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

}
