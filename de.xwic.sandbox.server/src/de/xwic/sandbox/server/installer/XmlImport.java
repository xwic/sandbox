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
package de.xwic.sandbox.server.installer;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.queries.PicklistTextQuery;
import de.xwic.appkit.core.model.util.ServerConfig;

/**
 * @author Florian Lippisch
 *
 */
public class XmlImport {

	private final Log log = LogFactory.getLog(getClass());

	private Map<EntityKey, Integer> importedEntities = new HashMap<EntityKey, Integer>();

	/**
	 * @param file
	 * @throws ConfigurationException
	 */
	public void importFile(File file) throws IOException, DocumentException, ConfigurationException {

		if (!file.exists()) {
			throw new FileNotFoundException("File not found: " + file.getName());
		}
		log.info("Importing " + file.getName());

		Document doc = new SAXReader().read(file);
		Element root = doc.getRootElement();

		String type = root.attributeValue("type");
		if (type != null && "security".equals(type)) {
			importAll(root.element(XmlExport.ELM_DATA));
		} else if (type != null && "entities".equals(type)) {
			importAll(root.element(XmlExport.ELM_DATA));
		} else if (type != null && "picklists".equals(type)) {
			importPicklists(root.element(XmlExport.ELM_DATA));
		} else if ("config".equals(root.getName())) {
			importConfig(root);
		}

	}

	/**
	 * @param root
	 */
	private void importConfig(Element root) {

		IPicklisteDAO dao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);

		for (Iterator<?> it = root.elementIterator("key"); it.hasNext();) {
			Element element = (Element) it.next();
			String id = element.attributeValue("id");

			Element elValue = element.element("value");
			Element elPl = element.element("picklistentry");
			if (elValue != null) {
				ServerConfig.set(id, elValue.getTextTrim());
			} else if (elPl != null) {
				String list = elPl.attributeValue("list");
				String lang = elPl.attributeValue("lang");
				String text = elPl.getTextTrim();
				if (list != null && lang != null && text != null) {
					IPickliste pListe = dao.getPicklisteByKey(list);
					if (pListe != null) {
						PicklistTextQuery tquery = new PicklistTextQuery();
						tquery.setLanguageId(lang);
						tquery.setBezeichnung(text);
						tquery.setPicklisteID(pListe.getId());

						List<?> result = dao.getEntities(null, tquery);
						if (result.isEmpty()) {
							log.info("WARNING: Picklistentry not found specified for " + id);
							log.info("Creating entry '" + text + "'");

							IPicklistEntry entry = dao.createPickListEntry(pListe);
							for (Iterator<?> itLang = ConfigurationManager.getSetup().getLanguages().iterator(); itLang.hasNext();) {
								Language language = (Language) itLang.next();
								dao.createBezeichnung(entry, language.getId(), text);
							}

							ServerConfig.set(id, entry.getId());

						} else {
							IPicklistText pText = (IPicklistText) result.get(0);
							ServerConfig.set(id, pText.getPicklistEntry().getId());
							if (result.size() > 1) {
								log.info("WARNING: Found more then one picklistentries that match for ID " + id);
							}
						}
					}
				}
			}

		}

	}

	/**
	 * @param root
	 */
	private void importPicklists(Element data) {

		IPicklisteDAO dao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);

		for (Iterator<?> it = data.elementIterator(); it.hasNext();) {
			Element elPl = (Element) it.next();
			if (elPl.getName().equals("pickliste")) {

				String key = elPl.attributeValue("key");
				IPickliste pickliste = (IPickliste) dao.createEntity();
				pickliste.setKey(key);

				Element elBeschr = elPl.element("beschreibung");
				if (elBeschr != null) {
					pickliste.setBeschreibung(elBeschr.getTextTrim());
				}
				Element elTitle = elPl.element("title");
				if (elTitle != null) {
					pickliste.setTitle(elTitle.getTextTrim());
				}

				dao.update(pickliste);

				Element elEntries = elPl.element("entries");
				if (elEntries != null) {

					for (Iterator<?> itEn = elEntries.elementIterator(); itEn.hasNext();) {
						Element elEntry = (Element) itEn.next();
						if (elEntry.getName().equals("entry")) {
							IPicklistEntry entry = dao.createPickListEntry(pickliste);
							String oldId = elEntry.attributeValue("id");
							if (oldId != null) {
								importedEntities.put(new EntityKey(IPicklistEntry.class.getName(), Integer.parseInt(oldId)),
										Integer.valueOf(entry.getId()));
							}
							boolean modified = false;
							if (elEntry.attributeValue("key") != null) {
								entry.setKey(elEntry.attributeValue("key"));
								modified = true;
							}
							if (elEntry.attributeValue("sortindex") != null) {
								entry.setSortIndex(Integer.parseInt(elEntry.attributeValue("sortindex")));
								modified = true;
							}
							if (modified) {
								dao.update(entry);
							}

							for (Iterator<?> itT = elEntry.elementIterator(); itT.hasNext();) {
								Element elText = (Element) itT.next();
								String langId = elText.attributeValue("lang");
								dao.createBezeichnung(entry, langId, elText.getTextTrim());
							}
						}
					}

				}
			}
		}

	}

	/**
	 * @param root
	 * @throws ConfigurationException
	 */
	private void importAll(Element data) throws ConfigurationException {

		for (Iterator<?> it = data.elementIterator(XmlExport.ELM_ENTITIES); it.hasNext();) {

			Element elEntities = (Element) it.next();
			importEntities(elEntities);

		}

	}

	/**
	 * @param elEntities
	 * @throws ConfigurationException
	 */
	private void importEntities(Element elEntities) throws ConfigurationException {

		String type = elEntities.attributeValue("type");
		DAO dao = DAOSystem.findDAOforEntity(type);
		EntityDescriptor descr = DAOSystem.getEntityDescriptor(type);

		int count = 0;
		for (Iterator<?> it = elEntities.elementIterator(XmlExport.ELM_ENTITY); it.hasNext();) {

			Element elEntity = (Element) it.next();
			String idStr = elEntity.attributeValue("id");
			int id = (idStr != null && idStr.length() != 0) ? Integer.parseInt(elEntity.attributeValue("id")) : -1;

			IEntity entity = dao.createEntity();
			readEntityProperties(descr, elEntity, entity);

			dao.update(entity);
			if (id != -1) {
				importedEntities.put(new EntityKey(type, id), Integer.valueOf(entity.getId()));
			}
			//log.info((count++) + " imp " + type + " id: "+ id + " as " + entity.getId());
			count++;
		}
		log.info("imported " + count + " entities.");

	}

	/**
	 * @param descr
	 * @param elEntity
	 * @param entity
	 */
	private void readEntityProperties(EntityDescriptor descr, Element elEntity, IEntity entity) {

		for (Iterator<?> it = elEntity.elementIterator(); it.hasNext();) {

			Element elProp = (Element) it.next();
			String propertyName = elProp.getName();

			Property property = descr.getProperty(propertyName);
			if (property != null) {

				try {
					loadPropertyValue(entity, property, elProp);
				} catch (Exception e) {
					throw new DataAccessException("Error loading properties: " + e, e);
				}
			}
		}
	}

	/**
	 * @param entity
	 * @param property
	 * @param elProp
	 */
	@SuppressWarnings("unchecked")
	private void loadPropertyValue(IEntity entity, Property property, Element elProp) throws Exception {

		PropertyDescriptor pd = property.getDescriptor();
		Class<?> type = pd.getPropertyType();
		Method mWrite = pd.getWriteMethod();
		// check if value is null
		boolean isNull = elProp.element(XmlExport.ELM_NULL) != null;

		Object value = null;
		if (!isNull) {

			if (Set.class.isAssignableFrom(type)) {
				// a set.
				Set<IEntity> set = new HashSet<IEntity>();
				Element elSet = elProp.element(XmlExport.ELM_SET);
				for (Iterator<?> itSet = elSet.elementIterator(XmlExport.ELM_ELEMENT); itSet.hasNext();) {
					Element elSetElement = (Element) itSet.next();
					String typeElement = elSetElement.attributeValue("type");
					int refId = Integer.parseInt(elSetElement.getText());

					Integer newId = importedEntities.get(new EntityKey(typeElement, refId));
					if (newId != null) {
						// its an imported object
						refId = newId.intValue();
					}
					DAO refDAO = DAOSystem.findDAOforEntity(typeElement);
					IEntity refEntity = refDAO.getEntity(refId);
					set.add(refEntity);
				}
				value = set;

			} else if (IEntity.class.isAssignableFrom(type)) {
				// entity type
				int refId = Integer.parseInt(elProp.getText());
				Integer newId = importedEntities.get(new EntityKey(type.getName(), refId));
				if (newId != null) {
					// its an imported object
					refId = newId.intValue();
				}
				if (IPicklistEntry.class.isAssignableFrom(type)) {
					IPicklisteDAO plDAO = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
					value = plDAO.getPickListEntryByID(refId);
				} else {
					DAO refDAO = DAOSystem.findDAOforEntity((Class<? extends IEntity>) type);
					IEntity refEntity = refDAO.getEntity(refId);
					value = refEntity;
				}

			} else {
				// basic type
				String text = elProp.getText();
				if (String.class.equals(type)) {
					value = text;
				} else if (int.class.equals(type) || Integer.class.equals(type)) {
					value = Integer.valueOf(text);
				} else if (long.class.equals(type) || Long.class.equals(type)) {
					value = Long.valueOf(text);
				} else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
					value = Boolean.valueOf(text.equals("true"));
				} else if (Date.class.equals(type)) {
					value = new Date(Long.parseLong(text));
				} else if (double.class.equals(type) || Double.class.equals(type)) {
					value = Double.valueOf(text);
				}
			}

		}
		mWrite.invoke(entity, new Object[] { value });

	}

}
