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
package de.xwic.sandbox.basegui.entityselection;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.model.IModelListener;
import de.xwic.appkit.webbase.toolkit.model.ModelEvent;


/**
 * @author Raluca Geogia
 *
 */
public class EntitySelectionPage extends InnerPage implements IModelListener {

	/**
	 * @param container
	 * @param name
	 */
	public EntitySelectionPage(IControlContainer container, String name, IEntitySelectionContributor contributor) {
		super(container, name);
		if (null == contributor.getSelectionModel()) {
			throw new IllegalArgumentException("Selection Model is not allowed to be null");
		}

		if (null == contributor.getListModel()) {
			throw new IllegalArgumentException("List Model is not allowed to be null");
		}

		contributor.getSelectionModel().addModelListener(this);
		setTitle(contributor.getPageTitle());
		setSubtitle(contributor.getPageSubTitle());

		new EntitySelectionView(this, "entitySelectionView", contributor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.model.IModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.model.ModelEvent)
	 */
	@Override
	public void modelContentChanged(ModelEvent event) {
		if (EntitySelectionModel.CLOSE_ENTITY_SELECTION == event.getEventType()) {
			// someone wants to close this
			Site site = ExtendedApplication.getInstance(this).getSite();
			site.popPage(this);
		}
	}

}
