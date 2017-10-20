/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add Contact description.
 */
public interface IContact extends IEntity {


	public final static String PL_CONTACT_ROLE = "Contact.role";
	public final static String PE_CONTACT_ROLE_CEO = "ceo";
	public final static String PE_CONTACT_ROLE_CIO = "cio";
	public final static String PE_CONTACT_ROLE_UNKNOWN = "unknown";
	public final static String PE_CONTACT_ROLE_SALESREP = "salesRep";
	public final static String PE_CONTACT_ROLE_ITMANAGER = "itManager";
	public final static String PE_CONTACT_ROLE_CFO = "cfo";
	public final static String PE_CONTACT_ROLE_OTHER = "other";

	/**
	 * Returns the value of name.
	 */
	public String getName();
	
	/**
	 * Set value for name.
	 */
	public void setName(String name);
	
	/**
	 * Returns the value of email.
	 */
	public String getEmail();
	
	/**
	 * Set value for email.
	 */
	public void setEmail(String email);
	
	/**
	 * Returns the value of phone.
	 */
	public String getPhone();
	
	/**
	 * Set value for phone.
	 */
	public void setPhone(String phone);
	
	/**
	 * Returns the value of mobile.
	 */
	public String getMobile();
	
	/**
	 * Set value for mobile.
	 */
	public void setMobile(String mobile);
	
	/**
	 * Returns the value of fax.
	 */
	public String getFax();
	
	/**
	 * Set value for fax.
	 */
	public void setFax(String fax);
	
	/**
	 * Returns the value of role.
	 */
	public IPicklistEntry getRole();
	
	/**
	 * Set value for role.
	 */
	public void setRole(IPicklistEntry role);
	

}
