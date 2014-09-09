package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import loggerFunctions.CustomAsserts;

/**
 * This class reads ElementIdentifiers.properties for the elements identifier
 * values and assigns them to the global variables having similar names.
 * 
 * @author Nilesh Awasthey
 * 
 */
public class ElementIdentifiers {

	public static String contactFormId = null;
	public static String contactNameTextBoxId = null;
	public static String contactEmailTextBoxId = null;
	public static String contactIssueDetailsTextAreaId = null;
	public static String contactSubmitButtonClassName = null;
	public static String contactUsButtonId = null;
	public static String contactusMsgXpath = null;
	public static String requiredMsgContactEmailXpath = null;
	public static String requiredMsgContactIssueDetailsXpath = null;
	public static String validationMsgContactEmailXpath = null;
	public static String contactUsHeaderTextXpath = null;
	public static String contactUsFormCloseIconXpath = null;
	public static String contactNameLabelClassName = null;
	public static String contactEmailLabelClassName = null;
	public static String contactIssueDetailsLabelClassName = null;
	static CustomAsserts asserts = new CustomAsserts();
	static {
		loadElementIdentifierValues();
	}

	/**
	 * Reads the ElementIdentifiers.properties for the identifier values and
	 * stores in the specified variables
	 * 
	 */
	private static void loadElementIdentifierValues() {
		Properties prop = new Properties();
		InputStream elementIdentifiersFileInputStream = null;
		String propertyValue = null;
		ElementIdentifiers obj = new ElementIdentifiers();
		try {

			elementIdentifiersFileInputStream = new FileInputStream(
					"src/main/resources/Properties_files/ElementIdentifiers.properties");
			prop.load(elementIdentifiersFileInputStream);
			
			Field[] elementIdentifiers = getDeclaredFieldsOfCurrentClass();

			for (Field elementIdentifier : elementIdentifiers) {
			if(elementIdentifier!=null)
			{
				propertyValue = prop.getProperty(elementIdentifier.getName());
				if (propertyValue != null) {
					elementIdentifier.setAccessible(true);
					elementIdentifier.set(obj, new String(propertyValue));
				} 
			}
								}
		} catch (IOException e) {
			asserts.log("Unable to read ElementIdentifiers.properties file.");
			asserts.addVerificationFailure(e);
		} catch (IllegalArgumentException e) {
			asserts.log("Unable to set field value due to IllegalArgumentException");
			asserts.addVerificationFailure(e);
		} catch (IllegalAccessException e) {
			asserts.log("Unable to set field value due to IllegalAccessException");
			asserts.addVerificationFailure(e);
		} finally {
			if (elementIdentifiersFileInputStream != null) {
				try {
					elementIdentifiersFileInputStream.close();
				} catch (IOException e) {
					asserts.log("Unable to close properites file.");
					asserts.addVerificationFailure(e);
				}
			}

		}
	}

	/**
	 * Returns a list of class variables/fields for the current class
	 * 
	 * @return declaredFields
	 */
	private static Field[] getDeclaredFieldsOfCurrentClass() {
		final String className = ElementIdentifiers.class.getCanonicalName();

		try {
			Class<?> currentClass = Class.forName(className);
			Field declaredFields[] = currentClass.getDeclaredFields();
			return declaredFields;
		} catch (ClassNotFoundException e) {
			asserts.log(className + " class not found!!!");
			return null;
		}

	}
}
