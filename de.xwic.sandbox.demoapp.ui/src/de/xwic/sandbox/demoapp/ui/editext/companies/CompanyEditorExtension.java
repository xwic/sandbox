/**
 * 
 */
package de.xwic.sandbox.demoapp.ui.editext.companies;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.controls.Button;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.editors.AbstractEntityEditorExtension;
import de.xwic.appkit.webbase.editors.EditorContext;
import de.xwic.appkit.webbase.editors.EntityEditor;
import de.xwic.appkit.webbase.editors.IEntityEditorExtension;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;

/**
 * This extension demonstrates how a generic entity editor can be extended. The
 * extension needs to be configured in the <code>extension.xml</code> file in the
 * root of the source folder of this module. 
 * 
 * @author lippisch
 */
public class CompanyEditorExtension extends AbstractEntityEditorExtension implements IEntityEditorExtension, EditorListener {

	protected final static Log log = LogFactory.getLog(CompanyEditorExtension.class);
	private Button btCampaign;
	
	/**
	 * 
	 */
	public CompanyEditorExtension() {
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.IEntityEditorExtension#initialize(de.xwic.appkit.webbase.editors.EditorContext)
	 */
	@Override
	public void initialize(EditorContext context, EntityEditor editor) {
		super.initialize(context, editor);
		
		// register ourselves to get editor events
		context.addEditorListener(this);
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.IEntityEditorExtension#addActions(de.jwic.controls.ToolBar)
	 */
	@Override
	public void addActions(ToolBar toolbar) {
		ToolBarGroup group = toolbar.addGroup();
		btCampaign = group.addButton();
		btCampaign.setTitle("Start Campaign");
		btCampaign.setEnabled(!context.isNew());
		btCampaign.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				startCampaign();
			}
		});
		
	}

	/**
	 * 
	 */
	protected void startCampaign() {
		
		// start a campaign!
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.events.EditorListener#afterSave(de.xwic.appkit.webbase.editors.events.EditorEvent)
	 */
	@Override
	public void afterSave(EditorEvent event) {
		log.debug("afterSaveEvent");
		
		btCampaign.setEnabled(true);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.events.EditorListener#entityLoaded(de.xwic.appkit.webbase.editors.events.EditorEvent)
	 */
	@Override
	public void entityLoaded(EditorEvent event) {
		log.debug("entityLoaded");
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.events.EditorListener#beforeSave(de.xwic.appkit.webbase.editors.events.EditorEvent)
	 */
	@Override
	public void beforeSave(EditorEvent event) {
		log.info("beforeSave");
		
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.events.EditorListener#pagesCreated(de.xwic.appkit.webbase.editors.events.EditorEvent)
	 */
	@Override
	public void pagesCreated(EditorEvent event) {
		log.debug("afterPagesCreated");
	}

}
