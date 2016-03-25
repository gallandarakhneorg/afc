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

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;

/** 2D Vector with 2 integer FX properties.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2ifx extends Tuple2ifx<Vector2D, Vector2ifx> implements Vector2D {

	private static final long serialVersionUID = 5782200591782721145L;

	/**
	 */
	public Vector2ifx() {
		//
	}

	/**
	 * @param xProperty
	 * @param yProperty
	 */
	public Vector2ifx(IntegerProperty xProperty, IntegerProperty yProperty) {
		super(xProperty, yProperty);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(long x, long y) {
		super(x,y);
	}

	@Pure
	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2D() {

			private static final long serialVersionUID = -3525974627723161583L;

			@Override
			public Vector2D clone() {
				return Vector2ifx.this.toUnmodifiable();
			}

			@Override
			public double getX() {
				return Vector2ifx.this.getX();
			}

			@Override
			public int ix() {
				return Vector2ifx.this.ix();
			}

			@Override
			public double getY() {
				return Vector2ifx.this.getY();
			}

			@Override
			public int iy() {
				return Vector2ifx.this.iy();
			}

		};
	}

}