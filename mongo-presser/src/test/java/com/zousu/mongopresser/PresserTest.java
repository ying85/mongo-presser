package com.zousu.mongopresser;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresserTest {
	
	private Presser presser;
	//private Logger logger = Logger.getLogger("com.zousu.mongopresser.MySQLHandlerTest");
	
	@Test
	public void testPress(){
		// Create the application context
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("root-context.xml");
	    // Obtain a reference to our DAO
	    presser = (Presser) ctx.getBean("presser");
	    presser.press();
	}
}
