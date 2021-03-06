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
package de.xwic.sandbox.system.ui.roles;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * @author Aron Cotrau
 * @author Dogot Nicu
 *
 */
public class RolesSubmodule extends SubModule {

	/**
	 * @param site
	 */
	public RolesSubmodule(Site site) {
		super(site);
		setTitle("Roles");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#createControls(de.jwic.base.IControlContainer)
	 */
	@Override
	public IControl createControls(IControlContainer container) {
		return new RolesPage(container, "rolesPage");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#getKey()
	 */
	@Override
	public String getKey() {
		return "rolesSubmodule";
	}

}
