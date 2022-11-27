package com.assignment.apirequester;

import java.io.IOException;

import com.assignment.bo.APIRequestBO;
import com.assignment.utilities.Helper;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class RequesterUtil implements IApiRequest
{
	Logger logger=Logger.getLogger(RequesterUtil.class);
	
	HttpRequestBase requestBase = null;

	public APIRequestBO makeGetCall(APIRequestBO requestBO) throws Exception
	{
		requestBase = new HttpGet(requestBO.getUrl());
		APIRequestBO responseBo=null;
		try
		{
			responseBo = makeApiRequest(requestBase, requestBO);
		}
		catch (IOException ioe)
		{			
			logger.error("Exception occured for URL:\""+requestBO.getUrl(),ioe);
		}
		catch (Exception e)
		{			
			logger.error("Exception occured for URL:\""+requestBO.getUrl(),e);
		}
		return responseBo;
	}

	public APIRequestBO makePostCall(APIRequestBO requestBO)
	{
		//Not writing Post Impl as All apis is of get type
		return requestBO;
	}

	public APIRequestBO makePutCall(APIRequestBO requestBO)
	{
		return requestBO;
	}

	public APIRequestBO makeDeleteCall(APIRequestBO requestBO)
	{
		return requestBO;
	}

	@SuppressWarnings("deprecation")
	public APIRequestBO makeApiRequest(HttpRequestBase requestBase, APIRequestBO requestBO) throws Exception
	{
		String responseString = "";
		SSLSocketFactory sslsf = Helper.handleSSLCertificate();
		CloseableHttpResponse response = null;
		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		try
		{
			response = client.execute(requestBase);

			int statusCode = response.getStatusLine().getStatusCode();
			requestBO.setStatusCode(statusCode);
			responseString = EntityUtils.toString(response.getEntity());
			requestBO.setResponseString(responseString);
		}
		catch (ClientProtocolException cpe)
		{
			requestBO.setStatusCode(0);
			logger.error("Exception occured for URL "+requestBO.getUrl(), cpe);
		}
		catch (IOException ioe)
		{
			requestBO.setStatusCode(0);
			logger.error("Exception occured for URL "+requestBO.getUrl(), ioe);
		}
		catch (ParseException pe)
		{
			requestBO.setStatusCode(0);
			logger.error("Exception occured for URL "+requestBO.getUrl(), pe);
		}
		catch(Exception e)
		{
			//Setting status code 0 for exception only so that we can have that object for comparison.(Not nullyfying this object)
			requestBO.setStatusCode(0);
			logger.error("Exception occured for URL "+requestBO.getUrl(), e);
		}
		return requestBO;
	}

}
