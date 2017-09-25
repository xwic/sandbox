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

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.combo.Combo;
import de.jwic.controls.combo.ComboBehavior;
import de.jwic.controls.combo.LifeSearchCombo;
import de.jwic.data.DataLabel;
import de.jwic.data.IBaseLabelProvider;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.model.IModelListener;
import de.xwic.appkit.webbase.toolkit.model.ModelEvent;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.sandbox.basegui.entityview.EntityDisplayListModel;
import de.xwic.sandbox.basegui.util.IPageListener;
import de.xwic.sandbox.basegui.util.IPageListener.PageEvent;


/**
 * @author Raluca Geogia
 *
 */
public class EntityComboSelector<E extends IEntity> extends ControlContainer implements IModelListener {

	protected Combo<E> combo;
	protected List<IPageListener> listeners = new ArrayList<IPageListener>();

	private EntitySelectionModel selectionModel;
	private IEntitySelectionContributor contributor;

	protected Button btnSelect;

	private boolean lifeSearch = true;

	private int width = 400;

	/**
	 * @param container
	 * @param name
	 * @param contributor
	 */
	public EntityComboSelector(IControlContainer container, String name, IEntitySelectionContributor contributor) {
		super(container, name);

		this.contributor = contributor;
		this.selectionModel = contributor.getSelectionModel();

		selectionModel.addModelListener(this);
		
		createControls();
	}

	/**
	 * @param container
	 * @param name
	 * @param contributor
	 * @param lifeSearch
	 */
	public EntityComboSelector(IControlContainer container, String name, IEntitySelectionContributor contributor, boolean lifeSearch) {
		super(container, name);
		this.contributor = contributor;
		this.selectionModel = contributor.getSelectionModel();

		selectionModel.addModelListener(this);

		this.lifeSearch = lifeSearch;
		createControls();
	}

	protected void createControls() {

		final DAO dao = contributor.getListModel().getDAO();

		if (lifeSearch) {
			combo = new LifeSearchCombo<E>(this, "comboEntity" + this.getName());
			((LifeSearchCombo<E>) combo).setAutoPickFirstHit(false);
			combo.getComboBehavior().setMaxFetchRows(40);

		} else {
			combo = new Combo<E>(this, "comboEntity" + this.getName());
			ComboBehavior comboBehavior = combo.getComboBehavior();
			comboBehavior.setOpenContentOnTextboxFocus(true);
			comboBehavior.setSelectTextOnFocus(true);
			comboBehavior.setTextEditable(true);
		}

		combo.setContentProvider(new EntityComboContentProvider<E>(contributor));
		combo.setChangeNotification(true);
		combo.setWidth(width - 38);
		combo.addElementSelectedListener(new ElementSelectedListener() {

			@Override
			public void elementSelected(ElementSelectedEvent event) {
				Object entity = combo.getSelectedElement();
				if (null != entity && entity instanceof IEntity) {
					selectionModel.setSelectedEntityId(((IEntity) entity).getId());
					selectionModel.entityChanged();
				} else {
					selectionModel.setSelectedEntityId(0);
					selectionModel.entityChanged();
				}
			}
		});

		combo.setBaseLabelProvider(new IBaseLabelProvider<E>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see de.jwic.data.IBaseLabelProvider#getBaseLabel(java.lang.Object)
			 */
			@Override
			public DataLabel getBaseLabel(E object) {
				return new DataLabel(dao.buildTitle(object));
			}
		});

		btnSelect = new Button(this, "btnSelect" + this.getName());
		btnSelect.setTitle("");
		btnSelect.setIconEnabled(ImageLibrary.ICON_LENS_ACTIVE);
		btnSelect.setTooltip("Detailed Search...");
		btnSelect.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				onSelect();
			}

		});
	}
	public void setQuery(EntityQuery query) {
		contributor.getListModel().setQuery(query);
		combo.requireRedraw();
	}

	/**
	 * 
	 */
	public void clearSelection() {
		combo.setSelectedElement(null);
	}
	
	/**
	 * @param event
	 */
	private void firePageEvent(PageEvent event) {
		for (IPageListener listener : listeners) {
			listener.pageStateChanged(event);
		}
	}
	
	private void onSelect() {
		firePageEvent(PageEvent.OPENED);

		Site site = ExtendedApplication.getInstance(this).getSite();
		// here is a bit tricky. we need to reset the query to the model,
		// because this model is created
		// before the creation of the control...
		EntityDisplayListModel listModel = contributor.getListModel();
		EntityQuery query = listModel.getOriginalQuery();
		listModel.setQuery(query);
		EntitySelectionPage contactPage = new EntitySelectionPage(site.getContentContainer(), "entitySelectionPage" + this.getName(),
				contributor);

		site.pushPage(contactPage);
	}

	public void setEntity(E entity) {
		if (null != entity) {
			combo.setSelectedElement(entity);
			//combo.setText(DAOSystem.findDAOforEntity(entity.type()).buildTitle(entity));
			selectionModel.setSelectedEntityId(entity.getId());
			selectionModel.entityChanged();
		}
	}

	public void setText(String text) {
		combo.setText(text);
	}

	public long getSelectedEntityId() {
		return contributor.getSelectionModel().getSelectedEntityId();
	}

	@SuppressWarnings("unchecked")
	public E getSelectedEntity() {
		long selectedId = getSelectedEntityId();
		DAO dao = contributor.getListModel().getDAO();
		return (E) dao.getEntity(selectedId);
	}

	@Override
	public void modelContentChanged(ModelEvent event) {
		if (event.getEventType() == EntitySelectionModel.CLOSE_ENTITY_SELECTION) {
			EntitySelectionModel customerModel = (EntitySelectionModel) event.getSource();
			long selectedId = customerModel.getSelectedEntityId();

			// see if something was selected
			if (selectedId > 0) {
				E entity = getSelectedEntity();
				setEntity(entity);
			}
		}
		firePageEvent(PageEvent.CLOSED);
	}

	public String getEntityInfoName() {
		return "comboEntity" + this.getName();
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

	public void setWidth(int width) {
		combo.setWidth(width - 38);
		this.width = width;
	}

	public String getText() {
		return combo.getText();
	}

	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
		btnSelect.setEnabled(enabled);
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param text
	 */
	public void setEmptyInfoText(String text) {
		combo.setEmptyInfoText(text);
	}

	/**
	 * @param isVisible
	 */
	public void setSelectButtonVisible(boolean isVisible) {
		btnSelect.setVisible(isVisible);
	}

	/**
	 * @param listener
	 */
	public void addPageListener(IPageListener listener) {
		listeners.add(listener);
	}

	/**
	 * @param listener
	 * @see de.xwic.appkit.webbase.toolkit.model.ModelEventSupport#addModelListener(de.xwic.appkit.webbase.toolkit.model.IModelListener)
	 */
	public void addModelListener(IModelListener listener) {
		selectionModel.addModelListener(listener);
	}

	/**
	 * @param listener
	 * @see de.xwic.appkit.webbase.toolkit.model.ModelEventSupport#removeModelListener(de.xwic.appkit.webbase.toolkit.model.IModelListener)
	 */
	public void removeModelListener(IModelListener listener) {
		selectionModel.removeModelListener(listener);
	}
}
