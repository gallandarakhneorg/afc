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

package org.arakhne.afc.math.geometry3d.continuous;

import java.util.Arrays;
import java.util.Collection;

import org.arakhne.afc.math.geometry.IntersectionType;
import org.arakhne.afc.math.geometry.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.MinimumBoundingRectangle;
import org.arakhne.afc.math.geometry.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;

/**
 * An Axis Aligned Bounding Box.
 * <p>
 * All the transformations on this bounding box are
 * relative to the box's center.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AxisAlignedBox
extends AbstractCombinableBounds3D 
implements AlignedCombinableBounds3D, OrientedCombinableBounds3D, TranslatableBounds3D {

	private static final long serialVersionUID = 7615135756504420528L;

	/**
	 * The lower corner of this bounding box
	 */
	EuclidianPoint3D lower = new EuclidianPoint3D();

	/**
	 * The upper corner of this bounding box
	 */
	EuclidianPoint3D upper = new EuclidianPoint3D();
	
	private boolean isBoundInit;
	
	/**
	 * The vertices that composes this box, it is compiled when it is required not before
	 */
	private transient Vector3f[] vertices = null;

	/** Uninitialized bouding box.
	 */
	public AxisAlignedBox() {
		this.isBoundInit = false;
	}

	/**
	 * @param lx is the lower point of the box.
	 * @param ly is the lower point of the box.
	 * @param lz is the lower point of the box.
	 * @param ux is the upper point of the box.
	 * @param uy is the upper point of the box.
	 * @param uz is the upper point of the box.
	 */
	public AxisAlignedBox(float lx, float ly, float lz, float ux, float uy, float uz) {
		this.lower.set(lx,ly,lz);
		this.upper.set(ux,uy,uz);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param lower is the lower point of the box.
	 * @param upper is the upper point of the box.
	 */
	public AxisAlignedBox(Tuple3f lower, Tuple3f upper) {
		this.lower.set(lower);
		this.upper.set(upper);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public AxisAlignedBox(Collection<? extends AxisAlignedBox> bboxList) {
		combineBounds(false, bboxList);
		this.isBoundInit = true;
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public AxisAlignedBox(CombinableBounds3D... bboxList) {
		combineBounds(false, Arrays.asList(bboxList));
		this.isBoundInit = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType3D getBoundType() {
		return BoundingPrimitiveType3D.ALIGNED_BOX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AxisAlignedBox clone() {
		AxisAlignedBox clone = (AxisAlignedBox)super.clone();
		clone.lower = (EuclidianPoint3D)this.lower.clone();
		clone.upper = (EuclidianPoint3D)this.upper.clone();
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.lower.set(0,0,0);
			this.upper.set(0,0,0);
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
		if (o instanceof AxisAlignedBox) {
			return (this.lower.equals(((AxisAlignedBox)o).lower) && this.upper.equals(((AxisAlignedBox)o).upper));
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
		if (this.upper.getA() < this.lower.getA()) {
			float t = this.upper.getA();
			this.upper.setA(this.lower.getA());
			this.lower.setA(t);
		}
		if (this.upper.getB() < this.lower.getB()) {
			float t = this.upper.getB();
			this.upper.setB(this.lower.getB());
			this.lower.setB(t);
		}
		if (this.upper.getC() < this.lower.getC()) {
			float t = this.upper.getC();
			this.upper.setC(this.lower.getC());
			this.lower.setC(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || (((this.upper.getA() - this.lower.getA()) <= 0)
				|| ((this.upper.getB() - this.lower.getB()) <= 0) || ((this.upper.getC() - this.lower.getC()) <= 0));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	/**
	 * Sets the lower corner of this bounding box
	 * 
	 * @param point
	 */
	public void setLower(Tuple3f point) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.lower.set(point);
		if (!init) this.upper.set(point);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the lower corner of this bounding box
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setLower(float x, float y, float z) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.lower.set(x, y, z);
		if (!init) this.upper.set(x,y,z);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the upper corner of this bounding box
	 * 
	 * @param point
	 */
	public void setUpper(Tuple3f point) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.upper.set(point);
		if (!init) this.lower.set(point);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the upper corner of this bounding box
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setUpper(float x, float y, float z) {
		boolean init = this.isBoundInit;
		this.isBoundInit = true;
		this.upper.set(x, y, z);
		if (!init) this.lower.set(x,y,z);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * Sets the lower and upper corners of this bounding box
	 * 
	 * @param lower
	 * @param upper
	 */
	public void set(Tuple3f lower, Tuple3f upper) {
		this.isBoundInit = true;
		this.lower.set(lower);
		this.upper.set(upper);
		this.vertices = null;
		checkBounds();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3f lower, Point3f upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(this.lower);
		upper.set(this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		return this.lower;
	}

	/**
	 * Gets the lower corner of this bounding box
	 * 
	 * @param point is the object to set with the coordinates of the lower point.
	 */
	public void getLower(Tuple3f point) {
		point.set(this.lower);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		return this.upper;
	}

	/**
	 * Gets the upper corner of this bounding box
	 * 
	 * @param point is the object to set with the coordinates of the upper point.
	 */
	public void getUpper(Tuple3f point) {
		point.set(this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		return new EuclidianPoint3D((this.upper.getA() + this.lower.getA()) / 2.f,
				(this.upper.getB() + this.lower.getB()) / 2.f,
				(this.upper.getC() + this.lower.getC()) / 2.f);
	}

	/**
	 * Gets the upper corner of this bounding box
	 * 
	 * @param point is the object to set with the coordinates of the center point.
	 */
	public void getCenter(Tuple3f point) {
		point.set((this.upper.getA() + this.lower.getA()) / 2.f,
				  (this.upper.getB() + this.lower.getB()) / 2.f,
				  (this.upper.getC() + this.lower.getC()) / 2.f);
	}
	
	private void ensureVertices() {
		if(this.vertices==null) {
			this.vertices = new Vector3f[8];
			float sx = getSizeX()/2.f;
			float sy = getSizeY()/2.f;
			float sz = getSizeZ()/2.f;
			
			this.vertices[0] = new Vector3f( sx, sy, sz);
			this.vertices[1] = new Vector3f(-sx, sy, sz);
			this.vertices[2] = new Vector3f(-sx,-sy, sz);
			this.vertices[3] = new Vector3f( sx,-sy, sz);
			this.vertices[4] = new Vector3f( sx,-sy,-sz);
			this.vertices[5] = new Vector3f( sx, sy,-sz);
			this.vertices[6] = new Vector3f(-sx, sy,-sz);
			this.vertices[7] = new Vector3f(-sx,-sy,-sz);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector3f> getLocalOrientedBoundVertices() {
		ensureVertices();
		return ArrayUtil.sizedIterator(this.vertices);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<EuclidianPoint3D> getGlobalOrientedBoundVertices() {
		return new LocalToGlobalVertexIterator(getCenter(), getLocalOrientedBoundVertices());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f[] getOrientedBoundAxis() {
		return new Vector3f[] {
				new Vector3f(1.,0.,0.),
				new Vector3f(0.,1.,0.),
				new Vector3f(0.,0.,1.)
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getOrientedBoundExtentVector() {
		return new Vector3f(
				getSizeX()/2.,
				getSizeY()/2.,
				getSizeZ()/2.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return new float[] {
				getSizeX()/2.f,
				getSizeY()/2.f,
				getSizeZ()/2.f
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getGlobalVertexAt(int index) {
		switch(index) {
		case 0:
			return new EuclidianPoint3D(
					this.upper.getA(),
					this.upper.getB(),
					this.upper.getC());
		case 1:
			return new EuclidianPoint3D(
					this.upper.getA(),
					this.upper.getB(),
					this.lower.getC());
		case 2:
			return new EuclidianPoint3D(
					this.upper.getA(),
					this.lower.getB(),
					this.upper.getC());
		case 3:
			return new EuclidianPoint3D(
					this.upper.getA(),
					this.lower.getB(),
					this.lower.getC());
		case 4:
			return new EuclidianPoint3D(
					this.lower.getA(),
					this.upper.getB(),
					this.upper.getC());
		case 5:
			return new EuclidianPoint3D(
					this.lower.getA(),
					this.upper.getB(),
					this.lower.getC());
		case 6:
			return new EuclidianPoint3D(
					this.lower.getA(),
					this.lower.getB(),
					this.upper.getC());
		case 7:
			return new EuclidianPoint3D(
					this.lower.getA(),
					this.lower.getB(),
					this.lower.getC());
		default:
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getLocalVertexAt(int index) {
		ensureVertices();
		return this.vertices[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		ensureVertices();
		return this.vertices.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getR() {
		return new Vector3f(1.,0.,0.);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getS() {
		return new Vector3f(0.,1.,0.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getT() {
		return new Vector3f(0.,0.,1.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return getSizeX() / 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return getSizeY() / 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getTExtent() {
		return getSizeZ() / 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getA() - this.lower.getA();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getB() - this.lower.getB();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getC() - this.lower.getC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		if (!this.isBoundInit) return null;
		return new Vector3f(this.upper.getA() - this.lower.getA(), this.upper.getB()
				- this.lower.getB(), this.upper.getC() - this.lower.getC());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("BoundingBox["); //$NON-NLS-1$
		sb.append(this.lower.getA());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.lower.getB());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.lower.getC());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getA());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getB());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getC());
		sb.append(']');
		return sb.toString();
	}

	/**
	 * Add the point into the bounds.
	 */
	@Override
	protected void combinePoints(boolean isAlreadyInit, Collection<? extends Tuple3f> pointList) {
		if (pointList == null || pointList.isEmpty())
			return;

		boolean init = isAlreadyInit;
		float minx, miny, minz;
		float maxx, maxy, maxz;

		minx = this.lower.getA();
		miny = this.lower.getB();
		minz = this.lower.getC();
		maxx = this.upper.getA();
		maxy = this.upper.getB();
		maxz = this.upper.getC();

		for (Tuple3f t : pointList) {
			if (t!=null) {
				if (!init) {
					init = true;
					minx = t.getX();
					miny = t.getY();
					minz = t.getZ();
					maxx = t.getX();
					maxy = t.getY();
					maxz = t.getZ();
				} else {
					if (t.getX() < minx)
						minx = t.getX();
					if (t.getY() < miny)
						miny = t.getY();
					if (t.getZ() < minz)
						minz = t.getZ();
					if (t.getX() > maxx)
						maxx = t.getX();
					if (t.getY() > maxy)
						maxy = t.getY();
					if (t.getZ() > maxz)
						maxz = t.getZ();
				}
			}
		}

		this.lower.set(minx, miny, minz);
		this.upper.set(maxx, maxy, maxz);
		this.isBoundInit = true;
		this.vertices = null;
	}

	/**
	 * Add the bounds into the bounds.
	 */
	@Override
	protected void combineBounds(boolean isInit,
			Collection<? extends Bounds3D> bounds) {
		if (bounds == null || bounds.isEmpty())
			return;

		boolean init = isInit;
		float minx, miny, minz;
		float maxx, maxy, maxz;

		minx = this.lower.getA();
		miny = this.lower.getB();
		minz = this.lower.getC();
		maxx = this.upper.getA();
		maxy = this.upper.getB();
		maxz = this.upper.getC();

		Point3f lowerPt, upperPt;

		for (Bounds3D b : bounds) {
			if (b!=null && b.isInit()) {
				lowerPt = b.getLower();
				upperPt = b.getUpper();
				if (!init) {
					init = true;
					minx = lowerPt.getX();
					miny = lowerPt.getY();
					minz = lowerPt.getZ();
					maxx = upperPt.getX();
					maxy = upperPt.getY();
					maxz = upperPt.getZ();
				} else {
					if (lowerPt.getX() < minx)
						minx = lowerPt.getX();
					if (lowerPt.getY() < miny)
						miny = lowerPt.getY();
					if (lowerPt.getZ() < minz)
						minz = lowerPt.getZ();
					if (upperPt.getX() > maxx)
						maxx = upperPt.getX();
					if (upperPt.getY() > maxy)
						maxy = upperPt.getY();
					if (upperPt.getZ() > maxz)
						maxz = upperPt.getZ();
				}
			}
		}

		this.lower.set(minx, miny, minz);
		this.upper.set(maxx, maxy, maxz);
		this.isBoundInit = true;
		this.vertices = null;
	}
	
	/** Replies the voxel that corresponds to the
	 * upper (max z), left (max y), front (min x)
	 * corner.
	 * 
	 * @return the frontal upper-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getNorthWestFrontVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				this.lower.getA(),midY,midZ,
				midX,this.upper.getB(),this.upper.getC());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), left (max y), back (max x)
	 * corner.
	 * 
	 * @return the background upper-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getNorthWestBackVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				midX,midY,midZ,
				this.upper.getA(),this.upper.getB(),this.upper.getC());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), right (min y), front (min x)
	 * corner.
	 * 
	 * @return the frontal upper-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getNorthEastFrontVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				this.lower.getA(),this.lower.getB(),midZ,
				midX,midY,this.upper.getC());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), right (min y), back (max x)
	 * corner.
	 * 
	 * @return the background upper-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getNorthEastBackVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				midX,this.lower.getB(),midZ,
				this.upper.getA(),midY,this.upper.getC());
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), left (max y), front (min x)
	 * corner.
	 * 
	 * @return the frontal lower-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getSouthWestFrontVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				this.lower.getA(),midY,this.lower.getC(),
				midX,this.upper.getB(),midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), left (max y), back (max x)
	 * corner.
	 * 
	 * @return the background lower-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getSouthWestBackVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				midX,midY,this.lower.getC(),
				this.upper.getA(),this.upper.getB(),midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), right (min y), front (min x)
	 * corner.
	 * 
	 * @return the frontal lower-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getSouthEastFrontVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				this.lower.getA(),this.lower.getB(),this.lower.getC(),
				midX,midY,midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), right (min y), back (max x)
	 * corner.
	 * 
	 * @return the background lower-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AxisAlignedBox getSouthEastBackVoxel() {
		float midX = (this.lower.getA()+this.upper.getA())/2.f;
		float midY = (this.lower.getB()+this.upper.getB())/2.f;
		float midZ = (this.lower.getC()+this.upper.getC())/2.f;
		return new AxisAlignedBox(
				midX,this.lower.getB(),this.lower.getC(),
				this.upper.getA(),midY,midZ);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f p) {
		if (!isInit()) return Float.NaN;
		float d1 = 0;
		if (p.getX()<this.lower.getA()) {
			d1 = this.lower.getA() - p.getX();
			d1 = d1*d1;
		}
		else if (p.getX()>this.upper.getA()) {
			d1 = p.getX() - this.upper.getA();
			d1 = d1*d1;
		}
		float d2 = 0;
		if (p.getY()<this.lower.getB()) {
			d2 = this.lower.getB() - p.getY();
			d2 = d2*d2;
		}
		else if (p.getY()>this.upper.getB()) {
			d2 = p.getY() - this.upper.getB();
			d2 = d2*d2;
		}
		float d3 = 0;
		if (p.getZ()<this.lower.getC()) {
			d3 = this.lower.getC() - p.getZ();
			d3 = d3*d3;
		}
		else if (p.getZ()>this.upper.getC()) {
			d3 = p.getZ() - this.upper.getC();
			d3 = d3*d3;
		}
		return d1+d2+d3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3f reference) {
		if (!isInit()) return null;
		EuclidianPoint3D nearest = new EuclidianPoint3D();
		
		if (reference.getX()<this.lower.getA()) {
			nearest.setA(this.lower.getA());
		}
		else if (reference.getX()>this.upper.getA()) {
			nearest.setA(this.upper.getA());
		}
		else {
			nearest.setA(reference.getX());
		}

		if (reference.getY()<this.lower.getB()) {
			nearest.setB(this.lower.getB());
		}
		else if (reference.getY()>this.upper.getB()) {
			nearest.setB(this.upper.getB());
		}
		else {
			nearest.setB(reference.getY());
		}

		if (reference.getZ()<this.lower.getC()) {
			nearest.setC(this.lower.getC());
		}
		else if (reference.getZ()>this.upper.getC()) {
			nearest.setC(this.upper.getC());
		}
		else {
			nearest.setC(reference.getZ());
		}

		return nearest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point3f p) {
		if (!isInit()) return Float.NaN;
		float d1 = 0;
		if (p.getX()<this.lower.getA()) {
			d1 = this.upper.getA() - p.getX();
		}
		else if (p.getX()>this.upper.getA()) {
			d1 = p.getX() - this.lower.getA();
		}
		else d1 = Math.max(p.getX()-this.lower.getA(), this.upper.getA()-p.getX());
		
		float d2 = 0;
		if (p.getY()<this.lower.getB()) {
			d2 = this.upper.getB() - p.getY();
		}
		else if (p.getY()>this.upper.getB()) {
			d2 = p.getY() - this.lower.getB();
		}
		else d2 = Math.max(p.getY()-this.lower.getB(), this.upper.getB()-p.getY());
		
		float d3 = 0;
		if (p.getZ()<this.lower.getC()) {
			d3 = this.upper.getC() - p.getZ();
		}
		else if (p.getZ()>this.upper.getC()) {
			d3 = p.getZ() - this.lower.getC();
		}
		else d3 = Math.max(p.getZ()-this.lower.getC(), this.upper.getC()-p.getZ());
		
		return d1*d1+d2*d2+d3*d3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		if (!isInit()) return null;
		EuclidianPoint3D farest = new EuclidianPoint3D();
		
		if (reference.getX()<this.lower.getA()) {
			farest.setA(this.upper.getA());
		}
		else if (reference.getX()>this.upper.getA()) {
			farest.setA(this.lower.getA());
		}
		else {
			float dl = Math.abs(reference.getX()-this.lower.getA());
			float du = Math.abs(reference.getX()-this.upper.getA());
			if (dl>du) {
				farest.setA(this.lower.getA());
			}
			else {
				farest.setA(this.upper.getA());
			}
		}

		if (reference.getY()<this.lower.getB()) {
			farest.setB(this.upper.getB());
		}
		else if (reference.getY()>this.upper.getB()) {
			farest.setB(this.lower.getB());
		}
		else {
			float dl = Math.abs(reference.getY()-this.lower.getB());
			float du = Math.abs(reference.getY()-this.upper.getB());
			if (dl>du) {
				farest.setB(this.lower.getB());
			}
			else {
				farest.setB(this.upper.getB());
			}
		}

		if (reference.getZ()<this.lower.getC()) {
			farest.setC(this.upper.getC());
		}
		else if (reference.getZ()>this.upper.getC()) {
			farest.setC(this.lower.getC());
		}
		else {
			float dl = Math.abs(reference.getZ()-this.lower.getC());
			float du = Math.abs(reference.getZ()-this.upper.getC());
			if (dl>du) {
				farest.setC(this.lower.getC());
			}
			else {
				farest.setC(this.upper.getC());
			}
		}

		return farest;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3f v) {
		if (isInit()) {
			this.lower.setA(this.lower.getA() + v.getX());
			this.lower.setB(this.lower.getB() + v.getY());
			this.lower.setC(this.lower.getC() + v.getZ());
			this.upper.setA(this.upper.getA() + v.getX());
			this.upper.setB(this.upper.getB() + v.getY());
			this.upper.setC(this.upper.getC() + v.getZ());
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position) {
		if (isInit()) {
			Point3f c = getPosition();
			assert(c!=null);
			this.lower.sub(c);
			this.upper.sub(c);
			this.lower.add(position);
			this.upper.add(position);
			this.vertices = null;
		}
	}
	
	/**
	 * Replies the height of this box.
	 * 
	 * @return the height of this box.
	 */
	private float getHeight(CoordinateSystem3D cs) {
		return (cs.getHeightCoordinateIndex()==1)
			?  getSizeY()
			: getSizeZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position, boolean onGround) {
		if (isInit()) {
			Point3f c = getPosition();
			assert(c!=null);
			this.lower.sub(c);
			this.upper.sub(c);
			if (onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				Point3f np = new Point3f(position);
				cs.addHeight(np, getHeight(cs)/2.f);
				this.lower.add(np);
				this.upper.add(np);
				
			}
			else {
				this.lower.add(position);
				this.upper.add(position);
			}
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinimumBoundingRectangle toBounds2D() {
		return toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MinimumBoundingRectangle toBounds2D(CoordinateSystem3D system) {
		if (!isInit()) return new MinimumBoundingRectangle();
		return new MinimumBoundingRectangle(
				system.toCoordinateSystem2D(this.lower),
				system.toCoordinateSystem2D(this.upper));
	}

	//---------------------------------------------
	// IntersectionClassifier
	//---------------------------------------------
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f p) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return (this.lower.getA()<=p.getX() && p.getX()<=this.upper.getA()
				&&
				this.lower.getB()<=p.getY() && p.getY()<=this.upper.getB()
				&&
				this.lower.getC()<=p.getZ() && p.getZ()<=this.upper.getC())
				? IntersectionType.INSIDE
				: IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f p) {
		if (!isInit()) return false;
		return (this.lower.getA()<=p.getX() && p.getX()<=this.upper.getA()
				&&
				this.lower.getB()<=p.getY() && p.getY()<=this.upper.getB()
				&&
				this.lower.getC()<=p.getZ() && p.getZ()<=this.upper.getC());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f l, Point3f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		assert(l.getZ()<=u.getZ());
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesAlignedBoxes(
				l, u, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f l, Point3f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		assert(l.getZ()<=u.getZ());
		if (!isInit()) return false;
		return IntersectionUtil.intersectsAlignedBoxes(
				l, u, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f c, float r) {
		assert(c!=null);
		assert(r>=0.);
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidSphereSolidAlignedBox(
				c, r, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f c, float r) {
		if (!isInit()) return false;
		assert(c!=null);
		assert(r>=0.);
		return IntersectionUtil.intersectsSolidSphereSolidAlignedBox(
				c, r, this.lower, this.upper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return (plane.classifies(
				this.lower.getA(), this.lower.getB(), this.lower.getC(),
				this.upper.getA(), this.upper.getB(), this.upper.getC())== PlanarClassificationType.COINCIDENT)
			? IntersectionType.SPANNING : IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		if (!isInit()) return false;
		return plane.intersects(
				this.lower.getA(), this.lower.getB(), this.lower.getC(),
				this.upper.getA(), this.upper.getB(), this.upper.getC());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		if (!isInit()) return null;
		return plane.classifies(
				this.lower.getA(), this.lower.getB(), this.lower.getC(),
				this.upper.getA(), this.upper.getB(), this.upper.getC());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f center, Vector3f[] axis, float[] extent) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesAlignedBoxOrientedBox(
				this.lower, this.upper,
				center, axis, extent).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f center, Vector3f[] axis, float[] extent) {
		if (!isInit()) return false;
		return IntersectionUtil.intersectsAlignedBoxOrientedBox(
				this.lower, this.upper,
				center, axis, extent);
	}

}
