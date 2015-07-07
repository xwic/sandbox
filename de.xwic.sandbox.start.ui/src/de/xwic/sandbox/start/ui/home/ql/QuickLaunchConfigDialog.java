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
package de.xwic.sandbox.start.ui.home.ql;


import de.jwic.base.Dimension;
import de.jwic.base.Page;
import de.jwic.controls.ScrollableContainer;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.sandbox.start.ui.home.QuickLaunchModel;

/**
 * 
 * @author Raluca Geogia
 *
 */
public class QuickLaunchConfigDialog extends AbstractDialogWindow {

	private QuickLaunchModel model;

	/**
	 * @param site
	 * @param model 
	 */
	public QuickLaunchConfigDialog(Site site, QuickLaunchModel model) {
		super(site);
		this.model = model;
		setCloseable(true);
		setModal(true);
		setTitle("Quick Launch");
		
		Page page = Page.findPage(site);
		Dimension pageSize = page.getPageSize();
		setWidth(600);
		setHeight((int)(pageSize.height * 0.8d));
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {

		ScrollableContainer cnt = new ScrollableContainer(content);
		cnt.setWidth("100%");
		cnt.setHeight((getHeight() - 50) + "px");
		new QuickLaunchSelector(cnt, "qlSel", model);
		
	}
	
}
