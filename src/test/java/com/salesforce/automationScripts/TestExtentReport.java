package com.salesforce.automationScripts;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class TestExtentReport {
	
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports report;
	public static ExtentTest test;

	public static void main(String[] args) {
		
		htmlReporter = new ExtentHtmlReporter("./extentReport.html");
		htmlReporter.config().setDocumentTitle("Test Execution Report");
		htmlReporter.config().setReportName("Salesforce Automation");
		htmlReporter.config().setTheme(Theme.STANDARD);
		
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		report.setSystemInfo("Host Name", "Salesforce");
		report.setSystemInfo("Environment", "Automation Testing");
		
		test=report.createTest("Test1");
		test.log(Status.INFO, "info mesaage");
		test.log(Status.PASS,MarkupHelper.createLabel("test1" + "is passed", ExtentColor.GREEN));
		test.log(Status.FAIL, MarkupHelper.createLabel("test1" + "is failed", ExtentColor.RED));
		
		report.flush();
		System.out.println("Execution complete");

	}

}