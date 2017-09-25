/**
 * 
 */
package de.xwic.sandbox.demoapp.model.entities;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Entities that contain an address like a company or a contact share this
 * interface.
 * @author lippisch
 */
public interface IAddress {
	
	public static final String PL_ADDRESS_COUNTRY = "address.country";
	
	/**
	 * @return the city
	 */
	public abstract String getCity();

	/**
	 * @param city the city to set
	 */
	public abstract void setCity(String city);

	/**
	 * @return the zip
	 */
	public abstract String getZip();

	/**
	 * @param zip the zip to set
	 */
	public abstract void setZip(String zip);

	/**
	 * @return the address1
	 */
	public abstract String getAddress1();

	/**
	 * @param address1 the address1 to set
	 */
	public abstract void setAddress1(String address1);

	/**
	 * @return the address2
	 */
	public abstract String getAddress2();

	/**
	 * @param address2 the address2 to set
	 */
	public abstract void setAddress2(String address2);

	/**
	 * @return the state
	 */
	public abstract String getState();

	/**
	 * @param state the state to set
	 */
	public abstract void setState(String state);

	/**
	 * @return the country
	 */
	public abstract IPicklistEntry getCountry();

	/**
	 * @param country the country to set
	 */
	public abstract void setCountry(IPicklistEntry country);


}
