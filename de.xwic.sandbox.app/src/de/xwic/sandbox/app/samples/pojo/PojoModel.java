/**
 * 
 */
package de.xwic.sandbox.app.samples.pojo;

import java.beans.PropertyEditorSupport;

import de.jwic.util.SerObservable;
import de.xwic.sandbox.app.samples.pojo.MyBean.BeanColor;

/**
 * Underlying model for the Pojo Manipulation Demo. In jWic, it is recommended to
 * use a Model-View-Controller architecture like it is used in Rich-Client applications.
 * In this case, the model keeps the object(s) being manipulated, implements the 
 * logic to do so and provides events for UI components to update their state if the
 * underlying object was updated.
 * 
 * This model uses a simple Observable pattern, that simply provides one event - that 
 * something changed. More complex models could use a PropertyEditorSupport that handles
 * each property individually or manage events entirely on their own.  
 * 
 * @author lippisch
 */
public class PojoModel extends SerObservable {

	private MyBean bean;
	
	/**
	 * Constructor that expects a bean to be provided.
	 * @param bean
	 */
	public PojoModel(MyBean bean) {
		this.bean = bean;
	}
	
	/**
	 * Returns the underlying bean.
	 * @return
	 */
	public MyBean getMyBean() {
		return bean;
	}

	/**
	 * @param title
	 * @param descr
	 * @param d
	 */
	public void updateBean(String title, String descr, double price) {

		bean.setTitle(title);
		bean.setDescription(descr);
		bean.setPrice(price);
		bean.setColor(BeanColor.BLUE);
		
		setChanged();
		notifyObservers();	// this triggers the event to the view components that need to refresh after the model changed
		
	}
	
	/**
	 * Increase the price of our little bean.
	 */
	public void increasePrice() {
		bean.setPrice(bean.getPrice() + 1);
		bean.setColor(BeanColor.GREEN);
		
		setChanged();
		notifyObservers();	// this triggers the event to the view components that need to refresh after the model changed
		
	}
	
	/**
	 * Decrease the price of our little bean.
	 */
	public void decreasePrice() {
		bean.setPrice(Math.max(0d, bean.getPrice() - 1));
		bean.setColor(BeanColor.RED);
		
		setChanged();
		notifyObservers();	// this triggers the event to the view components that need to refresh after the model changed
		
	}

}
