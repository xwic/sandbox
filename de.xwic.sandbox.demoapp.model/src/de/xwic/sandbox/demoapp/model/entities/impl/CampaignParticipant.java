/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.xwic.sandbox.demoapp.model.entities.impl;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.sandbox.demoapp.model.entities.ICampaign;
import de.xwic.sandbox.demoapp.model.entities.ICampaignParticipant;
import de.xwic.sandbox.demoapp.model.entities.ICompany;
import de.xwic.sandbox.demoapp.model.entities.IContact;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization.
 */
@javax.persistence.Entity
@Table(name = "CAMPAIGN_PARTICIPANT")
@AttributeOverride(name = "id", column = @Column(name = "CAMPAIGN_PARTICIPANT_ID"))
public class CampaignParticipant extends Entity implements ICampaignParticipant {
    @ManyToOne(targetEntity = Campaign.class)
    @JoinColumn(name = "CAMPAIGN_ID", nullable = false)
    private ICampaign campaign;

    @ManyToOne(targetEntity = Company.class)
    @JoinColumn(name = "COMPANY_ID", nullable = true)
    private ICompany company;

    @ManyToOne(targetEntity = Contact.class)
    @JoinColumn(name = "CONTACT_ID", nullable = true)
    private IContact contact;


    @Override
    public ICampaign getCampaign() {
        return campaign;
    }

    @Override
    public void setCampaign(ICampaign campaign) {
        this.campaign = campaign;
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
    public IContact getContact() {
        return contact;
    }

    @Override
    public void setContact(IContact contact) {
        this.contact = contact;
    }
}
