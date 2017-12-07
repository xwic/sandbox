/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.GroupControl;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEntryGroupControl extends GroupControl implements IEditorModelListener {

	private PicklistEditorModel editorModel;
	
	private ErrorWarning errorWarning;
	private TableViewer peTable;
	private PicklistEntryEditorControl peEditor;
	private Button btnNew;
	private Button btnDelete;
	
	private TableModel tableModel;
	private ElementSelectedListener tableListener = new ElementSelectedListener() {
		@Override
		public void elementSelected(ElementSelectedEvent event) {
			updatePicklistEntryEditorControl();
		}
	};
	
	/**
	 * @param container
	 * @param name
	 * @param editorModel
	 */
	public PicklistEntryGroupControl(IControlContainer container, String name, PicklistEditorModel editorModel) {
		super(container, name);
		this.editorModel = editorModel;
		editorModel.addModelListener(this);
		createControls();
	}
	
	/**
	 * 
	 */
	private void createControls() {
		
		errorWarning = new ErrorWarning(this, "errorWarning");
		peEditor = new PicklistEntryEditorControl(this, "peEditor", editorModel);
				
		btnNew = new Button(this, "btnNew");
		btnNew.setTitle("New Picklist Entry");
		btnNew.setTooltip("Create a new Picklist Entry under this Picklist");
		btnNew.setIconEnabled(ImageLibrary.IMAGE_ADD);
		btnNew.addSelectionListener(new SelectionListener() {
			
			@Override
			public void objectSelected(SelectionEvent event) {
				onAddNew();
			}
		});
		
		btnDelete = new Button(this, "btnDelete");
		btnDelete.setTitle("Delete");
		btnDelete.setEnabled(false);
		btnDelete.setTooltip("Delete the selected Picklist Entry");
		btnDelete.setIconEnabled(ImageLibrary.IMAGE_CANCEL_ACTIVE);
		btnDelete.setIconDisabled(ImageLibrary.IMAGE_CANCEL_INACTIVE);
		btnDelete.addSelectionListener(new SelectionListener() {
			
			@Override
			public void objectSelected(SelectionEvent event) {
				onDelete();
			}
		});
		
		createPicklistEntryControls();
	}

	/**
	 * 
	 */
    protected void createPicklistEntryControls() {
	    peTable = new TableViewer(this, "peTable");
	    peTable.setHeight(400);
		peTable.setScrollable(true);
		peTable.setResizeableColumns(true);
		peTable.setContentProvider(new PicklistEntryContentProvider(editorModel));
		peTable.setTableLabelProvider(new PicklistEntryLabelProvider());
		
		tableModel = peTable.getModel();
		tableModel.setSelectionMode(TableModel.SELECTION_SINGLE);
		tableModel.setMaxLines(-1);
		tableModel.addColumn(new TableColumn("Picklist Entry Key", 300));
		tableModel.addColumn(new TableColumn("Disabled", 60));
		tableModel.addColumn(new TableColumn("Sort Index", 70));
		tableModel.addElementSelectedListener(tableListener);
    }
	
	/**
	 * @param pe
	 */
	private void updatePicklistEntryEditorControl() {
		IPicklistEntry pe = editorModel.getPicklistEntry(tableModel.getFirstSelectedKey());
		if (pe == null) {
			errorWarning.showError("Could not load Picklist Editor Control");
			return;
		}
		
		peEditor.loadData(pe);
		btnDelete.setEnabled(true);
	}
	
	/**
	 * 
	 */
	private void onAddNew() {
		peEditor.loadNew();
		
		// we need to remove the listener because clearSelection triggers the ElementSelectedListener
		// which would try to load the PicklistEntityEditorControl
		tableModel.removeElementSelectedListener(tableListener);
		tableModel.clearSelection();
		tableModel.addElementSelectedListener(tableListener);
		btnDelete.setEnabled(false);
		peTable.requireRedraw();
	}

	/**
	 * 
	 */
	private void onDelete() {
		try {
			editorModel.deleteEntity(editorModel.getPicklistEntry(tableModel.getFirstSelectedKey()));
			peEditor.clearData();
			errorWarning.showWarning("Picklist Entry deleted");
		} catch (DataAccessException e) {
			errorWarning.showError("Cannot delete the Picklist Entry because it is referenced by other entities. You can choose to disable the Picklist Entry. <br> " + e.getMessage());
		}
		refresh();
	}
	
	private void refresh() {
		peTable.requireRedraw();
	}
	


	@Override
	public void modelContentChanged(EditorModelEvent event) {
		refresh();
	}

}
