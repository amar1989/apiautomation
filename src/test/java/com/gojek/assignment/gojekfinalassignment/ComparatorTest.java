package com.gojek.assignment.gojekfinalassignment;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.gojek.assignment.gojekfinalassignment.bo.APIRequestBO;
import com.gojek.assignment.gojekfinalassignment.bo.ResultBO;
import com.gojek.assignment.gojekfinalassignment.comparator.ComparatorUtil;
import com.gojek.assignment.gojekfinalassignment.service.ApiServiceImpl;

public class ComparatorTest
{
	Logger logger = Logger.getLogger(ComparatorTest.class);

	public ComparatorTest()
	{
		Properties props = new Properties();
		try
		{
			props.load(new FileInputStream("log4j.properties"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PropertyConfigurator.configure(props);
	}

	@Test
	public void testComparator()
	{
		String file1="UrlFile1.txt";
		String file2="UrlFile2.txt";
		ComparatorUtil comparatorUtil=new ComparatorUtil();
		boolean status=comparatorUtil.compare(file1, file2);
		Assert.assertTrue(status);
	}
}
