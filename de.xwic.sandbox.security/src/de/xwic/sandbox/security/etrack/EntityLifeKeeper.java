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

package de.xwic.sandbox.security.etrack;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.hibernate.LockMode;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.sandbox.security.SessionRequestFilter;

/**
 * Tracks a list of entities that is being "used" by the current session
 * an needs to be re-bound to the HibernateSession on each request.
 * One instance of the life keeper will be bound to each HttpSession.
 * @author Lippisch
 */
public class EntityLifeKeeper {

	public static final String SESSION_KEY = "EntityLifeKeeper";
	
	public Map<String, EntityRefTrack> entities = new HashMap<String, EntityRefTrack>();
	
	/**
	 * 
	 * @param entity
	 */
	public static void registerEntity(IEntity entity) {
		
		getEntityLifeKeeper()._registerEntity(entity);
	}
	
	/**
	 * @param entity
	 */
	private void _registerEntity(IEntity entity) {

		getEntityRefTrak(entity).addReference();

	}

	/**
	 * 
	 * @param entity
	 */
	public static void deregisterEntity(IEntity entity) {
		
		getEntityLifeKeeper()._deregisterEntity(entity);
		
	}
	
	/**
	 * @param entity
	 */
	private void _deregisterEntity(IEntity entity) {
		
		EntityRefTrack et = getEntityRefTrak(entity);
		if (et.removeReference() == 0) {
			// no longer referenced - remove
			String key = entity.getClass().getName() + "#" + entity.getId();
			entities.remove(key);
		}
		
	}

	/**
	 * Return the EntityRefTrak for this entity. It will be created if it
	 * does not exist yet.
	 * @param entity
	 * @return
	 */
	private EntityRefTrack getEntityRefTrak(IEntity entity) {

		String key = entity.getClass().getName() + "#" + entity.getId();
		EntityRefTrack et = entities.get(key);
		if (et == null) {
			et = new EntityRefTrack(entity);
			entities.put(key, et);
		}
		return et;
		
	}
	
	/**
	 * Retrieve the instance from the session.
	 * @return
	 */
	private static EntityLifeKeeper getEntityLifeKeeper() {
		HttpSession session = SessionRequestFilter.getSession();
		if (session == null) {
			throw new IllegalStateException("HttpSession is not available");
		}
		
		EntityLifeKeeper self = (EntityLifeKeeper)session.getAttribute(SESSION_KEY);
		if (self == null) {
			self = new EntityLifeKeeper();
			session.setAttribute(SESSION_KEY, self);
		}
		return self;
	}

	/**
	 * @param session
	 */
	public static void refreshEntities(HttpSession session) {
		EntityLifeKeeper self = (EntityLifeKeeper)session.getAttribute(SESSION_KEY);
		if (self != null) {
			self._refreshEntities();
		}
		
	}

	/**
	 * Refresh entities.
	 */
	private void _refreshEntities() {
		
		if (entities.size() > 0) {
			Session session = HibernateUtil.currentSession();
			for (EntityRefTrack et : entities.values()) {
				session.lock(et.getEntity(), LockMode.NONE);
			}
		}
	}
}
