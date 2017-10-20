/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.dao.impl;

import de.xwic.sandbox.crm.dao.ICustomerDAO;
import de.xwic.sandbox.crm.entities.ICustomer;
import de.xwic.sandbox.crm.entities.impl.Customer;
import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add Customer DAO implementation class description.
 */
public class CustomerDAO extends AbstractDAO<ICustomer, Customer> implements ICustomerDAO  {


	/**
	 * Default constructor.
	 */
	public CustomerDAO() {
		super(ICustomer.class, Customer.class);
	}

}
