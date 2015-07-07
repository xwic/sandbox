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
package de.xwic.sandbox.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.sandbox.base.model.util.RemoteClientsCache;
import de.xwic.sandbox.base.model.util.RemoteClientsCache.RemoteClient;
import de.xwic.sandbox.security.etrack.EntityLifeKeeper;

/**
 * @author Florian Lippisch
 *
 */
public class SessionRequestFilter implements Filter {

	private static final Log log = LogFactory.getLog(SessionRequestFilter.class);

	public static final String USER_SESSION_KEY = "xwic_username";

	private static ThreadLocal<HttpSession> tlSession = new ThreadLocal<HttpSession>();

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
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {

		try {

			HttpServletRequest httpReq = (HttpServletRequest) req;
			HttpSession session = httpReq.getSession();
			tlSession.set(session);

			String userName = (String) session.getAttribute(USER_SESSION_KEY);

			if (userName == null) {

				// check for a remote system id
				String remoteSystemId = httpReq.getParameter(RemoteDataAccessServlet.PARAM_RSID);

				if (remoteSystemId != null) {

					RemoteClient client = RemoteClientsCache.instance().getRemoteClient(remoteSystemId);

					if (client != null) {

						userName = httpReq.getParameter(RemoteDataAccessServlet.PARAM_USERNAME);

						if (userName == null) {
							// no username sent in the request, use the client's default username
							userName = client.getUsername();
						}

					} else {
						log.warn("Unknown remote system tried to connect: " + remoteSystemId);
						throw new IllegalStateException("Unknown remote system: " + remoteSystemId);
					}

				}
			}

			ServerSecurityManager.setCurrentUser(userName);

			EntityLifeKeeper.refreshEntities(session);

			if (Trace.isEnabled()) {
				ITraceContext tx = Trace.getTraceContext();
				tx.setAttribute("username", userName);
			}

			fc.doFilter(req, res);

		} finally {
			tlSession.set(null);
			ServerSecurityManager.clearUser();
			try {
				HibernateUtil.closeSession();
			} catch (Throwable t) {
				log.info("Error closing session: " + t);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	/**
	 * Returns the session bound to the current thread.
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		return tlSession.get();
	}

}
