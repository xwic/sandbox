/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import java.util.Iterator;

import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEntryContentProvider implements IContentProvider<IPicklistEntry> {

	PicklistEditorModel picklistModel;
	
	/**
	 * @param picklistModel
	 */
	public PicklistEntryContentProvider(PicklistEditorModel picklistModel) {
		this.picklistModel = picklistModel;
	}

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getContentIterator(de.jwic.data.Range)
	 */
	@Override
    public Iterator<IPicklistEntry> getContentIterator(Range range) {
	    return picklistModel.getPicklistEntries().iterator();
    }

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getTotalSize()
	 */
	@Override
    public int getTotalSize() {
	    return picklistModel.getPicklistEntries().size();
    }

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
    public String getUniqueKey(IPicklistEntry object) {
		IEntity entity = object;
		if (entity == null) {
			return "";
		}
		return Long.toString(entity.getId());
    }

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getObjectFromKey(java.lang.String)
	 */
	@Override
    public IPicklistEntry getObjectFromKey(String uniqueKey) {
	    return null;
    }

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#getChildren(java.lang.Object)
	 */
	@Override
    public Iterator<IPicklistEntry> getChildren(IPicklistEntry object) {
	    return null;
    }

	/* (non-Javadoc)
	 * @see de.jwic.data.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
    public boolean hasChildren(IPicklistEntry object) {
	    return false;
    }

}
