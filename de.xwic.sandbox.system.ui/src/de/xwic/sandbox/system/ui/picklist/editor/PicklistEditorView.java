/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.webbase.toolkit.components.BaseView;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEditorView extends BaseView {

	private InputBox ibKey;
	private InputBox ibTitle;
	private InputBox ibLinkedProperty;
	private PicklistEntryGroupControl grpPe;
	private EditorModel model;

	private Button btClose = null;
	
	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public PicklistEditorView(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		this.model = model;
		createControls();
		populateDefaults(((PicklistEditorModel)model).getPicklist());
	}

	/**
	 * 
	 */
	protected void createControls() {
		setupActionBar();
		ibKey = createInputBox("ibKey", 350);
		ibTitle = createInputBox("ibTitle", 350);
		ibLinkedProperty = createInputBox("ibLinkedProperty", 500);
		
		grpPe = new PicklistEntryGroupControl(this, "grpPe", (PicklistEditorModel)model);
		grpPe.setTitle("Picklist Entries");

	}
	
	/**
	 * @param picklist
	 */
	private void populateDefaults(IPickliste picklist) {
		ibKey.setText(picklist.getKey());
		ibTitle.setText(picklist.getTitle());
		ibLinkedProperty.setText(picklist.getBeschreibung());
	}
	
	/**
	 * @param name
	 */
	private InputBox createInputBox(String name, int width) {
		InputBox ib = new InputBox(this, name);
		ib.setEnabled(false);
		ib.setWidth(width);
		
		return ib;
	}
	
	/**
	 * Setup the action bar
	 */
	private void setupActionBar() {
		ToolBarGroup tg = toolbar.addGroup();

		btClose = tg.addButton();
		btClose.setTitle("Close");
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
	 * closes the editor
	 */
	protected void performClose() {
		try {
			model.close();
		} catch (Exception re) {
			showError(re);
		}
	}

}
