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
package de.xwic.sandbox.actiontracker;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.util.ConfigurationUtil;


public class ActionTrackerModule extends Module {

	public ActionTrackerModule(Site site) {
		super(site);
		setTitle("Action Tracker");
		setKey(getModuleKey());
	}

	protected List<SubModule> createSubModules(Site site) {
		List<SubModule> list = new ArrayList<SubModule>();
		
		if (ConfigurationUtil.hasAccess(SandboxModelConfig.SMOD_ACTION_TRACKER_ACTION_TRACKER)) {		
			//list.add(new ActionTrackerOverviewSubmodule(site));
		}

		return list;
	}

	protected String getModuleKey() {
		return "actionTracker";
	}

}
