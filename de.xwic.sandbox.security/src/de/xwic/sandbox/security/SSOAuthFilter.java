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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.security.IUser;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.base.model.util.ConfigurationUtil;

/**
 * @author Lippisch
 */
public class SSOAuthFilter implements Filter {

	private final Log log = LogFactory.getLog(getClass());

	private SSOHandler ssoHandler = null;
	private String baseDomain = null;
	private boolean enabled = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		if (!enabled) {
			// do not do any SSO authentication if disabled.
			chain.doFilter(request, response);
			return;
		}

		// is session authenticated?
		if (request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			HttpSession session = req.getSession();

			if (session.getAttribute(SessionRequestFilter.USER_SESSION_KEY) != null) {
				// user is authenticated -> pass through
				chain.doFilter(request, response);

			} else {
				// user is not yet authenticated ... try to detect via cookie
				IUser detectedUser = DAOSystem.getSecurityManager().detectUser();
				if (detectedUser != null) {
					// user was detected via cookie -> pass through
					log.debug("User '" + detectedUser.getName() + "' successfully detected through cookie (remote host: "
							+ req.getRemoteAddr() + ")");

					handleAlternativeUser(req, detectedUser.getLogonName());

					//SystemAccessLogger.logLogon(req.getRemoteAddr(), "Auth via Cookie");
					chain.doFilter(request, res);

				} else {

					// on localhost access can be passed through via syslogon command.
					if (req.getParameter("syslogon") != null && "127.0.0.1".equals(req.getRemoteAddr())) {
						log.info("SYSLOGON requested.");
						session.setAttribute("SYSLOGON_OVERRIDE", true);
						chain.doFilter(req, res);
						return;
					} else if (session.getAttribute("SYSLOGON_OVERRIDE") != null) {
						chain.doFilter(req, res);
						return;
					}

					// need to do SSO authentication process
					if (session.getAttribute(SSOHandler.SESSION_KEY_REQ_TOKEN) != null && "1".equals(req.getParameter("_auth"))) {

						String username = ssoHandler.checkAuthentication(req, res);
						if (username == null) {
							// authentication failed. try again 1 more time.
							if (session.getAttribute("AUTH_RETRY") != null) {
								// fall through, using app based authentication...
								log.info("User not authenticated by SSO auth. Passing on to application based authentication.");
								chain.doFilter(req, res);
							} else {
								log.debug("Retrying authentication process ...");
								session.setAttribute("AUTH_RETRY", "1");
								ssoHandler.startAuthentication(req, res);
							}
						} else {

							// logon successfull -> remove default domain (if set)
							if (baseDomain != null && username.toUpperCase().startsWith(baseDomain.toUpperCase() + "\\")) {
								username = username.substring(baseDomain.length() + 1);
							}
							log.debug("Logon successfull. Detected user: " + username);
							IUser user = ServerSecurityManager.getInstance().findUser(username);
							if (user == null) {
								//SystemAccessLogger.logLogonFailed(req.getRemoteAddr(), "User is unknown: " + username);
								res.sendRedirect("unknownuser.html");
								req.getSession().invalidate();
							} else {

								SessionRequestFilter.getSession().setAttribute(SessionRequestFilter.USER_SESSION_KEY, username);
								ServerSecurityManager.setCurrentUser(username);

								handleAlternativeUser(req, username);

								// check rights
								if (!DAOSystem.getSecurityManager().hasRight(IMitarbeiter.class.getName(), ISecurityManager.ACTION_READ)) {
									res.sendRedirect("noaccess.html");
									req.getSession().invalidate();
								} else {

									//SystemAccessLogger.logLogon(req.getRemoteAddr(), "Auth via SSO Plugin");
									ServerSecurityManager.getInstance().rememberActiveUser(); // create cookie
									chain.doFilter(req, res);

								}

							}
						}

					} else {

						// start the authentication process and do no longer forward the request.
						log.debug("Starting authentication process...");
						ssoHandler.startAuthentication(req, res);

					}

				}
			}

		} else {
			chain.doFilter(request, response);
		}

	}

	/**
	 * Logon with an alternative user. This is only available to SYSADMIN users to test the system with different credentials.
	 * 
	 * @param req
	 */
	private void handleAlternativeUser(HttpServletRequest req, String username) {

		String altUser = req.getParameter("altUser");
		if (altUser != null && !altUser.isEmpty()) {
			if (ConfigurationUtil.hasAccess(SandboxModelConfig.SCOPE_SYSADMIN)) {
				IUser aUser = ServerSecurityManager.getInstance().findUser(altUser);
				if (aUser != null) {
					log.warn("User ' " + username + "' logging on as user '" + altUser + "'.");
					ServerSecurityManager.clearUser(); // clear first	
					SessionRequestFilter.getSession().setAttribute(SessionRequestFilter.USER_SESSION_KEY, altUser);
					ServerSecurityManager.setCurrentUser(username);
				} else {
					log.warn("altUser '" + altUser + "' not found.");
				}
			} else {
				log.warn("altUser attempt by '" + username + "' denied");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		String authServerServiceUrl = config.getServletContext().getInitParameter("sso.auth.serviceurl");
		String authServerUserUrl = config.getServletContext().getInitParameter("sso.auth.userurl");
		baseDomain = config.getServletContext().getInitParameter("sso.auth.basedomain");
		String paramEnabled = config.getServletContext().getInitParameter("sso.auth.enabled");
		enabled = "true".equalsIgnoreCase(paramEnabled) || "1".equalsIgnoreCase(paramEnabled);
		if (authServerServiceUrl == null || authServerUserUrl == null) {
			throw new ServletException("sso.auth.serviceurl or sso.auth.userurl is not specified.");
		}

		ssoHandler = new SSOHandler(authServerServiceUrl, authServerUserUrl);

		if (!enabled) {
			log.warn("SSOAuthentication is disabled.");
		}

	}

}
