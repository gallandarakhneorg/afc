/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.nodefx.drawers;

import org.arakhne.afc.math.geometry.d2.PathIterator2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.nodefx.Drawer;
import org.arakhne.afc.nodefx.ZoomableGraphicsContext;

/** Drawer of a 2D path.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public class Path2afpDrawer implements Drawer<Path2afp<?, ?, ? extends PathElement2afp, ?, ?, ?>> {

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Path2afp<?, ?, ? extends PathElement2afp, ?, ?, ?>> getPrimitiveType() {
		return (Class<? extends Path2afp<?, ?, ? extends PathElement2afp, ?, ?, ?>>) Path2afp.class;
	}

	@Override
	public void draw(ZoomableGraphicsContext gc, Path2afp<?, ?, ? extends PathElement2afp, ?, ?, ?> element) {
		final PathIterator2D<? extends PathElement2afp> iterator = element.getPathIterator();
		gc.beginPath();
		while (iterator.hasNext()) {
			final PathElement2afp pelement = iterator.next();
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
				// TODO draw arc.
				gc.lineTo(pelement.getToX(), pelement.getToY());
				break;
			default:
			}
		}
		gc.fill();
		gc.stroke();
	}

}
