package testcases;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import common.CommonActions;
import common.ElementIdentifiers;
import loggerFunctions.CustomAsserts;

public class VerifyToolTips extends CommonActions {

	String browserToUse;
	String webApplicationURL;
	String testcaseFile;
	WebDriver browserDriver;
	String ById = "id";
	String ByXpath = "xpath";
	String ByClassName = "className";
	CustomAsserts asserts = new CustomAsserts();

	@BeforeTest
	@Parameters({ "browser", "appURL" })
	public void initializeValues(String browser, String appURL) {
		browserToUse = browser;
		webApplicationURL = appURL;
	}

	@BeforeTest
	public void launchWebApp() {
		/*
		 * launch browser using the url specified in the testng.xml file
		 */
		browserDriver = launchURLInBrowser(webApplicationURL, browserToUse);
		asserts.assertNotNull(browserDriver,
				"Unable to launch browser.Terminating script....");
	}

	@AfterTest
	public void closeWebApp() {
		// Close the browser
		closeBrowser(browserDriver);

	}

	@Test()
	public void verifyToolTip() {

		Point coordinates1;
		Point coordinates2;
		Point coordinates3;
		String contactNameTextBoxToolTip;
		String contactEmailTextBoxToolTip;
		String contactIssueDetailsTextAreaToolTip;

		WebElement contactUsButton = waitAndSearchElementUntilClickable(ById,
				ElementIdentifiers.contactUsButtonId);

		asserts.log("Searching and clicking on the CONTACT US button....");
		contactUsButton.click();
		asserts.log("CONTACT US button was clicked");

		WebElement contactForm = waitAndSearchElementUntilVisible(ById,
				ElementIdentifiers.contactFormId);
		asserts.assertNotNull(contactForm,
				"Contact Form was not launched properly...");
		asserts.assertTrue(contactForm.isDisplayed(),
				"Contact us form is not displayed");
		asserts.log("Contact Form is displayed...");

		WebElement contactNameTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactNameTextBoxId);
		WebElement contactEmailTextBox = searchElementInParentElement(
				contactForm, ById, ElementIdentifiers.contactEmailTextBoxId);
		WebElement contactIssueDetailsTextArea = searchElementInParentElement(
				contactForm, ById,
				ElementIdentifiers.contactIssueDetailsTextAreaId);

		asserts.log("TESTCASE - Verify tool tip for the text boxes on the Contact us form.");

		pauseExecution(5);
		// contactNameTextBox.click();
		asserts.log("Hovering the mouse over the 'Your Name' textbox and checking the tool tip for it....");
		coordinates1 = contactNameTextBox.getLocation();
		mouseMoveToLocation(coordinates1.getX() + 97, coordinates1.getY() + 97);
		pauseExecution(5);
		contactNameTextBoxToolTip = contactNameTextBox.getAttribute("title");
		asserts.verifyTrue(
				(contactNameTextBoxToolTip).equalsIgnoreCase("Name"),
				contactNameTextBoxToolTip + "- displayed as tooltip.",
				contactNameTextBoxToolTip + "- not displayed as tooltip.");

		// contactEmailTextBox.click();
		asserts.log("Hovering the mouse over the 'Your Email address' textbox and checking the tool tip for it....");
		coordinates2 = contactEmailTextBox.getLocation();
		mouseMoveToLocation(coordinates2.getX() + 97, coordinates2.getY() + 97);
		pauseExecution(5);
		contactEmailTextBoxToolTip = contactEmailTextBox.getAttribute("title");
		asserts.verifyTrue(
				(contactEmailTextBoxToolTip).equalsIgnoreCase("Email"),
				contactEmailTextBoxToolTip + "- displayed as tooltip.",
				contactEmailTextBoxToolTip + "- not displayed as tooltip.");

		// contactIssueDetailsTextArea.click();
		asserts.log("Hovering the mouse over the 'How can we help you?' textbox and checking the tool tip for it....");
		coordinates3 = contactIssueDetailsTextArea.getLocation();
		mouseMoveToLocation(coordinates3.getX() + 97, coordinates3.getY() + 97);
		pauseExecution(5);
		contactIssueDetailsTextAreaToolTip = contactIssueDetailsTextArea
				.getAttribute("title");
		asserts.verifyTrue((contactIssueDetailsTextAreaToolTip)
				.equalsIgnoreCase("Problem description"),
				contactIssueDetailsTextAreaToolTip + "- displayed as tooltip.",
				contactIssueDetailsTextAreaToolTip
						+ "- not displayed as tooltip.");

	}

}