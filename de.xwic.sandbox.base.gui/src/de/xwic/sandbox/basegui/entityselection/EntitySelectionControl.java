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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.model.IModelListener;
import de.xwic.appkit.webbase.toolkit.model.ModelEvent;
import de.xwic.sandbox.basegui.entityview.EntityDisplayListModel;

/**
 * 
 * @author Raluca Geogia
 *
 */
public class EntitySelectionControl extends ControlContainer implements IModelListener {

	private IEntitySelectionContributor contributor;

	private InputBox ibEntityInfo;

	/**
	 * @param container
	 * @param name
	 */
	public EntitySelectionControl(IControlContainer container, String name, IEntitySelectionContributor contributor) {
		super(container, name);

		this.contributor = contributor;
		contributor.getSelectionModel().addModelListener(this);

		createControls();
	}

	private void createControls() {

		ibEntityInfo = new InputBox(this, "ibEntityInfo" + this.getName());
		ibEntityInfo.setReadonly(true);
		ibEntityInfo.setFillWidth(true);

		Button btnSelect = new Button(this, "btnSelect" + this.getName());
		btnSelect.setTitle("Select");
		btnSelect.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				onSelect();
			}

		});

		Button btnClear = new Button(this, "btnClear" + this.getName());
		btnClear.setTitle("Clear");
		btnClear.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				onClear();
			}

		});
	}

	private void onSelect() {
		Site site = ExtendedApplication.getInstance(this).getSite();
		// here is a bit tricky. we need to reset the query to the model, because this model is created
		// before the creation of the control...
		EntityDisplayListModel listModel = contributor.getListModel();
		EntityQuery query = listModel.getOriginalQuery();
		listModel.setQuery(query);
		EntitySelectionPage contactPage = new EntitySelectionPage(site.getContentContainer(), "entitySelectionPage"
				+ this.getName(), contributor);

		site.pushPage(contactPage);
	}

	private void onClear() {
		ibEntityInfo.setText("");
		contributor.getSelectionModel().setSelectedEntityId(0);
		contributor.getSelectionModel().fieldCleared();
	}

	public void setEntity(IEntity entity) {
		String text = "";
		if (null != entity) {
			DAO dao = DAOSystem.findDAOforEntity(entity.type());
			text = dao.buildTitle(entity);
			
			EntitySelectionModel selectionModel = contributor.getSelectionModel();
			
			selectionModel.setSelectedEntityId(entity.getId());
			selectionModel.entityChanged();
		}
		
		ibEntityInfo.setText(text);
	}

	public int getSelectedEntityId() {
		return contributor.getSelectionModel().getSelectedEntityId();
	}
	
	public IEntity getSelectedEntity() {
		int selectedId = getSelectedEntityId();
		DAO dao = contributor.getListModel().getDAO();
		return dao.getEntity(selectedId);
	}

	@Override
	public void modelContentChanged(ModelEvent event) {
		if (event.getEventType() == EntitySelectionModel.CLOSE_ENTITY_SELECTION) {
			EntitySelectionModel customerModel = (EntitySelectionModel) event.getSource();
			int selectedId = customerModel.getSelectedEntityId();

			// see if something was selected
			if (selectedId > 0) {
				IEntity entity = getSelectedEntity();
				DAO dao = DAOSystem.findDAOforEntity(entity.type());
				String text = dao.buildTitle(entity);
				ibEntityInfo.setText(text);
				customerModel.entityChanged();
			}
		}
	}
	
	public String getEntityInfoName() {
		return "ibEntityInfo" + this.getName();
	}

	public String getBtnSelectName() {
		return "btnSelect" + this.getName();
	}
	
	public String getBtnClearName() {
		return "btnClear" + this.getName();
	}
	
	public void resetQuery(EntityQuery query) {
		contributor.getListModel().setOriginalQuery(query);
	}
	
}