/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.model.util.PulseUtil 
 */
package de.xwic.sandbox.crm.ui.util;

import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;


/**
 * @author Adrian Ionescu
 */
public class DaoValidationUtil {

	/**
	 * the key for the ENTITY_EXISTS message
	 */
	public static final String ENTITY_EXISTS_KEY = "ENTITY_EXISTS";
	/**
	 * the key for the PROPERTY_NOT_UNIQUE message
	 */
	public static final String PROPERTY_NOT_UNIQUE_KEY = "PROPERTY_NOT_UNIQUE";
	
	/**
	 * @param entity
	 * @param bundle
	 * @param validationResult
	 * @return
	 */
	public static String validateThroughDao(IEntity entity, Bundle bundle) {
		ValidationResult validationResult = DAOSystem.findDAOforEntity(entity.type()).validateEntity(entity);
		return extractValidationMessages(entity, validationResult, bundle);
	}

	/**
	 * @param entity
	 * @param validationResult
	 * @return
	 */
	public static String extractValidationMessages(IEntity entity, ValidationResult validationResult) {
		return extractValidationMessages(entity, validationResult, null);
	}
	
	/**
	 * @param entity
	 * @param validationResult
	 * @param customBundle
	 * @return
	 */
	public static String extractValidationMessages(IEntity entity, ValidationResult validationResult, Bundle customBundle) {
		if (validationResult == null || validationResult.getErrorMap() == null || validationResult.getErrorMap().isEmpty()) {
			return "";
		}
		
		StringBuilder msg = new StringBuilder();

		EntityDescriptor descriptor = null;
		Bundle entityBundle = null;
		try {
			descriptor = DAOSystem.getEntityDescriptor(entity.type().getName());
			if (customBundle != null) {
				entityBundle = customBundle;
			} else {
				entityBundle = descriptor.getDomain().getBundle(ConfigurationManager.getSetup().getDefaultLangId());
			}
		} catch (ConfigurationException e) {
			msg.append(e.getMessage());
			msg.append("<br>");
		}

		for (String strProperty : validationResult.getErrorMap().keySet()) {
			String propertyName = entityBundle != null ? entityBundle.getString(strProperty) : strProperty;
			String message = validationResult.getErrorMap().get(strProperty);

			String bundleMsg = "";
			
			if (message.equalsIgnoreCase(ValidationResult.FIELD_REQUIRED)) {
				bundleMsg = getFromBundle(customBundle, ValidationResult.FIELD_REQUIRED, "The field is mandatory");
			} else if (message.equalsIgnoreCase(ENTITY_EXISTS_KEY)) {
				bundleMsg = getFromBundle(customBundle, ENTITY_EXISTS_KEY, "This entity already exists");
			} else if (message.equalsIgnoreCase(PROPERTY_NOT_UNIQUE_KEY)) {
				bundleMsg = getFromBundle(customBundle, PROPERTY_NOT_UNIQUE_KEY, "The property is not unique");
			} else if (message.equalsIgnoreCase(ValidationResult.FIELD_TOO_LONG)) {
				Property prop = descriptor.getProperty(strProperty.substring(strProperty.lastIndexOf('.') + 1));
				String maxLength = String.valueOf(prop.getMaxLength());
				
				bundleMsg = getFromBundle(customBundle, ValidationResult.FIELD_TOO_LONG, "The maximum length for this field is %s characters");
				bundleMsg = String.format(bundleMsg, maxLength); 
			} else if (message.indexOf(".") > 0) {
				if (customBundle != null) {
					bundleMsg = customBundle.getString(message);
				} else if (entityBundle != null) {
					bundleMsg = entityBundle.getString(message);
				} else {
					bundleMsg = message;
				}
			} else {
				bundleMsg = message;
			}

			msg.append(propertyName + ": " + bundleMsg);
			msg.append("<br>");
		}
			
		return msg.toString();
	}
	
	/**
	 * @param key
	 * @param bundle
	 * @param fallback
	 * @return
	 */
	private static String getFromBundle(Bundle bundle, String key, String fallback) {
		if (bundle != null) {
			return bundle.getString(key); 
		}
		
		return fallback;
	}
}
