package com.zousu.mongopresser;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.zousu.mongopresser.dao.Column;
import com.zousu.mongopresser.dao.Table;
import com.zousu.mongopresser.exception.CollectionExistException;

/**
 *  Actual class where everything happens
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
public class Presser {

	private MySQLHandler mySQLHandler;
	private MongoHandler mongoHandler;
	private Logger logger = Logger.getLogger("com.zousu.mongopresser.Presser");
	
	public void setMySQLHandler(MySQLHandler mySQLHandler){
		this.mySQLHandler = mySQLHandler;
	}
	
	public void setMongoHandler(MongoHandler mongoHandler){
		this.mongoHandler = mongoHandler;
	}
	
	public void press(){
		//get a list of all the tables and columns
		logger.info("Starting MySQL to Mongo Conversion...");
		logger.info("Preparing Tables...");
		mySQLHandler.initialiseDatabase();
		List<Table> tables = mySQLHandler.getTableList();
		for(int i=0;i<tables.size();i++){
			Table table = tables.get(i);
			List<Column> columns = table.getColumns();
			List<DBObject> dboList = new ArrayList<DBObject>();
			SqlRowSet rs = mySQLHandler.selectAllFromTable(table.getTableName());
			logger.info("Creating objects for " + table.getTableName() + "...");
			while (rs.next()) {
				BasicDBObject dbo = new BasicDBObject();
				for (int j = 0; j < columns.size(); j++) {
					Column col = columns.get(j);
					String colName = col.getColumnName();
					switch(col.getType()){
					case Types.INTEGER:
					case Types.BIGINT:
						dbo.append(colName, rs.getInt(colName));
						break;
					case Types.DOUBLE:
						dbo.append(colName, rs.getDouble(colName));
						break;
					case Types.DATE:
						dbo.append(colName, rs.getDate(colName));
						break;
					default:
						dbo.append(colName, rs.getString(colName));
						break;
					}
					
				}
				dboList.add(dbo);
			}
			
			//now insert it
			logger.info("Inserting " + dboList.size() + " mongo rows into " + table.getTableName() + "...");
			table.setNumOfRows(dboList.size());
			try {
				mongoHandler.createCollection(table.getTableName(), true);
				mongoHandler.batchInsert(dboList, table.getTableName());
				assert(mongoHandler.getNumObjectsInCollection(table.getTableName())==dboList.size());
				logger.info(table.getTableName() + " DONE!");
			} catch (CollectionExistException e) {
				e.printStackTrace();
			}
		}
		logger.info(tables.size() + " collections added!");
		
		//now check go get it from mongo and check if the data there is correct
		logger.info("Checking mongo consistency...");
		for(int i=0;i<tables.size();i++){
			Table table = tables.get(i);
			assert(mongoHandler.getNumObjectsInCollection(table.getTableName())==table.getNumOfRows());
			logger.info(table.getTableName() + " consistent!");
		}
		logger.info("MySQL to Mongo Conversion Completed!!!!");
	}
	
	public void objectify(){
		
	}
	
	public static void main(String[] args){
		// Create the application context
	    ApplicationContext ctx = new ClassPathXmlApplicationContext("root-context.xml");
	    // Obtain a reference to our DAO
	    Presser presser = (Presser) ctx.getBean("presser");
	    presser.press();
	}
}
