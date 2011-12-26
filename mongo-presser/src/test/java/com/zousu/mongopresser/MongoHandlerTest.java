package com.zousu.mongopresser;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zousu.mongopresser.exception.CollectionExistException;
import com.zousu.mongopresser.exception.CollectionMissingException;

/**
 * 
 * @author Ying
 *
 */
public class MongoHandlerTest {

	//private Logger logger = Logger.getLogger("com.zousu.mongo.MongoHandlerTest");
	private MongoHandler mongoHandler;

	@Before
	@Test
	public void setupApplicationContext(){
		// Create the application context
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("root-context.xml");
	    mongoHandler = (MongoHandler)ctx.getBean("mongoHandler");
	}
	
	@Test
	public void testCreateCollection(){
		try {
			mongoHandler.createCollection("TestCollection", true);
		} catch (CollectionExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert(mongoHandler.getNumObjectsInCollection("TestCollection")==0);
	}
	
	@Test
	public void testGetObjectsFromCollection(){
		try {
			mongoHandler.getObjectsFromCollection("wp_comments");
		} catch (CollectionMissingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
