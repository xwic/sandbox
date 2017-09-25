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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;
import de.xwic.sandbox.demoapp.model.entities.IContact;

/**
 * @author WebEnd
 *
 */
@SuppressWarnings("serial")
public class AddressBookEditorControl extends ControlContainer implements IEditorModelListener {
	private EditorModel model = null;
	protected EditorToolkit toolkit;
	
	/**
	 * @param container
	 * @param name
	 * @param model
	 * @param toolKit
	 */
	public AddressBookEditorControl(IControlContainer container, String name, EditorModel model, EditorToolkit toolKit) {
		super(container, name);
		this.model = model;
		this.model.addModelListener(this);
		this.toolkit = toolKit;
		createControls();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent)
	 */
	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
		}
	}
	
	/**
	 * Create controls for entity edit
	 */
	private void createControls() {
		
	
		toolkit.createControl(PicklistEntryControl.class, this, "title", IContact.PL_CONTACT_TITLE);

		toolkit.createControl(InputBox.class, this, "firstName");
		toolkit.createControl(InputBox.class, this, "middleName");
		toolkit.createControl(InputBox.class, this, "lastName");
		
		toolkit.createControl(InputBox.class, this, "phone1");
		toolkit.createControl(InputBox.class, this, "phone2");
		
		toolkit.createControl(InputBox.class, this, "fax");

		toolkit.createControl(InputBox.class, this, "mobile1");
		toolkit.createControl(InputBox.class, this, "mobile2");
		
		toolkit.createControl(InputBox.class, this, "webSite");
		
		toolkit.createControl(InputBox.class, this, "email1");
		toolkit.createControl(InputBox.class, this, "email2");
		
		toolkit.createControl(InputBox.class, this, "addressCountry");
		toolkit.createControl(InputBox.class, this, "addressState");
		toolkit.createControl(InputBox.class, this, "addressCity");
		toolkit.createControl(InputBox.class, this, "addressZip");
				
		InputBox addressStreet = ((InputBox)(toolkit.createControl(InputBox.class, this, "addressStreet")));
		addressStreet.setMultiLine(true); 
		addressStreet.setHeight(45);
		addressStreet.setCols(50);
		
		InputBox note = ((InputBox)(toolkit.createControl(InputBox.class, this, "note"))); 
		note.setMultiLine(true); 
		note.setHeight(150);
		note.setCols(50);
		
	}
}
