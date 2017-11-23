/*
 * Generated with AppKitDev.
 */
 
package de.xwic.sandbox.crm.entities.impl;

import de.xwic.sandbox.crm.entities.*;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Product implementation class.
 */
public class Product extends Entity implements IProduct  {

	private String name;
	private String shortDescription;
	private String description;
	private IPicklistEntry category;


	/**
	 * Default constructor.
	 */
	public Product() {
	
	}
	
	/**
	 * Returns the value of name.
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Set value for name.
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the value of shortDescription.
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}
	
	/**
	 * Set value for shortDescription.
	 */
	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	/**
	 * Returns the value of description.
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set value for description.
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the value of category.
	 */
	@Override
	public IPicklistEntry getCategory() {
		return category;
	}
	
	/**
	 * Set value for category.
	 */
	@Override
	public void setCategory(IPicklistEntry category) {
		this.category = category;
	}
	

}
