package com.hassans.ui.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.google.common.io.Files;

import cucumber.runtime.Timeout;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class has wrapper functions for any Browser actions that can be taken on a web page like click, sendKeys, openUrl, etc,.
 * 
 * @author gaurav
 *
 */

public class DriverActions{

	protected WebDriver driver;
	protected Wait<WebDriver> webDriverWait;
	private ArrayList<String> tabs;
	public static final long DEFAULT_WAIT_TIME = 20;
	public static final long POOLING_WAIT_TIME = 2;
	protected Actions actions;

	private ExtentReports er;
	public ExtentTest etest;
	private Properties prop;

	public DriverActions(WebDriver webDriver) {
		this.driver = webDriver;
		actions = new Actions(webDriver);
		this.webDriverWait = new FluentWait<WebDriver>(driver)                            
				.withTimeout(Duration.ofSeconds(DEFAULT_WAIT_TIME))          
				.pollingEvery(Duration.ofSeconds(POOLING_WAIT_TIME))          
				.ignoring(NoSuchElementException.class);
	}


	public  String openUrl() {


		String url= getProperties().getProperty("url");
		try {
			driver.get(url);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public void getLocalInstance(String browser) throws Exception {

		if (browser.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver= new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("Chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments("--start-fullscreen");


			driver= new ChromeDriver(chromeOptions);

		} else if (browser.equalsIgnoreCase("EdgeC") || browser.equalsIgnoreCase("EdgeChromium")) {

		}else if(browser.equalsIgnoreCase("safari")){

		}


		driver.manage().window().maximize();


		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.MINUTES);
	}



	public WebDriver getWebDriver() {
		return driver;
	}
	public Properties getProperties() {
		return prop;
	}

	public ExtentTest etest() {
		return etest;
	}

	public void createReport() {

		ExtentHtmlReporter ereport=new ExtentHtmlReporter("extentReport//report.html");
		ereport.config().setReportName("DemoBlaze");
		ereport.config().setTheme(Theme.DARK);
		er=new ExtentReports();
		er.attachReporter(ereport);

	}


	public void createTest(String tsName) {
		etest=er.createTest(tsName);

	}
	public void generateReport() {
		er.flush();
	}

	public void moveToElementAndClick(WebElement we) {
		actions = new Actions(driver);
		actions.moveToElement(we).click().build().perform();

	}

	public String takeScreenShot(String snapshotName) {
		TakesScreenshot ts=(TakesScreenshot)driver;
		File source=ts.getScreenshotAs(OutputType.FILE);
		String dateAndTime=dateAndTime();
		File destination=new File(System.getProperty("user.dir")+"/extentReport/"+snapshotName+".png");


		try {
			Files.copy(source, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return destination.getAbsolutePath();
	}

	public void attachsnapshotToextentReport(String imgPath) {

		try {
			etest.addScreenCaptureFromPath(imgPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String dateAndTime() {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();  
		String dateTime= formatter.format(date);  
		return dateTime;

	}

	public void uiText_validation(String actualText,String expectedText) {
		try {
			if(actualText.equals(expectedText)) {
				etest.log(Status.PASS, "actualText "+actualText+" and expectedText "+expectedText+" matched");
			}else {
				etest.log(Status.FAIL, "actualText "+actualText+" and expectedText "+expectedText+" not matched");
			}
		}catch(Exception e) {
			etest.log(Status.FAIL, "actualText "+actualText+" and expectedText "+expectedText+" not matched");

		}
	}

	public void dragAndDropByHold(WebElement source,WebElement target) {
		actions.clickAndHold(source).moveToElement(target).release(target).build().perform();
	}

	public String getAlertMessage() {

		return driver.switchTo().alert().getText();
	}

	public void waitForElement(WebElement we) {

		WebDriverWait wait=new WebDriverWait(getWebDriver(), 50);
		wait.until(ExpectedConditions.visibilityOf(we));

	}

	public void inputData(WebElement we,String data,String elementName) {

		try{
			we.clear();
			we.sendKeys(data);
			etest.log(Status.INFO, data+" is inputed in "+elementName);


		}catch (ElementNotInteractableException e) {
			Actions ac =new Actions(driver);
			ac.sendKeys(we,data).build().perform();
			etest.log(Status.INFO, data+" is inputed in "+elementName);

		}catch (Exception e) {

			JavascriptExecutor js= (JavascriptExecutor) driver;
			js.executeScript("arguments[0].value["+data+"]", we);
			etest.log(Status.INFO, data+" is inputed in "+elementName);
		}

	}


	public void javaScriptSendKey(WebElement we,String data,String elementName) {
		JavascriptExecutor js= (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value='"+ data +"';", we);
		etest.log(Status.INFO, data+" is inputed in "+elementName);
	}

	public void invisibilityOfElement(WebElement we) {

		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.invisibilityOf(we));

	}

	public Properties loadPropertiesfile() {

		InputStream inputStream=null;
		try {
			inputStream = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/config.properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		prop=new Properties();

		try {
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}


	public void actionClick(WebElement we, String elementName) {
		actions=new Actions(driver);
		etest.log(Status.INFO, " Clicked on "+elementName);
		actions.click(we).build().perform();

	}
	public void actionSendKey(WebElement we,String data ,String elementName) {
		actions=new Actions(driver);
		etest.log(Status.INFO, " input data in "+elementName);
		actions.sendKeys(we,data).build().perform();

	}


	/**
	 * Returns the Current Page's Title
	 * 
	 * @return browserTitle
	 */
	public String getBrowserTitle() {
		String title = driver.getTitle();
		etest.log(Status.INFO, title+" is extrect");
		return title;
	}

	/**
	 * Returns the Current Page's URL
	 * 
	 * @return browserURL
	 */
	public String getBrowserURL() { 												 
		String url = null;
		url = driver.getCurrentUrl();
		etest.log(Status.INFO, url+" is extract");
		return url;
	}

	public void acceptAlert() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.switchTo().alert().accept();
		etest.log(Status.INFO,"Alert  accepted");
	}

	public void ClickElementJavaScript(WebElement we,String Element) {

		JavascriptExecutor js=(JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", we);

	}


	public void doubleclickElement(WebElement element,String elementName) {
		actions=new Actions(driver);
		actions.doubleClick(element).build().perform();
		//		return clickElement(element, true,elementName);
	}


	public boolean clickElement(WebElement element, boolean retry,String elementName) {
		boolean result = false;
		if (element != null) {
			try {
				element.click();
				etest.log(Status.INFO, "clicked on "+elementName);
				result = true;
			} catch (ElementNotInteractableException e) {
				if(retry) {
					Actions ac=new Actions(driver);
					ac.moveToElement(element).click(element).build().perform();
					etest.log(Status.INFO, "clicked on "+elementName);
				} 			
				return result;
			}catch (NoSuchElementException | StaleElementReferenceException f) {
				String xpath = element.toString().split("xpath:")[1]; 
				String xPath = xpath.substring(0, xpath.length() - 1);			
				return result;
			} 
			catch (Exception e) {

				return false;

			}
		}
		return result;
	}
	public void clickJavaScript(WebElement element,String elementName) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
		etest.log(Status.INFO, "clicked on"+elementName);

	}

	/**
	 * Verify if checkbox is Checked or not, using JavaScript
	 * 
	 * @param WebElement
	 * @return true/false
	 */
	public Boolean isCheckBoxChecked(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		Boolean checkBox = (Boolean) executor.executeScript("return arguments[0].checked", element);
		return checkBox;
	}

	/**
	 * Right Click a WebElement
	 * 
	 * @param element
	 * @param Item
	 */
	public void rightClick(WebElement element,String elementName) {
		Actions action = new Actions(driver);
		action.moveToElement(element);
		action.contextClick(element).build().perform();
		etest.log(Status.INFO, "Right clicked on"+elementName);
	}



	public void scrollToElement(WebElement element, boolean isElementAlignedToTopOfView,String elementName) {
		String js = "arguments[0].scrollIntoView(" + isElementAlignedToTopOfView + ");";
		executeScript(js, element);
		etest.log(Status.INFO, "scrolled on"+elementName);
	}



	public void scrollDown(WebElement element) {
		executeScript("arguments[0].lastChild.scrollIntoView(true);", element);
		etest.log(Status.INFO, "scroll down");
	}

	public void executeScript(String script, Object... args) {
		((JavascriptExecutor) driver).executeScript(script, args);
	}

	public String getText(WebElement element) {
		String text = null;
		if (element != null) {
			text = element.getText();
			if (text.length() != 0)
				System.out.println(text);
			etest.log(Status.INFO, element.getText()+" is extract");
		} else {
			return null;
		}
		return text.trim();
	}
	public String getAttribute(WebElement element,String attributeName) {
		String text = null;
		if (element != null) {
			text = element.getAttribute(attributeName);
			if (text.length() != 0)
				System.out.println(text);
			etest.log(Status.INFO, element.getAttribute(attributeName)+" is extract");
		} else {
			return null;
		}
		return text.trim();
	}
	public Boolean isEnabled(WebElement element) {
		return element.isEnabled() ? true : false;
	}

	public boolean isDisplayed(WebElement we) {
		return we.isDisplayed() ? true : false;
	}

	public boolean isSelected(WebElement element) {
		return (element.isSelected()) ? true : false;
	}



	public void mouseOverByAction(WebElement element) {
		Actions action = new Actions(driver);

		action.moveToElement(element).build().perform();
	}

	public void switchToIFrame(WebElement element) {
		driver.switchTo().frame(element);
	}

	public void switchToParentFrame() {
		driver.switchTo().parentFrame();
	}


	public void switchToNewTab() {
		tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}


	public void switchToParentTab() {
		driver.close();
		driver.switchTo().window(tabs.get(0));
	}

	public void selectOptionByValue(WebElement element, String optionToSelect) {
		Select sel = new Select(element);
		sel.selectByValue(optionToSelect);
	}


	public void selectOptionByVisibleText(WebElement locator, String optionToSelect) {
		Select sel = new Select(locator);
		sel.selectByVisibleText(optionToSelect);
	}


	public void navigateBack() {

		driver.navigate().back();

	}

	public void navigateForward() {

		driver.navigate().forward();

	}

	public boolean isAlertPresents() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void switchWindow(WebDriver driver) {
		String win=driver.getWindowHandle();
		Set<String> hand=driver.getWindowHandles();
		Iterator<String> it=hand.iterator();
		while(it.hasNext()) {
			String value=it.next();
			if(!win.equals(value)) {
				driver.switchTo().window(value);
				break;
			}
		}
	}

	public void closeBrowser() {
		driver.close();
	}
	public DriverActions() {
		loadPropertiesfile();
	}
}
