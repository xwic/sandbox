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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.sandbox.server.installer.InstallationManager;
import de.xwic.sandbox.server.installer.StringUtil;

/**
 * @author Adrian Ionescu
 */
public class RunSQLScriptsModule extends AbstractVersionFileProcessorModule {

	private static final String SQLS_FOLDER = "sqls";

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.IUpgradeModule#run(de.xwic.sandbox.server.installer.InstallationManager)
	 */
	@Override
	public void run(InstallationManager manager) throws Exception {
		File[] files = getMyFiles();		

		if (files == null || files.length == 0) {
			log.info("No SQL files found for version key '" + versionKey + "', exiting..");
			return;
		}

		for (File file : files) {
			Date start = new Date();

			log.info("Executing: " + file.getName());

			int updated = -1;

			SqlFile sqlFile = readAll(file);

			switch (sqlFile.type) {
			case MULTIPLE_ONE_PER_LINE:
				updated = processOnePerLine(sqlFile);
				break;
			case SINGLE_QUERY:
			default:
				updated = processSingleUpdate(sqlFile);
				break;
			}

			log.info("Total updated records: " + updated);

			long duration = new Date().getTime() - start.getTime();
			String strDuration;
			if (duration > 1000) {
				strDuration = duration / 1000 + " s";
			} else {
				strDuration = duration + " ms";
			}
			log.info("Duration: " + strDuration);
		}
	}

	/**
	 * @param sqlFile
	 */
	private int processSingleUpdate(SqlFile sqlFile) {
		Session session = HibernateUtil.currentSession();
		Transaction t = session.beginTransaction();

		Query query = session.createSQLQuery(sqlFile.content);
		int updated = query.executeUpdate();

		t.commit();

		return updated;
	}

	/**
	 * @param sqlFile
	 */
	private int processOnePerLine(SqlFile sqlFile) {
		int updated = 0;
		int numberOfValidLines = 0;

		Session session = HibernateUtil.currentSession();
		Transaction t = session.beginTransaction();

		// one line has one update
		List<String> lines = StringUtil.splitString(sqlFile.content, "\n\r");
		for (String line : lines) {
			if (StringUtil.isEmpty(line) || line.startsWith("--")) {
				continue;
			}

			if (line.endsWith(";")) {
				line = line.substring(0, line.length() - 1);
			}

			// so that HBN doesn't think it's a parameter
			line.replaceAll(":", "\\:");

			Query query = session.createSQLQuery(line);
			int localUpdated = query.executeUpdate();

			updated += localUpdated;
			numberOfValidLines++;

			log.info("Line " + numberOfValidLines + " updated records: " + localUpdated);
		}

		t.commit();

		log.info("Found " + numberOfValidLines + " valid SQL lines");

		return updated;
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private SqlFile readAll(File file) throws Exception {
		SqlFile sqlFile = new SqlFile();

		InputStream in = new FileInputStream(file);

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String s;
		StringBuilder sb = new StringBuilder();

		boolean firstLine = true;

		while ((s = reader.readLine()) != null) {

			if (firstLine) {
				// first line in the file tells us what it contains
				String strFileType = s.replace("--", "").trim();
				sqlFile.type = FileTypes.valueOf(strFileType);

				firstLine = false;
				continue;
			}

			sb.append(s).append("\n");
		}
		reader.close();

		in.close();

		sqlFile.content = sb.toString();

		return sqlFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.sandbox.server.installer.modules.AbstractVersionFileProcessorModule#getMyFolder()
	 */
	@Override
	protected String getMyFolder() {
		return SQLS_FOLDER;
	}

	/**
	 * @author Adrian Ionescu
	 */
	private class SqlFile {

		FileTypes type;
		String content;
	}

	/**
	 * @author Adrian Ionescu
	 */
	private enum FileTypes {
		SINGLE_QUERY, MULTIPLE_ONE_PER_LINE;
	}
}
