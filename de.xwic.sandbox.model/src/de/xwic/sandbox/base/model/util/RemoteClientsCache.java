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
package de.xwic.sandbox.base.model.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Adrian Ionescu
 */
public class RemoteClientsCache {

	private Map<String, RemoteClient> clients = new HashMap<String, RemoteClient>();

	private static RemoteClientsCache instance;
	
	/**
	 * 
	 */
	private RemoteClientsCache() {
		loadClientsConfig();
	}
	
	/**
	 * @return
	 */
	public static RemoteClientsCache instance() {
		if (instance == null) {
			instance = new RemoteClientsCache();
		}
		
		return instance;
	}
	
	/**
	 * 
	 */
	public void loadClientsConfig() {
		String config = ConfigurationUtil.getConfigurationProperty("remoteClients", "");
		
		if (!config.isEmpty()) {
			StringTokenizer st = new StringTokenizer(config, "\n\t");
			while (st.hasMoreTokens()) {
				String rawItem = st.nextToken().trim();
				String[] split = rawItem.split(":");
				if (split.length == 2) {
					String id = split[0];
					String name = split[1];
					
					clients.put(id, new RemoteClient(id, name));
				}
			}
		}
		
	}
	
	/**
	 * @param id
	 * @return
	 */
	public RemoteClient getRemoteClient(String id) {
		return clients.get(id);
	}
	
	
	/**
	 * @author Adrian Ionescu
	 */
	public class RemoteClient {
		private String id;
		private String name;
		
		/**
		 * @param id
		 * @param name
		 */
		public RemoteClient(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return the name
		 */
		public String getUsername() {
			return name;
		}
	}
	
}
