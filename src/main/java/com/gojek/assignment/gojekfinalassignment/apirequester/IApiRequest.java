package com.gojek.assignment.gojekfinalassignment.apirequester;

import com.gojek.assignment.gojekfinalassignment.bo.APIRequestBO;

public interface IApiRequest
{
	public APIRequestBO makeGetCall(APIRequestBO requestBO) throws Exception;

	public APIRequestBO makePostCall(APIRequestBO requestBO);

	public APIRequestBO makePutCall(APIRequestBO requestBO);

	public APIRequestBO makeDeleteCall(APIRequestBO requestBO);
}
