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
package de.xwic.sandbox.start.ui.home;

import de.jwic.base.ImageRef;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import de.xwic.sandbox.basegui.SandboxSubModule;

/**
 * 
 * @author Raluca Geogia
 *
 */
public class QuickLaunchUIItem {

	private String group = null;
	private String title = null;
	private String content = null;
	private ImageRef icon = null;
	private String reference = null;
	private int quickLinkId = 0;
	
	private int tempId = 0;
	private boolean mostCommon = false;
	
	/**
	 * @param sm
	 */
	public QuickLaunchUIItem(SubModule sm) {
		
		if (sm instanceof SandboxSubModule) {
			SandboxSubModule psm = (SandboxSubModule) sm;
			String qlTitle = psm.getFullTitle();
			if (qlTitle == null) {
				qlTitle = psm.getTitle();
			}
			setTitle(qlTitle);
			setContent(psm.getDescription());
			setIcon(psm.getIconLarge());
			setMostCommon(true);
		} else {
			setTitle(sm.getTitle());
		}
		
	}
	
	/**
	 * 
	 */
	public QuickLaunchUIItem() {
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the icon
	 */
	public ImageRef getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(ImageRef icon) {
		this.icon = icon;
	}
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	/**
	 * @return the quickLinkId
	 */
	public int getQuickLinkId() {
		return quickLinkId;
	}
	/**
	 * @param quickLinkId the quickLinkId to set
	 */
	public void setQuickLinkId(int quickLinkId) {
		this.quickLinkId = quickLinkId;
	}
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the tempId
	 */
	public int getTempId() {
		return tempId;
	}
	/**
	 * @param tempId the tempId to set
	 */
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	/**
	 * @return the mostCommon
	 */
	public boolean isMostCommon() {
		return mostCommon;
	}
	/**
	 * @param mostCommon the mostCommon to set
	 */
	public void setMostCommon(boolean mostCommon) {
		this.mostCommon = mostCommon;
	}
	
	
	

}
