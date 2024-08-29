package com.hassans.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.hassans.ui.selenium.DriverActions;

import java.util.ArrayList;
import java.util.Random;

public class DashboardController extends com.hassans.elementrepository.DashboardPage {


	private DriverActions driverAction;
	public DashboardController(DriverActions das) {
		this.driverAction=das;
		PageFactory.initElements(driverAction.getWebDriver(), this);
	}


	public String getCustomerMobileText() {
		String text = null;

		try {
			Thread.sleep(5000);
			driverAction.waitForElement(customerMobileNumberField);
			text = driverAction.getAttribute(customerMobileNumberField,"value");
			System.out.println(text);
		} catch (Exception e) {

		}
		return text;
	}
	public void getStatusText() {

		try {
			driverAction.waitForElement(statusBar);
			driverAction.getText(statusBar);
		} catch (Exception e) {

		}
	}

	public String selectCustomerNumber() throws Exception  {
		Thread.sleep(3000);
		driverAction.waitForElement(customerMobileNumberField);
		driverAction.clickElement(customerMobileNumberField,true,"Customer Mobile number");
		Thread.sleep(3000);
		driverAction.waitForElement(driverAction.getWebDriver().findElement(By.xpath("//ul[@role='listbox']//li[2]")));
		driverAction.clickElement(driverAction.getWebDriver().findElement(By.xpath("//ul[@role='listbox']//li["+randomNumberGenerator()+"]")),true,"");
		return driverAction.getAttribute(customerMobileNumberField,"value");
	}
	public int randomNumberGenerator() {
		Random rand = new Random();
		// Will work for [0 - 9].
		int n = rand.nextInt(10);
		return n;
	}
	public boolean getlatestPrescription() throws Exception  {

		return driverAction.isDisplayed(lastTestStatus);
	}
	public String getStatus() throws Exception  {

		return driverAction.getText(statusBar);
	}
	public void clickPOS() throws Exception {
		driverAction.waitForElement(posButton);
		driverAction.clickElement(posButton,true,"POS");
	}
}
