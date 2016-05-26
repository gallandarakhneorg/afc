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
package org.arakhne.afc.math.geometry.d3.ifx;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;

/** 3D Point with 3 integer FX properties.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3ifx extends Tuple3ifx<Point3ifx> implements Point3D<Point3ifx, Vector3ifx> {

	private static final long serialVersionUID = -1421615416984636660L;

	/**
	 */
	public Point3ifx() {
		//
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3ifx(IntegerProperty x, IntegerProperty y, IntegerProperty z) {
		super(x, y, z);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3ifx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3ifx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3ifx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3ifx(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3ifx(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Point3ifx(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3ifx(long x, long y, long z) {
		super(x,y,z);
	}
	
	@Override
	public GeomFactory3ifx getGeomFactory() {
		return GeomFactory3ifx.SINGLETON;
	}

	@Pure
	@Override
	public UnmodifiablePoint3D<Point3ifx, Vector3ifx> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3ifx, Vector3ifx>() {

			private static final long serialVersionUID = -8264299960804312720L;

			@Override
			public GeomFactory3D<Vector3ifx, Point3ifx> getGeomFactory() {
				return Point3ifx.this.getGeomFactory();
			}
			
			@Override
			public Point3ifx clone() {
				return Point3ifx.this.getGeomFactory().newPoint(
						Point3ifx.this.ix(),
						Point3ifx.this.iy(),
						Point3ifx.this.iz());
			}

			@Override
			public double getX() {
				return Point3ifx.this.getX();
			}

			@Override
			public int ix() {
				return Point3ifx.this.ix();
			}
			
			@Override
			public double getY() {
				return Point3ifx.this.getY();
			}
			
			@Override
			public int iy() {
				return Point3ifx.this.iy();
			}

			@Override
			public double getZ() {
				return Point3ifx.this.getZ();
			}

			@Override
			public int iz() {
				return Point3ifx.this.iz();
			}
			
		};
	}

}
