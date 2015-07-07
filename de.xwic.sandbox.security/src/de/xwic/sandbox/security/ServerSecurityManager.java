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
package de.xwic.sandbox.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.IUserSession;
import de.xwic.appkit.core.security.UserCredential;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.security.daos.IUserSessionDAO;
import de.xwic.appkit.core.security.queries.UserQuery;

/**
 * Server side implementation of the security manager.
 * 
 * @author Florian Lippisch
 */
public class ServerSecurityManager implements ISecurityManager {

	public static final String COOKIE_USER_SESSION_KEY = "xwic.usersession.key";
	public static final long MAX_COOKIE_AGE = 1000l * 60l * 60l * 24l * 30l; // 30 days (server side check)
	public static final int COOKIE_AGE = 60 * 60 * 24 * 30; // 90 days. (cookie check, seconds based)

	private static ThreadLocal<IUser> tlUser = new ThreadLocal<IUser>();
	private static ServerSecurityManager myInstance = null;

	private Map<String, IUser> userMap = new HashMap<String, IUser>();
	private Map<String, UserCredential> credentialMap = new HashMap<String, UserCredential>();

	/**
	 * Constructor.
	 *
	 */
	public ServerSecurityManager() {
		if (myInstance != null) {
			throw new IllegalStateException("ServerSecurityManager should be instantiated only once!");
		}
		myInstance = this;
	}

	/**
	 * Returns the single instance.
	 * 
	 * @return
	 */
	public static ServerSecurityManager getInstance() {
		if (myInstance == null) {
			throw new IllegalStateException("ServerSecurityManager not initialized.");
		}
		return myInstance;
	}

	/**
	 * Trys to detect the current user from a cookie.
	 * 
	 * @return
	 */
	@Override
	public IUser detectUser() {

		Cookie[] cookies = CookieHandler.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (COOKIE_USER_SESSION_KEY.equals(cookie.getName())) {
					String key = cookie.getValue();
					IUserSessionDAO usDAO = DAOSystem.getDAO(IUserSessionDAO.class);
					IUserSession session = usDAO.getUserSession(key);
					if (session != null) {
						Date now = new Date();
						long age = now.getTime() - session.getLastAccess().getTime();
						if (age > MAX_COOKIE_AGE) {
							usDAO.delete(session);
						} else {
							session.setLastAccess(now);
							usDAO.update(session);

							IUser user = findUser(session.getUsername());
							if (user != null) {
								SessionRequestFilter.getSession().setAttribute(SessionRequestFilter.USER_SESSION_KEY, user.getLogonName());
								tlUser.set(user);
								return user;
							}
						}
					}
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getCurrentUser()
	 */
	@Override
	public IUser getCurrentUser() {

		return tlUser.get();

	}

	/**
	 * Set the current user.
	 * 
	 * @param user
	 */
	public static void setCurrentUser(String logonName) {
		if (tlUser.get() != null) {
			throw new IllegalStateException("User already set");
		}
		if (logonName != null) {
			tlUser.set(myInstance.findUser(logonName));
		} else {
			tlUser.set(null);
		}
	}

	/**
	 * Remove the current user.
	 */
	public static void clearUser() {
		tlUser.set(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#hasRight(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasRight(String scope, String action) {
		IUser user = tlUser.get();
		if (user != null) {
			UserCredential credential = getCredential(user);
			return credential.hasRight(scope, action);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#getAccess(java.lang.String, java.lang.String)
	 */
	@Override
	public int getAccess(String scope, String subscope) {
		IUser user = tlUser.get();
		if (user != null) {
			UserCredential credential = getCredential(user);
			return credential.getAccess(scope, subscope);
		}
		return NONE;
	}

	/**
	 * Find a user by its logonname.
	 * 
	 * @param logonName
	 * @return
	 */
	@Override
	public IUser findUser(String logonName) {

		if (logonName == null) {
			throw new NullPointerException("logonName must be not null.");
		}

		IUser user = userMap.get(logonName);
		if (user == null) {
			IUserDAO dao = DAOSystem.getDAO(IUserDAO.class);
			EntityList list = dao.getEntities(null, new UserQuery(logonName));
			if (!list.isEmpty()) {
				user = (IUser) list.get(0);
				userMap.put(logonName, user);
				dropCredentialFromCache(user); // make sure that the credentials are re-loaded as well.
			}
		}

		return user;
	}

	/**
	 * Returns the credential for the specified user.
	 * 
	 * @param user
	 * @return
	 */
	public UserCredential getCredential(IUser user) {

		UserCredential credential = credentialMap.get(user.getLogonName());
		if (credential == null) {
			// set the user temporary to null to "ignore" the users rights.
			IUser savedUser = tlUser.get();
			tlUser.set(null);
			credential = new UserCredential(user);
			credentialMap.put(user.getLogonName(), credential);
			// restore the saved user.
			tlUser.set(savedUser);
		}
		return credential;
	}

	/**
	 * Drops the credential from the cache, which forces the server to reload the credentials for the specified user.
	 * 
	 * @param user
	 */
	@Override
	public void dropCredentialFromCache(IUser user) {
		credentialMap.remove(user.getLogonName());
	}

	/**
	 * Drop the user from the cache.
	 * 
	 * @param user
	 */
	@Override
	public void dropUserFromCache(IUser user) {

		myInstance.userMap.remove(user.getLogonName());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logon(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean logon(String logonName, String password) {

		IUser user = findUser(logonName);
		if (user != null) {
			boolean validPassword = user.validatePassword(password);
			SessionRequestFilter.getSession().setAttribute(SessionRequestFilter.USER_SESSION_KEY, user.getLogonName());

			//if password valid -> user can be set
			if (validPassword) {
				tlUser.set(user);
			}

			return validPassword;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#rememberActiveUser()
	 */
	@Override
	public void rememberActiveUser() {

		IUser user = getCurrentUser();
		if (user == null) {
			throw new IllegalStateException("No user logged in/detected.");
		}
		// create user session
		IUserSessionDAO dao = DAOSystem.getDAO(IUserSessionDAO.class);
		IUserSession session = dao.createUserSession(user);
		Cookie cookie = new Cookie(COOKIE_USER_SESSION_KEY, session.getKey());
		cookie.setMaxAge(COOKIE_AGE);
		CookieHandler.addCookie(cookie);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.ISecurityManager#logout()
	 */
	@Override
	public void logout() {

		IUser user = getCurrentUser();
		if (user != null) {
			userMap.remove(user.getLogonName());
			dropCredentialFromCache(user);
		}
		if (SessionRequestFilter.getSession() != null) {
			SessionRequestFilter.getSession().removeAttribute(SessionRequestFilter.USER_SESSION_KEY);
		}
		clearUser();

	}

}
