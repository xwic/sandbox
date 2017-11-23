/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add Product description.
 */
public interface IProduct extends IEntity {


	public final static String PL_PRODUCT_CATEGORY = "Product.category";
	public final static String PE_PRODUCT_CATEGORY_SW = "sw";
	public final static String PE_PRODUCT_CATEGORY_PC = "pc";
	public final static String PE_PRODUCT_CATEGORY_LAPTOP = "laptop";
	public final static String PE_PRODUCT_CATEGORY_MONITOR = "monitor";

	/**
	 * Returns the value of name.
	 */
	public String getName();
	
	/**
	 * Set value for name.
	 */
	public void setName(String name);
	
	/**
	 * Returns the value of shortDescription.
	 */
	public String getShortDescription();
	
	/**
	 * Set value for shortDescription.
	 */
	public void setShortDescription(String shortDescription);
	
	/**
	 * Returns the value of description.
	 */
	public String getDescription();
	
	/**
	 * Set value for description.
	 */
	public void setDescription(String description);
	
	/**
	 * Returns the value of category.
	 */
	public IPicklistEntry getCategory();
	
	/**
	 * Set value for category.
	 */
	public void setCategory(IPicklistEntry category);
	

}
