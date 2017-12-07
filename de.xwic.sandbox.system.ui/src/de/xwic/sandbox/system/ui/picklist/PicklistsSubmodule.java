/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistsSubmodule extends SubModule {

	/**
	 * @param site
	 */
	public PicklistsSubmodule(Site site) {
		super(site);
		setTitle("Picklists");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#createControls(de.jwic.base.IControlContainer)
	 */
	@Override
	public IControl createControls(IControlContainer container) {
		return new PicklistsPage(container, "picklistsPage");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.SubModule#getKey()
	 */
	@Override
	public String getKey() {
		return "picklists";
	}

}
