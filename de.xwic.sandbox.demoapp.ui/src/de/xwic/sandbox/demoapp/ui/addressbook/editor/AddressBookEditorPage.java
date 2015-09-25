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
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.sandbox.util.ui.editor.EntityEditorPage;

/**
 * @author WebEnd
 *
 */
@SuppressWarnings("serial")
public class AddressBookEditorPage extends EntityEditorPage {

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public AddressBookEditorPage(IControlContainer container, String name, EditorModel model) {
		super(container, name, model);
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorPage#createControls(de.xwic.appkit.webbase.toolkit.editor.EditorModel)
	 */
	@Override
	protected void createControls(EditorModel model) {
		new AddressBookEditorView(this, "addressBookEditorView", model);
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorPage#getPageSubtitle()
	 */
	@Override
	protected String getPageSubtitle() {
		return "Edit the details for this address book entry";
	}

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.util.ui.editor.EntityEditorPage#getNewTitle()
	 */
	@Override
	protected String getNewTitle() {
		return "New Address Book Entry";
	}

}
