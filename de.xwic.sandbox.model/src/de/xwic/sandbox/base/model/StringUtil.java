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
package de.xwic.sandbox.base.model;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.webbase.editors.ValidationException;

/**
 * @author Raluca Geogia
 *
 */
public class StringUtil {

	private static Log log = LogFactory.getLog(StringUtil.class);

	/**
	 *
	 */
	public interface Serializer<T extends Object> {

		String serialize(T item);
	}

	/**
	 * Returns the 2nd parameter 'nullValue' if the first value is null.
	 * 
	 * @param object
	 * @param nullValue
	 * @return
	 */
	public static String nvl(String object, String nullValue) {
		return object == null ? nullValue : object;
	}

	/**
	 * Returns an empty string if the object is null, otherwise the object.
	 * 
	 * @param object
	 * @return
	 */
	public static String nvlEmptyString(String object) {
		return object == null ? "" : object;
	}

	/**
	 * @param o
	 * @return
	 */
	public static String getNotNull(Object o) {
		return o == null ? "" : o.toString();
	}

	/**
	 * @param o
	 * @return
	 */
	public static String getNotNullTrim(Object o) {
		return o == null ? "" : o.toString().trim();
	}

	/**
	 * @param strToSplit
	 * @param tokens
	 * @return
	 */
	public static Collection<String> splitSortedUnique(String strToSplit, String tokens) {
		Set<String> hashSet = new HashSet<String>(splitString(strToSplit, tokens));
		List<String> arrayList = new ArrayList<String>(hashSet);
		Collections.sort(arrayList);
		return arrayList;
	}

	/**
	 * @param s
	 * @return
	 */
	public static int getUtf8BytesLength(String s) {

		if (s == null) {
			return 0;
		}

		try {
			return s.getBytes("UTF-8").length;
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}

		return -1;
	}

	/**
	 * @param s
	 * @param length
	 * @return
	 */
	public static String trimToSize(String s, int length) {
		if (isEmpty(s)) {
			return s;
		}
		int trimTo = s.length() < length ? s.length() : length;
		return s.substring(0, trimTo);
	}

	/**
	 * Replaces all the funky characters with normal ASCII
	 * 
	 * @param s
	 * @return
	 */
	public static String normalize(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}

	/**
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}

	/**
	 * @param head
	 * @param tail
	 * @return
	 */
	public static boolean allEmpty(final String head, final String... tail) {
		if (!isEmpty(head)) {
			return false;
		}
		for (final String string : tail) {
			if (!isEmpty(string)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param head
	 * @param tail
	 * @return
	 */
	public static boolean anyEmpty(final String head, final String... tail) {
		if (isEmpty(head)) {
			return true;
		}
		for (final String string : tail) {
			if (isEmpty(string)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param strToSplit
	 * @param tokens
	 * @return
	 */
	public static List<String> splitString(String strToSplit, String tokens) {
		List<String> result = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(strToSplit, tokens);

		while (st.hasMoreTokens()) {
			result.add(st.nextToken());
		}

		return result;
	}

	/**
	 * @param strToSplit
	 * @param tokens
	 * @return
	 */
	public static Collection<String> splitNoEmptyLinesTrim(String strToSplit, String tokens) {
		Collection<String> strings = new HashSet<String>();
		List<String> splitString = splitString(strToSplit, tokens);
		for (String string : splitString) {
			string = string.trim();
			if (!string.isEmpty()) {
				strings.add(string);
			}
		}
		return strings;
	}

	/**
	 * @param s
	 * @return
	 */
	public static String eliminateNonLiteralCharacters(String s) {
		String newS = s.replaceAll("[^a-zA-Z0-9]", "_").replaceAll("__", "_");
		return newS;
	}

	/**
	 * @param emailsString
	 * @return list of emails computed from the emailsString parameter
	 */
	public static List<String> parseEmails(String emailsString) {
		// preserve order
		Set<String> emails = new LinkedHashSet<String>();

		if (emailsString != null && !emailsString.trim().isEmpty()) {
			String[] arr = emailsString.split(";");

			for (String str : arr) {
				str = str.trim();
				if (!str.isEmpty() && !emails.contains(str)) {
					//	if (isValidEmailAddress(str)) {
						emails.add(str);
					//		} else {
					//			log.warn("Found bad address in '".concat(emailsString).concat("' : ").concat(str));
					//		}
				}
			}
		}

		return new ArrayList<String>(emails);
	}

	/**
	 * escapes Html and then replaces all \n and \r\n with <br />
	 * 
	 * @param string
	 * @return "" if string is <code>null</code>
	 */
	public static String toHtmlString(String string) {
		if (string == null) {
			return "";
		}

		String ds = StringEscapeUtils.escapeHtml(string);
		String replaceAll = ds.replaceAll("(\n\r)|\n", "<br />");

		return replaceAll;
	}

	/**
	 * 
	 * escapes Html and then replaces all \n and \r\n with <br />
	 * 
	 * @param input
	 * @return "" if string is <code>null</code>
	 */
	public static String toHtmlStringKeepBrs(final String input) {
		if (isEmpty(input)) {
			return "";
		}
		String escapedBrs = input.replace("<br>", "\n").replace("<br />", "\n");
		return toHtmlString(escapedBrs);
	}

	/**
	 * @param s0
	 * @param s1
	 * @return
	 */
	public static boolean isEqualWithNullCheck(String s0, String s1) {
		if (s0 == null) {
			return s1 == null;
		}
		if (isEmpty(s0) && isEmpty(s1)) {
			return true;
		}
		return s0.equals(s1);
	}

	/**
	 * @param text
	 * @param ch
	 * @param totalSize
	 * @return
	 */
	public static String padRight(String text, char ch, int totalSize) {
		StringBuilder sb = new StringBuilder(text);

		while (sb.length() < totalSize) {
			sb.append(ch);
		}

		return sb.toString();
	}

	/**
	 * @param text
	 * @param ch
	 * @param totalSize
	 * @return
	 */
	public static String padLeft(String text, char ch, int totalSize) {
		StringBuilder sb = new StringBuilder();

		int max = totalSize - text.length();

		while (sb.length() < max) {
			sb.append(ch);
		}

		sb.append(text);

		return sb.toString();
	}

	//
	// ******************** PSA ********************
	//

	/**
	 * @param value
	 * @param maxLength
	 * @param allowNewLine
	 * @param fieldName
	 * @return
	 * @throws Exception
	 */
	public static String validateForPsa(String value, int maxBytesLength, boolean allowNewLine, String fieldName) throws Exception {

		if (value == null || value.isEmpty()) {
			return value;
		}

		String s = makePsaString(value.trim(), allowNewLine);

		int bytesLength = getUtf8BytesLength(s);
		if (bytesLength > maxBytesLength) {
			throw new Exception("The maximum length of the " + fieldName + " field is " + maxBytesLength + " bytes. Actual length: "
					+ bytesLength);
		}

		return s;
	}

	/**
	 * @param value
	 * @param maxBytesLength
	 * @param allowNewLine
	 * @return
	 */
	public static boolean validForPsa(String value, int maxBytesLength, boolean allowNewLine) {
		if (value == null || value.isEmpty()) {
			return false;
		}

		String s = makePsaString(value.trim(), allowNewLine);

		return getUtf8BytesLength(s) <= maxBytesLength;
	}

	/**
	 * @param s
	 * @param allowNewLine
	 * @return
	 */
	public static String makePsaString(String s, boolean allowNewLine) {

		String newS = s.replace("'", " ").replace("`", " ").replace("�", " ").replace("\"", " ");

		if (allowNewLine) {
			newS = newS.replace("\n", "\\n").replace("\r", "\\n");
		} else {
			newS = newS.replace('\n', ' ').replace('\r', ' ');
		}

		return newS;
	}

	/**
	 * @param longName
	 * @param maxLen
	 * @return
	 */
	public static String getProjectName(String longName, int maxLen) {
		String shortName = longName;
		if (shortName.length() > maxLen) {
			shortName = shortName.substring(0, maxLen);
		}
		while (!StringUtil.validForPsa(shortName, maxLen, false)) {
			int length = shortName.length();
			if (length == 0) {
				throw new IllegalStateException("Cannot generate short name from long name.");
			}
			shortName = shortName.substring(0, length - 1);
		}
		return shortName;
	}

	//
	// ******************** JOIN ********************
	//

	/**
	 * @param items
	 * @param separator
	 * @param wrapper
	 * @return
	 */
	public static String join(Iterable<?> items, String separator, String wrapper) {
		StringBuilder buff = new StringBuilder();
		String sep = "";
		for (Object str : items) {
			buff.append(sep);
			buff.append(wrapper);
			buff.append(str.toString());
			buff.append(wrapper);
			sep = separator;
		}
		return buff.toString();
	}

	/**
	 * builds a (separator) separated String containing all the values in (strings)
	 * 
	 * @param strings
	 * @param separator
	 * @return
	 */
	public static String join(Iterable<?> strings, String separator) {
		StringBuilder buff = new StringBuilder();
		String sep = "";
		for (Object str : strings) {
			buff.append(sep);
			buff.append(str);
			sep = separator;
		}
		return buff.toString();
	}

	/**
	 * @param objects
	 * @param separator
	 * @param whatIfNothing
	 * @return
	 */
	public static String joinDefault(final Collection<?> objects, final String separator, final String whatIfNothing) {
		if (CollectionUtil.isEmpty(objects)) {
			return whatIfNothing;
		}
		final String join = join(objects, separator);
		if (!join.isEmpty()) {
			return join;
		}
		return whatIfNothing;
	}

	/**
	 * builds a (separator) separated String containing all the values in (items) serialized with the given serializer
	 * 
	 * @param strings
	 * @param serializer
	 * @param separator
	 * @return
	 */
	public static <T> String join(Iterable<T> items, Serializer<T> serializer, String separator) {
		StringBuilder buff = new StringBuilder();
		String sep = "";
		for (T item : items) {
			buff.append(sep);
			buff.append(serializer.serialize(item));
			sep = separator;
		}
		return buff.toString();
	}

	/**
	 * @param maybeNumber
	 *            parameter to check
	 * @return true only if the string contains only digits or false if it's null, empty or has any other characters (even whitespace)
	 */
	public static boolean isNumber(final String maybeNumber) {
		if (isEmpty(maybeNumber)) {
			return false;
		}
		for (final char c : maybeNumber.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return
	 */
	public static String messageFromValidationException(ValidationException ex) {
		StringBuilder msg = new StringBuilder();
		ValidationException validationException = ex;

		ValidationResult result = validationException.getResult();
		if (result == null) {
			return validationException.getMessage();
		} else {
			Set<String> keys = result.getErrorMap().keySet();

			for (String string : keys) {
				// TODO dotto: needs to be reemplemented by a bundle solution.
				//msg.append(string + ": " + bundleAdmin.getString(res.getErrorMap().get(string)));
				msg.append(string + ": " + result.getErrorMap().get(string));
				msg.append("<br>");
			}

			return msg.toString();
		}
	}

	//	public static boolean isValidEmailAddress(final String email) {
	//		if (isEmpty(email)) {
	//			return false;
	//		}
	//		try {
	//			new InternetAddress(email).validate();
	//			return true;
	//		} catch (AddressException e) {
	//			return false;
	//		}
	//	}

	/**
	 * @param string
	 * @return
	 */
	public static List<String> getNotEmptyLines(final String string) {
		if (string.isEmpty()) {
			return Collections.emptyList();
		}
		final String[] split = string.split("\n");
		final List<String> strings = new ArrayList<String>();
		for (final String piece : split) {
			final String trim = piece.trim();
			if (!trim.isEmpty()) {
				strings.add(trim);
			}
		}
		return Collections.unmodifiableList(strings);
	}

	/**
	 * @param input
	 * @return
	 */
	public static String removeDuplicateWhitespace(final String input) {
		if (null == input) {
			return "";
		}
		return input.trim().replaceAll("\\s+", " ");
	}

}
