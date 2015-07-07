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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.InputBox;
import de.jwic.controls.Label;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.sandbox.resources.model.people.editor.model.PeopleEditorModel;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class PeopleEditorControl extends ControlContainer implements IEditorModelListener {

	private EditorModel model = null;
	private final IMitarbeiter mit;

	private InputBox ibVorname = null;
	private InputBox ibNachname = null;
	private InputBox ibZusatz = null;
	private InputBox ibTelefon = null;
	private InputBox ibFax = null;
	private InputBox ibMobile = null;
	private InputBox ibEmail = null;

	private CheckBox chkRetired = null;

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public PeopleEditorControl(IControlContainer container, String name, EditorModel model) {
		super(container, name);

		this.model = model;
		this.mit = (IMitarbeiter) this.model.getEntity();
		this.model.addModelListener(this);

		createControls();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent
	 * )
	 */
	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
			fillMitarbeiter();
		}
	}

	/**
	 * 
	 */
	private void createControls() {
		String prefix = IMitarbeiter.class.getName();
		Bundle bundle;
		try {
			bundle = ConfigurationManager.getSetup().getEntityDescriptor(prefix).getDomain()
					.getBundle(getSessionContext().getLocale().getLanguage());
		} catch (ConfigurationException e) {
			throw new RuntimeException("Bundle not found!?!? " + e, e);
		}

		Label lblVorname = new Label(this, "lblVorname");
		lblVorname.setText(bundle.getString(prefix + ".vorname"));

		ibVorname = new InputBox(this, "ibVorname");
		ibVorname.setWidth(300);

		Label lblNachname = new Label(this, "lblNachname");
		lblNachname.setText(bundle.getString(prefix + ".nachname"));

		ibNachname = new InputBox(this, "ibNachname");
		ibNachname.setWidth(300);

		Label lblZusatz = new Label(this, "lblZusatz");
		lblZusatz.setText(bundle.getString(prefix + ".zusatz"));

		ibZusatz = new InputBox(this, "ibZusatz");
		ibZusatz.setWidth(300);

		Label lblTelefon = new Label(this, "lblTelefon");
		lblTelefon.setText(bundle.getString(prefix + ".telefon"));

		ibTelefon = new InputBox(this, "ibTelefon");
		ibTelefon.setWidth(300);

		Label lblFax = new Label(this, "lblFax");
		lblFax.setText(bundle.getString(prefix + ".fax"));

		ibFax = new InputBox(this, "ibFax");
		ibFax.setWidth(300);

		Label lblMobile = new Label(this, "lblMobile");
		lblMobile.setText(bundle.getString(prefix + ".handyNr"));

		ibMobile = new InputBox(this, "ibMobile");
		ibMobile.setWidth(300);

		Label lblEmail = new Label(this, "lblEmail");
		lblEmail.setText(bundle.getString(prefix + ".email"));

		ibEmail = new InputBox(this, "ibEmail");
		ibEmail.setWidth(300);

		chkRetired = new CheckBox(this, "chkAusgeschieden");
		chkRetired.setLabel("Retired");

		Label lblVorgesetzter = new Label(this, "lblVorgesetzter");
		lblVorgesetzter.setText(bundle.getString(prefix + ".vorgesetzter"));

		if (null != mit) {
			ibVorname.setText(mit.getVorname());
			ibNachname.setText(mit.getNachname());
			ibZusatz.setText(mit.getZusatz());
			ibTelefon.setText(mit.getTelefon());
			ibFax.setText(mit.getFax());
			ibMobile.setText(mit.getHandyNr());
			ibEmail.setText(mit.getEmail());
			chkRetired.setChecked(mit.isAusgeschieden());
		}
	}

	/**
	 * @return
	 */
	private IMitarbeiter fillMitarbeiter() {

		PeopleEditorModel peopleEditorModel = (PeopleEditorModel) model;
		IMitarbeiter mit = peopleEditorModel.getEmployee();

		mit.setVorname(ibVorname.getText());
		mit.setNachname(ibNachname.getText());
		mit.setZusatz(ibZusatz.getText());
		mit.setTelefon(ibTelefon.getText());
		mit.setFax(ibFax.getText());
		mit.setHandyNr(ibMobile.getText());
		mit.setEmail(ibEmail.getText());

		mit.setAusgeschieden(chkRetired.isChecked());

		return mit;
	}
}
