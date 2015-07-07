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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles SSO authentication with auth server.
 * 
 * @author Lippisch
 */
public class SSOHandler {

	public static final String SESSION_KEY_REQ_TOKEN = "reqtoken";

	private String authServerServiceUrl = null;
	private String authServerUserUrl = null;

	/**
	 * Initialize the handler.
	 * 
	 * @param authServerServiceUrl
	 * @param authServerUserUrl
	 */
	public SSOHandler(String authServerServiceUrl, String authServerUserUrl) {
		this.authServerServiceUrl = authServerServiceUrl;
		this.authServerUserUrl = authServerUserUrl;
	}

	/**
	 * Start the authentication.
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	public void startAuthentication(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String myToken = "ME" + new Random().nextInt(Integer.MAX_VALUE) + "_" + req.getRemoteAddr();
		String pubToken = invokeUrl(new URL(authServerServiceUrl + "?a=sa&token=" + myToken));

		String selfAddr = req.getScheme() + "://" + req.getServerName() + (req.getServerPort() != 80 ? ":" + req.getServerPort() : "")
				+ req.getRequestURI();
		if (req.getQueryString() != null) {
			selfAddr += "?" + req.getQueryString() + "&_auth=1";
		} else {
			selfAddr += "?_auth=1";
		}
		req.getSession().setAttribute(SESSION_KEY_REQ_TOKEN, myToken);

		String redir = authServerUserUrl + "?a=auth&token=" + pubToken + "&return=" + URLEncoder.encode(selfAddr, "UTF-8");

		res.sendRedirect(redir);

	}

	/**
	 * Return the identified username.
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws IOException
	 */
	public String checkAuthentication(HttpServletRequest req, HttpServletResponse res) throws IOException {

		String myToken = (String) req.getSession().getAttribute(SESSION_KEY_REQ_TOKEN);
		try {
			if (myToken != null) {
				req.getSession().removeAttribute(SESSION_KEY_REQ_TOKEN);
				String username = invokeUrl(new URL(authServerServiceUrl + "?a=reqauth&token=" + myToken));
				return username;
			}
		} catch (Exception e) {
			// the authentication failed.
		}
		return null;

	}

	/**
	 * Read data from a URL;
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private String invokeUrl(URL url) throws IOException {

		InputStream in = url.openStream();
		StringBuilder sb = new StringBuilder();
		BufferedInputStream bin = new BufferedInputStream(in);
		int i;
		while ((i = bin.read()) != -1) {
			sb.append((char) i);
		}
		return sb.toString();
	}

}
