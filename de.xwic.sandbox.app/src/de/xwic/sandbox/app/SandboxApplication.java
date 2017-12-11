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
package de.xwic.sandbox.app;


import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import de.jwic.base.IControl;
import de.jwic.base.Page;
import de.jwic.base.SessionContext;
import de.jwic.controls.AnchorLink;
import de.jwic.controls.Label;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.util.DateFormatter;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.entityselection.EntityComboSelector;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.login.LoginControl;
import de.xwic.appkit.webbase.toolkit.login.LoginModel;
import de.xwic.appkit.webbase.toolkit.login.LoginModel.ILoginListener;
import de.xwic.appkit.webbase.toolkit.util.BundleAccessor;
import de.xwic.sandbox.app.home.StartModule;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.util.ConfigurationUtil;
import de.xwic.sandbox.crm.ui.CrmModule;
import de.xwic.sandbox.crm.ui.util.ToolkitEntitySelectionCombo;
import de.xwic.sandbox.system.ui.SystemModule;

/**
 * @author Claudiu Mateias
 *
 */
public class SandboxApplication extends ExtendedApplication{
	
	static {
		Map<Class<? extends IControl>, IToolkitControlHelper> allControls = EditorToolkit.allControls;
		allControls.put(EntityComboSelector.class, new ToolkitEntitySelectionCombo());
	}
	
	private static final String HELP_LINK = "help.link";

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.ExtendedApplication#getHelpURL()
	 */
	@Override
	public final String getHelpURL() {
		return ConfigurationManager.getSetup().getProperty(HELP_LINK);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.ExtendedApplication#getPageTitle()
	 */
	@Override
	public final String getPageTitle() {
		Bundle bundle = BundleAccessor.getDomainBundle(this.getSessionContext(), ExtendedApplication.CORE_DOMAIN_ID);
		return bundle.getString("login.page.title");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.ExtendedApplication#loadApp(de.xwic.appkit.webbase.toolkit.app.Site)
	 */
	@Override
	protected void loadApp(Site site) {

		site.setApplicationId("SANDBOX");
		
		loadModules(site);
		
		final Bundle b = BundleAccessor.getDomainBundle(this.getSessionContext(), ExtendedApplication.CORE_DOMAIN_ID);
		final Setup setup = ConfigurationManager.getSetup();
		
		final String serverName = setup.getProperty("server.name", "UNKNOWN");
		if(serverName.toLowerCase().contains("prod")) {
			site.setTitle(b.getString("site.title"));
		} else {
			site.setTitle(serverName);
		}

		site.setTemplateName(getSiteTemplateName());

		final Label verInfo = new Label(site, "lblVersionInfo");
		verInfo.setText(setup.getAppTitle() + " v" + setup.getVersion() + " (" + serverName
				+ ")");

		// flip style (for testing/design right now)
		final AnchorLink flipStyle = new AnchorLink(site, "flipStyle");
		flipStyle.setTitle("Switch Theme");
		flipStyle.addSelectionListener(createThemeToggler());
		
		// Adding a link to reload the config. This is convenient for developers in the Sandbox.
		// In a productive application, you would want this link to be somewhere in an administration
		// section, not on every page...
		final AnchorLink reloadConfig = new AnchorLink(site, "reloadConfig");
		reloadConfig.setTitle("Reload Config");
		reloadConfig.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent arg0) {
				ConfigurationUtil.reloadConfiguration();
				getSessionContext().notifyMessage("Configuration Reloaded");
			}
		});
		
		
	}

	/**
	 * @param site
	 */
	protected void loadModules(Site site) {
		
		site.addModule(new StartModule(site));

		if (ConfigurationUtil.hasAccess(CrmModule.SCOPE_MOD_CRM)) {
			site.addModule(new CrmModule(site));
		}

		if (ConfigurationUtil.hasAccess(SandboxModelConfig.MOD_RESOURCES)) {
//			site.addModule(new ResourcesModule(site));
		}

		if (ConfigurationUtil.hasAccess(SandboxModelConfig.MOD_SYSTEM)) {
			site.addModule(new SystemModule(site));
		}
		
		if (Platform.isInitialized()) {
			IPreferenceStore prefStore = site.getPreferenceStore();
			boolean restore = "true".equals(prefStore.getString("restoreNavigation", "false"));
			if (!restore || !site.restoreNavigation()) {
				activateFirstModule(site);
			}
		}
	}
	
	/**
	 * @param site
	 */
	private void activateFirstModule(Site site) {
		final Iterator<Module> iterator = site.getModules().iterator();
		while (iterator.hasNext()) {
			final Module candidate = iterator.next();
			if (!candidate.getSubModules().isEmpty()) {
				activateModule(site, candidate);
				return;
			}
		}
		throw new IllegalStateException("No modules available");
	}
	
	/**
	 * @param site
	 * @param activeModule
	 */
	private void activateModule(Site site, Module activeModule) {
		site.actionSelectMenu(activeModule.getKey() + ";" + activeModule.getSubModules().get(0).getKey());
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.Application#initialize(de.jwic.base.SessionContext)
	 */
	@Override
	public final void initialize(SessionContext context) {
		context.setLocale(new Locale("en", "EN"));
		context.setThemeName("sandbox-normal");
	
		context.setDateFormat(DateFormatter.formatDate);
		context.setTimeFormat(DateFormatter.formatTime);
		
		super.initialize(context);
	}
	
	/**
	 * @return
	 */
	private SelectionListener createThemeToggler() {
		return new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				toggleTheme(getSessionContext());
			}

			/**
			 * @param sx
			 */
			private void toggleTheme(SessionContext sx) {
				String nextTheme = getNextTheme(sx.getThemeName());
				sx.setThemeName(nextTheme);
			}

			/**
			 * @param theme
			 * @return
			 */
			private String getNextTheme(String theme) {
				if (theme == null) {
					theme = "";
				}
				String normal = "sandbox-normal";
				String small = "sandbox-small";

				if (normal.equals(theme)) {
					return small;
				}
				return normal;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.ExtendedApplication#performLoad(de.jwic.base.SessionContext)
	 */
	@Override
	protected final void performLoad(final SessionContext container) {

		IUser user = DAOSystem.getSecurityManager().getCurrentUser();
		if (user == null) {
			user = DAOSystem.getSecurityManager().detectUser();
		}

		if (user != null) {
			// the user was identified by a cookie.
			loadSite(container);

		} else {

			Page loginPage = new Page(container);
			loginPage.setTemplateName(ExtendedApplication.class.getName() + "LoginPage");
			loginPage.setTitle(getPageTitle());

			LoginModel model = new LoginModel();
			new LoginControl(loginPage, "loginControl", model);
			container.pushTopControl(loginPage);

			model.addListener(new ILoginListener() {

				@Override
				public void loginSuccessful() {
					DAOSystem.getSecurityManager().rememberActiveUser();
					loadSite(container);
				}

			});
		}
	}
}
