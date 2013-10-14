/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.ui.vector.android;

import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.continous.object2d.Circle2f;
import org.arakhne.afc.math.continous.object2d.Ellipse2f;
import org.arakhne.afc.math.continous.object2d.Path2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f.ClosePathElement2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f.LinePathElement2f;
import org.arakhne.afc.math.continous.object2d.PathElement2f.MovePathElement2f;
import org.arakhne.afc.math.continous.object2d.PathIterator2f;
import org.arakhne.afc.math.continous.object2d.PathShadow2f;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Rectangle2f;
import org.arakhne.afc.math.continous.object2d.Segment2f;
import org.arakhne.afc.math.continous.object2d.Shape2f;
import org.arakhne.afc.math.generic.Path2D;
import org.arakhne.afc.math.generic.PathWindingRule;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.matrix.Transform2D;

import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PathMeasure;
import android.graphics.RectF;

/** Android implementation of the Path2f.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AndroidPath implements Shape2f, Path2D<Rectangle2f,PathElement2f,PathIterator2f>, NativeWrapper {

	private static final long serialVersionUID = 3395487582331474537L;

	private final Path path;

	/**
	 * @param path
	 */
	public AndroidPath(Path path) {
		this.path = path;
		if (this.path.isInverseFillType()) {
			this.path.toggleInverseFillType();
		}
	}

	@Override
	public AndroidPath clone() {
		try {
			return (AndroidPath)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/** Replies the native path.
	 * 
	 * @return the native path.
	 */
	public Path getPath() {
		return this.path;
	}

	@Override
	public void clear() {
		this.path.reset();
	}

	@Override
	public Point2D getClosestPointTo(Point2D p) {
		return Path2f.getClosestPointTo(getPathIterator(), p.getX(), p.getY());
	}

	@Override
	public PathWindingRule getWindingRule() {
		switch(this.path.getFillType()) {
		case EVEN_ODD:
		case INVERSE_EVEN_ODD:
			return PathWindingRule.EVEN_ODD;
		case WINDING:
		case INVERSE_WINDING:
		default:
			return PathWindingRule.NON_ZERO;
		}
	}

	@Override
	public boolean isEmpty() {
		return this.path.isEmpty();
	}

	@Override
	public <T> T getNativeObject(Class<T> type) {
		return type.cast(this.path);
	}

	@Override
	public Rectangle2f toBoundingBox() {
		RectF r = new RectF();
		this.path.computeBounds(r, true);
		return new Rectangle2f(r.left, r.top, r.width(), r.height());
	}

	@Override
	public void toBoundingBox(Rectangle2f box) {
		RectF r = new RectF();
		this.path.computeBounds(r, true);
		box.setFromCorners(r.left, r.top, r.right, r.bottom);
	}

	@Override
	public float distance(Point2D p) {
		return (float)Math.sqrt(distanceSquared(p));
	}

	@Override
	public float distanceSquared(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.distanceSquared(p);
	}

	@Override
	public float distanceL1(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.distanceL1(p);
	}

	@Override
	public float distanceLinf(Point2D p) {
		Point2D c = getClosestPointTo(p);
		return c.distanceLinf(p);
	}

	@Override
	public void translate(float dx, float dy) {
		this.path.offset(dx, dy);
	}

	@Override
	public Shape2f createTransformedShape(Transform2D transform) {
		return new Path2f(getPathIterator(transform));
	}

	@Override
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	@Override
	public boolean contains(float x, float y) {
		return Path2f.contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), x, y);
	}

	@Override
	public boolean contains(Rectangle2f r) {
		return Path2f.contains(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				r.getMinX(), r.getMinY(),
				r.getWidth(), r.getHeight());
	}

	@Override
	public boolean intersects(Rectangle2f s) {
		return Path2f.intersects(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getMinX(), s.getMinY(),
				s.getWidth(), s.getHeight());
	}

	@Override
	public boolean intersects(Ellipse2f s) {
		FillType type = this.path.getFillType();
		int mask = (type == FillType.WINDING || type == FillType.INVERSE_WINDING ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromEllipse(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getMinX(), s.getMinY(), s.getWidth(), s.getHeight(),
				false, true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(Circle2f s) {
		FillType type = this.path.getFillType();
		int mask = (type == FillType.WINDING || type == FillType.INVERSE_WINDING ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromCircle(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getX(), s.getY(), s.getRadius(),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(Segment2f s) {
		FillType type = this.path.getFillType();
		int mask = (type == FillType.WINDING || type == FillType.INVERSE_WINDING ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromSegment(
				0,
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				s.getX1(), s.getY1(), s.getX2(), s.getY2(),
				false);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(Path2f s) {
		FillType type = this.path.getFillType();
		int mask = (type == FillType.WINDING || type == FillType.INVERSE_WINDING ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromPath(
				s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				new PathShadow2f(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}

	@Override
	public boolean intersects(PathIterator2f s) {
		FillType type = this.path.getFillType();
		int mask = (type == FillType.WINDING || type == FillType.INVERSE_WINDING ? -1 : 2);
		int crossings = Path2f.computeCrossingsFromPath(
				s,
				new PathShadow2f(this),
				false,
				true);
		return (crossings == MathConstants.SHAPE_INTERSECTS ||
				(crossings & mask) != 0);
	}	

	@Override
	public PathIterator2f getPathIterator(float flatness) {
		return new FlatteningPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO, null);
	}

	@Override
	public PathIterator2f getPathIterator(Transform2D transform) {
		return new FlatteningPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO, transform);
	}

	@Override
	public PathIterator2f getPathIterator() {
		return getPathIterator(null);
	}

	/**
	 * Iterator that is replies the points of a flatenning path. 
	 *  
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class FlatteningPathIterator implements PathIterator2f {

		private final PathMeasure measure;
		private final float flatness;
		private final Transform2D transform;

		private final float[] tmpCoords = new float[2];
		private final Point2f tmpPoint = new Point2f();

		private float firstX, firstY;
		private float dist = 0;
		private float length;
		private PathElement2f next = null;
		private boolean finished = false;

		/**
		 * @param flatness
		 * @param transform
		 */
		public FlatteningPathIterator(float flatness, Transform2D transform) {
			this.transform = (transform==null || transform.isIdentity()) ? null : transform;
			this.flatness = flatness;
			this.measure = new PathMeasure(AndroidPath.this.getPath(), false);
			this.length = this.measure.getLength();
			this.firstX = this.firstY = Float.NaN;
			searchNext();
		}
		
		private void transform() {
			assert(this.tmpCoords!=null && this.tmpCoords.length==2);
			if (this.transform!=null) {
				this.tmpPoint.set(this.tmpCoords);
				this.transform.transform(this.tmpPoint);
				this.tmpPoint.get(this.tmpCoords);
			}
		}

		private void searchNext() {
			PathElement2f prev = this.next;
			this.next = null;
			do {
				if ((this.dist<=this.length) &&
					(this.measure.getPosTan(this.dist, this.tmpCoords, null))) {
					transform();
					if (prev==null) {
						this.firstX = this.tmpCoords[0];
						this.firstY = this.tmpCoords[1];
						this.next = new MovePathElement2f(this.firstX, this.firstY);
					}
					else {
						this.next = new LinePathElement2f(
								prev.toX, prev.toY,
								this.tmpCoords[0], this.tmpCoords[1]);
					}
					this.dist += this.flatness;
				}
				if (this.next==null) {
					if (prev!=null && this.measure.isClosed()) {
						this.next = new ClosePathElement2f(
								prev.toX, prev.toY,
								this.firstX, this.firstY);
					}
					else if (this.measure.nextContour()) {
						prev = null;
						this.dist = 0;
						this.length = this.measure.getLength();
						this.firstX = this.firstY = Float.NaN;
					}
					else {
						this.finished = true;
					}
				}
			}
			while (this.next==null && !this.finished);
		}

		@Override
		public boolean hasNext() {
			return this.next!=null;
		}

		@Override
		public PathElement2f next() {
			PathElement2f n = this.next;
			if (n==null) throw new NoSuchElementException();
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return AndroidPath.this.getWindingRule();
		}

	} // class CopyIterator

}