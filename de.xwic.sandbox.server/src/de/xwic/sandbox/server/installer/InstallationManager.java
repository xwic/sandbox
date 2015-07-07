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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.CommonConfiguration;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.Version;
import de.xwic.appkit.core.config.XmlConfigLoader;
import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DefaultUseCaseService;
import de.xwic.appkit.core.dao.SystemSecurityManager;
import de.xwic.appkit.core.dao.impl.hbn.HibernateDAOProvider;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.file.impl.hbn.HbnFileOracleFixDAO;
import de.xwic.appkit.core.model.util.ServerConfig;

/**
 * Used to detect the currently installed version and take the required steps to update/fix the installation.
 * 
 * @author Florian Lippisch
 */
public class InstallationManager {

	private static final Log log = LogFactory.getLog(InstallationManager.class);

	private static final String HBN_CONNECTION_USERNAME = "hibernate.connection.username";
	private static final String HBN_CONNECTION_PASSWORD = "hibernate.connection.password";
	private static final String HBN_CONNECTION_URL = "hibernate.connection.url";
	private static final String HBN_CONNECTION_DRIVER_CLASS = "hibernate.connection.driver_class";
	// these are all the possible locations where config files can be found
	private static final String[] LOCATIONS = { "", String.format("..%s", File.separator),
			String.format("..%s..%sconfig%s", File.separator, File.separator, File.separator), String.format("config%s", File.separator),
			String.format("WEB-INF%s", File.separator), String.format("web_root%s", File.separator),
			String.format("web_root%sWEB-INF%s", File.separator, File.separator),
			String.format("web_root%sconfig%s", File.separator, File.separator),
			String.format("..%sconfig%s", File.separator, File.separator) };

	private Settings settings = new Settings();
	private Setup setup = null;
	private Properties serverProperties = new Properties();
	private Properties installerProperties = new Properties();
	private IDatabaseHandler dbHandler = null;

	private boolean daoSystemInitialized = false;
	private boolean eiManagerInitialized = false;
	private String stampProductId = null;
	private String stampVersion = null;

	private Map<String, IUpgradeModule> modules = new HashMap<String, IUpgradeModule>();
	private List<String> moduleOrder = new ArrayList<String>();
	private boolean overrideCheck;

	/**
	 * Try to automatically detect the connection settings for the database and the configuration files.
	 */
	public void autoDetectSettings() {

		// look for installer.properties
		loadPropertiesFile(installerProperties, "installer.properties");

		// search for the hibernate.cfg.xml file in the classpath
		boolean found = false;
		try {
			log.info("Searching hibernate.cfg.xml in classpath");
			InputStream in = getClass().getResourceAsStream("/hibernate.cfg.xml");
			if (in != null) {
				log.info("hibernate.cfg.xml found - reading the file now.");
				SAXReader reader = new SAXReader();
				Document doc = reader.read(in);
				List<?> list = doc.selectNodes("//hibernate-configuration/session-factory/property");
				for (Iterator<?> it = list.iterator(); it.hasNext();) {
					Node node = (Node) it.next();
					if (node instanceof Element) {
						Element prop = (Element) node;
						String propName = prop.attributeValue("name");
						String propValue = prop.getText();

						if (HBN_CONNECTION_USERNAME.equals(propName)) {
							settings.setJdbcUserName(propValue);
						} else if (HBN_CONNECTION_PASSWORD.equals(propName)) {
							settings.setJdbcPassword(propValue);
						} else if (HBN_CONNECTION_URL.equals(propName)) {
							settings.setJdbcConnectionURL(propValue);
							found = true;
						} else if (HBN_CONNECTION_DRIVER_CLASS.equals(propName)) {
							settings.setJdbcDriverClass(propValue);
						}
					}
				}
			} else {
				log.info("hibernate.cfg.xml was not found in the classpath.");
			}
		} catch (Exception e) {
			log.warn("Error while trying to detect the hibernate.cfg.xml file in the classpath.", e);
		}

		if (!found) {
			log.info("The connection settings have not been found - searching for hibernate.properties file.");

			File file = findFile("hibernate.properties");

			if (file != null) {
				log.info(String.format("Found %s", file.getPath()));
				Properties prop = new Properties();
				FileInputStream inStream = null;
				try {
					inStream = new FileInputStream(file);
					prop.load(inStream);
					settings.setJdbcUserName(prop.getProperty(HBN_CONNECTION_USERNAME, settings.getJdbcUserName()));
					settings.setJdbcPassword(prop.getProperty(HBN_CONNECTION_PASSWORD, settings.getJdbcUserName()));
					settings.setJdbcConnectionURL(prop.getProperty(HBN_CONNECTION_URL, settings.getJdbcUserName()));
					settings.setJdbcDriverClass(prop.getProperty(HBN_CONNECTION_DRIVER_CLASS, settings.getJdbcUserName()));
				} catch (IOException e) {
					log.error("Error reading hibernate.properties: ", e);
				} finally {
					closeStream(inStream);
				}
			}
		}

		log.info("Searching for product configuration...");

		File file = findFile("product.setup.xml");
		if (file != null) {
			log.info(String.format("Found %s", file.getPath()));
			settings.setConfigDir(file.getParent());
		}

		// look for installer.properties
		loadPropertiesFile(serverProperties, "server.properties");

	}

	/**
	 * @param inStream
	 */
	protected void closeStream(FileInputStream inStream) {
		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
				log.error("Failed to close input stream", e);
			}
		}
	}

	/**
	 * @param prop
	 * @param fileName
	 */
	private void loadPropertiesFile(Properties prop, String fileName) {
		File file = findFile(fileName);
		if (file != null) {
			log.info("Reading " + fileName);
			FileInputStream inStream = null;
			try {
				inStream = new FileInputStream(file);
				prop.load(inStream);
			} catch (IOException e) {
				String msg = "Error reading " + fileName;
				log.error(msg, e);
				throw new RuntimeException(msg + e, e);
			} finally {
				closeStream(inStream);
			}
		} else {
			log.warn(fileName + " not found.");
		}
	}

	/**
	 * @param locations
	 * @param string
	 * @return
	 */
	public static File findFile(String filename) {
		File file = null;
		for (int i = 0; i < LOCATIONS.length; i++) {
			file = new File(LOCATIONS[i] + filename);
			if (file.exists()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * Loads the configuration based on the settings and determines database version.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	public void initialize() throws IOException, ParseException, SQLException {

		// load modules
		String moduleKeys = installerProperties.getProperty("modules", "");
		StringTokenizer stk = new StringTokenizer(moduleKeys, ";, ");
		while (stk.hasMoreTokens()) {
			String moduleKey = stk.nextToken();
			String moduleClassName = installerProperties.getProperty(moduleKey + ".class", null);
			if (moduleClassName == null) {
				log.warn("A module '" + moduleKey + "' has been specified without a classname. The module is ignored.");
			} else {
				try {
					IUpgradeModule module = (IUpgradeModule) Class.forName(moduleClassName).newInstance();
					modules.put(moduleKey, module);
					moduleOrder.add(moduleKey);
					log.info("Loaded module '" + moduleKey + "' with the name '" + module.getName() + "'");
				} catch (Exception e) {
					log.warn("Error loading specified module '" + moduleKey + "'. The module is not loaded.: ", e);
				}
			}
		}

		// load configuration
		if (settings.getConfigDir() == null) {
			throw new IllegalStateException("The settings are not completed - configDir not specified.");
		}
		setup = XmlConfigLoader.loadSetup(new File(settings.getConfigDir()).toURI().toURL());

		// merge server specific settings
		for (Object prop : serverProperties.keySet()) {
			String key = (String) prop;
			setup.setProperty(key, serverProperties.getProperty(key));
		}

		dbHandler = DatabaseHandlerFactory.getDatabaseHandler(settings);
		dbHandler.determineVersion();
		if (dbHandler.isDbInitialized()) { // don't check on "empty" database.
			stampProductId = dbHandler.getConfigValue(ApplicationData.CFG_DB_PRODUCT_ID);
			stampVersion = dbHandler.getConfigValue(ApplicationData.CFG_DB_PRODUCT_VERSION);
		}
	}

	/**
	 * Terminate the manager and close all open connections.
	 *
	 */
	public void terminate() {
		if (dbHandler != null) {
			try {
				dbHandler.close();
			} catch (SQLException e) {
				log.error("Error during terminate", e);
			}
		}
	}

	/**
	 * @return the setup
	 */
	public Setup getSetup() {
		return setup;
	}

	/**
	 * Returns the version of the database.
	 * 
	 * @return
	 */
	public Version getDatabaseVersion() {
		return dbHandler != null ? dbHandler.getVersion() : null;
	}

	/**
	 * Returns true if the database has been initialized.
	 * 
	 * @return
	 */
	public boolean isDatabaseInitialized() {
		return dbHandler != null ? dbHandler.isDbInitialized() : false;
	}

	/**
	 * Export the database schema based on the hibernate configuration to the database. This drops the database.
	 */
	public void schemaExport() throws IOException {

		log.info("Initialize database requested.");

		Configuration cfg = createHibernateConfiguration();

		SchemaExport export = new SchemaExport(cfg);
		export.create(true, true);

		log.info("The schema export has been finished successfully.");

	}

	/**
	 * Create a hibernate configuration.
	 * 
	 * @return
	 * @throws IOException
	 */
	private Configuration createHibernateConfiguration() {

		log.info("Creating hibernate configuration.");
		Configuration cfg = new Configuration();
		cfg.configure();
		// read and apply hibernate.properties
		File file = findFile("hibernate.properties");

		if (file != null) {
			log.info("Found " + file.getPath());
			Properties prop = new Properties();
			FileInputStream inStream = null;
			try {
				inStream = new FileInputStream(file);
				prop.load(inStream);
				cfg.setProperties(prop);
			} catch (IOException e) {
				log.warn("Error reading properties - trying without", e);
			} finally {
				closeStream(inStream);
			}
		}
		return cfg;
	}

	/**
	 * Import default values from an XML file into the database.
	 * 
	 * @param file
	 * @throws ConfigurationException
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void importDefaults(File dumpDir) throws IOException, DocumentException, ConfigurationException {

		if (!daoSystemInitialized) {
			initDaoSystem();
		}

		XmlImport imp = new XmlImport();
		imp.importFile(new File(dumpDir, "isis_picklists.xml"));
		imp.importFile(new File(dumpDir, "isis_security.xml"));
		imp.importFile(new File(dumpDir, "isis_config.xml"));
		imp.importFile(new File(dumpDir, "isis_mitarbeiter.xml"));
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void initDaoSystem() {

		log.info("Initializing DAO System...");

		// setup DAO System.
		HibernateUtil.initialize(createHibernateConfiguration());

		HibernateDAOProvider hbnDP = new HibernateDAOProvider();
		DAOFactory factory = CommonConfiguration.createCommonDaoFactory(hbnDP);

		DAOSystem.setDAOFactory(factory);
		DAOSystem.setSecurityManager(new SystemSecurityManager());
		DAOSystem.setUseCaseService(new DefaultUseCaseService(hbnDP));
		DAOSystem.setFileHandler(new HbnFileOracleFixDAO());
		

		ConfigurationManager.setSetup(setup);
		daoSystemInitialized = true;

		log.info("DAO System initialized!");
	}

	/**
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void initEIManager() throws IOException, DocumentException {
		log.info("Initializing EI Manager..");

		File fileConfig = InstallationManager.findFile("EIconfig.xml");

		if (!fileConfig.exists()) {
			throw new RuntimeException("Can not find EIconfig.xml");
		}

		eiManagerInitialized = true;

		log.info("EI Manager initialized!");
	}

	/**
	 * Write the current version/id into the database settings.
	 * 
	 * @throws IOException
	 */
	public void stampDatabase() throws IOException {
		if (!daoSystemInitialized) {
			initDaoSystem();
		}

		ServerConfig.set(ApplicationData.CFG_DB_PRODUCT_ID, setup.getId());
		ServerConfig.set(ApplicationData.CFG_DB_PRODUCT_VERSION, setup.getVersion());

		stampProductId = setup.getId();
		stampVersion = setup.getVersion();
	}

	/**
	 * @return the stampProductId
	 */
	public String getStampProductId() {
		return stampProductId;
	}

	/**
	 * @return the stampVersion
	 */
	public String getStampVersion() {
		return stampVersion;
	}

	/**
	 * Migrate the database to the current version.
	 * 
	 * @param file
	 * @throws SQLException
	 * @throws IOException
	 */
	public void runMigration() throws IOException, SQLException {

		Version verDB = getCurrentVersion();
		Version verSetup = new Version(setup.getVersion());
		String scriptPath = "script" + verDB.getMajor() + "." + verDB.getMinor() + "-" + verSetup.getMajor() + "." + verSetup.getMinor();

		File path = new File(scriptPath);
		if (!path.exists() && path.isDirectory()) {
			// check if the required scripts are available.
			if (verDB.getMajor() != verSetup.getMajor()) {
				// major upgrades require scripts!

				String msg = "This upgrade requires database scripts that can not be found in " + scriptPath + ". "
						+ "A migration between major versions is not possible without a script.";
				log.error(msg);
				throw new IllegalStateException(msg);
			}
		} else {
			// run the scripts
			String[] sqlFiles = { "preparing.sql", "createnewtables.sql", "dropdeletedtables.sql", "altercolumnstables.sql", "aksplit.sql",
					"updateidentity.sql", "recreatefks.sql", "finalupdates.sql" };

			for (int i = 0; i < sqlFiles.length; i++) {
				dbHandler.executeSqlScript(new File(path, sqlFiles[i]));
			}
		}

		// run schema export
		log.info("Updating database schema.");

		Configuration cfg = createHibernateConfiguration();
		SchemaUpdate update = new SchemaUpdate(cfg);
		update.execute(false, true);

		if (!daoSystemInitialized) {
			initDaoSystem();
		}
		// run the registered modules
		for (Iterator<String> it = moduleOrder.iterator(); it.hasNext();) {
			String modKey = it.next();
			IUpgradeModule module = modules.get(modKey);
			if (!module.isDirectCall()) {
				if (module.appliesToProduct(setup.getId())) {
					if (module.appliesToVersion(verDB, verSetup)) {
						runThisModule(modKey, module);
					} else {
						log.info(String.format("Module '%s' skipped, because it does not apply to this version.", modKey));
					}
				} else {
					log.info(String.format("Module '%s' skipped, because it does not apply to this product type.", modKey));
				}
			}
		}
		stampDatabase();

		log.info("Migration completed.");
	}

	/**
	 * @param modKey
	 * @param module
	 */
	protected void runThisModule(String modKey, IUpgradeModule module) {
		try {
			initalizeDaoAndEI();
			log.info("Executing module '" + modKey + "'..");
			module.run(this);
		} catch (NonCriticalInstallerException e) {
			// don't interrupt the entire migration!
			log.error(String.format("Non Critical Error executing module '%s': %s", modKey, e), e);
		} catch (Exception e) {
			log.error(String.format("Error executing module '%s': %s", modKey, e), e);
			throw new RuntimeException("Migration Aborted.", e);
		}
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return installerProperties;
	}

	/**
	 * Run the modules and make a schema update.
	 * 
	 * @throws IOException
	 */
	public void integrityCheck() throws IOException {

		Version verDB = getCurrentVersion();
		Version verSetup = new Version(setup.getVersion());

		log.info("Updateing database schema.");

		Configuration cfg = createHibernateConfiguration();
		SchemaUpdate update = new SchemaUpdate(cfg);
		update.execute(false, true);

		if (!daoSystemInitialized) {
			initDaoSystem();
		}
		// run the registered modules
		for (Iterator<String> it = moduleOrder.iterator(); it.hasNext();) {
			String modKey = it.next();
			IUpgradeModule module = modules.get(modKey);
			if (!module.isDirectCall()) {
				if (module.appliesToProduct(setup.getId())) {
					if (overrideCheck || module.appliesToVersion(verDB, verSetup)) {
						runThisModule(modKey, module);
					} else {
						log.info(String.format("Module '%s' skipped because it does not apply to this version.", modKey));
					}
				} else {
					log.info(String.format("Module '%s' skipped because it does not apply to this product type.", modKey));
				}
			}
		}

		stampDatabase();
		log.info("Integrity Check completed.");

	}

	/**
	 * Returns the current version.
	 * 
	 * @return
	 */
	private Version getCurrentVersion() {
		return getStampVersion() != null ? new Version(getStampVersion()) : dbHandler.getVersion();
	}

	/**
	 * Returns the database handler to execute SQL commands.
	 * 
	 * @return
	 */
	public IDatabaseHandler getDatabaseHandler() {
		return dbHandler;
	}

	/**
	 * @param moduleName
	 */
	public void runModule(String moduleKey) {
		Version verSetup = new Version(setup.getVersion());
		IUpgradeModule module = modules.get(moduleKey);
		if (module != null) {
			if (overrideCheck || module.appliesToProduct(setup.getId())) {
				if (overrideCheck || module.appliesToVersion(getCurrentVersion(), verSetup)) {
					try {
						initalizeDaoAndEI();
						log.info("Executing module '" + moduleKey + "'");

						Date start = new Date();

						module.run(this);

						Date end = new Date();

						log.info("Module runtime: " + (end.getTime() - start.getTime()) / 1000 + " seconds");

					} catch (Exception e) {
						log.error("Error executing module", e);
					}
				} else {
					log.warn("Module not invoked: The module does not apply to this version.");
				}
			} else {
				log.warn("Module not invoked: The module does not apply to this product type.");
			}
		} else {
			log.warn("Module '" + moduleKey + "' unknown.");
		}

	}

	/**
	 * @throws DocumentException
	 * @throws IOException
	 */
	private void initalizeDaoAndEI() throws IOException, DocumentException {
		if (!daoSystemInitialized) {
			initDaoSystem();
		}
	}

	/**
	 * @return the overrideCheck
	 */
	public boolean isOverrideCheck() {
		return overrideCheck;
	}

	/**
	 * @param overrideCheck
	 *            the overrideCheck to set
	 */
	public void setOverrideCheck(final boolean overrideCheck) {
		this.overrideCheck = overrideCheck;
	}

}
