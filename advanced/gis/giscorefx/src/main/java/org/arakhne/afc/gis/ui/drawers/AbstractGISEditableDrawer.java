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

package org.arakhne.afc.gis.ui.drawers;

import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.nodefx.Drawer;

/** Abstract drawer of a GIS editable.
 *
 * @param <T> the type of the polylines.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public abstract class AbstractGISEditableDrawer<T extends GISEditable & FlagContainer>  implements Drawer<T> {

	private static final int SELECTION_COLOR_RED = 0xFF;

	private static final int SELECTION_COLOR_GREEN = 0x00;

	private static final int SELECTION_COLOR_BLUE = 0x00;

	private static final double SELECTION_COLOR_INTERPOLATION = .5;

	/** Replies the color that should be used for drawing the given element.
	 *
	 * @param element the element.
	 * @return the drawing color.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	protected int getDrawingColor(T element) {
		int color = element.getColor();
		if (element.hasFlag(FlagContainer.FLAG_SELECTED)) {
			// Interpolate to the selection color
			int red = (0xFF0000 & color) >> 16;
			int green = (0x00FF00 & color) >> 8;
			int blue = 0x0000FF & color;
			red += (red - SELECTION_COLOR_RED) * SELECTION_COLOR_INTERPOLATION;
			green += (green - SELECTION_COLOR_GREEN) * SELECTION_COLOR_INTERPOLATION;
			blue += (blue - SELECTION_COLOR_BLUE) * SELECTION_COLOR_INTERPOLATION;
			color = 0xFF000000 | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
		}
		return color;
	}

}
