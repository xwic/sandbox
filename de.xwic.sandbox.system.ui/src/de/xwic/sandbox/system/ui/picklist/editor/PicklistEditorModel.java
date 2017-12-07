/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist.editor;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistEditorModel extends EditorModel {

	private static final Log log = LogFactory.getLog(PicklistEditorModel.class);
	public static final String LANG_EN = "en";
	public static final String LANG_DE = "de";

	private IPicklisteDAO plDao;

	/**
	 * @param entity
	 */
	public PicklistEditorModel(IEntity entity) {
		super(entity);

		plDao = DAOSystem.getDAO(IPicklisteDAO.class);
	}

	/**
	 * @return
	 */
	public IPickliste getPicklist() {
		return (IPickliste) getEntity();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<IPicklistEntry> getPicklistEntries() {
		return plDao.getAllEntriesToList(getPicklist());
	}

	/**
	 * @param id
	 * @return
	 */
	public IPicklistEntry getPicklistEntry(String id) {
		try {
			return plDao.getPickListEntryByID(Long.parseLong(id));
		} catch (NumberFormatException e) {
			log.error("Wrong picklist entry id", e);
		}
		return null;
	}

	/**
	 * @param pe
	 */
	public void savePicklistEntry(IPicklistEntry pe) {
		plDao.update(pe);
		for (IPicklistText pt : pe.getPickTextValues()) {
			plDao.update(pt);
		}
		plDao.cacheAll();
		fireModelChangedEvent(new EditorModelEvent(EditorModelEvent.AFTER_SAVE, this));
	}

	/**
	 * @return - a new unsaved PicklistEntry
	 */
	public IPicklistEntry newEntity() {
		IPicklistEntry pe = plDao.createPicklistEntry();
		pe.setPickliste(getPicklist());
		return pe;
	}

	/**
	 * @param pe
	 */
	public void deleteEntity(IPicklistEntry pe) {
		Set<IPicklistText> textValues = pe.getPickTextValues();
		if (!CollectionUtil.isEmpty(textValues)) {
			for (IPicklistText pt : textValues) {
				plDao.softDelete(pt);
			}
		}
		// if we don't close the session we get an exception when deleting the PicklistEntry because the pe would be associated with two sessions
		HibernateUtil.currentSession().close();
		plDao.softDelete(pe);

		plDao.cacheAll();
	}
}
