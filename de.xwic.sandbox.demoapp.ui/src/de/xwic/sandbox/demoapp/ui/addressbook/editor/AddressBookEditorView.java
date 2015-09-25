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
package de.xwic.sandbox.demoapp.ui.addressbook.editor;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.sandbox.util.ui.editor.EntityEditorView;

/**
 * @author WebEnd
 *
 */
@SuppressWarnings("serial")
public class AddressBookEditorView extends EntityEditorView {

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public AddressBookEditorView(IControlContainer container, String name, EditorModel model) {
		super(container, name, model);
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorView#createControls()
	 */
	@Override
	protected void createControls() {
		TabStrip tabStripControl = new TabStrip(this, "tabStripControl");
		Tab tabGeneral = tabStripControl.addTab("Address Book entry details", "general");
		new AddressBookEditorControl(tabGeneral, "addressBookEditorControl", model,toolkit);
	}

}
