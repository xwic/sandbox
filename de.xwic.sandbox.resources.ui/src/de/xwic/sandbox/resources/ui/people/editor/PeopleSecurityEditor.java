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
package de.xwic.sandbox.resources.ui.people.editor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBoxGroup;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.controls.wizard.ValidationException;
import de.jwic.data.ISelectElement;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.resources.model.people.editor.model.PeopleEditorModel;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class PeopleSecurityEditor extends ControlContainer implements IEditorModelListener {

	private static final String DEFAULT_ROLE = "default";

	private InputBox txtLogonName;

	private CheckBoxGroup chkRoles;
	private PeopleEditorModel model = null;

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public PeopleSecurityEditor(IControlContainer container, String name, PeopleEditorModel model) {
		super(container, name);

		this.model = model;
		this.model.addModelListener(this);
		createControls();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent
	 * )
	 */
	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (PeopleEditorModel.EMPLOYEE_SAVED == event.getEventType()) {
			// now also save the security stuff !
			performSave();
		}

		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
			performValidation();
		}

	}

	/**
	 * 
	 */
	private void createControls() {

		new Label(this, "loginNameLabel").setText("Login Name");

		IMitarbeiter mit = model.getEmployee();

		txtLogonName = new InputBox(this, "logonName");
		txtLogonName.setText(null != mit && mit.getLogonName() != null ? mit.getLogonName() : "");
		txtLogonName.setWidth(300);

		new Label(this, "rolesLabel").setText("Roles");

		chkRoles = new CheckBoxGroup(this, "chkRoles");
		chkRoles.setColumns(1);

		loadRoles();
		loadUserRoles();
	}

	/**
	 * 
	 */
	private void loadRoles() {
		boolean allowAllRoles = DAOSystem.getSecurityManager().hasRight(IRole.class.getName(), ApplicationData.SECURITY_ACTION_CREATE);
		Set<IEntity> usrRoles = new HashSet<IEntity>();
		if (!allowAllRoles) {
			// if the person is not the admin, allow him to only set the roles he has
			IUser usr = DAOSystem.getSecurityManager().getCurrentUser();
			usrRoles = usr.getRoles();
		}

		// load all roles
		IRoleDAO roleDAO = DAOSystem.getDAO(IRoleDAO.class);

		PropertyQuery pqRoles = new PropertyQuery();
		pqRoles.setSortField("name");

		List<IRole> roles = roleDAO.getEntities(null, pqRoles);
		for (IRole role : roles) {
			int roleId = role.getId();
			String key = String.valueOf(roleId);
			String roleName = role.getName();

			if (!allowAllRoles) {
				if (hasRole(usrRoles, roleId)) {
					chkRoles.addElement(roleName, key);
				}
			} else {
				chkRoles.addElement(roleName, key);
			}
		}
	}

	/**
	 * @param roles
	 * @param roleId
	 * @return
	 */
	private boolean hasRole(Set<IEntity> roles, int roleId) {
		for (IEntity entity : roles) {
			if (entity.getId() == roleId) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 */
	private void loadUserRoles() {
		IUser user = model.loadUser();

		// load user roles.
		if (user != null) {
			StringBuffer sb = new StringBuffer();
			Set<IEntity> userRoles = user.getRoles();

			for (IEntity role : userRoles) {
				if (chkRoles.getContentProvider().getObjectFromKey(String.valueOf(role.getId())) != null) {
					sb.append(";");
					sb.append(String.valueOf(role.getId()));
				}
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(0);
			}
			chkRoles.setSelectedKey(sb.toString());
		}
	}

	/**
	 * 
	 */
	private void performSave() {

		String logonName = txtLogonName.getText().trim();

		IUserDAO userDAO = DAOSystem.getDAO(IUserDAO.class);

		IUser user = model.loadUser();

		if (user == null) {
			user = userDAO.createEntity();
		}

		IMitarbeiter mit = model.getEmployee();

		user.setLogonName(logonName);
		user.setName(mit.getVorname() + " " + mit.getNachname());

		IRoleDAO roleDAO = DAOSystem.getDAO(IRoleDAO.class);
		List<?> roles = roleDAO.getEntities(null, new PropertyQuery());
		for (Iterator<?> it = roles.iterator(); it.hasNext();) {
			IRole role = (IRole) it.next();

			//if the user didn't have that role but was checked, add it
			if (chkRoles.isKeySelected(String.valueOf(role.getId())) && !user.getRoles().contains(role)) {
				user.getRoles().add(role);
			}
			//if the user had the role but was unchecked, remove it
			if (user.getRoles().contains(role) && chkRoles.getContentProvider().getObjectFromKey(String.valueOf(role.getId())) != null
					&& !chkRoles.isKeySelected(String.valueOf(role.getId()))) {
				user.getRoles().remove(role);
			}
		}

		String selectedKey = "en";
		if (null != selectedKey && selectedKey.length() > 0) {
			user.setLanguage(selectedKey);
		}

		try {
			userDAO.update(user);
		} catch (Exception ex) {
			ValidationException valExc = new ValidationException();
			valExc.addError(user.getLogonName(), "There is already a user registered with the logon name you entered.");
			model.addException(valExc);
		}

		boolean mitUpdate = false;
		String existingLoginName = mit.getLogonName();
		mitUpdate = existingLoginName == null || !existingLoginName.equals(logonName);

		if (mitUpdate) {
			mit.setLogonName(logonName);
			SandboxModelConfig.getMitarbeiterDAO().update(mit);
		}

		ISecurityManager sman = DAOSystem.getSecurityManager();
		IUser usr = sman.findUser(logonName);
		if (usr != null) {
			sman.dropUserFromCache(usr);
			sman.dropCredentialFromCache(usr);
		}
	}

	/**
	 * 
	 */
	private void performValidation() {
		// validate first...
		String logonText = txtLogonName.getText();
		ValidationException valExc = model.getValidationException();

		if (null == logonText || logonText.length() == 0) {
			valExc.addError(txtLogonName.getName(), "Logon name must be set.");
		}

		String logonName = logonText.trim();
		if (logonName.length() == 0) {
			valExc.addError(txtLogonName.getName(), "Logon name must be set.");
		}

		if (chkRoles.getSelectedKeys().length > 0 && !defaultRoleSelected()) {
			valExc.addError(chkRoles.getName(), "The default role must be assigned in order to assign any other roles");
		}

		model.addException(valExc);
	}

	/**
	 * @return
	 */
	private boolean defaultRoleSelected() {
		String[] selectedKeys = chkRoles.getSelectedKeys();
		Map<String, ISelectElement> elementsMap = chkRoles.buildElementsMap();
		for (String key : selectedKeys) {
			String title = elementsMap.get(key).getTitle();
			if (DEFAULT_ROLE.equalsIgnoreCase(title)) {
				return true;
			}
		}
		return false;
	}
}
