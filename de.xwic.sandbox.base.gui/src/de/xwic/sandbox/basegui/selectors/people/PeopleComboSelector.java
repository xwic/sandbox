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
/**
 * 
 */
package de.xwic.sandbox.basegui.selectors.people;

import de.jwic.base.IControlContainer;
import de.jwic.data.DataLabel;
import de.jwic.data.IBaseLabelProvider;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.sandbox.basegui.entityselection.EntityComboSelector;
import de.xwic.sandbox.basegui.entityselection.IEntitySelectionContributor;

/**
 * @author Raluca Geogia
 *
 */
public class PeopleComboSelector extends EntityComboSelector<IMitarbeiter> {

	public PeopleComboSelector(IControlContainer container, String name, IEntitySelectionContributor contributor, boolean lifeSearch) {
		super(container, name, contributor, lifeSearch);
		setTemplateName(EntityComboSelector.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.basegui.entityselection.EntityComboSelector#createControls()
	 */
	@Override
	protected void createControls() {
		super.createControls();

		// install custom base label provider for higher performance...
		combo.setBaseLabelProvider(new IBaseLabelProvider<IMitarbeiter>() {

			@Override
			public DataLabel getBaseLabel(IMitarbeiter object) {
					return new DataLabel(object.getNachname() + ", " + object.getVorname());
			}
		});

		combo.getComboBehavior().setLabelProviderJSClass("Sandbox.PeopleListRenderer");
		combo.getComboBehavior().setTransferFullObject(true); // we use a custom provider
		combo.setObjectSerializer(new EmployeeJsonSerializer());

	}

	public IMitarbeiter getSelectedEmployee() {
		return getSelectedEntity();
	}
}
