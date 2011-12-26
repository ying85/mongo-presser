package com.zousu.mongopresser;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MySQLHandlerTest {

	private MySQLHandler mySQLHandler;
	//private Logger logger = Logger.getLogger("com.zousu.mongopresser.MySQLHandlerTest");
	
	@Before
	@Test
	public void setupApplicationContext(){
		// Create the application context
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("root-context.xml");
	    // Obtain a reference to our DAO
	    mySQLHandler = (MySQLHandler) ctx.getBean("mySQLHandler");
	}
	
	@Test
	public void getClassPath(){
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		 
        URL[] urls = ((URLClassLoader)cl).getURLs();
 
        for(URL url: urls){
        	System.out.println(url.getFile());
        }
	}
	
	@Test
	public void testInitialiseDatabase(){
		mySQLHandler.initialiseDatabase();
	}
	
	@Test
	public void testSelectAllFromTable(){
		mySQLHandler.selectAllFromTable("wp_links");
	}
}
