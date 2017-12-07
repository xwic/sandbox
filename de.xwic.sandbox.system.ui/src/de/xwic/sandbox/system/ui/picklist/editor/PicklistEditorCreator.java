/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import de.jwic.base.IControl;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEditorCreator extends AbstractEntityEditorCreator {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator#getEditorPage(de.xwic.appkit.webbase.toolkit.app.Site, de.xwic.appkit.webbase.toolkit.editor.EditorModel)
	 */
	@Override
	protected IControl getEditorPage(Site site, EditorModel editorModel) {
		return new PicklistEditorPage(site.getContentContainer(), "picklistEditorPage", editorModel);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.editors.AbstractEntityEditorCreator#getEditorModel(de.xwic.appkit.core.dao.IEntity, de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
    protected EditorModel getEditorModel(IEntity entity, IEntity baseEntity) {
		return new PicklistEditorModel(entity);
	}

}
