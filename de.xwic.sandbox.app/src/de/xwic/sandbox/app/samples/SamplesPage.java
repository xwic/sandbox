/**
 * 
 */
package de.xwic.sandbox.app.samples;

import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.controls.Button;
import de.jwic.controls.Tab;
import de.jwic.controls.TabStrip;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.sandbox.app.samples.basics.BasicExamples;
import de.xwic.sandbox.app.samples.pojo.MyBean;
import de.xwic.sandbox.app.samples.pojo.PojoManipulateControl;
import de.xwic.sandbox.app.samples.pojo.PojoModel;

/**
 * This page contains a collection of examples of how to use JWic.
 * 
 * @author lippisch
 */
public class SamplesPage extends InnerPage {

	private ToolBar toolbar;

	/**
	 * @param container
	 * @param name
	 */
	public SamplesPage(IControlContainer container, String name) {
		super(container, name);
		
		setTitle("JWic Sandbox & Examples");
		setSubtitle("View examples on how the UI framework works & create your own..");
		
		createContent();
		
	}

	/**
	 * Create a TabStrip with a couple of tabs that contain various demos and 
	 * a place to play around with...
	 * 
	 */
	private void createContent() {

		toolbar = new ToolBar(this, "toolbar");
		ToolBarGroup toolBarGroup = toolbar.addGroup();
		Button about = toolBarGroup.addButton();
		about.setTitle("About");
		about.setIconEnabled(new ImageRef("icons/information.png"));
		about.addSelectionListener(e -> onShowAbout());
		
		TabStrip tabStrip = new TabStrip(this, "tabStrip");
		
		Tab tab = tabStrip.addTab("Your Sandbox...");
		new Playground(tab, "playground");
		
		tab = tabStrip.addTab("Basics");
		new BasicExamples(tab, "basics");

		tab = tabStrip.addTab("Pojo Manipulator");
		new PojoManipulateControl(tab, "pojo");

	}

	/**
	 * @return
	 */
	private void onShowAbout() {

		getSessionContext().notifyMessage("You can find the examples in the sandbox-app module, package de.xwic.sandbox.app.samples...");
	
	}

}
