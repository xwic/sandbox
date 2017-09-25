package de.xwic.sandbox.server.installer.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.Version;
import de.xwic.appkit.core.util.StreamUtil;
import de.xwic.sandbox.server.installer.DbColumn;
import de.xwic.sandbox.server.installer.IDatabaseHandler;
import de.xwic.sandbox.server.installer.InstallationManager;
import de.xwic.sandbox.server.installer.Settings;

public class HanaDatabaseHandler implements IDatabaseHandler {

	private final Log log = LogFactory.getLog(InstallationManager.class);

	private Connection connection = null;
	private boolean dbInitialized = false;
	private Version version = null;
	
	/**
	 * Opens a connection to the database specified in the settings.
	 * @param settings
	 * @throws SQLException 
	 */
	public HanaDatabaseHandler(Settings settings) throws SQLException {
		
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

		connection = DriverManager.getConnection(settings.getJdbcConnectionURL(),
				settings.getJdbcUserName(), 
				settings.getJdbcPassword());

	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#close()
	 */
	@Override
	public void close() throws SQLException {
		connection.close();
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#determineVersion()
	 */
	@Override
	public Version determineVersion() throws SQLException {
		
		// check for two common tables. if they dont exist,
		// the database is not even initialized.
		if (!(existsTable("CONF_PROP") && existsTable("SECURITY_USER"))) {
			dbInitialized = false;
			return null; // early exist.
		}
		dbInitialized = true;
		
		String versionString = getConfigValue(ApplicationData.CFG_DB_PRODUCT_VERSION);
		version = new Version(versionString);
		
		return version;	
		
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#existsTable(java.lang.String)
	 */
	@Override
	public boolean existsTable(String tablename) throws SQLException {
		ResultSet rs = null;
		try{
		   DatabaseMetaData metaData = connection.getMetaData();
		   rs = metaData.getTables(connection.getCatalog(), null, tablename, null);
		   return rs.next();
		} finally {
			if(rs != null){
				rs.close();
			}
		}
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#getConfigValue(java.lang.String)
	 */
	@Override
	public String getConfigValue(String key) throws SQLException {
		String rezult = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = connection.prepareStatement("SELECT CONF_PROP_VALUE FROM CONF_PROP WHERE CONF_PROP_KEY = ?");
			stmt.setString(1, key);
			rs = stmt.executeQuery();
			if (rs.next()) {
				rezult = rs.getString(1);
			}
		} finally {
			if(stmt != null){
				stmt.close();
			}
			if(rs != null){
				rs.close();
			}
		}
		
		return rezult;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#executeSqlScript(java.io.File)
	 */
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
			StreamUtil.close(log,reader);
		}

		log.info("The script file '" + file.getName() + "' completed succesfully.");
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#updateDefaultValues(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateDefaultValues(String tablename, String column, String value) {
		int rowsToUpdate = 100000;
		String sql = "UPDATE " + tablename + " SET \"" + column + "\" = " + value +
			" WHERE \"" + column + "\" IS NULL and rownum <= " + rowsToUpdate;
		try {
			int totalCount = 0;
			Statement stmt = connection.createStatement();
			try {
				int count;
				do { //updating millions of rows at once fails on dev machines because the undo tablespace is too small. Doing the update in batches.
					count = stmt.executeUpdate(sql);					 
					totalCount += count;
					if (count != 0) {
						log.info("Updated " + totalCount + " rows in table '" + tablename + "' on column '" + column + "'");
					}
				} while (count == rowsToUpdate);
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
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#dropColumn(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean dropColumn(String fromTable, String columnName) {
		
		String sql = "IF EXISTS ( SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
		 " WHERE TABLE_NAME = '" + fromTable + "' AND COLUMN_NAME = '" + columnName + "'" +
		 " ) ALTER TABLE " + fromTable + " DROP COLUMN " + columnName;
		
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
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#renameScope(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean renameScope(String oldScope, String newScope) {
		
		boolean exist = false;
		try {
			
			Statement stmt = null;
			ResultSet rs = null;
			String sql = "SELECT NAME FROM CRM_SEC_SCOPE WHERE NAME = '" + newScope + "'";
			
			try {
				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				
				if (stmt.execute(sql)) {
					rs = stmt.getResultSet();
					if (rs.next()) {
						exist = true;
					}
				}
			} finally {
				if(stmt != null){
					stmt.close();
				}
				if(rs != null){
					rs.close();
				}
			}
			
			// determine if the newScope does not exist?
			if (exist) {
				return false;
			}
			
			sql = "UPDATE CRM_SEC_SCOPE SET NAME = '" + newScope + "' WHERE NAME = '" + oldScope + "'";
			try {
				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				
				if (stmt.executeUpdate(sql) != 1) {
					log.warn("More than one entry with the name " + oldScope + " have been found.");
				}
			} finally {
				stmt.close();
			}

			// now update the SCOPE.propNAME scope types
			sql = "SELECT NAME FROM CRM_SEC_SCOPE WHERE NAME LIKE '" + oldScope + ".%'";
			try {
				stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				if (stmt.execute(sql)) {
					rs = stmt.getResultSet();
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
				if(stmt != null){
					stmt.close();
				}
				if(rs != null){
					rs.close();
				}
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
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#getVersion()
	 */
	@Override
	public Version getVersion() {
		return version;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#isDbInitialized()
	 */
	@Override
	public boolean isDbInitialized() {
		return dbInitialized;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#getTableColumns(java.lang.String)
	 */
	@Override
	public List<DbColumn> getTableColumns(String name) throws SQLException {

		List<DbColumn> list = null;
		ResultSet rs = null;

		try {

			list = new ArrayList<DbColumn>();

			rs = connection.getMetaData().getColumns(connection.getCatalog(), null, name, null);

			while (rs.next()) {

				DbColumn col = new DbColumn();
				col.setName(rs.getString("COLUMN_NAME"));
				col.setDataType(rs.getString("DATA_TYPE"));
				col.setNullable("YES".equals(rs.getString("IS_NULLABLE")));
				col.setLength(rs.getInt("COLUMN_SIZE"));

				list.add(col);

			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		
		return list;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.server.installer.IDatabaseHandler#alterColumn(java.lang.String, java.lang.String, java.lang.String)
	 */
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
