/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.util;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/** Utilities class that permits to represent a collection of values
 * and indicating if they are all the same or not.
 *
 * @param <T> is the type of the value.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiValue<T> implements Serializable {

	private static final long serialVersionUID = -3030110135594544927L;

	private boolean isSet;

	private boolean isMultiple;

	private T object;

	/**
	 * @param initialValue is the initial value of the output parameter.
	 */
	public MultiValue(T initialValue) {
		this.object = initialValue;
		this.isSet = true;
	}

	/**
	 * Create empty output parameter.
	 */
	public MultiValue() {
		this.object = null;
	}

	@Override
	@Pure
	public String toString() {
		return toString(Locale.getString("MULTIPLE_VALUES")); //$NON-NLS-1$
	}

	/**
	 * Replies a string representation of the value in this object.
	 * If this object is set and has multiple different values, the
	 * parameter {@code multiValueLabel} is replied.
	 * If this object is not set or equals to <code>null</code>,
	 * the empty string is replied.
	 * Otherwise the value is replied.
	 *
	 * @param multiValueLabel is the label to reply if this object is containing many different values.
	 * @return the string representation of this object.
	 */
	@Pure
	public String toString(String multiValueLabel) {
		if (this.isSet) {
			if (this.isMultiple) {
				return multiValueLabel;
			}
			if (this.object != null) {
				return this.object.toString();
			}
		}
		return ""; //$NON-NLS-1$
	}

	/** Replies the value.
	 *
	 * @return the value embedded inside this object.
	 */
	@Pure
	public T get() {
		return this.object;
	}

	/** Replies the type of the value.
	 *
	 * @return the type or <code>null</code> if there is no value.
	 */
	@Pure
	@SuppressWarnings("unchecked")
	public Class<? extends T> getValueType() {
		if (this.object == null) {
			return null;
		}
		return (Class<? extends T>) this.object.getClass();
	}

	/** Add the given value to the styored value.
	 *
	 * @param newValue the new value.
	 */
	public void add(T newValue) {
		if (this.isSet) {
			if (!this.isMultiple
					&& newValue != this.object && (newValue == null || !newValue.equals(this.object))) {
				this.isMultiple = true;
				this.object = null;
			}
		} else {
			this.object = newValue;
			this.isSet = true;
			this.isMultiple = false;
		}
	}

	/** Clear this multi-value.
	 */
	public void clear() {
		this.object = null;
		this.isSet = false;
		this.isMultiple = false;
	}

	/** Replies if the value was set.
	 *
	 * @return <code>true</code> is the value was set, otherwise <code>false</code>
	 */
	@Pure
	public boolean isSet() {
		return this.isSet;
	}

	/** Replies if this MultiValue contains different values.
	 *
	 * @return <code>true</code> is values are different, otherwise <code>false</code>
	 */
	@Pure
	public boolean isMultipleDifferentValues() {
		return this.isMultiple;
	}

	/** Set if this MultiValue contains different values.
	 *
	 * @param multiple is <code>true</code> is values are different, otherwise <code>false</code>
	 */
	public void setMultipleDifferentValues(boolean multiple) {
		this.isMultiple = true;
		this.isSet = true;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiValue<?>) {
			final MultiValue<?> v = (MultiValue<?>) obj;
			if (this.isSet != v.isSet || this.isMultiple != v.isMultiple) {
				return false;
			}
		}
		return (this.object == obj)
				|| (this.object != null && this.object.equals(obj));
	}

	@Pure
	@Override
	public int hashCode() {
		int hash = Boolean.hashCode(this.isSet);
		hash = 31 * hash + Boolean.hashCode(this.isMultiple);
		if (this.object != null) {
			hash = 31 * hash + Objects.hashCode(this.object);
		}
		return hash ^ (hash >> 31);
	}

}
