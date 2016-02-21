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
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Profile;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.webbase.editors.GenericEditorInput;
import de.xwic.appkit.webbase.editors.GenericEntityEditor;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Lippisch
 *
 */
public class ExperimentalEditorPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public ExperimentalEditorPage(IControlContainer container, String name, EditorModel editorModel) {
		super(container, name);
		
		setTitle("Experimental Editor");
		
		createContent(editorModel);
		
	}
	
	private void createContent(EditorModel editorModel) {
		
		try {
			Profile profile = ConfigurationManager.getSetup().getProfile("default");
			EditorConfiguration editorConfig = profile.getEditorConfiguration(editorModel.getEntity().type().getName());
			
			GenericEditorInput input = new GenericEditorInput(editorModel.getEntity(), editorConfig);
			GenericEntityEditor editor = new GenericEntityEditor(this, "editor", input);
		
		} catch (Exception e) {
			log.error("Error creating editor", e);
			getSessionContext().notifyMessage("Error: " + e.toString());
		}
		
	}

	
}
