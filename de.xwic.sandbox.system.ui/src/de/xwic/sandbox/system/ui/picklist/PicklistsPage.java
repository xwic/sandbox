/**
 * 
 */
package de.xwic.sandbox.system.ui.picklist;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;

/**
 * @author Claudiu Mateias
 *
 */
public class PicklistsPage extends InnerPage {

	/**
	 * @param container
	 * @param name
	 */
	public PicklistsPage(IControlContainer container, String name) {
		super(container, name);
		
		setTitle("Picklists");
        setSubtitle("Picklists Management");
        
        PropertyQuery defaultQuery = new PropertyQuery();
        defaultQuery.setSortField("key");
        
        EntityListViewConfiguration config = new EntityListViewConfiguration(IPickliste.class);
        config.setDefaultFilter(defaultQuery);
        
        try {
			new EntityListView<IPickliste>(this, config);
		} catch (ConfigurationException e) {
			throw new RuntimeException("Can not create EntityTable: " + e, e);
		}
	}

}
