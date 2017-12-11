/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.util.ui.toolkit.ToolkitEntitySelectionCombo 
 */
package de.xwic.sandbox.crm.ui.util;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.entityselection.EntityComboSelector;
import de.xwic.appkit.webbase.entityselection.GenericEntitySelectionContributor;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author dotto
 *
 */
public class ToolkitEntitySelectionCombo implements IToolkitControlHelper<EntityComboSelector<?>> {

	/**
	 * @param container
	 * @param name
	 * @param optionalParam
	 * @return
	 */
	@Override
	public EntityComboSelector<?> create(IControlContainer container, String name, Object optionalParam) {
		GenericEntitySelectionContributor contributor = (GenericEntitySelectionContributor) optionalParam;
		EntityComboSelector<?> control = new EntityComboSelector(container, name, contributor, true);
		return control;
	}

	/**
	 * @param control
	 * @param optionalParam
	 */
	@Override
	public void loadContent(EntityComboSelector<?> control, Object optionalParam) {
		if (control instanceof EntityComboSelector<?>) {
			EntityComboSelector<IEntity> listBox = (EntityComboSelector<IEntity>) control;
			listBox.setEntity((IEntity)optionalParam);
		}
	}

	/**
	 * @param control
	 * @return
	 */
	@Override
	public Object getContent(EntityComboSelector<?> control) {
		if (control instanceof EntityComboSelector<?>) {
			EntityComboSelector<?> listBox = (EntityComboSelector<?>) control;
			return listBox.getSelectedEntity();
		}
		return null;
	}

	/**
	 * @param control
	 * @param cssClass
	 */
	@Override
	public void markField(EntityComboSelector<?> control, String cssClass) {
		
	}

	/**
	 * @param control
	 * @return
	 */
	@Override
	public String getFieldMarkedCssClass(EntityComboSelector<?> control) {
		
		return null;
	}
}
