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
package de.xwic.sandbox.basegui.selectors.people;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.IControlContainer;
import de.jwic.base.SessionContext;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.toolkit.model.IModelListener;
import de.xwic.sandbox.basegui.entityselection.EntitySelectionModel;
import de.xwic.sandbox.basegui.entityselection.IEntitySelectionContributor;
import de.xwic.sandbox.basegui.entityview.EntityDisplayListModel;
import de.xwic.sandbox.basegui.util.IQuickSearchPanel;

/**
 * @author Raluca Geogia
 *
 */
public class PeopleComboFactory {

	private SessionContext context;
	private PropertyQuery query;

	/**
	 * @param baseQuery
	 */
	public PeopleComboFactory(PropertyQuery baseQuery) {
		this.query = baseQuery;
	}

	/**
	 * @param container
	 * @param name
	 * @param lifeSearch
	 * @return
	 */
	public PeopleComboSelector generateCombo(IControlContainer container, String name, boolean lifeSearch) {
		return generateCombo(container, name, (IModelListener) null, false, lifeSearch);
	}

	/**
	 * @param container
	 * @param name
	 * @param selectionModelListener
	 * @return
	 */
	public PeopleComboSelector generateCombo(IControlContainer container, String name, IModelListener selectionModelListener) {
		return generateCombo(container, name, selectionModelListener, false, true);
	}

	/**
	 * @param container
	 * @param name
	 * @param selectionModelListener
	 * @param lifeSearch
	 * @return
	 */
	public PeopleComboSelector generateCombo(IControlContainer container, String name, IModelListener selectionModelListener,
			boolean lifeSearch) {
		return generateCombo(container, name, selectionModelListener, false, lifeSearch);
	}

	/**
	 * @param container
	 * @param name
	 * @param selectionModelListener
	 * @param areaEmployeesOnly
	 * @param lifeSearch
	 * @return
	 */
	public PeopleComboSelector generateCombo(IControlContainer container, String name, IModelListener selectionModelListener,
			boolean areaEmployeesOnly, boolean lifeSearch) {
		this.context = container.getSessionContext();
		return new PeopleComboSelector(container, name, createDefaultContributor(selectionModelListener, areaEmployeesOnly),
				lifeSearch);
	}

	/**
	 * @param selectionModelListener
	 * @param areaEmployeesOnly
	 * @return
	 */
	private IEntitySelectionContributor createDefaultContributor(IModelListener selectionModelListener, boolean areaEmployeesOnly) {

		final EntitySelectionModel selectionModel = new EntitySelectionModel();
		List<String> queryProperties = new ArrayList<String>();
		queryProperties.add("vorname");
		queryProperties.add("nachname");
		selectionModel.setQueryProperties(queryProperties);

		if (null != selectionModelListener) {
			selectionModel.addModelListener(selectionModelListener);
		}

		if (null == query) {
			query = new PropertyQuery();
		}

		query.setSortField("nachname");
		final EntityDisplayListModel listModel = new EntityDisplayListModel(query, IMitarbeiter.class);

		IEntitySelectionContributor contributor = new IEntitySelectionContributor() {

			@Override
			public IQuickSearchPanel createQuickSearchPanel() {
				return new PeopleQuickSearchPanel(listModel);
			}

			@Override
			public EntityDisplayListModel getListModel() {
				return listModel;
			}

			@Override
			public String getListSetupId() {
				return null;
			}

			@Override
			public String getPageSubTitle() {
				return "Employee Selection";
			}

			@Override
			public String getPageTitle() {
				return "Select an Employee from the list below";
			}

			@Override
			public EntitySelectionModel getSelectionModel() {
				return selectionModel;
			}

			@Override
			public String getViewTitle() {
				return "";
			}

		};

		return contributor;
	}
}
