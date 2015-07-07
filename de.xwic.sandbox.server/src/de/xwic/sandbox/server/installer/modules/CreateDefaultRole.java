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
package de.xwic.sandbox.server.installer.modules;

import java.util.List;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * Creates default role
 * 
 * @author Dogot Nicu
 *
 */
public class CreateDefaultRole extends AbstractUpgradeModule {

	private IRole role;
	private IRoleDAO roleDAO;

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.server.installer.IUpgradeModule#run(de.xwic.sandbox.server.installer.InstallationManager)
	 */
	@Override
	public void run(InstallationManager manager) throws Exception {

		log.info("Creating default role...");

		roleDAO = DAOSystem.getDAO(IRoleDAO.class);

		PropertyQuery propertyQuery = new PropertyQuery();
		propertyQuery.addEquals("name", "Default");
		List<IRole> roles = roleDAO.getEntities(null, propertyQuery);

		if (roles.isEmpty()) {
			role = roleDAO.createEntity();
			role.setName("Default");
			roleDAO.update(role);
		}
	}

}
