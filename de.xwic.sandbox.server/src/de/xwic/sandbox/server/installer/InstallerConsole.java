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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.log4j.PropertyConfigurator;

import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.Version;

/**
 * This is a console version of the installer. A swing/swt version is possible too, but more effort.
 * 
 * @author Florian Lippisch
 */
public class InstallerConsole {

	private InstallationManager manager = null;
	private BufferedReader inp = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");

		InstallerConsole ic = new InstallerConsole();
		ic.start(args);

	}

	/**
	 * Start the installation process.
	 * 
	 * @param args
	 */
	private void start(String[] args) {

		try {
			System.out.println("XWic Installation Toolkit");
			System.out.println("=========================");
			System.out.println("");
			System.out.println("Autodetecting System Environment...");

			inp = new BufferedReader(new InputStreamReader(System.in));
			manager = new InstallationManager();
			manager.autoDetectSettings();
			manager.initialize();

			System.out.println("\n== Settings ==");
			System.out.println(manager.getSettings());

			System.out.println("\n== Configuration ==");
			Setup setup = manager.getSetup();
			if (setup != null) {
				System.out.println("Application:  " + setup.getAppTitle());
				System.out.println("ID:           " + setup.getId());
				System.out.println("Version:      " + setup.getVersion());
			} else {
				System.out.println("- No product configuration informations available -");
			}

			System.out.println("\n== Database ==");
			System.out.println("Initialized:  " + manager.isDatabaseInitialized());
			System.out.println("DB-Version:   " + manager.getDatabaseVersion());
			System.out.println("Stamp-ID:     " + manager.getStampProductId());
			System.out.println("Stamp-Version:" + manager.getStampVersion());

			System.out.println();
			if (setup == null) {
				System.out.println("The product configuration is not available. The configuration this installation");
				System.out.println("depends on must be available in a subdirectory named 'config'.");
				return; // EXIT
			}

			if (manager.getStampProductId() == null || manager.getStampProductId().length() == 0) {
				System.out.println("! The database is not 'stamped' with a product ID.");
			}

			boolean skipConfirmation = false;
			if (args.length > 0) {
				final Arguments arguments = new Arguments(args);
				manager.setOverrideCheck(arguments.isOverrideCheck());
				// handle arguments
				if (arguments.isInitDb()) {
					handleInitDatabase();
					return;
				}
				final String runModuleName = arguments.getRunModuleName();
				if (null != runModuleName) {
					manager.runModule(runModuleName);
					return;
				}
				skipConfirmation = arguments.isSkipConfirmation();
			}

			if (!manager.isDatabaseInitialized()) {

				System.out.println("The database has not been initialized yet.");
				handleInitDatabase();
				manager.integrityCheck();

			} else {

				// migration required?
				Version verSetup = new Version(setup.getVersion());
				Version verDB = new Version(manager.getDatabaseVersion());
				// clear build and adds, to ignore those
				verSetup.setPatch(0);
				verSetup.setBuild(0);
				verSetup.setAdds("");
				verDB.setPatch(0);
				verDB.setBuild(0);
				verDB.setAdds("");

				if (verDB.isHigher(verSetup)) {
					System.out.println("The database version is 'newer' then the given configuration.");
					System.out.println("Please update the configuration files.");
					return;
				}

				if (verDB.isLower(verSetup)) {
					System.out.println("The database is older then the configuration. A migration is required.");
					System.out.println("It is highly recommended that you make a BACKUP of the existing database,");
					System.out.println("before you start the migration.");
					System.out.println("Enter 'yes' to start the migration process...");
					if (skipConfirmation || "yes".equals(inp.readLine())) {
						manager.runMigration();
						System.out.println();
						System.out.println("The migration has been completed successfully.");
					}
				} else {

					System.out.println("No structural differences have been detected. Do you want to");
					System.out.println("check the database integrity? This check will search for missing");
					System.out.println("configuration settings, rights and makes a detailed table analyses.");
					System.out.println("Enter 'yes' to run the integrity check.");
					if (skipConfirmation || "yes".equals(inp.readLine())) {

						manager.integrityCheck();

					}

				}

			}

			manager.terminate();

		} catch (Throwable t) {
			System.out.println("Exception in InstallerConsole:");
			t.printStackTrace();
		}

		// The installer does not end automatically as some threads are started that do not stop properly (for some reasons?)
		try {
			Thread.sleep(1000);
		} catch (Throwable t) {
			// ignore interrupted exceptions
		}
		System.exit(0);
		
	}

	/**
	 * Initialize the database by exporting the schema.
	 * 
	 * @throws Exception
	 */
	private void handleInitDatabase() throws Exception {
		System.out.println("Enter 'init' to create the tables and import default values (if available).");
		System.out.println("All existing DATA in the database is beeing deleted!");
		if ("init".equalsIgnoreCase(inp.readLine())) {
			manager.schemaExport();

			// if the default directory exists, load the default settings.
			File file = new File("defaults");
			if (file.exists() && file.isDirectory()) {
				System.out.println("Importing default values...");
				manager.importDefaults(file);
			} else {
				System.out.println("No default values are available.");
			}

			manager.stampDatabase();
			manager.integrityCheck();

		} else {
			System.out.println("Aborted.");
		}

	}

}
