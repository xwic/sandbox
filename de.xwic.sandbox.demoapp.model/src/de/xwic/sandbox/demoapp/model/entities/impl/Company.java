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
package de.xwic.sandbox.demoapp.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.sandbox.demoapp.model.entities.ICompany;

/**
 * @author WebEnd
 *
 */
public class Company extends Entity implements ICompany {

	private IPicklistEntry companyType;
	private String companyName = null;
	private String notes = null;

	@Override public IPicklistEntry getCompanyType() {
		return companyType;
	}

	@Override public void setCompanyType(IPicklistEntry companyType) {
		this.companyType = companyType;
	}

	@Override public String getCompanyName() {
		return companyName;
	}

	@Override public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override public String getNotes() {
		return notes;
	}

	@Override public void setNotes(String notes) {
		this.notes = notes;
	}
}
