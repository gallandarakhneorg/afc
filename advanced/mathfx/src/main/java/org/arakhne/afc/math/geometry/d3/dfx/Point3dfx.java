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
package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiablePoint3D;

import javafx.beans.property.DoubleProperty;

/** 2D Point with 2 double precision floating-point FX properties.
 * 
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Point3dfx extends Tuple3dfx<Point3dfx> implements Point3D<Point3dfx, Vector3dfx> {

	private static final long serialVersionUID = 2543935341943572211L;

	/**
	 */
	public Point3dfx() {
		//
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3dfx(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		super(x, y, z);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3dfx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3dfx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Point3dfx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3dfx(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3dfx(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3dfx(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Point3dfx(long x, long y, long z) {
		super(x,y,z);
	}
	
	@Override
	public GeomFactory3dfx getGeomFactory() {
		return GeomFactory3dfx.SINGLETON;
	}

	@Override
	public UnmodifiablePoint3D<Point3dfx, Vector3dfx> toUnmodifiable() {
		return new UnmodifiablePoint3D<Point3dfx, Vector3dfx>() {
			
			private static final long serialVersionUID = 5419032367247268556L;

			@Override
			public GeomFactory3D<Vector3dfx, Point3dfx> getGeomFactory() {
				return Point3dfx.this.getGeomFactory();
			}
			
			@Override
			public Point3dfx clone() {
				return Point3dfx.this.getGeomFactory().newPoint(
						Point3dfx.this.getX(),
						Point3dfx.this.getY(),
						Point3dfx.this.getZ());
			}
			
			@Override
			public int iy() {
				return Point3dfx.this.iy();
			}
			
			@Override
			public int ix() {
				return Point3dfx.this.ix();
			}
			
			@Override
			public int iz() {
				return Point3dfx.this.iz();
			}
			
			@Override
			public double getY() {
				return Point3dfx.this.getY();
			}
			
			@Override
			public double getX() {
				return Point3dfx.this.getX();
			}
			
			@Override
			public double getZ() {
				return Point3dfx.this.getZ();
			}

		};
	}

}