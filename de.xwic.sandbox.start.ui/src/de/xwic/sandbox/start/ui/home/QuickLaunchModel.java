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
/**
 * 
 */
package de.xwic.sandbox.start.ui.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.ImageRef;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import de.xwic.sandbox.basegui.SandboxSubModule;
import de.xwic.sandbox.start.model.StartModelConfig;
import de.xwic.sandbox.start.model.dao.IQuickLaunchDAO;
import de.xwic.sandbox.start.model.entities.IQuickLaunch;
import de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction;

/**
 * 
 * @author Raluca Geogia
 *
 */
public class QuickLaunchModel {

	private static final Log log = LogFactory.getLog(QuickLaunchModel.class);
	private static final String EXTENSION_ID = "quickLaunchActions";
	public static final String RESTYPE_SUBMODULE = "submodule";
	public static final String RESTYPE_FUNCTION = "qlf";
	public static final String QL_INIT = "home-ql-init";
	
	private String username;
	
	/**
	 * 
	 */
	public QuickLaunchModel() {
		IUser user = DAOSystem.getSecurityManager().getCurrentUser();
		if (user != null) {
			username = user.getLogonName();
		} else {
			username = null;
		}
	}
	
	/**
	 * Return the items to display for the current user.
	 * @return
	 */
	public List<IQuickLaunch> getQuickLaunchItems() {
		
		if (username == null) {
			// return an empty list.
			log.error("Can not determine user in QuickLaunchModel!?");
			return new ArrayList<IQuickLaunch>();
		}
		
		PropertyQuery query = new PropertyQuery();
		query.addEquals("username", username);
		query.setSortField("order");
		query.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		return StartModelConfig.getQuickLaunchDAO().getEntities(null, query);
		
	}

	/**
	 * Check if the user is entering this module for the first time. If so,
	 * create a set of default QuickLaunch items.
	 * @param site 
	 */
	public void initUser(Site site) {

		IPreferenceStore prefStore = Platform.getContextPreferenceProvider().getPreferenceStore("sandbox-start");
		if ("false".equals(prefStore.getString(QL_INIT, "false"))) {
			
			if (getQuickLaunchItems().isEmpty()) {
				// create set of default items.
				int count = 0;

				IQuickLaunchDAO qlDAO = StartModelConfig.getQuickLaunchDAO();
				// add functions
				IUser user = DAOSystem.getSecurityManager().getCurrentUser();
				
				for (IQuickLaunchFunction qlf : getFunctions()) {
					if (qlf.isDefaultVisible() && qlf.isAvailable(user)) {
						IQuickLaunch ql = qlDAO.createEntity();
						ql.setUsername(username);
						ql.setOrder(count);
						ql.setReference(qlf.getReference());
						qlDAO.update(ql);
						count++;
					}
				}
				
				// scan modules for 'default' links
				for (Module module : site.getModules()) {
					count = scanAndInit(module.getSubModules(), module.getKey(), count);
				}
			}
			
			prefStore.setValue(QL_INIT, "true");
			try {
				prefStore.flush();
			} catch (IOException e) {
				log.error("Error storing user preference", e);
			}
			
		}
		
	}

	/**
	 * @param subModules
	 * @param count
	 */
	private int scanAndInit(List<SubModule> subModules, String path, int count) {
		
		IQuickLaunchDAO qlDAO = StartModelConfig.getQuickLaunchDAO();
		
		
		for (SubModule sm : subModules) {
			if (sm.hasSubModules()) {
				count = scanAndInit(sm.getSubModules(), path + "/" + sm.getKey(), count);
			} else {
				if (sm instanceof SandboxSubModule) {
					SandboxSubModule psm = (SandboxSubModule) sm;
					if (psm.isDefaultQuickLaunch()) {
						// create an entry
						
						IQuickLaunch ql = qlDAO.createEntity();
						ql.setUsername(username);
						ql.setOrder(count);
						ql.setReference(RESTYPE_SUBMODULE + ":" + path + "/" + sm.getKey());
						qlDAO.update(ql);
						count++;
					}
				}
			}
		}
		
		return count;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Update the QuickLaunch order attribute to store the order the user selected.
	 * @param ids
	 */
	public void reIndex(String[] ids) {

		IQuickLaunchDAO qlDAO = StartModelConfig.getQuickLaunchDAO();

		List<IQuickLaunch> items = getQuickLaunchItems();
		Map<Integer, IQuickLaunch> allItems = new HashMap<Integer, IQuickLaunch>();
		for (IQuickLaunch ql : items) {
			allItems.put(ql.getId(), ql);
		}
		
		int order = 1; // start with 1
		for (String s : ids) {
			Integer id = Integer.parseInt(s);
			IQuickLaunch ql = allItems.get(id);
			if (ql != null) {
				if (ql.getOrder() != order) {
					ql.setOrder(order);
					qlDAO.update(ql);
				}
				order++;
			}
		}
	}
	
	/**
	 * @return
	 */
	public List<IQuickLaunchFunction> getFunctions(){
		List<IExtension> viewQLExtention = ExtensionRegistry.getInstance().getExtensions(EXTENSION_ID);
		List<IQuickLaunchFunction> viewQLFunctions = new ArrayList<IQuickLaunchFunction>();
		for(IExtension ext:viewQLExtention){
			IQuickLaunchFunction function;
			try {
				function = (IQuickLaunchFunction) ext.createExtensionObject();
				function.setDescription(ext.getAttribute("description"));
				function.setTitle(ext.getAttribute("title"));
				function.setDefaultVisible("true".equalsIgnoreCase(ext.getAttribute("defaultVisible")));
				if (ext.getAttribute("icon") != null && !ext.getAttribute("icon").isEmpty()) {
					function.setIcon(new ImageRef(ext.getAttribute("icon")));
				}
				function.setReference(RESTYPE_FUNCTION+ ":" + ext.getId());
				viewQLFunctions.add(function);
			}catch(Exception e){
				log.error("Error reading functions", e);
			}
		}
		return viewQLFunctions;
	}
}

