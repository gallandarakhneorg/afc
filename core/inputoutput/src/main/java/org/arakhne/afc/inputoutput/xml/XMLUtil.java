/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.inputoutput.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.google.common.base.Strings;
import org.eclipse.xtext.xbase.lib.Pure;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.arakhne.afc.inputoutput.xml.XMLResources.Entry;
import org.arakhne.afc.vmutil.ClassLoaderFinder;
import org.arakhne.afc.vmutil.ColorNames;
import org.arakhne.afc.vmutil.FileSystem;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Utility class for manipulating XML data and files.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:classfanoutcomplexity", "checkstyle:classdataabstractioncoupling"})
public final class XMLUtil {

	/** {@code &lt;resource /&gt;}. */
	public static final String NODE_RESOURCE = "resource"; //$NON-NLS-1$

	/** {@code &lt;resources /&gt;}. */
	public static final String NODE_RESOURCES = "resources"; //$NON-NLS-1$

	/** Format of the date in XML files.
	 * The dateTime is specified in the following form "YYYY-MM-DDThh:mm:ss" where:<ul>
	 * <li>YYYY indicates the year</li>
	 * <li>MM indicates the month</li>
	 * <li>DD indicates the day</li>
	 * <li>T indicates the start of the required time section</li>
	 * <li>hh indicates the hour</li>
	 * <li>mm indicates the minute</li>
	 * <li>ss indicates the second</li>
	 * </ul>
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"; //$NON-NLS-1$

	/** {@code id=""}.
	 */
	public static final String ATTR_ID = "id"; //$NON-NLS-1$

	/** {@code name=""}.
	 */
	public static final String ATTR_NAME = "name"; //$NON-NLS-1$

	/** {@code color=""}.
	 */
	public static final String ATTR_COLOR = "color"; //$NON-NLS-1$

	/** {@code url=""}. */
	public static final String ATTR_URL = "url"; //$NON-NLS-1$

	/** {@code mime=""}. */
	public static final String ATTR_MIMETYPE = "mime"; //$NON-NLS-1$

	/** {@code file=""}. */
	public static final String ATTR_FILE = "file"; //$NON-NLS-1$

	private static final String POSITIVE_INTEGER_NUMBER_PATTERN = "[0-9]+"; //$NON-NLS-1$

	private static final String POSITIVE_DOUBLE_NUMBER_PATTERN = "(?:(?:[0-9]+(?:\\.[0-9]*)?)|" //$NON-NLS-1$
			+ "(?:\\.[0-9]+))(?:[eE][-+][0-9]+)?"; //$NON-NLS-1$

	private static final Pattern HTML_RGB_PATTERN = Pattern.compile(
			"rgb\\s*\\(\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*\\)"); //$NON-NLS-1$

	private static final Pattern HTML_RGBA_PATTERN = Pattern.compile(
			"rgba\\s*\\(\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_INTEGER_NUMBER_PATTERN + ")\\s*\\)"); //$NON-NLS-1$

	private static final Pattern HTML_HSL_PATTERN = Pattern.compile(
			"hsl\\s*\\(\\s*(" //$NON-NLS-1$
			+ POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)\\s*,\\s*(" //$NON-NLS-1$
			+ POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)\\s*\\)"); //$NON-NLS-1$

	private static final Pattern HTML_HSLA_PATTERN = Pattern.compile(
			"hsla\\s*\\(\\s*(" + POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)\\s*,\\s*(" //$NON-NLS-1$ //$NON-NLS-2$
					+ POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)?\\s*," //$NON-NLS-1$
			+ "\\s*(" + POSITIVE_DOUBLE_NUMBER_PATTERN + "\\%?)?\\s*,\\s*([0-9]+)\\s*\\)"); //$NON-NLS-1$ //$NON-NLS-2$

	private static final String CONSTANT_TRUE = "true"; //$NON-NLS-1$

	private static final String CONSTANT_YES = "yes"; //$NON-NLS-1$

	private static final String CONSTANT_ON = "on"; //$NON-NLS-1$

	private static final String CONSTANT_Y = "y"; //$NON-NLS-1$

	private static final String CONSTANT_T = "t"; //$NON-NLS-1$

	private static final String COLUMN_SEPARATOR = "[ \t]*;[ \t]*"; //$NON-NLS-1$

	private static final String INDENT_NUMBER = "indent-number"; //$NON-NLS-1$

	private XMLUtil() {
		//
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static int decodeHexInteger(String colorString) {
		try {
			return Integer.parseInt(colorString, 16);
		} catch (Throwable exception) {
			return 0;
		}
	}

	private static int decodeDecInteger(String colorString) {
		try {
			return Integer.parseInt(colorString);
		} catch (Throwable exception) {
			return 0;
		}
	}

	private static double decodeDouble(String colorString) {
		try {
			final Double dblval = Double.valueOf(colorString);
			return dblval.doubleValue();
		} catch (Throwable exception) {
			return 0.;
		}
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static double decodeFactor(String colorString) {
		if (colorString == null) {
			return 0.;
		}
		if (colorString.endsWith("%")) { //$NON-NLS-1$
			return decodeDecInteger(colorString.substring(0, colorString.length() - 1)) / 100.;
		}
		return decodeDouble(colorString);
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static int encodeRgbaColor(int red, int green, int blue, int alpha) {
		int col = (alpha & 0xFF) << 24;
		col |= (red & 0xFF) << 16;
		col |= (green & 0xFF) << 8;
		col |= blue & 0xFF;
		return Integer.valueOf(col);
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static int encodeHslaColor(double hue, double saturation, double lightness, int alpha) {
		final double red;
		final double green;
		final double blue;
		if (saturation == 0) {
			// achromatic
			red = lightness;
			green = lightness;
			blue = lightness;
		} else {
			final double q = lightness < .5 ? lightness * (1 + saturation) : lightness + saturation - lightness * saturation;
			final double p = 2 * lightness - q;
			red = hue2rgb(p, q, hue + 1. / 3.);
			green = hue2rgb(p, q, hue);
			blue = hue2rgb(p, q, hue - 1. / 3.);
		}
		return encodeRgbaColor((int) (red * 255), (int) (green * 255), (int) (blue * 255), alpha);
	}

	@SuppressWarnings("checkstyle:magicnumber")
	private static double hue2rgb(double p0, double q0, double t0) {
		double t = t0;
		if (t < 0.) {
			t += 1.;
		}
		if (t > 1.) {
			t -= 1.;
		}
		if (t < 1. / 6.) {
			return p0 + (q0 - p0) * 6. * t;
		}
		if (t < 1. / 2.) {
			return q0;
		}
		if (t < 2. / 3.) {
			return p0 + (q0 - p0) * (2. / 3. - t) * 6.;
		}
		return p0;
	}

	/** Replies the boolean value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the boolean value of the specified attribute or {@code false} if
	 *     it was node found in the document
	 */
	@Pure
	public static boolean getAttributeBoolean(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeBooleanWithDefault(document, true, false, path);
	}

	/** Replies the boolean value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the boolean value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static boolean getAttributeBoolean(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeBooleanWithDefault(document, true, false, path);
	}

	/** Replies the boolean value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the boolean value of the specified attribute or {@code false} if
	 *      it was node found in the document
	 */
	@Pure
	public static Boolean getAttributeBooleanWithDefault(Node document, boolean caseSensitive,
			Boolean defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v == null || v.isEmpty()) {
			return defaultValue;
		}
		return CONSTANT_TRUE.equalsIgnoreCase(v)
				|| CONSTANT_YES.equalsIgnoreCase(v)
				|| CONSTANT_ON.equalsIgnoreCase(v)
				|| CONSTANT_Y.equalsIgnoreCase(v)
				|| CONSTANT_T.equalsIgnoreCase(v);
	}

	/** Replies the boolean value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the boolean value of the specified attribute or {@code false} if
	 *     it was node found in the document
	 */
	@Pure
	public static Boolean getAttributeBooleanWithDefault(Node document, Boolean defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeBooleanWithDefault(document, true, defaultValue, path);
	}

	/** Read an enumeration value.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the java class or {@code null} if none.
	 */
	@Pure
	public static Class<?> getAttributeClass(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeClassWithDefault(document, caseSensitive, null, path);
	}

	/** Read an enumeration value.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the java class or {@code null} if none.
	 */
	@Pure
	public static Class<?> getAttributeClass(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeClassWithDefault(document, true, null, path);
	}

	/** Read a java class.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value replied if no attribute was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the java class or {@code defaultValue} if none.
	 */
	@Pure
	public static Class<?> getAttributeClassWithDefault(Node document, boolean caseSensitive,
			Class<?> defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			try {
				final ClassLoader loader = ClassLoaderFinder.findClassLoader();
				return loader.loadClass(v);
			} catch (Throwable e) {
				//
			}
		}
		return defaultValue;
	}

	/** Read an enumeration value.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value replied if no attribute was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the java class or {@code defaultValue} if none.
	 */
	@Pure
	public static Class<?> getAttributeClassWithDefault(Node document, Class<?> defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeClassWithDefault(document, true, defaultValue, path);
	}

	/** Replies the color that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the color of the specified attribute.
	 */
	@Pure
	public static int getAttributeColor(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeColorWithDefault(document, caseSensitive, 0, path);
	}

	/** Replies the color that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the color of the specified attribute.
	 */
	@Pure
	public static int getAttributeColor(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeColorWithDefault(document, true, 0, path);
	}

	/** Replies the color that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the color of the specified attribute or {@code null} if
	 *      it was node found in the document
	 */
	@Pure
	public static Integer getAttributeColorWithDefault(Node document, boolean caseSensitive, Integer defaultValue,
			String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !"".equals(v)) { //$NON-NLS-1$
			try {
				return parseColor(v);
			} catch (ColorFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the color that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @param defaultValue is the default value to reply.
	 * @return the color of the specified attribute.
	 */
	@Pure
	public static Integer getAttributeColorWithDefault(Node document, Integer defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeColorWithDefault(document, true, defaultValue, path);
	}

	/** Replies the date that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the date of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Date getAttributeDate(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDateWithDefault(document, caseSensitive, null, path);
	}

	/** Replies the date that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the date of the specified attribute or {@code null} if
	 *      it was node found in the document
	 */
	@Pure
	public static Date getAttributeDate(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDateWithDefault(document, true, null, path);
	}

	/** Replies the date that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the date of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Date getAttributeDateWithDefault(Node document, boolean caseSensitive, Date defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null) {
			try {
				return parseDate(v);
			} catch (DateFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the date that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @param defaultValue is the default value to reply.
	 * @return the date of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Date getAttributeDateWithDefault(Node document, Date defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDateWithDefault(document, true, defaultValue, path);
	}

	/** Replies the double value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the double value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static double getAttributeDouble(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDoubleWithDefault(document, caseSensitive, 0., path);
	}

	/** Replies the double value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the double value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static double getAttributeDouble(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDoubleWithDefault(document, true, 0., path);
	}

	/** Replies the double value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the double value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Double getAttributeDoubleWithDefault(Node document, boolean caseSensitive, Double defaultValue,
			String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null) {
			try {
				return Double.parseDouble(v);
			} catch (NumberFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the double value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the double value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static Double getAttributeDoubleWithDefault(Node document, Double defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeDoubleWithDefault(document, true, defaultValue, path);
	}

	/** Read an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the enumeration or {@code null} if none.
	 */
	@Pure
	public static <T extends Enum<T>> T getAttributeEnum(Node document, Class<T> type, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeEnumWithDefault(document, type, caseSensitive, null, path);
	}

	/** Read an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the enumeration or {@code null} if none.
	 */
	@Pure
	public static <T extends Enum<T>> T getAttributeEnum(Node document, Class<T> type, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeEnumWithDefault(document, type, true, null, path);
	}

	/** Read an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value replied if no attribute was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the enumeration or {@code null} if none.
	 */
	@Pure
	public static <T extends Enum<T>> T getAttributeEnumWithDefault(Node document, Class<T> type,
			boolean caseSensitive, T defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		assert type != null : AssertMessages.notNullParameter(1);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			if (caseSensitive) {
				try {
					final T value = Enum.valueOf(type, v);
					if (value != null) {
						return value;
					}
				} catch (Throwable e) {
					//
				}
			} else {
				for (final T value : type.getEnumConstants()) {
					if (v.equalsIgnoreCase(value.name())) {
						return value;
					}
				}
			}
		}
		return defaultValue;
	}

	/** Read an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param defaultValue is the default value replied if no attribute was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the enumeration or {@code null} if none.
	 */
	@Pure
	public static <T extends Enum<T>> T getAttributeEnumWithDefault(Node document, Class<T> type,
			T defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeEnumWithDefault(document, type, true, defaultValue, path);
	}

	/** Replies the float value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the float value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static float getAttributeFloat(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeFloatWithDefault(document, caseSensitive, 0f, path);
	}

	/** Replies the float value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the float value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static float getAttributeFloat(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeFloatWithDefault(document, true, 0f, path);
	}

	/** Replies the float value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the float value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Float getAttributeFloatWithDefault(Node document, boolean caseSensitive, Float defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null) {
			try {
				return Float.parseFloat(v);
			} catch (NumberFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the float value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the float value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static Float getAttributeFloatWithDefault(Node document, Float defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeFloatWithDefault(document, true, defaultValue, path);
	}

	/** Replies the integer value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the integer value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static int getAttributeInt(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeIntWithDefault(document, caseSensitive, 0, path);
	}

	/** Replies the integer value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the integer value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static int getAttributeInt(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeIntWithDefault(document, true, 0, path);
	}

	/** Replies the integer value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the integer value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Integer getAttributeIntWithDefault(Node document, boolean caseSensitive, Integer defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null) {
			try {
				return Integer.parseInt(v);
			} catch (NumberFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the integer value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the integer value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static Integer getAttributeIntWithDefault(Node document, Integer defaultValue, String... path) {
		return getAttributeIntWithDefault(document, true, defaultValue, path);
	}

	/** Replies the long value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the long value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static long getAttributeLong(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeLongWithDefault(document, caseSensitive, (long) 0, path);
	}

	/** Replies the long value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the long value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static long getAttributeLong(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeLongWithDefault(document, true, (long) 0, path);
	}

	/** Replies the long value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the long value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static Long getAttributeLongWithDefault(Node document, boolean caseSensitive, Long defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null) {
			try {
				return Long.parseLong(v);
			} catch (NumberFormatException e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the long value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the long value of the specified attribute or {@code 0}.
	 */
	@Pure
	public static Long getAttributeLongWithDefault(Node document, Long defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeLongWithDefault(document, true, defaultValue, path);
	}

	/** Replies the URL that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the URL in the specified attribute or {@code null} if
	 *      it was node found in the document
	 */
	@Pure
	public static URL getAttributeURL(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeURLWithDefault(document, caseSensitive, null, path);
	}

	/** Replies the URL that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the URL in the specified attribute or {@code null} if
	 *      it was node found in the document
	 */
	@Pure
	public static URL getAttributeURL(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeURLWithDefault(document, true, null, path);
	}

	/** Replies the URL that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the URL in the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static URL getAttributeURLWithDefault(Node document, boolean caseSensitive, URL defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			final URL url = FileSystem.convertStringToURL(v, true);
			if (url != null) {
				return url;
			}
		}
		return defaultValue;
	}

	/** Replies the URL that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the URL in the specified attribute or {@code null} if
	 *      it was node found in the document
	 */
	@Pure
	public static URL getAttributeURLWithDefault(Node document, URL defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeURLWithDefault(document, true, defaultValue, path);
	}

	/** Replies the UUID that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUID in the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static UUID getAttributeUUID(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeUUIDWithDefault(document, caseSensitive, null, path);
	}

	/** Replies the UUID that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUID in the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static UUID getAttributeUUID(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeUUIDWithDefault(document, true, null, path);
	}

	/** Replies the UUIDs that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUIDs in the specified attribute, never {@code null}
	 */
	@Pure
	public static List<UUID> getAttributeUUIDs(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final List<UUID> ids = new ArrayList<>();
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			for (final String id : v.split(COLUMN_SEPARATOR)) {
				try {
					ids.add(UUID.fromString(id));
				} catch (Exception e) {
					//
				}
			}
		}
		return ids;
	}

	/** Replies the UUIDs that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUIDs in the specified attribute, never {@code null}
	 */
	@Pure
	public static List<UUID> getAttributeUUIDs(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeUUIDs(document, true, path);
	}

	/** Replies the UUID that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUID in the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static UUID getAttributeUUIDWithDefault(Node document, boolean caseSensitive, UUID defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			try {
				final UUID id = UUID.fromString(v);
				if (id != null) {
					return id;
				}
			} catch (Exception e) {
				//
			}
		}
		return defaultValue;
	}

	/** Replies the UUID that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the UUID in the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static UUID getAttributeUUIDWithDefault(Node document, UUID defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeUUIDWithDefault(document, true, defaultValue, path);
	}

	/** Replies the value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param idxStart is the index of the first element of the path to use.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	private static String getAttributeValue(Node document, boolean caseSensitive, int idxStart, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		assert path != null && (path.length - idxStart) >= 0 : AssertMessages.invalidValue(2);

		if ((path.length - idxStart) > 1) {
			final NodeList nodes = document.getChildNodes();
			final int len = nodes.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = nodes.item(i);
				if (node != null) {
					final String name = node.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						final String value = getAttributeValue(node, caseSensitive, idxStart + 1, path);
						if (value != null) {
							return value;
						}
					}
				}
			}
		} else if (document instanceof Element) {
			if (caseSensitive) {
				return ((Element) document).getAttribute(path[idxStart]);
			}
			final NamedNodeMap map = ((Element) document).getAttributes();
			final int len = map.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = map.item(i);
				if (node instanceof Attr) {
					final Attr attr = (Attr) node;
					final String name = attr.getName();
					if (name != null && name.equalsIgnoreCase(path[idxStart])) {
						final String value = attr.getValue();
						if (value != null) {
							return value;
						}
					}
				}
			}
		} else {
			final NamedNodeMap attrs = document.getAttributes();
			if (attrs != null) {
				final int len = attrs.getLength();
				for (int idxAttr = 0; idxAttr < len; ++idxAttr) {
					final Node node = attrs.item(idxAttr);
					final String name = node.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						return node.getNodeValue();
					}
				}
			}
		}
		return null;
	}

	/** Replies the value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param casesSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static String getAttributeValue(Node document, boolean casesSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeValue(document, casesSensitive, 0, path);
	}

	/** Replies the value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static String getAttributeValue(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeValue(document, true, 0, path);
	}

	/** Replies the value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param defaultValue is the default value to reply if no attribute value was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static String getAttributeValueWithDefault(Node document, boolean caseSensitive, String defaultValue,
			String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final String v = getAttributeValue(document, caseSensitive, 0, path);
		if (v != null && !v.isEmpty()) {
			return v;
		}
		return defaultValue;
	}

	/** Replies the value that corresponds to the specified attribute's path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the attribute.
	 *
	 * @param document is the XML document to explore.
	 * @param defaultValue is the default value to reply if no attribute value was found.
	 * @param path is the list of and ended by the attribute's name.
	 * @return the value of the specified attribute or {@code null} if
	 *     it was node found in the document
	 */
	@Pure
	public static String getAttributeValueWithDefault(Node document, String defaultValue, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getAttributeValueWithDefault(document, true, defaultValue, path);
	}

	/** Replies the first child node that has the specified type.
	 *
	 * @param <T> is the type of the desired child
	 * @param parent is the element from which the child must be extracted.
	 * @param type is the type of the desired child
	 * @return the child node or {@code null} if none.
	 */
	@Pure
	public static <T extends Node> T getChild(Node parent, Class<T> type) {
		assert parent != null : AssertMessages.notNullParameter(0);
		assert type != null : AssertMessages.notNullParameter(1);
		final NodeList children = parent.getChildNodes();
		final int len = children.getLength();
		for (int i = 0; i < len; ++i) {
			final Node child = children.item(i);
			if (type.isInstance(child)) {
				return type.cast(child);
			}
		}
		return null;
	}

	/** Replies the XML Document that is containing the given node.
	 *
	 * @param node the node.
	 * @return the Document in which the given node is, or {@code null}
	 *     if not found.
	 */
	@Pure
	public static Document getDocumentFor(Node node) {
		Node localnode = node;
		while (localnode != null) {
			if (localnode instanceof Document) {
				return (Document) localnode;
			}
			localnode = localnode.getParentNode();
		}
		return null;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param idxStart is the index of the first element of the path to use.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *     it was not found in the document.
	 */
	@Pure
	private static Element getElementFromPath(Node document, boolean caseSensitive, int idxStart, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		if (path != null && (path.length - idxStart) >= 1) {
			final NodeList nodes = document.getChildNodes();
			final int len = nodes.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = nodes.item(i);
				if (node instanceof Element) {
					final Element element = (Element) node;
					final String name = node.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						final Element nd = (path.length - idxStart) == 1
								? element : getElementFromPath(node, caseSensitive, idxStart + 1, path);
						if (nd != null) {
							return nd;
						}
					}
				}
			}
		}
		return null;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static Element getElementFromPath(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getElementFromPath(document, caseSensitive, 0, path);
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static Element getElementFromPath(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getElementFromPath(document, true, 0, path);
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param constraint is the constraint that the replied element must respect.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static Element getElementMatching(Node document, XMLConstraint constraint, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		assert constraint != null : AssertMessages.notNullParameter(1);
		for (final Element element : getElementsFromPath(document, caseSensitive, path)) {
			if (constraint.isValidElement(element)) {
				return element;
			}
		}
		return null;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param constraint is the constraint that the replied element must respect.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static Element getElementMatching(Node document, XMLConstraint constraint, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		assert constraint != null : AssertMessages.notNullParameter(1);
		return getElementMatching(document, constraint, true, path);
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param idxStart is the index of the first element of the path to use.
	 * @param result is the node list to fill.
	 * @param path is the list of names.
	 */
	private static void getElementsFromPath(Node document, boolean caseSensitive, int idxStart,
			List<Element> result, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		if (path != null && (path.length - idxStart) >= 1) {
			final NodeList nodes = document.getChildNodes();
			final int len = nodes.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = nodes.item(i);
				if (node instanceof Element) {
					final Element element = (Element) node;
					final String name = element.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						if  ((path.length - idxStart) == 1) {
							result.add(element);
						} else {
							getElementsFromPath(element, caseSensitive, idxStart + 1, result, path);
						}
					}
				}
			}
		}
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static List<Element> getElementsFromPath(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final List<Element> list = new ArrayList<>();
		getElementsFromPath(document, caseSensitive, 0, list, path);
		return list;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of names.
	 * @return the node or {@code null} if it was not found in the document.
	 */
	@Pure
	public static List<Element> getElementsFromPath(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final List<Element> list = new ArrayList<>();
		getElementsFromPath(document, true, 0, list, path);
		return list;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param idxStart is the index of the first element of the path to use.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *     it was not found in the document.
	 */
	@Pure
	private static Node getNodeFromPath(Node document, boolean caseSensitive, int idxStart, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		if (path != null && (path.length - idxStart) >= 1) {
			final NodeList nodes = document.getChildNodes();
			final int len = nodes.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = nodes.item(i);
				if (node != null) {
					final String name = node.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						final Node nd = (path.length - idxStart) == 1
								? node : getNodeFromPath(node, caseSensitive, idxStart + 1, path);
						if (nd != null) {
							return nd;
						}
					}
				}
			}
		}
		return null;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *      it was not found in the document.
	 */
	@Pure
	public static Node getNodeFromPath(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getNodeFromPath(document, caseSensitive, 0, path);
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *     it was not found in the document.
	 */
	@Pure
	public static Node getNodeFromPath(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return getNodeFromPath(document, true, 0, path);
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param idxStart is the index of the first element of the path to use.
	 * @param result is the node list to fill.
	 * @param path is the list of names.
	 */
	private static void getNodesFromPath(Node document, boolean caseSensitive, int idxStart, List<Node> result, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		if (path != null && (path.length - idxStart) >= 1) {
			final NodeList nodes = document.getChildNodes();
			final int len = nodes.getLength();
			for (int i = 0; i < len; ++i) {
				final Node node = nodes.item(i);
				if (node != null) {
					final String name = node.getNodeName();
					if (name != null
							&& ((caseSensitive && name.equals(path[idxStart]))
									|| (!caseSensitive && name.equalsIgnoreCase(path[idxStart])))) {
						if  ((path.length - idxStart) == 1) {
							result.add(node);
						} else {
							getNodesFromPath(node, caseSensitive, idxStart + 1, result, path);
						}
					}
				}
			}
		}
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 *
	 * @param document is the XML document to explore.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *      it was not found in the document.
	 */
	@Pure
	public static List<Node> getNodesFromPath(Node document, boolean caseSensitive, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final List<Node> list = new ArrayList<>();
		getNodesFromPath(document, caseSensitive, 0, list, path);
		return list;
	}

	/** Replies the node that corresponds to the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of names.
	 * @return the node or {@code null} if
	 *     it was not found in the document.
	 */
	@Pure
	public static List<Node> getNodesFromPath(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		final List<Node> list = new ArrayList<>();
		getNodesFromPath(document, true, 0, list, path);
		return list;
	}

	/** Replies the text inside the node at the specified path.
	 *
	 * <p>The path is an ordered list of tag's names and ended by the name of
	 * the desired node.
	 * Be careful about the fact that the names are case sensitives.
	 *
	 * @param document is the XML document to explore.
	 * @param path is the list of names. This path may be empty.
	 * @return the text or {@code null} if the node was not found in the document or the text
	 *     inside was empty.
	 */
	@Pure
	public static String getText(Node document, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		Node parentNode = getNodeFromPath(document, path);
		if (parentNode == null) {
			parentNode = document;
		}
		final StringBuilder text = new StringBuilder();
		final NodeList children = parentNode.getChildNodes();
		final int len = children.getLength();
		for (int i = 0; i < len; ++i) {
			final Node child = children.item(i);
			if (child instanceof Text) {
				text.append(((Text) child).getWholeText());
			}
		}
		if (text.length() > 0) {
			return text.toString();
		}
		return null;
	}

	/** Replies an iterator on nodes that have the specified node name.
	 *
	 * @param parent is the node from which the children must be extracted.
	 * @param nodeName is the name of the extracted nodes
	 * @return the iterator on the parents.
	 */
	@Pure
	public static Iterator<Node> iterate(Node parent, String nodeName) {
		assert parent != null : AssertMessages.notNullParameter(0);
		assert nodeName != null && !nodeName.isEmpty() : AssertMessages.notNullParameter(0);
		return new NameBasedIterator(parent, nodeName);
	}

	/**
	 * Parses an XML/HTML color.
	 *
	 * <p>The supported formats of color are:<ul>
	 * <li>{@code #<i>hex</i>}</li>
	 * <li>{@code <i>name</i>}</li>
	 * <li>{@code rgb(<i>r</i>, <i>g</i>, <i>b</i>)}</li>
	 * <li>{@code rgba(<i>r</i>, <i>g</i>, <i>b</i>, <i>a</i>)}</li>
	 * <li>{@code hsl(<i>h</i>[%], <i>s</i>[%], <i>l</i>[%])}</li>
	 * <li>{@code hsla(<i>h</i>[%], <i>s</i>[%], <i>l</i>[%], <i>a</i>)}</li>
	 * </ul>
	 *
	 * <p>The supported color names are listed in {@link ColorNames}.
	 *
	 * @param xmlColor is the color to translate from a XML/HTML string.
	 * @return the color.
	 * @throws ColorFormatException if the color has invalid format.
	 * @see #toColor(int, int, int, int)
	 * see {@link ColorNames}
	 */
	@Pure
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:npathcomplexity"})
	public static int parseColor(String xmlColor) throws ColorFormatException {
		if (xmlColor == null || "".equals(xmlColor)) { //$NON-NLS-1$
			return 0;
		}
		if (xmlColor.startsWith("#")) { //$NON-NLS-1$
			try {
				final String str = xmlColor.substring(1);
				if (str.length() == 6) {
					return 0xFF000000 | decodeHexInteger(str.toString());
				}
				return decodeHexInteger(str.toString());
			} catch (NumberFormatException e) {
				throw new ColorFormatException(xmlColor);
			}
		}
		Matcher matcher = HTML_RGB_PATTERN.matcher(xmlColor);
		if (matcher.find()) {
			final int red = decodeDecInteger(matcher.group(1));
			final int green = decodeDecInteger(matcher.group(2));
			final int blue = decodeDecInteger(matcher.group(3));
			return encodeRgbaColor(red, green, blue, 0xFF);
		}
		matcher = HTML_RGBA_PATTERN.matcher(xmlColor);
		if (matcher.find()) {
			final int red = decodeDecInteger(matcher.group(1));
			final int green = decodeDecInteger(matcher.group(2));
			final int blue = decodeDecInteger(matcher.group(3));
			final int alpha = decodeDecInteger(matcher.group(4));
			return encodeRgbaColor(red, green, blue, alpha);
		}
		matcher = HTML_HSL_PATTERN.matcher(xmlColor);
		if (matcher.find()) {
			final double hue = decodeFactor(matcher.group(1));
			final double saturation = decodeFactor(matcher.group(2));
			final double lightness = decodeFactor(matcher.group(3));
			return encodeHslaColor(hue, saturation, lightness, 0xFF);
		}
		matcher = HTML_HSLA_PATTERN.matcher(xmlColor);
		if (matcher.find()) {
			final double hue = decodeFactor(matcher.group(1));
			final double saturation = decodeFactor(matcher.group(2));
			final double lightness = decodeFactor(matcher.group(3));
			final int alpha = decodeDecInteger(matcher.group(4));
			return encodeHslaColor(hue, saturation, lightness, alpha);
		}
		final Integer color = ColorNames.getColorFromName(xmlColor);
		if (color != null) {
			return color.intValue();
		}
		throw new ColorFormatException(xmlColor);
	}

	/** Parses an XML/HTML date.
	 *
	 * @param xmlDate is the date to translate from a XML/HTML string.
	 * @return the date.
	 * @throws DateFormatException if the date has invalid format.
	 * @see #toString(Date)
	 */
	@Pure
	public static Date parseDate(String xmlDate) throws DateFormatException {
		if (xmlDate == null || xmlDate.isEmpty()) {
			return null;
		}
		final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			return format.parse(xmlDate);
		} catch (Exception e) {
			//
		}
		throw new DateFormatException(xmlDate);
	}

	/** Deserialize an object from the given XML string.
	 *
	 * @param xmlSerializedObject is the string which is containing the serialized object.
	 * @return the serialized object extracted from the XML string.
	 * @throws IOException if something wrong append during deserialization.
	 * @throws ClassNotFoundException is thrown when the class for the deserialized object is not found.
	 */
	@Pure
	public static Object parseObject(String xmlSerializedObject) throws IOException, ClassNotFoundException {
		assert xmlSerializedObject != null : AssertMessages.notNullParameter(0);
		try (ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(xmlSerializedObject))) {
			final ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
	}

	/** Parse a Base64 string with contains a set of bytes.
	 *
	 * @param text is the string to uudecode
	 * @return the decoding result
	 * @see #toString(byte[])
	 */
	@Pure
	public static byte[] parseString(String text) {
		return Base64.getDecoder().decode(Strings.nullToEmpty(text).trim());
	}

	/** Parse a string representation of an XML document.
	 *
	 * @param xmlString is the string representation of the XML document.
	 * @return the document or {@code null} in case of error.
	 */
	@Pure
	public static Document parseXML(String xmlString) {
		assert xmlString != null : AssertMessages.notNullParameter(0);
		try {
			return readXML(new StringReader(xmlString));
		} catch (Exception e) {
			//
		}
		return null;
	}

	/**
	 * Read an XML file and replies the DOM document.
	 *
	 * @param file is the file to read
	 * @return the DOM document red from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static Document readXML(File file) throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		try (FileInputStream fis = new FileInputStream(file)) {
			return readXML(fis);
		}
	}

	/**
	 * Read an XML file and replies the DOM document.
	 *
	 * @param stream is the stream to read
	 * @return the DOM document red from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static Document readXML(InputStream stream) throws IOException, SAXException, ParserConfigurationException {
		assert stream != null : AssertMessages.notNullParameter();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(stream);
		} finally {
			stream.close();
		}
	}

	/**
	 * Read an XML file and replies the DOM document.
	 *
	 * @param reader is the stream to read
	 * @return the DOM document red from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static Document readXML(Reader reader) throws IOException, SAXException, ParserConfigurationException {
		assert reader != null : AssertMessages.notNullParameter();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(reader));
		} finally {
			reader.close();
		}
	}

	/**
	 * Read an XML file and replies the DOM document.
	 *
	 * @param file is the file to read
	 * @return the DOM document red from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static Document readXML(String file) throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		try (FileInputStream fis = new FileInputStream(file)) {
			return readXML(fis);
		}
	}

	/**
	 * Read an XML file and replies the DOM document.
	 *
	 * @param file is the file to read
	 * @return the DOM document red from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static Document readXML(URL file) throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		return readXML(file.openStream());
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(File file) throws IOException, SAXException, ParserConfigurationException {
		return readXMLFragment(file, false);
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @param skipRoot if {@code true} the root element itself is not part of the fragment, and the children
	 *     of the root element are directly added within the fragment.
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(File file, boolean skipRoot)
			throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		try (FileInputStream fis = new FileInputStream(file)) {
			return readXMLFragment(fis, skipRoot);
		}
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input stream.
	 *
	 * @param stream is the stream to read
	 * @return the fragment from the {@code stream}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(InputStream stream) throws IOException,
			SAXException, ParserConfigurationException {
		return readXMLFragment(stream, false);
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input stream.
	 *
	 * @param stream is the stream to read
	 * @param skipRoot if {@code true} the root element itself is not part of the fragment, and the children
	 *     of the root element are directly added within the fragment.
	 * @return the fragment from the {@code stream}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(InputStream stream, boolean skipRoot) throws IOException,
			SAXException, ParserConfigurationException {
		assert stream != null : AssertMessages.notNullParameter();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(stream);

			final DocumentFragment fragment = doc.createDocumentFragment();
			if (skipRoot) {
				final NodeList root = doc.getChildNodes();
				if (root.getLength() == 0) {
					return fragment;
				}
				final NodeList children = root.item(0).getChildNodes();
				final int len = children.getLength();
				for (int i = 0; i < len; ++i) {
					fragment.appendChild(children.item(0));
				}
			} else {
				final NodeList children = doc.getChildNodes();
				final int len = children.getLength();
				for (int i = 0; i < len; ++i) {
					fragment.appendChild(children.item(i));
				}
			}
			return fragment;
		} finally {
			stream.close();
		}
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input stream.
	 *
	 * @param reader is the stream to read
	 * @return the fragment from the {@code reader}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(Reader reader) throws IOException, SAXException,
			ParserConfigurationException {
		return readXMLFragment(reader, false);
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input stream.
	 *
	 * @param reader is the stream to read
	 * @param skipRoot if {@code true} the root element itself is not part of the fragment, and the children
	 *     of the root element are directly added within the fragment.
	 * @return the fragment from the {@code reader}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(Reader reader, boolean skipRoot) throws IOException, SAXException,
			ParserConfigurationException {
		assert reader != null : AssertMessages.notNullParameter();
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(new InputSource(reader));
			final DocumentFragment fragment = doc.createDocumentFragment();
			if (skipRoot) {
				final NodeList root = doc.getChildNodes();
				if (root.getLength() == 0) {
					return fragment;
				}
				final NodeList children = root.item(0).getChildNodes();
				final int len = children.getLength();
				for (int i = 0; i < len; ++i) {
					fragment.appendChild(children.item(0));
				}
			} else {
				final NodeList children = doc.getChildNodes();
				final int len = children.getLength();
				for (int i = 0; i < len; ++i) {
					fragment.appendChild(children.item(i));
				}
			}
			return fragment;
		} finally {
			reader.close();
		}
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(String file) throws IOException, SAXException, ParserConfigurationException {
		return readXMLFragment(file, false);
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @param skipRoot if {@code true} the root element itself is not part of the fragment, and the children
	 *     of the root element are directly added within the fragment.
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(String file, boolean skipRoot)
			throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		try (FileInputStream fis = new FileInputStream(file)) {
			return readXMLFragment(fis, skipRoot);
		}
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(URL file) throws IOException, SAXException, ParserConfigurationException {
		return readXMLFragment(file, false);
	}

	/**
	 * Read an XML fragment from an XML file.
	 * The XML file is well-formed. It means that the fragment will contains a single element: the root element
	 * within the input file.
	 *
	 * @param file is the file to read
	 * @param skipRoot if {@code true} the root element itself is not part of the fragment, and the children
	 *     of the root element are directly added within the fragment.
	 * @return the fragment from the {@code file}.
	 * @throws IOException if the stream cannot be read.
	 * @throws SAXException if the stream does not contains valid XML data.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static DocumentFragment readXMLFragment(URL file, boolean skipRoot)
			throws IOException, SAXException, ParserConfigurationException {
		assert file != null : AssertMessages.notNullParameter();
		return readXMLFragment(file.openStream(), skipRoot);
	}

	/** Write an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param caseSensitive indicates of the {@code path}'s components are case sensitive.
	 * @param value is the value to put in the document.
	 * @param path is the list of and ended by the attribute's name.
	 * @return {@code true} if written, {@code false} if not written.
	 */
	public static <T extends Enum<T>> boolean setAttributeEnum(Node document, Class<T> type,
			boolean caseSensitive, T value, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		assert type != null : AssertMessages.notNullParameter(1);
		if (value != null) {
			final String[] thePath = Arrays.copyOf(path, path.length - 1);
			final String attrName = path[path.length - 1];
			if (attrName != null && !attrName.isEmpty()) {
				Element node = null;
				if (thePath != null && thePath.length > 0) {
					node = getElementFromPath(document, caseSensitive, 0, thePath);
				} else if (document instanceof Element) {
					node = (Element) document;
				}
				if (node != null) {
					node.setAttribute(attrName, value.name());
					return true;
				}
			}
		}
		return false;
	}

	/** Write an enumeration value.
	 *
	 * @param <T> is the type of the enumeration.
	 * @param document is the XML document to explore.
	 * @param type is the type of the enumeration.
	 * @param value is the value to put in the document.
	 * @param path is the list of and ended by the attribute's name.
	 * @return {@code true} if written, {@code false} if not written.
	 */
	public static <T extends Enum<T>> boolean setAttributeEnum(Node document, Class<T> type, T value, String... path) {
		assert document != null : AssertMessages.notNullParameter(0);
		return setAttributeEnum(document, type, true, value, path);
	}

	/**
	 * Replies an XML/HTML color.
	 *
	 * @param rgba the color components.
	 * @return the XML color encoding.
	 * @see #parseColor(String)
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static String toColor(int rgba) {
		final String code = ColorNames.getColorNameFromValue(rgba);
		if (!Strings.isNullOrEmpty(code)) {
			return code;
		}
		final StringBuilder s = new StringBuilder("#"); //$NON-NLS-1$
		s.append(Integer.toHexString(rgba));
		while (s.length() < 7) {
			s.insert(1, '0');
		}
		return s.toString();
	}

	/**
	 * Replies an XML/HTML color.
	 *
	 * @param red the red component.
	 * @param green the green component.
	 * @param blue the blue component.
	 * @param alpha the alpha component.
	 * @return the XML color encoding.
	 * @see #parseColor(String)
	 */
	@Pure
	public static String toColor(int red, int green, int blue, int alpha) {
		return toColor(encodeRgbaColor(red, green, blue, alpha));
	}

	/** Translate a set of bytes into an XML-compliant set of characters.
	 *
	 * @param array is the string to uuencode.
	 * @return the string representation of the given array.
	 * @see #parseString(String)
	 */
	@Pure
	public static String toString(byte[] array) {
		assert array != null : AssertMessages.notNullParameter(0);
		return new String(Base64.getEncoder().encode(array));
	}

	/**
	 * Replies an XML/HTML date.
	 *
	 * @param date is the date to translate into a XML/HTML string.
	 * @return the XML date encoding.
	 * @see #parseDate(String)
	 */
	@Pure
	public static String toString(Date date) {
		if (date == null) {
			return ""; //$NON-NLS-1$
		}
		final SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);
		return f.format(date);
	}

	/** Translate the XML tree into a string representation.
	 *
	 * @param node is the object that contains the node tree
	 * @return the string representation or {@code null} in case of error.
	 */
	@Pure
	public static String toString(Node node) {
		assert node != null : AssertMessages.notNullParameter(0);
		try (StringWriter writer = new StringWriter()) {
			writeXML(node, writer);
			return writer.toString();
		} catch (Exception e) {
			//
		}
		return null;
	}

	/** Serialize the given object and put it in a string which is compliant with XML format.
	 *
	 * @param object is the object to serialize.
	 * @return the XML representation of the object.
	 * @throws IOException if something wrong append during serialization.
	 */
	@Pure
	public static String toString(Serializable object) throws IOException {
		assert object != null : AssertMessages.notNullParameter(0);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			final ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			return new String(Base64.getEncoder().encode(baos.toByteArray()));
		}
	}

	private static void writeNode(Node node, OutputStream stream) throws IOException {
		assert node != null : AssertMessages.notNullParameter(0);
		assert stream != null : AssertMessages.notNullParameter(1);
		try {
			final TransformerFactory transFactory = TransformerFactory.newInstance();
			transFactory.setAttribute(INDENT_NUMBER, Integer.valueOf(2));
			final Transformer trans = transFactory.newTransformer();
			trans.setParameter(OutputKeys.INDENT, CONSTANT_YES);

			final DOMSource source = new DOMSource(node);
			try (PrintStream flot = new PrintStream(stream)) {
				final StreamResult xmlStream = new StreamResult(flot);
				trans.transform(source, xmlStream);
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private static void writeNode(Node node, Writer writer) throws IOException {
		assert node != null : AssertMessages.notNullParameter(0);
		assert writer != null : AssertMessages.notNullParameter(1);
		try {
			final TransformerFactory transFactory = TransformerFactory.newInstance();
			transFactory.setAttribute(INDENT_NUMBER, Integer.valueOf(2));
			final Transformer trans = transFactory.newTransformer();
			trans.setParameter(OutputKeys.INDENT, CONSTANT_YES);

			final DOMSource source = new DOMSource(node);
			try (PrintWriter flot = new PrintWriter(writer)) {
				final StreamResult xmlStream = new StreamResult(flot);
				trans.transform(source, xmlStream);
			}
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param xmldocument is the object that contains the node tree
	 * @param file is the file name.
	 * @throws IOException if the stream cannot be read.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static void writeXML(Document xmldocument, File file) throws ParserConfigurationException, IOException {
		assert xmldocument != null : AssertMessages.notNullParameter();
		assert file != null : AssertMessages.notNullParameter();
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNode(xmldocument, fos);
		}
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param xmldocument is the object that contains the node tree
	 * @param stream is the target stream
	 * @throws IOException if the stream cannot be read.
	 */
	public static void writeXML(Document xmldocument, OutputStream stream) throws IOException {
		assert xmldocument != null : AssertMessages.notNullParameter(0);
		assert stream != null : AssertMessages.notNullParameter(1);
		writeNode(xmldocument, stream);
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param xmldocument is the object that contains the node tree
	 * @param file is the file name.
	 * @throws IOException if the stream cannot be read.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static void writeXML(Document xmldocument, String file) throws ParserConfigurationException, IOException {
		assert file != null : AssertMessages.notNullParameter();
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNode(xmldocument, fos);
		}
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param xmldocument is the object that contains the node tree
	 * @param writer is the target stream
	 * @throws IOException if the stream cannot be read.
	 */
	public static void writeXML(Document xmldocument, Writer writer) throws IOException {
		assert xmldocument != null : AssertMessages.notNullParameter(0);
		assert writer != null : AssertMessages.notNullParameter(1);
		writeNode(xmldocument, writer);
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param fragment is the object that contains the node tree
	 * @param file is the file name.
	 * @throws IOException if the stream cannot be read.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static void writeXML(DocumentFragment fragment, File file) throws ParserConfigurationException, IOException {
		assert fragment != null : AssertMessages.notNullParameter(0);
		assert file != null : AssertMessages.notNullParameter(1);
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNode(fragment, fos);
		}
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param fragment is the object that contains the node tree
	 * @param stream is the target stream
	 * @throws IOException if the stream cannot be read.
	 */
	public static void writeXML(DocumentFragment fragment, OutputStream stream) throws IOException {
		assert fragment != null : AssertMessages.notNullParameter(0);
		assert stream != null : AssertMessages.notNullParameter(1);
		writeNode(fragment, stream);
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param fragment is the object that contains the node tree
	 * @param file is the file name.
	 * @throws IOException if the stream cannot be read.
	 * @throws ParserConfigurationException if the parser cannot  be configured.
	 */
	public static void writeXML(DocumentFragment fragment, String file) throws ParserConfigurationException, IOException {
		assert fragment != null : AssertMessages.notNullParameter(0);
		assert file != null : AssertMessages.notNullParameter(1);
		try (FileOutputStream fos = new FileOutputStream(file)) {
			writeNode(fragment, fos);
		}
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param fragment is the object that contains the node tree
	 * @param writer is the target stream
	 * @throws IOException if the stream cannot be read.
	 */
	public static void writeXML(DocumentFragment fragment, Writer writer) throws IOException {
		assert fragment != null : AssertMessages.notNullParameter(0);
		assert writer != null : AssertMessages.notNullParameter(1);
		writeNode(fragment, writer);
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param node is the object that contains the node tree
	 * @param stream is the target stream
	 * @throws IOException if the stream cannot be read.
	 */
	public static void writeXML(Node node, OutputStream stream) throws IOException {
		writeNode(node, stream);
	}

	/** Write the given node tree into a XML file.
	 *
	 * @param node is the object that contains the node tree
	 * @param writer is the target stream
	 * @throws IOException if the writer cannot be used.
	 */
	public static void writeXML(Node node, Writer writer) throws IOException {
		writeNode(node, writer);
	}

	/** Write the given resources into the given XML node.
	 *
	 * @param node is the XML node to fill.
	 * @param resources are the resources to put out.
	 * @param builder is the tool to create XML nodes.
	 * @throws Error in case of unexpected error.
	 */
	public static void writeResources(Element node, XMLResources resources, XMLBuilder builder) {
		if (resources != null) {
			final Element resourcesNode = builder.createElement(NODE_RESOURCES);
			for (final java.util.Map.Entry<Long, Entry> pair : resources.getPairs().entrySet()) {
				final Entry e = pair.getValue();
				if (e.isURL()) {
					final Element resourceNode = builder.createElement(NODE_RESOURCE);
					resourceNode.setAttribute(ATTR_ID, XMLResources.getStringIdentifier(pair.getKey()));
					resourceNode.setAttribute(ATTR_URL, e.getURL().toExternalForm());
					resourceNode.setAttribute(ATTR_MIMETYPE, e.getMimeType());
					resourcesNode.appendChild(resourceNode);
				} else if (e.isFile()) {
					final Element resourceNode = builder.createElement(NODE_RESOURCE);
					resourceNode.setAttribute(ATTR_ID, XMLResources.getStringIdentifier(pair.getKey()));
					final File file = e.getFile();
					final StringBuilder url = new StringBuilder();
					url.append("file:"); //$NON-NLS-1$
					boolean addSlash = false;
					for (final String elt : FileSystem.split(file)) {
						try {
							if (addSlash) {
								url.append("/"); //$NON-NLS-1$
							}
							url.append(URLEncoder.encode(elt, Charset.defaultCharset().name()));
						} catch (UnsupportedEncodingException e1) {
							throw new Error(e1);
						}
						addSlash = true;
					}
					resourceNode.setAttribute(ATTR_FILE, url.toString());
					resourceNode.setAttribute(ATTR_MIMETYPE, e.getMimeType());
					resourcesNode.appendChild(resourceNode);
				} else if (e.isEmbeddedData()) {
					final Element resourceNode = builder.createElement(NODE_RESOURCE);
					resourceNode.setAttribute(ATTR_ID, XMLResources.getStringIdentifier(pair.getKey()));
					resourceNode.setAttribute(ATTR_MIMETYPE, e.getMimeType());
					final byte[] data = e.getEmbeddedData();
					final CDATASection cdata = builder.createCDATASection(toString(data));
					resourceNode.appendChild(cdata);
					resourcesNode.appendChild(resourceNode);
				}
			}
			if (resourcesNode.getChildNodes().getLength() > 0) {
				node.appendChild(resourcesNode);
			}
		}
	}

	/** Read the given resources from the given XML node.
	 * The <var>node</var> should have a node named "resources" with
	 * a node named "resource" for each resource inside.
	 *
	 * <p>The given <var>resources</var> are not cleared before the XML document
	 * is read.
	 *
	 * @param node is the XML node to read.
	 * @param resources is the collection of resources to fill out.
	 * @return the number of read resources.
	 * @throws IOException in case of error.
	 */
	@SuppressWarnings({"checkstyle:nestedifdepth"})
	public static int readResources(Element node, XMLResources resources) throws IOException {
		int nb = 0;
		if (resources != null) {
			for (final Element resourceNode : getElementsFromPath(node, NODE_RESOURCES, NODE_RESOURCE)) {
				try {
					final String sid = getAttributeValue(resourceNode, ATTR_ID);
					final long id = XMLResources.getNumericalIdentifier(sid);
					final String mimeType = getAttributeValue(resourceNode, ATTR_MIMETYPE);
					String ssource = getAttributeValue(resourceNode, ATTR_URL);
					if (ssource != null && !"".equals(ssource)) { //$NON-NLS-1$
						// Read URL resource
						resources.add(id, new URL(ssource), mimeType);
						++nb;
					} else {
						ssource = getAttributeValue(resourceNode, ATTR_FILE);
						if (ssource != null && !"".equals(ssource)) { //$NON-NLS-1$
							// Read File resource
							final URL url = new URL(ssource);
							final String path = url.getPath();
							final StringBuilder b = new StringBuilder();
							boolean addSlash = false;
							for (final String elt : path.split("[/]")) { //$NON-NLS-1$
								try {
									if (addSlash) {
										b.append(File.separator);
									}
									b.append(URLDecoder.decode(elt, Charset.defaultCharset().name()));
								} catch (UnsupportedEncodingException e1) {
									throw new Error(e1);
								}
								addSlash = true;
							}
							resources.add(id, new File(b.toString()), mimeType);
							++nb;
						} else {
							// Read embedded resource
							final CDATASection rawData = getChild(resourceNode, CDATASection.class);
							if (rawData != null) {
								final byte[] data = parseString(rawData.getData());
								if (data != null) {
									resources.add(id, data, mimeType);
									++nb;
								}
							}
						}
					}
				} catch (AssertionError e) {
					throw e;
				} catch (IOException e) {
					throw e;
				} catch (Throwable e) {
					throw new IOException(e);
				}
			}
		}
		return nb;
	}

	/** Replies the resource URL that is contained inside the XML attribute
	 * defined in the given node and with the given XML path.
	 *
	 * @param node is the XML node to read.
	 * @param resources is the collection of resources to read.
	 * @param path is the XML path, relative to the node, of the attribute. The last
	 *     element of the array is the name of the attribute.
	 * @return the resource URL, or {@code null} if it cannot be retrieved.
	 */
	@Pure
	public static URL readResourceURL(Element node, XMLResources resources, String... path) {
		final String stringValue = getAttributeValue(node, path);
		if (XMLResources.isStringIdentifier(stringValue)) {
			try {
				final long id = XMLResources.getNumericalIdentifier(stringValue);
				return resources.getResourceURL(id);
			} catch (Throwable exception) {
				//
			}
		}
		return null;
	}

	/**
	 * Utility class for manipulating XML data and files.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private static class NameBasedIterator implements Iterator<Node> {

		private final String nodeName;

		private final NodeList list;

		private Node next;

		private int index;

		/** Constructor.
		 * @param parent the parent node.
		 * @param nodeName the child node name.
		 */
		NameBasedIterator(Node parent, String nodeName) {
			this.index = 0;
			this.list = parent.getChildNodes();
			this.nodeName = nodeName;
			searchNode();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public Node next() {
			final Node child = this.next;
			if (child == null) {
				throw new NoSuchElementException();
			}
			searchNode();
			return child;
		}

		private void searchNode() {
			this.next = null;
			while (this.index < this.list.getLength()) {
				final Node child = this.list.item(this.index);
				++this.index;
				if (this.nodeName.equals(child.getNodeName())) {
					this.next = child;
					return;
				}
			}
		}

	}

}
