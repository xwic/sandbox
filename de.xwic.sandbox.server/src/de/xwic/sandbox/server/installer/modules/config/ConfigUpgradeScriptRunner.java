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

package de.xwic.sandbox.server.installer.modules.config;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.xwic.appkit.core.config.Version;
import de.xwic.sandbox.server.installer.AbstractUpgradeModule;
import de.xwic.sandbox.server.installer.InstallationManager;

/**
 * @author lippisch
 */
public class ConfigUpgradeScriptRunner extends AbstractUpgradeModule {

	private static final String PREFIX = "ConfigUpdateScript_";
	private static final String SUFFIX = ".groovy";
	private static final String FILE_NAME = PREFIX + "%s_%s" + SUFFIX;
	private static final Comparator<File> SORT_DESCENDING = new Comparator<File>() {

		@Override
		public int compare(final File o1, final File o2) {
			return -o1.getName().compareTo(o2.getName());
		}
	};

	private Version fromVersion;
	private Version toVersion;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.AbstractUpgradeModule#appliesToVersion(de.xwic.appkit.core.config.Version,
	 * de.xwic.appkit.core.config.Version)
	 */
	@Override
	public boolean appliesToVersion(final Version fromVersion, final Version toVersion) {
		this.fromVersion = fromVersion;
		this.toVersion = toVersion;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.IUpgradeModule#run(de.xwic.sandbox.server.installer.InstallationManager)
	 */
	@Override
	public void run(final InstallationManager manager) throws Exception {

		// the file name should contain the current version and next version

		final File config = new File(manager.getSettings().getConfigDir(), "updateScripts");
		final File file = getFile(config, manager.isOverrideCheck());
		if (!file.exists()) {
			log.info(file.getAbsolutePath() + "' is missing");
			return;
		}

		final Binding binding = new Binding();
		binding.setVariable("manager", manager);
		binding.setVariable("fromVersion", fromVersion);
		binding.setVariable("toVersion", toVersion);
		binding.setVariable("ctx", new ConfigUpgradeScriptHelper(manager));

		final GroovyShell shell = new GroovyShell(binding);
		shell.evaluate(file);

	}

	/**
	 * @param sbFileName
	 * @param config
	 * @param b
	 * @return
	 */
	private File getFile(final File config, final boolean overrideCheck) {
		if (!config.exists() || !config.isDirectory()) {
			return config;
		}
		final File[] files = config.listFiles();
		if (null == files || files.length == 0) {
			return config;
		}
		final String name = String.format(FILE_NAME, getVersion(fromVersion), getVersion(toVersion));
		final File file = new File(config, name);
		if (file.exists() || !overrideCheck) {
			return file;
		}
		return getLatestFile(file, files);
	}

	/**
	 * @param file
	 * @param files
	 * @return
	 */
	private File getLatestFile(final File file, final File[] files) {
		final List<File> fileList = new ArrayList<File>(Arrays.asList(files));
		Collections.sort(fileList, SORT_DESCENDING);
		for (final File f : fileList) {
			if (isAcceptable(f)) {
				log.info("Latest acceptable file is " + f.getAbsolutePath());
				return f;
			}
		}
		return file;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean isAcceptable(final File file) {
		final String name = file.getName();
		if (!name.startsWith(PREFIX)) {
			return false;
		}
		if (!name.endsWith(SUFFIX)) {
			return false;
		}
		final String replace = name.replace(PREFIX, "").replace(SUFFIX, "");
		//		###_###
		if (replace.length() != "###_###".length()) {
			return false;
		}
		final String[] split = replace.split("_");
		if (2 != split.length) {
			return false;
		}
		return split[1].equals(getVersion(toVersion));
	}

	/**
	 * @param version
	 * @return
	 */
	private static String getVersion(final Version version) {
		final StringBuilder sb = new StringBuilder();
		sb.append(version.getMajor());
		sb.append(version.getMinor());
		return sb.append(version.getPatch()).toString();
	}
}
