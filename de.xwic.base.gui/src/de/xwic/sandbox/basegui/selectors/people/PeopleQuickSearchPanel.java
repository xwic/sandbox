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

import java.util.Iterator;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.events.KeyEvent;
import de.jwic.events.KeyListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.toolkit.components.ListModel;
import de.xwic.sandbox.basegui.entityview.EntityDisplayListModel;
import de.xwic.sandbox.basegui.util.AbstractQuickSearchPanel;

/**
 * @author Raluca Geogia
 *
 */
public class PeopleQuickSearchPanel extends AbstractQuickSearchPanel {

	private InputBox ibName;

	public PeopleQuickSearchPanel(ListModel model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.util.IQuickSearchPanel#createQuickSearchPanel(de.jwic.base.IControlContainer,
	 * de.xwic.sandbox.basegui.entityview.EntityDisplayListModel)
	 */
	@Override
	public Control createQuickSearchPanel(IControlContainer parent, final EntityDisplayListModel model) {
		ControlContainer panel = new ControlContainer(parent, "peopleQSpanel");
		panel.setTemplateName(getClass().getName());

		EntityQuery originalQuery = model.getOriginalQuery();

		ibName = new InputBox(panel, "ibNachname");
		ibName.setWidth(300);
		ibName.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent event) {
				model.setQuery(generateEntityQuery(model.getOriginalQuery()));
			}

		});
		// listen to ENTER
		ibName.setListenKeyCode(13);

		PropertyQuery query = new PropertyQuery();
		query.setSortField("title");

		Button btnSearch = new Button(panel, "btnsearch");
		btnSearch.setTitle("Search");
		btnSearch.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				model.setQuery(generateEntityQuery(model.getOriginalQuery()));
			}

		});

		// prefill fields
		if (originalQuery != null && originalQuery instanceof PropertyQuery) {
			PropertyQuery propQuery = (PropertyQuery) originalQuery;
			PropertyQuery baseQuery = propQuery.cloneQuery();
			for (Iterator<QueryElement> it = propQuery.getElements().iterator(); it.hasNext();) {
				QueryElement qe = it.next();
				if (qe.getPropertyName() != null && qe.getValue() != null) {
					// remove this element, as it will get set by the quickfilter.
					it.remove();
					model.setQuery(baseQuery);
					break;
				}

			}

		}

		return panel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.util.AbstractQuickSearchPanel#generateQuickSearchQuery()
	 */
	@Override
	public EntityQuery generateQuickSearchQuery() {
		PropertyQuery query = new PropertyQuery();

		String text = ibName.getText();
		if (null != text && text.trim().length() > 0) {
			if (!text.endsWith("%")) {
				text = text + "%";
			}

			query.addLike("nachname", text);
		}

		return query;
	}

}
