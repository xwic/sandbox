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
/**
 * 
 */
package de.xwic.sandbox.basegui.util;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.toolkit.components.ListModel;


/**
 * @author Raluca Geogia
 *
 */
public abstract class AbstractQuickSearchPanel implements IQuickSearchPanel {

	protected ListModel model;

	public AbstractQuickSearchPanel(ListModel model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.util.IEntityQueryGenerator#generateEntityQuery(de.xwic.appkit.core.dao.EntityQuery)
	 */
	@Override
	public EntityQuery generateEntityQuery(EntityQuery originalQuery) {
		if (null == originalQuery) {
			originalQuery = new PropertyQuery();
		}

		EntityQuery qsQuery = generateQuickSearchQuery();

		if (originalQuery instanceof PropertyQuery && qsQuery instanceof PropertyQuery) {
			PropertyQuery originalPropQuery = (PropertyQuery) originalQuery;
			PropertyQuery qsPropQuery = (PropertyQuery) qsQuery;

			PropertyQuery resultQuery = new PropertyQuery();

			if (!originalPropQuery.getElements().isEmpty()) {
				resultQuery.addQueryElement(new QueryElement(QueryElement.AND, originalPropQuery));
			}

			if (!qsPropQuery.getElements().isEmpty()) {
				resultQuery.addQueryElement(new QueryElement(QueryElement.AND, qsPropQuery));
			}

			EntityQuery modelQuery = model.getQuery();
			if (null != modelQuery.getSortField()) {
				resultQuery.setSortField(modelQuery.getSortField());
				resultQuery.setSortDirection(modelQuery.getSortDirection());
			}

			return resultQuery;
		}

		return qsQuery;
	}

	/**
	 * @return an EntityQuery implementation for the quick search panel entries
	 */
	public abstract EntityQuery generateQuickSearchQuery();
}
