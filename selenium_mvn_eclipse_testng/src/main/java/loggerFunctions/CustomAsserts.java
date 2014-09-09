package loggerFunctions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * Custom asserts class
 *
 * @author xyz
 * 
 * @modified Nilesh Awasthey - Added some verify methods which internally call
 *           the standard assert methods in a try/catch block
 */

public class CustomAsserts {

	private static Map<ITestResult, List<Throwable>> verificationFailuresMap = new HashMap<ITestResult, List<Throwable>>();

	public void assertTrue(boolean condition) {
		Assert.assertTrue(condition);
	}

	public void assertTrue(boolean condition, String message) {
		Assert.assertTrue(condition, message);
	}

	public void assertFalse(boolean condition) {
		Assert.assertFalse(condition);
	}

	public void assertFalse(boolean condition, String message) {
		Assert.assertFalse(condition, message);
	}

	public void assertEquals(boolean actual, boolean expected) {
		Assert.assertEquals(actual, expected);
	}

	public void assertEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);
	}

	public void assertEquals(Object[] actual, Object[] expected) {
		Assert.assertEquals(actual, expected);
	}

	public void assertEquals(Object actual, Object expected, String message) {
		Assert.assertEquals(actual, expected, message);
	}

	public void assertNotNull(Object object) {
		Assert.assertNotNull(object);
	}

	public void assertNotNull(Object object, String message) {
		Assert.assertNotNull(object, message);
	}

	public void assertNull(Object object) {
		Assert.assertNull(object);
	}

	public void assertNull(Object object, String message) {
		Assert.assertNull(object, message);
	}

	public void verifyTrue(boolean condition) {
		try {
			assertTrue(condition);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyTrue(boolean condition, String msgOnSuccess,
			String msgOnFailure) {
		try {
			assertTrue(condition, msgOnFailure);
			Reporter.log(msgOnSuccess + "<br>");

		} catch (Throwable e) {
			Reporter.log(msgOnFailure + "<br>");
			addVerificationFailure(e);
		}
	}

	public void verifyFalse(boolean condition) {
		try {
			assertFalse(condition);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyFalse(boolean condition, String msgOnSuccess,
			String msgOnFailure) {
		try {
			assertFalse(condition, msgOnFailure);
			Reporter.log(msgOnSuccess + "<br>");
		} catch (Throwable e) {
			Reporter.log(msgOnFailure + "<br>");
			addVerificationFailure(e);
		}
	}

	public void verifyEquals(boolean actual, boolean expected) {
		try {
			assertEquals(actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyEquals(Object actual, Object expected) {
		try {
			assertEquals(actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyEquals(Object[] actual, Object[] expected) {
		try {
			assertEquals(actual, expected);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyNotNull(Object object) {
		try {
			Assert.assertNotNull(object);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyNotNull(Object object, String msgOnSuccess,
			String msgOnFailure) {
		try {
			Assert.assertNotNull(object, msgOnFailure);
			Reporter.log(msgOnSuccess + "<br>");

		} catch (Throwable e) {
			Reporter.log(msgOnFailure + "<br>");
			addVerificationFailure(e);
		}
	}

	public void verifyNull(Object object) {
		try {
			Assert.assertNull(object);
		} catch (Throwable e) {
			addVerificationFailure(e);
		}
	}

	public void verifyNull(Object object, String msgOnSuccess,
			String msgOnFailure) {
		try {
			Assert.assertNull(object, msgOnFailure);
			Reporter.log(msgOnSuccess + "<br>");

		} catch (Throwable e) {
			Reporter.log(msgOnFailure + "<br>");
			addVerificationFailure(e);
		}

	}

	public void fail(String message) {
		Assert.fail(message);
	}

	public void log(String message) {
		Reporter.log(message + "<br>");
	}

	public static List<Throwable> getVerificationFailures() {
		List<Throwable> failures = verificationFailuresMap.get(Reporter
				.getCurrentTestResult());
		return failures == null ? new ArrayList<Throwable>() : failures;
	}

	public void addVerificationFailure(Throwable e) {
		List<Throwable> verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(),
				verificationFailures);
		verificationFailures.add(e);
	}

}
