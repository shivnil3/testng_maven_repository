package common;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SubjectTerm;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;

import loggerFunctions.CustomAsserts;

import com.opera.core.systems.OperaDriver;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.testng.SauceOnDemandAuthenticationProvider;
import com.saucelabs.testng.SauceOnDemandTestListener;

/**
 * Contains the common methods used by the test cases
 * 
 * @author Nilesh Awasthey
 *
 */

public class CommonActions
// implements /*SauceOnDemandSessionIdProvider,*/
// SauceOnDemandAuthenticationProvider
{

	private String ieDriverExe = "src/main/resources/BrowserDriverExe/IEDriverServer.exe";
	private String chromeDriverExe = "src/main/resources/BrowserDriverExe/chromedriver.exe";
	private Class<String> paramString;
	private Class<?> byClass;
	private final String byClassName = "org.openqa.selenium.By";
	private Method byMethod;
	private WebDriver currentDriver;
	private String sauceOnDemandUsername;
	private String sauceOnDemandAccessKey;
	private long waitTimeInSeconds = 30;
	private String testCaseName;

	CustomAsserts asserts = new CustomAsserts();

	private enum Bylocator {
		id, className, name, tagName, cssSelector, linkText, partialLinkText, xpath
	};

	/**
	 * Constructor for the class
	 * 
	 */
	public CommonActions() {
		try {
			byClass = Class.forName(byClassName);
			paramString = String.class;
		} catch (ClassNotFoundException e) {
			asserts.log(byClassName + " class not found!!!");
			asserts.addVerificationFailure(e);
		}

	}

	public void launchUrlInBrowser(WebDriver driver, String url) {
		driver.get(url);
	}

	/**
	 * Close the browser
	 * 
	 * @param driver
	 */
	public void closeBrowser(WebDriver driver) {
		driver.quit();
	}

	/**
	 * Searches and returns object/WebElement based on ByLocator and locator
	 * values using the driver object
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElement
	 */

	public WebElement searchElement(String byLocator, String locatorValue) {
		WebElement uiElement = null;

		if (checkInSupportedByLocatorList(byLocator)) {
			try {
				byMethod = byClass.getDeclaredMethod(byLocator, paramString);
				uiElement = getCurrentDriver().findElement(
						(By) byMethod.invoke(byClass, locatorValue));
			} catch (NoSuchElementException e) {
				asserts.log("WebElement having " + byLocator + " as "
						+ locatorValue + "- not found...");
			} catch (NoSuchMethodException e) {
				asserts.log(byLocator + " method not found in " + byClass);
				asserts.addVerificationFailure(e);
			} catch (IllegalAccessException e) {
				asserts.log("Unable to access method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (InvocationTargetException e) {
				asserts.log("Unable to invoke method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (TimeoutException e) {
				asserts.log("Timeout!! Unable to find element in specified time using "
						+ byLocator + " as " + locatorValue);
				asserts.addVerificationFailure(e);
			} catch (Exception e) {
				asserts.log("Error while finding element having " + byLocator
						+ " as " + locatorValue);
				asserts.addVerificationFailure(e);
			}

		} else
			asserts.log("The ByLocator value passed to searchElement() method is not supported or invalid");

		return uiElement;

	}

	/**
	 * Searches and returns a list of objects/WebElements based on ByLocator and
	 * locator values using the driver object
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElements
	 */
	public List<WebElement> searchElements(String byLocator, String locatorValue) {
		List<WebElement> uiElements = null;
		if (checkInSupportedByLocatorList(byLocator)) {
			try {
				byMethod = byClass.getDeclaredMethod(byLocator, paramString);
				uiElements = getCurrentDriver().findElements(
						(By) byMethod.invoke(byClass, locatorValue));
			} catch (NoSuchElementException e) {
				asserts.log("WebElement having " + byLocator + " as "
						+ locatorValue + "not found...");
			} catch (NoSuchMethodException e) {
				asserts.log(byLocator + " method not found in " + byClass);
				asserts.addVerificationFailure(e);
			} catch (IllegalAccessException e) {
				asserts.log("Unable to access method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (InvocationTargetException e) {
				asserts.log("Unable to invoke method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (Exception e) {
				asserts.log("Error while finding element having " + byLocator
						+ " as " + locatorValue);
				asserts.addVerificationFailure(e);
			}
		} else
			asserts.log("The ByLocator value passed to searchElements() method is not supported or invalid");

		return uiElements;
	}

	/**
	 * Try to search and waits for specified time until the object/element is
	 * visible. Returns the WebElement that is found.
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElementAfterWait
	 */
	public WebElement waitAndSearchElementUntilVisible(String byLocator,
			String locatorValue) {
		WebDriverWait wait = new WebDriverWait(getCurrentDriver(),
				waitTimeInSeconds);
		WebElement uiElementAfterWait = (WebElement) wait
				.until(ExpectedConditions.visibilityOf(searchElement(byLocator,
						locatorValue)));
		return uiElementAfterWait;
	}

	/**
	 * Try to search and waits for specified time until the object/element is
	 * clickable. Returns the WebElement that is found.
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElementAfterWait
	 */
	public WebElement waitAndSearchElementUntilClickable(String byLocator,
			String locatorValue) {
		WebDriverWait wait = new WebDriverWait(getCurrentDriver(),
				waitTimeInSeconds);
		WebElement uiElementAfterWait = (WebElement) wait
				.until(ExpectedConditions.elementToBeClickable(searchElement(
						byLocator, locatorValue)));
		return uiElementAfterWait;
	}

	/**
	 * Try to search in the parent element and waits for specified time until
	 * the object/element is visible. Returns the WebElement that is found.
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElementAfterWait
	 */
	public WebElement waitAndSearchElementInParentElementUntilVisible(
			WebElement parentElement, String byLocator, String locatorValue) {
		WebDriverWait wait = new WebDriverWait(getCurrentDriver(),
				waitTimeInSeconds);
		WebElement uiElementAfterWait = (WebElement) wait
				.until(ExpectedConditions
						.visibilityOf(searchElementInParentElement(
								parentElement, byLocator, locatorValue)));
		return uiElementAfterWait;
	}

	/**
	 * Wait until the element is not visible on UI
	 * 
	 * @param element
	 */
	public void waitUntilElementNotVisibleOnUI(WebElement element) {

		long waitTime = 2;
		WebDriverWait wait = new WebDriverWait(getCurrentDriver(), waitTime);
		try {
			while (wait.until(ExpectedConditions.elementToBeSelected(element))) {
			}
		} catch (TimeoutException e) {
		}

	}

	/**
	 * Try to search in the parent element and waits for specified time until
	 * the object/element is clickable. Returns the WebElement that is found.
	 * 
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElementAfterWait
	 */
	public WebElement waitAndSearchElementInParentElementUntilClickabe(
			WebElement parentElement, String byLocator, String locatorValue) {
		WebDriverWait wait = new WebDriverWait(getCurrentDriver(),
				waitTimeInSeconds);
		WebElement uiElementAfterWait = (WebElement) wait
				.until(ExpectedConditions
						.elementToBeClickable(searchElementInParentElement(
								parentElement, byLocator, locatorValue)));
		return uiElementAfterWait;

	}

	/**
	 * Searches the WebElement within the specified parent WebElement using the
	 * ByLocator and locator values
	 * 
	 * @param parentElement
	 * @param byLocator
	 * @param locatorValue
	 * @return uiElement
	 */
	public WebElement searchElementInParentElement(WebElement parentElement,
			String byLocator, String locatorValue) {
		WebElement uiElement = null;
		if (checkInSupportedByLocatorList(byLocator)) {
			try {
				byMethod = byClass.getDeclaredMethod(byLocator, paramString);
				uiElement = parentElement.findElement((By) byMethod.invoke(
						byClass, locatorValue));
			} catch (NoSuchElementException e) {
				asserts.log("WebElement having " + byLocator + " as "
						+ locatorValue + "not found...");
			} catch (NoSuchMethodException e) {
				asserts.log(byLocator + " method not found in " + byClass);
				asserts.addVerificationFailure(e);
			} catch (IllegalAccessException e) {
				asserts.log("Unable to access method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (InvocationTargetException e) {
				asserts.log("Unable to invoke method " + byLocator
						+ " from class " + byClass);
				asserts.addVerificationFailure(e);
			} catch (Exception e) {
				asserts.log("Error while finding element having " + byLocator
						+ " as " + locatorValue);
				asserts.addVerificationFailure(e);
			}
		} else
			asserts.log("The ByLocator value passed to searchElementInParentElement() method is not supported or invalid.");

		return uiElement;

	}

	/**
	 * Verifies the notification is received by the test user after submitting
	 * the contact us form.
	 * 
	 * @param user
	 * @param passwd
	 * @param subjectText
	 * @return isMailFound
	 */
	public boolean confirmNotificationMail(String user, String passwd,
			String subjectText) {

		Boolean isMailFound = false;
		Boolean isMailDateSameAsCurrentDate = false;
		Message[] messages = null;
		String contentText = "Thank you for contacting us: We'll get back to you in a short while.";
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date currentDate = new Date();
		String mailContent;
		String protocol = "imaps";
		String host = "imap.gmail.com";
		String inboxFolder = "INBOX";
		String subjectTextAfterTrimmedForSpaces = subjectText.trim();

		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getDefaultInstance(props, null);

		try {
			Store store = session.getStore(protocol);
			store.connect(host, user, passwd);

			Folder folder = store.getFolder(inboxFolder);
			folder.open(Folder.READ_ONLY);

			for (int i = 0; i < 5; i++) {
				messages = folder
						.search(new SubjectTerm(
								subjectTextAfterTrimmedForSpaces), folder
								.getMessages());
				if (messages.length != 0)
					break;
			}

			asserts.log("Searching mail with subject "
					+ subjectTextAfterTrimmedForSpaces);
			if (messages.length != 0) {
				asserts.log("Found mail(s) with subject -"
						+ subjectTextAfterTrimmedForSpaces);
				for (Message mail : messages) {
					isMailDateSameAsCurrentDate = (dateFormat
							.format(currentDate)).equalsIgnoreCase(dateFormat
							.format(mail.getReceivedDate()));
					asserts.log("Found mail(s) having received date as as -"
							+ dateFormat.format(currentDate));
					if (isMailDateSameAsCurrentDate) {
						mailContent = mail.getContent().toString();
						if (mailContent.contains(contentText)) {
							isMailFound = true;
							asserts.log("Found mail containing content -"
									+ contentText);
						}
					}
				}
			} else
				asserts.log("The notification mail with subject "
						+ subjectTextAfterTrimmedForSpaces + " not found");
			store.close();
		} catch (MessagingException e) {
			asserts.log("Unable to retrieve message from mail account");
			asserts.addVerificationFailure(e);

		} catch (IOException e) {
			asserts.log("Unable to retrieve message content");
			asserts.addVerificationFailure(e);
		}
		return isMailFound;

	}

	/**
	 * Reads the testcase data input file
	 * 
	 * @param testcaseFile
	 * @return
	 */
	public String[][] getTestcaseFileData(String testcaseFile) {

		int i = 0;
		int j = 0;
		ArrayList<String> rowData = new ArrayList<String>();
		rowData = readCSVFile(testcaseFile);
		String values[];
		String[][] inputData = new String[rowData.size()][3];

		for (String row : rowData) {

			if (!(row.startsWith("#") || row.isEmpty())) {
				values = row.split(",");
				j = 0;
				for (String value : values) {
					if (value.isEmpty() || value == null)
						inputData[i][j++] = "";
					else
						inputData[i][j++] = value;
				}

				i++;
			}

		}

		String[][] data = new String[i][3];
		for (int row = 0; row < i; row++) {
			for (int column = 0; column < j; column++) {
				data[row][column] = inputData[row][column];
			}
		}
		return data;

	}

	/**
	 * Reads the data from a csv file and returns of a String array list of rows
	 * 
	 * @param fileName
	 * @return Arraylist of rows
	 */
	private ArrayList<String> readCSVFile(String fileName) {
		ArrayList<String> csvRow = new ArrayList<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(fileName));
			scanner.useDelimiter(",");
			while (scanner.hasNext()) {
				csvRow.add(scanner.nextLine().toString());
			}
			asserts.log(fileName + " was read successfully.");
		} catch (FileNotFoundException e) {
			asserts.log(fileName + " not found..");
			return csvRow;
		} finally {
			scanner.close();
		}
		return csvRow;
	}

	/**
	 * Generates and returns any random number in the range of 1-1000
	 * 
	 * @return random number
	 */
	public int getRandomNum() {
		int maximum = 1000;
		int minimum = 1;
		Random rn = new Random();
		int range = maximum - minimum + 1;

		return rn.nextInt(range) + minimum;

	}

	/**
	 * Move mouse to the specified location
	 * 
	 * @param xCoordinates
	 * @param YCoordinates
	 */
	public void mouseMoveToLocation(int xCoordinates, int YCoordinates) {
		try {
			Robot robot = new Robot();
			robot.mouseMove(xCoordinates, YCoordinates);
		} catch (AWTException e) {
			asserts.log("Unable to move the mouse pointer to the specified coordinates.");
			asserts.addVerificationFailure(e);
		}
	}

	/**
	 * Pauses the execution of the script for the specified time
	 * 
	 * @param timeInSeconds
	 */
	public void pauseExecution(long timeInSeconds) {
		try {
			Thread.sleep(timeInSeconds * 1000);
		} catch (InterruptedException e) {
			asserts.log("Interrupted.");
			asserts.addVerificationFailure(e);

		}
	}

	/**
	 * Check whether the byLocatorName is valid supported bylocator and exists
	 * in the bylocator enum.
	 * 
	 * @param byLocatorName
	 * @return byLocatorFound
	 */
	private Boolean checkInSupportedByLocatorList(String byLocatorName) {
		Boolean byLocatorFound = false;

		for (Bylocator bEnum : Bylocator.values()) {
			if (bEnum.name().equalsIgnoreCase(byLocatorName)) {
				byLocatorFound = true;
			}
		}
		return byLocatorFound;
	}

	/**
	 * /** Constructs a new {@link RemoteWebDriver} instance which is configured
	 * to use the capabilities defined by the browser, version and os
	 * parameters, and which is configured to run against
	 * ondemand.saucelabs.com, using the username and access key populated by
	 * the {@link #authentication} instance.
	 *
	 * @param browser
	 *            Represents the browser to be used as part of the test run.
	 * @param version
	 *            Represents the version of the browser to be used as part of
	 *            the test run.
	 * @param os
	 *            Represents the operating system to be used as part of the test
	 *            run.
	 * @return WebDriver
	 */
	public WebDriver createDriver(ThreadLocal<WebDriver> webDriver,
			ThreadLocal<String> sessionId, String browser, String version,
			String os) {
		try {

			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
			if (version != null) {
				capabilities.setCapability(CapabilityType.VERSION, version);
			}
			capabilities.setCapability(CapabilityType.PLATFORM, os);
			capabilities.setCapability("name", getTestCaseName());
			webDriver.set(new RemoteWebDriver(new URL("http://"
					+ getSauceOnDemandUsername() + ":"
					+ getSauceOnDemandAccessKey()
					+ "@ondemand.saucelabs.com:80/wd/hub"), capabilities));
			sessionId.set(((RemoteWebDriver) webDriver.get()).getSessionId()
					.toString());
			setCurrentDriver(webDriver.get());

		} catch (MalformedURLException e) {
			asserts.log("Unable to connect to the SaunceOnDemand hub...");
			asserts.addVerificationFailure(e);
		} catch (Exception e) {
			asserts.log("Unable to initiate WebDriver...");
			asserts.addVerificationFailure(e);
		}

		return getCurrentDriver();
	}

	/**
	 * Returns the value of the cuurrent runnng test case
	 * 
	 * @return
	 */
	public String getTestCaseName() {
		return testCaseName;
	}

	/**
	 * Sets the value for the current running test case
	 * 
	 * @param testCaseName
	 */
	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	/**
	 * Returns the value of sauceOnDemandUsername used to connect to the
	 * SauceOnDemand
	 * 
	 * @return
	 */
	public String getSauceOnDemandUsername() {
		return sauceOnDemandUsername;
	}

	/**
	 * Sets the value of sauceOnDemandUsername used to connect to the
	 * SauceOnDemand
	 * 
	 * @param sauceOnDemandUsername
	 */
	public void setSauceOnDemandUsername(String sauceOnDemandUsername) {
		this.sauceOnDemandUsername = sauceOnDemandUsername;
	}

	/**
	 * Returns the value of sauceOnDemandAccessKey used to connect to the
	 * SauceOnDemand
	 * 
	 * @return sauceOnDemandAccessKey
	 */
	public String getSauceOnDemandAccessKey() {
		return sauceOnDemandAccessKey;
	}

	/**
	 * Sets the value for sauceOnDemanAccessKey used to connect to the
	 * SauceOnDemand
	 * 
	 * @param sauceOnDemanAccessKey
	 */
	public void setSauceOnDemandAccessKey(String sauceOnDemandAccessKey) {
		this.sauceOnDemandAccessKey = sauceOnDemandAccessKey;
	}

	/**
	 * Returns the current driver
	 * 
	 * @return currentDriver
	 */
	private WebDriver getCurrentDriver() {
		return currentDriver;
	}

	/**
	 * Sets the current driver
	 * 
	 * @param driver
	 */
	public void setCurrentDriver(WebDriver driver) {
		currentDriver = driver;

	}

	// Commenting these methods as they are not used while running on Sauce
	// labs. Using a RemoteWebDriver which will take the browser name at runtime
	// and simulate the corresponding browser driver function. This code can be
	// de-commented and test cases can be modified to run on local environment.

	/**
	 * Launch url in the specified browser
	 * 
	 * @param url
	 * @param browser
	 * @return browserDriver
	 */
	/*
	 * public WebDriver launchURLInBrowser(String url, String browser) {
	 * WebDriver browserDriver = null;
	 * 
	 * // Works only on java7, not using the case option
	 * 
	 * switch (browser.toUpperCase()) { case "INTERNETEXPLORER": browserDriver =
	 * launchIEBrowser(); break; case "FIREFOX": browserDriver =
	 * launchFirefoxBrowser(); break; case "SAFARI": browserDriver =
	 * launchSafariBrowser(); break; case "OPERA": browserDriver =
	 * launchOperaBrowser(); break; case "CHROME": browserDriver =
	 * launchChromeBrowser(); break; default:
	 * asserts.asserts.log("Invalid or not supported browser value"); break; }
	 * 
	 * if (browser.equalsIgnoreCase("InternetExplorer")) { browserDriver =
	 * launchIEBrowser(); } else if (browser.equalsIgnoreCase("Firefox")) {
	 * browserDriver = launchFirefoxBrowser(); } else if
	 * (browser.equalsIgnoreCase("Safari")) { browserDriver =
	 * launchSafariBrowser(); } else if (browser.equalsIgnoreCase("Opera")) {
	 * browserDriver = launchOperaBrowser(); } else if
	 * (browser.equalsIgnoreCase("Chrome")) { browserDriver =
	 * launchChromeBrowser(); } else
	 * asserts.log("Invalid or not supported browser value");
	 * 
	 * if ((browserDriver != null)) { try { browserDriver.get(url);
	 * setCurrentDriver(browserDriver); asserts.log("The specified url - " + url
	 * + " was launched succesfully in " + browser + " browser."); } catch
	 * (Exception e) { asserts.log("Unable to launch the url in " + browser +
	 * " browser"); asserts.addVerificationFailure(e); return browserDriver; } }
	 * else { asserts.log("Unable to launch " + browser +
	 * "using the respective driver."); }
	 * 
	 * return browserDriver;
	 * 
	 * }
	 *//**
	 * Launch Internet Explorer using InternetExplorerDriver
	 * 
	 * @return ieDriver
	 */
	/*
	 * private WebDriver launchIEBrowser() { WebDriver ieDriver = null; try {
	 * System.setProperty("webdriver.ie.driver", ieDriverExe); ieDriver = new
	 * InternetExplorerDriver(); ieDriver.manage().window().maximize(); } catch
	 * (IllegalStateException e) {
	 * asserts.log("The path to the IE driver executable is not set correctly."
	 * ); asserts.addVerificationFailure(e);
	 * 
	 * } catch (Exception e) {
	 * asserts.log("Unable to launch the Internet Explorer");
	 * asserts.addVerificationFailure(e);
	 * 
	 * } return ieDriver; }
	 *//**
	 * Launch Firefox browser using FirefoxDriver
	 * 
	 * @return firefoxDriver
	 */
	/*
	 * private WebDriver launchFirefoxBrowser() { WebDriver firefoxDriver =
	 * null; try { firefoxDriver = new FirefoxDriver();
	 * firefoxDriver.manage().window().maximize(); } catch (Exception e) {
	 * asserts.log("Unable to launch the Firefox browser");
	 * asserts.addVerificationFailure(e); } return firefoxDriver; }
	 *//**
	 * Launch Chrome browser using Chromedriver
	 * 
	 * @return chromeDriver
	 */
	/*
	 * private WebDriver launchChromeBrowser() { WebDriver chromeDriver = null;
	 * try { System.setProperty("webdriver.chrome.driver", chromeDriverExe);
	 * chromeDriver = new ChromeDriver();
	 * chromeDriver.manage().window().maximize(); } catch (Exception e) {
	 * asserts.log(
	 * "Unable to launch the Chrome browser.There could be multiple reasons for the failure. Chromedriver does not support the version above Opera 12, please install the appopriate versions (preferably using the 32 bit installer)"
	 * ); asserts.addVerificationFailure(e);
	 * 
	 * } return chromeDriver; }
	 *//**
	 * Launch Safari browser using SafariDriver
	 * 
	 * @return safariDriver
	 */
	/*
	 * private WebDriver launchSafariBrowser() { WebDriver safariDriver = null;
	 * try { safariDriver = new SafariDriver(); } catch (Exception e) {
	 * asserts.log("Unable to launch the Safari browser");
	 * asserts.addVerificationFailure(e);
	 * 
	 * } return safariDriver; }
	 *//**
	 * Launch Opera browser using OperaDriver
	 * 
	 * @return operaDriver
	 */
	/*
	 * private WebDriver launchOperaBrowser() { WebDriver operaDriver = null;
	 * try { operaDriver = new OperaDriver(); } catch (Exception e) {
	 * asserts.log("Unable to launch the Opera browser");
	 * asserts.addVerificationFailure(e);
	 * 
	 * } return operaDriver; }
	 */

}
