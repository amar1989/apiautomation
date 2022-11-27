package com.assignment;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.assignment.comparator.ComparatorUtil;

public class ComparatorTest
{
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
