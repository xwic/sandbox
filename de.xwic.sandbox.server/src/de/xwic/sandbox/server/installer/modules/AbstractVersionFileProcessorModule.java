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
package de.xwic.sandbox.server.installer.modules;

import java.io.File;
import java.io.FilenameFilter;

import de.xwic.appkit.core.config.Version;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;

/**
 * @author Claudiu Mateias
 *
 */
public abstract class AbstractVersionFileProcessorModule extends AbstractUpgradeModule {

	protected static final String INSTALLER_FOLDER = "installer_files" + File.separator;
	protected String versionKey;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.AbstractUpgradeModule#appliesToVersion(de.xwic.appkit.core.config.Version,
	 * de.xwic.appkit.core.config.Version)
	 */
	@Override
	public boolean appliesToVersion(Version fromVersion, Version toVersion) {
		this.versionKey = String.format("%d%d%d_%d%d%d", fromVersion.getMajor(), fromVersion.getMinor(), fromVersion.getPatch(),
				toVersion.getMajor(), toVersion.getMinor(), toVersion.getPatch());

		return true;
	}

	/**
	 * @return
	 */
	protected abstract String getMyFolder();

	/**
	 * @return
	 */
	protected File[] getMyFiles() {
		File folder = new File(INSTALLER_FOLDER + getMyFolder());
		if (!folder.exists() || !folder.isDirectory()) {
			return null;
		}
		File[] files = folder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				// if the module is ran manually it will have no version key so we accept all files
				if (versionKey != null) {
					return versionKey.equals(name.substring(0, 7));
				}
				return true;
			}
		});
		return files;
	}

}
