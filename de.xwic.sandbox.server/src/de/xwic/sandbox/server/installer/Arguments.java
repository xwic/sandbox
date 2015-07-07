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

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.util.CollectionUtil;

/**
 * @author Alexandru Bledea
 * @since Jul 24, 2014
 */
final class Arguments {

	private static final String PARAMETER_INITDB = "initdb";
	private static final String PARAMETER_RUN = "run";
	private static final String PARAMETER_NO_CHECK = "-nocheck";
	private static final String PARAMETER_SKIP_CONFIRMATION = "-skip-confirmation";

	private final boolean initDb;
	private final boolean overrideCheck;
	private final String runModuleName;
	private final boolean skipConfirmation;

	/**
	 * @param args
	 * @throws ArgumentException
	 */
	public Arguments(final String... args) throws ArgumentException {
		final List<String> arguments = new ArrayList<String>(CollectionUtil.convertToList(args));
		initDb = arguments.remove(PARAMETER_INITDB);
		if (initDb && !arguments.isEmpty()) {
			throw new ArgumentException(PARAMETER_INITDB + " is a standalone parameter. No other parameters allowed!");
		}

		runModuleName = getRunModuleName(arguments);
		overrideCheck = arguments.remove(PARAMETER_NO_CHECK);
		skipConfirmation = arguments.remove(PARAMETER_SKIP_CONFIRMATION);

		if (!arguments.isEmpty()) {
			throw new ArgumentException("Unknown arguments: " + arguments);
		}
	}

	/**
	 * @param arguments
	 * @return
	 * @throws ArgumentException
	 */
	private static String getRunModuleName(final List<String> arguments) throws ArgumentException {
		final int indexOfRun = arguments.indexOf(PARAMETER_RUN);
		if (-1 == indexOfRun) {
			return null;
		}
		if (arguments.size() - 1 == indexOfRun) { // run is last parameter
			throw new ArgumentException("Must specify module name.");
		}
		final String remove = arguments.remove(indexOfRun + 1); // remove run class
		arguments.remove(indexOfRun); // remove parameter run
		return remove;
	}

	/**
	 * @return the initDb
	 */
	public boolean isInitDb() {
		return initDb;
	}

	/**
	 * @return the overrideCheck
	 */
	public boolean isOverrideCheck() {
		return overrideCheck;
	}

	/**
	 * @return the skipConfirmation
	 */
	public boolean isSkipConfirmation() {
		return skipConfirmation;
	}

	/**
	 * @return the runModuleName
	 */
	public String getRunModuleName() {
		return runModuleName;
	}

	/**
	 * @author Alexandru Bledea
	 * @since Jul 24, 2014
	 */
	@SuppressWarnings("serial")
	static class ArgumentException extends Exception {

		/**
		 * @param message
		 */
		public ArgumentException(final String message) {
			super(message);
		}

	}

}
