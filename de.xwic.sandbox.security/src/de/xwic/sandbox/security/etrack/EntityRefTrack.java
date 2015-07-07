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

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Lippisch
 */
public class EntityRefTrack {
	
	private IEntity entity;
	private int refCount = 0;
	
	public EntityRefTrack(IEntity entity) {
		this.entity = entity;
		refCount = 0;
	}
	
	/**
	 * Add a reference
	 * @return
	 */
	public int addReference() {
		refCount++;
		return refCount;
	}
	
	/**
	 * Remove a reference.
	 * @return
	 */
	public int removeReference() {
		refCount--;
		return refCount;
	}

	/**
	 * @return the entity
	 */
	public IEntity getEntity() {
		return entity;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entity == null) ? 0 : entity.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		EntityRefTrack other = (EntityRefTrack) obj;
		if (entity == null) {
			if (other.entity != null){
				return false;
			}
		} else if (!entity.equals(other.entity)){
			return false;
		}
		return true;
	}

}
