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

import de.xwic.appkit.core.config.Version;

/**
 * Upgrade modules are registered with the InstallationManager to perform specific upgrades or database fixes.
 * 
 * @author Florian Lippisch
 */
public interface IUpgradeModule {

	/**
	 * Returns true if this module can only be executed directly. Direct call modules are not executed automatically during migration or
	 * integrity check.
	 * 
	 * @return
	 */
	public boolean isDirectCall();

	/**
	 * Must return true if this module applies to the speicfied product id.
	 * 
	 * @param productId
	 * @return
	 */
	public boolean appliesToProduct(String productId);

	/**
	 * Returns true if this module applies to a migration from the specified version to the target version.
	 * 
	 * @param fromVersion
	 * @param toVersion
	 * @return
	 */
	public boolean appliesToVersion(Version fromVersion, Version toVersion);

	/**
	 * Returns the name of the module.
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Executes the module.
	 * 
	 * @param manager
	 * @throws Exception
	 */
	public void run(InstallationManager manager) throws Exception;

}
