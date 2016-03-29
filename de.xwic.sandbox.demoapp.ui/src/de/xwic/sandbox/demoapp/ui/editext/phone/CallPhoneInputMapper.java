/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.sandbox.demoapp.ui.editext.phone;

import de.jwic.controls.InputBox;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.queries.IPropertyQuery;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.editors.mappers.MappingException;
import de.xwic.appkit.webbase.editors.mappers.PropertyMapper;

/**
 * This mapper is a "custom" mapper for the CallPhone field. It doesnt do anything different than 
 * the InputBox mapper, but here you could handle custom controls etc...
 *
 * @author Florian Lippisch
 */
public class CallPhoneInputMapper extends PropertyMapper<InputBox> {

	public final static String MAPPER_ID = "phoneDialMapper"; 
	
	private char[] VALID_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '-', '/'};
	
	/**
	 * @param baseEntity
	 */
	public CallPhoneInputMapper(EntityDescriptor baseEntity) {
		super(baseEntity);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#validateWidget(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public String validateWidget(InputBox widget, Property[] property) {
		String error = null;
		
		// yeah, a simple RegEx would do it, but its a demo... imagine a really cool and complex validation here :)
		String s = widget.getText().trim();
		for (char c : s.toCharArray()) {
			boolean ok = false;
			for (char v : VALID_CHARS) {
				if (c == v) {
					ok = true;
					break;
				}
			}
			if (!ok) {
				error = "Invalid character in phone number: " + c;
				break;
			}
		}
		
		return error;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#loadContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void loadContent(IEntity entity, InputBox text, Property[] property) throws MappingException {
		
		Object value = readValue(entity, property);
		text.setText(value != null ? value.toString() : "");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#setEditable(de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[], boolean)
	 */
    @Override
	public void setEditable(InputBox text, Property[] property, boolean editable) {
		text.setReadonly(!editable);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#storeContent(de.xwic.appkit.core.dao.IEntity, de.jwic.base.IControl, de.xwic.appkit.core.config.model.Property[])
	 */
    @Override
	public void storeContent(IEntity entity, InputBox text, Property[] property) throws MappingException,
			ValidationException {
		writeValue(entity, property, text.getText());
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper#addPropertyToQuery(org.eclipse.swt.widgets.Widget, de.xwic.appkit.core.config.model.Property[], de.xwic.appkit.core.model.queries.PropertyQuery)
	 */
    @Override
	protected void addPropertyToQuery(InputBox text, Property[] property, IPropertyQuery query) {
		if (text.getText().length() > 0) {
			query.addLikeWithWildcardSetting(getPropertyKey(property), text.getText());
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.mappers.PropertyMapper#highlightWidget(de.jwic.base.IControl, de.xwic.appkit.core.dao.ValidationResult.Severity)
	 */
	@Override
	protected void highlightWidget(InputBox text, Severity error) {
		if (error == null) {
			text.setFlagAsError(false);
		} else if (error == Severity.ERROR) {
			text.setFlagAsError(true);
		} else {
			text.setFlagAsError(false); // do not flag warnings at this time..
			
		}
	}
	
}
