/*
 * Generated with AppKitDev.
 */
 
package de.xwic.sandbox.crm.entities.impl;

import de.xwic.sandbox.crm.entities.*;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Contact implementation class.
 */
public class Contact extends Entity implements IContact  {

	private ICustomer customer;
	private String name;
	private String email;
	private String phone;
	private String mobile;
	private String fax;
	private IPicklistEntry role;


	/**
	 * Default constructor.
	 */
	public Contact() {
	
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
	 * Returns the value of email.
	 */
	@Override
	public String getEmail() {
		return email;
	}
	
	/**
	 * Set value for email.
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns the value of phone.
	 */
	@Override
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Set value for phone.
	 */
	@Override
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * Returns the value of mobile.
	 */
	@Override
	public String getMobile() {
		return mobile;
	}
	
	/**
	 * Set value for mobile.
	 */
	@Override
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	/**
	 * Returns the value of fax.
	 */
	@Override
	public String getFax() {
		return fax;
	}
	
	/**
	 * Set value for fax.
	 */
	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}
	
	/**
	 * Returns the value of role.
	 */
	@Override
	public IPicklistEntry getRole() {
		return role;
	}
	
	/**
	 * Set value for role.
	 */
	@Override
	public void setRole(IPicklistEntry role) {
		this.role = role;
	}
	

}
