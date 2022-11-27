package com.assignment.service;

import java.util.List;
import java.util.Map;

import com.assignment.bo.APIRequestBO;

public interface IApiService
{
	public Map<String, List<APIRequestBO>> getDataFromFile(String fileName1, String fileName2) throws Exception;

	public Map<String, List<APIRequestBO>> getApiResponse(Map<String, List<APIRequestBO>> urlListMap) throws Exception;

}
