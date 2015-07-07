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
package de.xwic.sandbox.util.ui;

import de.jwic.base.IControl;
import de.jwic.base.SessionContext;
import de.xwic.appkit.core.util.VoidEvaluator;

/**
 * @author Adrian Ionescu
 */
public class NotificationUtil {

	public static final String WARNING_CSS_CLASS = "warning";
	public static final String ERROR_CSS_CLASS = "error";
	public static final String INFO_CSS_CLASS = "information";

	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showWarning(String message, SessionContext sessionContext) {
		sessionContext.notifyMessage(message, WARNING_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration
	 *            (in seconds)
	 */
	public static void showWarning(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, WARNING_CSS_CLASS, duration, 0);
	}

	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showError(String message, SessionContext sessionContext) {
		sessionContext.notifyMessage(message, ERROR_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration
	 *            (in seconds)
	 */
	public static void showError(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, ERROR_CSS_CLASS, duration, 0);
	}

	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showInfo(String message, SessionContext sessionContext) {
		sessionContext.notifyMessage(message, INFO_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration
	 *            (in seconds)
	 */
	public static void showInfo(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, INFO_CSS_CLASS, duration, 0);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Sep 7, 2013
	 */
	public static class LazyNotificationUtil extends VoidEvaluator<String> {

		private final SessionContext sessionContext;

		/**
		 * @param c
		 */
		public LazyNotificationUtil(IControl c) {
			if (c == null) {
				throw new IllegalStateException("No such control");
			}
			sessionContext = c.getSessionContext();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.xwic.appkit.core.util.VoidEvaluator#evaluateNoResult(java.lang.Object)
		 */
		@Override
		public void evaluateNoResult(String obj) {
			showError(obj, sessionContext);
		}

	}

}
