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
import de.xwic.sandbox.demoapp.model.entities.ICompany;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * A group of more than one persons to carry out an enterprise and so a form of business organization. 
 *
 */
@javax.persistence.Entity
@Table(name = "CAMPAIGN")
@AttributeOverride(name = "id", column = @Column(name = "CAMPAIGN_ID"))
public class Campaign extends Entity implements ICampaign {
    @Column(name = "NAME")
    private String name;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;

    @ManyToOne(targetEntity = de.xwic.appkit.core.model.entities.impl.PicklistEntry.class)
    @JoinColumn(name = "TYPE_ID",nullable = true)
    private IPicklistEntry type;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public Date getStartDate() {
        return startDate;
    }
    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Override
    public Date getEndDate() {
        return endDate;
    }
    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Override
    public IPicklistEntry getType() {
        return type;
    }
    @Override
    public void setType(IPicklistEntry type) {
        this.type = type;
    }
}
