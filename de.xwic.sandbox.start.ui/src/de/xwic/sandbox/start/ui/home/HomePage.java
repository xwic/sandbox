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
package de.xwic.sandbox.start.ui.home;

import java.io.IOException;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.Label;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.IPageControl;


public class HomePage extends ControlContainer implements IPageControl {

	private CheckBox ckRestore;

	public HomePage(IControlContainer container, String name) {
		super(container, name);

		Label lbWelcome = new Label(this, "lbWelcome");
		new QuickLaunchPanel(this, "qlPanel");

		IMitarbeiter ma = getMitarbeiterDAO().getByCurrentUser();
		if (ma != null) {
			lbWelcome.setText("Welcome, " + ma.getVorname() + "!");
		} else {
			lbWelcome.setText("Welcome [Unknown]");
		}

		IPreferenceStore prefStore = ExtendedApplication.getInstance(this).getSite().getPreferenceStore();
		ckRestore = new CheckBox(this, "ckRestore");
		ckRestore.setLabel("Always open this page when starting Sandbox Ap");
		ckRestore.setChecked(!"true".equals(prefStore.getString("restoreNavigation", "false")));
		ckRestore.setInfoText("If you disable this option, Sandbox App will open the module you have activated the last time.");
		ckRestore.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				toggleRestorePreference();
			}
		});

	}

	protected void toggleRestorePreference() {
		IPreferenceStore prefStore = ExtendedApplication.getInstance(this).getSite().getPreferenceStore();
		prefStore.setValue("restoreNavigation", ckRestore.isChecked() ? "false" : "true");

		try {
			prefStore.flush();
		} catch (IOException e) {
			log.error("Error flushing the settings", e);
		}

	}

	private static IMitarbeiterDAO getMitarbeiterDAO() {
		return DAOSystem.getDAO(IMitarbeiterDAO.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IPageControl#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		throw new UnsupportedOperationException("Not supported.");
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IPageControl#getTitle()
	 */
	@Override
	public String getTitle() {
		return "Home";
	}

}
