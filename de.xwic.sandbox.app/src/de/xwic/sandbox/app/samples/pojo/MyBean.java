/**
 * 
 */
package de.xwic.sandbox.app.samples.pojo;

/**
 * This is the object being manipulated for the demo case. It has a couple of simple
 * properties.
 * 
 * @author lippisch
 */
public class MyBean {

	public enum BeanColor {
		RED("#F00"),
		GREEN("#0F0"),
		BLUE("#00F"),
		BLACK("#000");
		
		private String htmlColor;
		private BeanColor(String color) {
			this.htmlColor = color;
		}
		public String getHtmlColor() {
			return htmlColor;
		}
	}
	
	private String title = "Untitled";
	private String description = null;
	private Double price = 1d;
	
	private BeanColor color = BeanColor.BLACK;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the color
	 */
	public BeanColor getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(BeanColor color) {
		this.color = color;
	} 
	
	
}
