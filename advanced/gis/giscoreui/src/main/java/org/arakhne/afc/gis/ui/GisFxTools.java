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

package org.arakhne.afc.gis.ui;

import javafx.scene.paint.Color;
import org.eclipse.xtext.xbase.lib.Pure;

/** JavaFX Tools for GIS.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GisFxTools {

	private GisFxTools() {
		//
	}

	/** Parse the given RGB color.
	 *
	 * @param color the RGB color.
	 * @return the JavaFX color.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	@Pure
	public static Color rgb(int color) {
		final int red = (color & 0x00FF0000) >> 16;
		final int green = (color & 0x0000FF00) >> 8;
		final int blue = color & 0x000000FF;
		return Color.rgb(red, green, blue);
	}

	/** Parse the given RGBA color.
	 *
	 * @param color the RGB color.
	 * @return the JavaFX color.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	@Pure
	public static Color rgba(int color) {
		final int alpha = (color & 0xFF000000) >> 24;
		final int red = (color & 0x00FF0000) >> 16;
		final int green = (color & 0x0000FF00) >> 8;
		final int blue = color & 0x000000FF;
		final double opacity;
		switch (alpha) {
		case 0:
			opacity = 0.;
			break;
		case 255:
			opacity = 1.;
			break;
		default:
			opacity = ((double) alpha) / 255;
		}
		return Color.rgb(red, green, blue, opacity);
	}

}
