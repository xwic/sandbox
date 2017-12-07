/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEntryLabelProvider implements ITableLabelProvider {

	public static final int COL_PICKLIST_ENTRY_KEY = 0;
	public static final int COL_PICKLIST_DISABLED = 1;
	public static final int COL_PICKLIST_SORT_INDEX = 2;
	public static final int COL_PICKLIST_KEY = 3;
	
	/* (non-Javadoc)
	 * @see de.jwic.controls.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object, de.jwic.controls.tableviewer.TableColumn, de.jwic.controls.tableviewer.RowContext)
	 */
	@Override
	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		CellLabel cellLabel = new CellLabel();
		
		if (row != null) {
			IPicklistEntry pe = (IPicklistEntry) row;
			
			switch (column.getIndex()) {
			case COL_PICKLIST_ENTRY_KEY:
				cellLabel.text = pe.getKey();
				break;
			case COL_PICKLIST_DISABLED:
				cellLabel.text = pe.isVeraltet() ? "Yes" : "No";
				break;
			case COL_PICKLIST_SORT_INDEX:
				cellLabel.text = String.valueOf(pe.getSortIndex());
				break;
			case COL_PICKLIST_KEY:
				cellLabel.text = pe.getPickliste().getKey();
				break;
			default:
				break;
			}
		}
		
		return cellLabel;
	}

}
