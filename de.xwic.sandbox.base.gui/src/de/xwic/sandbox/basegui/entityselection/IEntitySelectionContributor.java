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

import de.xwic.sandbox.basegui.entityview.EntityDisplayListModel;
import de.xwic.sandbox.basegui.util.IQuickSearchPanel;


/**
 * @author Raluca Geogia
 *
 */
public interface IEntitySelectionContributor {

	public String getPageTitle();

	public String getPageSubTitle();

	public String getViewTitle();

	public String getListSetupId();

	public IQuickSearchPanel createQuickSearchPanel();

	public EntitySelectionModel getSelectionModel();

	public EntityDisplayListModel getListModel();
}