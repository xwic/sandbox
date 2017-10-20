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

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.components.BaseView;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.system.model.roles.editor.model.OtherControlId;
import de.xwic.system.model.roles.editor.model.RolesEditorModel;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class RolesEditorView extends BaseView {

	private RolesEditorModel model = null;
	private EntityControl entityControl;
	private List<ModuleControl> moduleControls;

	private Button btSave = null;
	private Button btClose = null;

	public RolesEditorView(IControlContainer container, String name, RolesEditorModel model) {
		super(container, name);

		this.model = model;

		setupActionBar();
		createTabStrip();
	}

	/**
	 * Setup the action bar
	 */
	private void setupActionBar() {
		ToolBarGroup tg = toolbar.addGroup();

		btSave = tg.addButton();
		btSave.setTitle("Save");
		btSave.setIconEnabled(ImageLibrary.IMAGE_SAVE_ACTIVE_SMALL);
		btSave.setIconDisabled(ImageLibrary.IMAGE_SAVE_INACTIVE_SMALL);
		btSave.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				performSave();
			}
		});

		btClose = tg.addButton();
		btClose.setTitle("Close");
		btClose.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btClose.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);
		btClose.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				performClose();
			}
		});
	}

	private void createTabStrip() {
		TabStrip tabStrip = new TabStrip(this, "tabs");

		Tab tab = tabStrip.addTab("Entities", "entity");
		entityControl = new EntityControl(tab, "EntityControl", model);

		moduleControls = new ArrayList<ModuleControl>();

		tab = tabStrip.addTab("Get Started", "getStarted");
		moduleControls.add(new ModuleControl(tab, "GetStartedControl", model));
		
		tab = tabStrip.addTab("Demo Application", "demoApplication");
		moduleControls.add(new ModuleControl(tab, "DemoApplicationControl", model));

		tab = tabStrip.addTab("Resources", "resources");
		moduleControls.add(new ModuleControl(tab, "ResourcesControl", model));
		
		tab = tabStrip.addTab("System", "system");
		moduleControls.add(new ModuleControl(tab, "SystemControl", model));

		tab = tabStrip.addTab("Others", "others");
		moduleControls.add(new ModuleControl(tab, "OthersControl", model));
	}

	/**
	 * 
	 */
	private void performSave() {
		try {

			String name = entityControl.getRoleName();

			if (name == null || name.trim().isEmpty()) {
				showError("Please enter a name!");
				return;
			}

			model.setName(name);

			// save the entity scopes
			model.saveEntityList(entityControl.getEntityControlIds());

			// save the non-entity scopes
			List<OtherControlId> otherControlIds = new ArrayList<OtherControlId>();
			for (ModuleControl mc : moduleControls) {
				otherControlIds.addAll(mc.getOtherControlsIds());
			}
			model.saveOtherList(otherControlIds);

			model.close();

		} catch (Exception re) {
			showError(re);
		}
	}

	/**
	 * closes the editor
	 */
	protected void performClose() {
		try {
			model.close();
		} catch (Exception re) {
			showError(re);
		}
	}
}
