/**
 * 
 */
package de.xwic.sandbox.demoapp.ui.editext.phone;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.config.editor.ECustom;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.builders.Builder;

/**
 * For the sake of the demo, this builder generates a regular input box field with a button that 
 * would call the number in the field. It's just a demo of course..
 * @author lippisch
 */
public class CallPhoneInputBuilder extends Builder<ECustom> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement, de.jwic.base.IControlContainer, de.xwic.appkit.webbase.editors.IBuilderContext)
	 */
	@Override
	public IControl buildComponents(ECustom element, IControlContainer parent, IBuilderContext context) {
		

		InputBox inputBox = new InputBox(parent);

		inputBox.addValueChangedListener(new FieldChangeListener(context, element.getProperty()));
		Property prop = element.getFinalProperty();
		if (prop.getMaxLength() > 0) {
			inputBox.setMaxLength(prop.getMaxLength());
		}
		if (prop.getRequired()) {
			inputBox.setEmptyInfoText("Required Field");
		}
		
		Style style = element.getStyle();
		if (style.getStyleInt(Style.WIDTH_HINT) != 0) {
			inputBox.setWidth(style.getStyleInt(Style.WIDTH_HINT) );
		}

		if (style.getStyleInt(Style.HEIGHT_HINT) != 0) {
			inputBox.setHeight(style.getStyleInt(Style.HEIGHT_HINT) );
		}
		
		inputBox.setEmptyInfoText("+## ##### ####");

		context.registerField(element.getProperty(), inputBox, element, CallPhoneInputMapper.MAPPER_ID);
		
		return inputBox;
		
	}

}
