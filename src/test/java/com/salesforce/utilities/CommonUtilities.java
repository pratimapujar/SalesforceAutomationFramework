package com.salesforce.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class CommonUtilities {

	public static String getApplicationProperty(String key) {

		Properties properties = new Properties();

		String filePath = Constants.APPLICATION_PROPERTIES_PATH;

		FileInputStream inputFile = null;
		try {
			inputFile = new FileInputStream(filePath);
		}

		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		String value = null;

		try {
			try {
				properties.load(inputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			value = properties.getProperty(key);
			System.out.println("Property from file is " + value);
		} finally {
			try {
				inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return value;
	}
}