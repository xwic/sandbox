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
package de.xwic.sandbox.server.installer.modules.security;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.util.ServerConfig;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * @author Florian Lippisch
 */
public class AddDefaultRightsModule extends AbstractUpgradeModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#run(de.xwic.appkit.core.installer.InstallationManager)
	 */
	public void run(InstallationManager manager) throws Exception {
		// check scopes
		IScopeDAO scDAO = (IScopeDAO) DAOSystem.getDAO(IScopeDAO.class);
		IActionSetDAO asDAO = (IActionSetDAO) DAOSystem.getDAO(IActionSetDAO.class);
		IRoleDAO roDAO = (IRoleDAO) DAOSystem.getDAO(IRoleDAO.class);
		//IRightDAO riDAO = (IRightDAO)DAOSystem.getDAO(IRightDAO.class);

		// find CRUD ActionSet
		PropertyQuery q1 = new PropertyQuery();
		q1.addEquals("name", "CRUD");
		EntityList result = asDAO.getEntities(null, q1);
		if (result.isEmpty()) {
			throw new IllegalStateException("CRUD ActionSet does not exist!");
		}
		IActionSet asCRUD = (IActionSet) result.get(0);

		IAction acACCESS = createOrGetAction("ACCESS");
		IAction acAPPROVE = createOrGetAction(ApplicationData.SECURITY_ACTION_APPROVE);
		//IAction acREAD = createOrGetAction(ApplicationData.SECURITY_ACTION_READ);

		// find Administration Role
		q1 = new PropertyQuery();
		q1.addEquals("name", "System Administrator");
		result = roDAO.getEntities(null, q1);
		IRole roAdmin;
		if (result.isEmpty()) {
			roAdmin = (IRole) roDAO.createEntity();
			roAdmin.setName("System Administrator");
			roDAO.update(roAdmin);
		} else {
			roAdmin = (IRole) result.get(0);
		}

		// Add approve action to CRUD actionSet
		if (!asCRUD.getActions().contains(acAPPROVE)) {
			log.info("Added APPROVE Action to CRUD ActionSet");
			asCRUD.getActions().add(acAPPROVE);
			asDAO.update(asCRUD);
		}

		Setup setup = ConfigurationManager.getSetup();
		for (Iterator<?> it = setup.getModels().iterator(); it.hasNext();) {
			Model model = (Model) it.next();
			for (Iterator<?> itE = model.getManagedEntities().iterator(); itE.hasNext();) {
				String key = (String) itE.next();

				if (!scopeExists(key)) {
					// search for scope

					log.info("Creating scope: " + key);

					IScope scope = (IScope) scDAO.createEntity();
					scope.setName(key);

					// add CRUD ActionSet;
					Set<IEntity> asSet = new HashSet<IEntity>();
					asSet.add(asCRUD);
					scope.setActionSets(asSet);
					scDAO.update(scope);
				}
			}
		}

		// check if "System-Scopes" exist.
		IScope scope;

		scope = createOrGetScope(ApplicationData.SCOPE_USERADMIN, acACCESS);
		createRight(roAdmin, scope, acACCESS);

		scope = createOrGetScope(ApplicationData.SCOPE_PICKLISTADMIN, acACCESS);
		createRight(roAdmin, scope, acACCESS);

		createOrGetScope(ApplicationData.SCOPE_SYSADMIN, acACCESS);

		ServerConfig.set(SandboxModelConfig.ROLE_ADMIN_ID, roAdmin.getId());
	}
}
