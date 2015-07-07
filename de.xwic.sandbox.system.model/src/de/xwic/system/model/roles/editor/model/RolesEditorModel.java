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
package de.xwic.system.model.roles.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.sandbox.base.model.StringUtil;

/**
 * @author Aron Cotrau
 */
public class RolesEditorModel extends EditorModel {

	private DAO roleDAO = null;
	private DAO scopeDAO = null;
	private DAO actionDAO = null;
	private DAO rightDAO = null;

	private Map<String, IScope> scopesMap;
	private List<String> nonEntityScopes;

	/**
	 * @param role
	 */
	@SuppressWarnings("unchecked")
	public RolesEditorModel(IRole role) {
		super(role, true);

		roleDAO = DAOSystem.getDAO(IRoleDAO.class);
		scopeDAO = DAOSystem.getDAO(IScopeDAO.class);
		actionDAO = DAOSystem.getDAO(IActionDAO.class);
		rightDAO = DAOSystem.getDAO(IRightDAO.class);

		scopesMap = new HashMap<String, IScope>();

		List<IScope> scopes = scopeDAO.getEntities(null, new PropertyQuery());
		for (IScope scope : scopes) {
			scopesMap.put(scope.getName(), scope);
		}

		nonEntityScopes = new ArrayList<String>();

		Setup setup = ConfigurationManager.getSetup();
		String extraActions = setup.getProperty("extra.access.scopes", null);
		if (extraActions != null) {
			StringTokenizer stk = new StringTokenizer(extraActions);
			while (stk.hasMoreTokens()) {
				String scopeName = stk.nextToken();
				if (!StringUtil.isEmpty(scopeName)) {
					nonEntityScopes.add(scopeName);
				}
			}
		}
	}

	/**
	 * sets the name to the role
	 * 
	 * @param name
	 */
	public void setName(String name) {
		getRole().setName(name);
		roleDAO.update(getRole());
	}

	/**
	 * @param controlIds
	 */
	public void saveEntityList(List<EntityControlId> controlIds) {
		for (EntityControlId controlId : controlIds) {

			String scopeId = controlId.getScopeId();

			boolean read = controlId.isRead();
			boolean create = controlId.isCreate();
			boolean update = controlId.isUpdate();
			boolean delete = controlId.isDelete();

			PropertyQuery query = new PropertyQuery();
			query.addEquals("name", scopeId);
			EntityList list = scopeDAO.getEntities(null, query);

			if (list.getTotalSize() == 1) {
				IScope scope = (IScope) list.get(0);
				IRole r = getRole();

				query = new PropertyQuery();
				query.addEquals("role", r);
				query.addEquals("scope", scope);
				list = rightDAO.getEntities(null, query);

				for (Object obj : list) {
					IRight right = (IRight) obj;
					IAction action = right.getAction();

					if (null != action) {
						String name = action.getName();

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_CREATE) && !create) {
							rightDAO.delete(right);
						}

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_DELETE) && !delete) {
							rightDAO.delete(right);
						}

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_UPDATE) && !update) {
							rightDAO.delete(right);
						}

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_READ) && !read) {
							rightDAO.delete(right);
						}
					}
				}

				if (read) {
					createRight(scope, ApplicationData.SECURITY_ACTION_READ);
				}

				if (update) {
					createRight(scope, ApplicationData.SECURITY_ACTION_UPDATE);
				}

				if (create) {
					createRight(scope, ApplicationData.SECURITY_ACTION_CREATE);
				}

				if (delete) {
					createRight(scope, ApplicationData.SECURITY_ACTION_DELETE);
				}
			}
		}
	}

	/**
	 * @param otherControlIds
	 */
	public void saveOtherList(List<OtherControlId> otherControlIds) {
		for (OtherControlId controlId : otherControlIds) {

			String scopeId = controlId.getScopeId();

			boolean execute = controlId.isExecute();
			boolean access = controlId.isAccess();

			PropertyQuery query = new PropertyQuery();
			query.addEquals("name", scopeId);
			EntityList list = scopeDAO.getEntities(null, query);

			if (list.getTotalSize() == 1) {
				IScope scope = (IScope) list.get(0);
				IRole r = getRole();

				query = new PropertyQuery();
				query.addEquals("role", r);
				query.addEquals("scope", scope);
				list = rightDAO.getEntities(null, query);

				for (Object obj : list) {
					IRight right = (IRight) obj;
					IAction action = right.getAction();

					if (null != action) {
						String name = action.getName();

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_ACCESS) && !access) {
							rightDAO.delete(right);
						}

						if (name.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_APPROVE) && !execute) {
							rightDAO.delete(right);
						}
					}
				}

				if (execute) {
					createRight(scope, ApplicationData.SECURITY_ACTION_APPROVE);
				}

				if (access) {
					createRight(scope, ApplicationData.SECURITY_ACTION_ACCESS);
				}
			}
		}
	}

	/**
	 * @param scope
	 * @param actionId
	 * @return
	 */
	private IRight createRight(IScope scope, String actionId) {
		IRole r = getRole();
		IAction action = null;
		PropertyQuery query = new PropertyQuery();
		query.addEquals("name", actionId);
		EntityList list = actionDAO.getEntities(null, query);

		if (list.getTotalSize() == 0) {
			action = (IAction) actionDAO.createEntity();
			action.setName(actionId);
			actionDAO.update(action);
		} else {
			action = (IAction) list.get(0);
		}

		IRight right = null;
		query = new PropertyQuery();
		query.addEquals("scope", scope);
		query.addEquals("role", r);
		query.addEquals("action", action);
		list = rightDAO.getEntities(null, query);

		if (list.getTotalSize() == 0) {
			right = (IRight) rightDAO.createEntity();

			if (r.getId() == 0) {
				// new role, update it first,
				// because we get transient exception
				// assigning unsaved entities to another new one.
				roleDAO.update(r);
			}

			right.setRole(r);
			right.setScope(scope);
			right.setAction(action);
			rightDAO.update(right);

			// check if the action is assigned to the scope
			// if not, add it.
			boolean found = false;

			for (IEntity entity : scope.getActions()) {
				IAction act = (IAction) entity;

				if (act.getName().equals(action.getName())) {
					found = true;
					break;
				}
			}

			if (!found) {
				scope.getActions().add(action);
				scopeDAO.update(scope);
			}

			return right;
		}

		return (IRight) list.get(0);
	}

	/**
	 * @return the scopesMap
	 */
	public Map<String, IScope> getScopesMap() {
		return scopesMap;
	}

	/**
	 * @return
	 */
	public List<String> getNonEntityScopes() {
		return nonEntityScopes;
	}

	/**
	 * @return
	 */
	public IRole getRole() {
		return (IRole) entity;
	}
}
