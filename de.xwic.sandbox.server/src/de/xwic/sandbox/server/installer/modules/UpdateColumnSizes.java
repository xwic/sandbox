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
package de.xwic.sandbox.server.installer.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Table;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.DbColumn;
import de.xwic.sandbox.server.installer.IDatabaseHandler;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * The Module checks the size of VARCHAR columns and increases them if needed.
 * 
 * @author Florian Lippisch
 */
public class UpdateColumnSizes extends AbstractUpgradeModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#run(de.xwic.appkit.core.installer.InstallationManager)
	 */
	public void run(InstallationManager manager) throws Exception {

		IDatabaseHandler dbHandler = manager.getDatabaseHandler();

		Configuration cfg = HibernateUtil.getConfiguration();

		for (Iterator<?> it = cfg.getTableMappings(); it.hasNext();) {

			Table table = (Table) it.next();
			List<DbColumn> list = dbHandler.getTableColumns(table.getName());
			// put it into a map for easier access
			Map<String, DbColumn> map = new HashMap<String, DbColumn>();
			for (Iterator<DbColumn> itL = list.iterator(); itL.hasNext();) {
				DbColumn col = itL.next();
				map.put(col.getName(), col);
			}

			for (Iterator<?> itC = table.getColumnIterator(); itC.hasNext();) {
				Column column = (Column) itC.next();
				DbColumn dbCol = map.get(column.getName());
				if (dbCol != null && "varchar".equalsIgnoreCase(dbCol.getDataType()) && dbCol.getLength() != column.getLength()) {
					if (dbCol.getLength() > column.getLength()) {
						log.warn("The column '" + column.getName() + "' in table '" + table.getName() + "' is larger (" + dbCol.getLength()
								+ ") then specified (" + column.getLength() + ") in the mapping. The column size is not modified. ");
					} else {
						log.info("Changing column size for '" + table.getName() + "." + column.getName() + "' from " + dbCol.getLength()
								+ " to " + column.getLength());
						dbHandler.alterColumn(table.getName(), dbCol.getName(), "VARCHAR(" + column.getLength() + ")");
					}
				}
			}

		}

	}

}
