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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lippisch
 *
 */
public class CookieHandler implements Filter {

	private static ThreadLocal<HttpServletRequest> trdRequest = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> trdResponse = new ThreadLocal<HttpServletResponse>();

	/**
	 * Returns the currently associated cookies.
	 * 
	 * @return
	 */
	public static Cookie[] getCookies() {
		HttpServletRequest request = trdRequest.get();
		if (request != null) {
			return request.getCookies();
		}
		return null;
	}

	/**
	 * Add a cookie to the next response.
	 * 
	 * @param cookie
	 */
	public static void addCookie(Cookie cookie) {
		HttpServletResponse response = trdResponse.get();
		if (response != null) {
			response.addCookie(cookie);
		} else {
			throw new IllegalStateException("The HttpServletResponse object is not avialalbe - Is the cookie-handler installed?");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpRes = (HttpServletResponse) res;

		try {
			preHandle(httpReq, httpRes);

			chain.doFilter(req, res);

		} finally {
			postHandle(httpReq, httpRes);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.web.ServletInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void postHandle(HttpServletRequest request, HttpServletResponse resp) {

		trdRequest.set(null);
		trdResponse.set(null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.web.ServletInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void preHandle(HttpServletRequest request, HttpServletResponse resp) {

		trdRequest.set(request);
		trdResponse.set(resp);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig cfg) throws ServletException {

	}

}
