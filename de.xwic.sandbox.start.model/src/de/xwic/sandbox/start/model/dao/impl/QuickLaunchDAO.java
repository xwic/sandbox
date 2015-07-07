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
package de.xwic.sandbox.start.model.dao.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.sandbox.start.model.dao.IQuickLaunchDAO;
import de.xwic.sandbox.start.model.entities.IQuickLaunch;
import de.xwic.sandbox.start.model.entities.impl.QuickLaunch;

/**
 * @author Raluca Geogia
 *
 */
public class QuickLaunchDAO extends AbstractDAO<IQuickLaunch, QuickLaunch> implements IQuickLaunchDAO {

	public QuickLaunchDAO() {
		super(IQuickLaunch.class, QuickLaunch.class);
	}
}
