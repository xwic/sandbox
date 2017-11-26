/**
 * 
 */
package de.xwic.sandbox.app.samples.pojo;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.Label;

/**
 * Display a form with one part that manipulates a pojo and the 2nd part that renders the 
 * pojo. This demo uses a model to handle manipulation and UI events.
 * 
 * @author lippisch
 */
public class PojoManipulateControl extends ControlContainer {

	private PojoModel model;

	/**
	 * @param container
	 * @param name
	 */
	public PojoManipulateControl(IControlContainer container, String name) {
		super(container, name);
		
		
		MyBean myBean = new MyBean();
		myBean.setTitle("Demo Bean");
		myBean.setDescription("This beautiful bean has even a nice price!");
		myBean.setPrice(20d);
		
		this.model = new PojoModel(myBean);

		createContent();
	
	}

	/**
	 * Create a label and a button. When the button is clicked, 
	 * update the label with a new message.
	 */
	private void createContent() {
		
		new PojoManipulator(this, "manipulator", model);
		new PojoViewer(this, "viewer", model);
		
	}
	
}
