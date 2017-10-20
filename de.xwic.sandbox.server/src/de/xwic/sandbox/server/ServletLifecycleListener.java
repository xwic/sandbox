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
package de.xwic.sandbox.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

import de.xwic.appkit.core.CommonConfiguration;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.XmlConfigLoader;
import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DefaultUseCaseService;
import de.xwic.appkit.core.dao.impl.hbn.HibernateDAOProvider;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.file.impl.hbn.HbnFileOracleFixDAO;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.impl.StorageProvider;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.crm.CrmModelConfig;
import de.xwic.sandbox.security.ServerSecurityManager;
import de.xwic.sandbox.security.UserContextPreferenceProvider;

/**
 * @author Claudiu Mateias
 *
 */
public class ServletLifecycleListener implements ServletContextListener {
	private final Log log = LogFactory.getLog(ServletLifecycleListener.class);

	/**
	 * 
	 */
	public ServletLifecycleListener() {
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		HibernateDAOProvider hbnDP = new HibernateDAOProvider();

		DAOFactory factory = CommonConfiguration.createCommonDaoFactory(hbnDP);

		DAOSystem.setDAOFactory(factory);
		DAOSystem.setSecurityManager(new ServerSecurityManager());
		DAOSystem.setUseCaseService(new DefaultUseCaseService(hbnDP));
		DAOSystem.setFileHandler(new HbnFileOracleFixDAO());

		
		SandboxModelConfig.register(factory);
		CrmModelConfig.register(factory);
		
		final ServletContext context = event.getServletContext();
		SandboxModelConfig.setWebRootDirectory(new File(context.getRealPath("/")));

		final String rootPath = context.getRealPath("");

		final File path = new File(rootPath + "/config");
		Setup setup;
		try {
			setup = XmlConfigLoader.loadSetup(path.toURI().toURL());
		} catch (Exception e) {
			log.error("Error loading product configuration", e);
			throw new RuntimeException("Error loading product configuration: " + e, e);
		}
		ConfigurationManager.setSetup(setup);
		
		if (!HibernateUtil.isInitialized()) {
        	Configuration configuration = new Configuration();
        	configuration.configure(); // load configuration settings from hbm file.
        	// load properties
        	Properties prop = new Properties();
        	InputStream in = context.getResourceAsStream("WEB-INF/hibernate.properties");
        	if (in == null) {
        		in = context.getResourceAsStream("/WEB-INF/hibernate.properties");
        	}
        	if (in != null) {
	        	try {
					prop.load(in);
		        	configuration.setProperties(prop);
				} catch (IOException e) {
					log.error("Error loading hibernate.properties. Skipping this step! : " + e);
				}
        	}
        	HibernateUtil.initialize(configuration);
        }

		File prefStorePath = new File(new File(rootPath), "WEB-INF/prefstore");
		if (!prefStorePath.exists() && !prefStorePath.mkdirs()) {
			throw new IllegalStateException("Error initializing preference store: can not create directory "
					+ prefStorePath.getAbsolutePath());
		}
		Platform.initialize(new StorageProvider(prefStorePath), new UserContextPreferenceProvider());

	}

}
