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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IActionSet;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;
import de.xwic.sandbox.server.installer.StringUtil;

/**
 * Searches for the System Administrator role and grants all rights to this role.
 * 
 * @author Florian Lippisch
 */
public class GrantAdminRightsModule extends AbstractUpgradeModule {

	private Map<String, List<String>> rights = new HashMap<String, List<String>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#run(de.xwic.appkit.core.installer.InstallationManager)
	 */
	public void run(InstallationManager manager) throws Exception {

		IActionSetDAO asDAO = (IActionSetDAO) DAOSystem.getDAO(IActionSetDAO.class);
		IRoleDAO roleDAO = (IRoleDAO) DAOSystem.getDAO(IRoleDAO.class);
		IScopeDAO scDAO = (IScopeDAO) DAOSystem.getDAO(IScopeDAO.class);

		// find Administration Role
		PropertyQuery q1 = new PropertyQuery();
		q1.addEquals("name", "System Administrator");
		List<?> result = roleDAO.getEntities(null, q1);
		if (result.isEmpty()) {
			log.warn("The role 'System Administrator' does not exist. Rights not updated.");
			return; // early exit
		}

		IRole roAdmin = (IRole) result.get(0);
		// find CRUD ActionSet
		q1 = new PropertyQuery();
		q1.addEquals("name", "CRUD");
		result = asDAO.getEntities(null, q1);
		if (result.isEmpty()) {
			log.warn("The ActionSet 'CRUD' does not exist. Rights not updated.");
			return;
		}
		IActionSet asCRUD = (IActionSet) result.get(0);

		// find ACCESS Action
		IAction acACCESS = createOrGetAction(ApplicationData.SECURITY_ACTION_ACCESS);
		IAction acREAD = createOrGetAction(ApplicationData.SECURITY_ACTION_READ);
		IAction acUPDATE = createOrGetAction(ApplicationData.SECURITY_ACTION_UPDATE);
		IAction acCREATE = createOrGetAction(ApplicationData.SECURITY_ACTION_CREATE);
		IAction acDELETE = createOrGetAction(ApplicationData.SECURITY_ACTION_DELETE);
		IAction acAPPROVE = createOrGetAction(ApplicationData.SECURITY_ACTION_APPROVE);

		cacheRights(roAdmin);

		Setup setup = manager.getSetup();
		for (Iterator<?> it = setup.getModels().iterator(); it.hasNext();) {
			Model model = (Model) it.next();
			for (Iterator<?> itE = model.getManagedEntities().iterator(); itE.hasNext();) {
				String key = (String) itE.next();

				PropertyQuery query = new PropertyQuery();
				query.addEquals("name", key);
				EntityList list = scDAO.getEntities(null, query);
				IScope scope;
				if (!list.isEmpty()) {
					scope = (IScope) list.get(0);
				} else {
					// search for scope
					log.info("Creating scope: " + key);

					scope = (IScope) scDAO.createEntity();
					scope.setName(key);

					// add CRUD ActionSet;
					Set<IEntity> asSet = new HashSet<IEntity>();
					asSet.add(asCRUD);
					scope.setActionSets(asSet);
					scDAO.update(scope);
				}
				grantAdminRight(roAdmin, scope, acREAD);
				grantAdminRight(roAdmin, scope, acUPDATE);
				grantAdminRight(roAdmin, scope, acCREATE);
				grantAdminRight(roAdmin, scope, acDELETE);
				grantAdminRight(roAdmin, scope, acAPPROVE);
			}
		}

		String extraActions = setup.getProperty("extra.access.scopes", null);
		if (extraActions != null) {
			StringTokenizer stk = new StringTokenizer(extraActions);
			while (stk.hasMoreTokens()) {
				String name = stk.nextToken();
				if (!StringUtil.isEmpty(name)) {
					IScope scope = createOrGetScope(name, acACCESS);
					grantAdminRight(roAdmin, scope, acACCESS);
				}
			}
		} else {
			log.warn("There are no scopes defined in the configuration (extra.access.scopes). Scopes not created.");
		}

	}

	/**
	 * Creates the specified right, if it does not already exist.
	 * 
	 * @param role
	 * @param scope
	 * @param action
	 */
	private void cacheRights(IRole role) {
		IRightDAO rightDAO = (IRightDAO) DAOSystem.getDAO(IRightDAO.class);
		EntityList list = rightDAO.getRightsByRole(role);

		for (Iterator<?> it = list.iterator(); it.hasNext();) {
			IRight right = (IRight) it.next();

			// get the scopes for this right
			IScope scope = right.getScope();
			IAction action = right.getAction();

			List<String> actions = rights.get(scope.getName());

			if (null == actions) {
				actions = new ArrayList<String>();
				rights.put(scope.getName(), actions);
			}

			actions.add(action.getName());
		}
	}

	private boolean hasRight(IScope scope, IAction action) {
		List<String> actions = rights.get(scope.getName());
		if (null == actions) {
			return false;
		}

		return actions.contains(action.getName());
	}

	private void grantAdminRight(IRole role, IScope scope, IAction action) {
		if (!hasRight(scope, action)) {
			// create the right
			IRightDAO rightDAO = (IRightDAO) DAOSystem.getDAO(IRightDAO.class);
			IRight currRight = (IRight) rightDAO.createEntity();

			currRight.setScope(scope);
			currRight.setRole(role);
			currRight.setAction(action);

			rightDAO.update(currRight);
		}
	}
}
