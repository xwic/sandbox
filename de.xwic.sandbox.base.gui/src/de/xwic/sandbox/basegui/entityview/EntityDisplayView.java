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
package de.xwic.sandbox.basegui.entityview;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ListBox;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.menu.Menu;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.util.SerObservable;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.IUserListProfile;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.actions.EntityActionsHelper;
import de.xwic.appkit.webbase.actions.IEntityAction;
import de.xwic.appkit.webbase.actions.IEntityProvider;
import de.xwic.appkit.webbase.controls.export.ExcelExportControl;
import de.xwic.appkit.webbase.core.Platform;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.InnerPage;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.components.AbstractListContainer;
import de.xwic.appkit.webbase.toolkit.components.BaseLabelProvider;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.appkit.webbase.utils.UserConfigXmlReader;
import de.xwic.appkit.webbase.utils.UserListUtil;
import de.xwic.appkit.webbase.utils.UserProfileWrapper;
import de.xwic.appkit.webbase.viewer.EntityTableViewer;
import de.xwic.appkit.webbase.viewer.base.PropertyLabelProvider;
import de.xwic.appkit.webbase.viewer.columns.ColumnSelector.IColumnSelectorListener;
import de.xwic.appkit.webbase.viewer.columns.UserListSetup;
import de.xwic.sandbox.base.model.SandboxModelConfig;
import de.xwic.sandbox.basegui.util.IEditorCreator;
import de.xwic.sandbox.basegui.util.IQuickSearchPanel;
import de.xwic.sandbox.basegui.util.ListSetupInfo;
import de.xwic.sandbox.basegui.util.ListSetupInfoList;
import de.xwic.sandbox.util.ui.SandboxImageLibrary;


/**
 * @author Raluca Geogia
 *
 */
public abstract class EntityDisplayView extends AbstractListContainer implements IEntityProvider {

	/** user id */
	public static final String ID_USER = "user";

	/** the table viewer */
	protected EntityTableViewer tableViewer = null;
	/** the ListModel implementation for this BaseView */
	protected EntityDisplayListModel listModel;
	/** the editor creator */
	protected IEditorCreator editorCreator;
	/** the quicksearch panel */
	protected IQuickSearchPanel quickSearchPanel;
	/** the list setup id */
	protected String listSetupID = null;

	private Class<? extends IEntity> entityClass;

	private Control qsPanel = null;

	protected ListSetupInfoList lsInfoList;
	protected ListBox lbListSetup;

	private IColumnSelectorListener listener = new IColumnSelectorListener() {

		@Override
		public void selectionAborted() {
			// do nothing here
		}

		@Override
		public void profileSavedAs(Object source, UserProfileWrapper newUserProfile) {
			onNewProfile(newUserProfile);
		}

		@Override
		public void profileSaved(Object source, UserProfileWrapper userProfile) {
			onProfileSaved(userProfile);
		}
	};

	private Button btnOpenSelector;

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel) {
		this(container, name, listModel, null, (ListSetupInfoList) null);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param lsInfoList
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, ListSetupInfoList lsInfoList) {
		this(container, name, listModel, (String) null, lsInfoList);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param listSetupId
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String listSetupId) {
		this(container, name, listModel, listSetupId, (ListSetupInfoList) null);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param listSetupId
	 * @param lsInfoList
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String listSetupId,
			ListSetupInfoList lsInfoList) {
		super(container, name, listModel);
		this.setTemplateName(EntityDisplayView.class.getName());
		init();
		this.listModel = listModel;
		this.editorCreator = getEditorCreator();
		if (null != listSetupId) {
			this.listSetupID = listSetupId;
		} else {
			this.listSetupID = getListSetupID();
		}

		this.entityClass = listModel.getEntityClass();
		this.quickSearchPanel = createQuickSeachPanel();
		//		setTitle(getViewTitle());

		if (lsInfoList == null) {
			lsInfoList = new ListSetupInfoList();
			lsInfoList.addListSetupInfo(this.listSetupID, this.listSetupID);
		}

		this.lsInfoList = lsInfoList;

		createControls();
		addCustomActions();

		createListSetupCombo();

		updateButtonStatus();
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param title
	 * @param listSetupID
	 * @param editorCreator
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String title, String listSetupID,
			IEditorCreator editorCreator) {
		this(container, name, listModel, title, listSetupID, editorCreator, (ListSetupInfoList) null);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param title
	 * @param listSetupID
	 * @param editorCreator
	 * @param lsInfoList
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String title, String listSetupID,
			IEditorCreator editorCreator, ListSetupInfoList lsInfoList) {
		super(container, name, listModel);

		init();
		this.listModel = listModel;
		this.editorCreator = editorCreator;
		this.listSetupID = listSetupID;
		if (this.listSetupID == null || this.listSetupID.length() == 0) {
			this.listSetupID = "default";
		}
		this.entityClass = listModel.getEntityClass();
		this.quickSearchPanel = createQuickSeachPanel();
		//		setTitle(title);

		if (lsInfoList == null) {
			lsInfoList = new ListSetupInfoList();
			lsInfoList.addListSetupInfo(this.listSetupID, this.listSetupID);
		}

		this.lsInfoList = lsInfoList;

		createControls();
		addCustomActions();

		createListSetupCombo();

		updateButtonStatus();
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param title
	 * @param listSetupID
	 * @param editorCreator
	 * @param qsPanel
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String title, String listSetupID,
			IEditorCreator editorCreator, IQuickSearchPanel qsPanel) {
		this(container, name, listModel, title, listSetupID, editorCreator, qsPanel, (ListSetupInfoList) null);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param title
	 * @param listSetupID
	 * @param editorCreator
	 * @param qsPanel
	 * @param lsInfoList
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String title, String listSetupID,
			IEditorCreator editorCreator, IQuickSearchPanel qsPanel, ListSetupInfoList lsInfoList) {
		super(container, name, listModel);
		this.setTemplateName(EntityDisplayView.class.getName());
		init();
		this.listModel = listModel;
		this.editorCreator = editorCreator;
		this.listSetupID = listSetupID;
		if (this.listSetupID == null || this.listSetupID.length() == 0) {
			this.listSetupID = "default";
		}
		this.entityClass = listModel.getEntityClass();
		this.quickSearchPanel = qsPanel;
		//		setTitle(title);

		if (lsInfoList == null) {
			lsInfoList = new ListSetupInfoList();
			lsInfoList.addListSetupInfo(this.listSetupID, this.listSetupID);
		}

		this.lsInfoList = lsInfoList;

		createControls();
		addCustomActions();

		createListSetupCombo();

		updateButtonStatus();
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param listSetupId
	 * @param qsPanel
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String listSetupId,
			IQuickSearchPanel qsPanel) {
		this(container, name, listModel, listSetupId, qsPanel, (ListSetupInfoList) null);
	}

	/**
	 * @param container
	 * @param name
	 * @param listModel
	 * @param listSetupId
	 * @param qsPanel
	 * @param lsInfoList
	 */
	public EntityDisplayView(IControlContainer container, String name, EntityDisplayListModel listModel, String listSetupId,
			IQuickSearchPanel qsPanel, ListSetupInfoList lsInfoList) {
		super(container, name, listModel);

		init();
		this.listModel = listModel;
		this.editorCreator = getEditorCreator();
		if (null != listSetupId) {
			this.listSetupID = listSetupId;
		} else {
			this.listSetupID = getListSetupID();
		}

		this.entityClass = listModel.getEntityClass();
		this.quickSearchPanel = qsPanel;
		//		setTitle(getViewTitle());

		if (lsInfoList == null) {
			lsInfoList = new ListSetupInfoList();
			lsInfoList.addListSetupInfo(this.listSetupID, this.listSetupID);
		}

		this.lsInfoList = lsInfoList;

		createControls();

		addCustomActions();

		createListSetupCombo();

		updateButtonStatus();
	}

	protected void createListSetupCombo() {
		ToolBarGroup tg = toolbar.addRightGroup();

		btnOpenSelector = tg.addButton();
		btnOpenSelector.setTitle("View");
		btnOpenSelector.setIconEnabled(SandboxImageLibrary.IMAGE_COLUMN_SELECTOR);
		btnOpenSelector.setTooltip("Configure table settings");
		btnOpenSelector.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				tableViewer.openColumnSelector();
			}
		});

		lbListSetup = new ListBox(tg, "lbListSetup");
		lbListSetup.setWidth(250);
		lbListSetup.setSize(1);

		// now add the selection listener to the combo, for changing the
		// columns in the table
		lbListSetup.setChangeNotification(true);
		lbListSetup.addElementSelectedListener(new ElementSelectedListener() {

			@Override
			public void elementSelected(ElementSelectedEvent event) {
				// load the list setup with the given ID
				String lsId = event.getElement() == null ? "" : event.getElement().toString();

				try {
					tableViewer.saveColumnSetup();
					ListSetupInfo listSetupInfo = lsInfoList.getListSetupInfo(lsId);
					if (null != listSetupInfo) {
						String description = listSetupInfo.getDisplayName();
						updateTableListSetup(lsId, description);
					} else {
						// here we are in the case when a user list profile was
						// selected. load it
						PropertyQuery query = new PropertyQuery();
						query.addEquals("className", entityClass.getName());
						query.addEquals("profileId", lsId);

						// also load the public profiles here
						PropertyQuery subQuery = new PropertyQuery();
						subQuery.addEquals("owner", SandboxModelConfig.getMitarbeiterDAO().getByCurrentUser());
						subQuery.addOrEquals("publicProfile", true);
						QueryElement element = new QueryElement(QueryElement.AND, subQuery);

						query.addQueryElement(element);

						EntityList list = SandboxModelConfig.getUserListProfileDAO().getEntities(new Limit(0, 1), query);
						if (!list.isEmpty()) {
							IUserListProfile userLP = (IUserListProfile) list.get(0);
							UserProfileWrapper userProfileWrapper = UserListUtil.toUserProfileWrapper(userLP);
							tableViewer.setUserListProfile(userProfileWrapper);
						}
					}

					storeSelectedProfile();
				} catch (ConfigurationException e) {
					throw new RuntimeException(e);
				}

			}

		});

		loadListSetupsCombo();
	}

	public void hideColumnSelector() {
		btnOpenSelector.setVisible(false);
		lbListSetup.setVisible(false);
	}

	private void loadListSetupsCombo() {
		if (null != lsInfoList) {
			IPreferenceStore prefStore = Platform.getContextPreferenceProvider()
					.getPreferenceStore("de.xwic.appkit.webbase.UserColumnList");

			Site site = ExtendedApplication.getInstance(this).getSite();
			String key = site.getActiveModuleKey() + ";" + site.getActiveSubModuleKey();

			String selection = prefStore.getString(key + ";listSetup", "");
			lbListSetup.clear();

			for (Iterator<ListSetupInfo> iter = lsInfoList.iterator(); iter.hasNext();) {
				ListSetupInfo lsInfo = (ListSetupInfo) iter.next();

				try {
					ListSetup listSetup = ConfigurationManager.getUserProfile()
							.getListSetup(entityClass.getName(), lsInfo.getListSetupId());
					long profileId = UserListUtil.toUserProfile(listSetup, lsInfo.getDisplayName());
					IUserListProfile profile = (IUserListProfile) SandboxModelConfig.getUserListProfileDAO().getEntity(profileId);
					lbListSetup.addElement(profile.getDescription(), profile.getProfileId());

					if (selection.equals("")) {
						if (profile.getBaseProfileName().equals(listSetupID)) {
							selection = profile.getProfileId();
						}
					}
				} catch (ConfigurationException e) {
					showError(e);
					return;
				}

				// query for the existing IUserListSetup profiles
				PropertyQuery query = new PropertyQuery();
				query.addEquals("className", entityClass.getName());
				query.addEquals("baseProfileName", lsInfo.getListSetupId());

				// also add the public profiles here
				PropertyQuery subQuery = new PropertyQuery();
				subQuery.addEquals("owner", SandboxModelConfig.getMitarbeiterDAO().getByCurrentUser());
				subQuery.addOrEquals("publicProfile", true);
				QueryElement element = new QueryElement(QueryElement.AND, subQuery);

				query.addQueryElement(element);

				EntityList profiles = SandboxModelConfig.getUserListProfileDAO().getEntities(null, query);
				for (Iterator<?> it = profiles.iterator(); it.hasNext();) {
					IUserListProfile profile = (IUserListProfile) it.next();
					lbListSetup.addElement(profile.getDescription(), profile.getProfileId());
				}
			}

			if (null != selection && selection.length() > 0) {

				// set the profile manually
				PropertyQuery query = new PropertyQuery();
				query.addEquals("owner", SandboxModelConfig.getMitarbeiterDAO().getByCurrentUser());
				query.addEquals("className", entityClass.getName());
				query.addEquals("profileId", selection);

				EntityList profiles = SandboxModelConfig.getUserListProfileDAO().getEntities(null, query);
				if (profiles.size() == 1) {
					IUserListProfile userListProfile = (IUserListProfile) profiles.get(0);
					UserProfileWrapper wrapper = UserListUtil.toUserProfileWrapper(userListProfile);
					tableViewer.setUserListProfile(wrapper);
				}

				// set change notification to false, because we have already set the user profile
				// and not trigger the selection listener
				lbListSetup.setChangeNotification(false);
				//now preselect the default selection, if it exists !
				lbListSetup.setSelectedKey(selection);
				storeSelectedProfile();
				// reset change notification to true again
				lbListSetup.setChangeNotification(true);
			}
		}
	}

	/**
	 * @param lsId
	 * @throws ConfigurationException
	 */
	private void updateTableListSetup(String lsId, String description) throws ConfigurationException {
		String className = listModel.getEntityClass().getName();
		ListSetup ls = ConfigurationManager.getUserProfile().getListSetup(className, lsId);
		if (null != ls) {
			listSetupID = lsId;
			tableViewer.setListSetup(ls);

			tableViewer.setUserListProfile(getUserProfile(description));
		}
	}

	protected void setupActionBar() {
		super.setupActionBar();
	}

	/**
	 * adds custom actions
	 * 
	 * @param actionBar
	 */
	protected void addCustomActions() {
		ToolBarGroup tg = toolbar.addRightGroup();

		ExcelExportControl excelExport = new ExcelExportControl(tg, "excel", tableViewer.getTableViewer());
		excelExport.setCssClass("j-button-h j-btn-small");
		excelExport.setHeight(26);
		excelExport.setIconEnabled(ImageLibrary.ICON_EXCEL);
		excelExport.setTitle("");
		excelExport.setTooltip("Export the sheet to Excel");
	}

	private void createControls() {
		// generate the quicksearch panel first
		if (null != quickSearchPanel) {
			// add it !
			qsPanel = quickSearchPanel.createQuickSearchPanel(this, listModel);
		}

		ListSetup lSetup;

		try {
			ConfigurationManager.getSetup();
			lSetup = ConfigurationManager.getUserProfile().getListSetup(entityClass.getName(), listSetupID);
		} catch (ConfigurationException e) {
			showError(e);
			return;
		}

		tableViewer = new EntityTableViewer(this, "entityTable", listModel.getDAO(), lSetup, null);
		if (null != getDefaultLabelProvider()) {
			tableViewer.setLabelProvider(getDefaultLabelProvider());
		} else {
			tableViewer.setLabelProvider(new BaseLabelProvider());
		}

		EntityQuery query = getDefaultContentQuery();
		if (null == query) {
			query = listModel.getQuery();
		}

		tableViewer.getContentProvider().setEntityQuery(query);
		tableViewer.getToolBar().setVisible(false);
		tableViewer.addColumnSelectorListener(listener);

		TableModel model = tableViewer.getTableViewer().getModel();
		model.addElementSelectedListener(new ElementSelectedListener() {

			public void elementSelected(ElementSelectedEvent event) {
				String key = (String) event.getElement();

				if (key != null && key.length() > 0) {
					listModel.setSelectedEntryId(Integer.parseInt(key));
				}

				updateButtonStatus();
				if (event.isDblClick()) {
					performDefaultSelectionAction();
				}
			}
		});

		loadActionsFromExtensions();
	}

	/**
	 * 
	 */
	protected void loadActionsFromExtensions() {
		ToolBarGroup tg = toolbar.addGroup();

		Site site = ExtendedApplication.getInstance(this).getSite();

		Map<String, List<IEntityAction>> actionsInGroups = EntityActionsHelper.getEntityActionsInGroups(site, this, getClass().getName(),
				listModel.getEntityClass());

		for (Entry<String, List<IEntityAction>> entry : actionsInGroups.entrySet()) {
			List<IEntityAction> actions = entry.getValue();
			if (!actions.isEmpty()) {

				boolean inDropDown = false;
				// if any action in the group is in a drop down, all actions are
				for (IEntityAction action : actions) {
					if (action.isInDropDown()) {
						inDropDown = true;
						break;
					}
				}

				tg.addSpacer();

				if (inDropDown) {
					Menu menu = new Menu(tg);

					for (IEntityAction action : actions) {
						menu.addMenuItem(action);
					}

					Button btActions = tg.addButton();
					btActions.setIconEnabled(ImageLibrary.ICON_APPLICATION_CASCADE);
					btActions.setTitle(entry.getKey());
					btActions.setMenu(menu);
				} else {
					for (IEntityAction action : actions) {
						tg.addAction(action);
					}
				}
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		if (tableViewer != null) {
			tableViewer.removeColumnSelectorListener(listener);
		}
	}

	/**
	 * 
	 */
	private void storeSelectedProfile() {
		Site site = ExtendedApplication.getInstance(this).getSite();
		String key = site.getActiveModuleKey() + ";" + site.getActiveSubModuleKey();
		IPreferenceStore prefStore = Platform.getContextPreferenceProvider().getPreferenceStore("de.xwic.appkit.webbase.UserColumnList");

		try {
			prefStore.setValue(key + ";listSetup", lbListSetup.getSelectedKey());
			prefStore.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return the UserListSetup for the given user
	 */
	protected UserProfileWrapper getUserProfile(String description) {
		return this.getUserProfile(listSetupID, description);
	}

	/**
	 * @return the UserListSetup for the given user and for the given ListSetupId
	 */
	protected UserProfileWrapper getUserProfile(String pListId, String description) {
		UserListSetup setup = null;

		try {
			IPreferenceStore prefStore = Platform.getContextPreferenceProvider()
					.getPreferenceStore("de.xwic.appkit.webbase.UserColumnList");
			String fileName = getUserFileName();

			if (null == pListId || "".equals(pListId)) {
				pListId = Setup.ID_DEFAULT;
			}

			setup = UserConfigXmlReader.getUserColumnList(prefStore.getString(fileName, ""));
			if (null == setup) {
				// the directory and the file was now created
				// add the default visible columns
				setup = getDefaultVisibleColumns();
			}
		} catch (Exception e) {
			log.error("Error while loading user list setup", e);
			return null;
		}

		return UserListUtil.toUserProfileWrapper(setup, description);
	}

	/**
	 * @return the EntityDescriptor for the current entity type
	 */
	public EntityDescriptor getEntityDescriptor() {
		EntityDescriptor ed = null;

		try {
			ed = ConfigurationManager.getSetup().getEntityDescriptor(entityClass.getName());
		} catch (ConfigurationException e) {
			log.error("No configuration found", e);
		}

		return ed;
	}

	/**
	 * @return a user list setup with the default visible columns
	 */
	private UserListSetup getDefaultVisibleColumns() {
		UserListSetup userList = UserListUtil.toUserListSetup(getListSetup());

		try {
			// UserConfigXmlReader.setUserColumnList(userList);
		} catch (Throwable e) {
			log.error("Exception occured while saving preferences !", e);
		}

		return userList;
	}

	/**
	 * @return the column setup for the setted list setup id
	 */
	protected ListSetup getListSetup() {
		return getListSetup(listSetupID);
	}

	/**
	 * @param pListId
	 * @return the column setup of the given listSetupId
	 */
	protected ListSetup getListSetup(String pListId) {
		ListSetup listSetup;

		try {
			if (null == pListId || "".equals(pListId)) {
				pListId = Setup.ID_DEFAULT;
			}

			listSetup = ConfigurationManager.getUserProfile().getListSetup(entityClass.getName(), pListId);
			return listSetup;
		} catch (ConfigurationException e) {
			log.error("No configuration found");
		}

		return null;
	}

	private String getUserFileName() {
		String str = "";

		if (null == listSetupID || "".equals(listSetupID)) {
			listSetupID = Setup.ID_DEFAULT;
		}
		str = entityClass.getName() + "_" + listSetupID + "_" + ID_USER + ".xml";

		return str;
	}

	/**
	 * 
	 */
	protected void performDefaultSelectionAction() {
		if (canEdit()) {
			performEdit();
		}
	}

	protected abstract String getViewTitle();

	protected abstract void init();

	/**
	 * subclasses should override this method when wanting to display the quicksearch panel
	 * 
	 * @return
	 */
	protected IQuickSearchPanel createQuickSeachPanel() {
		return null;
	}

	/**
	 * @param selectedEntityId
	 */
	protected void openEditor(int selectedEntityId) {
		if (null == editorCreator) {
			editorCreator = getEditorCreator();
		}

		if (null != editorCreator) {
			// open the edit sheet data
			Site site = ExtendedApplication.getInstance(this).getSite();
			DAO dao = listModel.getDAO();
			IEntity entity = null;

			if (selectedEntityId > 0) {
				entity = dao.getEntity(selectedEntityId);
			} else {
				entity = createNewEntity(dao);
			}

			EditorModel model = getCustomModel(entity);
			if (null == model) {
				model = new EditorModel(entity, isEditorEditable());
			}

			model.setBaseEntity(listModel.getBaseEntity());

			InnerPage page = editorCreator.createEditorPage(site.getContentContainer(), model);
			if (null != page) {
				site.pushPage(page);
			}
		}
	}

	/**
	 * @param dao
	 * @return a new Entity
	 */
	protected IEntity createNewEntity(DAO dao) {
		return dao.createEntity();
	}

	protected String getListSetupID() {
		return "default";
	}

	/**
	 * @return an editor creator. can be null
	 */
	protected abstract IEditorCreator getEditorCreator();

	/**
	 * @return a custom label provider. to be overridden by extending classes.
	 */
	protected PropertyLabelProvider getDefaultLabelProvider() {
		return null;
	}

	protected boolean isEditorEditable() {
		return true;
	}

	/**
	 * @param entity
	 * @return a custom implementation for the EditorModel
	 */
	protected EditorModel getCustomModel(IEntity entity) {
		return null;
	}

	/**
	 * @return
	 * @return a custom query for the content provider. to be overridden by extending classes.
	 */
	protected EntityQuery getDefaultContentQuery() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractListContainer#performDelete ()
	 */
	@Override
	public void performDelete() {
		Collection<?> sel = tableViewer.getTableViewer().getModel().getSelection();
		if (!sel.isEmpty()) {
			String key = (String) sel.iterator().next();
			try {
				int id = Integer.parseInt(key);
				listModel.delete(id);

				tableViewer.getTableViewer().getModel().clearSelection();
				listModel.setSelectedEntryId(-1);
				tableViewer.getTableViewer().setRequireRedraw(true);
			} catch (Exception ex) {
				showError(ex);
			}
		}

		updateButtonStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractListContainer#performEdit ()
	 */
	@Override
	public boolean performEdit() {
		// the key is the id of the object.
		String key = tableViewer.getTableViewer().getModel().getFirstSelectedKey();

		if (key != null && key.length() > 0) {
			int selectedEntityId = Integer.parseInt(key);
			openEditor(selectedEntityId);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractListContainer#performNew ()
	 */
	@Override
	public void performNew() {
		// signalize it's a new entity
		int selectedEntityId = -1;
		openEditor(selectedEntityId);
	}

	@Override
	public boolean canEdit() {
		boolean hasRight = super.canEdit();
		Collection<String> sel = tableViewer.getTableViewer().getModel().getSelection();
		return hasRight && sel != null && !sel.isEmpty();
	}

	@Override
	public boolean canDelete() {
		boolean hasRight = super.canDelete();
		Collection<String> sel = tableViewer.getTableViewer().getModel().getSelection();
		return hasRight && sel != null && !sel.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.util.SerObserver#update(de.jwic.util.SerObservable, java.lang.Object)
	 */
	@Override
	public void update(SerObservable o, Object arg) {
		if (tableViewer != null) { // ignore updates as long as the control is
			// not fully initialized.
			if (o instanceof EntityDisplayListModel) {
				// query was reset in the model. reset the GUI too
				EntityDisplayListModel model = (EntityDisplayListModel) o;
				setContentQuery(model.getQuery());
			}

			updateButtonStatus();
		}
	}

	/**
	 * @param enabled
	 * @param updateStatus
	 *            true if the buttons state should be even more updated, even if enabled is true, to the default behaviour
	 */
	public void changeEntityActionsEnablement(boolean enabled, boolean updateStatus) {
		btDelete.setEnabled(enabled);
		btNew.setEnabled(enabled);
		btEdit.setEnabled(enabled);

		if (enabled && updateStatus) {
			updateButtonStatus();
		}
	}

	/**
	 * @return the listModel
	 */
	public EntityDisplayListModel getListModel() {
		return listModel;
	}

	/**
	 * resets the content query and refreshes the table
	 * 
	 * @param query
	 */
	public void setContentQuery(EntityQuery query) {
		tableViewer.getContentProvider().setEntityQuery(query);
		tableViewer.getTableViewer().getModel().pageFirst(); // go back to first
		// page.
		refreshTable();
	}

	/**
	 * refreshes the table
	 */
	public void refreshTable() {
		tableViewer.getTableViewer().setRequireRedraw(true);
	}

	/**
	 * @return
	 */
	public String getQuickSearchControlName() {
		return null != qsPanel ? qsPanel.getName() : "";
	}

	/**
	 * @return
	 */
	public IEntity getEntity() {
		String key = tableViewer.getTableViewer().getModel().getFirstSelectedKey();

		if (key != null && key.length() > 0) {
			int selectedEntityId = Integer.parseInt(key);
			DAO dao = listModel.getDAO();
			if (selectedEntityId > 0) {
				return dao.getEntity(selectedEntityId);
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getBaseEntity()
	 */
	@Override
	public IEntity getBaseEntity() {
		return listModel.getBaseEntity();
	}

	@Override
	protected void updateButtonStatus() {
		if (btNew != null) {
			btNew.setEnabled(canNew());
		}
		if (btEdit != null) {
			btEdit.setEnabled(canEdit() && hasEntity());
		}
		if (btDelete != null) {
			btDelete.setEnabled(canDelete() && hasEntity());
		}
	}

	protected void onProfileSaved(UserProfileWrapper userProfile) {
		internalUpdateSetup(userProfile, true);
	}

	protected void onNewProfile(UserProfileWrapper newUserProfile) {
		internalUpdateSetup(newUserProfile, false);
	}

	private void internalUpdateSetup(UserProfileWrapper newUserProfile, boolean updated) {
		if (null == lsInfoList) {
			lsInfoList = new ListSetupInfoList();
		}

		// new profile saved, select it
		String listId = newUserProfile.getProfileId();
		this.listSetupID = listId;

		lbListSetup.addElement(newUserProfile.getDescription(), listId);
		lbListSetup.setSelectedKey(listId);

		if (updated) {
			// now remove the element which was added manually. we already have
			// it in the DB, as a user list profile
			lsInfoList.removeSetup(newUserProfile.getBaseProfileName());
			lbListSetup.removeElementByKey(newUserProfile.getBaseProfileName());
		}
	}

	// ****************** FROM IEntityProvider *********************************

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getEntityKey()
	 */
	@Override
	public String getEntityKey() {
		return tableViewer.getTableViewer().getModel().getFirstSelectedKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#hasEntity()
	 */
	public boolean hasEntity() {
		return !tableViewer.getTableViewer().getModel().getSelection().isEmpty();
	}

	// FLI - this method exists in the superclass and 'seems' to do what needs to be done
	//	private void showError(Exception t){
	//		errorWarning.showError(t);
	//	}

}
