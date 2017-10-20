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
package de.xwic.sandbox.crm.ui;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import de.xwic.sandbox.crm.ui.cases.CasesListSubmodule;
import de.xwic.sandbox.crm.ui.contact.ContactListSubmodule;
import de.xwic.sandbox.crm.ui.customer.CustomerListSubmodule;

/**
 * The module represents the entry point for the user. It defines the available sub-modules, which 
 * open a specific page. The module itself does not have any content for the user.
 *
 */
public class CrmModule extends Module {

	public final static String SCOPE_MOD_CRM = "MOD_CRM";
	
	public CrmModule(Site site) {
		super(site);
		setTitle("CRM");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.Module#createSubModules(de.xwic.appkit.webbase.toolkit.app.Site)
	 */
	@Override
	protected List<SubModule> createSubModules(Site site) {
		List<SubModule> list = new ArrayList<SubModule>();
		
		// The security manager is used to check rights of the current user.
		ISecurityManager securityManager = DAOSystem.getSecurityManager();
		
		if (securityManager.hasAccess(CustomerListSubmodule.SCOPE_SMOD_CRM_CUSTOMERS)) {		
			list.add(new CustomerListSubmodule(site));
		}
		if (securityManager.hasAccess(ContactListSubmodule.SCOPE_SMOD_CRM_CONTACTS)) {		
			list.add(new ContactListSubmodule(site));
		}
		if (securityManager.hasAccess(CasesListSubmodule.SCOPE_SMOD_CRM_CASES)) {		
			list.add(new CasesListSubmodule(site));
		}

		return list;
	}

}
