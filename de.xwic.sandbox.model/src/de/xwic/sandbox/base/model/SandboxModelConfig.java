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
/**
 * 
 */
package de.xwic.sandbox.base.model;

import java.io.File;

import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.daos.IUserListProfileDAO;

/**
 * @author Raluca Geogia
 *
 */
public class SandboxModelConfig {

	private static File webRootDirectory;
	
	public static final String DOMAIN_ID = "de.xwic.sandbox";

	public static final String ROLE_ADMIN_ID = "adminID";
	public static final String ROLE_DEFAULT = "Default";
	public static final String SCOPE_SYSADMIN = "SYSADMIN";

	// START
	public static final String MOD_GET_STARTED = "MOD_GET_STARTED";
	public static final String SMOD_GET_STARTED_HOME = "SMOD_GET_STARTED_HOME";

	//DEMO_APP
	public static final String MOD_DEMO_APP = "MOD_DEMO_APP";
	public static final String SMOD_DEMO_APP_ADDRESS_BOOK = "SMOD_DEMO_APP_ADDRESS_BOOK";

	// RESOURCES
	public static final String MOD_RESOURCES = "MOD_RESOURCES";
	public static final String SMOD_RESOURCES_EMPLOYEE = "SMOD_RESOURCES_EMPLOYEE";

	//System
	public static final String MOD_SYSTEM = "MOD_SYSTEM";
	public static final String SMOD_SYSTEM_ROLES = "SMOD_SYSTEM_ROLES";
	public static final String SMOD_SYSTEM_PICKLIST = "SMOD_SYSTEM_PICKLIST";

	/**
	 * Register all daos and queries here for the model.
	 * 
	 * @param factory
	 */
	public static void register(DAOFactory factory) {
	}

	/**
	 * @return
	 */
	public static File getWebRootDirectory() {
		return webRootDirectory;
	}

	/**
	 * @param webRootDirectory
	 *            the webRootDirectory to set
	 */
	public static void setWebRootDirectory(File webRootDirectory) {
		SandboxModelConfig.webRootDirectory = webRootDirectory;
	}

	/**
	 * @return
	 */
	public static IMitarbeiterDAO getMitarbeiterDAO() {
		return DAOSystem.getDAO(IMitarbeiterDAO.class);
	}

	/**
	 * @return
	 */
	public static IUserListProfileDAO getUserListProfileDAO() {
		return DAOSystem.getDAO(IUserListProfileDAO.class);
	}

	/**
	 * @return
	 */
	public static IPicklisteDAO getPicklistDAO() {
		return DAOSystem.getDAO(IPicklisteDAO.class);
	}
}
