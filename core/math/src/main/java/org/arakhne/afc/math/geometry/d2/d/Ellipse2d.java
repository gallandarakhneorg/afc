/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;

/** An ellipse with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Ellipse2d extends AbstractRectangularShape2d<Ellipse2d>
		implements Ellipse2afp<Shape2d<?>, Ellipse2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -4493000182971183282L;

	/** Construct an empty ellipse.
	 */
	public Ellipse2d() {
		//
	}

	/** Construct an ellipse with the given corners of its bouding box.
	 * @param min is the min corner of the ellipse.
	 * @param max is the max corner of the ellipse.
	 */
	public Ellipse2d(Point2D<?, ?> min, Point2D<?, ?> max) {
		assert min != null : "Minimum point must be not null"; 
		assert max != null : "Maximum point must be not null"; 
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/** Construct an ellipse with the given minimum corner and sizes of its bouding box.
	 * @param x x coordinate of the minium corner of the ellipse's bounding box.
	 * @param y y coordinate of the minium corner of the ellipse's bounding box.
	 * @param width width of the ellipse's bounding box.
	 * @param height height of the ellipse's bounding box.
	 */
	public Ellipse2d(double x, double y, double width, double height) {
		assert width >= 0. : "Width must be positive or zero"; 
		assert height >= 0. : "Height must be positive or zero"; 
		setFromCorners(x, y, x + width, y + height);
	}

	/** Constructor by copy.
	 * @param ellipse the ellipse to copy.
	 */
	public Ellipse2d(Ellipse2afp<?, ?, ?, ?, ?, ?> ellipse) {
		super(ellipse);
	}

}
