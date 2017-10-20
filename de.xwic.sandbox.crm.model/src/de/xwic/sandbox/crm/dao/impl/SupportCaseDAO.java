/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.dao.impl;

import de.xwic.sandbox.crm.dao.ISupportCaseDAO;
import de.xwic.sandbox.crm.entities.ISupportCase;
import de.xwic.sandbox.crm.entities.impl.SupportCase;
import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add SupportCase DAO implementation class description.
 */
public class SupportCaseDAO extends AbstractDAO<ISupportCase, SupportCase> implements ISupportCaseDAO  {


	/**
	 * Default constructor.
	 */
	public SupportCaseDAO() {
		super(ISupportCase.class, SupportCase.class);
	}

}
