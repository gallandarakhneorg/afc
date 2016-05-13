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
package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;

/** 2D Point with 2 integer FX properties.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point2ifx extends Tuple2ifx<Point2ifx> implements Point2D<Point2ifx, Vector2ifx> {

	private static final long serialVersionUID = -1421615416984636660L;

	/**
	 */
	public Point2ifx() {
		//
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2ifx(IntegerProperty x, IntegerProperty y) {
		super(x, y);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point2ifx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2ifx(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2ifx(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2ifx(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point2ifx(long x, long y) {
		super(x,y);
	}
	
	@Override
	public GeomFactory2ifx getGeomFactory() {
		return GeomFactory2ifx.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint2D<Point2ifx, Vector2ifx> toUnmodifiable() {
		return new UnmodifiablePoint2D<Point2ifx, Vector2ifx>() {

			private static final long serialVersionUID = -8264299960804312720L;

			@Override
			public GeomFactory<Vector2ifx, Point2ifx> getGeomFactory() {
				return Point2ifx.this.getGeomFactory();
			}
			
			@Override
			public Point2ifx clone() {
				return Point2ifx.this.getGeomFactory().newPoint(
						Point2ifx.this.ix(),
						Point2ifx.this.iy());
			}

			@Override
			public double getX() {
				return Point2ifx.this.getX();
			}

			@Override
			public int ix() {
				return Point2ifx.this.ix();
			}

			@Override
			public double getY() {
				return Point2ifx.this.getY();
			}

			@Override
			public int iy() {
				return Point2ifx.this.iy();
			}
			
		};
	}

}
