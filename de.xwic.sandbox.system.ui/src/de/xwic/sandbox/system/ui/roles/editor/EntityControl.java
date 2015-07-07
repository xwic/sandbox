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
package de.xwic.sandbox.system.ui.roles.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.system.model.roles.editor.model.EntityControlId;
import de.xwic.system.model.roles.editor.model.RolesEditorModel;

/**
 * @author Claudiu Mateias
 */
public class EntityControl extends ControlContainer {

	private RolesEditorModel model;

	private InputBox ibName = null;

	private SortedMap<String, List<EntityControlId>> packageMap = new TreeMap<String, List<EntityControlId>>();

	/**
	 * 
	 * @param container
	 * @param name
	 * @param model
	 * @param scopesMap
	 */
	public EntityControl(IControlContainer container, String name, RolesEditorModel model) {
		super(container, name);

		this.model = model;
		Setup setup = ConfigurationManager.getSetup();

		int idx = 0;
		String langId = getSessionContext().getLocale().getLanguage();

		for (Model entitymodel : setup.getModels()) {

			for (String id : entitymodel.getManagedEntities()) {

				IScope scope = model.getScopesMap().get(id);

				if (scope != null) {

					try {
						EntityDescriptor entityDescriptor = entitymodel.getEntityDescriptor(id);
						Bundle bundle = entityDescriptor.getDomain().getBundle(langId);
						String text = bundle.getString(id);
						String packageName = id.substring(0, id.lastIndexOf("."));

						// create the entity group by package if it doesn't exists

						List<EntityControlId> list = null;
						if (packageMap.containsKey(packageName)) {
							list = packageMap.get(packageName);
						} else {
							list = new ArrayList<EntityControlId>();
							packageMap.put(packageName, list);
						}

						EntityControlId controlId = createControlId(scope, idx, text, id);
						idx += 2;

						list.add(controlId);

					} catch (ConfigurationException e) {
						throw new RuntimeException(e);
					}
				}

				model.getScopesMap().remove(id);
			}
		}

		new Label(this, "lblName").setText("Name");

		ibName = new InputBox(this, "ibName");
		ibName.setWidth(300);
		ibName.setText(model.getRole().getName());
	}

	/**
	 * @param scope
	 * @param idx
	 * @param text
	 * @param id
	 * @return the controlId
	 */
	private EntityControlId createControlId(IScope scope, int idx, String text, String id) {

		final CheckBox chkRead = new CheckBox(this, "chkRead" + (idx + 1));
		chkRead.setChangeNotification(true);

		final CheckBox chkUpdate = new CheckBox(this, "chkUpdate" + (idx + 1));
		chkUpdate.setChangeNotification(true);

		final CheckBox chkCreate = new CheckBox(this, "chkCreate" + (idx + 1));
		chkCreate.setChangeNotification(true);

		final CheckBox chkDelete = new CheckBox(this, "chkDelete" + (idx + 1));
		chkDelete.setChangeNotification(true);

		// label for the class name of the entity
		new Label(this, "lbl" + idx).setText(id.substring(id.lastIndexOf(".") + 1, id.length()));
		// label for the entity name
		new Label(this, "lbl" + (idx + 1)).setText(text);

		final EntityControlId controlId = new EntityControlId();
		controlId.setScopeId(scope.getName());
		controlId.setLblId("lbl" + (idx + 1));
		controlId.setClassName("lbl" + (idx));
		controlId.setChkReadId("chkRead" + (idx + 1));
		controlId.setChkUpdateId("chkUpdate" + (idx + 1));
		controlId.setChkDeleteId("chkDelete" + (idx + 1));
		controlId.setChkCreateId("chkCreate" + (idx + 1));

		chkRead.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setRead(chkRead.isChecked());
			}
		});

		chkCreate.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setCreate(chkCreate.isChecked());
			}
		});

		chkUpdate.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setUpdate(chkUpdate.isChecked());
			}
		});

		chkDelete.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setDelete(chkDelete.isChecked());
			}
		});

		PropertyQuery query = new PropertyQuery();
		query.addEquals("role", model.getRole());
		query.addEquals("scope", scope);

		IRightDAO rightDAO = (IRightDAO) DAOSystem.getDAO(IRightDAO.class);
		EntityList list = rightDAO.getEntities(null, query);

		for (Object obj : list) {
			IRight right = (IRight) obj;
			IAction action = right.getAction();

			if (null != action) {

				String actionName = action.getName();
				if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_READ)) {
					chkRead.setChecked(true);
				} else if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_CREATE)) {
					chkCreate.setChecked(true);
				} else if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_UPDATE)) {
					chkUpdate.setChecked(true);
				} else if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_DELETE)) {
					chkDelete.setChecked(true);
				}
			}
		}

		return controlId;
	}

	/**
	 * @return the entControlIds
	 */
	public List<EntityControlId> getEntityControlIds() {
		List<EntityControlId> result = new ArrayList<EntityControlId>();

		for (List<EntityControlId> list : packageMap.values()) {
			result.addAll(list);
		}

		return result;
	}

	/**
	 * 
	 * @return the packageMap
	 */
	public Map<String, List<EntityControlId>> getPackageMap() {
		return packageMap;
	}

	/**
	 * 
	 * @return the text from the input box ibName
	 */
	public String getRoleName() {
		return ibName.getText();
	}
}
