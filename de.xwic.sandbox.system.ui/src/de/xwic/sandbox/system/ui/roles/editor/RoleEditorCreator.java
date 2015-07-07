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
package de.xwic.sandbox.system.ui.roles.editor;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.webbase.actions.editors.IEntityEditorCreator;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.system.model.roles.editor.model.RolesEditorModel;

/**
 * @author Adrian Ionescu
 */
public class RoleEditorCreator implements IEntityEditorCreator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.editors.IEntityEditorCreator#createAndOpenEditor(de.xwic.appkit.webbase.toolkit.app.Site,
	 * de.xwic.appkit.core.dao.IEntity, de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public EditorModel createAndOpenEditor(Site site, IEntity entity, IEntity baseEntity) {
		RolesEditorModel model = new RolesEditorModel((IRole) entity);

		RolesEditorPage page = new RolesEditorPage(site.getContentContainer(), "rolesEditor", model);
		site.pushPage(page);

		return model;
	}

}
