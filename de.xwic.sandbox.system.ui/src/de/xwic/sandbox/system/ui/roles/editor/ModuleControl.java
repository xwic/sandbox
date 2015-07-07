package de.xwic.sandbox.system.ui.roles.editor;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.Label;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IAction;
import de.xwic.appkit.core.security.IRight;
import de.xwic.appkit.core.security.IScope;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.system.model.roles.editor.model.OtherControlId;
import de.xwic.system.model.roles.editor.model.RolesEditorModel;

/**
 * @author Claudiu Mateias
 */
public class ModuleControl extends ControlContainer {

	private RolesEditorModel model;

	// the controls for the scopes of the current module
	private List<OtherControlId> otherControlsIds = new ArrayList<OtherControlId>();

	private final Map<String, String> namePattern = new HashMap<String, String>() {

		{
			put("GetStartedControl", "MOD_GET_STARTED");
			put("ActionTrackerControl", "MOD_ACTION_TRACKER");
			put("ResourcesControl", "MOD_RESOURCES");
			put("SystemControl", "MOD_SYSTEM");
			put("OthersControl", "");
		}
	};

	/**
	 * 
	 * @param container
	 * @param name
	 * @param model
	 * @param otherScopes
	 */
	public ModuleControl(IControlContainer container, String name, RolesEditorModel model) {
		super(container, name);

		this.model = model;

		List<String> nonEntityScopes = model.getNonEntityScopes();

		String prefix = namePattern.get(name);

		SortedMap<String, IScope> localScopes = new TreeMap<String, IScope>();

		for (IScope scope : model.getScopesMap().values()) {
			if (scope.getName().contains(prefix) && nonEntityScopes.contains(scope.getName())) {
				localScopes.put(scope.getName(), scope);
			} else if ("OthersControl".equals(name) && nonEntityScopes.contains(scope.getName())) {
				localScopes.put(scope.getName(), scope);
			}
		}

		int idx = 0;
		// create the controls in sorted order
		for (IScope scope : localScopes.values()) {
			// remove the scopes processed now because we don't need them anymore
			model.getScopesMap().remove(scope.getName());
			// create the controls for these scopes 
			OtherControlId control = createOtherControlId(scope, idx++, scope.getName());
			otherControlsIds.add(control);
		}
	}

	/**
	 * @param scope
	 * @param idx
	 * @param text
	 * @return
	 */
	private OtherControlId createOtherControlId(IScope scope, int idx, String text) {
		new Label(this, "lbl" + idx).setText(text);

		final CheckBox chkExecute = new CheckBox(this, "chkExecute" + idx);
		chkExecute.setChangeNotification(true);

		final CheckBox chkAccess = new CheckBox(this, "chkAccess" + idx);
		chkAccess.setChangeNotification(true);

		final OtherControlId controlId = new OtherControlId();
		controlId.setScopeId(scope.getName());
		controlId.setLblId("lbl" + idx);
		controlId.setChkExecuteId("chkExecute" + idx);
		controlId.setChkAccessId("chkAccess" + idx);

		chkAccess.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setAccess(chkAccess.isChecked());
			}
		});

		chkExecute.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				controlId.setExecute(chkExecute.isChecked());
			}
		});

		PropertyQuery query = new PropertyQuery();
		query.addEquals("role", model.getRole());
		query.addEquals("scope", scope);

		IRightDAO rightDAO = DAOSystem.getDAO(IRightDAO.class);
		EntityList list = rightDAO.getEntities(null, query);

		for (Object obj : list) {
			IRight right = (IRight) obj;
			IAction action = right.getAction();

			if (null != action) {
				String actionName = action.getName();
				if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_APPROVE)) {
					chkExecute.setChecked(true);
				} else if (actionName.equalsIgnoreCase(ApplicationData.SECURITY_ACTION_ACCESS)) {
					chkAccess.setChecked(true);
				}
			}
		}

		return controlId;
	}

	/**
	 * @return the otherControlsIds
	 */
	public List<OtherControlId> getOtherControlsIds() {
		return otherControlsIds;
	}
}
