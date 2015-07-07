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
package de.xwic.sandbox.start.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.sandbox.start.model.entities.IQuickLaunch;

/**
 * References a module that can be opened from the home page of Sabdbox App.
 * 
 * @author Raluca Geogia
 *
 */
public class QuickLaunch extends Entity implements IQuickLaunch {

	private String username = null;
	private String reference = null;
	private int order = 0;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#getReference()
	 */
	@Override
	public String getReference() {
		return reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#setReference(java.lang.String)
	 */
	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#getOrder()
	 */
	@Override
	public int getOrder() {
		return order;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.model.entities.IQuickLaunch#setOrder(int)
	 */
	@Override
	public void setOrder(int order) {
		this.order = order;
	}
	
}
