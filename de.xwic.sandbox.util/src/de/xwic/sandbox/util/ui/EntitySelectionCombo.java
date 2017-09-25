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
package de.xwic.sandbox.util.ui;

import java.util.List;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.ScopeActionKey;
import de.xwic.appkit.webbase.toolkit.components.AbstractEntityComboControl;

/**
 * @author Raluca Geogia
 *
 */
public class EntitySelectionCombo<I extends IEntity> extends AbstractEntityComboControl<I> {

	protected DAO<I> dao;
	protected EntityQuery query;

	private ScopeActionKey scopeAction;

	private long selectedId = 0;
	private DAO selectedEntityDAO = null;

	private boolean addSelectedEntry;

	private String emptySelectionText = "";

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param entityDAO
	 * @param query
	 * @param setupEntries
	 */
	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query,
			boolean setupEntries) {
		this(container, name, allowEmptySelection, entityDAO, query, setupEntries, null, true);
	}

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param entityDAO
	 * @param query
	 * @param scopeAction
	 */
	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query,
			ScopeActionKey scopeAction) {
		this(container, name, allowEmptySelection, entityDAO, query, true, scopeAction, true);
	}

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param entityDAO
	 * @param query
	 * @param scopeAction
	 */
	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query,
			String scope) {
		this(container, name, allowEmptySelection, entityDAO, query, true,
				new ScopeActionKey(scope, ApplicationData.SECURITY_ACTION_ACCESS), true);
	}

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param entityDAO
	 * @param query
	 */
	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query) {
		this(container, name, allowEmptySelection, entityDAO, query, true, null, true);
	}

	/**
	 * @param container
	 * @param name
	 * @param allowEmptySelection
	 * @param entityDAO
	 * @param query
	 * @param setupEntries
	 */
	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query,
			boolean setupEntries, ScopeActionKey scopeAction, boolean addSelectedEntry) {
		super(container, name);
		this.allowEmptySelection = allowEmptySelection;
		this.dao = entityDAO;
		this.query = query;
		this.scopeAction = scopeAction;
		this.addSelectedEntry = addSelectedEntry;

		if (setupEntries) {
			setupEntries();

			if (allowEmptySelection) {
				setSelectedKey("0");
			}
		}

		boolean hasRight = hasRight(scopeAction);
		setEnabled(hasRight);
	}

	public EntitySelectionCombo(IControlContainer container, String name, boolean allowEmptySelection, DAO entityDAO, EntityQuery query,
			String scope, boolean addSelectedEntry) {
		this(container, name, allowEmptySelection, entityDAO, query, true,
				new ScopeActionKey(scope, ApplicationData.SECURITY_ACTION_ACCESS), addSelectedEntry);
	}

	private boolean hasRight(ScopeActionKey scopeAction) {
		if (null == scopeAction) {
			return true;
		}

		String scope = scopeAction.getScopeName();
		String action = scopeAction.getActionName();

		boolean hasRight = DAOSystem.getSecurityManager().hasRight(scope, action);
		return hasRight;
	}

	/**
	 * Change the query.
	 * 
	 * @param query
	 */
	public void setQuery(EntityQuery query) {
		this.query = query;
		// see if we already have something selected, if yes, add that entity to
		// the new list
		IEntity selection = getSelectedEntry();
		if (addSelectedEntry && null != selection) {
			selectedId = selection.getId();
			selectedEntityDAO = DAOSystem.findDAOforEntity(selection.type());
		} else {
			selectedId = 0;
		}

		clear();
		setupEntries();
	}

	/**
	 * @return the query for the input
	 */
	public EntityQuery getQuery() {
		return query;
	}

	/**
	 * Reload the content.
	 */
	public void refreshList() {
		refreshList(false);
	}

	/**
	 * Reload the content. The parameter refers to wherever the old selection should be held or not
	 * 
	 * @param keepSelection
	 */
	public void refreshList(boolean keepSelection) {
		String oldSelectionKey = null;
		if (keepSelection) {
			oldSelectionKey = getSelectedKey();
		}

		clear();
		setupEntries(keepSelection, oldSelectionKey);

		if (oldSelectionKey == null && allowEmptySelection) {
			setSelectedKey("0");
		}
	}

	/**
	 * set an internal array for the given list of entries
	 * 
	 * @param entryList
	 */
	protected void setupEntries(boolean keepSelection, String oldSelectionKey) {
		List<?> entryList = createInputList();

		if (null != entryList) {
			// add empty selection
			if (allowEmptySelection) { // && getContentProvider().getObjectFromKey("0") == null) {
				addElement(emptySelectionText, "0");
			}

			String defSel = keepSelection ? oldSelectionKey : null;

			for (int i = 0; i < entryList.size(); i++) {
				IEntity entity = (IEntity) entryList.get(i);
				if (entity != null) {
					String title = toStringEntity(entity);

					String key = String.valueOf(entity.getId());

					if (i == 0 && !keepSelection) {
						defSel = key;
					}

					addElement(title, key);
				}
			}

			if (selectedId == 0) {
				if (defSel != null && !allowEmptySelection && !keepSelection) {
					setSelectedKey(defSel);
				} else if (keepSelection && null != oldSelectionKey) {
					setSelectedKey(oldSelectionKey);
				}
			} else if (addSelectedEntry) {
				// add the selection, if there is one
				addEntity(selectedEntityDAO.getEntity(selectedId), true);
			}
		}
	}

	/**
	 * @param entity
	 * @param select
	 */
	public void addEntity(IEntity entity, boolean select) {
		String key = String.valueOf(entity.getId());
		if (!containsEntity(entity)) {
			String title = toStringEntity(entity);

			addElement("[" + title + "]", key);
			if (select) {
				setSelectedKey(key);
			}
		} else if (select) {
			setSelectedKey(key);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled && hasRight(scopeAction));
	}

	/**
	 * @param entity
	 * @return a String representation of the entity, for GUI display
	 */
	protected String toStringEntity(IEntity entity) {
		return DAOSystem.findDAOforEntity(entity.type()).buildTitle(entity);
	}

	@Override
	public DAO<I> getEntityDao() {
		return dao;
	}

	/**
	 * @return a list of IEntity objects
	 */
	protected List<I> createInputList() {
		return dao.getEntities(null, query);
	}

	@Override
	protected void setupEntries() {
		setupEntries(false, null);
	}

	public void selectEntry(IEntity entity) {
		if (null != entity) {
			if (containsEntity(entity)) {
				setSelectedKey(Long.toString(entity.getId()));
			} else if (addSelectedEntry) {
				// add the new entity to the list
				addEntity(entity, true);
			}
		} else {
			setSelectedKey("0");
		}

	}

	/**
	 * @param entity
	 * @return if an entity exists in the combo
	 */
	public boolean containsEntity(IEntity entity) {
		return containsId(entity.getId());
	}

	/**
	 * @param id
	 * @return if an id is already inserted in the combo
	 */
	public boolean containsId(long id) {
		return getContentProvider().getObjectFromKey(String.valueOf(id)) != null;
	}

	/**
	 * @return the emptySelectionText
	 */
	public String getEmptySelectionText() {
		return emptySelectionText;
	}

	/**
	 * @param emptySelectionText
	 *            the emptySelectionText to set
	 */
	public void setEmptySelectionText(String emptySelectionText) {
		this.emptySelectionText = emptySelectionText;
		refreshList();
	}

	/**
	 * @return the addSelectedEntry
	 */
	public boolean isAddSelectedEntry() {
		return addSelectedEntry;
	}

	/**
	 * @param addSelectedEntry
	 *            the addSelectedEntry to set
	 */
	public void setAddSelectedEntry(boolean addSelectedEntry) {
		this.addSelectedEntry = addSelectedEntry;
	}

}
