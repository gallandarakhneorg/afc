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

import org.arakhne.afc.gis.mapelement.MapPoint;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Abstract drawer of a map point.
 *
 * @param <T> the type of the map points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public abstract class AbstractMapPointDrawer<T extends MapPoint> extends AbstractGISEditableDrawer<T> {

	/** Define a path that corresponds to the small rectangle around a point.
	 *
	 * <p>Caution: this function does not call {@link ZoomableGraphicsContext#beginPath()}.
	 * You must call it yourself before the first call to this function.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	protected void defineSmallRectangle(ZoomableGraphicsContext gc, T element) {
		final double ptsSize = element.getPointSize() / 2.;
		final double x = element.getX() - ptsSize;
		final double y = element.getY() - ptsSize;
		final double mx = element.getX() + ptsSize;
		final double my = element.getY() + ptsSize;
		gc.moveTo(x, y);
		gc.lineTo(mx, y);
		gc.lineTo(mx, my);
		gc.lineTo(x, my);
		gc.closePath();
	}

	/** Define a path that corresponds to the big rectangle around a point.
	 *
	 * <p>Caution: this function does not call {@link ZoomableGraphicsContext#beginPath()}.
	 * You must call it yourself before the first call to this function.
	 *
	 * @param gc the graphics context that must be used for drawing.
	 * @param element the map element.
	 */
	protected void defineBigRectangle(ZoomableGraphicsContext gc, T element) {
		final double ptsSize = element.getPointSize();
		final double x = element.getX() - ptsSize;
		final double y = element.getY() - ptsSize;
		final double mx = element.getX() + ptsSize;
		final double my = element.getY() + ptsSize;
		gc.moveTo(x, y);
		gc.lineTo(mx, y);
		gc.lineTo(mx, my);
		gc.lineTo(x, my);
		gc.closePath();
	}

}
