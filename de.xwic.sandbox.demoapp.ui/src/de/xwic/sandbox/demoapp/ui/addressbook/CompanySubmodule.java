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
package de.xwic.sandbox.demoapp.ui.addressbook;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.sandbox.basegui.SandboxSubModule;

/**
 * @author WebEnd
 *
 */
public class CompanySubmodule extends SandboxSubModule {

	/**
	 * @param site
	 */
	public CompanySubmodule(Site site) {
		super(site);
		setTitle("Companies");

		setFullTitle("Company Overview");
		setDescription("Manage all companies related to you.");
		setIconLarge(new ImageRef("images/module_icons/address_book.png"));
		setDefaultQuickLaunch(true);		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#createControls(de.jwic.base.IControlContainer)
	 */
	@Override
	public IControl createControls(IControlContainer container) {
		return new CompanyPage(container, "companyPage");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#getKey()
	 */
	@Override
	public String getKey() {
		return "companySubmodule";
	}

}
