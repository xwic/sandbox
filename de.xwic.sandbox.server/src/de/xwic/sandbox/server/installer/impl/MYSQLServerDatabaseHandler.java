package de.xwic.sandbox.server.installer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.Version;
import de.xwic.sandbox.base.model.util.StreamUtil;
import de.xwic.sandbox.server.installer.DbColumn;
import de.xwic.sandbox.server.installer.IDatabaseHandler;
import de.xwic.sandbox.server.installer.InstallationManager;
import de.xwic.sandbox.server.installer.Settings;

public class MYSQLServerDatabaseHandler implements IDatabaseHandler {
	private final Log log = LogFactory.getLog(InstallationManager.class);

	private Connection connection = null;
	private boolean dbInitialized = false;
	private Version version = null;
	
	
	/**
	 * Opens a connection to the database specified in the settings.
	 * 
	 * @param settings
	 * @throws SQLException
	 */
	public MYSQLServerDatabaseHandler(Settings settings) throws SQLException {

		openConnection(settings);

	}

	/**
	 * @return the connection to the DB
	 * @throws SQLException
	 * @throws Exception
	 */
	private void openConnection(Settings settings) throws SQLException {

		try {
			Class.forName(settings.getJdbcDriverClass());
		} catch (ClassNotFoundException e) {
			log.warn("Error loading driver - trying to continue...", e);
		}

		// get connection details from the configuration file

		connection = DriverManager.getConnection(settings.getJdbcConnectionURL(), settings.getJdbcUserName(), settings.getJdbcPassword());

	}
	
	@Override
	public void close() throws SQLException {
		connection.close();
		
	}

	@Override
	public Version determineVersion() throws SQLException {
		// check for two common tables. if they dont exist,
		// the database is not even initialized.
		if (!(existsTable("CONF_PROP") && existsTable("SECURITY_USER"))) {
			dbInitialized = false;
			return null; // early exist.
		}
		dbInitialized = true;

		version = new Version();

		version.setMajor(0);

		return version;
	}

	@Override
	public boolean existsTable(String tablename) throws SQLException {
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("show tables like '" + tablename + "'");
		if (rs.next()) {
			return true;
		}

		return false;
	}

	@Override
	public String getConfigValue(String key) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement("SELECT CONF_PROP_VALUE FROM CONF_PROP WHERE CONF_PROP_KEY = ?");
		stmt.setString(1, key);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return rs.getString(1);
		}
		return null;
	}

	@Override
	public void executeSqlScript(File file) throws IOException, SQLException {
		BufferedReader reader = null;
		try {
			try {
				FileReader fileReader = new FileReader(file);
				reader = new BufferedReader(fileReader);
			} catch (FileNotFoundException e) {
				log.debug("File not found: " + file.getName() + ". Continuing with next script file.");
				return;
			}

			String line = "";

			log.info("Found SQL script file: " + file.getName());
			log.info("Begin script execution.");
			while (null != (line = reader.readLine())) {
				StringBuffer buffer = new StringBuffer();

				// get the SQL query from the file, until "GO" is reached
				while ((null != line) && (!"go".equalsIgnoreCase(line))) {
					buffer.append(line).append('\n');
					line = reader.readLine();
				}

				// execute the SQL query
				Statement stmt = connection.createStatement();
				try {
					stmt.execute(buffer.toString());
				} catch (SQLException e) {
					log.error("Error executing the script snippet: \n" + buffer, e);
					throw e;
				} finally {
					stmt.close();
				}
			}
		} finally {
			StreamUtil.close(log, reader);
		}

		log.info("The script file '" + file.getName() + "' completed succesfully.");
		
	}

	@Override
	public boolean updateDefaultValues(String tablename, String column, String value) {
		String sql = "UPDATE " + tablename + " SET \"" + column + "\" = " + value + " WHERE \"" + column + "\" IS NULL";
		try {
			Statement stmt = connection.createStatement();
			try {
				int count = stmt.executeUpdate(sql);
				if (count != 0) {
					log.info("Updated " + count + " rows in table '" + tablename + "'");
				}
			} finally {
				stmt.close();
			}
			return true;
		} catch (Exception e) {
			// the update process should not be aborted when this happens, so its just a warning
			log.warn("Error writing default value in table '" + tablename + "', column '" + column + "'", e);
			return false;
		}
	}

	@Override
	public boolean dropColumn(String fromTable, String columnName) {
		String sql = "IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.COLUMNS " + " WHERE TABLE_NAME = '" + fromTable
				+ "' AND COLUMN_NAME = '" + columnName + "'" + " ) ALTER TABLE " + fromTable + " DROP COLUMN " + columnName;

		try {
			Statement stmt = connection.createStatement();
			try {
				stmt.execute(sql);
			} finally {
				stmt.close();
			}
			return true;
		} catch (Exception e) {
			// the update process should not be aborted when this happens, so
			// its just a warning
			log.warn("Error dropping in table '" + fromTable + "', column '" + columnName + "'");
			return false;
		}
	}

	@Override
	public boolean renameScope(String oldScope, String newScope) {

		boolean exist = false;
		try {

			String sql = "SELECT NAME FROM CRM_SEC_SCOPE WHERE NAME = '" + newScope + "'";
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			try {
				if (stmt.execute(sql)) {
					ResultSet rs = stmt.getResultSet();
					if (rs.next()) {
						exist = true;
					}
				}
			} finally {
				stmt.close();
			}

			// determine if the newScope does not exist?
			if (exist) {
				return false;
			}
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			sql = "UPDATE CRM_SEC_SCOPE SET NAME = '" + newScope + "' WHERE NAME = '" + oldScope + "'";
			try {
				if (stmt.executeUpdate(sql) != 1) {
					log.warn("More than one entry with the name " + oldScope + " have been found.");
				}
			} finally {
				stmt.close();
			}

			// now update the SCOPE.propNAME scope types
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			sql = "SELECT NAME FROM CRM_SEC_SCOPE WHERE NAME LIKE '" + oldScope + ".%'";
			try {
				if (stmt.execute(sql)) {
					ResultSet rs = stmt.getResultSet();
					while (rs.next()) {
						String oldName = rs.getString("NAME");
						String propertyName = oldName.substring(oldScope.length() + 1);
						String newName = newScope + "." + propertyName;

						//rs.updateString("NAME", newName);
						//rs.updateRow();

						Statement stmtUpdate = connection.createStatement();
						try {
							String sqlUpdate = "UPDATE CRM_SEC_SCOPE SET NAME = '" + newName + "' WHERE NAME='" + oldName + "'";
							int num = stmtUpdate.executeUpdate(sqlUpdate);
							if (num == 0) {
								log.warn("Warning: RENAME FAILED FROM " + oldName + " TO " + newName);
							}
						} finally {
							stmtUpdate.close();
						}

					}
				}
			} finally {
				stmt.close();
			}

			// else, rename the scope + all dependent scopes
			// that include the property names
			// oldScopeName + ".%"

			return true;
		} catch (Exception e) {
			// the update process should not be aborted when this happens, so
			// its just a warning
			log.warn("Error renaming '" + oldScope + "', into '" + newScope + "'", e);
			return false;
		}
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public boolean isDbInitialized() {
		return dbInitialized;
	}

	@Override
	public List<DbColumn> getTableColumns(String name) throws SQLException {

		String query = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " + " WHERE TABLE_NAME = '" + name + "'";

		Statement stmt = connection.createStatement();
		List<DbColumn> list = new ArrayList<DbColumn>();
		try {
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {

				DbColumn col = new DbColumn();
				col.setName(rs.getString("COLUMN_NAME"));
				col.setDataType(rs.getString("DATA_TYPE"));
				col.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
				col.setLength(rs.getInt("CHARACTER_MAXIMUM_LENGTH"));

				list.add(col);

			}

		} finally {
			stmt.close();
		}

		return list;
	}

	@Override
	public void alterColumn(String tableName, String columnName, String newType) throws SQLException {
		Statement stmt = connection.createStatement();
		try {
			String sql = "ALTER TABLE \"" + tableName + "\" ALTER COLUMN \"" + columnName + "\" " + newType;
			stmt.execute(sql);
			log.debug("Modified column '" + columnName + "' in table '" + tableName + "' to type '" + newType + "'");
		} finally {
			stmt.close();
		}

		
	}

}
