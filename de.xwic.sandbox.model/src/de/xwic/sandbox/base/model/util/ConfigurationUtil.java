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
package de.xwic.sandbox.base.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.JWicRuntime;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Domain;
import de.xwic.appkit.core.config.Resource;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.XmlConfigLoader;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.StringUtil;

/**
 * @author Adrian Ionescu
 */
public class ConfigurationUtil {

	private static final Log log = LogFactory.getLog(ConfigurationUtil.class);

	//
	// ******************** SECURITY ********************
	//

	/**
	 * @param scope
	 * @return
	 */
	public static boolean hasAccess(String scope){
		return hasRight(scope, ApplicationData.SECURITY_ACTION_ACCESS);
	}

	/**
	 * @param scope
	 * @param action
	 * @return
	 */
	public static boolean hasRight(String scope, String action){
		return DAOSystem.getSecurityManager().hasRight(scope, action);
	}

	/**
	 * @param roleName
	 * @return
	 */
	public static boolean hasRole(String roleName) {
		IUser user = DAOSystem.getSecurityManager().getCurrentUser();

		for (IEntity r : user.getRoles()) {
			IRole role = (IRole) r;
			if (role.getName().equals(roleName)) {
				return true;
			}
		}

		return false;
	}

	//
	// ******************** CONFIGURATION ********************
	//

	/**
	 * @param key
	 * @return
	 * @throws ConfigurationException
	 */
	public static Resource getResource(String key) throws ConfigurationException {
		return getResourceDomain().getResource(key);
	}

	/**
	 * @return
	 * @throws ConfigurationException
	 */
	public static Domain getResourceDomain() throws ConfigurationException {
		return ConfigurationManager.getSetup().getDomain("resources");
	}
	
	/**
	 * 
	 * @return
	 * 
	 */
	
	private static String getCurrentUserEmail() {
		String senderEmail  = "";
		IMitarbeiter ma = SandboxModelConfig.getMitarbeiterDAO().getByCurrentUser();
		
		if (ma != null) {
			if (StringUtil.isEmpty(ma.getEmail())) {
				senderEmail = ma.getLogonName() + "@xwic.de";
			} else {
				senderEmail = ma.getEmail();
			}
		}
		return senderEmail;
	}

	/**
	 * 
	 * @return
	 * 
	 */
	public static String getContactEmailOrCurrentUser() {
		
		String contactFormSender = getContactFormSender();
		  
		if (!contactFormSender.isEmpty()) {
		   return contactFormSender;
		}
		return getCurrentUserEmail();
	}

	/**
	 * 
	 * @return
	 * 
	 */
	public static String getFeedbackEmailOrCurrentUser() {
	
		String feedbackFormSender = getFeedbackFormSender();
		  
		if (!feedbackFormSender.isEmpty()) {
			return feedbackFormSender;
		}
		return getCurrentUserEmail();
	}
	
	/**
	 * 
	 */
	public static String getSupportEmailAddress(){
		return getConfigurationProperty("supportEmailAddress", "support@xwic.de");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getContactFormSender(){
		return getConfigurationProperty("contactFormSenderAddress", "");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getFeedbackFormSender(){
		return getConfigurationProperty("feedbackFormSenderAddress", "");
	}
	
	/**
	 * @return
	 */
	public static boolean isTestMode(){
		return "true".equals(getConfigurationProperty("testmode", "true"));
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigurationProperty(String key, String defaultValue) {
		Setup setup = ConfigurationManager.getSetup();
		return setup == null ? defaultValue : setup.getProperty(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 */
	public static boolean isConfigPresent(final String key) {
		final Setup setup = ConfigurationManager.getSetup();
		if (null == setup) {
			return false;
		}
		return setup.getProperties().containsKey(key);
	}

	/**
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(final String key) {
		if (!isConfigPresent(key)) {
			return false;
		}
		return Boolean.valueOf(getConfigurationProperty(key, "false"));
	}

	/**
	 * @param key
	 * @return
	 * @throws ConfigurationException
	 */
	public static String getIncludePathForVTL(String key) throws ConfigurationException {
		return getResource(key).getFilePath();
	}

	/**
	 * @return
	 */
	public static String getRootPath() {
		return JWicRuntime.getJWicRuntime().getRootPath();
	}

	/**
	 * @return
	 */
	public static String getConfigPath() {
		String config = ConfigurationUtil.getConfigurationProperty("config_folder", "config");
		return getRootPath() + File.separator + config;
	}

	/**
	 *
	 */
	public static void reloadConfiguration() {
		 // load product configuration
        File path = new File(SandboxModelConfig.getWebRootDirectory(), "config");
        Setup setup;
		try {
			setup = XmlConfigLoader.loadSetup(path.toURI().toURL());
		} catch (Exception e) {
			log.error("Error loading product configuration", e);
			throw new RuntimeException("Error loading product configuration: " + e, e);
		}

        // merge server specific settings
		InputStream in = null;
		Properties srvProp = new Properties();
		try {
			File serverPropertiesFile = new File(SandboxModelConfig.getWebRootDirectory(), "WEB-INF/server.properties");
			if (serverPropertiesFile.exists()) {
				in = new FileInputStream(serverPropertiesFile);
				if (in != null) {
					srvProp.load(in);
					for (Object prop : srvProp.keySet()) {
						String key = (String)prop;
						setup.setProperty(key, srvProp.getProperty(key));
					}
				}
			} else {
				log.warn("No server.properties file found.");
			}
		} catch (Exception e) {
			log.error("Error loading Server properties", e);
			throw new RuntimeException("Error loading Server properties", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					log.error("Failed to close the input stream!", e);
				}
			}
 		}

		ConfigurationManager.setSetup(setup);
        log.info("Configuration loaded: " + setup.getAppTitle() + " version " + setup.getVersion());
	}
	
	/**
	 * @param domain
	 * @return
	 * @throws ConfigurationException
	 */
	public static String getResourceAsString(final Domain domain, String resourceId) throws ConfigurationException {
		if(domain == null){
			throw new ConfigurationException("Domain should not be null");
		}
		
		final Resource resource = domain.getResource(resourceId);
		InputStream stream = null;
		try {
			stream = resource.getLocation().openStream();
			final String join = StringUtils.join(IOUtils.readLines(stream), "\n");
			return join;
		} catch (IOException e) {
			throw new ConfigurationException("Error in reading resource file for id "+resourceId, e);
		}finally  {
			StreamUtil.close(stream);
		}
	}
	
	/**
	 * @param domain
	 * @param resourceId
	 * @param orElse - the string that is return of an error is thorwn
	 * @return
	 */
	public static String getResourceAsStringOrElse(final Domain domain, String resourceId, String orElse){
		try {
			return getResourceAsString(domain, resourceId);
		} catch (ConfigurationException e) {
			return orElse;
		}
	}

}
