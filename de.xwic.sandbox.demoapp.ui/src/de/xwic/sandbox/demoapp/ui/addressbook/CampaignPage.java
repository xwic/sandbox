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
package de.xwic.sandbox.demoapp.ui.addressbook;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.sandbox.demoapp.model.entities.ICampaign;

/**
 * @author WebEnd
 *
 */
@SuppressWarnings("serial")
public class CampaignPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public CampaignPage(IControlContainer container, String name) {
		super(container, name);

		setTitle("Campaigns");
		setSubtitle("Manage campaigns you know/do business with.");

		PropertyQuery baseQuery = new PropertyQuery();

		PropertyQuery defaultQuery = new PropertyQuery();
		defaultQuery.setSortField("name");
		defaultQuery.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		
		
		EntityListViewConfiguration config = new EntityListViewConfiguration(ICampaign.class);
		config.setBaseFilter(baseQuery);
		config.setDefaultFilter(defaultQuery);
		
		try {
			new EntityListView<ICampaign>(this,config);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}

}
