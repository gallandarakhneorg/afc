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

import java.util.concurrent.Callable;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/** 2D Vector with 2 double precision floating-point FX properties.
 * 
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2fx extends Tuple2fx<Vector2D, Vector2fx> implements Vector2D {

	private static final long serialVersionUID = 8394433458442716159L;

	/** Property that contains the squared length.
	 */
	ReadOnlyDoubleWrapper lengthSquareProperty = new ReadOnlyDoubleWrapper();
	
	/** Property that contains the length.
	 */
	ReadOnlyDoubleWrapper lengthProperty = new ReadOnlyDoubleWrapper();

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 * 
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	public static Vector2fx toOrientationVector(double angle) {
		return new Vector2fx(Math.cos(angle), Math.sin(angle));
	}

	/**
	 */
	public Vector2fx() {
		bind();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fx(DoubleProperty x, DoubleProperty y) {
		super(x, y);
		bind();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fx(Tuple2D<?> tuple) {
		super(tuple);
		bind();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fx(int[] tuple) {
		super(tuple);
		bind();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2fx(double[] tuple) {
		super(tuple);
		bind();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fx(int x, int y) {
		super(x,y);
		bind();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fx(float x, float y) {
		super(x,y);
		bind();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fx(double x, double y) {
		super(x,y);
		bind();
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2fx(long x, long y) {
		super(x,y);
		bind();
	}
	
	private void bind() {
		this.lengthSquareProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return Vector2fx.this.x.doubleValue() * Vector2fx.this.x.doubleValue()
						+ Vector2fx.this.y.doubleValue() * Vector2fx.this.y.doubleValue();
			}
		}, this.x, this.y));
		
		this.lengthProperty.bind(Bindings.createDoubleBinding(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return Math.sqrt(Vector2fx.this.lengthSquareProperty.doubleValue());
			}
		}, this.lengthSquareProperty));
	}
	
	@Override
	public Vector2fx clone() {
		Vector2fx clone = super.clone();
		clone.lengthSquareProperty = new ReadOnlyDoubleWrapper();
		clone.lengthProperty = new ReadOnlyDoubleWrapper();
		clone.bind();
		return clone;
	}

	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2D() {
			
			private static final long serialVersionUID = 1638306005394957111L;

			@Override
			public Vector2D clone() {
				return Vector2fx.this.toUnmodifiable();
			}
			
			@Override
			public int iy() {
				return Vector2fx.this.iy();
			}
			
			@Override
			public int ix() {
				return Vector2fx.this.ix();
			}
			
			@Override
			public double getY() {
				return Vector2fx.this.getY();
			}
			
			@Override
			public double getX() {
				return Vector2fx.this.getX();
			}

		};
	}

}