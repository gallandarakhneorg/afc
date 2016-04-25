/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.attrs.attr;

import java.text.MessageFormat;
import java.util.Date;

/**
 * Container of timestamp.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class Timestamp extends Number {

	private static final long serialVersionUID = 499564400887069856L;
	
	private final long time;
	
	/**
	 * @param time
	 */
	public Timestamp(long time) {
		this.time = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doubleValue() {
		return this.time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		return this.time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int intValue() {
		return (int)this.time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		return this.time;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Number) {
			return this.time==((Number)obj).longValue();
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(this.time).hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return MessageFormat.format("{0,date,full} {0,time,full}", //$NON-NLS-1$
				new Date(this.time));
	}
	
}