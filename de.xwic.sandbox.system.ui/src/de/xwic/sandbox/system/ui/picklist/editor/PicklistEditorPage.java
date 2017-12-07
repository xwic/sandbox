/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.editors.EntityEditorPage;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEditorPage extends InnerPage implements IEditorModelListener {

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public PicklistEditorPage(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		
		setTitle("Picklist Editor");
		new PicklistEditorView(this, "picklistEditorView", model);
		model.addModelListener(this);
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
