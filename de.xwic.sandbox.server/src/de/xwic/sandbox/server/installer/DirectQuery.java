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

package de.xwic.sandbox.server.installer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.trace.ITraceOperation;
import de.xwic.appkit.core.trace.Trace;
import de.xwic.appkit.core.util.CollectionUtil;

/**
 * Execute a direct query against Hibernate. This is used for special queries that bring huge performance gain if they pass the object
 * layer. No security checks apply, so be carefull using it.
 *
 * @author lippisch
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
	 * Use {@link #getCustomSql(Class, String, Object...)} instead.
	 */
	public static List<?> executeSQLQuery(String sql, Object... params) {

		Session session = HibernateUtil.currentSession();
		Query query = session.createSQLQuery(sql);

		setParams(query, params);

		return query.list();

	}

	/**
	 * Execute a direct SQL update.
	 * 
	 * @param hql
	 * @return
	 */
	public static int executeSQLUpdate(String sql, Object... params) {

		Session session = HibernateUtil.currentSession();
		Transaction t = session.beginTransaction();
		Query query = session.createSQLQuery(sql);

		setParams(query, params);

		int updated = query.executeUpdate();

		t.commit();

		return updated;

	}

	/**
	 * Execute a query and return the numeric value in the first row and first column of the result. If the value is non-numeric, null is
	 * returned. This is usually used to do a select count(*) from a specific table.
	 *
	 * Use {@link #getIntHql(String, Object...)} or {@link #getIntSql(String, Object...)} instead.
	 *
	 * @param string
	 * @param id
	 * @return
	 */
	public static Integer executeNumeric(String hql, Object... params) {
		List<?> result = executeQuery(hql, params);
		if (result != null && !result.isEmpty()) {
			Object row = result.get(0);
			if (row instanceof Object[]) {
				Object[] data = (Object[]) row;
				if (data.length > 0) {
					row = data[0];
				} else {
					return null;
				}
			}
			if (row instanceof Number) {
				return ((Number) row).intValue();
			}
		}
		return null;
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
	 * Execute a direct query. <br>
	 * <br>
	 *
	 * @param resultClass
	 * @param hql
	 * @param params
	 * @return
	 */
	public static <X> List<X> getCustomHql(Class<X> resultClass, String hql, Object... params) {
		return safeCast(resultClass, executeQuery(hql, params));
	}

	/**
	 * Execute a direct query. <br>
	 * <br>
	 * You may also be interested in {@link #getIntsSql(String, Object...)} and {@link #getObjectArraySql(String, Object...)}
	 *
	 * @param resultClass
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <X> List<X> getCustomSql(Class<X> resultClass, String sql, Object... params) {
		return safeCast(resultClass, executeSQLQuery(sql, params));

	}

	/**
	 * @param hql
	 * @param params
	 * @return
	 */
	public static List<Integer> getIntsHql(String hql, Object... params) {
		return getIntsFromNumbers(getCustomHql(Number.class, hql, params));
	}

	/**
	 * @param sql
	 * @param params
	 * @return
	 */
	public static List<Integer> getIntsSql(String sql, Object... params) {
		return getIntsFromNumbers(getCustomSql(Number.class, sql, params));
	}

	/**
	 * @param hql
	 * @param params
	 * @return
	 */
	public static List<Object[]> getObjectArrayHql(String hql, Object... params) {
		return getCustomHql(Object[].class, hql, params);
	}

	/**
	 * @param hql
	 * @param params
	 */
	public static List<Object[]> getObjectArraySql(String sql, Object... params) {
		return getCustomSql(Object[].class, sql, params);
	}

	/**
	 * Execute a query and return the numeric value in the first row and first column of the result. If the value is non-numeric, null is
	 * returned. This is usually used to do a select count(*) from a specific table.
	 *
	 * @param hql
	 * @param params
	 * @return
	 * @throws IllegalStateException
	 *             if the query does not return exactly one row
	 */
	public static Integer getIntHql(String hql, Object... params) throws IllegalStateException {
		return getNumber(getIntsHql(hql, params));
	}

	/**
	 * Execute a query and return the numeric value in the first row and first column of the result. If the value is non-numeric, null is
	 * returned. This is usually used to do a select count(*) from a specific table.
	 *
	 * @param sql
	 * @param params
	 * @return {@link null} if something is
	 * @throws IllegalStateException
	 *             if the query does not return exactly one row
	 */
	public static Integer getIntSql(String sql, Object... params) throws IllegalStateException {
		return getNumber(getIntsSql(sql, params));
	}

	/**
	 * @param list
	 * @return
	 * @throws IllegalStateException
	 *             if the query does not return exactly one row
	 */
	private static Integer getNumber(List<? extends Number> list) throws IllegalStateException {
		if (list.isEmpty()) {
			throw new IllegalStateException("Query did not return any result.");
		}
		if (list.size() > 1) {
			throw new IllegalStateException("Query returned more than one row.");
		}
		return list.get(0).intValue();
	}

	/**
	 * @param list
	 * @return
	 */
	private static <N extends Number> List<Integer> getIntsFromNumbers(List<N> list) {
		if (isClass(Integer.class, list)) {
			return (List<Integer>) list;
		}
		List<Integer> ints = new ArrayList();
		CollectionUtil.createCollection(list, Evaluators.NUMBER_TO_INT, ints);
		return ints;
	}

	/**
	 * @param resultClass
	 * @param list
	 * @return
	 */
	private static <X> List<X> safeCast(Class<X> resultClass, List<?> list) {
		if (list.isEmpty()) {
			return new ArrayList<X>(); // this is as far as you go, mister!
		}
		if (isClass(resultClass, list)) {
			return (List<X>) list;
		}
		throw new IllegalArgumentException(String.format("%s cannot be cast to %s", getClass(list), resultClass));
	}

	/**
	 * @return
	 */
	private static String getClass(final List<?> list) {
		if (list != null) {
			for (final Object object : list) {
				if (object != null) {
					return object.getClass().getName();
				}
			}
		}
		return null;
	}

	/**
	 * @param resultClass
	 * @param list
	 * @return
	 */
	private static <X> boolean isClass(final Class<X> resultClass, final List<?> list) {
		if (resultClass == null) {
			throw new IllegalArgumentException("Illegal result class.");
		}
		if (list.isEmpty()) {
			return true;
		}
		for (final Object cal : list) {
			if (cal != null && !resultClass.isInstance(cal)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param seq
	 * @return
	 */
	public static int getNextSequenceValue(Sequence seq) {
		if (seq == null) {
			throw new IllegalArgumentException("Missing sequence");
		}
		try {
			return getIntSql(String.format("SELECT %s.NEXTVAL FROM DUAL", seq.sequenceName));
		} catch (Exception e) {
			throw new IllegalStateException("Failed to fetch the next value from sequence " + seq.sequenceName, e);
		}
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 9, 2013
	 */
	public enum Sequence {
		TICKET("SUPPORT_TICKET_NUMBER_SEQ"), FSR_ASSIGNMENT_CODES("FSR_ASSIGNMENT_CODES_SEQ");

		private final String sequenceName;

		/**
		 * @param sequenceName
		 */
		private Sequence(String sequenceName) {
			this.sequenceName = sequenceName;
		}

		public String getSequenceName() {
			return sequenceName;
		}

	}
}
