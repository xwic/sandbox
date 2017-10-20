/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add SupportCase description.
 */
public interface ISupportCase extends IEntity {


	public final static String PL_SUPPORTCASE_SEVERITY = "SupportCase.severity";
	public final static String PE_SUPPORTCASE_SEVERITY_P1 = "p1";
	public final static String PE_SUPPORTCASE_SEVERITY_P2 = "p2";
	public final static String PE_SUPPORTCASE_SEVERITY_P3 = "p3";
	public final static String PE_SUPPORTCASE_SEVERITY_P4 = "p4";

	/**
	 * Returns the value of caseNumber.
	 */
	public String getCaseNumber();
	
	/**
	 * Set value for caseNumber.
	 */
	public void setCaseNumber(String caseNumber);
	
	/**
	 * Returns the value of shortDescription.
	 */
	public String getShortDescription();
	
	/**
	 * Set value for shortDescription.
	 */
	public void setShortDescription(String shortDescription);
	
	/**
	 * Returns the value of customer.
	 */
	public ICustomer getCustomer();
	
	/**
	 * Set value for customer.
	 */
	public void setCustomer(ICustomer customer);
	
	/**
	 * Returns the value of reportedBy.
	 */
	public IContact getReportedBy();
	
	/**
	 * Set value for reportedBy.
	 */
	public void setReportedBy(IContact reportedBy);
	
	/**
	 * Returns the value of description.
	 */
	public String getDescription();
	
	/**
	 * Set value for description.
	 */
	public void setDescription(String description);
	
	/**
	 * Returns the value of severity.
	 */
	public IPicklistEntry getSeverity();
	
	/**
	 * Set value for severity.
	 */
	public void setSeverity(IPicklistEntry severity);
	

}
