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
import de.xwic.sandbox.demoapp.model.entities.IAddressBook;
import de.xwic.sandbox.demoapp.model.entities.ICompany;

/**
 * @author WebEnd
 *
 */
public class AddressBook extends Entity implements IAddressBook {
	private IPicklistEntry title;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String phone1 = null;
	private String phone2 = null;
	private String fax = null;
	private String mobile1 = null;
	private String mobile2 = null;
	private String webSite = null;
	private String email1 = null;
	private String email2 = null;
	private String addressCountry = null;
	private String addressState = null;
	private String addressCity = null;
	private String addressZip = null;
	private String addressStreet = null;
	private String note = null;
	private Boolean displayName;
	private ICompany company;

	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getTitle()
	 */
	@Override
	public IPicklistEntry getTitle() {
		return title;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(IPicklistEntry title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getMiddleName()
	 */
	@Override
	public String getMiddleName() {
		return middleName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setMiddleName(java.lang.String)
	 */
	@Override
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getPhone1()
	 */
	@Override
	public String getPhone1() {
		return phone1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setPhone1(java.lang.String)
	 */
	@Override
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getPhone2()
	 */
	@Override
	public String getPhone2() {
		return phone2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setPhone2(java.lang.String)
	 */
	@Override
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getFax()
	 */
	@Override
	public String getFax() {
		return fax;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setFax(java.lang.String)
	 */
	@Override
	public void setFax(String fax) {
		this.fax = fax;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getMobile1()
	 */
	@Override
	public String getMobile1() {
		return mobile1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setMobile1(java.lang.String)
	 */
	@Override
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getMobile2()
	 */
	@Override
	public String getMobile2() {
		return mobile2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setMobile2(java.lang.String)
	 */
	@Override
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getWeb_site()
	 */
	@Override
	public String getWebSite() {
		return webSite;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setWeb_site(java.lang.String)
	 */
	@Override
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getEmail1()
	 */
	@Override
	public String getEmail1() {
		return email1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setEmail1(java.lang.String)
	 */
	@Override
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getEmail2()
	 */
	@Override
	public String getEmail2() {
		return email2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setEmail2(java.lang.String)
	 */
	@Override
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getAddressCountry()
	 */
	@Override
	public String getAddressCountry() {
		return addressCountry;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setAddressCountry(java.lang.String)
	 */
	@Override
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getAddressState()
	 */
	@Override
	public String getAddressState() {
		return addressState;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setAddressState(java.lang.String)
	 */
	@Override
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getAddressCity()
	 */
	@Override
	public String getAddressCity() {
		return addressCity;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setAddressCity(java.lang.String)
	 */
	@Override
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getAddressZip()
	 */
	@Override
	public String getAddressZip() {
		return addressZip;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setAddressZip(java.lang.String)
	 */
	@Override
	public void setAddressZip(String addressZip) {
		this.addressZip = addressZip;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getAddressStreet()
	 */
	@Override
	public String getAddressStreet() {
		return addressStreet;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setAddressStreet(java.lang.String)
	 */
	@Override
	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getNote()
	 */
	@Override
	public String getNote() {
		return note;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setNote(java.lang.String)
	 */
	@Override
	public void setNote(String note) {
		this.note = note;
	}


	@Override
	public ICompany getCompany() {
		return company;
	}

	@Override
	public void setCompany(ICompany company) {
		this.company = company;
	}


    @Override
    public Boolean getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(Boolean displayName) {
        this.displayName = displayName;
    }
}
