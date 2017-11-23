/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm;

import de.xwic.sandbox.crm.dao.*;
import de.xwic.sandbox.crm.dao.impl.*;

import de.xwic.appkit.core.dao.DAOFactory;
import de.xwic.appkit.core.dao.DAOSystem;

/**
 * Registers the available DAOs in this model with the DAO factory and provides
 * convenient methods to access DAOs in this model.
 */
public class CrmModelConfig {
	/**
	 * @param factory
	 */
	public static void register(DAOFactory factory) {
		factory.registerDao(ICustomerDAO.class, new CustomerDAO());
		factory.registerDao(IContactDAO.class, new ContactDAO());
		factory.registerDao(IProductDAO.class, new ProductDAO());
		factory.registerDao(IInstallBaseDAO.class, new InstallBaseDAO());
	}

	/**
	 * Return the Customer DAO.
	 * @return
	 */
	public static ICustomerDAO getCustomerDAO() {
		return (ICustomerDAO) DAOSystem.getDAO(ICustomerDAO.class);
	}
	/**
	 * Return the Contact DAO.
	 * @return
	 */
	public static IContactDAO getContactDAO() {
		return (IContactDAO) DAOSystem.getDAO(IContactDAO.class);
	}
	/**
	 * Return the Product DAO.
	 * @return
	 */
	public static IProductDAO getProductDAO() {
		return (IProductDAO) DAOSystem.getDAO(IProductDAO.class);
	}
	/**
	 * Return the InstallBase DAO.
	 * @return
	 */
	public static IInstallBaseDAO getInstallBaseDAO() {
		return (IInstallBaseDAO) DAOSystem.getDAO(IInstallBaseDAO.class);
	}
}
