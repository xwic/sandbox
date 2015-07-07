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

import de.jwic.base.ControlContainer;
import de.jwic.base.ImageRef;
import de.xwic.appkit.core.security.IUser;


/**
 * @author Alex Cioroianu
 *
 */
public interface IQuickLaunchFunction {
	
	/**
	 * @return
	 */
	public String getTitle();
	
	/**
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * @return
	 */
	public String getDescription() ;
	
	/**
	 * @param description
	 */
	public void setDescription(String description) ;	

	/**
	 * @param icon
	 */
	public void setIcon(ImageRef icon) ;
	
	/**
	 * @return
	 */
	public ImageRef getIcon();

	/**
	 * @param icon
	 */
	public void setReference(String icon) ;
	
	/**
	 * @return
	 */
	public String getReference();
	
	/**
	 * 
	 */
	public void run(ControlContainer quickLaunchPanel);
	
	/**
	 * @param user
	 * @return
	 */
	boolean isAvailable(IUser user);
	
	/**
	 * @return the defaultVisible
	 */
	public boolean isDefaultVisible();

	
	/**
	 * @param defaultVisible the defaultVisible to set
	 */
	public void setDefaultVisible(boolean defaultVisible);

	
}
