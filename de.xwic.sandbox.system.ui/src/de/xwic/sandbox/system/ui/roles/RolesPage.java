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
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.sandbox.system.model.roles.model.RolesModel;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class RolesPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public RolesPage(IControlContainer container, String name) {
		super(container, name);

		setTitle("Roles");
		setSubtitle("View/Edit the roles in the system");

		PropertyQuery baseQuery = new PropertyQuery();

		PropertyQuery defaultQuery = new PropertyQuery();
		defaultQuery.setSortField("name");
		defaultQuery.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);

		EntityListViewConfiguration config = new EntityListViewConfiguration(IRole.class);
		config.setBaseFilter(baseQuery);
		config.setDefaultFilter(defaultQuery);
		config.setViewId("system_roles");

		RolesModel model = new RolesModel();

		try {
			new RolesView(this, config, model);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}
}
