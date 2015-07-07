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
package de.xwic.sandbox.resources.ui.people.editor;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
import de.jwic.controls.wizard.ValidationException;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.sandbox.resources.model.people.editor.model.PeopleEditorModel;
import de.xwic.sandbox.util.ui.editor.EntityEditorView;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
@SuppressWarnings("serial")
public class PeopleEditorView extends EntityEditorView {

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public PeopleEditorView(IControlContainer container, String name, EditorModel model) {
		super(container, name, model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorView#createControls()
	 */
	@Override
	protected void createControls() {

		TabStrip tabStripControl = new TabStrip(this, "tabStripControl");

		Tab tabGeneral = tabStripControl.addTab("General", "general");
		new PeopleEditorControl(tabGeneral, "peopleEditorControl", model);

		Tab tabSecurity = tabStripControl.addTab("Security", "security");
		new PeopleSecurityEditor(tabSecurity, "peopleSecurityControl", (PeopleEditorModel) model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorView#performSave(boolean)
	 */
	@Override
	protected void performSave(boolean showInfo) {
		try {
			PeopleEditorModel peopleEditorModel = (PeopleEditorModel) model;

			peopleEditorModel.clearExceptions();
			peopleEditorModel.validateEntity();
			ValidationException valExc = peopleEditorModel.getValidationException();

			if (null == valExc || !valExc.hasErrors()) {
				((PeopleEditorModel) model).saveEntityAndGrades();
				finish(showInfo);
			} else {
				StringBuffer msg = new StringBuffer();
				for (Object field : valExc.getErrorFields()) {
					msg.append(valExc.getMessage(field.toString())).append("<br>");
				}
				showError(msg.toString());
			}
		} catch (ValidationException e) {
			processException(e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			processException(e);
		}
	}

	/**
	 * @param showInfo
	 */
	private void finish(boolean showInfo) {
		if (showInfo) {
			showWarning("Entity was saved successfully");
		} else {
			// direct close here
			model.close();
		}
	}
}
