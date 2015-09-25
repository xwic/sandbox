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
public interface IAddressBook extends IEntity {
	
	public static final String PL_ADDRESS_BOOK_TITLE = "addressbook.title";
	
	/**
	 * @return address book entry title
	 */
	IPicklistEntry getTitle();

	/**
	 * @param title
	 */
	void setTitle(IPicklistEntry title);

	/**
	 * @return address book entry firstName
	 */
	String getFirstName();

	/**
	 * @param firstName
	 */
	void setFirstName(String firstName);

	/**
	 * @return address book entry middleName
	 */
	String getMiddleName();

	/**
	 * @param middleName
	 */
	void setMiddleName(String middleName);

	/**
	 * @return address book entry lastName
	 */
	String getLastName();

	/**
	 * @param lastName
	 */
	void setLastName(String lastName);

	/**
	 * @return address book entry phone1
	 */
	String getPhone1();

	/**
	 * @param phone1
	 */
	void setPhone1(String phone1);

	/**
	 * @return address book entry phone2
	 */
	String getPhone2();

	/**
	 * @param phone2
	 */
	void setPhone2(String phone2);

	/**
	 * @return address book entry fax
	 */
	String getFax();

	/**
	 * @param fax
	 */
	void setFax(String fax);

	/**
	 * @return address book entry mobile1
	 */
	String getMobile1();

	/**
	 * @param mobile1
	 */
	void setMobile1(String mobile1);

	/**
	 * @return address book entry mobile2
	 */
	String getMobile2();

	/**
	 * @param mobile2
	 */
	void setMobile2(String mobile2);

	/**
	 * @return address book entry webSite
	 */
	String getWebSite();

	/**
	 * @param webSite
	 */
	void setWebSite(String webSite);

	/**
	 * @return address book entry email1
	 */
	String getEmail1();

	/**
	 * @param email1
	 */
	void setEmail1(String email1);

	/**
	 * @return address book entry email2
	 */
	String getEmail2();

	/**
	 * @param email2
	 */
	void setEmail2(String email2);

	/**
	 * @return address book entry addressCountry
	 */
	String getAddressCountry();

	/**
	 * @param addressCountry
	 */
	void setAddressCountry(String addressCountry);

	/**
	 * @return address book entry addressState
	 */
	String getAddressState();

	/**
	 * @param addressState
	 */
	void setAddressState(String addressState);

	/**
	 * @return address book entry addressCity
	 */
	String getAddressCity();

	/**
	 * @param addressCity
	 */
	void setAddressCity(String addressCity);

	/**
	 * @return address book entry addressZip
	 */
	String getAddressZip();

	/**
	 * @param addressZip
	 */
	void setAddressZip(String addressZip);

	/**
	 * @return address book entry addressStreet
	 */
	String getAddressStreet();

	/**
	 * @param addressStreet
	 */
	void setAddressStreet(String addressStreet);

	/**
	 * @return address book entry note
	 */
	String getNote();

	/**
	 * @param note
	 */
	void setNote(String note);
}
