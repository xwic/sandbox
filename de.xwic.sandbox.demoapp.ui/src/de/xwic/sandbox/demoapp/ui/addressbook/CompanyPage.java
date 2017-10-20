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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.util.ConfigurationUtil;
import de.xwic.sandbox.demoapp.model.DemoAppModelConfig;
import de.xwic.sandbox.demoapp.model.dao.ICompanyDAO;
import de.xwic.sandbox.demoapp.model.entities.ICompany;

/**
 * @author WebEnd
 *
 */
@SuppressWarnings("serial")
public class CompanyPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public CompanyPage(IControlContainer container, String name) {
		super(container, name);

		setTitle("Companies");
		setSubtitle("Manage companies you know/do business with.");
	
		PropertyQuery baseQuery = new PropertyQuery();
		
		
		//baseQuery.addEquals("type.key", "private");

		PropertyQuery defaultQuery = new PropertyQuery();
		defaultQuery.setSortField("companyName");
		defaultQuery.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		//defaultQuery.addEquals("city", "Munich");
		
		EntityListViewConfiguration config = new EntityListViewConfiguration(ICompany.class);
		config.setBaseFilter(baseQuery);
		config.setDefaultFilter(defaultQuery);
		
		try {
			EntityListView<ICompany> listView = new EntityListView<ICompany>(this,config);
			Button button = listView.getToolbar().addGroup().addButton();
			
			button.setTitle("Test Me");
			button.addSelectionListener(new SelectionListener() {
				
				@Override
				public void objectSelected(SelectionEvent event) {
					doSome();
				}
			});
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}

	/**
	 * 
	 */
	protected void doSome() {

		try {

			
			System.out.println(ConfigurationUtil.hasAccess("MY_TEST_SCOPE"));
			
			
			
//			InvocationHandler ih = new InvocationHandler() {
//				@Override
//				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//					System.out.println("Called '" + method.getName() + "' with args: " + args[0]);
//					return null;
//				}
//			};
//			
//			ICompany comp = (ICompany)Proxy.newProxyInstance(
//					getClass().getClassLoader(), 
//					new Class<?>[] {ICompany.class}, 
//					ih);
//			
//			comp.setAddress1("bla");
			
			//getSessionContext().notifyMessage("P: maxLength: " + p.getMaxLength());
			
		} catch (Exception e) {
			getSessionContext().notifyMessage("Ups: " + e);
		}
		
		
		
	}

}
