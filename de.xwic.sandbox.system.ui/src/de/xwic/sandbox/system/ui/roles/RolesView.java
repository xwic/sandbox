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
package de.xwic.sandbox.system.ui.roles;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.sandbox.system.model.roles.model.RolesModel;

/**
 * @author Dogot Nicu
 *
 */
public class RolesView extends EntityListView {

	private RolesModel model;

	/**
	 * @param container
	 * @param configuration
	 * @param model
	 * @throws ConfigurationException
	 */
	public RolesView(IControlContainer container, EntityListViewConfiguration configuration, RolesModel model)
			throws ConfigurationException {
		super(container, configuration);
		this.model = model;
	}
}
