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
package de.xwic.sandbox.basegui.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Raluca Geogia
 *
 */
public class ListSetupInfoList {

	private Map<String, ListSetupInfo> listSetups;

	public ListSetupInfoList() {
		listSetups = new LinkedHashMap<String, ListSetupInfo>();
	}

	/**
	 * @param lsInfo
	 */
	public void addListSetupInfo(ListSetupInfo lsInfo) {
		listSetups.put(lsInfo.getListSetupId(), lsInfo);
	}

	/**
	 * @param listSetupId
	 * @param displayName
	 */
	public void addListSetupInfo(String listSetupId, String displayName) {
		addListSetupInfo(new ListSetupInfo(listSetupId, displayName));
	}

	/**
	 * @return an iterator over the list
	 */
	public Iterator<ListSetupInfo> iterator() {
		return listSetups.values().iterator();
	}

	/**
	 * @return the size of the list
	 */
	public int size() {
		return listSetups.values().size();
	}

	/**
	 * @return the list
	 */
	public Collection<ListSetupInfo> getList() {
		return listSetups.values();
	}

	/**
	 * @param listSetupId
	 * @return
	 */
	public ListSetupInfo getListSetupInfo(String listSetupId) {
		return listSetups.get(listSetupId);
	}

	/**
	 * @param listSetupId
	 */
	public void removeSetup(String listSetupId) {
		listSetups.remove(listSetupId);
	}

	/**
	 * @param key
	 * @return
	 */
	public boolean contains(String key) {
		return listSetups.containsKey(key);
	}
}
