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
package de.xwic.sandbox.system.ui;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.listpage.BasicListPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * Displays a list of customers.
 */
public class MitarbeiterListSubmodule extends SubModule {

	public final static String SCOPE_SMOD_SYSTEM_USERS = "SMOD_SYSTEM_USERS";
	private final static ImageRef MODULE_ICON = new ImageRef("images/module_icons/address_book.png");
	
	/**
	 * @param site
	 */
	public MitarbeiterListSubmodule(Site site) {
		super(site);
		setTitle("Users & Access");
		setDescription("List of Employees/Users");
		setDefaultQuickLaunch(true);
		setFullTitle("User List");
		setIconLarge(MODULE_ICON);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#createControls(de.jwic.base.IControlContainer)
	 */
	@Override
	public IControl createControls(IControlContainer container) {
		return new BasicListPage(container, "listPage", IMitarbeiter.class);
	}

}
