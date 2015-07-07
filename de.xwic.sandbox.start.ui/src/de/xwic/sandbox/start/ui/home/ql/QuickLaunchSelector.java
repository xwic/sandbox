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
package de.xwic.sandbox.start.ui.home.ql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.Field;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Module;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.app.SubModule;
import de.xwic.sandbox.start.model.StartModelConfig;
import de.xwic.sandbox.start.model.dao.IQuickLaunchDAO;
import de.xwic.sandbox.start.model.entities.IQuickLaunch;
import de.xwic.sandbox.start.ui.home.QuickLaunchModel;
import de.xwic.sandbox.start.ui.home.QuickLaunchUIItem;
import de.xwic.sandbox.util.ui.DirectQuery;

/**
 * @author Raluca Geogia
 *
 */
public class QuickLaunchSelector extends ControlContainer {

	private List<QuickLaunchUIItem> items = new ArrayList<QuickLaunchUIItem>();
	private QuickLaunchModel model;

	private ValueChangedListener chkUpdateListener = new ValueChangedListener() {

		@Override
		public void valueChanged(ValueChangedEvent event) {
			onSelection(event);
		}
	};
	private CheckBox cbAllItems;

	/**
	 * @param container
	 * @param name
	 * @param model
	 */
	public QuickLaunchSelector(IControlContainer container, String name, QuickLaunchModel model) {
		super(container, name);
		this.model = model;

		buildData();

		cbAllItems = new CheckBox(this, "cbAllItems");
		cbAllItems.setLabel("View all available Quick Link items");
		cbAllItems.setChecked(false);
		cbAllItems.addValueChangedListener(new ValueChangedListener() {

			@Override
			public void valueChanged(ValueChangedEvent event) {
				requireRedraw();
			}
		});

	}

	/**
	 * @param event
	 */
	protected void onSelection(ValueChangedEvent event) {

		IQuickLaunchDAO qlDAO = StartModelConfig.getQuickLaunchDAO();

		// A bit of a hack... extract the control name to identify the index number...
		Field ctrl = (Field) event.getEventSource();
		String[] names = ctrl.getId().split("\\.");
		int tmpId = Integer.parseInt(names[names.length - 2].substring(4));

		QuickLaunchUIItem itm = items.get(tmpId);
		if (itm.getQuickLinkId() != 0) {

			IQuickLaunch qlEntity = qlDAO.getEntity(itm.getQuickLinkId());
			if (qlEntity != null) {
				qlDAO.delete(qlEntity);
			}

			// reset the ID
			itm.setQuickLinkId(0);

		} else {
			IQuickLaunch ql = qlDAO.createEntity();

			List<?> result = DirectQuery.executeQuery("select max(ql.order) from QuickLaunch ql where " + "username = ?",
					model.getUsername());

			if (!result.isEmpty() && result.get(0) != null) {
				int newOrder = ((Integer) result.get(0)) + 1;
				ql.setOrder(newOrder);
			}

			// create the item...
			ql.setUsername(model.getUsername());
			ql.setReference(itm.getReference());
			qlDAO.update(ql);

			// set the ID
			itm.setQuickLinkId(ql.getId());

		}

	}

	/**
	 * 
	 */
	private void buildData() {

		Site site = ExtendedApplication.getInstance(this).getSite();

		addFunctions();

		for (Module mod : site.getModules()) {
			for (SubModule sm : mod.getSubModules()) {
				addSubModule(sm, mod.getKey(), mod.getTitle());
			}
		}

		Collections.sort(items, new Comparator<QuickLaunchUIItem>() {

			@Override
			public int compare(QuickLaunchUIItem o1, QuickLaunchUIItem o2) {
				int x = o1.getGroup().compareTo(o2.getGroup());
				if (x != 0) {
					return x;
				}
				return o1.getTitle().compareTo(o2.getTitle());
			}
		});

		List<IQuickLaunch> userItems = model.getQuickLaunchItems();
		int tmpIdCnt = 0;
		for (QuickLaunchUIItem itm : items) {
			itm.setTempId(tmpIdCnt++);

			for (IQuickLaunch ql : userItems) {
				if (itm.getReference().equals(ql.getReference())) {
					itm.setQuickLinkId(ql.getId());
					break;
				}
			}

			CheckBox cb = new CheckBox(this, "cbql" + itm.getTempId());
			cb.setChecked(itm.getQuickLinkId() != 0);
			cb.addValueChangedListener(chkUpdateListener);

		}
	}

	/**
	 * 
	 */
	private void addFunctions() {
		IUser user = DAOSystem.getSecurityManager().getCurrentUser();
		for (IQuickLaunchFunction qlf : model.getFunctions()) {
			if (qlf.isAvailable(user)) {
				QuickLaunchUIItem itm = new QuickLaunchUIItem();
				itm.setReference(qlf.getReference());
				itm.setTitle(qlf.getTitle());
				itm.setContent(qlf.getDescription());
				itm.setMostCommon(true);
				itm.setGroup("Functions");
				items.add(itm);
			}
		}
	}

	/**
	 * @param sm
	 */
	private void addSubModule(SubModule sm, String path, String group) {

		path = path + "/" + sm.getKey();

		if (sm.hasSubModules()) {
			for (SubModule subSm : sm.getSubModules()) {
				addSubModule(subSm, path, group);
			}
		} else {

			// create QuickLaunchUIItem from module
			QuickLaunchUIItem itm = new QuickLaunchUIItem(sm);
			itm.setReference(QuickLaunchModel.RESTYPE_SUBMODULE + ":" + path);
			itm.setGroup(group);
			items.add(itm);

		}

	}

	/**
	 * @return the items
	 */
	public List<QuickLaunchUIItem> getItems() {
		if (cbAllItems.isChecked()) {
			return items;
		}

		List<QuickLaunchUIItem> filter = new ArrayList<QuickLaunchUIItem>();
		for (QuickLaunchUIItem itm : items) {
			if (itm.isMostCommon()) {
				filter.add(itm);
			}
		}
		return filter;

	}
}
