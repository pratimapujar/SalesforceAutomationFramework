package com.salesforce.base;

import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.time.*;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.salesforce.utilities.CommonUtilities;
import com.salesforce.utilities.GenerateReports;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseAutomation extends CommonUtilities {
	public static WebDriver driver = null;
	public static WebElement username;
	public static WebElement password;
	public static WebElement loginButton;
	public static WebElement forgotPassword;
	public static WebElement rememberMe;
	public static GenerateReports report = null;
	public static WebDriverWait wait;

	@BeforeTest
	public static void setupBeforeTest() {
		report = GenerateReports.getInstance();
		GenerateReports.startExtentReport();
		System.out.println("before test is executing");
	}

	@BeforeMethod
	public static void beforeMethod1(Method m) throws Exception {
		System.out.println("before method is executing");
		browserSel("chrome");
		GenerateReports.StartSingleTestReport(m.getName());
		loadLoginPage();
		maximizehPage();
		untilPageLoad();
		System.out.println("before method is done");
	}

	@AfterMethod
	public static void afterMethod() {
		System.out.println("after method is executing");
		report.logTestInfo("after method execution is started");
		try {
			closeDriver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterTest
	public static void afterTest() {
		System.out.println("After test method is executing");
		report.endReport();

	}

	public static WebDriver browserSel(String browser) throws Exception {

		switch (browser) {

		case "chrome":
			
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;

		case "firefox":
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();

			break;

		case "edge":
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		default:
			System.out.println("None of the chrome, edge or firefox browser selected");
		}
		return driver;
	}

	public static void loginToSalesforce() throws Exception {
		getWebElements();
		String usernamePath = getApplicationProperty("valid-username");
		enterElementText(username, usernamePath, "user name");
		String passwordPath = getApplicationProperty("valid-password");
		enterElementText(password, passwordPath, "pass word");
		takeScreenshot(username, "username");
		clickElement(loginButton, "Login Button");
	}
	
	public static void clickUserNameDropDown() throws Exception{
		WebElement usernameHomePage = driver.findElement(By.xpath("//*[@id=\"userNavLabel\"]"));
		waitUntilVisible(usernameHomePage, "user name @ Home Page");
		clickElement(usernameHomePage, "User Name at Home PAge");
	}
	
	public static void untilPageLoad() {
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50));
	}

	public static void loadLoginPage() throws InterruptedException {
		driver.get("https://login.salesforce.com/");
		
	}

	public static void maximizehPage() {
		driver.manage().window().maximize();
	}

	public static void refreshPage() {
		driver.navigate().refresh();
		report.logTestInfo("page got refreshed");
	}
	
	public static String getPageTitle() {
		String title = driver.getTitle();
		return title;
	}

	public static void switchToFrame(WebElement element) {
		driver.switchTo().frame(element);
	}
	
	public static void switchToFrame(String text) {
		driver.switchTo().frame(text);
	}
	
	public static void switchToDefaultWindow() {
		driver.switchTo().defaultContent();
	}
	
	public static void getWebElements() {
		username = driver.findElement(By.id("username"));
		password = driver.findElement(By.id("password"));
		loginButton = driver.findElement(By.xpath("//input[@id=\"Login\"]"));
		forgotPassword = driver.findElement(By.xpath("//*[@id=\"forgot_password_link\"]"));
		rememberMe = driver.findElement(By.xpath("//*[@id=\"rememberUn\"]"));

	}

	public static void clearElementText(WebElement element, String objName) {
		if (element.isDisplayed()) {
			element.clear();
			report.logTestInfo(objName + " field text cleared successfully");
			// System.out.println(objName + " field text cleared successfully");
		} else
			report.logTestFail(objName + " not displayed");
		// System.out.println(objName + " not displayed");
	}

	public static void enterElementText(WebElement element, String text, String objName) throws Exception {
		try {
			waitUntilVisible(element, objName);
			clearElementText(element, objName);

			element.sendKeys(text);
			report.logTestInfo("text entered in " + objName + " field successfully");
			// System.out.println("text entered in " + objName + " field successfully");
		} catch (Exception e) {
			report.logTestFail(objName + " not displayed");
			// System.out.println(objName + " not displayed");
		}

	}

	public static void clickElement(WebElement element, String objName) throws Exception {
		if (element.isDisplayed()) {
			waitUntilClickable(element);
			element.click();
			report.logTestInfo(objName + " pressed scuccesfully.");
			// System.out.println(objName + " pressed scuccesfully.");
		} else
			report.logTestFail(objName + " not displayed");
		// System.out.println(objName + " not displayed");

	}

	public static void moveToElement(WebElement element, String objName) throws Exception {
		Actions ac = new Actions(driver);
		ac.moveToElement(element).build().perform();

	}
	
	public static void sleep(int value) throws InterruptedException {
		Thread.sleep(value);
	}

	public static void waitUntilVisible(WebElement element, String objName) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOf(element));

	}

	public static void waitUntilClickable(WebElement element) {
		wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.elementToBeClickable(element));

	}

	public static void handlePopup(WebElement element1, WebElement element2, String objName) throws Exception {

		if (element1.isDisplayed()) {
			clickElement(element2, objName);

		} else
			report.logTestFail("Pop up on account click isn't displayed");
		// System.out.println("Pop up on account click isn't displayed");
	}

	public static void selectElement(WebElement element, String text, String objName) {
		Select sc = new Select(element);
		if (element.isEnabled()) {
			sc.selectByValue(text);
		}
		else
			report.logTestFail(objName + " is not enabled");
		// System.out.println(objName + " is not enabled");
	}
	
	public static void selectElementByVisibleText(WebElement element, String text, String objName) {
		Select sc = new Select(element);
		if (element.isEnabled()) {
			sc.selectByVisibleText(text);
		}
		else
			report.logTestFail(objName + " is not enabled");
		// System.out.println(objName + " is not enabled");
	}
	
//Method Overloading
	public static void selectElement(WebElement element, int value, String objName) {
		Select sc = new Select(element);
		if (element.isEnabled()) {
			sc.selectByIndex(value);
		}
		else
			report.logTestFail(objName + " is not enabled");
		// System.out.println(objName + " is not enabled");
	}
	
	
	public static String readText(WebElement element, String objName) {
		waitUntilVisible(element, objName);
		String text = element.getText();
		return text;

	}

	public static void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	public static void dismissAlert() {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}
	
	public static void checkCheckBox(WebElement element) {
		if (element.isSelected())
			System.out.println("Already selected");
		else
			try {
				clickElement(element, "checkBox1");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static void takeScreenshot(WebElement element, String objName) {
		// it will take screenshot of single element
		File scrFile = element.getScreenshotAs(OutputType.FILE);

		// this is to take full screen screenshot, method available in firefox only
		File src1File = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		LocalDateTime date = LocalDateTime.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy_hh_mm_ss");
		String dateTime = dateFormat.format(date);
		File dstFile = new File(System.getProperty("user.dir") + "/Screenshots/" + dateTime + ".jpg");
		try {
			// FileHandler.copy(scrFile, dstFile);
			FileHandler.copy(src1File, dstFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			report.logTestFail("problem while takign screenshot");
			// System.out.println("problem while takign screenshot");
		}

	}
	public static void switchToOpenWindow(String baseWindowHandle) {
		Set<String> allWindowHandle = driver.getWindowHandles();
		for (String handle : allWindowHandle) {
			if (!baseWindowHandle.equals(handle)) {
				driver.switchTo().window(handle);
				System.out.println("Switched to developer console windown");
				break;
			}
			String title = driver.getTitle();
			System.out.println("Current windown is  " + title);
		}
		
	}

	public static void closeDriver() throws Exception {
		Thread.sleep(8000);
		driver.close();
	}

}