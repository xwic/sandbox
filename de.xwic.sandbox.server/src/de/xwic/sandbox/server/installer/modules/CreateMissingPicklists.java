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
package de.xwic.sandbox.server.installer.modules;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.DefaultPicklistEntry;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IPicklisteDAO;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.IPickliste;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * Create picklist entries that are references in the EntityDescriptors but that do not exist.
 *
 * This module can run on any version and product.
 *
 * @author Florian Lippisch
 */
public class CreateMissingPicklists extends AbstractUpgradeModule {

	private IPicklisteDAO plDAO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.installer.IUpgradeModule#run(de.xwic.appkit.core.installer.InstallationManager)
	 */
	@Override
	public void run(InstallationManager manager) throws Exception {

		log.info("Scanning picklists...");

		plDAO = DAOSystem.getDAO(IPicklisteDAO.class);

		for (Model model : ConfigurationManager.getSetup().getModels()) {
			for (String key : model.getManagedEntities()) {
				EntityDescriptor descr = model.getEntityDescriptor(key);
				for (Property property : descr.getProperties().values()) {
					if (property.getPicklistId() != null && property.getPicklistId().length() != 0) {

						// scan picklist
						IPickliste pl = plDAO.getPicklisteByKey(property.getPicklistId());
						if (pl == null) {
							log.info("Creating Pickliste: " + property.getPicklistId());
							pl = (IPickliste) plDAO.createEntity();
							pl.setTitle(property.getPicklistId());
							pl.setKey(property.getPicklistId());
							pl.setBeschreibung(descr.getClassname() + "." + property.getName());
							plDAO.update(pl);
						}

						// now check default picklist entries
						checkDefaultEntries(pl, property.getDefaultPicklistEntries());

					}
				}
			}
		}

	}

	/**
	 * @param defaultPicklistEntries
	 */
	private void checkDefaultEntries(IPickliste pl, List<DefaultPicklistEntry> entries) {

		if (entries != null) {
			for (DefaultPicklistEntry dpe : entries) {

				// check if the entry does already exist
				IPicklistEntry entry = null;
				if (dpe.getKey() != null) {
					entry = plDAO.getPickListEntryByKey(pl.getKey(), dpe.getKey());
				}
				if (entry == null) { // search by title
					List<IPicklistEntry> allPE = plDAO.getAllEntriesToList(pl);
					for (Iterator<IPicklistEntry> it = allPE.iterator(); entry == null && it.hasNext();) {
						IPicklistEntry pe = it.next();

						Map<String, String> titles = dpe.getTitles();
						for (Iterator<String> itLang = titles.keySet().iterator(); entry == null && itLang.hasNext();) {
							String lang = itLang.next();
							String title = titles.get(lang);
							if (title.equalsIgnoreCase(pe.getBezeichnung(lang))) {
								entry = pe;
								break;
							}
						}
					}
				}

				if (entry == null) {
					// create new entry
					log.info("Creating PicklistEntry '" + dpe.getKey() + "'");
					entry = plDAO.createPicklistEntry();
					entry.setPickliste(pl);
					entry.setKey(dpe.getKey());
					entry.setSortIndex(dpe.getIndex());
					plDAO.update(entry);
					Map<String, String> titles = dpe.getTitles();
					Set<Entry<String, String>> entrySet = titles.entrySet();
					for (Entry<String, String> e : entrySet) {
						plDAO.createBezeichnung(entry, e.getKey(), e.getValue());
					}
				} else {

					boolean modified = false;

					if (dpe.getKey() != null && (entry.getKey() == null || entry.getKey().length() == 0)) {

						entry.setKey(dpe.getKey());
						modified = true;

					}

					if (dpe.getIndex() != entry.getSortIndex()) {
						entry.setSortIndex(dpe.getIndex());
						modified = true;
					}

					if (modified) {
						plDAO.update(entry);
						log.info("Updated PicklistEntry with key '" + dpe.getKey() + "'");
					}
				}

			}
		}

	}

}
