/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Strings;
import org.apache.log4j.ConsoleAppender;

/**
 * The target on the console.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public enum ConsoleTarget {

	/** Standard output.
	 */
	STDOUT {

		@Override
		public String toLog4j() {
			return ConsoleAppender.SYSTEM_OUT;
		}

	},

	/** Standard error.
	 */
	STDERR {

		@Override
		public String toLog4j() {
			return ConsoleAppender.SYSTEM_OUT;
		}

	};

	/** Replies the log4j representation of the target.
	 *
	 * @return the target representation.
	 */
	public abstract String toLog4j();

	/** Parse a case insensitive string for obtaining the console target.
	 *
	 * @param name the name to parse.
	 * @return the console target.
	 * @throws NullPointerException is case the given name is {@code null} or empty.
	 * @since 16.0
	 */
	@JsonCreator
	public static ConsoleTarget valueOfCaseInsensitive(String name) {
		if (Strings.isNullOrEmpty(name)) {
			throw new NullPointerException("Name is null"); //$NON-NLS-1$
		}
		return valueOf(name.toUpperCase());
	}

	/** Replies the preferred string representation of the console target within a Json stream.
	 *
	 * @return the string representation of the console target.
	 * @since 16.0
	 */
	@JsonValue
	public String toJsonString() {
		return name().toLowerCase();
	}

}
