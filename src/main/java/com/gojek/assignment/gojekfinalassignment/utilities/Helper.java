package com.gojek.assignment.gojekfinalassignment.utilities;

import java.io.IOException;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class Helper
{
	private static Logger logger = Logger.getLogger(Helper.class);

	public static Map<String, List<Object>> getDataMap(String responseJson) throws IOException, XMLStreamException
	{
		Map<String, List<Object>> dataMap = null;

		ObjectMapper mapper = new ObjectMapper();
		try
		{
			JsonNode rootNode1 = mapper.readTree(responseJson);

			Map<String, List<Object>> tempDataMap = new LinkedHashMap<String, List<Object>>();
			// iterateObject is a recursion that write data in a map so sending
			// empty map as parameter.

			dataMap = parseJson(rootNode1, "", tempDataMap);
		}
		catch (Exception e)
		{
			logger.error("Exception occured in parsing JSON.", e);
		}

		return dataMap;
	}

	public static Map<String, List<Object>> parseJson(JsonNode node, String parentName,
			Map<String, List<Object>> dataMap) throws IOException
	{

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();

		while (fieldsIterator.hasNext())
		{
			try
			{
				Map.Entry<String, JsonNode> field = fieldsIterator.next();

				String fieldKey = field.getKey();
				JsonNode jsonNode = field.getValue();
				if (jsonNode.getNodeType() == JsonNodeType.OBJECT)
				{
					parseJson(jsonNode, fieldKey, dataMap);
				}
				else if (jsonNode.getNodeType() == JsonNodeType.ARRAY)
				{
					if (jsonNode.get(0) != null)
					{
						if (jsonNode.get(0).getNodeType() == JsonNodeType.OBJECT
								|| jsonNode.get(0).getNodeType() == JsonNodeType.ARRAY)
						{
							for (int i = 0; i < jsonNode.size(); i++)
							{
								parseJson(jsonNode.get(i), fieldKey, dataMap);
							}
						}
						else
						{
							List<Object> valueList = new ArrayList<Object>();
							for (int i = 0; i < jsonNode.size(); i++)
							{
								valueList.add(jsonNode.get(i));
							}
							dataMap.put(fieldKey, valueList);
						}
					}
				}

				else
				{
					if (parentName == "")
					{
						if (dataMap.get(parentName) == null)
						{
							List<Object> nodeList = new ArrayList<Object>();
							nodeList.add(jsonNode);
							dataMap.put(fieldKey, nodeList);
						}
						else
						{
							dataMap.get(parentName).add(jsonNode);
						}

					}
					else
					{

						if (dataMap.get(parentName + "." + fieldKey) == null)
						{
							List<Object> nodeList = new ArrayList<Object>();
							nodeList.add(jsonNode);
							dataMap.put(parentName + "." + fieldKey, nodeList);
						}
						else
						{
							dataMap.get(parentName + "." + fieldKey).add(jsonNode);
						}

					}

				}
			}
			catch (Exception e)
			{
				logger.error("Exception occured in parsing JSON:", e);
			}
		}
		return dataMap;

	}

	public static boolean compareResponseMap(Map<String, List<Object>> compareMap1,
			Map<String, List<Object>> compareMap2)
	{
		try
		{
			for (String key : compareMap1.keySet())
			{
				if (!compareMap1.get(key).equals(compareMap2.get(key)))
				{
					return false;
				}
			}
			for (String y : compareMap1.keySet())
			{
				if (!compareMap2.containsKey(y))
				{
					return false;
				}
			}
		}
		catch (NullPointerException np)
		{
			logger.error("",np);
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public static SSLSocketFactory handleSSLCertificate()
	{
		SSLContext sslcontext = SSLContexts.createSystemDefault();
		SSLSocketFactory sslsf = new SSLSocketFactory(sslcontext) {

			@Override
			public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host,
					InetSocketAddress remoteAddress, InetSocketAddress localAddress, HttpContext context)
					throws IOException, ConnectTimeoutException
			{
				if (socket instanceof SSLSocket)
				{
					try
					{
						PropertyUtils.setProperty(socket, "host", host.getHostName());
					}
					catch (NoSuchMethodException ex)
					{
					}
					catch (IllegalAccessException ex)
					{
					}
					catch (InvocationTargetException ex)
					{
					}
				}
				return super.connectSocket(connectTimeout, socket, host, remoteAddress, localAddress, context);
			}

		};
		return sslsf;
	}

	public static boolean compareResponseString(String responseString1, String responseString2)
			throws IOException, XMLStreamException
	{
		Map<String, List<Object>> dataMap1 = getDataMap(responseString1);
		Map<String, List<Object>> dataMap2 = getDataMap(responseString2);
		boolean status = compareResponseMap(dataMap1, dataMap2);
		return status;
	}
}
