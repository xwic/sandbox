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

/**
 * System settings.
 * 
 * @author Florian Lippisch
 */
public class Settings {

	private String jdbcDriverClass = null;
	private String jdbcUserName = null;
	private String jdbcPassword = null;
	private String jdbcConnectionURL = null;

	private String configDir = null;

	/**
	 * @return the configDir
	 */
	public String getConfigDir() {
		return configDir;
	}

	/**
	 * @param configDir
	 *            the configDir to set
	 */
	public void setConfigDir(String configDir) {
		this.configDir = configDir;
	}

	/**
	 * @return the jdbcConnectionURL
	 */
	public String getJdbcConnectionURL() {
		return jdbcConnectionURL;
	}

	/**
	 * @param jdbcConnectionURL
	 *            the jdbcConnectionURL to set
	 */
	public void setJdbcConnectionURL(String jdbcConnectionURL) {
		this.jdbcConnectionURL = jdbcConnectionURL;
	}

	/**
	 * @return the jdbcDriverClass
	 */
	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}

	/**
	 * @param jdbcDriverClass
	 *            the jdbcDriverClass to set
	 */
	public void setJdbcDriverClass(String jdbcDriverClass) {
		this.jdbcDriverClass = jdbcDriverClass;
	}

	/**
	 * @return the jdbcPassword
	 */
	public String getJdbcPassword() {
		return jdbcPassword;
	}

	/**
	 * @param jdbcPassword
	 *            the jdbcPassword to set
	 */
	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	/**
	 * @return the jdbcUserName
	 */
	public String getJdbcUserName() {
		return jdbcUserName;
	}

	/**
	 * @param jdbcUserName
	 *            the jdbcUserName to set
	 */
	public void setJdbcUserName(String jdbcUserName) {
		this.jdbcUserName = jdbcUserName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Driver:         " + jdbcDriverClass + "\n").append("Connection URL: " + jdbcConnectionURL + "\n")
				.append("Username:       " + jdbcUserName + "\n").append("Password:       " + jdbcPassword + "\n")
				.append("ConfigDir:      " + configDir);
		return sb.toString();
	}

}
