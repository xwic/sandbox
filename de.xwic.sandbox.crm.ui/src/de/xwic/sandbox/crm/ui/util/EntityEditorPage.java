/**
 *
 */
package de.xwic.sandbox.crm.ui.util;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;

/**
 *
 * @author Aron Cotrau
 */
public abstract class EntityEditorPage extends InnerPage implements IEditorModelListener {

	private EditorModel model;

	/**
	 * @param container
	 * @param name
	 */
	public EntityEditorPage(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		this.model = model;
		init();
		model.addModelListener(this);

		boolean isEditingEntity = model.getEntity() != null ? model.getEntity().getId() > 0 : false;
		String title = isEditingEntity ? model.getEntityDAO().buildTitle(model.getEntity()) : getNewTitle();
		String subTitle = getPageSubtitle();

		setTitle(title);
		setSubtitle(subTitle);

		createControls(model);
	}

	/** creates the controls */
	protected abstract void createControls(EditorModel model);

	/** returns the subtitle of the page */
	protected abstract String getPageSubtitle();

	/** returns the page name when a new entity in editing */
	protected abstract String getNewTitle();

	/**
	 * method called before the object is added as a listener
	 */
	protected void init() {

	}

	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
			// someone wants to close this
			Site site = ExtendedApplication.getInstance(this).getSite();
			site.popPage(this);
		}
	}

	public EditorModel getModel() {
		return model;
	}

}
