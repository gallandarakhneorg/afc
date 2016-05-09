/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.afc.math.geometry.d2.dfx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;

/** Ellipse with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Ellipse2dfx extends AbstractRectangularShape2dfx<Ellipse2dfx>
	implements Ellipse2afp<Shape2dfx<?>, Ellipse2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	private static final long serialVersionUID = -4493000182971183282L;

	/**
	 */
	public Ellipse2dfx() {
		//		
	}
	/**
	 * @param min is the min corner of the ellipse.
	 * @param max is the max corner of the ellipse.
	 */
	public Ellipse2dfx(Point2D<?, ?> min, Point2D<?, ?> max) {
		assert (min != null) : "Minimum point must be not null"; //$NON-NLS-1$
		assert (max != null) : "Maximum point must be not null"; //$NON-NLS-1$
		setFromCorners(min.getX(), min.getY(), max.getX(), max.getY());
	}

	/**
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Ellipse2dfx(double x, double y, double width, double height) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		setFromCorners(x, y, x + width, y + height);
	}

	/**
	 * @param e
	 */
	public Ellipse2dfx(Ellipse2afp<?, ?, ?, ?, ?, ?> e) {
		super(e);
	}

}
