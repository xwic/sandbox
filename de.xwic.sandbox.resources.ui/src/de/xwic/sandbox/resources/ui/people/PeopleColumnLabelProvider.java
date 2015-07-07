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

package de.xwic.sandbox.resources.ui.people;

import java.util.Locale;
import java.util.TimeZone;

import de.jwic.controls.tableviewer.CellLabel;
import de.xwic.appkit.core.config.list.ListColumn;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.DefaultColumnLabelProvider;
import de.xwic.appkit.webbase.table.RowData;

/**
 * Displays the first and last name of a person.
 * @author lippisch
 */
public class PeopleColumnLabelProvider extends DefaultColumnLabelProvider {
	
	private String[] props = null;
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.DefaultColumnLabelProvider#initialize(java.util.TimeZone, java.util.Locale, de.xwic.appkit.webbase.table.Column)
	 */
	@Override
	public void initialize(TimeZone timeZone, Locale locale, String dateFormat, String timeFormat, Column col) {
		super.initialize(timeZone, locale, dateFormat, timeFormat, col);
		String propertyId = col.getListColumn().getPropertyId();
		props = new String[] { propertyId + ".nachname", propertyId + ".vorname" }; 
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.DefaultColumnLabelProvider#getRequiredProperties(de.xwic.appkit.core.config.list.ListColumn, java.lang.String)
	 */
	@Override
	public String[] getRequiredProperties(ListColumn listColumn, String propertyId) {
		return props;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.table.DefaultColumnLabelProvider#getCellLabel(de.xwic.appkit.webbase.table.RowData)
	 */
	@Override
	public CellLabel getCellLabel(RowData row) {
		StringBuilder sb = new StringBuilder();
		for (String s : props) {
			Object value = row.getData(s);
			if (value != null && value instanceof String) {
				if (sb.length() != 0) {
					sb.append(", ");
				}
				sb.append(value);
			}
		}
		return new CellLabel(sb.toString());
	}
	
}
