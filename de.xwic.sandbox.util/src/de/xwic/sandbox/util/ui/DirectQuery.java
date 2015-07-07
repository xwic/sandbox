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
package de.xwic.sandbox.util.ui;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.Trace;


/**
 * @author Raluca Geogia
 *
 */
public class DirectQuery {
	/**
	 * Use {@link #getCustomHql(Class, String, Object...)} instead
	 */
	public static List<?> executeQuery(String hql, Object... params) {

		ITraceOperation traceOp = null;
		if (Trace.isEnabled()) {
			traceOp = Trace.startOperation("DirectQuery");
			traceOp.setInfo(hql);
		}

		try {
			Session session = HibernateUtil.currentSession();
			Query query = session.createQuery(hql);

			setParams(query, params);

			return query.list();
		} finally {
			if (traceOp != null) {
				traceOp.finished();
			}
		}

	}

	/**
	 * @param query
	 * @param params
	 */
	private static void setParams(Query query, Object... params) {
		if (null != params) {
			for (int i = 0; i < params.length; i++) {
				if (params[i] instanceof String) {
					query.setString(i, (String) params[i]);
				} else if (params[i] instanceof Integer) {
					query.setInteger(i, (Integer) params[i]);
				} else if (params[i] instanceof Double) {
					query.setDouble(i, (Double) params[i]);
				} else if (params[i] instanceof Date) {
					query.setTimestamp(i, (Date) params[i]);
				} else {
					throw new IllegalArgumentException("Param type not supported #" + i);
				}
			}
		}
	}

	/**
	 * Use {@link #getCustomSql(Class, String, Object...)} instead.
	 */
	public static List<?> executeSQLQuery(String sql, Object... params) {

		Session session = HibernateUtil.currentSession();
		Query query = session.createSQLQuery(sql);

		setParams(query, params);

		return query.list();

	}

}
