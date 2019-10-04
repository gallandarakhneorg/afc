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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;

/** Enumeration converter. This class is created in order to avoid a direct dependency to the Sun API.
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
public class EnumConverter<E extends Enum<E>> extends StyleConverter<String, E> {

	// package for unit testing
	final Class<E> enumClass;

	public EnumConverter(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override
	public E convert(ParsedValue<String, E> value, Font not_used) {
		if (this.enumClass == null) {
			return null;
		}
		String string = value.getValue();
		final int dotPos = string.lastIndexOf('.');
		if (dotPos > -1) {
			string = string.substring(dotPos + 1);
		}
		try {
			string = string.replace('-', '_');
			return Enum.valueOf(this.enumClass, string.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException e) {
			// may throw another IllegalArgumentException
			return Enum.valueOf(this.enumClass, string);
		}
	}

	public static StyleConverter<?,?> readBinary(DataInputStream is, String[] strings)
			throws IOException {

		short index = is.readShort();
		String ename = 0 <= index && index <= strings.length ? strings[index] : null;

		if (ename == null || ename.isEmpty()) return null;

		if (converters == null || converters.containsKey(ename) == false) {
			StyleConverter<?,?> converter = getInstance(ename);

			if (converters == null) converters = new HashMap<>();
			converters.put(ename, converter);
			return converter;
		}
		return converters.get(ename);
	}

	private static Map<String,StyleConverter<?,?>> converters;

	// package for unit testing
	static public StyleConverter<?,?> getInstance(final String ename) {

		StyleConverter<?,?> converter = null;

		switch (ename) {
		case "com.sun.javafx.cursor.CursorType" : //$NON-NLS-1$
			converter = new EnumConverter<>(com.sun.javafx.cursor.CursorType.class);
			break;
		case "javafx.scene.layout.BackgroundRepeat" : //$NON-NLS-1$
		case "com.sun.javafx.scene.layout.region.Repeat" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.layout.BackgroundRepeat.class);
			break;
		case "javafx.geometry.HPos" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.geometry.HPos.class);
			break;
		case "javafx.geometry.Orientation" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.geometry.Orientation.class);
			break;
		case "javafx.geometry.Pos" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.geometry.Pos.class);
			break;
		case "javafx.geometry.Side" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.geometry.Side.class);
			break;
		case "javafx.geometry.VPos" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.geometry.VPos.class);
			break;
		case "javafx.scene.effect.BlendMode" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.effect.BlendMode.class);
			break;
		case "javafx.scene.effect.BlurType" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.effect.BlurType.class);
			break;
		case "javafx.scene.paint.CycleMethod" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.paint.CycleMethod.class);
			break;
		case "javafx.scene.shape.ArcType" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.shape.ArcType.class);
			break;
		case "javafx.scene.shape.StrokeLineCap" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.shape.StrokeLineCap.class);
			break;
		case "javafx.scene.shape.StrokeLineJoin" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.shape.StrokeLineJoin.class);
			break;
		case "javafx.scene.shape.StrokeType" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.shape.StrokeType.class);
			break;
		case "javafx.scene.text.FontPosture" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.text.FontPosture.class);
			break;
		case "javafx.scene.text.FontSmoothingType" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.text.FontSmoothingType.class);
			break;
		case "javafx.scene.text.FontWeight" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.text.FontWeight.class);
			break;
		case "javafx.scene.text.TextAlignment" : //$NON-NLS-1$
			converter = new EnumConverter<>(javafx.scene.text.TextAlignment.class);
			break;

		default :
			//
			// Enum types that are not in the javafx-ui-common source tree.
			//
			// Because the parser doesn't explicitly know about these enums
			// outside of the javafx-ui-common package, I don't expect these
			// EnumConverters to have been persisted. For example, the
			// -fx-text-overrun and -fx-content-display properties, will yield
			// a ParsedValue<String,String> with a null converter.
			//
			// If assertions are disabled, then null is returned. The StyleHelper
			// code will use the StyleableProperty's converter in this case.
			//
			assert false : "EnumConverter<"+ ename + "> not expected"; //$NON-NLS-1$ //$NON-NLS-2$

		break;
		}

		return converter;
	}


	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other == null || !(other instanceof EnumConverter)) return false;
		return (this.enumClass.equals(((EnumConverter)other).enumClass));
	}

	@Override
	public int hashCode() {
		return this.enumClass.hashCode();
	}

	@Override
	public String toString() {
		return "EnumConveter[" + this.enumClass.getName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
