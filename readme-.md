# Project Title
This project if for comparing responses of APIs whose URL are stored in Two .txt files.

### Prerequisites

java 1.8 and maven if project is being imported in IDE,maven not required if used as a jar.

### Installing
Java 1.8
Maven(If it is being imported in IDE,however default maven support is also provided by most of IDEs)

## Running the tests
import ComparatorUtil;
1.create two .txt file and put get APIs Url in that
2.boolean status=comparatorUtil.compare(file1, file2);
This method will return true if all api responses gets compared successfully. 

##Start Point
ComparatorTest inside src/test/java

## Built With
Maven

##Project Flow:
1.APIRequestBO:
  This is pojo containing all details of a particular request.Property:url,statusCode,responseString,methodType.
  Initially it is initialized with Url and method type but after making request to API statusCode and responseString gets initialized.
2.MethodType is enum containing different HTTP methods.like Get, Post, Put,Delete.
3.compare(String file1,String file2) 
   This method takes two file names as parameter containing n numbers of API Urls.
   Inside compare method doing batch processing for memory management that at one time only 1000 records of each file will be read and that much reqquests will be processed.This
   will be useful if both files will have millions of records then there will be memory issue in keeping all the details in memory.
  2.1. Map<String, List<APIRequestBO>> urlMap = serviceImpl.getDataFromFile(file1, file2);
       This method returns the Map containing List of API details POJO "APIRequestBO" specific to file i.e key of Map is file name and value is List Of Pojo containig details of 
	   each request.
  2.2.Map<String, List<APIRequestBO>> fileResponseMap = serviceImpl.getApiResponse(urlMap);
      Method getApiResponse() returns Map containing List of POJO but this POJO contaiins status code and Response String also.
	  
4> Comparision:
   Comparison is being done node to node of response json of both APIs i.e each node of one response json is compared with that of others.
   How Comparison is Being Done:
   1.First creating two Maps after parsing response json of both APIs.
     method involved: Helper.getDataMap(String responseJson)
   2.Comparing Both Maps:
     Method Involved:Helper.compareResponseMap(Map<String, List<Object>> compareMap1,
			Map<String, List<Object>> compareMap2)
5> Used ThreadPool Executor for parallel execution.
   Method:ApiServiceImpl.compareResponse(Map<String, List<APIRequestBO>> fileResponseMap)
   
6> Implemented apache Log4j for logging.

7>SSL certificate issue handled.
   
