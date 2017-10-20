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
package de.xwic.sandbox.app.home;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.webbase.home.HomeSubmodule;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * This module contains the 'Start-Page' for the user. 
 */
public class StartModule extends Module {

	/**
	 * @param site
	 */
	public StartModule(Site site) {
		super(site);
		setTitle("Get Started");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.Module#createSubModules(de.xwic.appkit.webbase.toolkit.app.Site)
	 */
	@Override
	protected List<SubModule> createSubModules(Site site) {
		List<SubModule> subModules = new ArrayList<SubModule>();
		
		// All users have access to the 'Home' module, so no security check here.
		subModules.add(new HomeSubmodule(site));

		return subModules;
	}

}
