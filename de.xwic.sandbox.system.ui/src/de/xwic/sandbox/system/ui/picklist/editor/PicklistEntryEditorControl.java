/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.picklists.editor.PicklistEntryEditorControl 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.Configuration;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.CheckBox;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.InputBox;
import de.jwic.controls.ValidatedInputBox;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.impl.PicklistText;
import de.xwic.appkit.core.util.StringUtil;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEntryEditorControl extends ControlContainer {

	private PicklistEditorModel picklistModel;
	private IPicklistEntry baseEntity;
	
	private List<PicklistEntryEditorMultiControl> pickListTextControls;
	
	// for picklist entry
	private InputBox ibKey;
	private ValidatedInputBox ibSortIndex;
	private CheckBox chkDisabled;
	
	private Button btnSave;
	
	private ErrorWarning errorWarning;
	private Button addControl;
	private List<Language> languageList;
	
	/**
	 * @param container
	 * @param name
	 * @param picklistModel
	 */
	public PicklistEntryEditorControl(IControlContainer container, String name, PicklistEditorModel picklistModel) {
		super(container, name);
		this.picklistModel = picklistModel;
		
		IPicklisteDAO plDao = DAOSystem.getDAO(IPicklisteDAO.class);
		Setup setup = ConfigurationManager.getSetup();
		languageList = setup.getLanguages();
		
		createControls();
	}
	
	/**
	 * 
	 */
	private void createControls() {
		errorWarning = new ErrorWarning(this, "errorWarning");
		
		addControl = new Button(this, "bAdd");
		addControl.setTitle("Add Language");
		addControl.setVisible(false);
		addControl.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				addPickListTextControl();
			}
		});
		
		ibKey = new InputBox(this, "ibKey");
		ibKey.setWidth(300);
		ibKey.setEmptyInfoText("Picklist Key");
		
		ibSortIndex = new ValidatedInputBox(this, "ibSortIndex");
		ibSortIndex.setWidth(150);
		// any digit, but not 0123
		ibSortIndex.setRegExp("^(?:[1-9]\\d*|0)$");
		ibSortIndex.setText("0");
		
		chkDisabled = new CheckBox(this, "chkDisabled");
		chkDisabled.setLabel("Disabled");
		
		btnSave = new Button(this, "btnSave");
		btnSave.setTitle("Save");
		btnSave.setEnabled(false);
		btnSave.setIconEnabled(ImageLibrary.IMAGE_SAVE_ACTIVE_SMALL);
		btnSave.setIconDisabled(ImageLibrary.IMAGE_SAVE_INACTIVE_SMALL);
		btnSave.addSelectionListener(new SelectionListener() {
			
			@Override
			public void objectSelected(SelectionEvent event) {
				save();
			}
		});
		
		enableControls(false);
	}
	
	/**
	 * @param pe
	 */
	public void loadData(IPicklistEntry pe) {
		baseEntity = pe;
		pickListTextControls = new ArrayList<PicklistEntryEditorMultiControl>();
		if (baseEntity != null && baseEntity.getPickTextValues() != null) {
			for (Language language : languageList) {
				if (pe.getBezeichnung(language.getId()) != null && !pe.getBezeichnung(language.getId()).isEmpty()) {
					pickListTextControls.add(new PicklistEntryEditorMultiControl(this, pe, language));
				}
			}

			addControl.setVisible(true);
			ibKey.setText(baseEntity.getKey());
			ibSortIndex.setText(String.valueOf(baseEntity.getSortIndex()));
			chkDisabled.setChecked(baseEntity.isVeraltet());
			btnSave.setEnabled(true);
		} else {
			addControl.setVisible(true);
			ibKey.setText("");
			ibSortIndex.setText("0");
			chkDisabled.setChecked(false);
			btnSave.setEnabled(false);
			requireRedraw();
		}
		enableControls(true);
	}
	
	/**
	 * @param enabled
	 */
	private void enableControls(boolean enabled) {
		ibKey.setEnabled(enabled);
		ibSortIndex.setEnabled(enabled);
		chkDisabled.setEnabled(enabled);
	}
	
	/**
	 * 
	 */
	public void clearData() {
		loadData(null);
	}
	
	/**
	 * 
	 */
	public void loadNew() {
		loadData(picklistModel.newEntity());
	}
	
	/**
	 * 
	 */
	private void save() {
		if (baseEntity == null) {
			errorWarning.showWarning("Nothing to save");
			return;
		}
		if (validateAndPopulateFields()) {
			picklistModel.savePicklistEntry(baseEntity);
			
			errorWarning.showWarning("Values Saved!", "panelInfo");
		}
	}
	
	/**
	 * @return
	 */
	private boolean validateAndPopulateFields() {
		StringBuilder msg = new StringBuilder();
		String newKey = ibKey.getText();
		if (StringUtil.isEmpty(newKey)) {
			msg.append("'Key' cannot be blank<br>");
		}
		if (!ibSortIndex.isValid()) {
			msg.append("'Sort Index' must be a numeric value<br>");
		}
		
		baseEntity.setKey(newKey);
		baseEntity.setSortIndex(Integer.parseInt(ibSortIndex.getText()));
		baseEntity.setVeraltet(chkDisabled.isChecked());
		
		Set<IPicklistText> pickTextValues = baseEntity.getPickTextValues();

		if (pickTextValues == null) {
			pickTextValues = new HashSet<IPicklistText>();
		}

		Set<IPicklistText> newSet = null;

		for (PicklistEntryEditorMultiControl control : pickListTextControls) {
			control.setControls();
			if (StringUtil.isEmpty(control.getText())) {
				msg.append("'Picklist Text' cannot be blank");
			}

			if (StringUtil.isEmpty(control.getLanguage())) {
				msg.append(" 'Language' cannot be blank");
			}

			if (!StringUtil.isEmpty(control.getText()) || !StringUtil.isEmpty(control.getLanguage())) {
				newSet = addPicklistTextToSet((newSet == null) ? pickTextValues : newSet, control.getLanguage(), control.getText());
			}
		}

		if (newSet != null){
			baseEntity.setPickTextValues(newSet);
		} else {
			msg.append("At least one language must be added");
		}

		if (!msg.toString().isEmpty()) {
			errorWarning.showError(msg.toString());
			return false;
		}

		return true;
	}
	
	/**
	 * @param set
	 * @param langId
	 * @param text
	 */
	private Set<IPicklistText> addPicklistTextToSet(Set<IPicklistText> set, String langId, String text) {
		Set<IPicklistText> newSet = new HashSet<IPicklistText>(set);
		PicklistText newText = null;
		for (IPicklistText pt : newSet) {
			if (pt.getLanguageID().equals(langId)) {
				newText = (PicklistText) pt;
				// if pick list has been added in the past, enable it again
				if (pt.isDeleted() == true) {
					newText.setDeleted(false);
				}
				newText.setBezeichnung(text);
				break;
			}
		}
		if (newText == null) {
			newText = new PicklistText();
			newText.setPicklistEntry(baseEntity);
			newText.setLanguageID(langId);
			newText.setBezeichnung(text);
			newSet.add(newText);
		}

		return newSet;
	}

	/**
	 * @param param
	 */
	public void actionRemoveControl(String param) {
		for (PicklistEntryEditorMultiControl control : pickListTextControls) {
			if (control.getName().equals(param)) {
				control.destroy();
				control.setControls();
				if (!StringUtil.isEmpty(control.getLanguage())) {
					Set<IPicklistText> pickTextValues = baseEntity.getPickTextValues();
					if (pickTextValues == null) {
						pickTextValues = new HashSet<IPicklistText>();
					}
					removePicklistText(pickTextValues, control.getLanguage());
				}
				pickListTextControls.remove(control);
				requireRedraw();
				break;
			}
		}
	}

	/**
	 * @param set
	 * @param langId
	 * @return
	 */
	private void removePicklistText(Set<IPicklistText> set, String langId) {
		Set<IPicklistText> newSet = new HashSet<IPicklistText>(set);
		PicklistText newText = null;
		for (IPicklistText pt : newSet) {
			if (pt.getLanguageID().equals(langId)) {
				newText = (PicklistText) pt;
				newText.setDeleted(true);
				break;
			}
		}
	}

	/**
	 * @return
	 */
	public List<PicklistEntryEditorMultiControl> getPickListTextControls() {
		return pickListTextControls;
	}

	/**
	 * 
	 */
	private void addPickListTextControl() {
		pickListTextControls.add(new PicklistEntryEditorMultiControl(this, languageList, pickListTextControls));
		btnSave.setEnabled(true);
	}

}
