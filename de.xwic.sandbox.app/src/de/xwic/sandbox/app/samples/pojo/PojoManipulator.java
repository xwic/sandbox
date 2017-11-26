/**
 * 
 */
package de.xwic.sandbox.app.samples.pojo;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.controls.NumericInputBox;

/**
 * 
 * @author lippisch
 */
public class PojoManipulator extends ControlContainer {

	private PojoModel model;
	private InputBox inpTitle;
	private InputBox inpDescr;
	private NumericInputBox inpPrice;

	/**
	 * @param container
	 * @param name
	 * @param model 
	 */
	public PojoManipulator(IControlContainer container, String name, PojoModel model) {
		super(container, name);
		this.model = model;
		
		createControls();
		loadDataFromModel();
		
		model.addObserver((e, o) -> onModelChanged());
	}

	/**
	 * This method handles changes to the model.
	 * @return
	 */
	private void onModelChanged() {
		// to make it simple, we just load the values from the bean again
		loadDataFromModel();
	}

	/**
	 * 
	 */
	private void loadDataFromModel() {
		
		MyBean bean = model.getMyBean();
		
		inpTitle.setText(bean.getTitle());
		inpDescr.setText(bean.getDescription());
		inpPrice.setNumber(bean.getPrice());
		
	}

	/**
	 * 
	 */
	private void createControls() {

		inpTitle = new InputBox(this, "inpTitle");
		inpTitle.setWidth(250);
		
		inpDescr = new InputBox(this, "inpDescr");
		inpDescr.setMultiLine(true);
		inpDescr.setWidth(250);
		inpDescr.setHeight(80);
		
		inpPrice = new NumericInputBox(this, "inpPrice");
		
		Button btUpdate = new Button(this, "btUpdate");
		btUpdate.setTitle("Update");
		btUpdate.addSelectionListener(e -> onUpdateModel());
		
		Button btIncr = new Button(this, "btIncr");
		btIncr.setTitle("Increase Price");
		btIncr.addSelectionListener(e -> onIncreasePrice());
		
		Button btDecr = new Button(this, "btDecr");
		btDecr.setTitle("Decrease Price");
		btDecr.addSelectionListener(e -> onDecreasePrice());
		
	}

	/**
	 * @return
	 */
	private void onDecreasePrice() {
		model.decreasePrice();
	}

	/**
	 * @return
	 */
	private void onIncreasePrice() {
		model.increasePrice();
	}

	/**
	 * @return
	 */
	private void onUpdateModel() {

		String title = inpTitle.getText();
		String descr = inpDescr.getText();
		Number price = inpPrice.getNumber();
		
		model.updateBean(title, descr, price != null ? price.doubleValue() : 0d);
		
	}

}
