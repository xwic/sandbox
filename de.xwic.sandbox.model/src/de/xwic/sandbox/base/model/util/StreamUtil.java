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
package de.xwic.sandbox.base.model.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public class StreamUtil {

	/**
	 * @param closable
	 * @param log
	 */
	public static void close(Log log, Closeable... closables) {
		for (Closeable closable : closables) {
			if (closable != null) {
				try {
					closable.close();
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @param closable
	 * @param log
	 */
	public static void close(Closeable... closables) {
		close(LogFactory.getLog(StreamUtil.class), closables);
	}

}
