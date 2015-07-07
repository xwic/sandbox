/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *******************************************************************************/
package de.xwic.sandbox.server.installer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import de.xwic.appkit.core.config.Version;

public interface IDatabaseHandler {

	/**
	 * Close the connection.
	 * 
	 * @throws SQLException
	 *
	 */
	public abstract void close() throws SQLException;

	/**
	 * Find out the version that is currently beeing used.
	 * 
	 * @throws SQLException
	 */
	public abstract Version determineVersion() throws SQLException;

	/**
	 * Returns true if the given table exists.
	 * 
	 * @param tablename
	 * @return
	 * @throws SQLException
	 */
	public abstract boolean existsTable(String tablename) throws SQLException;

	/**
	 * Read a configuration property from the database.
	 * 
	 * @param key
	 * @return
	 * @throws SQLException
	 */
	public abstract String getConfigValue(String key) throws SQLException;

	/**
	 * Execute a sql script file.
	 * 
	 * @param file
	 */
	public abstract void executeSqlScript(File file) throws IOException, SQLException;

	/**
	 * Updates the specified column where the value is NULL. This is required after a database schema update, where a column was added by
	 * hibernate. These columns have NULL values, wich can not be read by hibernate when the type is a primitive.
	 * 
	 * @param tablename
	 * @param column
	 * @param value
	 * @throws SQLException
	 */
	public abstract boolean updateDefaultValues(String tablename, String column, String value);

	/**
	 * drops the given column from the table
	 * 
	 * @param fromTable
	 * @param columnName
	 * @return
	 */
	public abstract boolean dropColumn(String fromTable, String columnName);

	/**
	 * Renames a scope name and all its children if the old scope name exists and the new one does not.
	 * 
	 * @param oldScope
	 * @param newScope
	 * @return
	 */
	public abstract boolean renameScope(String oldScope, String newScope);

	/**
	 * @return the version
	 */
	public abstract Version getVersion();

	/**
	 * @return the dbInitialized
	 */
	public abstract boolean isDbInitialized();

	/**
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public abstract List<DbColumn> getTableColumns(String name) throws SQLException;

	/**
	 * @param name
	 * @param name2
	 * @param string
	 * @throws SQLException
	 */
	public abstract void alterColumn(String tableName, String columnName, String newType) throws SQLException;

}
