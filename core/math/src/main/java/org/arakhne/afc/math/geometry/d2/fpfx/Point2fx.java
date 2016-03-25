/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.fpfx;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;

import javafx.beans.property.DoubleProperty;

/** 2D Point with 2 double precision floating-point FX properties.
 * 
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2fx extends Tuple2fx<Point2D, Point2fx> implements Point2D {

	private static final long serialVersionUID = 2543935341943572211L;

	/**
	 */
	public Point2fx() {
		//
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fx(DoubleProperty x, DoubleProperty y) {
		super(x, y);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2fx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fx(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fx(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fx(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2fx(long x, long y) {
		super(x,y);
	}

	@Override
	public Point2D toUnmodifiable() {
		return new UnmodifiablePoint2D() {
			
			private static final long serialVersionUID = 5419032367247268556L;

			@Override
			public Point2D clone() {
				return Point2fx.this.toUnmodifiable();
			}
			
			@Override
			public int iy() {
				return Point2fx.this.iy();
			}
			
			@Override
			public int ix() {
				return Point2fx.this.ix();
			}
			
			@Override
			public double getY() {
				return Point2fx.this.getY();
			}
			
			@Override
			public double getX() {
				return Point2fx.this.getX();
			}

		};
	}

}