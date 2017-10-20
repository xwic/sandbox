/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.dao.impl;

import de.xwic.sandbox.crm.dao.IContactDAO;
import de.xwic.sandbox.crm.entities.IContact;
import de.xwic.sandbox.crm.entities.impl.Contact;
import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add Contact DAO implementation class description.
 */
public class ContactDAO extends AbstractDAO<IContact, Contact> implements IContactDAO  {


	/**
	 * Default constructor.
	 */
	public ContactDAO() {
		super(IContact.class, Contact.class);
	}

}
