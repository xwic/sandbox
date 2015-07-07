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
package de.xwic.sandbox.resources.ui.people;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.util.ConfigurationUtil;
import de.xwic.sandbox.util.ui.SandboxImageLibrary;

/**
 * @author Dogot Nicu
 *
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class PeopleView extends EntityListView {

	/**
	 * @param container
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public PeopleView(IControlContainer container, EntityListViewConfiguration configuration) throws ConfigurationException {
		super(container, configuration);
		
		createAdditionalButtons();
	}
	
	/**
	 * 
	 */
	private void createAdditionalButtons() {
		ToolBarGroup tg = toolbar.addGroup();
		tg.addSpacer();
		
		if (ConfigurationUtil.hasAccess(SandboxModelConfig.MOD_SYSTEM)) {
			Button btnUpdateEmployees = tg.addButton();
			btnUpdateEmployees.setTitle("Synchronize with CED");
			btnUpdateEmployees.setTooltip("Read employee data from CED and update in Pulse");
			btnUpdateEmployees.setIconEnabled(SandboxImageLibrary.IMAGE_REFRESH_SMALL);
			btnUpdateEmployees.addSelectionListener(new SelectionListener() {
				
				@Override
				public void objectSelected(SelectionEvent event) {

				}
			});
		}
	}
}
