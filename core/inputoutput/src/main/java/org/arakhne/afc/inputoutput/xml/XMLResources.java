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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.inputoutput.filetype.FileType;
import org.arakhne.afc.inputoutput.mime.MimeName;
import org.arakhne.afc.inputoutput.path.PathBuilder;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Class that store a collection of resources (URL, files and raw data) and their associated
 * identifiers within a XML file. An object of this type may be used as a repository
 * of resources that are defined within an XML file.
 *
 * <p>MIME types are string and not {@link MimeName} because this enumeration
 * does not exhaustively contains all the MIME types on the World. Use a string
 * is more generic in this case.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class XMLResources implements Comparator<Object> {

	/** Prefix used to create string-representation of an identifier.
	 */
	public static final String IDENTIFIER_PREFIX = "#resource"; //$NON-NLS-1$

	private final PathBuilder pathBuilder;

	private final Map<Object, Long> identifiersFromResources = new TreeMap<>(this);

	private final Map<Long, Entry> resourcesFromIdentifiers = new TreeMap<>();

	private long nextFreeId;

	/** Constructor.
	 * @param pathBuilder is the path builder to use to build the paths of the resources in order to compare the them.
	 */
	public XMLResources(PathBuilder pathBuilder) {
		this.pathBuilder = pathBuilder;
	}

	@Override
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	@Pure
	public int compare(Object o1, Object o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			return Integer.MIN_VALUE;
		}
		if (o2 == null) {
			return Integer.MAX_VALUE;
		}

		if (o1 instanceof byte[] && o2 instanceof byte[]) {
			return compareArrays((byte[]) o1, (byte[]) o2);
		} else if (o1 instanceof byte[]) {
			return Integer.MAX_VALUE;
		} else if (o2 instanceof byte[]) {
			return Integer.MIN_VALUE;
		}

		final URL a1;
		final URL a2;
		if (this.pathBuilder != null) {
			if (o1 instanceof URL) {
				a1 = this.pathBuilder.makeAbsolute((URL) o1);
			} else if (o1 instanceof File) {
				a1 = this.pathBuilder.makeAbsolute((File) o1);
			} else {
				throw new IllegalStateException();
			}
			if (o2 instanceof URL) {
				a2 = this.pathBuilder.makeAbsolute((URL) o2);
			} else if (o2 instanceof File) {
				a2 = this.pathBuilder.makeAbsolute((File) o2);
			} else {
				throw new IllegalStateException();
			}
		} else {
			if (o1 instanceof URL) {
				a1 = (URL) o1;
			} else if (o1 instanceof File) {
				try {
					a1 = ((File) o1).toURI().toURL();
				} catch (MalformedURLException e) {
					throw new IllegalStateException(e);
				}
			} else {
				throw new IllegalStateException();
			}
			if (o2 instanceof URL) {
				a2 = (URL) o2;
			} else if (o2 instanceof File) {
				try {
					a2 = ((File) o2).toURI().toURL();
				} catch (MalformedURLException e) {
					throw new IllegalStateException(e);
				}
			} else {
				throw new IllegalStateException();
			}
		}
		return a1.toString().compareTo(a2.toString());
	}

	private static int compareArrays(byte[] a1, byte[] a2) {
		assert a1 != null : AssertMessages.notNullParameter(0);
		assert a2 != null : AssertMessages.notNullParameter(1);
		return System.identityHashCode(a1) - System.identityHashCode(a2);
	}

	/** Replies a string-representation of the given identifier.
	 *
	 * <p>The function add the prefix {@link #IDENTIFIER_PREFIX} to the string representation of the given identifier.
	 *
	 * @param identifier the identifier to convert.
	 * @return the string-representation of the given identifier.
	 * @see #getNumericalIdentifier(String)
	 * @see #isStringIdentifier(String)
	 */
	@Pure
	public static String getStringIdentifier(long identifier) {
		return IDENTIFIER_PREFIX + Long.toString(identifier);
	}

	/** Replies if the given string is a string-representation of a resource identifier.
	 *
	 * <p>A string is an identifier if it starts with {@link #IDENTIFIER_PREFIX}.
	 *
	 * @param str the string to test.
	 * @return {@code true} if the given string is the string-representation
	 *     of an identifier; otherwise {@code false}.
	 * @see #getNumericalIdentifier(String)
	 * @see #getStringIdentifier(long)
	 */
	@Pure
	public static boolean isStringIdentifier(String str) {
		return str != null && str.length() > IDENTIFIER_PREFIX.length() && str.startsWith(IDENTIFIER_PREFIX);
	}

	/** Replies the numerical identifier for the given string-identifier.
	 *
	 * @param identifier the identifier to convert.
	 * @return the numerical-representation of the given identifier.
	 * @see #getStringIdentifier(long)
	 * @see #isStringIdentifier(String)
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	@Pure
	public static long getNumericalIdentifier(String identifier) {
		if (identifier != null && identifier.startsWith(IDENTIFIER_PREFIX)) {
			try {
				return Long.valueOf(identifier.substring(9));
			} catch (Throwable exception) {
				//
			}
		}
		throw new IllegalArgumentException();
	}

	/** Add an URL in the classifier.
	 *
	 * @param identifier the identifier of the URL.
	 * @param url the value of the URL.
	 * @param mimeType may be {@code null}
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 */
	public synchronized String add(long identifier, URL url, String mimeType) {
		assert url != null : AssertMessages.notNullParameter(1);
		String mt = mimeType;
		if (mt == null || "".equals(mt)) { //$NON-NLS-1$
			mt = FileType.getContentType(url);
		}
		Long idObj;
		if (identifier < 0) {
			idObj = getIdentifierFromResource(url);
			if (idObj == null) {
				idObj = computeNextIdentifier();
			}
		} else {
			idObj = Long.valueOf(identifier);
		}
		this.identifiersFromResources.put(url, idObj);
		this.resourcesFromIdentifiers.put(idObj, new Entry(url, mt));
		return getStringIdentifier(idObj.longValue());
	}

	/** Add a file in the classifier.
	 *
	 * @param identifier the identifier of the file.
	 * @param file the name of the file.
	 * @param mimeType may be {@code null}
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 */
	public synchronized String add(long identifier, File file, String mimeType) {
		assert file != null : AssertMessages.notNullParameter(1);
		String mt = mimeType;
		if (mt == null || "".equals(mt)) { //$NON-NLS-1$
			mt = FileType.getContentType(file);
		}
		Long idObj;
		if (identifier < 0) {
			idObj = getIdentifierFromResource(file);
			if (idObj == null) {
				idObj = computeNextIdentifier();
			}
		} else {
			idObj = Long.valueOf(identifier);
		}
		this.identifiersFromResources.put(file, idObj);
		this.resourcesFromIdentifiers.put(idObj, new Entry(file, mt));
		return getStringIdentifier(idObj.longValue());
	}

	/** Add a embedded data in the classifier.
	 *
	 * @param identifier the identifier of the data.
	 * @param rawData the value of the data.
	 * @param mimeType may be {@code null}
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 */
	public synchronized String add(long identifier, byte[] rawData, String mimeType) {
		assert rawData != null : AssertMessages.notNullParameter(1);
		String mt = mimeType;
		if (mt == null || "".equals(mt)) { //$NON-NLS-1$
			mt = MimeName.MIME_OCTET_STREAM.getMimeConstant();
		}
		Long idObj;
		if (identifier < 0) {
			idObj = getIdentifierFromResource(rawData);
			if (idObj == null) {
				idObj = computeNextIdentifier();
			}
		} else {
			idObj = Long.valueOf(identifier);
		}
		this.identifiersFromResources.put(rawData, idObj);
		this.resourcesFromIdentifiers.put(idObj, new Entry(rawData, mt));
		return getStringIdentifier(idObj.longValue());
	}

	/** Add the given URL into this classifier and replies
	 * the string representation of the given URL.
	 * Try to simplify the given URL with the path builder and
	 * to use a reference registered through the file classifier.
	 *
	 * @param url is the URL to treat.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add($1, null)")
	public final String add(URL url) throws IOException {
		return add(url, null);
	}

	/** Add the given URL into this classifier and replies
	 * the string representation of the given URL.
	 * Try to simplify the given URL with the path builder and
	 * to use a reference registered through the file classifier.
	 *
	 * @param url is the URL to treat.
	 * @param mimeType is the mime type associated to the file.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add(-1, $1, $2)")
	public final String add(URL url, String mimeType) throws IOException {
		return add(-1, url, mimeType);
	}

	/** Add the given file into this classifier and replies
	 * the string representation of the given file.
	 * Try to simplify the given file with the path builder and
	 * to use a reference registered through the file classifier.
	 *
	 * @param file is the file  to treat.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add($1, null)")
	public final String add(File file) throws IOException {
		return add(file, null);
	}

	/** Add the given File into this classifier and replies
	 * the string representation of the given file.
	 * Try to simplify the given file with the path builder and
	 * to use a reference registered through the file classifier.
	 *
	 * @param file is the file to treat.
	 * @param mimeType is the mime type associated to the file.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add(-1, $1, $2)")
	public final String add(File file, String mimeType) throws IOException {
		return add(-1, file, mimeType);
	}

	/** Add the given embedded data into this classifier and replies
	 * the string representation of the given embedded data.
	 *
	 * @param rawData is the embedded data to treat.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add($1, null)")
	public final String add(byte[] rawData) throws IOException {
		return add(rawData, null);
	}

	/** Add the given embedded data into this classifier and replies
	 * the string representation of the given embedded data.
	 *
	 * @param rawData is the embedded data to treat.
	 * @param mimeType is the mime type associated to the embedded data.
	 * @return the string representation, basically the reference to the
	 *     resource given by the file classifier.
	 * @throws IOException in case of error.
	 */
	@Inline(value = "add(-1, $1, $2)")
	public final String add(byte[] rawData, String mimeType) throws IOException {
		return add(-1, rawData, mimeType);
	}

	/** Compute the next available identifier for
	 * a resource.
	 *
	 * @return an identifier for a resource.
	 */
	public synchronized long computeNextIdentifier() {
		long id = this.nextFreeId;
		while (this.resourcesFromIdentifiers.containsKey(id)) {
			++id;
		}
		this.nextFreeId = id + 1;
		return id;
	}

	/** Replies the identifier that is corresponding to the given URL.
	 *
	 * @param resource is the resource to search for.
	 * @return the identifier; or {@code null} if not found.
	 */
	@Pure
	protected synchronized Long getIdentifierFromResource(Object resource) {
		return this.identifiersFromResources.get(resource);
	}

	/** Replies the identifier that is corresponding to the given URL.
	 *
	 * @param id is the identifier to search for.
	 * @return the resource; or {@code null} if not found.
	 */
	@Pure
	protected synchronized Entry getResourceFromIdentifier(long id) {
		return this.resourcesFromIdentifiers.get(id);
	}

	/** Replies the identifier of the given url.
	 *
	 * @param url the url to search for.
	 * @return the identifier or {@code -1} if the url was not found.
	 */
	@Pure
	public final long getIdentifier(URL url) {
		final Long id = getIdentifierFromResource(url);
		if (id == null) {
			return -1;
		}
		return id.longValue();
	}

	/** Replies the identifier of the given file.
	 *
	 * @param file the file to search for.
	 * @return the identifier or {@code -1} if the file was not found.
	 */
	@Pure
	public final long getIdentifier(File file) {
		final Long id = getIdentifierFromResource(file);
		if (id == null) {
			return -1;
		}
		return id.longValue();
	}

	/** Replies the identifier of the given embedded data.
	 *
	 * @param rawData the data to search for.
	 * @return the identifier or {@code -1} if the data was not found.
	 */
	@Pure
	public final long getIdentifier(byte[] rawData) {
		final Long id = getIdentifierFromResource(rawData);
		if (id == null) {
			return -1;
		}
		return id.longValue();
	}

	/** Replies the resource URL.
	 *
	 * @param identifier the identifier to search for.
	 * @return the URL or {@code null}
	 */
	@Pure
	public final URL getResourceURL(long identifier) {
		final Entry o = getResourceFromIdentifier(identifier);
		if (o.isURL()) {
			return this.pathBuilder.makeAbsolute(o.getURL());
		}
		if (o.isFile()) {
			return this.pathBuilder.makeAbsolute(o.getFile());
		}
		return null;
	}

	/** Replies the resource.
	 *
	 * @param identifier the identifier to search for.
	 * @return the URL or {@code null}
	 */
	@Pure
	@Inline(value = "getResourceFromIdentifier($1)")
	public final Entry getResource(long identifier) {
		return getResourceFromIdentifier(identifier);
	}

	/** Remove the given url from this classifier.
	 *
	 * @param url the url to remove.
	 */
	public synchronized void remove(URL url) {
		final Long id = this.identifiersFromResources.remove(url);
		if (id != null) {
			this.resourcesFromIdentifiers.remove(id);
		}
	}

	/** Remove the given file from this classifier.
	 *
	 * @param file the file to remove.
	 */
	public synchronized void remove(File file) {
		final Long id = this.identifiersFromResources.remove(file);
		if (id != null) {
			this.resourcesFromIdentifiers.remove(id);
		}
	}

	/** Remove the given embedded data from this classifier.
	 *
	 * @param rawData the data to remove.
	 */
	public synchronized void remove(byte[] rawData) {
		final Long id = this.identifiersFromResources.remove(rawData);
		if (id != null) {
			this.resourcesFromIdentifiers.remove(id);
		}
	}

	/** Remove the resource with the given identifier.
	 *
	 * @param identifier the identifier to remove.
	 */
	public synchronized void remove(long identifier) {
		final Entry o = this.resourcesFromIdentifiers.remove(identifier);
		if (o != null) {
			this.identifiersFromResources.remove(o.getResource());
		}
	}

	/** Replies the content of this classifier.
	 *
	 * @return the resource-identifier pairs.
	 */
	@Pure
	public Map<Long, Entry> getPairs() {
		return Collections.unmodifiableMap(Collections.synchronizedMap(this.resourcesFromIdentifiers));
	}

	/** Clear all the register file and URL.
	 */
	public synchronized void clear() {
		this.identifiersFromResources.clear();
		this.resourcesFromIdentifiers.clear();
		this.nextFreeId = 0;
	}

	/** Entry in a XMLResourceClassifier.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 15.0
	 */
	public class Entry {

		private final Object resource;

		private final String mimeType;

		/** Constructor.
		 *
		 * @param resource the resource.
		 * @param mimeType the mime type of the resource.
		 */
		public Entry(Object resource, String mimeType) {
			this.resource = resource;
			this.mimeType = mimeType;
		}

		/** Replies the resource.
		 *
		 * @return the resource.
		 */
		Object getResource() {
			return this.resource;
		}

		/** Replies if this entry contains an URL.
		 *
		 * @return {@code true} if the resource is an URL, otherwise {@code false}
		 */
		@Pure
		public boolean isURL() {
			return this.resource instanceof URL;
		}

		/** Replies if this entry contains a File.
		 *
		 * @return {@code true} if the resource is a File, otherwise {@code false}
		 */
		@Pure
		public boolean isFile() {
			return this.resource instanceof File;
		}

		/** Replies if this entry contains an embedded data.
		 *
		 * @return {@code true} if the resource is an embedded data, otherwise {@code false}
		 */
		@Pure
		public boolean isEmbeddedData() {
			return this.resource instanceof byte[];
		}

		/** Replies the URL in this entry.
		 *
		 * @return the url or {@code null} if the entry
		 *     is not an URL.
		 */
		@Pure
		public URL getURL() {
			if (this.resource instanceof URL) {
				return (URL) this.resource;
			}
			return null;
		}

		/** Replies the File in this entry.
		 *
		 * @return the url or {@code null} if the entry
		 *     is not a File.
		 */
		@Pure
		public File getFile() {
			if (this.resource instanceof File) {
				return (File) this.resource;
			}
			return null;
		}

		/** Replies the embedded data in this entry.
		 *
		 * @return the embedded data or {@code null} if the entry
		 *     is not an embedded data.
		 */
		@Pure
		public byte[] getEmbeddedData() {
			if (this.resource instanceof byte[]) {
				return (byte[]) this.resource;
			}
			return null;
		}

		/** Replies the mime type associated to the entry.
		 *
		 * @return the mime type.
		 */
		@Pure
		public String getMimeType() {
			return this.mimeType;
		}

	}

}
