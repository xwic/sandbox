/*
 * Generated with AppKitDev.
 */
 package de.xwic.sandbox.crm.dao.impl;

import de.xwic.sandbox.crm.dao.IProductDAO;
import de.xwic.sandbox.crm.entities.IProduct;
import de.xwic.sandbox.crm.entities.impl.Product;
import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * TODO: Add Product DAO implementation class description.
 */
public class ProductDAO extends AbstractDAO<IProduct, Product> implements IProductDAO  {


	/**
	 * Default constructor.
	 */
	public ProductDAO() {
		super(IProduct.class, Product.class);
	}

}
