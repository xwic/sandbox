/**
 * 
 */
package de.xwic.sandbox.crm.ui.customer;

import de.jwic.controls.Button;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.xwic.appkit.webbase.editors.AbstractEntityEditorExtension;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.EntityEditor;
import de.xwic.appkit.webbase.editors.events.EditorAdapter;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * Extends the Customer editor with extra functionality such as an action button.
 * 
 * @author lippisch
 */
public class CustomerEditorExtension extends AbstractEntityEditorExtension {

	private Button btProductSold;

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.AbstractEntityEditorExtension#initialize(de.xwic.appkit.webbase.editors.EditorContext, de.xwic.appkit.webbase.editors.EntityEditor)
	 */
	@Override
	public void initialize(EditorContext context, EntityEditor editor) {
		super.initialize(context, editor);
		
		// add a listener to the context so we get notified when the entity was saved.
		context.addEditorListener(new EditorAdapter() {
			@Override
			public void afterSave(EditorEvent event) {
				updateButtonStates();
			}
		});
		
	}
	
	/**
	 * Update the states of the buttons after the entity was saved. 
	 */
	protected void updateButtonStates() {
		btProductSold.setEnabled(!context.isNew());
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.AbstractEntityEditorExtension#addActions(de.jwic.controls.ToolBar)
	 */
	@Override
	public void addActions(ToolBar toolbar) {
		super.addActions(toolbar);
		
		ToolBarGroup tbGroup = toolbar.addGroup();
		btProductSold = tbGroup.addButton();
		btProductSold.setTitle("Product Sold");
		btProductSold.setIconEnabled(ImageLibrary.ICON_ADD);
		btProductSold.addSelectionListener(e -> onProductSold());
		btProductSold.setEnabled(!context.isNew());
		
	}

	/**
	 * @return
	 */
	private void onProductSold() {
		
		System.out.println("Open the dialog now...");
	
	}
	
}
