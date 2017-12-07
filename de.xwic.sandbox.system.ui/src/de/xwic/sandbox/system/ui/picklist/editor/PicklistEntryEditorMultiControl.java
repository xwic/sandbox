/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.picklists.editor.PicklistEntryEditorMultiControl 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.jwic.controls.combo.DropDown;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author mariusb
 *
 */
public class PicklistEntryEditorMultiControl extends ControlContainer {

	private InputBox ibText;
	private IPicklistEntry pe;
	private Language languageAppkit;
	private List<Language> languageList;
	private String text;
	private String language;
	private DropDown ddLanguages;
	private List<PicklistEntryEditorMultiControl> pickListTextControls;

	/**
	 * @param container
	 * @param pickListLanguagesFromCountry
	 * @param pickListTextControls
	 */
	public PicklistEntryEditorMultiControl(IControlContainer container, List<Language> languageList,
			List<PicklistEntryEditorMultiControl> pickListTextControls) {
		super(container);
		this.languageList = languageList;
		this.pickListTextControls = pickListTextControls;

		createControls();
	}

	/**
	 * @param container
	 * @param pe
	 * @param language
	 */
	public PicklistEntryEditorMultiControl(IControlContainer container, IPicklistEntry pe, Language language) {
		super(container);
		this.pe = pe;
		this.languageAppkit = language;

		createControls();
	}

	/**
	 * 
	 */
	private void createControls() {
		ibText = new InputBox(this, "ibText");
		ibText.setWidth(300);

		ddLanguages = new DropDown(this, "ddLanguages");
		ddLanguages.setWidth(50);

		if (pe != null) {
			ibText.setText(pe.getBezeichnung(languageAppkit.getId()));

			ddLanguages.setText(languageAppkit.getId().toUpperCase());
			ddLanguages.setEnabled(false);

			setLanguage(ddLanguages.getText());
			setText(ibText.getText());
		} else {
			// get the distinct country languages
			for (Language languageAppkit : languageList) {
				boolean found = false;
				// get the languages from the drop down
				for (PicklistEntryEditorMultiControl control : pickListTextControls) {
					if (control.ddLanguages.getText() != null
							&& control.ddLanguages.getText().equals(languageAppkit.getId().toUpperCase())) {
						found = true;
						break;
					}
				}
				// add only languages to the drop down that have not been already added 
				if (!found) {
					ddLanguages.addElement(languageAppkit.getId().toUpperCase(), languageAppkit.getId());
				}
			}
		}
	}

	/**
	 * 
	 */
	public void setControls() {
		setLanguage(ddLanguages.getText());
		setText(ibText.getText());
	}

	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return this.language.toLowerCase();
	}

	/**
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return
	 */
	public String getText() {
		return this.text;
	}

}
