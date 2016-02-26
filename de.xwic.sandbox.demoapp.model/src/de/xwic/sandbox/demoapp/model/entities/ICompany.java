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
package de.xwic.sandbox.demoapp.model.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author WebEnd
 *
 */
public interface ICompany extends IEntity {
	
	public static final String PL_ADDRESS_BOOK_COPMANY_TITLE = "company.type";

	IPicklistEntry getCompanyType();

	void setCompanyType(IPicklistEntry companyType);

	String getCompanyName();

	void setCompanyName(String companyName);

	String getNotes();

	void setNotes(String notes);
}
