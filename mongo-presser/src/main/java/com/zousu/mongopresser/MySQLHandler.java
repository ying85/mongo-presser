package com.zousu.mongopresser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.zousu.mongopresser.dao.Column;
import com.zousu.mongopresser.dao.Table;

/**
 *  Manages MySQL Operations
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
public class MySQLHandler {
	/**
	 * Data source reference which will be provided by Spring.
	 */
	private DataSource dataSource;

	private String selectAllQuery = "select * from";

	private String dbName;

	private List<Table> tableList;

	private Logger logger = Logger
			.getLogger("com.zousu.mongopresser.MySQLHandler");

	/**
	 * A JavaBean setter-style method to allow Spring to inject the data source.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 
	 * @param dbName
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void initialiseDatabase() {

		try {
			tableList = retrieveTables();
			for (int i = 0; i < tableList.size(); i++) {
				Table table = tableList.get(i);
				fillColumnsForTable(table);

				// print it out
				table.printColumns();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves a list of table names within the current database
	 * 
	 * @return
	 */
	public List<Table> retrieveTables() throws SQLException {
		
		List<Table> tableList = new ArrayList<Table>();
		ResultSet rs = dataSource.getConnection().getMetaData().getTables(dbName, null, null, null);
		while(rs.next()){
			Table table = new Table();
			table.setTableName(rs.getString("TABLE_NAME"));
			tableList.add(table);
		}
		return tableList;
	}

	/**
	 * Fill up a Table object with Column
	 * 
	 * @param table
	 */
	private void fillColumnsForTable(Table table) throws SQLException{
		
		ResultSet rs = dataSource.getConnection().getMetaData().getColumns(dbName, null, table.getTableName(), null);
		List<Column> columnList = new ArrayList<Column>();
		while (rs.next()) {
			Column col = new Column();
			col.setColumnName(rs.getString("COLUMN_NAME"));
			col.setType(rs.getInt("DATA_TYPE"));
			columnList.add(col);
		}
		table.setColumns(columnList);
	}

	public List<Table> getTableList() {
		return tableList;
	}

	/**
	 * Returns a ResultSet of entire table
	 * 
	 * @param tableName
	 * @return
	 */
	public SqlRowSet selectAllFromTable(String tableName) {
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(
				dataSource);

		logger.info("Calling: " + selectAllQuery + " " + tableName);

		return template.queryForRowSet(selectAllQuery + " " + tableName,
				new HashMap<String, String>());
	}
}