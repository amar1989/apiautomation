package com.assignment.threadimpl;

import java.util.concurrent.Callable;

import com.assignment.bo.APIRequestBO;
import com.assignment.utilities.Helper;
import org.apache.log4j.Logger;

public class WorkerComparator implements Callable<Boolean>
{
	Logger logger=Logger.getLogger(WorkerComparator.class);
	APIRequestBO reqBo1 = null;
	APIRequestBO reqBo2 = null;

	public WorkerComparator(APIRequestBO reqBo1, APIRequestBO reqBo2)
	{
		this.reqBo1 = reqBo1;
		this.reqBo2 = reqBo2;
	}

	
	public Boolean call() throws Exception
	{
		boolean status = false;
		try
		{
			if (reqBo1.getStatusCode() == reqBo2.getStatusCode())
			{										
				status = Helper.compareResponseString(reqBo1.getResponseString(), reqBo2.getResponseString());
			}

		}
		catch (Exception e)
		{
            status=false;
			logger.error("Exception is caught",e);
		}
		return status;
	}
}
