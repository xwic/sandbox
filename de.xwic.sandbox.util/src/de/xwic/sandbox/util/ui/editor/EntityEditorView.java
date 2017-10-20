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
package de.xwic.sandbox.util.ui.editor;

import org.hibernate.StaleObjectStateException;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;
import de.xwic.appkit.webbase.toolkit.components.BaseView;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;


public abstract class EntityEditorView extends BaseView implements IEditorModelListener {

	public static final String EDITOR_ACTION_CLOSE = "Close";
	public static final String EDITOR_ACTION_SAVE = "Save";
	public static final String PAGE_EDITOR_SAVE_DONE = "Entity was saved successfully";
	public static final String ATTRIBUTE_MISSING = "The field is mandatory";
	public static final String ATTRIBUTE_TOO_LONG = "The maximum length for this field is %s characters";
	public static final String ENTITY_EXISTS = "This entity already exists";

	protected EditorToolkit toolkit;
	protected EditorModel model;

	protected Button btSave = null;
	protected Button btClose = null;

	/**
	 * @param container
	 * @param name
	 */
	public EntityEditorView(IControlContainer container, String name, EditorModel model) {
		super(container, name);

		toolkit = new EditorToolkit(model);
		this.model = model;

		model.addModelListener(this);

		setupActionBar();
		createControls();

		btSave.setVisible(model.isEditable());

		toolkit.loadFieldValues();
	}

	/**
	 * Setup the action bar
	 */
	protected void setupActionBar() {
		ToolBarGroup tg = toolbar.addGroup();

		btSave = tg.addButton();
		btSave.setTitle(EDITOR_ACTION_SAVE);
		btSave.setIconEnabled(ImageLibrary.IMAGE_SAVE_ACTIVE_SMALL);
		btSave.setIconDisabled(ImageLibrary.IMAGE_SAVE_INACTIVE_SMALL);
		btSave.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performSave(isShowInfoAfterSave());
			}
		});

		btClose = tg.addButton();
		btClose.setTitle(EDITOR_ACTION_CLOSE);
		btClose.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btClose.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);
		btClose.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performClose();
			}
		});
	}

	/**
	 * creates the controls
	 */
	protected abstract void createControls();

	/**
	 * closes the editor
	 */
	protected void performClose() {
		try {
			model.close();
		} catch (Exception re) {
			showError(re);
		}
	}

	/**
	 * performs save
	 */
	protected void performSave(boolean showInfo) {
		try {
			model.validateEntity();

			model.saveEntity();
			if (showInfo) {
				showInfo(PAGE_EDITOR_SAVE_DONE);
			} else {
				// direct close here
				model.close();
			}
		} catch (StaleObjectStateException e) {
			processStaleObjectException(e);
		} catch (Exception re) {
			processException(re);

		}
	}

	/**
	 * @param re
	 */
	public void processException(Exception re) {
		if (re instanceof ValidationException) {
			StringBuffer msg = new StringBuffer();
			ValidationException valExc = (ValidationException) re;
			ValidationResult res = valExc.getResult();

			EntityDescriptor descriptor = null;
			Bundle entityBundle = null;
			try {
				descriptor = DAOSystem.getEntityDescriptor(model.getEntity().type().getName());
				entityBundle = descriptor.getDomain().getBundle(getSessionContext().getLocale().getLanguage());
			} catch (ConfigurationException e) {
				msg.append(e.getMessage());
				msg.append("<br>");
			}

			if (res != null && res.getErrorMap() != null) {
				for (String strProperty : res.getErrorMap().keySet()) {
					String message = res.getErrorMap().get(strProperty);
					String propertyName = entityBundle != null ? entityBundle.getString(strProperty) : strProperty;

					String bundleMsg = "";
					if (message.equalsIgnoreCase(ValidationResult.FIELD_REQUIRED)) {
						bundleMsg = ATTRIBUTE_MISSING;
					} else if ("ENTITY_EXISTS".equalsIgnoreCase(message)) {
						bundleMsg = ENTITY_EXISTS;
					} else if (message.equalsIgnoreCase(ValidationResult.FIELD_TOO_LONG)) {
						String maxLength = "n/a";
						if (descriptor != null) {
							Property prop = descriptor.getProperty(strProperty.substring(strProperty.lastIndexOf('.') + 1));
							maxLength = String.valueOf(prop.getMaxLength());
						}

						bundleMsg = String.format(ATTRIBUTE_TOO_LONG, maxLength);
					} else {
						bundleMsg = message;
					}

					msg.append(propertyName + ": " + bundleMsg);
					msg.append("<br>");
				}
			}

			showError(msg.toString());
		} else {
			if (null != re.getMessage()) {
				showError(re.getMessage());
			} else {
				showError(re);
			}
		}
	}

	/**
	 * This is the default process but this method should be overridden by the Views to process the StaleObjectException properly
	 * 
	 * @see FieldSupportRequestFormView
	 * @param e
	 */
	protected void processStaleObjectException(StaleObjectStateException e) {
		processException(e);
	}

	/**
	 * @return whether or not the save should be
	 */
	protected boolean isShowInfoAfterSave() {
		return true;
	}

	/**
	 * @return the toolkit
	 */
	public EditorToolkit getToolkit() {
		return toolkit;
	}

	/**
	 * @param toolkit
	 *            the toolkit to set
	 */
	public void setToolkit(EditorToolkit toolkit) {
		this.toolkit = toolkit;
	}

	/**
	 * 
	 */
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
			toolkit.saveFieldValues();
		}
	}

}
