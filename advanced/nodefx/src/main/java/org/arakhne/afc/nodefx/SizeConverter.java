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

import com.sun.javafx.css.Size;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/** Size converter. This class is created in order to avoid a direct dependency to the Sun API.
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
public class SizeConverter extends StyleConverter<ParsedValue<?, Size>, Number> {

	// lazy, thread-safe instatiation
	private static class Holder {
		static final SizeConverter INSTANCE = new SizeConverter();
		static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();
	}

	public static StyleConverter<ParsedValue<?, Size>, Number> getInstance() {
		return Holder.INSTANCE;
	}

	private SizeConverter() {
		super();
	}

	@Override
	public Number convert(ParsedValue<ParsedValue<?, Size>, Number> value, Font font) {
		ParsedValue<?, Size> size = value.getValue();
		return size.convert(font).pixels(font);
	}

	@Override
	public String toString() {
		return "SizeConverter"; //$NON-NLS-1$
	}

	/*
	 * Convert [<size>]+ to an array of Number[].
	 */
	public static final class SequenceConverter extends StyleConverter<ParsedValue[], Number[]> {

		public static SequenceConverter getInstance() {
			return Holder.SEQUENCE_INSTANCE;
		}

		private SequenceConverter() {
			super();
		}

		@Override
		public Number[] convert(ParsedValue<ParsedValue[], Number[]> value, Font font) {
			ParsedValue[] sizes = value.getValue();
			Number[] doubles = new Number[sizes.length];
			for (int i = 0; i < sizes.length; i++) {
				doubles[i] = ((Size)sizes[i].convert(font)).pixels(font);
			}
			return doubles;
		}

		@Override
		public String toString() {
			return "Size.SequenceConverter"; //$NON-NLS-1$
		}
	}

}
