package com.zousu.mongopresser.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *  Represents a Table in a database
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
public class Table implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger("com.zousu.mongopresser.dao.Table");
	
	private String tableName;
	private List<Column> columns;
	private int numOfRows;
	
	public void setTableName(String name) {
		this.tableName = name;
	}

	public String getTableName() {
		return tableName;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public int getNumOfRows() {
		return numOfRows;
	}

	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}
	
	public void printColumns(){
		String colNames = "";
		for(int i = 0; i < columns.size(); i++){
			Column col = columns.get(i);
			colNames += col.getColumnName() + " | " + col.getType() + ", ";
		}
		logger.info(getTableName() + ": " + colNames);
	}
}
