package selenium_mvn_eclipse_testng;

import common.CommonActions;

public class TestMail {

	public static void main(String[] args) {
		CommonActions obj=new CommonActions();
		
		if(obj.confirmNotificationMail("nilautotest@gmail.com","auto@021","testissue403 "))
			System.out.println("hellow");
		else
			System.out.println("no found");
			
		
		
	}

}
