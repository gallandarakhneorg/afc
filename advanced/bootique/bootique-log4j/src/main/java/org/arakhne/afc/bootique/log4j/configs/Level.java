/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.bootique.log4j.configs;

/**
 * Enumeration of the Log4j levels.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public enum Level {

	/** No logging.
	 */
	OFF(org.apache.log4j.Level.OFF),

	/** Error.
	 */
	ERROR(org.apache.log4j.Level.ERROR),

	/** Warning.
	 */
	WARNING(org.apache.log4j.Level.WARN),

	/** Information.
	 */
	INFO(org.apache.log4j.Level.INFO),

	/** Debug.
	 */
	DEBUG(org.apache.log4j.Level.DEBUG),

	/** Trace.
	 */
	TRACE(org.apache.log4j.Level.TRACE),

	/** All.
	 */
	ALL(org.apache.log4j.Level.ALL);

	private final org.apache.log4j.Level log4j;

	Level(org.apache.log4j.Level log4j) {
		this.log4j = log4j;
	}

	/** Replies the log4j level.
	 *
	 * @return the log4j level.
	 */
	public org.apache.log4j.Level toLog4j() {
		return this.log4j;
	}

	/** Replies the string representation of all the labels.
	 *
	 * @return all the labels.
	 */
	public static String getLabels() {
		final StringBuilder labels = new StringBuilder();
		for (final Level level : values()) {
			if (labels.length() > 0) {
				labels.append(", "); //$NON-NLS-1$
			}
			labels.append(level.name());
		}
		return labels.toString();
	}

}
