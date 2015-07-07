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
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
@SuppressWarnings("serial")
public class PeoplePage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public PeoplePage(IControlContainer container, String name) {
		super(container, name);

		setTitle("People");
		setSubtitle("People Management");

		PropertyQuery baseQuery = new PropertyQuery();

		PropertyQuery defaultQuery = new PropertyQuery();
		defaultQuery.setSortField("nachname");
		defaultQuery.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);

		EntityListViewConfiguration config = new EntityListViewConfiguration(IMitarbeiter.class);
		config.setBaseFilter(baseQuery);
		config.setDefaultFilter(defaultQuery);
		config.setViewId("people_all");

		try {
			new PeopleView(this, config);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}

}
