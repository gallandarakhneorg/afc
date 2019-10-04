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

package org.arakhne.afc.nodefx;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/** Boolean converter. This class is created in order to avoid a direct dependency to the Sun API.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 16.0
 * @deprecated since 0.16, will be removed when moving to openjfx that is providing an implementation of this converter.
 */
@Deprecated
@SuppressWarnings("all")
public class BooleanConverter extends StyleConverter<String, Boolean> {

	// lazy, thread-safe instatiation
	private static class Holder {
		static final BooleanConverter INSTANCE = new BooleanConverter();
	}

	public static StyleConverter<String, Boolean> getInstance() {
		return Holder.INSTANCE;
	}

	private BooleanConverter() {
		super();
	}

	@Override
	public Boolean convert(ParsedValue<String, Boolean> value, Font notUsed) {
		String str = value.getValue();
		return Boolean.valueOf(str);
	}

	@Override
	public String toString() {
		return "BooleanConverter"; //$NON-NLS-1$
	}
}
