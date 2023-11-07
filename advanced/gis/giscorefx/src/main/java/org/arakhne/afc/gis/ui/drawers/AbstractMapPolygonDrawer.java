/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import javafx.scene.shape.FillRule;

import org.arakhne.afc.gis.mapelement.MapPolygon;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.d.PathElement2d;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Abstract drawer of a map polygon.
 *
 * @param <T> the type of the polygons.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public abstract class AbstractMapPolygonDrawer<T extends MapPolygon> extends AbstractGISEditableDrawer<T> {

	/** Draw the polygon path.
	 *
	 * <p>Caution: this function calls {@link ZoomableGraphicsContext#beginPath()}.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	protected void definePath(ZoomableGraphicsContext gc, T element) {
		gc.beginPath();
		final PathIterator2afp<PathElement2d> pathIterator = element.toPath2D().getPathIterator();
		switch (pathIterator.getWindingRule()) {
		case EVEN_ODD:
			gc.setFillRule(FillRule.EVEN_ODD);
			break;
		case NON_ZERO:
			gc.setFillRule(FillRule.NON_ZERO);
			break;
		default:
			throw new IllegalStateException();
		}
		while (pathIterator.hasNext()) {
			final PathElement2d pelement = pathIterator.next();
			switch (pelement.getType()) {
			case LINE_TO:
				gc.lineTo(pelement.getToX(), pelement.getToY());
				break;
			case MOVE_TO:
				gc.moveTo(pelement.getToX(), pelement.getToY());
				break;
			case CLOSE:
				gc.closePath();
				break;
			case CURVE_TO:
				gc.bezierCurveTo(
						pelement.getCtrlX1(), pelement.getCtrlY1(),
						pelement.getCtrlX2(), pelement.getCtrlY2(),
						pelement.getToX(), pelement.getToY());
				break;
			case QUAD_TO:
				gc.quadraticCurveTo(
						pelement.getCtrlX1(), pelement.getCtrlY1(),
						pelement.getToX(), pelement.getToY());
				break;
			case ARC_TO:
				//TODO: implements arcTo
				gc.lineTo(pelement.getToX(), pelement.getToY());
				break;
			default:
				break;
			}
		}
	}

}
