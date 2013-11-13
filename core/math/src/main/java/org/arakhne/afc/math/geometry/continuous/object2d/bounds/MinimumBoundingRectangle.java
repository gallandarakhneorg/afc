/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.geometry.continuous.object2d.bounds;

import java.util.Arrays;
import java.util.Collection;

import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.AlignedBoundingBox;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;

/**
 * A minimum bounding rectangle (MBR), at least a 2d axis-aligned bounding box.
 * <p>
 * All the transformations on this bounding rectangle are
 * relative to the rectangle's center.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MinimumBoundingRectangle extends AbstractCombinableBounds2D implements AlignedCombinableBounds2D, OrientedCombinableBounds2D, TranslatableBounds2D {

	private static final long serialVersionUID = 7615135756504420528L;

	/**
	 * The lower corner of this bounding rectangle
	 */
	EuclidianPoint2D lower = new EuclidianPoint2D();

	/**
	 * The upper corner of this bounding rectangle
	 */
	EuclidianPoint2D upper = new EuclidianPoint2D();
	
	private boolean isBoundInit;
	
	/**
	 * The vertices that composes this rectangle, it is compiled when it is required not before
	 */
	private transient Vector2f[] vertices = null;

	/** Uninitialized bouding rectangle.
	 */
	public MinimumBoundingRectangle() {
		this.isBoundInit = false;
	}

	/**
	 * @param lx is the lower point of the rectangle.
	 * @param ly is the lower point of the rectangle.
	 * @param ux is the upper point of the rectangle.
	 * @param uy is the upper point of the rectangle.
	 */
	public MinimumBoundingRectangle(float lx, float ly, float ux, float uy) {
		this.lower.set(lx,ly);
		this.upper.set(ux,uy);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param lower is the lower point of the rectangle.
	 * @param upper is the upper point of the rectangle.
	 */
	public MinimumBoundingRectangle(Tuple2f<?> lower, Tuple2f<?> upper) {
		this.lower.set(lower);
		this.upper.set(upper);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public MinimumBoundingRectangle(Collection<? extends MinimumBoundingRectangle> bboxList) {
		combineBounds(false, bboxList);
		this.isBoundInit = true;
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public MinimumBoundingRectangle(CombinableBounds2D... bboxList) {
		combineBounds(false, Arrays.asList(bboxList));
		this.isBoundInit = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinimumBoundingRectangle clone() {
		MinimumBoundingRectangle clone = (MinimumBoundingRectangle)super.clone();
		clone.lower = (EuclidianPoint2D)this.lower.clone();
		clone.upper = (EuclidianPoint2D)this.upper.clone();
		return clone;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType2D getBoundType() {
		return BoundingPrimitiveType2D.ALIGNED_RECTANGLE;
	}

	/** {@inheritDoc}
	 */
	@Override
	public AlignedBoundingBox toBounds3D() {
		return toBounds3D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public AlignedBoundingBox toBounds3D(float z, float zsize) {
		return toBounds3D(z, zsize, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public AlignedBoundingBox toBounds3D(CoordinateSystem3D system) {
		if (!isInit()) return new AlignedBoundingBox();		
		return new AlignedBoundingBox(
				system.fromCoordinateSystem2D(this.lower),
				system.fromCoordinateSystem2D(this.upper));
	}

	/** {@inheritDoc}
	 */
	@Override
	public AlignedBoundingBox toBounds3D(float z, float zsize, CoordinateSystem3D system) {
		if (!isInit()) return new AlignedBoundingBox();		
		return new AlignedBoundingBox(
				system.fromCoordinateSystem2D(this.lower, z),
				system.fromCoordinateSystem2D(this.upper, z+zsize));
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.lower.set(0,0);
			this.upper.set(0,0);
			this.vertices = null;
		}
	}

	/** {@inheritDoc}
	 * 
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!isInit()) return false;		
		if (this==o) return true;
		if (o instanceof MinimumBoundingRectangle) {
			return (this.lower.equals(((MinimumBoundingRectangle)o).lower) && this.upper.equals(((MinimumBoundingRectangle)o).upper));
		}
		return false;
	}
	
	/** Check if the following constraints are respected:
	 * <ul>
	 * <li>{@code lower.x &lt;= upper.x}</li>
	 * <li>{@code lower.y &lt;= upper.y}</li>
	 * <li>{@code lower.z &lt;= upper.z}</li>
	 * </ul>
	 */
	protected void checkBounds() {
		if (this.upper.getX() < this.lower.getX()) {
			float t = this.upper.getX();
			this.upper.setX(this.lower.getX());
			this.lower.setX(t);
		}
		if (this.upper.getY() < this.lower.getY()) {
			float t = this.upper.getY();
			this.upper.setY(this.lower.getY());
			this.lower.setY(t);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || (((this.upper.getX() - this.lower.getX()) <= 0)
				|| ((this.upper.getY() - this.lower.getY()) <= 0));
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	/**
	 * Sets the lower corner of this bounding rectangle
	 * 
	 * @param point
	 */
	public void setLower(Tuple2f<?> point) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.lower.set(point);
		if (!init) this.upper.set(point);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the lower corner of this bounding rectangle
	 * 
	 * @param x
	 * @param y
	 */
	public void setLower(float x, float y) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.lower.set(x, y);
		if (!init) this.upper.set(x, y);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the upper corner of this bounding rectangle
	 * 
	 * @param point
	 */
	public void setUpper(Tuple2f<?> point) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.upper.set(point);
		if (!init) this.lower.set(point);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the upper corner of this bounding rectangle
	 * 
	 * @param x
	 * @param y
	 */
	public void setUpper(float x, float y) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.upper.set(x, y);
		if (!init) this.lower.set(x, y);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the lower and upper corners of this bounding rectangle
	 * 
	 * @param lower
	 * @param upper
	 */
	public void set(Tuple2f<?> lower, Tuple2f<?> upper) {
		this.isBoundInit = true;
		this.lower.set(lower);
		this.upper.set(upper);
		this.vertices = null;
		checkBounds();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point2f lower, Point2f upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(this.lower);
		upper.set(this.upper);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getLower() {
		return this.lower;
	}

	/**
	 * Gets the lower corner of this bounding rectangle
	 * 
	 * @param point is the object to set with the coordinates of the lower point.
	 */
	public void getLower(Tuple2f<?> point) {
		point.set(this.lower);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getUpper() {
		return this.upper;
	}

	/**
	 * Gets the upper corner of this bounding rectangle
	 * 
	 * @param point is the object to set with the coordinates of the upper point.
	 */
	public void getUpper(Tuple2f<?> point) {
		point.set(this.upper);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getCenter() {
		return new EuclidianPoint2D((this.upper.getX() + this.lower.getX()) / 2.f,
				(this.upper.getY() + this.lower.getY()) / 2.f);
	}

	/**
	 * Gets the upper corner of this bounding rectangle
	 * 
	 * @param point is the object to set with the coordinates of the center point.
	 */
	public void getCenter(Tuple2f<?> point) {
		point.set((this.upper.getX() + this.lower.getX()) / 2.f,
				  (this.upper.getY() + this.lower.getY()) / 2.f);
	}

	private void ensureVertices() {
		if(this.vertices==null) {
			this.vertices = new Vector2f[4];
			float sx = getSizeX()/2.f;
			float sy = getSizeY()/2.f;
			
			this.vertices[0] = new Vector2f( sx, sy);
			this.vertices[1] = new Vector2f(-sx, sy);
			this.vertices[2] = new Vector2f(-sx,-sy);
			this.vertices[3] = new Vector2f( sx,-sy);
		}		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector2f> getLocalOrientedBoundVertices() {
		ensureVertices();
		return ArrayUtil.sizedIterator(this.vertices);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getLocalVertexAt(int index) {
		ensureVertices();
		return this.vertices[index];
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		ensureVertices();
		return this.vertices.length;
	}

	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<EuclidianPoint2D> getGlobalOrientedBoundVertices() {
		return new LocalToGlobalVertexIterator(getCenter(), getLocalOrientedBoundVertices());
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getGlobalVertexAt(int index) {
		ensureVertices();
		EuclidianPoint2D p = new EuclidianPoint2D(getCenter());
		p.add(this.vertices[index]);
		return p;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f[] getOrientedBoundAxis() {
		return new Vector2f[] {
				new Vector2f(1.,0.),
				new Vector2f(0.,1.)
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getOrientedBoundExtentVector() {
		return new Vector2f(
				getSizeX()/2.,
				getSizeY()/2.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return new float[] {
				getSizeX()/2.f,
				getSizeY()/2.f
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getR() {
		return new Vector2f(1.,0.);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getS() {
		return new Vector2f(0.,1.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return getSizeX() / 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return getSizeY() / 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getX() - this.lower.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getY() - this.lower.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getSize() {
		if (!this.isBoundInit) return null;
		return new Vector2f(this.upper.getX() - this.lower.getX(), this.upper.getY()
				- this.lower.getY());
	}

	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("MinimumBoundingRectangle["); //$NON-NLS-1$
		sb.append(this.lower.getX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.lower.getY());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getY());
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Add the point into the bounds.
	 */
	@Override
	protected void combinePoints(boolean isAlreadyInit, Collection<? extends Tuple2f> pointList) {
		if (pointList == null || pointList.isEmpty())
			return;

		boolean init = isAlreadyInit;
		float minx, miny;
		float maxx, maxy;

		minx = this.lower.getX();
		miny = this.lower.getY();
		maxx = this.upper.getX();
		maxy = this.upper.getY();

		for (Tuple2f<?> t : pointList) {
			if (t!=null) {
				if (!init) {
					init = true;
					minx = t.getX();
					miny = t.getY();
					maxx = t.getX();
					maxy = t.getY();
				} else {
					if (t.getX() < minx)
						minx = t.getX();
					if (t.getY() < miny)
						miny = t.getY();
					if (t.getX() > maxx)
						maxx = t.getX();
					if (t.getY() > maxy)
						maxy = t.getY();
				}
			}
		}

		this.lower.set(minx, miny);
		this.upper.set(maxx, maxy);
		this.isBoundInit = true;
		this.vertices = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void combineBounds(boolean isInit,
			Collection<? extends Bounds2D> bounds) {
		if (bounds == null || bounds.isEmpty())
			return;

		boolean init = isInit;
		float minx, miny;
		float maxx, maxy;

		minx = this.lower.getX();
		miny = this.lower.getY();
		maxx = this.upper.getX();
		maxy = this.upper.getY();

		EuclidianPoint2D lowerPt, upperPt;

		for (Bounds2D b : bounds) {
			if (b!=null && b.isInit()) {
				lowerPt = b.getLower();
				upperPt = b.getUpper();
				if (!init) {
					init = true;
					minx = lowerPt.getX();
					miny = lowerPt.getY();
					maxx = upperPt.getX();
					maxy = upperPt.getY();
				} else {
					if (lowerPt.getX() < minx)
						minx = lowerPt.getX();
					if (lowerPt.getY() < miny)
						miny = lowerPt.getY();
					if (upperPt.getX() > maxx)
						maxx = upperPt.getX();
					if (upperPt.getY() > maxy)
						maxy = upperPt.getY();
				}
			}
		}

		this.lower.set(minx, miny);
		this.upper.set(maxx, maxy);
		this.isBoundInit = true;
		this.vertices = null;
	}
	
	/** Replies the rectangle that corresponds to the
	 * upper (max y), right (max x) corner.
	 * 
	 * @return the upper-right quarter of this rectangle.
	 */
	public MinimumBoundingRectangle getNorthEastRectangle() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		return new MinimumBoundingRectangle(
				midX,midY,
				this.upper.getX(),this.upper.getY());
	}
	
	/** Replies the rectangle that corresponds to the
	 * upper (max y), left (min x) corner.
	 * 
	 * @return the upper-left quarter of this rectangle.
	 */
	public MinimumBoundingRectangle getNorthWestRectangle() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		return new MinimumBoundingRectangle(
				this.lower.getX(),midY,
				midX,this.upper.getY());
	}

	/** Replies the rectangle that corresponds to the
	 * lower (min y), right (max x) corner.
	 * 
	 * @return the lower-right quarter of this rectangle.
	 */
	public MinimumBoundingRectangle getSouthEastRectangle() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		return new MinimumBoundingRectangle(
				midX,this.lower.getY(),
				this.upper.getX(),midY);
	}
	
	/** Replies the rectangle that corresponds to the
	 * lower (min y), left (min x) corner.
	 * 
	 * @return the lower-left quarter of this rectangle.
	 */
	public MinimumBoundingRectangle getSouthWestRectangle() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		return new MinimumBoundingRectangle(
				this.lower.getX(),this.lower.getY(),
				midX,midY);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2f p) {
		if (!isInit()) return Float.NaN;		
		float d1 = 0;
		if (p.getX()<this.lower.getX()) {
			d1 = this.lower.getX() - p.getX();
			d1 = d1*d1;
		}
		else if (p.getX()>this.upper.getX()) {
			d1 = p.getX() - this.upper.getX();
			d1 = d1*d1;
		}
		float d2 = 0;
		if (p.getY()<this.lower.getY()) {
			d2 = this.lower.getY() - p.getY();
			d2 = d2*d2;
		}
		else if (p.getY()>this.upper.getY()) {
			d2 = p.getY() - this.upper.getY();
			d2 = d2*d2;
		}
		return d1+d2;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D nearestPoint(Point2f reference) {
		if (!isInit()) return null;		
		EuclidianPoint2D nearest = new EuclidianPoint2D();
		
		if (reference.getX()<this.lower.getX()) {
			nearest.setX(this.lower.getX());
		}
		else if (reference.getX()>this.upper.getX()) {
			nearest.setX(this.upper.getX());
		}
		else {
			nearest.setX(reference.getX());
		}

		if (reference.getY()<this.lower.getY()) {
			nearest.setY(this.lower.getY());
		}
		else if (reference.getY()>this.upper.getY()) {
			nearest.setY(this.upper.getY());
		}
		else {
			nearest.setY(reference.getY());
		}

		return nearest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point2f p) {
		if (!isInit()) return Float.NaN;		
		float d1 = 0;
		if (p.getX()<this.lower.getX()) {
			d1 = this.upper.getX() - p.getX();
		}
		else if (p.getX()>this.upper.getX()) {
			d1 = p.getX() - this.lower.getX();
		}
		else d1 = Math.max(p.getX()-this.lower.getX(), this.upper.getX()-p.getX());
		
		float d2 = 0;
		if (p.getY()<this.lower.getY()) {
			d2 = this.upper.getY() - p.getY();
		}
		else if (p.getY()>this.upper.getY()) {
			d2 = p.getY() - this.lower.getY();
		}
		else d2 = Math.max(p.getY()-this.lower.getY(), this.upper.getY()-p.getY());
				
		return d1*d1+d2*d2;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D farestPoint(Point2f reference) {
		if (!isInit()) return null;		
		EuclidianPoint2D farest = new EuclidianPoint2D();
		
		if (reference.getX()<(this.lower.getX()+this.upper.getX()) / 2.) {
			farest.setX(this.upper.getX());
		}
		else {
			farest.setX(this.lower.getX());
		}

		if (reference.getY()<(this.lower.getY()+this.upper.getY()) / 2.) {
			farest.setY(this.upper.getY());
		}
		else {
			farest.setY(this.lower.getY());
		}

		return farest;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void translate(Vector2f v) {
		if (isInit()) {
			this.lower.setX(this.lower.getX() + v.getX());
			this.lower.setY(this.lower.getY() + v.getY());
			this.upper.setX(this.upper.getX() + v.getX());
			this.upper.setY(this.upper.getY() + v.getY());
			this.vertices = null;
		}
	}

	//---------------------------------------------
	// IntersectionClassifier
	//---------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f p) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return (this.lower.getX()<=p.getX() && p.getX()<=this.upper.getX()
				&&
				this.lower.getY()<=p.getY() && p.getY()<=this.upper.getY())
				? IntersectionType.INSIDE
				: IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f p) {
		if (!isInit()) return false;		
		return (this.lower.getX()<=p.getX() && p.getX()<=this.upper.getX()
				&&
				this.lower.getY()<=p.getY() && p.getY()<=this.upper.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f l, Point2f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesAlignedRectangles(
				l, u, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f l, Point2f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsAlignedRectangles(
				l, u, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f c, float r) {
		assert(c!=null);
		assert(r>=0.);
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesSolidCircleSolidAlignedRectangle(
				c, r, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f c, float r) {
		assert(c!=null);
		assert(r>=0.);
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsSolidCircleSolidAlignedRectangle(
				c, r, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f center, Vector2f[] axis, float[] extent) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesAlignedRectangleOrientedRectangle(
				this.lower, this.upper,
				center, axis, extent).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f center, Vector2f[] axis, float[] extent) {
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsAlignedRectangleOrientedRectangle(
				this.lower, this.upper,
				center, axis, extent);
	}

}
