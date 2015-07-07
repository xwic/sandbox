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

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.system.model.roles.editor.model.RolesEditorModel;

/**
 * @author Aron Cotrau
 * 
 */
@SuppressWarnings("serial")
public class RolesEditorPage extends InnerPage implements IEditorModelListener {

	/**
	 * @param container
	 * @param name
	 */
	public RolesEditorPage(IControlContainer container, String name, RolesEditorModel model) {
		super(container, name);

		setTitle(model.isNewEntity() ? "New Role" : DAOSystem.getDAO(IRoleDAO.class).buildTitle(model.getRole()));
		setSubtitle("Edit the rights for the role");

		model.addModelListener(this);
		new RolesEditorView(this, "editorView", model);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener#modelContentChanged(de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent
	 * )
	 */
	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
			// someone wants to close this
			Site site = ExtendedApplication.getInstance(this).getSite();
			site.popPage(this);
		}
	}
}
