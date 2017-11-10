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

import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapMultiPoint;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

/** Abstract renderer of a map multi-point.
 *
 * @param <T> the type of the rendered elements.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractMapMultiPointRenderer<T extends MapMultiPoint> extends AbstractGisElementRenderer<T> {

	/** Create the point path.
	 *
	 * @param element the map element.
	 * @param bounds the gis area bounds.
	 * @return the JavaFX path.
	 */
	@Pure
	protected Path[] createPaths(T element, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final Path path1 = new Path();
		final Path path2;
		if (element.isDoubleFramed()) {
			path2 = new Path();
		} else {
			path2 = null;
		}
		final double ptsSize = element.getPointSize() / 2.;
		for (final Point2d pts : element.points()) {
			double x = pts.getX() - ptsSize;
			double y = pts.getY() - ptsSize;
			double mx = pts.getX() + ptsSize;
			double my = pts.getY() + ptsSize;
			path1.getElements().add(new MoveTo(x, y));
			path1.getElements().add(new LineTo(mx, y));
			path1.getElements().add(new LineTo(mx, my));
			path1.getElements().add(new LineTo(x, my));
			path1.getElements().add(new ClosePath());
			if (path2 != null) {
				x -= ptsSize;
				y -= ptsSize;
				mx += ptsSize;
				my += ptsSize;
				path2.getElements().add(new MoveTo(x, y));
				path2.getElements().add(new LineTo(mx, y));
				path2.getElements().add(new LineTo(mx, my));
				path2.getElements().add(new LineTo(x, my));
				path2.getElements().add(new ClosePath());
			}
		}
		if (path2 != null) {
			return new Path[] {path1, path2};
		}
		return new Path[] {path1};
	}

}
