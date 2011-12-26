package com.zousu.mongopresser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.zousu.mongopresser.exception.CollectionExistException;
import com.zousu.mongopresser.exception.CollectionMissingException;

/**
 *  Manages Mongo Operations
 *  
 *	Copyright (C) 2011  Ying Zou
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
public class MongoHandler {

	private Logger logger = Logger.getLogger("com.zousu.mongo.MongoHandler");
	private MongoOperations mongoOperation;
	
	public void setMongoOperation(MongoOperations mongoOperation){
		this.mongoOperation = mongoOperation;
	}

	public void createCollection(String collectionName) throws CollectionExistException{
		createCollection(collectionName, false);
	}
	
	public void createCollection(String collectionName, boolean dropExisting) throws CollectionExistException{
		if (mongoOperation.collectionExists(collectionName)){
			if(dropExisting)
				mongoOperation.dropCollection(collectionName);
			else
				throw new CollectionExistException();
		}
		else{
			logger.info("Create new collection: " + collectionName);
			mongoOperation.createCollection(collectionName);
		}
	}
	
	public void batchInsert(List<DBObject> dboList, String collectionName){
		mongoOperation.insert(dboList, collectionName);
	}
	
	public int getNumObjectsInCollection(String collectionName){
		return (int)mongoOperation.getCollection(collectionName).count();
	}
	
	public List<DBObject> getObjectsFromCollection(String collectionName) throws CollectionMissingException{
		if(!mongoOperation.collectionExists(collectionName))
			throw new CollectionMissingException();
		
		DBCollection dbCol = mongoOperation.getCollection(collectionName);
		List<DBObject> objList = new ArrayList<DBObject>();
		Iterator<DBObject> it = dbCol.find().iterator();
		while(it.hasNext()){
			DBObject obj = it.next();
			objList.add(obj);
		}
		return objList;
	}
}
