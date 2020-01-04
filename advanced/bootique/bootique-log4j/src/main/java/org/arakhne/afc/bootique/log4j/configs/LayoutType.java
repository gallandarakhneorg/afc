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
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Layout;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.TTCCLayout;
import org.apache.log4j.xml.XMLLayout;

/**
 * The type of logger layout.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public enum LayoutType {

	/** Simple layout.
	 *
	 * @see SimpleLayout
	 */
	SIMPLE {

		@Override
		public Layout createLayout(String defaultLogFormat) {
			return new SimpleLayout();
		}

	},

	/** Pattern layout.
	 *
	 * @see PatternLayout
	 */
	PATTERN {

		@Override
		public Layout createLayout(String defaultLogFormat) {
			if (Strings.isNullOrEmpty(defaultLogFormat)) {
				return SIMPLE.createLayout(defaultLogFormat);
			}
			return new PatternLayout(defaultLogFormat);
		}

	},

	/** Date layout.
	 *
	 * @see TTCCLayout
	 */
	DATE {

		@Override
		public Layout createLayout(String defaultLogFormat) {
			if (Strings.isNullOrEmpty(defaultLogFormat)) {
				return SIMPLE.createLayout(defaultLogFormat);
			}
			return new TTCCLayout();
		}

	},

	/** HTML layout.
	 *
	 * @see HTMLLayout
	 */
	HTML {

		@Override
		public Layout createLayout(String defaultLogFormat) {
			if (Strings.isNullOrEmpty(defaultLogFormat)) {
				return SIMPLE.createLayout(defaultLogFormat);
			}
			return new HTMLLayout();
		}

	},

	/** XML layout.
	 *
	 * @see XMLLayout
	 */
	XML {

		@Override
		public Layout createLayout(String defaultLogFormat) {
			if (Strings.isNullOrEmpty(defaultLogFormat)) {
				return SIMPLE.createLayout(defaultLogFormat);
			}
			return new XMLLayout();
		}

	};

	/** Create a layout.
	 *
	 * @param defaultLogFormat the default log format.
	 * @return the layout.
	 */
	public abstract Layout createLayout(String defaultLogFormat);

	/** Parse a case insensitive string for obtaining the layout type.
	 *
	 * @param name the name to parse.
	 * @return the layout type.
	 * @since 16.0
	 */
	@JsonCreator
	public static LayoutType valueOfCaseInsensitive(String name) {
		if (Strings.isNullOrEmpty(name)) {
			throw new NullPointerException("Name is null"); //$NON-NLS-1$
		}
		return valueOf(name.toUpperCase());
	}

	/** Replies the preferred string representation of the layout type within a Json stream.
	 *
	 * @return the string representation of the layout type.
	 * @since 16.0
	 */
	@JsonValue
	public String toJsonString() {
		return name().toLowerCase();
	}

}
