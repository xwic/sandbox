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

package de.xwic.sandbox.server.installer.modules.config;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.security.util.Rights;
import de.xwic.appkit.core.util.MapUtil;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * This class is available in the ConfigUpdateScript as variable "ctx" and allows many configuration checks/updates with a few simple lines.
 * 
 * @author lippisch
 */
public class ConfigUpgradeScriptHelper {

	protected final Log log = LogFactory.getLog(getClass());
	private final InstallationManager manager;

	/**
	 * @param manager
	 */
	public ConfigUpgradeScriptHelper(InstallationManager manager) {
		this.manager = manager;
	}

	/**
	 * Find a mitarbeiter by its logon name.
	 * 
	 * @param logonName
	 * @return
	 */
	public IMitarbeiter findMitarbeiter(String logonName) {
		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("logonName", logonName);
		List<IMitarbeiter> list = SandboxModelConfig.getMitarbeiterDAO().getEntities(new Limit(0, 1), pq);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		log.warn("Can not find IMitarbeiter with logon name '" + logonName + "'");
		return null;
	}

	/**
	 * Generic method to apply property/attribute values from a map to an object.
	 * 
	 * @param area
	 * @param args
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void applyAttributes(Object bean, Map<String, Object> args) throws Exception {

		for (String propertyName : args.keySet()) {

			Object value = args.get(propertyName);

			PropertyDescriptor pd = new PropertyDescriptor(propertyName, bean.getClass());
			Method mWrite = pd.getWriteMethod();
			if (mWrite == null) {
				throw new RuntimeException("Property '" + propertyName + "' does not exist or does not have a write method in class '"
						+ bean.getClass().getName() + "'");
			}

			// convert arguments
			Class<?> propertyType = pd.getPropertyType();
			if (propertyType.equals(IPicklistEntry.class)) {

			}
			mWrite.invoke(bean, value);

		}

	}

	/**
	 * @param roleName
	 */
	public void createRole(String roleName) {
		IRole role = getRole(roleName);

		if (role != null) {
			log.warn("Role '" + roleName + "' already exists");
			return;
		}

		IRoleDAO roleDao = DAOSystem.getDAO(IRoleDAO.class);
		role = roleDao.createEntity();
		role.setName(roleName);
		roleDao.update(role);

		log.info("Created role: " + roleName);
	}

	/**
	 * @param scopeName
	 * @param roleNames
	 * @param actions
	 */
	public void addScopeToRoles(String scopeName, List<String> roleNames, String... actions) {

		IRightDAO rightDao = (IRightDAO) DAOSystem.getDAO(IRightDAO.class);

		for (String roleName : roleNames) {
			IScope scope = getScope(scopeName);
			IRole role = getRole(roleName);

			if (scope == null) {
				log.error("Scope " + scopeName + " cannot be found!!!!!!!");
				continue;
			}

			if (role == null) {
				log.error("Role " + roleName + " cannot be found!!!!!!!");
				continue;
			}

			for (String strAction : actions) {

				IAction action = getAction(strAction);

				if (action == null) {
					log.error("Action " + strAction + " cannot be found");
					return;
				}

				try {
					rightDao.createRight(role, scope, action);

					log.info(String.format("Added '%s' to '%s' with action %s", scopeName, roleName, action.getName()));
				} catch (DataAccessException ex) {
					log.info(String.format("Right '%s' - '%s' - '%s' already exists, moving on...", role.getName(), scope.getName(),
							action.getName()));
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 *
	 * @param configValuesMap
	 * @return
	 */
	public String getJSONStringForMap(Map<String, Object> configValuesMap) {
		JSONObject jsonObject = new JSONObject(configValuesMap);
		return jsonObject.toString();
	}

	/**
	 * @param name
	 * @return
	 */
	private IAction getAction(String name) {
		IActionDAO actionDao = (IActionDAO) DAOSystem.getDAO(IActionDAO.class);

		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", name);

		return (IAction) actionDao.getEntities(null, pq).get(0);
	}

	/**
	 * @param name
	 * @return
	 */
	private IScope getScope(String name) {
		IScopeDAO scopeDao = (IScopeDAO) DAOSystem.getDAO(IScopeDAO.class);

		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", name);

		EntityList list = scopeDao.getEntities(null, pq);

		return !list.isEmpty() ? (IScope) list.get(0) : null;
	}

	/**
	 * @param name
	 * @return
	 */
	private IRole getRole(String name) {
		IRoleDAO roleDao = (IRoleDAO) DAOSystem.getDAO(IRoleDAO.class);

		PropertyQuery pq = new PropertyQuery();
		pq.addEquals("name", name);

		EntityList list = roleDao.getEntities(null, pq);

		return !list.isEmpty() ? (IRole) list.get(0) : null;
	}

	/**
	 * @param fromEntity
	 * @param toEntities
	 */
	public void duplicateEntityAccessLevel(final String fromEntity, final String... toEntities) {
		if (null == toEntities || 0 == toEntities.length) {
			log.error("No To entities provided", new IllegalStateException());
			return;
		}
		final IRightDAO rightDao = DAOSystem.getDAO(IRightDAO.class);
		final Map<IRole, List<IAction>> fromRolesAndRights = Collections.unmodifiableMap(getRolesAndRights(rightDao, fromEntity));
		if (fromRolesAndRights.isEmpty()) {
			log.error(fromEntity + " doesn't have any actions in any roles", new IllegalStateException());
			return;
		}
		for (final String entity : toEntities) {
			final Map<IRole, List<IAction>> entityRolesAndRights = getRolesAndRights(rightDao, entity);
			createScopes(entity, entityRolesAndRights, fromRolesAndRights, rightDao);
		}
	}

	/**
	 * @param scope
	 * @param entityRolesAndRights
	 * @param fromRolesAndRights
	 * @param rightDao
	 */
	private void createScopes(final String scope, final Map<IRole, List<IAction>> entityRolesAndRights,
			final Map<IRole, List<IAction>> fromRolesAndRights, final IRightDAO rightDao) {
		final IScope theScope = getScope(scope);
		for (final Entry<IRole, List<IAction>> fromEntry : fromRolesAndRights.entrySet()) {
			final IRole currentRole = fromEntry.getKey();
			final List<IAction> shouldHaveThese = fromEntry.getValue();

			final List<IAction> currentScopeActions;
			if (entityRolesAndRights.containsKey(currentRole)) {
				currentScopeActions = entityRolesAndRights.get(currentRole);
				currentScopeActions.retainAll(shouldHaveThese);
			} else {
				currentScopeActions = Collections.emptyList();
			}

			for (final IAction mustHaveAction : shouldHaveThese) {
				if (currentScopeActions.contains(mustHaveAction)) {
					log.info(String.format("Right %s already exists for %s on %s", mustHaveAction.getName(), currentRole.getName(), scope));
				} else {
					log.info(String.format("Creating %s right for %s on %s", mustHaveAction.getName(), currentRole.getName(), scope));
					rightDao.createRight(currentRole, theScope, mustHaveAction);
				}
			}
		}
	}

	/**
	 * @param scopeName
	 * @return
	 */
	private static Map<IRole, List<IAction>> getRolesAndRights(final IRightDAO rightDao, final String scopeName) {
		final PropertyQuery pq = new PropertyQuery();
		pq.addEquals("scope.name", scopeName);

		final EntityList<IRight> entities = rightDao.getEntities(null, pq);
		return MapUtil.generateMapOfList(entities, Rights.GET_ROLE, Rights.GET_ACTION);

	}
}
