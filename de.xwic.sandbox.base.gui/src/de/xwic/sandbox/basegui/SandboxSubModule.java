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
package de.xwic.sandbox.basegui;

import de.jwic.base.ImageRef;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * Adds some descriptive properties to a SubModule that can be displayed in the navigation or the Sandbox Home page.
 * 
 * @author Raluca Geogia
 *
 */
public abstract class SandboxSubModule extends SubModule {

	protected String description = null;
	protected String fullTitle = null;
	protected ImageRef iconLarge = null;
	protected boolean defaultQuickLaunch = false;

	/**
	 * @param site
	 */
	public SandboxSubModule(Site site) {
		super(site);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the moduleTitle
	 */
	public String getFullTitle() {
		return fullTitle;
	}

	/**
	 * @param moduleTitle
	 *            the moduleTitle to set
	 */
	public void setFullTitle(String moduleTitle) {
		this.fullTitle = moduleTitle;
	}

	/**
	 * @return the largeIcon
	 */
	public ImageRef getIconLarge() {
		return iconLarge;
	}

	/**
	 * @param largeIcon
	 *            the largeIcon to set
	 */
	public void setIconLarge(ImageRef largeIcon) {
		this.iconLarge = largeIcon;
	}

	/**
	 * @return the qlDefault
	 */
	public boolean isDefaultQuickLaunch() {
		return defaultQuickLaunch;
	}

	/**
	 * @param qlDefault
	 *            the qlDefault to set
	 */
	public void setDefaultQuickLaunch(boolean qlDefault) {
		this.defaultQuickLaunch = qlDefault;
	}
}
