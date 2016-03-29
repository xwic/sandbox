/**
 * 
 */
package de.xwic.sandbox.demoapp.ui.editext.companies;

import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;
import de.jwic.controls.Label;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.actions.AbstractEntityAction;
import de.xwic.appkit.webbase.actions.ICustomEntityActionCreator;
import de.xwic.appkit.webbase.actions.IEntityAction;
import de.xwic.appkit.webbase.dialog.CenteredWindow;
import de.xwic.appkit.webbase.editors.AbstractEntityEditorExtension;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.EntityEditor;
import de.xwic.appkit.webbase.editors.IEntityEditorExtension;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.sandbox.demoapp.model.entities.ICompany;
import de.xwic.sandbox.demoapp.model.entities.impl.Company;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This extension demonstrates how a generic entity editor can be extended. The
 * extension needs to be configured in the <code>extension.xml</code> file in the
 * root of the source folder of this module. 
 * 
 * @author lippisch
 */
public class CompanyUrlActionExtension implements ICustomEntityActionCreator {

	protected final static Log log = LogFactory.getLog(CompanyUrlActionExtension.class);

	/**
	 *
	 */
	public CompanyUrlActionExtension() {
	}

	@Override
	public IEntityAction createAction(Site site, String entityType, String id) {
		IEntityAction entityAction = new AbstractEntityAction() {
			@Override
			public void run() {
				Company company = (Company) entityDao.getEntity(Integer.parseInt(id));
				String js =
						"window.open('" + company.getWebSite() + "','_blank' );";
				site.getSessionContext().queueScriptCall(js);
			}
		};
		entityAction.setTitle("Show company URL");
		entityAction.setSite(site);
		entityAction.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		entityAction.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
		entityAction.setId(id);
		entityAction.setEntityDao(DAOSystem.findDAOforEntity(entityType));
		return entityAction;
	}
}