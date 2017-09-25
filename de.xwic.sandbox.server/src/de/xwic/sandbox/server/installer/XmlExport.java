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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Language;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPicklistText;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.daos.IActionDAO;
import de.xwic.appkit.core.security.daos.IActionSetDAO;
import de.xwic.appkit.core.security.daos.IRightDAO;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IScopeDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;

/**
 * @author Florian Lippisch
 *
 */
public class XmlExport {

	private final Log log = LogFactory.getLog(getClass());

	/**
	 * 
	 */
	public static final String ELM_NULL = "null";
	/**
	 * 
	 */
	public static final String ELM_ELEMENT = "element";
	/**
	 * 
	 */
	public static final String ELM_SET = "set";
	/**
	 * 
	 */
	public static final String ELM_ENTITY = "entity";
	/**
	 * 
	 */
	public static final String ELM_ENTITIES = "entities";
	/**
	 * 
	 */
	public static final String ELM_DATA = "data";
	/**
	 * 
	 */
	public static final String ELM_EXPORTDDATE = "exportddate";
	/**
	 * 
	 */
	public static final String ELM_EXPORT = "export";

	private static Set<Object> SKIP_PROPS = new HashSet<Object>();
	static {
		SKIP_PROPS.add("deleted");
		SKIP_PROPS.add("id");
		SKIP_PROPS.add("version");
		SKIP_PROPS.add("changed");
		SKIP_PROPS.add("lastModifiedAt");
		SKIP_PROPS.add("lastModifiedFrom");
		SKIP_PROPS.add("createdAt");
		SKIP_PROPS.add("createdFrom");
		SKIP_PROPS.add("serverEntityId");
		SKIP_PROPS.add("downloadVersion");
	}

	private boolean exportAll = false;

	/**
	 * Constructor.
	 */
	public XmlExport() {

	}

	/**
	 * Export data with all properties.
	 * 
	 * @param exportAll
	 */
	public XmlExport(boolean exportAll) {
		this.exportAll = exportAll;
	}

	/**
	 * @param secDump
	 */
	public void exportSecurity(File secDump) throws IOException, ConfigurationException {

		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "security");

		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));

		Element data = root.addElement(ELM_DATA);

		addAll(IActionDAO.class, data);
		addAll(IActionSetDAO.class, data);
		addAll(IScopeDAO.class, data);
		addAll(IRoleDAO.class, data);
		addAll(IRightDAO.class, data);
		addAll(IUserDAO.class, data);

		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(secDump);
		XMLWriter writer = new XMLWriter(out, prettyFormat);
		writer.write(doc);
		writer.flush();
		out.close();

	}

	/**
	 * @param maDump
	 */
	//	public void exportMitarbeiter(File maDump) throws IOException, ConfigurationException {
	//		
	//		Document doc = DocumentFactory.getInstance().createDocument();
	//		Element root = doc.addElement(ELM_EXPORT);
	//		root.addAttribute("type", "entities");
	//		
	//		Element info = root.addElement(ELM_EXPORTDDATE);
	//		info.setText(DateFormat.getDateTimeInstance().format(new Date()));
	//		
	//		Element data = root.addElement(ELM_DATA);
	//		
	//		addAll(IMitarbeiterDAO.class, data);
	//		
	//		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
	//		OutputStream out = new FileOutputStream(maDump);
	//		XMLWriter writer = new XMLWriter(out, prettyFormat);
	//		writer.write(doc);
	//		writer.flush();
	//		out.close();
	//		
	//	}

	/**
	 * @param plDump
	 */
	public void exportPicklists(File plDump) throws IOException, ConfigurationException {

		Document doc = DocumentFactory.getInstance().createDocument();
		Element root = doc.addElement(ELM_EXPORT);
		root.addAttribute("type", "picklists");

		Element info = root.addElement(ELM_EXPORTDDATE);
		info.setText(DateFormat.getDateTimeInstance().format(new Date()));

		Element data = root.addElement(ELM_DATA);

		addPicklisten(data);

		OutputFormat prettyFormat = OutputFormat.createPrettyPrint();
		OutputStream out = new FileOutputStream(plDump);
		XMLWriter writer = new XMLWriter(out, prettyFormat);
		writer.write(doc);
		writer.flush();
		out.close();

	}

	/**
	 * @param data
	 */
	private void addPicklisten(Element data) {

		IPicklisteDAO dao = (IPicklisteDAO) DAOSystem.getDAO(IPicklisteDAO.class);
		List<?> langs = ConfigurationManager.getSetup().getLanguages();

		List<?> list = dao.getEntities(null, new PropertyQuery()); // get all Picklisten
		for (Iterator<?> it = list.iterator(); it.hasNext();) {

			IPickliste pl = (IPickliste) it.next();
			Element elPl = data.addElement("pickliste");
			elPl.addAttribute("key", pl.getKey());

			elPl.addElement("title").setText(pl.getTitle());
			elPl.addElement("beschreibung").setText(pl.getBeschreibung());

			Element elEntries = elPl.addElement("entries");
			// add entries
			List<?> entries = dao.getAllEntriesToList(pl);
			for (Iterator<?> itE = entries.iterator(); itE.hasNext();) {
				IPicklistEntry entry = (IPicklistEntry) itE.next();
				if (!entry.isDeleted()) {
					Element elEntry = elEntries.addElement("entry");
					elEntry.addAttribute("id", Long.toString(entry.getId()));
					elEntry.addAttribute("sortindex", Long.toString(entry.getSortIndex()));
					if (entry.getKey() != null) {
						elEntry.addAttribute("key", entry.getKey());
					}
					for (Iterator<?> itT = langs.iterator(); itT.hasNext();) {
						Language language = (Language) itT.next();
						IPicklistText pt = dao.getPicklistText(entry, language.getId());
						Element elText = elEntry.addElement("text");
						elText.addAttribute("lang", pt.getLanguageID());
						elText.setText(pt.getBezeichnung());
					}
				}

			}

		}

	}

	/**
	 * @param class1
	 * @param data
	 * @throws ConfigurationException
	 */
	private void addAll(Class<? extends DAO> daoClass, Element data) throws ConfigurationException {

		DAO dao = DAOSystem.getDAO(daoClass);

		Element entities = data.addElement(ELM_ENTITIES);
		entities.addAttribute("type", dao.getEntityClass().getName());

		EntityDescriptor descr = DAOSystem.getEntityDescriptor(dao.getEntityClass().getName());

		List<?> all = dao.getEntities(null, new PropertyQuery());

		log.info("Adding " + all.size() + " " + dao.getEntityClass().getName());

		for (Iterator<?> it = all.iterator(); it.hasNext();) {

			IEntity entity = (IEntity) it.next();
			if (!entity.isDeleted()) {
				addEntity(entities, descr, entity);
			}

		}

	}

	/**
	 * @param entities
	 * @param descr
	 * @param entity
	 */
	private void addEntity(Element entities, EntityDescriptor descr, IEntity entity) {

		try {
			Element elm = entities.addElement(ELM_ENTITY);
			elm.addAttribute("id", Long.toString(entity.getId()));

			for (Iterator<?> it = descr.getProperties().keySet().iterator(); it.hasNext();) {

				String propertyName = (String) it.next();

				if (exportAll || !SKIP_PROPS.contains(propertyName)) {
					Property property = (Property) descr.getProperty(propertyName);

					Method mRead = property.getDescriptor().getReadMethod();
					Method mWrite = property.getDescriptor().getWriteMethod();

					if (mWrite != null) { // only export properties that have a write method

						Element pValue = elm.addElement(propertyName);
						Object value = mRead.invoke(entity, (Object[]) null);

						addValue(pValue, value, false);

					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Error transforming entity into XML: " + entity.type().getName() + ", #" + entity.getId() + " :" + e, e);
		}

	}

	/**
	 * @param value
	 * @param value2
	 */
	private void addValue(Element elm, Object value, boolean addTypeInfo) {

		String typeInfo = value != null ? value.getClass().getName() : null;
		if (value == null) {
			elm.addElement(ELM_NULL);
		} else if (value instanceof String) {
			elm.setText((String) value);
		} else if (value instanceof Integer) {
			elm.setText(value.toString());
		} else if (value instanceof Boolean) {
			Boolean bo = (Boolean) value;
			elm.setText(bo.booleanValue() ? "true" : "false");
		} else if (value instanceof Long) {
			elm.setText(value.toString());
		} else if (value instanceof Double) {
			elm.setText(value.toString());
		} else if (value instanceof Date) {
			Date date = (Date) value;
			elm.setText(Long.toString(date.getTime()));

		} else if (value instanceof IEntity) {
			IEntity entity = (IEntity) value;
			typeInfo = entity.type().getName();
			elm.setText(Long.toString(entity.getId()));

		} else if (value instanceof Set<?>) {
			Set<?> set = (Set<?>) value;
			Element elmSet = elm.addElement(ELM_SET);
			for (Iterator<?> it = set.iterator(); it.hasNext();) {
				Object o = it.next();
				Element entry = elmSet.addElement(ELM_ELEMENT);
				addValue(entry, o, true);
			}
		}

		if (addTypeInfo && typeInfo != null) {
			elm.addAttribute("type", typeInfo);
		}

	}

}
