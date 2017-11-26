/**
 * 
 */
package de.xwic.sandbox.app.samples.pojo;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;

/**
 * Simple viewer of a JavaBean based on a velocity template (PojoViewer.vtl).
 * @author lippisch
 */
public class PojoViewer extends Control {

	private PojoModel model;

	/**
	 * @param container
	 * @param name
	 */
	public PojoViewer(IControlContainer container, String name, PojoModel model) {
		super(container, name);
		this.model = model;
		
		// listen to the model for changes by adding an observer that
		// calls the requireRedraw method of this control. This is needed
		// so that the jWic rendering engine knows which controls to send back
		// to the client.
		model.addObserver((e, o) -> requireRedraw());
		
	}
	
	/**
	 * Returns the bean inside the model. Use by the velocity template to render
	 * the beans properties.
	 * @return
	 */
	public MyBean getMyBean() {
		return model.getMyBean();
	}

}
