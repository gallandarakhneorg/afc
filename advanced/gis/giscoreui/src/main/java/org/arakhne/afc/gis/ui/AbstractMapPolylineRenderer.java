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

import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.RectangularShape2afp;
import org.arakhne.afc.math.geometry.d2.d.PathElement2d;

/** Abstract renderer of a map polyline.
 *
 * @param <T> the type of the polylines.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractMapPolylineRenderer<T extends MapPolyline> extends AbstractGisElementRenderer<T> {

	/** Create the polyline path.
	 *
	 * @param element the map element.
	 * @param bounds the gis area bounds.
	 * @return the JavaFX path.
	 */
	@Pure
	protected Path createPath(T element, RectangularShape2afp<?, ?, ?, ?, ?, ?> bounds) {
		final PathIterator2afp<PathElement2d> pathIterator = element.toPath2D().getPathIterator();
		final Path fxPath = new Path();
		switch (pathIterator.getWindingRule()) {
		case EVEN_ODD:
			fxPath.setFillRule(FillRule.EVEN_ODD);
			break;
		case NON_ZERO:
			fxPath.setFillRule(FillRule.NON_ZERO);
			break;
		default:
			throw new IllegalStateException();
		}
		while (pathIterator.hasNext()) {
			final PathElement2d pelement = pathIterator.next();
			final PathElement pelt;
			switch (pelement.getType()) {
			case LINE_TO:
				pelt = new LineTo(
						gis2screenx(pelement.getToX(), bounds),
						gis2screeny(pelement.getToY(), bounds));
				break;
			case MOVE_TO:
				pelt = new MoveTo(
						gis2screenx(pelement.getToX(), bounds),
						gis2screeny(pelement.getToY(), bounds));
				break;
			case CLOSE:
				pelt = new ClosePath();
				break;
			case CURVE_TO:
				pelt = new CubicCurveTo(
						gis2screenx(pelement.getCtrlX1(), bounds),
						gis2screeny(pelement.getCtrlY1(), bounds),
						gis2screenx(pelement.getCtrlX2(), bounds),
						gis2screeny(pelement.getCtrlY2(), bounds),
						gis2screenx(pelement.getToX(), bounds),
						gis2screeny(pelement.getToY(), bounds));
				break;
			case QUAD_TO:
				pelt = new QuadCurveTo(
						gis2screenx(pelement.getCtrlX1(), bounds),
						gis2screeny(pelement.getCtrlY1(), bounds),
						gis2screenx(pelement.getToX(), bounds),
						gis2screeny(pelement.getToY(), bounds));
				break;
			case ARC_TO:
				pelt = new ArcTo(
						pelement.getRadiusX(),
						pelement.getRadiusY(),
						pelement.getRotationX(),
						gis2screenx(pelement.getToX(), bounds),
						gis2screeny(pelement.getToY(), bounds),
						pelement.getLargeArcFlag(),
						pelement.getSweepFlag());
				break;
			default:
				pelt = null;
				break;
			}
			if (pelt != null) {
				fxPath.getElements().add(pelt);
			}
		}
		return fxPath;
	}

}
