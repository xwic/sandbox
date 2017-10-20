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
package de.xwic.sandbox.basegui.entityselection;

import java.util.Collection;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.sandbox.basegui.entityview.EntityDisplayView;
import de.xwic.sandbox.basegui.util.IEditorCreator;
import de.xwic.sandbox.basegui.util.IQuickSearchPanel;


/**
 * @author Raluca Geogia
 *
 */
public class EntitySelectionView extends EntityDisplayView {

	private Button btSelect;
	private Button btAbort;

	private EntitySelectionModel model;

	private IEntitySelectionContributor contributor;

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param selectionModel
	 * @param contributor
	 * @param listSetupID
	 */
	public EntitySelectionView(IControlContainer container, String name, IEntitySelectionContributor contributor) {
		super(container, name, contributor.getListModel(), contributor.getViewTitle(), contributor.getListSetupId(), null, contributor
				.createQuickSearchPanel());
		this.contributor = contributor;
		setTemplateName(EntityDisplayView.class.getName());
		this.model = contributor.getSelectionModel();

		tableViewer.setHeightDecrease(300);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.entityview.EntityDisplayView#setupActionBar()
	 */
	@Override
	protected void setupActionBar() {
		ToolBarGroup tg = toolbar.addGroup();

		btSelect = tg.addButton();
		btSelect.setTitle("Select");
		btSelect.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performSelect();
			}

		});
		btSelect.setIconEnabled(ImageLibrary.IMAGE_TICK_ACTIVE);
		btSelect.setIconDisabled(ImageLibrary.IMAGE_TICK_INACTIVE);

		btAbort = tg.addButton();
		btAbort.setTitle("Cancel");
		btAbort.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performAbort();
			}

		});
		btAbort.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btAbort.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);
	}

	private void performSelect() {
		Collection<?> sel = tableViewer.getTableViewer().getModel().getSelection();
		if (!sel.isEmpty()) {
			String key = (String) sel.iterator().next();
			int id = Integer.parseInt(key);

			model.setSelectedEntityId(id);
		}

		model.closeEntitySelection();
	}

	private void performAbort() {
		//		model.setSelectedEntityId(0);
		model.closeEntitySelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.entityview.EntityDisplayView#getEditorCreator()
	 */
	@Override
	protected IEditorCreator getEditorCreator() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.entityview.EntityDisplayView#getViewTitle()
	 */
	@Override
	protected String getViewTitle() {
		return null != contributor ? contributor.getViewTitle() : "";
	}

	@Override
	protected IQuickSearchPanel createQuickSeachPanel() {
		return null != contributor ? contributor.createQuickSearchPanel() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.entityview.EntityDisplayView#init()
	 */
	@Override
	protected void init() {

	}

}
