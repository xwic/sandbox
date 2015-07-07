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
package de.xwic.sandbox.server.installer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.util.Picklists;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Aug 2, 2013
 */
public class Evaluators {

	@SuppressWarnings("rawtypes")
	private static final Function IDENTITY_EVALUATOR = new Function() {

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public Object evaluate(final Object obj) {
			return obj;
		}
	};

	public static final Function<IPicklistEntry, String> PL_TEXT_EVAL = new Function<IPicklistEntry, String>() {

		@Override
		public String evaluate(final IPicklistEntry obj) {
			return obj.getBezeichnung("en");
		}
	};

	public static final Function<Number, Integer> NUMBER_TO_INT = new Function<Number, Integer>() {

		@Override
		public Integer evaluate(final Number obj) {
			return obj.intValue();
		}
	};

	public static final Function<IEntity, Long> VERSION_EVAL = new Function<IEntity, Long>() {

		@Override
		public Long evaluate(final IEntity obj) {
			return obj.getVersion();
		}
	};

	public static final Function<Integer, String> INT_TO_STRING = new Function<Integer, String>() {

		@Override
		public String evaluate(final Integer obj) {
			if (obj == null) {
				return null;
			}
			return obj.toString();
		}
	};

	private final static Function<? extends IEntity, Integer> ENTITY_ID = new Function<IEntity, Integer>() {

		@Override
		public Integer evaluate(final IEntity obj) {
			return obj.getId();
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IEntity> Function<T, Integer> entityIdEvaluator() {
		return (Function<T, Integer>) ENTITY_ID;
	}

	public static <E extends Exception> Function<E, String> exceptionMessageEvaluator() {
		return new Function<E, String>() {

			@Override
			public String evaluate(final E arg0) {
				return arg0.getMessage();
			}
		};
	}

	/**
	 * @param pe
	 * @return
	 */
	public static String getPicklistKey(final IPicklistEntry pe) {
		return evaluateIfNotNull(pe, Picklists.GET_KEY);
	}

	/**
	 * @param pe
	 * @return
	 */
	public static String getPicklistText(final IPicklistEntry pe) {
		return evaluateIfNotNull(pe, PL_TEXT_EVAL);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static Long getVersion(final IEntity entity) {
		return evaluateIfNotNull(entity, VERSION_EVAL);
	}

	/**
	 * @param evaluator
	 * @param o
	 * @return
	 */
	public static <T, O> T evaluateIfNotNull(final O o, final Function<O, T> evaluator) {
		if (o != null) {
			return evaluator.evaluate(o);
		}
		return null;
	}

	/**
	 * @deprecated use {@link Picklists} instead
	 */
	@Deprecated
	public static List<String> getKeysFromPicklists(Collection<IPicklistEntry> entries) {
		if (entries == null) {
			entries = new ArrayList<IPicklistEntry>();
		}
		return CollectionUtil.createList(entries, Picklists.GET_KEY);
	}

	/**
	 * @param entries
	 * @return
	 */
	public static List<String> getTextFromPicklists(Collection<IPicklistEntry> entries) {
		if (entries == null) {
			entries = new ArrayList<IPicklistEntry>();
		}
		return CollectionUtil.createList(entries, Evaluators.PL_TEXT_EVAL);
	}
}
