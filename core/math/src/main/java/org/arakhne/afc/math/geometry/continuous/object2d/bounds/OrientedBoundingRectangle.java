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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.OrientedBoundingBox;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.util.CollectionUtil;


/**
 * Definition of a fixed Oriented Bounding Rectangle (OBR),
 * at least a 2D oriented bounding box.
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG)
 * and from 3D Game Engine Design (GED)
 * and from Real Time Collision Detection (RTCD).
 * <p>
 * Rotations are not managed yet.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see <a href="http://www.terathon.com/books/mathgames2.html">Mathematics for 3D Game Programming &amp; Computer Graphics</a>
 */
public class OrientedBoundingRectangle extends AbstractCombinableBounds2D
implements TranslatableBounds2D, OrientedCombinableBounds2D {	
	
	private static final long serialVersionUID = 4478594563705504363L;

	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param <P> is the type of the points.
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	public static <P extends Tuple2f> void computeOBRAxis(P[] points, Vector2f R, Vector2f S) {
		computeOBRAxis(Arrays.asList(points), R, S);
	}

	/**
	 * Compute the oriented bounding box axis.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the vector where the R axis of the OBR is put
	 * @param S is the vector where the S axis of the OBR is put
	 * @see "MGPCG pages 219-221"
	 */
	public static void computeOBRAxis(Iterable<? extends Tuple2f> points, Vector2f R, Vector2f S) {
		// Determining the covariance matrix of the points
		// and set the center of the box
		Matrix2f cov = new Matrix2f();
		MathUtil.cov(cov, points);
		
		//Determining eigenvectors of covariance matrix and defines RS axis
		Matrix2f rs = new Matrix2f();//eigenvectors                         
		
		MathUtil.eigenVectorsOfSymmetricMatrix(cov, rs);
		
		rs.getColumn(0,R);
		rs.getColumn(1,S);
	}
	
	/**
	 * Compute the OBR center and extents.
	 * 
	 * @param <P> is the type of the points.
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the R axis of the OBR
	 * @param S is the S axis of the OBR
	 * @param center is the point which is set with the OBR's center coordinates.
	 * @param extents are the extents of the OBR for the R and S axis.
	 * @see "MGPCG pages 222-223"
	 */
	public static <P extends Tuple2f> void computeOBRCenterExtents(
			P[] points,
			Vector2f R, Vector2f S,
			Point2f center, float[] extents) {
		computeOBRCenterExtents(Arrays.asList(points), R, S, center, extents);
	}

	/**
	 * Compute the OBR center and extents.
	 * 
	 * @param points is the list of the points enclosed by the OBR
	 * @param R is the R axis of the OBR
	 * @param S is the S axis of the OBR
	 * @param center is the point which is set with the OBR's center coordinates.
	 * @param extents are the extents of the OBR for the R and S axis.
	 * @see "MGPCG pages 222-223"
	 */
	public static void computeOBRCenterExtents(
			Iterable<? extends Tuple2f> points,
			Vector2f R, Vector2f S,
			Point2f center, float[] extents) {
		assert(points!=null);
		assert(center!=null);
		assert(extents!=null && extents.length>=2);
		
		float minR = Float.POSITIVE_INFINITY;
		float maxR = Float.NEGATIVE_INFINITY;
		float minS = Float.POSITIVE_INFINITY;
		float maxS = Float.NEGATIVE_INFINITY;
		
		float PdotR;
		float PdotS;
		Vector2f v = new Vector2f();
		
		for(Tuple2f tuple : points) {
			v.set(tuple);

			PdotR = v.dot(R);
			PdotS = v.dot(S);
			
			if (PdotR < minR) minR = PdotR;			
			if (PdotR > maxR) maxR = PdotR;			
			if (PdotS < minS) minS = PdotS;			
			if (PdotS > maxS) maxS = PdotS;			
		}
		
		float a = (maxR + minR) / 2.f;
		float b = (maxS + minS) / 2.f;
		
		// Set the center of the OBR
		center.set(
				a*R.getX()
				+b*S.getX(),
				
				a*R.getY()
				+b*S.getY());
		
		// Compute extents
		extents[0] = (maxR - minR) / 2.f;
		extents[1] = (maxS - minS) / 2.f;
	}

	private boolean isBoundInit;
	
	/**
	 * Center of the OBR
	 */
	EuclidianPoint2D center = new EuclidianPoint2D();
	
	/**
	 * Orthonormal set of vector composing an axis base
	 * <table>
	 * <tr><th>index</th><th>explanation</th></tr>
	 * <tr><td>0</td><td>R axis</td></tr>
	 * <tr><td>1</td><td>S axis</td></tr>
	 * </table>
	 */
	Vector2f[] axis = new Vector2f[2];
	
	/**
	 * Extent of the OBR, cannot be negative.
	 * <table>
	 * <tr><th>index</th><th>explanation</th></tr>
	 * <tr><td>0</td><td>extent in R axis</td></tr>
	 * <tr><td>1</td><td>extent in S axis</td></tr>
	 * </table>
	 */
	float[] extent = new float[2];
	
	/** Lower point of the axis-aligned bounding box.
	 */
	private transient EuclidianPoint2D aabbLower = null;
	
	/** Upper point of the axis-aligned bounding box.
	 */
	private transient EuclidianPoint2D aabbUpper = null;

	/**
	 * The various vertices that compose this OBR
	 */
	private transient Vector2f[] vertices = null;
	
	/**
	 * Build an empty OBR.
	 */
	public OrientedBoundingRectangle() {
		this.isBoundInit = false;
	}
	
	/**
	 * Build an OBR from the set of vertex that composes the corresponding object 2D.
	 * 
	 * @param vertices
	 */
	public OrientedBoundingRectangle(Tuple2f... vertices) {
		this.isBoundInit = false;
		combinePoints(false, Arrays.asList(vertices));
	}

	/**
	 * Build an OBR.
	 * The given parameters are copied.
	 * 
	 * @param center
	 * @param axis
	 * @param extent
	 */
	public OrientedBoundingRectangle(Point2f center, Vector2f[] axis, float[] extent) {
		assert(center!=null);
		assert(axis!=null);
		assert(axis[0]!=null);
		assert(axis[1]!=null);
		assert(extent!=null);
		assert(extent[0]>=0.);
		assert(extent[1]>=0.);
		this.center.set(center);
		for(int i=0; i<2; ++i) {
			this.axis[i] = new Vector2f(axis[i]);
			this.extent[i] = extent[i];
		}
		this.isBoundInit = true;
	}

	/**
	 * Build an OBR.
	 * The given parameters are copied.
	 * 
	 * @param center
	 * @param axis
	 * @param extent
	 */
	public OrientedBoundingRectangle(Point2f center, Vector2f[] axis, Tuple2f extent) {
		assert(center!=null);
		assert(axis!=null);
		assert(axis[0]!=null);
		assert(axis[1]!=null);
		assert(extent!=null);
		this.center.set(center);
		for(int i=0; i<2; ++i) {
			this.axis[i] = new Vector2f(axis[i]);
		}
		this.extent[0] = extent.getX();
		this.extent[1] = extent.getY();
		this.isBoundInit = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrientedBoundingRectangle clone() {
		OrientedBoundingRectangle clone = (OrientedBoundingRectangle)super.clone();
		clone.axis = new Vector2f[this.axis.length];
		for(int i=0; i<this.axis.length; ++i) {
			clone.axis[i] = this.axis[i].clone();
		}
		clone.center = (EuclidianPoint2D)this.center.clone();
		clone.extent = this.extent.clone();

		return clone;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType2D getBoundType() {
		return BoundingPrimitiveType2D.ORIENTED_RECTANGLE;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!isInit()) return false;
		if (this==o) return true;
		if (o instanceof OrientedBoundingRectangle) {
			OrientedBoundingRectangle s = (OrientedBoundingRectangle)o;
			if (this.center.equals(s.center)) {
				for(int i=0; i<this.axis.length; ++i) {
					if (!this.axis[i].equals(s.axis[i]))
						return false;
				}
				for(int i=0; i<this.extent.length; ++i) {
					if (this.extent[i]!=s.extent[i])
						return false;
				}
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D() {
		return toBounds3D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D(float z, float zsize) {
		return toBounds3D(z, zsize, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D(CoordinateSystem3D system) {
		if (!isInit()) return new OrientedBoundingBox();		
		Vector3f vx = system.fromCoordinateSystem2D(this.axis[0]);
		Vector3f vy = system.fromCoordinateSystem2D(this.axis[1]);
		Vector3f vz;
		if (system.isLeftHanded())
			vz = MathUtil.crossProductLeftHand(vx.getX(), vx.getY(), vx.getZ(), vy.getX(), vy.getY(), vy.getZ());
		else
			vz = MathUtil.crossProductRightHand(vx.getX(), vx.getY(), vx.getZ(), vy.getX(), vy.getY(), vy.getZ());
		return new OrientedBoundingBox(
				system.fromCoordinateSystem2D(this.center),
				new Vector3f[] {
					vx, vy, vz,
				},
				system.fromCoordinateSystem2D(getOrientedBoundExtentVector()));
	}

	/** {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox toBounds3D(float z, float zsize, CoordinateSystem3D system) {
		if (!isInit()) return new OrientedBoundingBox();		
		Vector3f vx = system.fromCoordinateSystem2D(this.axis[0]);
		Vector3f vy = system.fromCoordinateSystem2D(this.axis[1]);
		Vector3f vz;
		if (system.isLeftHanded())
			vz = MathUtil.crossProductLeftHand(vx.getX(), vx.getY(), vx.getZ(), vy.getX(), vy.getY(), vy.getZ());
		else
			vz = MathUtil.crossProductRightHand(vx.getX(), vx.getY(), vx.getZ(), vy.getX(), vy.getY(), vy.getZ());
		return new OrientedBoundingBox(
				system.fromCoordinateSystem2D(this.center, z),
				new Vector3f[] {
					vx, vy, vz,
				},
				system.fromCoordinateSystem2D(getOrientedBoundExtentVector(), Math.abs(zsize/2.f)));
	}

	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector2f> getLocalOrientedBoundVertices() {
		if (this.vertices==null) {
			computeVertices();
		}
		return ArrayUtil.sizedIterator(this.vertices);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getLocalVertexAt(int index) {
		if (this.vertices==null) {
			computeVertices();
		}
		return this.vertices[index];
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		if (this.vertices==null) {
			computeVertices();
		}
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
		if (this.vertices==null) {
			computeVertices();
		}
		EuclidianPoint2D p = new EuclidianPoint2D(getCenter());
		p.add(this.vertices[index]);
		return p;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f[] getOrientedBoundAxis() {
		return this.axis;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getOrientedBoundExtentVector() {
		return new Vector2f(this.extent[0], this.extent[1]);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return this.extent;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getR() {
		return this.axis[0];
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getS() {
		return this.axis[1];
	}

	/** {@inheritDoc}
	 */
	public float[] getExtent() {
		return this.extent;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return this.extent[0];
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return this.extent[1];
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getCenter() {
		return this.center;
	}
	
	private void computeVertices() {
		this.vertices = new Vector2f[4];

		Vector2f Rpos = new Vector2f(this.axis[0]);
		Rpos.scale(this.extent[0]);
		Vector2f Rneg = new Vector2f(Rpos);
		Rneg.negate();
		Vector2f Spos = new Vector2f(this.axis[1]);
		Spos.scale(this.extent[1]);
		Vector2f Sneg = new Vector2f(Spos);
		Sneg.negate();
		
		this.vertices[0].add(Rpos, Spos);
		this.vertices[1].add(Rneg, Spos);
		this.vertices[2].add(Rneg, Sneg);
		this.vertices[3].add(Rpos, Sneg);
		
		this.aabbLower = new EuclidianPoint2D();
		this.aabbUpper = new EuclidianPoint2D();
		for(int i=0; i<this.vertices.length; ++i) {
			if (this.vertices[i].getX()<this.aabbLower.getX()) this.aabbLower.setX(this.vertices[i].getX());
			if (this.vertices[i].getY()<this.aabbLower.getY()) this.aabbLower.setY(this.vertices[i].getY());
			if (this.vertices[i].getX()>this.aabbUpper.getX()) this.aabbUpper.setX(this.vertices[i].getX());
			if (this.vertices[i].getY()>this.aabbUpper.getY()) this.aabbUpper.setY(this.vertices[i].getY());
		}
		
		Point2f Tcenter = new Point2f(0,0);
		Vector2f vide = new Vector2f(0,0);
		
		this.center.toTuple2f(Tcenter);
		
		this.aabbLower.add(Tcenter, vide);
		this.aabbUpper.add(Tcenter, vide);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point2f lower, Point2f upper) {
		assert(lower!=null);
		assert(upper!=null);
		if (this.aabbLower==null || this.aabbUpper==null) {
			computeVertices();
		}
		lower.set(this.aabbLower);
		upper.set(this.aabbUpper);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getLower() {
		if (this.aabbLower==null) {
			computeVertices();
		}
		return this.aabbLower;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getUpper() {
		if (this.aabbUpper==null) {
			computeVertices();
		}
		return this.aabbUpper;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2f p) {
		if (!isInit()) return Float.NaN;		
		Vector2f v = new Vector2f();
		v.sub(p,this.center);//p - this.center;
		float sqDist = 0.f;
		float d, excess;
		for (int i=0; i<2; ++i) {
			// Project vector from box center to p on each axis, getting the distance
			// of p along that axis, and count any excess distance outside box extents
			d = v.dot(this.axis[i]);
			excess = 0.f;
			if (d < -this.extent[i]) {
				excess = -this.extent[i] - d;
			}
			else if (d > this.extent[i]) {
				excess = d - this.extent[i];
			}
			sqDist += excess * excess;
		}
		return sqDist;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D nearestPoint(Point2f reference) {
		if (!isInit()) return null;		
		EuclidianPoint2D q = new EuclidianPoint2D();
		IntersectionUtil.closestFarthestPointsOBRPoint(
				this.center, this.axis, this.extent,
				reference, q, null);
		return q;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point2f p) {
		if (!isInit()) return Float.NaN;		
		Vector2f v = new Vector2f();
		v.sub(p,this.center);//p - this.center;
		float sqDist = 0.f;
		float d,excess;
		for (int i = 0; i < 2; ++i) {
			// Project vector from box center to p on each axis, getting the distance
			// of p along that axis, and count any excess distance outside box extents
			d = v.dot(this.axis[i]);
			if (d < 0.) {
				excess = this.extent[i] - d;
			}
			else {
				excess = d + this.extent[i];
			}
			sqDist += excess * excess;
		}
		return sqDist;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D farestPoint(Point2f reference) {
		if (!isInit()) return null;		
		EuclidianPoint2D q = new EuclidianPoint2D();
		IntersectionUtil.closestFarthestPointsOBRPoint(
				this.center, this.axis, this.extent,
				reference, null, q);
		return q;		
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {		
		return !isInit() || this.extent[0]<=0.
				||this.extent[1]<=0;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	//----------------------------------------------
	// CombinableBounds
	//----------------------------------------------
	
	/**
	 * Set this oriented bounding rectangle according to the given
	 * points and the given predefined R vector.
	 * <p>
	 * The parameter <var>predefinedR</var> is used as the main 
	 * direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param expectedR is the desired main direction of the rectangle.
	 * @param points is the list of the points which may be enclosed by this rectangle.
	 */
	public final void set(Vector2f expectedR, Tuple2f... points) {
		combinePoints(expectedR, false, Arrays.asList(points));
	}

	/**
	 * Set this oriented bounding rectangle according to the given
	 * points and the given predefined R vector.
	 * <p>
	 * The parameter <var>predefinedR</var> is used as the main 
	 * direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param expectedR is the desired main direction of the rectangle.
	 * @param points is the list of the points which may be enclosed by this rectangle.
	 */
	public final void set(Vector2f expectedR, Collection<? extends Tuple2f> points) {
		combinePoints(expectedR, false, points);
	}

	/**
	 * Set this oriented bounding rectangle according to the given
	 * bounds and the given predefined R vector.
	 * <p>
	 * The parameter <var>predefinedR</var> is used as the main 
	 * direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param expectedR is the desired main direction of the rectangle.
	 * @param bounds is the list of the bounds which may be enclosed by this rectangle.
	 */
	public final void setFromBounds(Vector2f expectedR, Collection<? extends Bounds2D> bounds) {
		combineBounds(expectedR, false, bounds);
	}

	/**
	 * Set this oriented bounding rectangle according to the given
	 * bounds and the given predefined R vector.
	 * <p>
	 * The parameter <var>predefinedR</var> is used as the main 
	 * direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param expectedR is the desired main direction of the rectangle.
	 * @param bounds is the list of the bounds which may be enclosed by this rectangle.
	 */
	public final void setFromBounds(Vector2f expectedR, Bounds2D... bounds) {
		combineBounds(expectedR, false, Arrays.asList(bounds));
	}

	/**
	 * Compute the oriented bounding box from the set of vertices and
	 * the associated axis-aligned bounding box.
	 * <p>
	 * If the parameter <var>predefinedR</var> is not <code>null</code>,
	 * it will be used as the main direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param predefinedR is the desired main direction of the rectangle, if given. If not
	 * given, R will be computed and selected.
	 * @param vertexList
	 * @see MGPCG pages 219-221
	 */
	private void computeBoundingBoxes(
			Vector2f predefinedR,
			Iterable<? extends Tuple2f> vertexList) {
		// Determining the OBR axis
		if (this.axis==null || this.axis.length!=2) this.axis = new Vector2f[2];
		for(int i=0; i<2; ++i) {
			if (this.axis[i]==null) this.axis[i] = new Vector2f();
		}
		
		if (predefinedR==null || predefinedR.lengthSquared()==0.) {
			computeOBRAxis(vertexList, this.axis[0], this.axis[1]);
		}
		else {
			this.axis[0].set(predefinedR);
			this.axis[0].normalize();
			MathUtil.perpendicularVector(this.axis[0], this.axis[1]);
			this.axis[1].normalize();
		}
		
		//Extent and center computation
		computeOBRCenterExtents(vertexList, this.axis[0], this.axis[1],
				this.center, this.extent);

		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
		this.isBoundInit = true;
	}
	
	/**
	 * Add the points into the bounds.
	 *
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param pointList are the points used to update the bounding box coordinates 
	 */	
	@Override
	protected void combinePoints(boolean isInit, Collection<? extends Tuple2f> pointList) {
		combinePoints(null, isInit, pointList);		
	}

	/**
	 * Add the points into the bounds.
	 * <p>
	 * If the parameter <var>predefinedR</var> is not <code>null</code>,
	 * it will be used as the main direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param predefinedR is the desired main direction of the rectangle, if given. If not
	 * given, R will be computed and selected.
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param pointList are the points used to update the bounding box coordinates 
	 */	
	protected void combinePoints(Vector2f predefinedR, boolean isInit, Collection<? extends Tuple2f> pointList) {
		if (!isInit) {
			computeBoundingBoxes(predefinedR, pointList);
		}
		else {
			computeBoundingBoxes(predefinedR, new MergedPointList(pointList));
		}		
	}

	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param bounds are the bounds used to update the bounding box coordinates 
	 */
	@Override
	protected void combineBounds(boolean isInit, Collection<? extends Bounds2D> bounds) {
		combineBounds(null, isInit, bounds);
	}
	
	/**
	 * Add the bounds into the bounds.
	 * <p>
	 * If the parameter <var>predefinedR</var> is not <code>null</code>,
	 * it will be used as the main direction of the rectangle.
	 * Consequently, both R and S vectors are not computed according
	 * to the given list of vertices, and S will be perpendicular to 
	 * this predefined R.
	 * 
	 * @param predefinedR is the desired main direction of the rectangle, if given. If not
	 * given, R will be computed and selected.
	 * 
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param bounds are the bounds used to update the bounding box coordinates 
	 */
	protected void combineBounds(Vector2f predefinedR, boolean isInit, Collection<? extends Bounds2D> bounds) {
		if (!isInit) { 
			if (bounds.size()==1) {
				Bounds2D cb = CollectionUtil.firstElement(bounds);
				if (cb instanceof OrientedBoundingRectangle) {
					OrientedBoundingRectangle obr = (OrientedBoundingRectangle)cb;
					this.center = (EuclidianPoint2D)obr.center.clone();
					for(int i=0; i<2; ++i) {
						this.axis[i] = obr.axis[i].clone();
						this.extent[i] = obr.extent[i];
					}
				}
				else if (cb instanceof BoundingCircle) {
					BoundingCircle sphere = (BoundingCircle)cb;
					this.center = (EuclidianPoint2D)sphere.center.clone();
					this.axis[0] = new Vector2f(1.,0.);
					this.axis[1] = new Vector2f(0.,1.);
					for(int i=0; i<2; ++i) {
						this.extent[i] = sphere.radius;
					}
				}
				else if (cb instanceof MinimumBoundingRectangle) {
					MinimumBoundingRectangle aabb = (MinimumBoundingRectangle)cb;
					this.center = (EuclidianPoint2D)aabb.getCenter().clone();
					this.axis[0] = new Vector2f(1.,0.);
					this.axis[1] = new Vector2f(0.,1.);
					this.extent[0] = aabb.getSizeX() / 2.f;
					this.extent[1] = aabb.getSizeY() / 2.f;
				}
				else if (cb instanceof ComposedBounds2D) {
					ComposedBounds2D ccb = (ComposedBounds2D)cb;
					computeBoundingBoxes(predefinedR, new MergedBoundList(ccb));
				}
			}
			else {
				computeBoundingBoxes(predefinedR, new MergedBoundList(bounds));
			}
		}
		else {
			computeBoundingBoxes(predefinedR, new MergedBoundList(bounds, this));
		}
		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
		this.isBoundInit = true;
	}

	/**
	 * Reset the content of this bounds
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.center.set(0,0);
			
			for(int i=0;i<2;++i) {
				this.axis[i].set(0,0);
				this.extent[i] = 0;
			}	
			
			this.vertices = null;
			this.aabbLower = this.aabbUpper = null;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit) return Float.NaN;
		if (this.aabbLower==null || this.aabbUpper==null)
			computeVertices();
		return this.aabbUpper.getX() - this.aabbLower.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		if (this.aabbLower==null || this.aabbUpper==null)
			computeVertices();
		return this.aabbUpper.getY() - this.aabbLower.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getSize() {
		if (!this.isBoundInit) return null;
		if (this.aabbLower==null || this.aabbUpper==null)
			computeVertices();
		return new Vector2f(
				this.aabbUpper.getX() - this.aabbLower.getX(),
				this.aabbUpper.getY() - this.aabbLower.getY());
	}

	//--------------------------------------------------------
	// TranslatableBounds
	//--------------------------------------------------------

	@Override
	public void translate(Vector2f v) {
		if (isInit()) {
			this.center.add(v);
			if (this.aabbLower!=null) this.aabbLower.add(v);
			if (this.aabbUpper!=null) this.aabbUpper.add(v);
			if (this.vertices!=null) {
				for(Vector2f vv : this.vertices) {
					if (vv!=null) {
						vv.add(v);
					}
				}
			}
		}
	}

	//--------------------------------------------------------
	// IntersectionClassifier
	//--------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f p) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		Vector2f d = new Vector2f();
		d.sub(p, this.center); // d = p - center

		// For each OBR axis...
		float dist;
		for (int i=0; i<this.axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			dist = d.dot(this.axis[i]);
			// If distance farther than the box extents, return OUTSIDE
			if (Math.abs(dist) > this.extent[i]) return IntersectionType.OUTSIDE;
		}
		
		return IntersectionType.INSIDE;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f p) {
		if (!isInit()) return false;		
		Vector2f d = new Vector2f();
		d.sub(p, this.center); // d = p - center
			
		// For each OBR axis...
		float dist;
		for (int i=0; i<this.axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			dist = d.dot(this.axis[i]);
			// If distance farther than the box extents, return OUTSIDE
			if (Math.abs(dist) > this.extent[i]) return false;
		}
		
		return true;	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f c, float r) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesSolidCircleOrientedRectangle(
				c, r, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f c, float r) {
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsSolidCircleOrientedRectangle(
				c, r, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f l, Point2f u) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesAlignedRectangleOrientedRectangle(
				l, u, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f l, Point2f u) {
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsAlignedRectangleOrientedRectangle(
				l, u, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f obbCenter, Vector2f[] obbAxis, float[] obbExtent) {
		if (!isInit()) return IntersectionType.OUTSIDE;		
		return IntersectionUtil.classifiesOrientedRectangles(
				obbCenter, obbAxis, obbExtent,
				this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f obbCenter, Vector2f[] obbAxis, float[] obbExtent) {
		if (!isInit()) return false;		
		return IntersectionUtil.intersectsOrientedRectangles(
				obbCenter, obbAxis, obbExtent,
				this.center, this.axis, this.extent);
	}

	/**
	 * An iterable on the OBR vertices and on a list of points.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class MergedPointList implements Iterable<Tuple2f> {
		
		private final Collection<? extends Tuple2f> points; 
		
		/** 
		 * @param pointList is a list of point to merge inside the iterable.
		 */
		public MergedPointList(Collection<? extends Tuple2f> pointList) {
			this.points = pointList;
		}

		@Override
		public Iterator<Tuple2f> iterator() {
			return CollectionUtil.mergeIterators(
					getGlobalOrientedBoundVertices(),
					CollectionUtil.sizedIterator(this.points));
		}

	}
	
	/**
	 * An iterable on vertices which is merging a collection of 
	 * bounds and optionnally an oriented bounding box.
	 * 
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class MergedBoundList implements Iterable<Tuple2f> {
		
		private final Collection<? extends Bounds2D> bounds;
		private final OrientedBoundingRectangle obr;
		
		/** 
		 * @param bounds are the list of bounds to merge inside
		 */
		public MergedBoundList(Collection<? extends Bounds2D> bounds) {
			this.bounds = bounds;
			this.obr = null;
		}

		/** 
		 * @param bounds are the list of bounds to merge inside
		 * @param OBR is the OBR to put back in the merging list.
		 */
		public MergedBoundList(Collection<? extends Bounds2D> bounds, OrientedBoundingRectangle OBR) {
			this.bounds = bounds;
			this.obr = OBR;
		}

		@Override
		public Iterator<Tuple2f> iterator() {
			Iterator<Tuple2f> iterator = new ExtractionIterator(this.bounds.iterator());

			if (this.obr==null) return iterator;

			return CollectionUtil.mergeIterators(iterator, this.obr.getGlobalOrientedBoundVertices());
		}

		/**
		 * An iterable on vertices which is merging a collection of 
		 * bounds and optionnally an oriented bounding box.
		 * 
		 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
		 * @version $Name$ $Revision$ @mavengroupid fr.utbm.set.sfc fr.utbm.set.sfc
		 * @mavenartifactid setmath
		 */
		private static class ExtractionIterator implements Iterator<Tuple2f> {

			private final Iterator<? extends Bounds2D> bounds;
			private LinkedList<Bounds2D> subBounds;
			private Iterator<EuclidianPoint2D> vertices;
			private Tuple2f next;
			
			/**
			 * @param bounds
			 */
			public ExtractionIterator(Iterator<? extends Bounds2D> bounds) {
				this.bounds = bounds;
				searchNext();
			}
			
			private void searchNext() {
				this.next = null;
				while (this.vertices==null || !this.vertices.hasNext()) {
					Bounds2D cb;
					this.vertices = null;
					while ((this.subBounds!=null && !this.subBounds.isEmpty())
						   || this.bounds.hasNext()) {
						cb = this.bounds.next();
						if (cb.isInit()) {
							if (cb instanceof OrientedBounds2D) {
								this.vertices = ((OrientedBounds2D)cb).getGlobalOrientedBoundVertices();
								break;
							}
							else if (cb instanceof ComposedBounds2D) {
								if (this.subBounds==null)
									this.subBounds = new LinkedList<Bounds2D>();
								this.subBounds.addAll((ComposedBounds2D)cb);
							}
						}
					}
					if (this.subBounds!=null && this.subBounds.isEmpty())
						this.subBounds = null;
					if (this.vertices==null) return; // No more vertex
				}
				
				// One vertex exists!
				this.next = this.vertices.next();
			}

			@Override
			public boolean hasNext() {
				return this.next!=null;
			}

			@Override
			public Tuple2f next() {
				if (this.next==null) throw new NoSuchElementException();
				Tuple2f n = this.next;
				searchNext();
				return n;
			}

			@Override
			public void remove() {
				//
			}
			
		}
		
	}

}
