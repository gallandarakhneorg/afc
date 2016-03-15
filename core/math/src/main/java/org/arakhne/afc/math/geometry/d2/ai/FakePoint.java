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
package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** A fake implementation of a point that serves as temporary container for computations.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
final class FakePoint implements Point2D {

	private static final long serialVersionUID = -4264989069101518368L;

	private int x;
	
	private int y;
	
	/** Construct a point that is initialized to zero.
	 */
	public FakePoint() {
		//
	}
	
	/** Construct a point.
	 * 
	 * @param x the x value of the point.
	 * @param y the y value of the point.
	 */
	public FakePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public FakePoint clone() {
		try {
			return (FakePoint) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = (int) x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = (int) y;
	}

	@Override
	public Point2D toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	/** Internal usage factory.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("rawtypes")
	static class FakeGeomFactory implements GeomFactory2ai {

		/** Singleton of this internal factory.
		 */
		static final FakeGeomFactory SINGLETON = new FakeGeomFactory();
		
		@Override
		public FakePoint convertToPoint(Point2D p) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector2D convertToVector(Point2D p) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Point2D convertToPoint(Vector2D v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector2D convertToVector(Vector2D v) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Point2D newPoint() {
			return new FakePoint();
		}

		@Override
		public Point2D newPoint(int x, int y) {
			return new FakePoint(x, y);
		}

		@Override
		public Vector2D newVector() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector2D newVector(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Path2ai newPath(PathWindingRule rule) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Rectangle2ai newBox() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathElement2ai newMovePathElement(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathElement2ai newLinePathElement(int startX, int startY, int targetX, int targetY) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathElement2ai newClosePathElement(int lastPointX, int lastPointy, int firstPointX, int firstPointY) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathElement2ai newCurvePathElement(int startX, int startY, int controlX, int controlY, int targetX,
				int targetY) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathElement2ai newCurvePathElement(int startX, int startY, int controlX1, int controlY1, int controlX2,
				int controlY2, int targetX, int targetY) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Segment2ai newSegment(int x1, int y1, int x2, int y2) {
			throw new UnsupportedOperationException();
		}
		
	}

}