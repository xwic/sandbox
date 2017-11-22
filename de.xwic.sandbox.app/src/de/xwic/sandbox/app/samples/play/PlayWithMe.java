/**
 * 
 */
package de.xwic.sandbox.app.samples.play;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Label;

/**
 * Your entry point to start playing with JWic within the Sandbox.
 * 
 * @author lippisch
 */
public class PlayWithMe extends ControlContainer {

	/**
	 * @param container
	 * @param name
	 */
	public PlayWithMe(IControlContainer container, String name) {
		super(container, name);
		
		createContent();
	}

	/**
	 * Create some controls and play with them...
	 */
	private void createContent() {

		// create a simple "Hello World" Label...
		
		Label label = new Label(this, "label");
		label.setText("Hello World!"); 
		
	}

}
