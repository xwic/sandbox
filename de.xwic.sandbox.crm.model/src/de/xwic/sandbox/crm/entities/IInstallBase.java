/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.entities;

import java.util.Date;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add InstallBase description.
 */
public interface IInstallBase extends IEntity {



	/**
	 * Returns the value of customer.
	 */
	public ICustomer getCustomer();
	
	/**
	 * Set value for customer.
	 */
	public void setCustomer(ICustomer customer);
	
	/**
	 * Returns the value of product.
	 */
	public IProduct getProduct();
	
	/**
	 * Set value for product.
	 */
	public void setProduct(IProduct product);
	
	/**
	 * Returns the value of serialNr.
	 */
	public String getSerialNr();
	
	/**
	 * Set value for serialNr.
	 */
	public void setSerialNr(String serialNr);
	
	/**
	 * Returns the value of purchaseDate.
	 */
	public Date getPurchaseDate();
	
	/**
	 * Set value for purchaseDate.
	 */
	public void setPurchaseDate(Date purchaseDate);
	

}
