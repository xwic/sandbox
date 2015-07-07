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

import java.sql.SQLException;

import de.xwic.sandbox.server.installer.impl.SQLServerDatabaseHandler;

/**
 * @author ronny
 *
 */
public class DatabaseHandlerFactory {

	public static IDatabaseHandler getDatabaseHandler(Settings settings) throws SQLException {
		//rpf: missing getting by config, atm hardcoded
		return new SQLServerDatabaseHandler(settings);
	}
}
