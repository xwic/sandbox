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
package de.xwic.sandbox.model.dao.impl;

import java.util.List;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.sandbox.model.dao.INextableDAO;
import de.xwic.sandbox.util.ui.DirectQuery;

/**
 * @author Raluca Geogia
 *
 */
public abstract class AbstractNextableDAO<I extends IEntity, E extends Entity> extends AbstractDAO<I, E> implements
		INextableDAO {

	private final String SEQUENCE_SQL;

	/**
	 * @param iClass
	 * @param eClass
	 * @param seq
	 */
	public AbstractNextableDAO(final Class<I> iClass, final Class<E> eClass, final String seq) {
		super(iClass, eClass);
		SEQUENCE_SQL = "select next value for ".concat(seq);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.model.dao.INextableDAO#getNextId()
	 */
	@Override
	public int getNextId() {
		try {
			List<?> list = DirectQuery.executeSQLQuery(SEQUENCE_SQL);
			Number nb = (Number) list.get(0);
			if (nb == null) {
				throw new NullPointerException("Sequence didn't return any result!");
			}
			return nb.intValue();
		} catch (Exception e) {
			throw new RuntimeException("Could not retrieve next Action Number, please contact support!", e);
		}
	}
}
