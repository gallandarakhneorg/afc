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

import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Abstract drawer of a map multi-point.
 *
 * @param <T> the type of the map points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public abstract class AbstractMapMultiPointDrawer<T extends MapMultiPoint> extends AbstractGISEditableDrawer<T> {

	/** Define a path that corresponds to the small rectangles around the points.
	 *
	 * <p>Caution: this function does not call {@link ZoomableGraphicsContext#beginPath()}.
	 * You must call it yourself before the first call to this function.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	protected void defineSmallRectangles(ZoomableGraphicsContext gc, T element) {
		final double ptsSize = element.getPointSize() / 2.;
		final Rectangle2afp<?, ?, ?, ?, ?, ?> visibleArea = gc.getVisibleArea();
		for (final Point2d point : element.points()) {
			if (visibleArea.contains(point)) {
				final double x = point.getX() - ptsSize;
				final double y = point.getY() - ptsSize;
				final double mx = point.getX() + ptsSize;
				final double my = point.getY() + ptsSize;
				gc.moveTo(x, y);
				gc.lineTo(mx, y);
				gc.lineTo(mx, my);
				gc.lineTo(x, my);
				gc.closePath();
			}
		}
	}

	/** Define a path that corresponds to the big rectangles around a points.
	 *
	 * <p>Caution: this function does not call {@link ZoomableGraphicsContext#beginPath()}.
	 * You must call it yourself before the first call to this function.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	protected void defineBigRectangles(ZoomableGraphicsContext gc, T element) {
		final double ptsSize = element.getPointSize();
		final Rectangle2afp<?, ?, ?, ?, ?, ?> visibleArea = gc.getVisibleArea();
		for (final Point2d point : element.points()) {
			if (visibleArea.contains(point)) {
				final double x = point.getX() - ptsSize;
				final double y = point.getY() - ptsSize;
				final double mx = point.getX() + ptsSize;
				final double my = point.getY() + ptsSize;
				gc.moveTo(x, y);
				gc.lineTo(mx, y);
				gc.lineTo(mx, my);
				gc.lineTo(x, my);
				gc.closePath();
			}
		}
	}

}
