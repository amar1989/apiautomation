package com.gojek.assignment.gojekfinalassignment.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.gojek.assignment.gojekfinalassignment.apirequester.RequesterUtil;
import com.gojek.assignment.gojekfinalassignment.bo.APIRequestBO;
import com.gojek.assignment.gojekfinalassignment.bo.MethodType;
import com.gojek.assignment.gojekfinalassignment.bo.ResultBO;
import com.gojek.assignment.gojekfinalassignment.threadimpl.WorkerComparator;

public class ApiServiceImpl extends Thread implements IApiService
{
	Logger logger = Logger.getLogger(ApiServiceImpl.class);

	private static int counter = 0;
	private static int previousCounter = 0;
	private static String file1Const = "UrlFile1.txt";
	private static String file2Const = "UrlFile2.txt";

	public Map<String, List<APIRequestBO>> getDataFromFile(String file1, String file2) throws IOException
	{
		Map<String, List<APIRequestBO>> urlListMap = null;
		try
		{
			urlListMap = fileReader(file1, file2);
		}
		catch (IOException ioe)
		{
			throw new IOException(
					"Please check file" + file1 + " , " + file2 + " and its content,it should be readable.", ioe);
		}
		return urlListMap;
	}

	/*
	 * To minimize in memory data,taking 1000 request at a time(using variable
	 * counter for that only). This will be useful when we have millions of
	 * request inside file then it will be difficult in keeping all data in
	 * memory
	 */
	public Map<String, List<APIRequestBO>> fileReader(String file1, String file2) throws IOException
	{
		//For Batch Processing of 1000 records at a time
		counter = counter + 1000;
		Map<String, List<APIRequestBO>> urlListMap = new LinkedHashMap<String, List<APIRequestBO>>();
		try
		{
			List<APIRequestBO> urlList1 = readFile(file1, counter);
			urlListMap.put(file1, urlList1);
			List<APIRequestBO> urlList2 = readFile(file2, counter);
			urlListMap.put(file2, urlList2);

			if (urlList1.size() == 0 && urlList2.size() == 0)
			{
				urlListMap = null;
			}
			previousCounter = counter;
		}
		catch (IOException ioe)
		{
			throw new IOException(
					"Please check file" + file1 + " , " + file2 + " and its content,it should be readable.", ioe);
		}

		return urlListMap;
	}

	private List<APIRequestBO> readFile(String fileName, int maxLineNumber) throws IOException
	{
		String st;
		/*
		 * Using POJO(APIRequestBO) ,contains all info of a single
		 * request,instead of String as if we have to handle method type also
		 * then in that case it will be helpful.
		 */
		List<APIRequestBO> apiRequestBOList = new LinkedList<APIRequestBO>();

		File file = new File(fileName);
		try
		{

			LineNumberReader lnr = new LineNumberReader(new FileReader(file));

			while ((st = lnr.readLine()) != null)
			{
				if (lnr.getLineNumber() >= previousCounter && lnr.getLineNumber() < maxLineNumber)
				{
					APIRequestBO requestBO = new APIRequestBO();
					requestBO.setMethodType(MethodType.GET);// As all request in
															// file are of "get"
															// type so
															// hardcoding it
															// otherwise can
															// configure for
															// different HTTP
															// methods.
					requestBO.setUrl(st.trim());
					apiRequestBOList.add(requestBO);
				}

			}
		}
		catch (IOException ioe)
		{
			throw new IOException("Please check file " + fileName + " and its content,it should be readable.", ioe);
		}
		return apiRequestBOList;
	}

	public Map<String, List<APIRequestBO>> getApiResponse(Map<String, List<APIRequestBO>> urlListMap) throws Exception
	{
		Map<String, List<APIRequestBO>> fileResponseMap = new LinkedHashMap<String, List<APIRequestBO>>();
		try
		{
			for (String fileName : urlListMap.keySet())
			{
				List<APIRequestBO> urlList = urlListMap.get(fileName);

				List<APIRequestBO> responseList = getResponseObjList(urlList);

				fileResponseMap.put(fileName, responseList);
			}
		}
		catch (Exception e)
		{
			fileResponseMap = null;
			logger.error("",e);
		}
		return fileResponseMap;
	}

	public List<APIRequestBO> getResponseObjList(List<APIRequestBO> requestBOList)
	{
		List<APIRequestBO> responseBoList = new LinkedList<APIRequestBO>();
		for (APIRequestBO requestBO : requestBOList)
		{
			APIRequestBO responseBO = null;
			try
			{
				responseBO = callApi(requestBO);
				responseBoList.add(responseBO);
			}
			catch (Exception e)
			{
				logger.error("",e);;
			}
		}
		return responseBoList;
	}

	public APIRequestBO callApi(APIRequestBO requestBO) throws Exception
	{
		RequesterUtil apiCaller = new RequesterUtil();
		APIRequestBO responseBO = null;
		if (requestBO.getMethodType() == MethodType.GET)
		{
			try
			{
				responseBO = apiCaller.makeGetCall(requestBO);
			}
			catch (IOException e)
			{				
				throw new IOException("",e);
			}
		}
		else if (requestBO.getMethodType() == MethodType.POST)
		{
           
		}
		else if (requestBO.getMethodType() == MethodType.PUT)
		{

		}
		else if (requestBO.getMethodType() == MethodType.DELETE)
		{

		}
		else
		{
			logger.info("Method type not allowed");
		}
		return responseBO;
	}

	public List<ResultBO> compareResponse(Map<String, List<APIRequestBO>> fileResponseMap) throws Exception
	{
		List<ResultBO> resultBOList = new LinkedList<ResultBO>();
		boolean status = false;
		try
		{
			List<APIRequestBO> responseBoList_1 = fileResponseMap.get(file1Const);
			List<APIRequestBO> responseBoList_2 = fileResponseMap.get(file2Const);

			// Considering size of both the File is not same.
			int minSize = responseBoList_1.size() < responseBoList_2.size() ? responseBoList_1.size()
					: responseBoList_2.size();
			// For multithreaded execution
			ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

			for (int i = 0; i < minSize; i++)
			{
				APIRequestBO responseBo_1 = responseBoList_1.get(i);
				APIRequestBO responseBo_2 = responseBoList_2.get(i);

				if (responseBo_1 != null && responseBo_2 != null)
				{
					WorkerComparator worker = new WorkerComparator(responseBo_1, responseBo_2);
					Future<Boolean> status1 = executor.submit(worker);
					status = status1.get();

					ResultBO resultBO = new ResultBO();
					resultBO.setFile1Url(responseBo_1.getUrl());
					resultBO.setFile2Url(responseBo_2.getUrl());
					resultBO.setStatus(status);

					resultBOList.add(resultBO);
				}

			}
		}
		catch (Exception e)
		{
			resultBOList = null;
			throw new Exception("", e);
		}
		return resultBOList;

	}

	public void printResult(List<ResultBO> resultBoList)
	{
		System.out.println("------------------------------------------OUTPUT----------------------------------------------");
		for (ResultBO resultBO : resultBoList)
		{
			
			if (resultBO.isStatus())
			{
				System.out.println(resultBO.getFile1Url() + " equals " + resultBO.getFile2Url());
			}
			else
			{
				System.out.println(resultBO.getFile1Url() + " not equals " + resultBO.getFile2Url());
			}
		}
	}

}
