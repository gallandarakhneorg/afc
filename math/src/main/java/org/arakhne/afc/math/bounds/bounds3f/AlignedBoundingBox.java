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

package org.arakhne.afc.math.bounds.bounds3f;

import java.util.Arrays;
import java.util.Collection;

import org.arakhne.afc.math.bounds.bounds2f.MinimumBoundingRectangle;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.intersection.IntersectionUtil;
import org.arakhne.afc.math.plane.PlanarClassificationType;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
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
public class AlignedBoundingBox
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
	public AlignedBoundingBox() {
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
	public AlignedBoundingBox(float lx, float ly, float lz, float ux, float uy, float uz) {
		this.lower.set(lx,ly,lz);
		this.upper.set(ux,uy,uz);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param lower is the lower point of the box.
	 * @param upper is the upper point of the box.
	 */
	public AlignedBoundingBox(Tuple3f lower, Tuple3f upper) {
		this.lower.set(lower);
		this.upper.set(upper);
		this.isBoundInit = true;
		checkBounds();
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public AlignedBoundingBox(Collection<? extends AlignedBoundingBox> bboxList) {
		combineBounds(false, bboxList);
		this.isBoundInit = true;
	}

	/**
	 * @param bboxList are the boxes to combine to initialize this bounding object.
	 */
	public AlignedBoundingBox(CombinableBounds3D... bboxList) {
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
	public AlignedBoundingBox clone() {
		AlignedBoundingBox clone = (AlignedBoundingBox)super.clone();
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
		if (o instanceof AlignedBoundingBox) {
			return (this.lower.equals(((AlignedBoundingBox)o).lower) && this.upper.equals(((AlignedBoundingBox)o).upper));
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
		if (this.upper.getZ() < this.lower.getZ()) {
			float t = this.upper.getZ();
			this.upper.setZ(this.lower.getZ());
			this.lower.setZ(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || (((this.upper.getX() - this.lower.getX()) <= 0)
				|| ((this.upper.getY() - this.lower.getY()) <= 0) || ((this.upper.getZ() - this.lower.getZ()) <= 0));
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
		return new EuclidianPoint3D((this.upper.getX() + this.lower.getX()) / 2.f,
				(this.upper.getY() + this.lower.getY()) / 2.f,
				(this.upper.getZ() + this.lower.getZ()) / 2.f);
	}

	/**
	 * Gets the upper corner of this bounding box
	 * 
	 * @param point is the object to set with the coordinates of the center point.
	 */
	public void getCenter(Tuple3f point) {
		point.set((this.upper.getX() + this.lower.getX()) / 2.f,
				  (this.upper.getY() + this.lower.getY()) / 2.f,
				  (this.upper.getZ() + this.lower.getZ()) / 2.f);
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
					this.upper.getX(),
					this.upper.getY(),
					this.upper.getZ());
		case 1:
			return new EuclidianPoint3D(
					this.upper.getX(),
					this.upper.getY(),
					this.lower.getZ());
		case 2:
			return new EuclidianPoint3D(
					this.upper.getX(),
					this.lower.getY(),
					this.upper.getZ());
		case 3:
			return new EuclidianPoint3D(
					this.upper.getX(),
					this.lower.getY(),
					this.lower.getZ());
		case 4:
			return new EuclidianPoint3D(
					this.lower.getX(),
					this.upper.getY(),
					this.upper.getZ());
		case 5:
			return new EuclidianPoint3D(
					this.lower.getX(),
					this.upper.getY(),
					this.lower.getZ());
		case 6:
			return new EuclidianPoint3D(
					this.lower.getX(),
					this.lower.getY(),
					this.upper.getZ());
		case 7:
			return new EuclidianPoint3D(
					this.lower.getX(),
					this.lower.getY(),
					this.lower.getZ());
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
		return this.upper.getX() - this.lower.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getY() - this.lower.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		if (!this.isBoundInit) return Float.NaN;
		return this.upper.getZ() - this.lower.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		if (!this.isBoundInit) return null;
		return new Vector3f(this.upper.getX() - this.lower.getX(), this.upper.getY()
				- this.lower.getY(), this.upper.getZ() - this.lower.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("BoundingBox["); //$NON-NLS-1$
		sb.append(this.lower.getX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.lower.getY());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.lower.getZ());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getX());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getY());
		sb.append(", "); //$NON-NLS-1$
		sb.append(this.upper.getZ());
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

		minx = this.lower.getX();
		miny = this.lower.getY();
		minz = this.lower.getZ();
		maxx = this.upper.getX();
		maxy = this.upper.getY();
		maxz = this.upper.getZ();

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

		minx = this.lower.getX();
		miny = this.lower.getY();
		minz = this.lower.getZ();
		maxx = this.upper.getX();
		maxy = this.upper.getY();
		maxz = this.upper.getZ();

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
	public AlignedBoundingBox getNorthWestFrontVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				this.lower.getX(),midY,midZ,
				midX,this.upper.getY(),this.upper.getZ());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), left (max y), back (max x)
	 * corner.
	 * 
	 * @return the background upper-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getNorthWestBackVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				midX,midY,midZ,
				this.upper.getX(),this.upper.getY(),this.upper.getZ());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), right (min y), front (min x)
	 * corner.
	 * 
	 * @return the frontal upper-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getNorthEastFrontVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				this.lower.getX(),this.lower.getY(),midZ,
				midX,midY,this.upper.getZ());
	}

	/** Replies the voxel that corresponds to the
	 * upper (max z), right (min y), back (max x)
	 * corner.
	 * 
	 * @return the background upper-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getNorthEastBackVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				midX,this.lower.getY(),midZ,
				this.upper.getX(),midY,this.upper.getZ());
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), left (max y), front (min x)
	 * corner.
	 * 
	 * @return the frontal lower-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getSouthWestFrontVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				this.lower.getX(),midY,this.lower.getZ(),
				midX,this.upper.getY(),midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), left (max y), back (max x)
	 * corner.
	 * 
	 * @return the background lower-left quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getSouthWestBackVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				midX,midY,this.lower.getZ(),
				this.upper.getX(),this.upper.getY(),midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), right (min y), front (min x)
	 * corner.
	 * 
	 * @return the frontal lower-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getSouthEastFrontVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				this.lower.getX(),this.lower.getY(),this.lower.getZ(),
				midX,midY,midZ);
	}

	/** Replies the voxel that corresponds to the
	 * lower (min z), right (min y), back (max x)
	 * corner.
	 * 
	 * @return the background lower-right quarter of this box.
	 * @see CoordinateSystem3D#XYZ_RIGHT_HAND
	 */
	public AlignedBoundingBox getSouthEastBackVoxel() {
		float midX = (this.lower.getX()+this.upper.getX())/2.f;
		float midY = (this.lower.getY()+this.upper.getY())/2.f;
		float midZ = (this.lower.getZ()+this.upper.getZ())/2.f;
		return new AlignedBoundingBox(
				midX,this.lower.getY(),this.lower.getZ(),
				this.upper.getX(),midY,midZ);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f p) {
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
		float d3 = 0;
		if (p.getZ()<this.lower.getZ()) {
			d3 = this.lower.getZ() - p.getZ();
			d3 = d3*d3;
		}
		else if (p.getZ()>this.upper.getZ()) {
			d3 = p.getZ() - this.upper.getZ();
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

		if (reference.getZ()<this.lower.getZ()) {
			nearest.setZ(this.lower.getZ());
		}
		else if (reference.getZ()>this.upper.getZ()) {
			nearest.setZ(this.upper.getZ());
		}
		else {
			nearest.setZ(reference.getZ());
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
		
		float d3 = 0;
		if (p.getZ()<this.lower.getZ()) {
			d3 = this.upper.getZ() - p.getZ();
		}
		else if (p.getZ()>this.upper.getZ()) {
			d3 = p.getZ() - this.lower.getZ();
		}
		else d3 = Math.max(p.getZ()-this.lower.getZ(), this.upper.getZ()-p.getZ());
		
		return d1*d1+d2*d2+d3*d3;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		if (!isInit()) return null;
		EuclidianPoint3D farest = new EuclidianPoint3D();
		
		if (reference.getX()<this.lower.getX()) {
			farest.setX(this.upper.getX());
		}
		else if (reference.getX()>this.upper.getX()) {
			farest.setX(this.lower.getX());
		}
		else {
			float dl = Math.abs(reference.getX()-this.lower.getX());
			float du = Math.abs(reference.getX()-this.upper.getX());
			if (dl>du) {
				farest.setX(this.lower.getX());
			}
			else {
				farest.setX(this.upper.getX());
			}
		}

		if (reference.getY()<this.lower.getY()) {
			farest.setY(this.upper.getY());
		}
		else if (reference.getY()>this.upper.getY()) {
			farest.setY(this.lower.getY());
		}
		else {
			float dl = Math.abs(reference.getY()-this.lower.getY());
			float du = Math.abs(reference.getY()-this.upper.getY());
			if (dl>du) {
				farest.setY(this.lower.getY());
			}
			else {
				farest.setY(this.upper.getY());
			}
		}

		if (reference.getZ()<this.lower.getZ()) {
			farest.setZ(this.upper.getZ());
		}
		else if (reference.getZ()>this.upper.getZ()) {
			farest.setZ(this.lower.getZ());
		}
		else {
			float dl = Math.abs(reference.getZ()-this.lower.getZ());
			float du = Math.abs(reference.getZ()-this.upper.getZ());
			if (dl>du) {
				farest.setZ(this.lower.getZ());
			}
			else {
				farest.setZ(this.upper.getZ());
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
			this.lower.setX(this.lower.getX() + v.getX());
			this.lower.setY(this.lower.getY() + v.getY());
			this.lower.setZ(this.lower.getZ() + v.getZ());
			this.upper.setX(this.upper.getX() + v.getX());
			this.upper.setY(this.upper.getY() + v.getY());
			this.upper.setZ(this.upper.getZ() + v.getZ());
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
		return (this.lower.getX()<=p.getX() && p.getX()<=this.upper.getX()
				&&
				this.lower.getY()<=p.getY() && p.getY()<=this.upper.getY()
				&&
				this.lower.getZ()<=p.getZ() && p.getZ()<=this.upper.getZ())
				? IntersectionType.INSIDE
				: IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f p) {
		if (!isInit()) return false;
		return (this.lower.getX()<=p.getX() && p.getX()<=this.upper.getX()
				&&
				this.lower.getY()<=p.getY() && p.getY()<=this.upper.getY()
				&&
				this.lower.getZ()<=p.getZ() && p.getZ()<=this.upper.getZ());
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
				this.lower.getX(), this.lower.getY(), this.lower.getZ(),
				this.upper.getX(), this.upper.getY(), this.upper.getZ())== PlanarClassificationType.COINCIDENT)
			? IntersectionType.SPANNING : IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		if (!isInit()) return false;
		return plane.intersects(
				this.lower.getX(), this.lower.getY(), this.lower.getZ(),
				this.upper.getX(), this.upper.getY(), this.upper.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		if (!isInit()) return null;
		return plane.classifies(
				this.lower.getX(), this.lower.getY(), this.lower.getZ(),
				this.upper.getX(), this.upper.getY(), this.upper.getZ());
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
