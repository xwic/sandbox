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
package de.xwic.sandbox.demoapp.model;

import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.sandbox.demoapp.model.dao.ICompanyDAO;
import de.xwic.sandbox.demoapp.model.dao.IContactDAO;
import de.xwic.sandbox.demoapp.model.dao.impl.CompanyDAO;
import de.xwic.sandbox.demoapp.model.dao.impl.ContactDAO;

/**
 * @author WebEnd
 *
 */
public class DemoAppModelConfig {
	/**
	 * @param factory
	 */
	public static void register(DAOFactory factory) {
		factory.registerDao(IContactDAO.class, new ContactDAO());
		factory.registerDao(ICompanyDAO.class, new CompanyDAO());
	}
	
	/**
	 * @return
	 */
	public static IContactDAO getContactDAO() {
		return DAOSystem.getDAO(IContactDAO.class);
	}

	public static ICompanyDAO getCompanyDAO() {
		return DAOSystem.getDAO(ICompanyDAO.class);
	}
}
