package com.gojek.assignment.gojekfinalassignment.comparator;

import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.gojek.assignment.gojekfinalassignment.bo.APIRequestBO;
import com.gojek.assignment.gojekfinalassignment.bo.ResultBO;
import com.gojek.assignment.gojekfinalassignment.service.ApiServiceImpl;

public class ComparatorUtil
{
	Logger logger = Logger.getLogger(ComparatorUtil.class);

	public boolean compare(String file1, String file2)
	{
		boolean status = true;
		ApiServiceImpl serviceImpl = new ApiServiceImpl();
		try
		{
			// Implementing batch processing due to in memory issue.Processing only 1000 request at a time.	
			while (true)
			{
				Map<String, List<APIRequestBO>> urlMap = serviceImpl.getDataFromFile(file1, file2);
				if (urlMap != null)
				{
					Map<String, List<APIRequestBO>> fileResponseMap = serviceImpl.getApiResponse(urlMap);
					if (fileResponseMap != null)
					{
						List<ResultBO> resultBoList = serviceImpl.compareResponse(fileResponseMap);
						if (resultBoList != null)
						{
							serviceImpl.printResult(resultBoList);
						}
						else
						{
							status = false;
						}
					}
					else
					{
						status = false;
					}
				}
				else
				{
					break;
				}

			}
		}
		catch (Exception e)
		{
			logger.error("Comparator failed.", e);
		}
		return status;
	}
}
