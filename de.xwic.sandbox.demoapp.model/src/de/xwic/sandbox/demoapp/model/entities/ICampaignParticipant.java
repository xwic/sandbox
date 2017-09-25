/**
 * 
 */
package de.xwic.sandbox.demoapp.model.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

import java.util.Date;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization.
 * 
 * @author lippisch
 */
public interface ICampaignParticipant extends IEntity {

    ICampaign getCampaign();

    void setCampaign(ICampaign campaign);

    ICompany getCompany();

    void setCompany(ICompany company);

    IContact getContact();

    void setContact(IContact contact);
}
