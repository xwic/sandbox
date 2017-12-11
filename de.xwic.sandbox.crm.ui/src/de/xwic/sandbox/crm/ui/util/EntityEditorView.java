/**
 * 
 */
package de.xwic.sandbox.crm.ui.util;

import org.hibernate.StaleObjectStateException;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.util.StringUtil;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;
import de.xwic.appkit.webbase.toolkit.components.BaseView;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Aron Cotrau
 */
public abstract class EntityEditorView extends BaseView implements IEditorModelListener {

	public static final String EDITOR_ACTION_CLOSE = "Close";
	public static final String EDITOR_ACTION_SAVE = "Save";
	public static final String PAGE_EDITOR_SAVE_DONE = "Entity was saved successfully";
	
	protected EditorToolkit toolkit;
	protected EditorModel model;

	protected Button btSave = null;
	protected Button btClose = null;


	/**
	 * @param container
	 * @param name
	 */
	public EntityEditorView(IControlContainer container, String name, EditorModel model) {
		super(container, name);

		toolkit = new EditorToolkit(model);
		this.model = model;

		model.addModelListener(this);

		setupActionBar();
		createControls();

		btSave.setVisible(model.isEditable());

		toolkit.loadFieldValues();
	}

	/**
	 * Setup the action bar
	 */
	protected void setupActionBar() {
		ToolBarGroup tg = toolbar.addGroup();

		btSave = tg.addButton();
		btSave.setTitle(EDITOR_ACTION_SAVE);
		btSave.setIconEnabled(ImageLibrary.IMAGE_SAVE_ACTIVE_SMALL);
		btSave.setIconDisabled(ImageLibrary.IMAGE_SAVE_INACTIVE_SMALL);
		btSave.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				performSave(isShowInfoAfterSave());
			}
		});

		btClose = tg.addButton();
		btClose.setTitle(EDITOR_ACTION_CLOSE);
		btClose.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btClose.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);
		btClose.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				performClose();
			}
		});
	}

	/**
	 * creates the controls
	 */
	protected abstract void createControls();

	/**
	 * closes the editor
	 */
	protected void performClose() {
		try {
			model.close();
		} catch (Exception re) {
			showError(re);
		}
	}

	/**
	 * performs save
	 */
	protected void performSave(boolean showInfo) {
		try {
			model.validateEntity();

			model.saveEntity();
			if (showInfo) {
				showInfo(PAGE_EDITOR_SAVE_DONE);
			} else {
				// direct close here
				model.close();
			}
		} catch (StaleObjectStateException e) {
			processStaleObjectException(e);
		} catch (Exception re) {
			processException(re);

		}
	}

	/**
	 * @param re
	 */
	public void processException(Exception re) {
		if (re instanceof ValidationException) {
			ValidationResult res = ((ValidationException) re).getResult();
			
			String err = DaoValidationUtil.extractValidationMessages(model.getEntity(), res);
			if (!StringUtil.isEmpty(err)) {
				showError(err);
			}
		} else {
			if (null != re.getMessage()) {
				showError(re.getMessage());
			} else {
				showError(re);
			}
		}
	}

	/**
	 * This is the default process but this method should be overridden by the Views to process the StaleObjectException properly
	 * @see FieldSupportRequestFormView
	 * @param e
	 */
	protected void processStaleObjectException(StaleObjectStateException e) {
		processException(e);
	}

	/**
	 * @return whether or not the save should be
	 */
	protected boolean isShowInfoAfterSave() {
		return true;
	}

	/**
	 * @return the toolkit
	 */
	public EditorToolkit getToolkit() {
		return toolkit;
	}

	/**
	 * @param toolkit the toolkit to set
	 */
	public void setToolkit(EditorToolkit toolkit) {
		this.toolkit = toolkit;
	}

	/**
	 * 
	 */
	@Override
	public void modelContentChanged(EditorModelEvent event) {
		if (EditorModelEvent.VALIDATION_REQUEST == event.getEventType()) {
			toolkit.saveFieldValues();
		}
	}
}
