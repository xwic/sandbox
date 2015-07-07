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
package de.xwic.sandbox.start.ui.home.ql;

import de.jwic.base.ImageRef;


/**
 * 
 * @author Raluca Geogia
 *
 */
public abstract class AbstractQuickLaunchFunction implements IQuickLaunchFunction{
	
	private String title;
	private String description;
	private ImageRef icon;
	private String reference;
	private boolean defaultVisible = false;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#setIcon(de.jwic.base.ImageRef)
	 */
	@Override
	public void setIcon(ImageRef icon) {
		this.icon = icon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#getIcon()
	 */
	@Override
	public ImageRef getIcon() {
		return this.icon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#setReference(java.lang.String)
	 */
	@Override
	public void setReference(String reference) {
		this.reference = reference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.start.ui.home.ql.IQuickLaunchFunction#getReference()
	 */
	@Override
	public String getReference() {
		return reference;
	}

	
	/**
	 * @return the defaultVisible
	 */
	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	
	/**
	 * @param defaultVisible the defaultVisible to set
	 */
	public void setDefaultVisible(boolean defaultVisible) {
		this.defaultVisible = defaultVisible;
	}

}
