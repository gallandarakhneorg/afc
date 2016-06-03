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

package org.arakhne.afc.attrs.attr;

import java.text.MessageFormat;
import java.util.Date;

import org.eclipse.xtext.xbase.lib.Pure;

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

	/** Construct.
	 *
	 * @param time the time.
	 */
	Timestamp(long time) {
		this.time = time;
	}

	@Pure
	@Override
	public double doubleValue() {
		return this.time;
	}

	@Pure
	@Override
	public float floatValue() {
		return this.time;
	}

	@Pure
	@Override
	public int intValue() {
		return (int) this.time;
	}

	@Pure
	@Override
	public long longValue() {
		return this.time;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Number) {
			return this.time == ((Number) obj).longValue();
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		return Long.valueOf(this.time).hashCode();
	}

	@Pure
	@Override
	public String toString() {
		return MessageFormat.format("{0, date, full} {0, time, full}", //$NON-NLS-1$
				new Date(this.time));
	}

}
