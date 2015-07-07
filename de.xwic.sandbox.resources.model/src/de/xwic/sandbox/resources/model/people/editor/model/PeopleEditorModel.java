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
package de.xwic.sandbox.resources.model.people.editor.model;

import java.util.Iterator;
import java.util.List;

import de.jwic.controls.wizard.ValidationException;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.security.queries.UserQuery;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.StringUtil;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class PeopleEditorModel extends EditorModel {

	public static final int EMPLOYEE_SAVED = 10;
	private IMitarbeiter employee;
	private IMitarbeiterDAO maDao = SandboxModelConfig.getMitarbeiterDAO();
	private ValidationException valExc = new ValidationException();

	/**
	 * @param entity
	 */
	public PeopleEditorModel(IEntity entity) {
		super(entity, true);

		employee = (IMitarbeiter) entity;
	}

	/**
	 * @return
	 */
	public IMitarbeiter getEmployee() {
		return employee;
	}

	/**
	 * @return
	 */
	public IUser loadUser() {

		// check if a user exists.
		if (null != employee && !StringUtil.isEmpty(employee.getLogonName())) {
			IUserDAO userDAO = (IUserDAO) DAOSystem.getDAO(IUserDAO.class);
			List<IUser> list = userDAO.getEntities(null, new UserQuery(employee.getLogonName()));
			if (!list.isEmpty()) {
				return (IUser) list.get(0);
			}
		}

		return null;
	}

	/**
	 * 
	 */
	public void refreshEmployee() {
		if (employee.getId() > 0) {
			employee = (IMitarbeiter) maDao.getEntity(employee.getId());
		}
	}

	/**
	 * 
	 */
	public void clearExceptions() {
		valExc = new ValidationException();
	}

	/**
	 * @return
	 */
	public ValidationException getValidationException() {
		return valExc;
	}

	/**
	 * @throws ValidationException
	 */
	public void saveEntityAndGrades() throws ValidationException {
		maDao.update(employee);
		saveEntity();

		fireModelChangedEvent(new EditorModelEvent(EMPLOYEE_SAVED, this));
	}

	/**
	 * @param e
	 */
	public void addException(ValidationException e) {
		if (e != null && e.hasErrors()) {
			for (Iterator<?> it = e.getErrorFields().iterator(); it.hasNext();) {

				String errField = (String) it.next();
				this.valExc.addError(errField, e.getMessage(errField));
			}
		}
	}
}
