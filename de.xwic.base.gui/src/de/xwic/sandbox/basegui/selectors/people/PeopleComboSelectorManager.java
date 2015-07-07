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
package de.xwic.sandbox.basegui.selectors.people;

import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * @author Raluca Geogia
 *
 */
public class PeopleComboSelectorManager {

	/**
	 * @return
	 */
	public static PeopleComboFactory getFactory() {
		// AI 09-Dec-2013: we add a filter anyway, to exclude the special accounts 

		PropertyQuery baseQuery = new PropertyQuery();
		baseQuery.addEquals("specialAccount", false);

		return new PeopleComboFactory(baseQuery);
	}

	/**
	 * @param baseQuery
	 * @return
	 */
	public static PeopleComboFactory getFactory(PropertyQuery baseQuery) {
		return new PeopleComboFactory(baseQuery);
	}
}
