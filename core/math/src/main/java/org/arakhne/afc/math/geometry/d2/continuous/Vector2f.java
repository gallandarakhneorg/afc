/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.FunctionalVector2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D Vector with 2 floating-point values.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Vector2f extends Tuple2f<Vector2D> implements FunctionalVector2D {

	private static final long serialVersionUID = -2062941509400877679L;

	/**
	 */
	public Vector2f() {
		//
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2f(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2f(long x, long y) {
		super(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f clone() {
		return (Vector2f)super.clone();
	}

	@Override
	public Vector2D toUnmodifiable() {
		return new UnmodifiableVector2f();
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class UnmodifiableVector2f implements FunctionalVector2D.UnmodifiableVector2f {

		private static final long serialVersionUID = -8670105082548919880L;

		/**
		 */
		public UnmodifiableVector2f() {
			//
		}

		@Override
		public UnmodifiableVector2f clone() {
			return new UnmodifiableVector2f();
		}

		@Override
		public void get(Vector2D t) {
			Vector2f.this.get(t);
		}

		@Override
		public void get(int[] t) {
			Vector2f.this.get(t);
		}

		@Override
		public void get(double[] t) {
			Vector2f.this.get(t);
		}

		@Override
		public double getX() {
			return Vector2f.this.getX();
		}

		@Override
		public int ix() {
			return Vector2f.this.ix();
		}

		@Override
		public double getY() {
			return Vector2f.this.getY();
		}

		@Override
		public int iy() {
			return Vector2f.this.iy();
		}

		@Override
		public boolean equals(Tuple2D<?> t1) {
			return Vector2f.this.equals(t1);
		}

		@Override
		public int hashCode() {
			return Vector2f.this.hashCode();
		}

		@Override
		public boolean epsilonEquals(Vector2D t1, double epsilon) {
			return Vector2f.this.epsilonEquals(t1, epsilon);
		}

		@Override
		public double dot(Vector2D v1) {
			return Vector2f.this.dot(v1);
		}

		@Override
		public double length() {
			return Vector2f.this.length();
		}

		@Override
		public double lengthSquared() {
			return Vector2f.this.lengthSquared();
		}

		@Override
		public double angle(Vector2D v1) {
			return Vector2f.this.angle(v1);
		}

		@Override
		public double signedAngle(Vector2D v) {
			return Vector2f.this.signedAngle(v);
		}

		@Override
		public double getOrientationAngle() {
			return Vector2f.this.getOrientationAngle();
		}

		@Override
		public boolean isUnitVector() {
			return Vector2f.this.isUnitVector();
		}

		@Override
		public Vector2D toUnmodifiable() {
			return this;
		}

		@Override
		public double perp(Vector2D x2) {
			return Vector2f.this.perp(x2);
		}

	}

}