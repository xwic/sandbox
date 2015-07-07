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
package de.xwic.sandbox.util.ui;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.editors.ValidationException;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author bogdan
 *
 */
public abstract class ClosableDialogWindow extends AbstractDialogWindow implements IWindowManipulator {

	private final List<IOnOkListener> listeners = new ArrayList<IOnOkListener>();

	private boolean okVisible;

	/**
	 * @param site
	 * @param name
	 */
	public ClosableDialogWindow(final Site site) {
		super(site);
	}

	@Override
	protected void createControls() {
		super.createControls();
		this.btOk.setVisible(okVisible);
		this.btCancel.setVisible(true);
		this.setCloseable(true);
	}

	
	/* (non-Javadoc)
	 * @see de.xwic.sandbox.util.ui.IWindowManipulator#addActionOnOk(de.xwic.sandbox.util.ui.IWindowManipulator.IOnOkListener)
	 */
	@Override
	public void addActionOnOk(final IOnOkListener listener) {
		listeners.add(listener);
		okVisible = true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#onOk()
	 */
	@Override
	protected void onOk() {
		try {
			for (final IOnOkListener listener : listeners) {
				listener.onOk();
			}
		} catch (ValidationException e) {
			NotificationUtil.showError(e.getMessage(), getSessionContext());
			return;
		}

		super.onOk();
	}
}
