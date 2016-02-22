/*
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
**/
package de.xwic.sandbox.demoapp.ui.addressbook.experimental;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.actions.Action;
import de.jwic.controls.actions.IAction;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Profile;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.EntityModelException;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.GenericEditorInput;
import de.xwic.appkit.webbase.editors.GenericEntityEditor;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.editors.mappers.MappingException;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Lippisch
 *
 */
public class ExperimentalEditorPage extends InnerPage {

	private GenericEntityEditor editor;
	private EditorContext context;

	private IAction actionSave;
	private IAction actionSaveClose;
	private IAction actionClose;

	/**
	 * @param container
	 * @param name
	 */
	public ExperimentalEditorPage(IControlContainer container, String name, EditorModel editorModel) {
		super(container, name);
		
		setTitle("Experimental Editor");
		
		createActions();
		createToolbar();
		createContent(editorModel);
		
	}
	
	/**
	 * 
	 */
	private void createActions() {

		actionSave = new Action() {
			@Override
			public void run() {
				performSave();
			}
		};
		actionSave.setTitle("Save");
		actionSave.setIconEnabled(ImageLibrary.ICON_SAVE_ACTIVE);
		actionSave.setIconDisabled(ImageLibrary.ICON_SAVE_INACTIVE);

		actionSaveClose = new Action() {
			@Override
			public void run() {
				if (performSave()) {
					closeEditor();
				}
			}
		};
		actionSaveClose.setTitle("Save & Close");
		actionSaveClose.setIconEnabled(ImageLibrary.ICON_SAVECLOSE_ACTIVE);
		actionSaveClose.setIconDisabled(ImageLibrary.ICON_SAVECLOSE_INACTIVE);

		actionClose = new Action() {
			@Override
			public void run() {
				closeEditor();
			}
		};
		actionClose.setTitle("Close");
		actionClose.setIconEnabled(ImageLibrary.ICON_CLOSED);
		actionClose.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);

	}

	/**
	 * 
	 */
	protected void closeEditor() {
		
		Site site = ExtendedApplication.getInstance(this).getSite();
		site.popPage(this);
		
	}

	/**
	 * 
	 */
	protected boolean performSave() {
		try {
			ValidationResult validationResult = context.saveToEntity();
			if (validationResult.hasErrors()) {
				getSessionContext().notifyMessage("There have been issues... *eek*", "error");
			} else {
				getSessionContext().notifyMessage("Your changes have been saved...");
				return true;
			}
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 */
	private void createToolbar() {
		
		ToolBar toolbar = new ToolBar(this, "toolbar");
		ToolBarGroup grpDefault = toolbar.addGroup();

		grpDefault.addAction(actionClose);
		grpDefault.addAction(actionSaveClose);
		grpDefault.addAction(actionSave);
		
		
	}

	/**
	 * Create the editor.
	 * @param editorModel
	 */
	private void createContent(EditorModel editorModel) {
		
		try {
			Profile profile = ConfigurationManager.getSetup().getProfile("default");
			EditorConfiguration editorConfig = profile.getEditorConfiguration(editorModel.getEntity().type().getName());
			
			GenericEditorInput input = new GenericEditorInput(editorModel.getEntity(), editorConfig);
			editor = new GenericEntityEditor(this, "editor", input);
			context = editor.getContext();
		
		} catch (Exception e) {
			log.error("Error creating editor", e);
			getSessionContext().notifyMessage("Error: " + e.toString());
		}
		
	}

	
}
