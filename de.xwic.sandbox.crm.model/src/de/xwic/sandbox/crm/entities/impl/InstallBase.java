/*
 * Generated with AppKitDev.
 */
 
package de.xwic.sandbox.crm.entities.impl;

import java.util.Date;
import de.xwic.sandbox.crm.entities.*;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * InstallBase implementation class.
 */
public class InstallBase extends Entity implements IInstallBase  {

	private ICustomer customer;
	private IProduct product;
	private String serialNr;
	private Date purchaseDate;


	/**
	 * Default constructor.
	 */
	public InstallBase() {
	
	}
	
	/**
	 * Returns the value of customer.
	 */
	@Override
	public ICustomer getCustomer() {
		return customer;
	}
	
	/**
	 * Set value for customer.
	 */
	@Override
	public void setCustomer(ICustomer customer) {
		this.customer = customer;
	}
	
	/**
	 * Returns the value of product.
	 */
	@Override
	public IProduct getProduct() {
		return product;
	}
	
	/**
	 * Set value for product.
	 */
	@Override
	public void setProduct(IProduct product) {
		this.product = product;
	}
	
	/**
	 * Returns the value of serialNr.
	 */
	@Override
	public String getSerialNr() {
		return serialNr;
	}
	
	/**
	 * Set value for serialNr.
	 */
	@Override
	public void setSerialNr(String serialNr) {
		this.serialNr = serialNr;
	}
	
	/**
	 * Returns the value of purchaseDate.
	 */
	@Override
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	
	/**
	 * Set value for purchaseDate.
	 */
	@Override
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	

}
