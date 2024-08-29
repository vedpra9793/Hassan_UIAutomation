package com.hassans.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.hassans.ui.selenium.DriverActions;


public class LoginPage extends com.hassans.elementrepository.LoginPage{

	private DriverActions driverAction;

//	public void inputUserName(String inputuserName) {
//		driverAction.waitForElement(userName);
//		driverAction.inputData(userName, inputuserName,"UserName");
//	}
//
//	public void inputPassword(String inputPassword) {
//		driverAction.waitForElement(password);
//		driverAction.inputData(password, inputPassword,"Pasword");
//	}
//	public void loginButton() {
//
//		driverAction.clickElement(loginButton,true,"Login");
//	}


	public LoginPage(DriverActions das) {
		this.driverAction=das;
		PageFactory.initElements(driverAction.getWebDriver(), this);
	}



	public void login() throws Exception {
		enterUserName();
		enterPassword();
		clickOnLoginBtn();
	}
	
	

	public void enterUserName() throws Exception {
		Thread.sleep(5000);
		driverAction.waitForElement(userNameInputBox);
		driverAction.clickElement(userNameInputBox,true,"UserName");
		driverAction.inputData(userNameInputBox, "system","UserName");

	}
	public void enterPassword() throws Exception {
		driverAction.clickElement(passwordInputBox,true,"Password");
		driverAction.inputData(passwordInputBox, "Admin@123","Pasword");

	}
	public void clickOnLoginBtn() throws Exception {
		driverAction.clickElement(login,true,"Login Button");
	}


	public void logOut() throws Exception {
		driverAction.waitForElement(profileIcon);
		driverAction.clickElement(profileIcon,true,"profile icon");
		driverAction.waitForElement(logOutBT);
		driverAction.clickElement(logOutBT,true,"sign out");
	}
}
