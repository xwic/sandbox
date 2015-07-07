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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.Version;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.sandbox.server.installer.DirectQuery;

/**
 * Abstract implementation of the IUpgradeModule interface. The implementation applies to all products and versions by default.
 * 
 * @author Florian Lippisch
 */
public abstract class AbstractUpgradeModule implements IUpgradeModule {

	/** logging instance */
	protected final Log log = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#isDirectCall()
	 */
	@Override
	public boolean isDirectCall() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#appliesToProduct(java.lang.String)
	 */
	@Override
	public boolean appliesToProduct(String productId) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#appliesToVersion(de.xwic.appkit.core.installer.Version,
	 * de.xwic.appkit.core.installer.Version)
	 */
	@Override
	public boolean appliesToVersion(Version fromVersion, Version toVersion) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#getName()
	 */
	@Override
	public String getName() {
		return getClass().getName();
	}

	/**
	 * Create a new picklist entry, if it does not already exist.
	 * 
	 * @param liste
	 * @param de
	 * @param en
	 * @param key
	 * @return
	 */
	protected IPicklistEntry createPicklistEntry(IPickliste liste, String de, String en) {
		return createPicklistEntry(liste, de, en, null);
	}

	/**
	 * Create a new picklist entry, if it does not already exist.
	 * 
	 * @param liste
	 * @param de
	 * @param en
	 * @param key
	 * @return
	 */
	protected IPicklistEntry createPicklistEntry(IPickliste liste, String de, String en, String key) {

		IPicklisteDAO dao = DAOSystem.getDAO(IPicklisteDAO.class);
		IPicklistEntry entry = null;
		if (key != null) {
			entry = dao.getPickListEntryByKey(liste.getKey(), key);
		}
		if (entry == null) {
			// search for a entry that has the same german title
			List<?> entries = dao.getAllEntriesToList(liste);
			for (Iterator<?> it = entries.iterator(); it.hasNext();) {
				IPicklistEntry pe = (IPicklistEntry) it.next();
				String bez = pe.getBezeichnung("DE");
				if (de.equals(bez)) {
					entry = pe;
					if (key != null && (entry.getKey() == null || entry.getKey().length() == 0)) {
						entry.setKey(key);
						dao.update(entry);
					}
					break;
				}
			}
		}

		// entry does not exist -> create it.
		if (entry == null) {
			entry = dao.createPickListEntry(liste);
			dao.createBezeichnung(entry, "DE", de);
			dao.createBezeichnung(entry, "EN", en != null ? en : de);
			entry.setKey(key);
			dao.update(entry);
		}

		return entry;
	}

	/**
	 * Returns true if the specified scope exists.
	 * 
	 * @param scopeDAO
	 * @param key
	 * @return
	 */
	protected boolean scopeExists(String key) {
		IScopeDAO scopeDAO = DAOSystem.getDAO(IScopeDAO.class);
		PropertyQuery query = new PropertyQuery();
		query.addEquals("name", key);
		EntityList list = scopeDAO.getEntities(null, query);
		return (!list.isEmpty());
	}

	/**
	 * Creates or gets the specified scope.
	 * 
	 * @param scDAO
	 * @param scope_useradmin
	 * @param acACCESS
	 */
	protected IScope createOrGetScope(String scopeName, IAction action) {

		IScopeDAO scopeDAO = DAOSystem.getDAO(IScopeDAO.class);
		PropertyQuery query = new PropertyQuery();
		query.addEquals("name", scopeName);
		EntityList list = scopeDAO.getEntities(null, query);
		if (list.isEmpty()) {
			log.info("Creating scope " + scopeName);
			IScope scope = scopeDAO.createEntity();
			scope.setName(scopeName);

			// add Action;
			Set<IEntity> acSet = new HashSet<IEntity>();
			acSet.add(action);
			scope.setActions(acSet);
			scopeDAO.update(scope);
			return scope;
		}
		return (IScope) list.get(0);

	}

	/**
	 * Creates the specified right, if it does not already exist.
	 * 
	 * @param role
	 * @param scope
	 * @param action
	 */
	protected void createRight(IRole role, IScope scope, IAction action) {
		IRightDAO rightDAO = DAOSystem.getDAO(IRightDAO.class);
		PropertyQuery query = new PropertyQuery();

		query.addEquals("scope", scope);
		query.addEquals("role", role);
		query.addEquals("action", action);

		EntityList list = rightDAO.getEntities(null, query);

		if (list.getTotalSize() == 0) {
			IRight currRight = rightDAO.createEntity();

			currRight.setScope(scope);
			currRight.setRole(role);
			currRight.setAction(action);

			rightDAO.update(currRight);
		}
	}

	/**
	 * @param name
	 * @return
	 */
	protected IRole createOrGetRole(String name) {

		IRoleDAO roDAO = DAOSystem.getDAO(IRoleDAO.class);

		PropertyQuery query = new PropertyQuery();
		query.addEquals("name", name);
		EntityList result = roDAO.getEntities(null, query);
		IRole role = null;

		if (result.isEmpty()) {
			role = roDAO.createEntity();
			role.setName(name);
			roDAO.update(role);
		} else {
			role = (IRole) result.get(0);
		}

		return role;

	}

	/**
	 * Searches for the action with the specified name. If the action does not exist, a new action is created.
	 * 
	 * @param actionName
	 * @return
	 */
	protected IAction createOrGetAction(String actionName) {
		IActionDAO acDAO = DAOSystem.getDAO(IActionDAO.class);
		PropertyQuery q1 = new PropertyQuery();
		q1.addEquals("name", actionName);
		List<?> result = acDAO.getEntities(null, q1);
		IAction action;
		if (result.isEmpty()) {
			action = acDAO.createEntity();
			action.setName(actionName);
			acDAO.update(action);
		} else {
			action = (IAction) result.get(0);
		}
		return action;
	}

	/**
	 * @param manager
	 * @param prop
	 * @return
	 */
	protected String getProperty(InstallationManager manager, String prop) {
		return manager.getProperties().getProperty(getClass().getSimpleName() + "." + prop);
	}

	/**
	 * @param query
	 * @param batch
	 * @return
	 */
	protected List<Collection<Integer>> getIds(String query, int batch, Object... params) {
		List<Integer> createCollection = (List<Integer>) DirectQuery.executeQuery(query, params);
		return CollectionUtil.breakInSets(createCollection, batch);
	}

	/**
	 * @param batches
	 */
	protected int getTotalSize(List<Collection<Integer>> batches) {
		int size = 0;

		for (Collection<Integer> collection : batches) {
			size += collection.size();
		}

		return size;
	}
}
