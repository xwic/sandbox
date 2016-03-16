/**
 * 
 */
package de.xwic.sandbox.demoapp.model.entities;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization.
 * 
 * @author lippisch
 */
public interface ICampaign extends IEntity {

    String getName();

    void setName(String name);

    Date getStartDate();

    void setStartDate(Date startDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    IPicklistEntry getType();

    void setType(IPicklistEntry type);
}
