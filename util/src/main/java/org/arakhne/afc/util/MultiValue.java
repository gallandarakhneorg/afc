/* 
 * $Id$
 * 
 * Copyright (c) 2005-10 Multiagent Team, Laboratoire Systemes et Transports,
 *                       Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.util;

import org.arakhne.vmutil.locale.Locale;

/** Utilities class that permits to represent a collection of values
 * and indicating if they are all the same or not.
 *
 * @param <T> is the type of the value.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MultiValue<T> {

	private boolean isSet =  false;
	private boolean isMultiple = false;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return toString(Locale.getString("MULTIPLE_VALUES")); //$NON-NLS-1$
	}

	/**
	 * Replies a string representation of the value in this object.
	 * If this object is set and has multiple different values, the
	 * parameter <var>multiValueLabel</var> is replied.
	 * If this object is not set or equals to <code>null</code>,
	 * the empty string is replied.
	 * Otherwise the value is replied. 
	 * 
	 * @param multiValueLabel is the label to reply if this object is containing many different values.
	 * @return the string representation of this object.
	 */
	public String toString(String multiValueLabel) {
		if (this.isSet) {
			 if (this.isMultiple) {
				 return multiValueLabel;
			 }
			 if (this.object!=null) {
				 return this.object.toString();
			 }
		}
		return ""; //$NON-NLS-1$
	}

	/** Replies the value.
	 * 
	 * @return the value embedded inside this object.
	 */
	public T get() {
		return this.object;
	}
	
	/** Replies the type of the value.
	 * 
	 * @return the type or <code>null</code> if there is no value.
	 */
	@SuppressWarnings("unchecked")
	public Class<? extends T> getValueType() {
		if (this.object==null) return null;
		return (Class<? extends T>)this.object.getClass();
	}

	/** Add the given value to the styored value.
	 * 
	 * @param newValue
	 */
	public void add(T newValue) {
		if (this.isSet) {
			if (!this.isMultiple
				&& newValue!=this.object && (newValue==null || !newValue.equals(this.object))) {
				this.isMultiple = true;
				this.object = null;
			}
		}
		else {
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
	public boolean isSet() {
		return this.isSet;
	}

	/** Replies if this MultiValue contains different values.
	 * 
	 * @return <code>true</code> is values are different, otherwise <code>false</code>
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof MultiValue<?>) {
			MultiValue<?> v = (MultiValue<?>)o;
			if (this.isSet!=v.isSet || this.isMultiple!=v.isMultiple)
				return false;
		}
		return (this.object==o)
				||
				(this.object!=null && this.object.equals(o));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int h = HashCodeUtil.hash(this.isSet);
		h = HashCodeUtil.hash(h, this.isMultiple);
		if (this.object!=null) h = HashCodeUtil.hash(h, this.object);
		return h;
	}

}