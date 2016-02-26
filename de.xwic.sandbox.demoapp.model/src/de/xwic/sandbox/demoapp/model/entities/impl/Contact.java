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
import de.xwic.sandbox.demoapp.model.entities.IContact;
import de.xwic.sandbox.demoapp.model.entities.ICompany;

/**
 * @author WebEnd
 *
 */
public class Contact extends Entity implements IContact {
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

	private IPicklistEntry country = null;
	private String state = null;
	private String city = null;
	private String zip = null;
	private String address1 = null;
	private String address2 = null;
	
	private String notes = null;
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
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#getNotes()
	 */
	@Override
	public String getNotes() {
		return notes;
	}
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.demoapp.model.entities.impl.IAddressBook#setNotes(java.lang.String)
	 */
	@Override
	public void setNotes(String note) {
		this.notes = note;
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
	/**
	 * @return the country
	 */
	public IPicklistEntry getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(IPicklistEntry country) {
		this.country = country;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}
	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}
	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}
	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
    
    
}
