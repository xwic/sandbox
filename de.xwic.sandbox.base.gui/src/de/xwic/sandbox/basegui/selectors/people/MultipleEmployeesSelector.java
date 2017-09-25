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
package de.xwic.sandbox.basegui.selectors.people;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.controls.Button;
import de.jwic.controls.ListBox;
import de.jwic.controls.ListEntry;
import de.jwic.data.ISelectElement;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.ILazyEval;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.StringUtil;
import de.xwic.sandbox.basegui.util.IPageListener;


/**
 * @author Raluca Geogia
 *
 */
public class MultipleEmployeesSelector extends ControlContainer {

	protected ListBox usersList;
	protected PeopleComboSelector employeeCombo;
	protected Button btRemoveSelected;
	protected Button btAddEmployee;
	protected IMitarbeiterDAO mitDAO = SandboxModelConfig.getMitarbeiterDAO();
	
	protected Map<String, IMitarbeiter> selectedEmployees;

	/**
	 * @param container
	 * @param name
	 */
	public MultipleEmployeesSelector(IControlContainer container, String name) {
		super(container, name);

		selectedEmployees = new HashMap<String, IMitarbeiter>();
		createControls();
	}
	
	/**
	 * 
	 */
	protected void createControls() {
		usersList = new ListBox(this, "usersListBox");
		usersList.setChangeNotification(true);
		usersList.setSize(10);
		usersList.setFillWidth(true);
		usersList.setMultiSelect(true);
		usersList.addElementSelectedListener(new ElementSelectedListener() {

			@Override
			public void elementSelected(ElementSelectedEvent event) {
				btRemoveSelected.setEnabled(!usersList.getSelectedKey().isEmpty());
			}
		});

		employeeCombo = PeopleComboSelectorManager.getFactory().generateCombo(this, "employeeCombo", true);
		employeeCombo.setWidth(250);

		btRemoveSelected = new Button(this, "btRemoveSelected");
		btRemoveSelected.setTitle("Remove Selected");
		btRemoveSelected.setEnabled(false);
		btRemoveSelected.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				for (String key : usersList.getSelectedKeys()) {
					selectedEmployees.remove(key);
					usersList.removeElementByKey(key);
				}
			}
		});

		btAddEmployee = new Button(this, "btAddEmployee");
		btAddEmployee.setTitle("Add");
		btAddEmployee.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				long selectedEntityId = employeeCombo.getSelectedEntityId();
				Map<String, ISelectElement> elements = usersList.buildElementsMap();
				String stringId = String.valueOf(selectedEntityId); // this is the Employee Flags Id
				if (selectedEntityId > 0 && !elements.containsKey(stringId)) {
					usersList.addElement(mitDAO.buildTitle(employeeCombo.getSelectedEmployee()), stringId);
					selectedEmployees.put(stringId, employeeCombo.getSelectedEmployee());
				}
				employeeCombo.clearSelection();
			}
		});
	}

	public void addPageListenerOnEmployeeCombo(IPageListener listener) {
		employeeCombo.addPageListener(listener);
	}

	/**
	 * 
	 */
	public void setDefaultUsers() {
		usersList.clear();
		selectedEmployees.clear();
	}

	/**
	 * @param peopleIds
	 */
	public void setUsersFromPeopleIds(String peopleIds) {
		if (peopleIds != null && !peopleIds.isEmpty()) {
			PropertyQuery query = getQueryForIds(peopleIds);
			List<IMitarbeiter> userList = mitDAO.getEntities(null, query);
			setUsersFromMitarbeiterCollection(userList);
		}
	}

	/**
	 * @param col
	 */
	public void setUsersFromMitarbeiterCollection(Collection<IMitarbeiter> col) {
		if (col != null && !col.isEmpty()) {
			for (IMitarbeiter mit : col) {
				addEmployeeToList(mit);
			}
		}
	}

	/**
	 * @param col
	 */
	public void setUsersFromPeopleCollection(Collection<IMitarbeiter> col) {
		List<String> ids = CollectionUtil.createCollection(col, new ILazyEval<IMitarbeiter, String>() {

			@Override
			public String evaluate(IMitarbeiter obj) {
				return String.valueOf(obj.getId());
			}
		}, new ArrayList<String>());

		setUsersFromPeopleIds(StringUtil.join(ids, ";"));
	}

	/**
	 * @param ef
	 */
	private void addEmployeeToList(IMitarbeiter mit) {
		String key = String.valueOf(mit.getId());
		usersList.addElement(mitDAO.buildTitle(mit), key);
		selectedEmployees.put(key, mit);
	}

	/**
	 * @param ids
	 * @return
	 */
	private PropertyQuery getQueryForIds(String ids) {
		String[] list = ids.split(";");
		PropertyQuery query = new PropertyQuery();
		for (String id : list) {
			query.addOrEquals("id", Integer.valueOf(id));
		}
		return query;
	}

	/**
	 * @return
	 */
	public String getSelectedEmployeesKeys() {
		StringBuilder sb = new StringBuilder();
		for (ListEntry listEntry : usersList.buildEntryList()) {
			sb.append(listEntry.getKey());
			sb.append(";");
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}

	/**
	 * @return - a Collection of IEmployeeFlags
	 */
	public Collection<IMitarbeiter> getSelectedEmployees() {
		return selectedEmployees.values();
	}

	/**
	 * @return - a Set of IMitarbeiter
	 */
	public Set<IMitarbeiter> getSelectedEmployeesSet() {
		Set<IMitarbeiter> set = new HashSet<IMitarbeiter>();
		for (IMitarbeiter mit : getSelectedEmployees()) {
			set.add(mit);
		}

		return set;
	}

	/**
	 * Set the number of rows for the users list
	 * 
	 * @param size
	 */
	public void setUsersListSize(int size) {
		usersList.setSize(size);
	}

	/**
	 * @param width
	 */
	public void setWidth(int width) {
		employeeCombo.setWidth(width);
	}

	/**
	 * @param title
	 * @param tooltip
	 * @param iconEnabled
	 * @param iconDisabled
	 */
	public void configureAddButton(String title, String tooltip, ImageRef iconEnabled, ImageRef iconDisabled) {
		btAddEmployee.setTitle(title);
		btAddEmployee.setTooltip(tooltip);
		btAddEmployee.setIconEnabled(iconEnabled);
		btAddEmployee.setIconDisabled(iconDisabled);
	}

	/**
	 * @param title
	 * @param tooltip
	 * @param iconEnabled
	 * @param iconDisabled
	 */
	public void configureRemoveButton(String title, String tooltip, ImageRef iconEnabled, ImageRef iconDisabled) {
		btRemoveSelected.setTitle(title);
		btRemoveSelected.setTooltip(tooltip);
		btRemoveSelected.setIconEnabled(iconEnabled);
		btRemoveSelected.setIconDisabled(iconDisabled);
	}

}
