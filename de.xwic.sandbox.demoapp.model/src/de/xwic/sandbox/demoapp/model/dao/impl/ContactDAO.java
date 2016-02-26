/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *******************************************************************************/
package de.xwic.sandbox.demoapp.model.dao.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.sandbox.demoapp.model.dao.IContactDAO;
import de.xwic.sandbox.demoapp.model.entities.IContact;
import de.xwic.sandbox.demoapp.model.entities.impl.Contact;

/**
 * @author WebEnd
 *
 */
public class ContactDAO extends AbstractDAO<IContact, Contact> implements IContactDAO {

	/**
	 * 
	 */
	public ContactDAO() {
		super(IContact.class, Contact.class);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.AbstractDAO#validateEntity(de.xwic.appkit.core.dao.IEntity)
	 */
	@Override
	public ValidationResult validateEntity(IEntity entity) {
		ValidationResult result = super.validateEntity(entity);
		
		IContact ab = (IContact)entity;
		
		if (ab.getEmail1() == null || ab.getEmail1().trim().isEmpty()) {
			result.addWarning(entity.type().getName() + "." + "email1", "entity.validate.warn.email.missing");
		}
		
		return result;
	}
	
}
