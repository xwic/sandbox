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

import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;

public abstract class EntityEditorPage extends InnerPage implements IEditorModelListener {

	private EditorModel model;

	/**
	 * 
	 * @param container
	 * @param name
	 * @param model
	 */
	public EntityEditorPage(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		this.model = model;
		init();
		model.addModelListener(this);

		boolean isEditingEntity = model.getEntity().getId() > 0;
		String title = isEditingEntity ? model.getEntityDAO().buildTitle(model.getEntity()) : getNewTitle();
		String subTitle = getPageSubtitle();

		setTitle(title);
		setSubtitle(subTitle);

		createControls(model);
	}

	/** creates the controls */
	protected abstract void createControls(EditorModel model);

	/** returns the subtitle of the page */
	protected abstract String getPageSubtitle();

	/** returns the page name when a new entity in editing */
	//util.ui.editor
	protected abstract String getNewTitle();

	/**
	 * method called before the object is added as a listener
	 */
	protected void init() {

	}

	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
			// someone wants to close this
			Site site = ExtendedApplication.getInstance(this).getSite();
			site.popPage(this);
		}
	}

	public EditorModel getModel() {
		return model;
	}


}
