package com.hassans.elementrepository;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class DashboardPage {

	@FindBy(xpath = "(//input[@role='combobox'])[1]")
	public WebElement customerMobileNumberField;
	@FindBy(xpath = "//button[text()='Find']")
	public WebElement findButton;
	@FindBy(xpath = "//h6[contains(text(),'Pending Sales')]")
	public WebElement statusBar;
	@FindBy(xpath = "//span[contains(@class,'MuiTypography-root MuiTypography') and contains(text(),'Last')]")
	public WebElement lastTestStatus;
	@FindBy(xpath = "//button[text()='POS']")
	public WebElement posButton;
	
}
