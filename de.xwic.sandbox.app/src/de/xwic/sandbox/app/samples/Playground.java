/**
 * 
 */
package de.xwic.sandbox.app.samples;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.xwic.sandbox.app.samples.play.PlayWithMe;

/**
 * Container for the PlayWithMe control. Also leaves some "instructions" for users
 * to find the control & know how to get started..
 * 
 * @author lippisch
 */
public class Playground extends ControlContainer {

	/**
	 * @param container
	 * @param name
	 */
	public Playground(IControlContainer container, String name) {
		super(container, name);
		
		// create the control users will play with..
		new PlayWithMe(this, "playWithMe");
		
	}

}
