/**
 * 
 */
package de.xwic.sandbox.app.samples.basics;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.Label;

/**
 * Creates a page with a couple of basic examples on how to use JWic.
 * The layout of the page is defined in the related Velocity template BasicExamples.vlt.
 * 
 * 
 * @author lippisch
 */
public class BasicExamples extends ControlContainer {

	private Label label;
	private int count = 0;

	/**
	 * @param container
	 * @param name
	 */
	public BasicExamples(IControlContainer container, String name) {
		super(container, name);

		createContent();
	
	}

	/**
	 * Create a label and a button. When the button is clicked, 
	 * update the label with a new message.
	 */
	private void createContent() {
		
		label = new Label(this, "label");
		label.setText("Nothing happened yet...");

		Button button = new Button(this, "button");
		button.setTitle("Click Me!");
		button.addSelectionListener(e -> onButtonClicked());
		
		// with Java 8, you could also write it like the below, but for better readability, we directly
		// invoke a method that performs the action...
		// button.addSelectionListener(e -> label.setText(String.format("Button was clicked %d times.", ++count)));
		
		
	}
	
	/**
	 * Handle the event when the button was clicked..
	 */
	private void onButtonClicked() {
		
		label.setText(String.format("Button was clicked %d times.", ++count));
		
	}
	
}
