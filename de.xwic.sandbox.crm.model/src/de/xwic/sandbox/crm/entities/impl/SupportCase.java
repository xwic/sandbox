/*
 * Generated with AppKitDev.
 */
 
package de.xwic.sandbox.crm.entities.impl;

import de.xwic.sandbox.crm.entities.*;
import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * SupportCase implementation class.
 */
public class SupportCase extends Entity implements ISupportCase  {

	private String caseNumber;
	private String shortDescription;
	private ICustomer customer;
	private IContact reportedBy;
	private String description;
	private IPicklistEntry severity;


	/**
	 * Default constructor.
	 */
	public SupportCase() {
	
	}
	
	/**
	 * Returns the value of caseNumber.
	 */
	@Override
	public String getCaseNumber() {
		return caseNumber;
	}
	
	/**
	 * Set value for caseNumber.
	 */
	@Override
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
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
	 * Returns the value of reportedBy.
	 */
	@Override
	public IContact getReportedBy() {
		return reportedBy;
	}
	
	/**
	 * Set value for reportedBy.
	 */
	@Override
	public void setReportedBy(IContact reportedBy) {
		this.reportedBy = reportedBy;
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
	 * Returns the value of severity.
	 */
	@Override
	public IPicklistEntry getSeverity() {
		return severity;
	}
	
	/**
	 * Set value for severity.
	 */
	@Override
	public void setSeverity(IPicklistEntry severity) {
		this.severity = severity;
	}
	

}
