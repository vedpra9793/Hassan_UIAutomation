package com.hassans.tests;

import java.lang.reflect.Method;

import org.json.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.hassans.json.JsonParser;
import com.hassans.pages.LoginPage;
import com.hassans.ui.selenium.DriverActions;


public class BaseTestSuite{

	protected static DriverActions driverAction ;

	public JSONObject placeOrderdata;
	public JSONObject contactPagedata;

	@BeforeSuite
	public void createObject() {
		driverAction=new DriverActions();
		driverAction.createReport();
	}


	
	@BeforeTest(alwaysRun = true)
	@Parameters({"browserName"})
	public void setUp(String browserName) throws Exception{
		
		driverAction.getLocalInstance(browserName);
		driverAction.openUrl();
	}

	@AfterTest(alwaysRun = true)
	public void afterTestThreadContextCleanup() {
		driverAction.closeBrowser();
		driverAction.generateReport();
	}

	@BeforeMethod(alwaysRun = true)
	public void verifyloginPage(Method tsName) {

		placeOrderdata = JsonParser.parse("placeOrderTestData.json");
		contactPagedata = JsonParser.parse("contactPageData.json");

		String testCaseName=tsName.getName();
		driverAction.createTest(testCaseName);

		LoginPage lp=new LoginPage(driverAction);
		try {
			Thread.sleep(8000);
			lp.login();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		HomeLandingPage homePageLanding=new HomeLandingPage(driverAction);
//		LoginPage loginPage=homePageLanding.clickOnLoginTab();
//		String userName=driverAction.getProperties().getProperty("userName");
//		String password=driverAction.getProperties().getProperty("Password");
//		loginPage.inputUserName(userName);
//		loginPage.inputPassword(password);
//		loginPage.loginButton();


	}

	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result,Method m) {
		int status=result.getStatus();
		if(status==ITestResult.FAILURE) {
			String imgPath=driverAction.takeScreenShot(m.getName());
			driverAction.attachsnapshotToextentReport(imgPath);

		}

		LoginPage lp=new LoginPage(driverAction);
		try {
			lp.logOut();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driverAction.generateReport();
	}

	@AfterSuite
	public void generateReport() {
		driverAction.generateReport();
	}
}
