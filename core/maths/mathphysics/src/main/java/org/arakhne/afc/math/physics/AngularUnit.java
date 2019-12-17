/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.physics;

import java.util.concurrent.TimeUnit;

import org.eclipse.xtext.xbase.lib.Pure;

/** A <tt>AngularUnit</tt> represents speed at a given unit of
 * granularity.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see MeasureUnitUtil
 */
public enum AngularUnit {

	/** turns/second.
	 */
	TURNS_PER_SECOND,
	/** radians/second.
	 */
	RADIANS_PER_SECOND,
	/** degrees/second.
	 */
	DEGREES_PER_SECOND;

	/** Replies the time unit that corresponds to this angular unit.
	 *
	 * @return the time unit.
	 */
	@Pure
	public TimeUnit toTimeUnit() {
		switch (this) {
		case TURNS_PER_SECOND:
		case RADIANS_PER_SECOND:
		case DEGREES_PER_SECOND:
			return TimeUnit.SECONDS;
		default:
			throw new IllegalArgumentException();
		}
	}

}
