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

package org.arakhne.afc.gis.ui.drawers;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.FlagContainer;
import org.arakhne.afc.gis.primitive.GISEditable;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.vmutil.ColorNames;

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

	private static final int SELECTION_COLOR = ColorNames.getColorFromName("lightcoral"); //$NON-NLS-1$

	/** Replies the given element is selected.
	 *
	 * @param element the element.
	 * @return {@code true} if the element is selected.
	 */
	@Pure
	protected boolean isSelected(T element) {
		return element.hasFlag(FlagContainer.FLAG_SELECTED);
	}

	/** Replies the color that should be used for drawing the given element.
	 *
	 * @param element the element.
	 * @return the drawing color.
	 */
	@Pure
	protected int getDrawingColor(T element) {
		return getDrawingColor(element.getColor(), isSelected(element));
	}

	/** Adapt the given color in order to be used for displaying.
	 *
	 * @param color the color to adapt.
	 * @param isSelected indicates if the color should be adapted for selection.
	 * @return the drawing color.
	 */
	@Pure
	@SuppressWarnings({ "static-method" })
	protected int getDrawingColor(int color, boolean isSelected) {
		if (isSelected) {
			return SELECTION_COLOR;
		}
		return color;
	}

}
