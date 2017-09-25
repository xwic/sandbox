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
package de.xwic.sandbox.server.installer.modules;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * Creates the admin user if the user doesn't exist and gives it the System Administrator role and Default role
 * 
 * @author Dogot Nicu
 *
 */
public class CreateAdminUser extends AbstractUpgradeModule {

	private IUserDAO userDAO;
	private IRoleDAO roleDAO;
	private IUser user;
	private IRole adminRole;
	private IRole defaultRole;
	private MessageDigest messageDigest;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.IUpgradeModule#run(de.xwic.sandbox.server .installer.InstallationManager)
	 */
	@Override
	public void run(InstallationManager manager) throws Exception {

		log.info("Creating admin...");

		userDAO = DAOSystem.getDAO(IUserDAO.class);
		roleDAO = DAOSystem.getDAO(IRoleDAO.class);

		messageDigest = MessageDigest.getInstance("MD5");

		PropertyQuery propertyQueryUser = new PropertyQuery();
		propertyQueryUser.addEquals("logonName", "admin");
		List<IUser> users = userDAO.getEntities(null, propertyQueryUser);

		if (users.isEmpty()) {

			user = userDAO.createEntity();
			user.setName("admin");
			user.setLogonName("admin");
			user.setLanguage("en");

			messageDigest.update("admin".getBytes());
			byte[] digest = messageDigest.digest();

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < digest.length; ++i) {
				sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
			}

			user.setPasswordHash(sb.toString());

			PropertyQuery propertyQueryRole = new PropertyQuery();

			List<String> rolesName = new ArrayList<String>();

			rolesName.add("System Administrator");
			rolesName.add("Default");

			propertyQueryRole.addIn("name", rolesName);

			List<IRole> roles = roleDAO.getEntities(null, propertyQueryRole);

			if (!roles.isEmpty()) {
				user.setRoles(new HashSet<IRole>(roles));
			}

			userDAO.update(user);

		} else {
			// if user exist, we check if it has the System Administrator role
			user = users.get(0);

			List<String> rolesName = new ArrayList<String>();

			rolesName.add("System Administrator");
			rolesName.add("Default");

			PropertyQuery propertyQueryRole = new PropertyQuery();
			propertyQueryRole.addIn("name", rolesName);

			List<IRole> roles = roleDAO.getEntities(null, propertyQueryRole);

			if (!roles.isEmpty()) {

				Set<IRole> userRoles = user.getRoles();

				boolean adminFlag = false;
				boolean defaultFlag = false;

				for (IRole role : roles) {
					if ("System Administrator".equals(role.getName())) {
						adminRole = role;
					} else if ("Default".equals(role.getName())) {
						defaultRole = role;
					}
				}

				for (IRole userRole : userRoles) {
					if (adminRole != null && userRole.getId() == adminRole.getId()) {
						adminFlag = true;
					} else if (defaultRole != null && userRole.getId() == defaultRole.getId()) {
						defaultFlag = true;
					}

					if (adminFlag && defaultFlag) {
						break;
					}
				}

				if ((adminRole != null && !adminFlag) || (defaultRole != null && !defaultFlag)) {
					if (adminRole != null && !adminFlag) {
						userRoles.add(adminRole);
						user.setRoles(userRoles);
					}

					if (defaultRole != null && !defaultFlag) {
						userRoles.add(defaultRole);
						user.setRoles(userRoles);
					}

					userDAO.update(user);
				}
			}
		}
	}
}
