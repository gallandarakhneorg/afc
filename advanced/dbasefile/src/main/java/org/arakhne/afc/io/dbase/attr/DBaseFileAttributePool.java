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

package org.arakhne.afc.io.dbase.attr;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.attr.AttributeException;
import org.arakhne.afc.attrs.attr.AttributeType;
import org.arakhne.afc.attrs.collection.NoAttributeFoundException;
import org.arakhne.afc.io.dbase.DBaseFieldType;
import org.arakhne.afc.io.dbase.DBaseFileField;
import org.arakhne.afc.io.dbase.DBaseFileReader;
import org.arakhne.afc.io.dbase.DBaseFileRecord;
import org.arakhne.afc.references.WeakValueTreeMap;
import org.arakhne.afc.util.OutputParameter;
import org.arakhne.afc.vmutil.Resources;

/**
 * This class provides a pool of attributes obtained from
 * a DBase file and that could be used by dedicated attribute providers.
 *
 * <p>This class permits to syndicate the access to a single dBase file
 * by several attribute provider, assuming that an attribute provider
 * accesses only one record of the dBase file.
 *
 * <p>This class uses a soft-referenced {@link DBaseFileReader} instance.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see DBaseFileAttributeProvider
 * @see DBaseFileAttributeProvider
 */
public final class DBaseFileAttributePool {

	/** List of loaded attribute pool.
	 *
	 * <p>This list contains weak references because
	 * the pools should be removed from memory
	 * when they are no more used by a pool client.
	 */
	private static Map<String, DBaseFileAttributePool> pools;

	/** Location of the dBase file.
	 */
	private final URL url;

	/** List of accessors associated to this pool.
	 */
	private final Map<Integer, DBaseFileAttributeAccessor> accessors = new WeakValueTreeMap<>();

	/** Raw reader of the dBase file.
	 */
	private transient DBaseFileReader reader;

	/** Create an attribute pool from the specified local resource.
	 *
	 * @param dbaseFile is the resource that corresponds to a dBase file.
	 */
	private DBaseFileAttributePool(URL dbaseFile) {
		assert dbaseFile != null;
		this.url = dbaseFile;
	}

	/** Get the attribute pool that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file from which the attributes could be extracted.
	 * @return the pool associated to the given file.
	 */
	static DBaseFileAttributePool getPool(URL dbaseFile) {
		DBaseFileAttributePool pool = null;
		if (dbaseFile != null) {
			if (pools == null) {
				pools = new WeakValueTreeMap<>();
			}
			pool = pools.get(dbaseFile.toExternalForm());
			if (pool == null) {
				pool = new DBaseFileAttributePool(dbaseFile);
				pools.put(dbaseFile.toExternalForm(), pool);
			}
		}
		return pool;
	}

	/**
	 * Get the attribute pool that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file from which the attributes could be extracted.
	 * @return the pool associated to the given file.
	 */
	static DBaseFileAttributePool getPool(URI dbaseFile) {
		try {
			return getPool(dbaseFile.toURL());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/** Get the attribute pool that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file from which the attributes could be extracted.
	 * @return the pool associated to the given file.
	 */
	static DBaseFileAttributePool getPool(File dbaseFile) {
		try {
			return getPool(dbaseFile.toURI().toURL());
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * Get the attribute pool that corresponds to the specified resource.
	 *
	 * <p>The resource should be located according to the
	 * {@link Class#getResource(String)} or
	 * {@link Class#getResourceAsStream(String)} functions.
	 *
	 * @param resource is the resource from which the attributes could be extracted.
	 * @return the pool associated to the given resource.
	 */
	static DBaseFileAttributePool getPool(String resource) {
		return getPool(Resources.getResource(resource));
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeProvider getContainer(URL dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeProvider(accessor);
			}
		}
		return null;
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeProvider getProvider(URI dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeProvider(accessor);
			}
		}
		return null;
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeProvider getProvider(File dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeProvider(accessor);
			}
		}
		return null;
	}

	/**
	 * Get an attribute container that corresponds to the specified file
	 *
	 * <p>The resource should be located according to the
	 * {@link Class#getResource(String)} or
	 * {@link Class#getResourceAsStream(String)} functions.
	 *
	 * @param resource is the resource to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeProvider getProvider(String resource, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(resource);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeProvider(accessor);
			}
		}
		return null;
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeCollection getCollection(URL dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeCollection(accessor);
			}
		}
		return null;
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeCollection getCollection(URI dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeCollection(accessor);
			}
		}
		return null;
	}

	/** Get an attribute container that corresponds to the specified file.
	 *
	 * @param dbaseFile is the file to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeCollection getCollection(File dbaseFile, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(dbaseFile);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeCollection(accessor);
			}
		}
		return null;
	}

	/**
	 * Get an attribute container that corresponds to the specified file
	 *
	 * <p>The resource should be located according to the
	 * {@link Class#getResource(String)} or
	 * {@link Class#getResourceAsStream(String)} functions.
	 *
	 * @param resource is the resource to read
	 * @param recordNumber is the index of the record inside the file ({@code 0..size-1}).
	 * @return a container or {@code null} on error
	 */
	@Pure
	public static DBaseFileAttributeCollection getCollection(String resource, int recordNumber) {
		final DBaseFileAttributePool pool = getPool(resource);
		if (pool != null) {
			final DBaseFileAttributeAccessor accessor = pool.getAccessor(recordNumber);
			if (accessor != null) {
				return new DBaseFileAttributeCollection(accessor);
			}
		}
		return null;
	}

	/** Close all opened accessors.
	 *
	 * @throws IOException in case of error.
	 */
	public void close() throws IOException {
		this.accessors.clear();
		if (this.reader != null) {
			this.reader.close();
			this.reader = null;
		}
	}

	@Override
	@SuppressWarnings("checkstyle:nofinalizer")
	@Deprecated(since = "17.0", forRemoval = true)
	public void finalize() throws Throwable {
		try {
			close();
		} catch (IOException exception) {
			// Ignore error
		}
		super.finalize();
	}

	/** Replies the resource used by this pool.
	 *
	 * @return the resource used by this pool.
	 */
	@Pure
	public URL getResource() {
		return this.url;
	}

	/** Replies the reader.
	 *
	 * @return the reader.
	 * @throws IOException in case of error.
	 */
	@Pure
	@SuppressWarnings("resource")
	protected DBaseFileReader getReader() throws IOException {
		if (this.reader == null) {
			this.reader = new DBaseFileReader(this.url.openStream());
			this.reader.readDBFHeader();
			this.reader.readDBFFields();
		}
		return this.reader;
	}

	/**
	 * Replies the names of all the attributes that corresponds to the specified
	 * record number.
	 *
	 * <p>The parameter is not used because, in a dBase file, all the record
	 * have the same attribute names.
	 *
	 * @param recordNumber the number of the record.
	 * @return the collection of attribute names
	 */
	@SuppressWarnings("resource")
	@Pure
	public Collection<String> getAllAttributeNames(int recordNumber) {
		try {
			final DBaseFileReader dbReader = getReader();
			synchronized (dbReader) {
				final List<DBaseFileField> fields = dbReader.getDBFFields();
				if (fields == null) {
					return Collections.emptyList();
				}
				final ArrayList<String> titles = new ArrayList<>(fields.size());
				for (int i = 0; i < fields.size(); ++i) {
					titles.add(fields.get(i).getName());
				}
				return titles;
			}
		} catch (IOException exception) {
			return Collections.emptyList();
		}
	}

	/** Replies the count of attributes.
	 *
	 * @return the count of attributes
	 */
	@SuppressWarnings("resource")
	@Pure
	public int getAttributeCount() {
		try {
			final DBaseFileReader dbReader = getReader();
			synchronized (dbReader) {
				return dbReader.getDBFFieldCount();
			}
		} catch (IOException exception) {
			return 0;
		}
	}

	/**
	 * Replies the raw value that corresponds to the specified
	 * attribute name for the given record.
	 *
	 * @param recordNumber is the index of the record to read ({@code 0..recordCount-1}).
	 * @param name is the name of the attribute value to replies.
	 * @param type is the type of the replied value. Tis attribute will be set by this function.
	 * @return the value extracted from the pool.
	 * @throws AttributeException if an attribute cannot be read.
	 * @throws NoAttributeFoundException no attribute with the given name was found into the database.
	 */
	@SuppressWarnings("resource")
	@Pure
	public Object getRawValue(int recordNumber, String name, OutputParameter<AttributeType> type) throws AttributeException {
		try {
			final DBaseFileReader dbReader = getReader();
			synchronized (dbReader) {
				dbReader.seek(recordNumber);
				final DBaseFileRecord record = dbReader.readNextDBFRecord();
				if (record != null) {
					final int column = dbReader.getDBFFieldIndex(name);
					if (column >= 0) {
						final Object value = record.getFieldValue(column);
						final DBaseFieldType dbfType = dbReader.getDBFFieldType(column);
						type.set(dbfType.toAttributeType());
						return value;
					}
				}
				throw new NoAttributeFoundException(name);
			}
		} catch (IOException e) {
			throw new AttributeException(e);
		}
	}

	/** Replies the attribute accessor associated to the specified
	 * record number.
	 *
	 * @param recordNumber is the number of the record for which an accessor may be obtainable.
	 * @return the accessor to the record at the given position.
	 */
	@Pure
	DBaseFileAttributeAccessor getAccessor(int recordNumber) {
		DBaseFileAttributeAccessor accessor = this.accessors.get(recordNumber);
		if (accessor == null) {
			accessor = new DBaseFileAttributeAccessor(this, recordNumber);
			this.accessors.put(recordNumber, accessor);
		}
		return accessor;
	}

}
