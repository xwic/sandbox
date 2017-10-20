/*
 * Generated with AppKitDev.
 */
 
package de.xwic.sandbox.crm.entities.impl;

import de.xwic.sandbox.crm.entities.*;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Customer implementation class.
 */
public class Customer extends Entity implements ICustomer  {

	private String name;
	private String address1;
	private String address2;
	private String zipCode;
	private String city;
	private String country;
	private String webSite;


	/**
	 * Default constructor.
	 */
	public Customer() {
	
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
	 * Returns the value of address1.
	 */
	@Override
	public String getAddress1() {
		return address1;
	}
	
	/**
	 * Set value for address1.
	 */
	@Override
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	
	/**
	 * Returns the value of address2.
	 */
	@Override
	public String getAddress2() {
		return address2;
	}
	
	/**
	 * Set value for address2.
	 */
	@Override
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	/**
	 * Returns the value of zipCode.
	 */
	@Override
	public String getZipCode() {
		return zipCode;
	}
	
	/**
	 * Set value for zipCode.
	 */
	@Override
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/**
	 * Returns the value of city.
	 */
	@Override
	public String getCity() {
		return city;
	}
	
	/**
	 * Set value for city.
	 */
	@Override
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Returns the value of country.
	 */
	@Override
	public String getCountry() {
		return country;
	}
	
	/**
	 * Set value for country.
	 */
	@Override
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Returns the value of webSite.
	 */
	@Override
	public String getWebSite() {
		return webSite;
	}
	
	/**
	 * Set value for webSite.
	 */
	@Override
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	

}
