/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Immutable point 2D.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class ImmutablePoint2D implements UnmodifiablePoint2D<ImmutablePoint2D, ImmutableVector2D> {

	private static final long serialVersionUID = 2208831768117217178L;

	private final double x;
	
	private final double y;
	
	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutablePoint2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public ImmutablePoint2D(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public ImmutableGeomFactory getGeomFactory() {
		return ImmutableGeomFactory.SINGLETON;
	}

	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			Tuple2D<?> tuple = (Tuple2D<?>) object;
			return tuple.getX() == getX() && tuple.getY() == getY();
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x
				+";" //$NON-NLS-1$
				+this.y
				+")"; //$NON-NLS-1$
	}
	
	@Pure
	@Override
	public ImmutablePoint2D clone() {
		try {
			return (ImmutablePoint2D) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
	@Override
	public int ix() {
		return (int) this.x;
	}

	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	@Pure
	@Override
	public int iy() {
		return (int) this.y;
	}

}
