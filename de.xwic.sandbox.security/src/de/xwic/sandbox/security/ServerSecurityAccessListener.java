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
package de.xwic.sandbox.security;

import de.xwic.appkit.core.access.AccessAdapter;
import de.xwic.appkit.core.access.AccessEvent;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Florian Lippisch
 */
public class ServerSecurityAccessListener extends AccessAdapter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.access.AccessAdapter#entityDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityDeleted(AccessEvent event) {
		checkEntity(event.getEntity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.access.AccessAdapter#entitySoftDeleted(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entitySoftDeleted(AccessEvent event) {
		checkEntity(event.getEntity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.access.AccessAdapter#entityUpdated(de.xwic.appkit.core.access.AccessEvent)
	 */
	public void entityUpdated(AccessEvent event) {
		checkEntity(event.getEntity());
	}

	private void checkEntity(IEntity entity) {
		if (entity instanceof IUser) {
			DAOSystem.getSecurityManager().dropUserFromCache((IUser) entity);
		}
	}

}
