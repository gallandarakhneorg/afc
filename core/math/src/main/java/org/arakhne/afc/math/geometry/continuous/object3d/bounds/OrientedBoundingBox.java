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

package org.arakhne.afc.math.geometry.continuous.object3d.bounds;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.continuous.PointFilterXYIterable;
import org.arakhne.afc.math.geometry.continuous.PointFilterXZIterable;
import org.arakhne.afc.math.geometry.continuous.PointFilterYZIterable;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.OrientedBoundingRectangle;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Quaternion;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.PlanarClassificationType;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.Plane;
import org.arakhne.afc.math.geometry.continuous.object4d.AxisAngle4f;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;
import org.arakhne.afc.util.CollectionUtil;

/**
 * Definition of a fixed Oriented Bounding Box (OBB).
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG) and from 3D Game Engine Design (GED) and from Real Time Collision Detection (RTCD).
 * <p>
 * Rotations are not managed yet.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see <a href="http://www.terathon.com/books/mathgames2.html">Mathematics for 3D Game Programming &amp; Computer Graphics</a>
 */
public class OrientedBoundingBox extends AbstractCombinableBounds3D implements TranslatableBounds3D, RotatableBounds3D, OrientedCombinableBounds3D {

	private static final long serialVersionUID = 5092008376628616496L;

	/**
	 * Compute the oriented bounding box center, axis and extent.
	 * 
	 * @param <P>
	 *            is the type of the points.
	 * @param points
	 *            is the list of the points enclosed by the OBB
	 * @param R
	 *            is the vector where the R axis of the OBB is put
	 * @param S
	 *            is the vector where the S axis of the OBB is put
	 * @param T
	 *            is the the vector where T axis of the OBB is put
	 * @param center
	 *            is the point which is set with the OBB's center coordinates.
	 * @param extents
	 *            are the extents of the OBB for the R, S and T axis.
	 */
	public static <P extends Tuple3f> void computeOBBCenterAxisExtents(P[] points, Vector3f R, Vector3f S, Vector3f T, Point3f center, float[] extents) {
		computeOBBCenterAxisExtents(Arrays.asList(points), R, S, T, center, extents);
	}

	/**
	 * Compute the oriented bounding box center, axis and extent.
	 * 
	 * @param points
	 *            is the list of the points enclosed by the OBB
	 * @param R
	 *            is the vector where the R axis of the OBB is put
	 * @param S
	 *            is the vector where the S axis of the OBB is put
	 * @param T
	 *            is the the vector where T axis of the OBB is put
	 * @param center
	 *            is the point which is set with the OBB's center coordinates.
	 * @param extents
	 *            are the extents of the OBB for the R, S and T axis.
	 */
	public static void computeOBBCenterAxisExtents(Iterable<? extends Tuple3f> points, Vector3f R, Vector3f S, Vector3f T, Point3f center, float[] extents) {
		assert (points != null);
		assert (center != null);
		assert (extents != null && extents.length >= 3);

		Vector3f vecNull = new Vector3f();
		if (R.equals(vecNull) && S.equals(vecNull) && T.equals(vecNull)) {
			// Determining the covariance matrix of the points
			// and set the center of the box
			Matrix3f cov = new Matrix3f();
			MathUtil.cov(cov, points);

			// Determining eigenvectors of covariance matrix and defines RST axis
			Matrix3f rst = new Matrix3f();// eigenvectors

			MathUtil.eigenVectorsOfSymmetricMatrix(cov, rst);
			rst.getColumn(0, R);
			rst.getColumn(1, S);
			rst.getColumn(2, T);
		}//else we have already set some constraints on OBB axis, dosen't need to compute them again 

		float minR = Float.POSITIVE_INFINITY;
		float maxR = Float.NEGATIVE_INFINITY;
		float minS = Float.POSITIVE_INFINITY;
		float maxS = Float.NEGATIVE_INFINITY;
		float minT = Float.POSITIVE_INFINITY;
		float maxT = Float.NEGATIVE_INFINITY;

		float PdotR;
		float PdotS;
		float PdotT;
		Vector3f v = new Vector3f();

		for (Tuple3f tuple : points) {
			v.set(tuple);

			PdotR = v.dot(R);
			PdotS = v.dot(S);
			PdotT = v.dot(T);

			if (PdotR < minR)
				minR = PdotR;
			if (PdotR > maxR)
				maxR = PdotR;
			if (PdotS < minS)
				minS = PdotS;
			if (PdotS > maxS)
				maxS = PdotS;
			if (PdotT < minT)
				minT = PdotT;
			if (PdotT > maxT)
				maxT = PdotT;
		}

		float a = (maxR + minR) / 2.f;
		float b = (maxS + minS) / 2.f;
		float c = (maxT + minT) / 2.f;

		// Set the center of the OBB
		center.set(a * R.getX() + b * S.getX() + c * T.getX(),

		a * R.getY() + b * S.getY() + c * T.getY(),

		a * R.getZ() + b * S.getZ() + c * T.getZ());

		// Compute extents
		extents[0] = (maxR - minR) / 2.f;
		extents[1] = (maxS - minS) / 2.f;
		extents[2] = (maxT - minT) / 2.f;

		// Normalize axis
		R.normalize();
		S.normalize();
		T.normalize();

		// Selection with largest magnitude eigenvalue to identify R
		int max = -1;
		for (int i = 0; i < 3; i++) {
			if (extents[i] > max) {
				max = i;
			}
		}
		switch (max) {
		case 0: {
			// do nothing, right order
		}
			break;

		case 1: {
			Vector3f tmpVec = new Vector3f(R);
			R.set(S);
			S.set(T);
			T.set(tmpVec);

			float tmpD = extents[0];
			extents[0] = extents[1];
			extents[1] = extents[2];
			extents[2] = tmpD;
		}
			break;
		case 2: {
			Vector3f tmpVec = new Vector3f(S);
			S.set(R);
			R.set(T);
			T.set(tmpVec);

			float tmpD = extents[1];
			extents[1] = extents[0];
			extents[0] = extents[2];
			extents[2] = tmpD;

		}
			break;
		}
	}

	private boolean isBoundInit;

	/**
	 * Center of the OBB
	 */
	EuclidianPoint3D center = new EuclidianPoint3D();

	/**
	 * Orthonormal set of vector composing an axis base
	 * <table>
	 * <tr>
	 * <th>index</th>
	 * <th>explanation</th>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>R axis</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>S axis</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>T axis</td>
	 * </tr>
	 * </table>
	 */
	Vector3f[] axis = new Vector3f[3];

	/**
	 * Extent of the OBB, cannot be negative.
	 * <table>
	 * <tr>
	 * <th>index</th>
	 * <th>explanation</th>
	 * </tr>
	 * <tr>
	 * <td>0</td>
	 * <td>extent in R axis</td>
	 * </tr>
	 * <tr>
	 * <td>1</td>
	 * <td>extent in S axis</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>extent in T axis</td>
	 * </tr>
	 * </table>
	 */
	float[] extent = new float[3];

	/**
	 * Lower point of the axis-aligned bounding box.
	 */
	private transient EuclidianPoint3D aabbLower = null;

	/**
	 * Upper point of the axis-aligned bounding box.
	 */
	private transient EuclidianPoint3D aabbUpper = null;

	/**
	 * The various vertices that compose this OBB
	 */
	private transient Vector3f[] vertices = null;

	/**
	 * Build an empty OBB.
	 */
	public OrientedBoundingBox() {
		this.isBoundInit = false;
	}

	/**
	 * Build an OBB from the set of vertex that composes the corresponding object 3D.
	 * 
	 * @param vertices
	 */
	public OrientedBoundingBox(Tuple3f... vertices) {
		this.isBoundInit = false;
		combinePoints(false, Arrays.asList(vertices));
	}

	/**
	 * Build an OBB from the set of vertex that composes the corresponding object 3D.
	 * 
	 * @param vertices
	 */
	public OrientedBoundingBox(Collection<? extends Tuple3f> vertices) {
		this.isBoundInit = false;
		combinePoints(false, vertices);
	}

	/**
	 * Build an OBB. The given parameters are copied.
	 * 
	 * @param center
	 * @param axis
	 * @param extent
	 */
	public OrientedBoundingBox(Point3f center, Vector3f[] axis, float[] extent) {
		assert (center != null);
		assert (axis != null);
		assert (axis[0] != null);
		assert (axis[1] != null);
		assert (axis[2] != null);
		assert (extent != null);
		assert (extent[0] >= 0.);
		assert (extent[1] >= 0.);
		assert (extent[2] >= 0.);
		this.center.set(center);
		for (int i = 0; i < 3; ++i) {
			this.axis[i] = new Vector3f(axis[i]);
			this.extent[i] = extent[i];
		}
		this.isBoundInit = true;
	}

	/**
	 * Build an OBB. The given parameters are copied.
	 * 
	 * @param center
	 * @param axis
	 * @param extent
	 */
	public OrientedBoundingBox(Point3f center, Vector3f[] axis, Tuple3f extent) {
		assert (center != null);
		assert (axis != null);
		assert (axis[0] != null);
		assert (axis[1] != null);
		assert (axis[2] != null);
		assert (extent != null);
		this.center.set(center);
		for (int i = 0; i < 3; ++i) {
			this.axis[i] = new Vector3f(axis[i]);
		}
		this.extent[0] = extent.getX();
		this.extent[1] = extent.getY();
		this.extent[2] = extent.getZ();
		this.isBoundInit = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType3D getBoundType() {
		return BoundingPrimitiveType3D.ORIENTED_BOX;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("OBB "); //$NON-NLS-1$
		s.append("center: "); //$NON-NLS-1$
		s.append(this.center.toString());
		s.append(" axis: "); //$NON-NLS-1$
		s.append(this.axis.toString());
		s.append(" extent: "); //$NON-NLS-1$
		s.append(this.extent.toString());
		return s.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrientedBoundingBox clone() {
		OrientedBoundingBox clone = (OrientedBoundingBox) super.clone();
		clone.axis = new Vector3f[this.axis.length];
		for (int i = 0; i < this.axis.length; ++i) {
			clone.axis[i] = (Vector3f) this.axis[i].clone();
		}
		clone.center = (EuclidianPoint3D) this.center.clone();
		clone.extent = this.extent.clone();

		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrientedBoundingRectangle toBounds2D() {
		return toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrientedBoundingRectangle toBounds2D(CoordinateSystem3D system) {
		if (!isInit())
			return new OrientedBoundingRectangle();
		SizedIterator<EuclidianPoint3D> pts = getGlobalOrientedBoundVertices();
		Point2f[] points = new Point2f[pts.totalSize()];
		for (int i = 0; pts.hasNext(); ++i) {
			points[i] = system.toCoordinateSystem2D(pts.next());
		}
		return new OrientedBoundingRectangle(points);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!isInit())
			return false;
		if (this == o)
			return true;
		if (o instanceof OrientedBoundingBox) {
			OrientedBoundingBox s = (OrientedBoundingBox) o;
			if (this.center.equals(s.center)) {
				for (int i = 0; i < this.axis.length; ++i) {
					if (!this.axis[i].equals(s.axis[i]))
						return false;
				}
				for (int i = 0; i < this.extent.length; ++i) {
					if (this.extent[i] != s.extent[i])
						return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector3f> getLocalOrientedBoundVertices() {
		if (this.vertices == null) {
			computeVertices();
		}
		return ArrayUtil.sizedIterator(this.vertices);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getLocalVertexAt(int index) {
		if (this.vertices == null) {
			computeVertices();
		}
		return this.vertices[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		if (this.vertices == null) {
			computeVertices();
		}
		return this.vertices.length;
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
	public EuclidianPoint3D getGlobalVertexAt(int index) {
		if (this.vertices == null) {
			computeVertices();
		}
		EuclidianPoint3D p = new EuclidianPoint3D(getCenter());
		p.add(this.vertices[index]);
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f[] getOrientedBoundAxis() {
		return this.axis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getOrientedBoundExtentVector() {
		return new Vector3f(this.extent[0], this.extent[1], this.extent[2]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return this.extent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getR() {
		return this.axis[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getS() {
		return this.axis[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getT() {
		return this.axis[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return this.extent[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return this.extent[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getTExtent() {
		return this.extent[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		return this.center;
	}

	private void computeVertices() {
		this.vertices = new Vector3f[8];

		Vector3f Rpos = new Vector3f(this.axis[0]);
		Rpos.scale(this.extent[0]);
		Vector3f Rneg = new Vector3f(Rpos);
		Rneg.negate();
		Vector3f Spos = new Vector3f(this.axis[1]);
		Spos.scale(this.extent[1]);
		Vector3f Sneg = new Vector3f(Spos);
		Sneg.negate();
		Vector3f Tpos = new Vector3f(this.axis[2]);
		Tpos.scale(this.extent[2]);
		Vector3f Tneg = new Vector3f(Tpos);
		Tneg.negate();

		this.vertices[0] = MathUtil.add(Rpos, Spos, Tpos);
		this.vertices[1] = MathUtil.add(Rneg, Spos, Tpos);
		this.vertices[2] = MathUtil.add(Rneg, Sneg, Tpos);
		this.vertices[3] = MathUtil.add(Rpos, Sneg, Tpos);
		this.vertices[4] = MathUtil.add(Rpos, Sneg, Tneg);
		this.vertices[5] = MathUtil.add(Rpos, Spos, Tneg);
		this.vertices[6] = MathUtil.add(Rneg, Spos, Tneg);
		this.vertices[7] = MathUtil.add(Rneg, Sneg, Tneg);

		this.aabbLower = new EuclidianPoint3D();
		this.aabbUpper = new EuclidianPoint3D();
		for (int i = 0; i < this.vertices.length; ++i) {
			if (this.vertices[i].getX() < this.aabbLower.getX())
				this.aabbLower.setX(this.vertices[i].getX());
			if (this.vertices[i].getY() < this.aabbLower.getY())
				this.aabbLower.setY(this.vertices[i].getY());
			if (this.vertices[i].getZ() < this.aabbLower.getZ())
				this.aabbLower.setZ(this.vertices[i].getZ());
			if (this.vertices[i].getX() > this.aabbUpper.getX())
				this.aabbUpper.setX(this.vertices[i].getX());
			if (this.vertices[i].getY() > this.aabbUpper.getY())
				this.aabbUpper.setY(this.vertices[i].getY());
			if (this.vertices[i].getZ() > this.aabbUpper.getZ())
				this.aabbUpper.setZ(this.vertices[i].getZ());
		}
		this.aabbLower.add(this.center);
		this.aabbUpper.add(this.center);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3f lower, Point3f upper) {
		assert (lower != null);
		assert (upper != null);
		if (this.aabbLower == null || this.aabbUpper == null) {
			computeVertices();
		}
		lower.set(this.aabbLower);
		upper.set(this.aabbUpper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		if (this.aabbLower == null) {
			computeVertices();
		}
		return this.aabbLower;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		if (this.aabbUpper == null) {
			computeVertices();
		}
		return this.aabbUpper;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f p) {
		if (!isInit())
			return Float.NaN;
		Vector3f v = new Vector3f();
		v.sub(p, this.center);// p - this.center;
		float sqDist = 0.f;
		float d, excess;
		for (int i = 0; i < 3; ++i) {
			// Project vector from box center to p on each axis, getting the distance
			// of p along that axis, and count any excess distance outside box extents
			d = v.dot(this.axis[i]);
			excess = 0.f;
			if (d < -this.extent[i]) {
				excess = -this.extent[i] - d;
			} else if (d > this.extent[i]) {
				excess = d - this.extent[i];
			}
			sqDist += excess * excess;
		}
		return sqDist;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3f reference) {
		if (!isInit())
			return null;
		EuclidianPoint3D q = new EuclidianPoint3D();
		IntersectionUtil.computeClosestFarestOBBPoints(this.center, this.axis, this.extent, reference, q, null);
		return q;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point3f p) {
		if (!isInit())
			return Float.NaN;
		Vector3f v = new Vector3f();
		v.sub(p, this.center);// p - this.center;
		float sqDist = 0.f;
		float d, excess;
		for (int i = 0; i < 3; ++i) {
			// Project vector from box center to p on each axis, getting the distance
			// of p along that axis, and count any excess distance outside box extents
			d = v.dot(this.axis[i]);
			if (d < 0.) {
				excess = this.extent[i] - d;
			} else {
				excess = d + this.extent[i];
			}
			sqDist += excess * excess;
		}
		return sqDist;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		if (!isInit())
			return null;
		EuclidianPoint3D q = new EuclidianPoint3D();
		IntersectionUtil.computeClosestFarestOBBPoints(this.center, this.axis, this.extent, reference, null, q);
		return q;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || this.extent[0] <= 0. || this.extent[1] <= 0. || this.extent[2] <= 0.;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	// ----------------------------------------------
	// CombinableBounds
	// ----------------------------------------------

	/**
	 * Compute the oriented bounding box from the set of vertices and the associated axis-aligned bounding box.
	 * 
	 * @param constraint
	 *            indicates the plane which must be coplanar to the box, or <code>null</code> if none.
	 * @param vertexList
	 * @see MGPCG pages 219-221
	 */
	private void computeBoundingBoxes(PlaneConstraint constraint, Iterable<? extends Tuple3f> vertexList) {
		if (this.axis == null || this.axis.length != 3)
			this.axis = new Vector3f[3];
		for (int i = 0; i < 3; ++i) {
			if (this.axis[i] == null)
				this.axis[i] = new Vector3f();
			else
				this.axis[i].set(0,0,0);
		}

		// boolean treated = false;

		if (constraint != null) {
			Vector2f R = new Vector2f();
			Vector2f S = new Vector2f();
			switch (constraint) {
			case OXY:
				// Determining the OBB axis restricted on OXY plane
				OrientedBoundingRectangle.computeOBRAxis(new PointFilterXYIterable(vertexList), R, S);
				this.axis[0].set(new Vector3f(R.getX(), R.getY(), 0.));
				this.axis[1].set(new Vector3f(S.getX(), S.getY(), 0.));
				this.axis[2].set(new Vector3f(0., 0., 1.));
				// treated = true;
				break;
			case OXZ:
				// Determining the OBB axis restricted on OXZ plane
				OrientedBoundingRectangle.computeOBRAxis(new PointFilterXZIterable(vertexList), R, S);
				this.axis[0].set(new Vector3f(R.getX(), 0., R.getY()));
				this.axis[1].set(new Vector3f(0., 1., 0.));
				this.axis[2].set(new Vector3f(S.getX(), 0., S.getY()));
				// treated = true;
				break;
			case OYZ:
				// Determining the OBB axis restricted on OYZ plane
				OrientedBoundingRectangle.computeOBRAxis(new PointFilterYZIterable(vertexList), R, S);
				this.axis[0].set(new Vector3f(1., 0., 0.));
				this.axis[1].set(new Vector3f(0., R.getX(), R.getY()));
				this.axis[2].set(new Vector3f(0., S.getX(), S.getY()));
				// treated = true;
				break;
			}
		}

		/*
		 * if (!treated) { // Determining the OBB axis computeOBBAxis(vertexList, this.axis[0], this.axis[1], this.axis[2]); }
		 * 
		 * //Extent and center computation computeOBBCenterExtents(vertexList, this.axis[0], this.axis[1], this.axis[2], this.center, this.extent);
		 */
		computeOBBCenterAxisExtents(vertexList, this.axis[0], this.axis[1], this.axis[2], this.center, this.extent);

		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
		this.isBoundInit = true;
	}

	/**
	 * Set this bounds from a set of points but force this oriented bounding box to be coplanar with the given plane.
	 * 
	 * @param constraint
	 *            indicates which plane may be coplanar to the box.
	 * @param points
	 *            are the points used to update this bounding object.
	 */
	public final void set(PlaneConstraint constraint, Tuple3f... points) {
		set(constraint, Arrays.asList(points));
	}

	/**
	 * Set this bounds from a set of points but force this oriented bounding box to be coplanar with the given plane.
	 * 
	 * @param constraint
	 *            indicates which plane may be coplanar to the box.
	 * @param points
	 *            are the points used to update this bounding object.
	 */
	public void set(PlaneConstraint constraint, Collection<? extends Tuple3f> points) {
		assert (constraint != null);
		computeBoundingBoxes(constraint, points);
	}

	/**
	 * Add the points into the bounds.
	 * 
	 * @param isInit
	 *            must indicates if this bounding object is supposed to be already initialized or not. This parameter is used to improve the performances.
	 * @param pointList
	 *            are the points used to update the bounding box coordinates
	 */
	@Override
	protected void combinePoints(boolean isInit, Collection<? extends Tuple3f> pointList) {
		if (!isInit) {
			computeBoundingBoxes(null, pointList);
		} else {
			computeBoundingBoxes(null, new MergedPointList(pointList));
		}
	}

	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param isInit
	 *            must indicates if this bounding object is supposed to be already initialized or not. This parameter is used to improve the performances.
	 * @param bounds
	 *            are the bounds used to update the bounding box coordinates
	 */
	@Override
	protected void combineBounds(boolean isInit, Collection<? extends Bounds3D> bounds) {
		if (!isInit) {
			if (bounds.size() == 1) {
				Bounds3D cb = CollectionUtil.firstElement(bounds);
				if (cb instanceof OrientedBoundingBox) {
					OrientedBoundingBox obb = (OrientedBoundingBox) cb;
					this.center = (EuclidianPoint3D) obb.center.clone();
					for (int i = 0; i < 3; ++i) {
						this.axis[i] = (Vector3f) obb.axis[i].clone();
						this.extent[i] = obb.extent[i];
					}
					this.vertices = null;
					this.aabbLower = this.aabbUpper = null;
					this.isBoundInit = true;
				} else if (cb instanceof BoundingSphere) {
					BoundingSphere sphere = (BoundingSphere) cb;
					this.center = (EuclidianPoint3D) sphere.center.clone();
					this.axis[0] = new Vector3f(1., 0., 0.);
					this.axis[1] = new Vector3f(0., 1., 0.);
					this.axis[2] = new Vector3f(0., 0., 1.);
					for (int i = 0; i < 3; ++i) {
						this.extent[i] = sphere.radius;
					}
					this.vertices = null;
					this.aabbLower = this.aabbUpper = null;
					this.isBoundInit = true;
				} else if (cb instanceof AlignedBoundingBox) {
					AlignedBoundingBox aabb = (AlignedBoundingBox) cb;
					this.center = (EuclidianPoint3D) aabb.getCenter().clone();
					this.axis[0] = new Vector3f(1., 0., 0.);
					this.axis[1] = new Vector3f(0., 1., 0.);
					this.axis[2] = new Vector3f(0., 0., 1.);
					this.extent[0] = aabb.getSizeX() / 2.f;
					this.extent[1] = aabb.getSizeY() / 2.f;
					this.extent[2] = aabb.getSizeZ() / 2.f;
					this.vertices = null;
					this.aabbLower = this.aabbUpper = null;
					this.isBoundInit = true;
				} else if (cb instanceof ComposedBounds3D) {
					ComposedBounds3D ccb = (ComposedBounds3D) cb;
					computeBoundingBoxes(null, new MergedBoundList(ccb));
				}
			} else {
				computeBoundingBoxes(null, new MergedBoundList(bounds));
			}
		} else {
			computeBoundingBoxes(null, new MergedBoundList(bounds, this));
		}
	}

	/**
	 * Reset the content of this bounds
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.center.set(0, 0, 0);

			for (int i = 0; i < 3; ++i) {
				this.axis[i].set(0, 0, 0);
				this.extent[i] = 0;
			}

			this.vertices = null;
			this.aabbLower = this.aabbUpper = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit)
			return Float.NaN;
		if (this.aabbLower == null || this.aabbUpper == null)
			computeVertices();
		return this.aabbUpper.getX() - this.aabbLower.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit)
			return Float.NaN;
		if (this.aabbLower == null || this.aabbUpper == null)
			computeVertices();
		return this.aabbUpper.getY() - this.aabbLower.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		if (!this.isBoundInit)
			return Float.NaN;
		if (this.aabbLower == null || this.aabbUpper == null)
			computeVertices();
		return this.aabbUpper.getZ() - this.aabbLower.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		if (!this.isBoundInit)
			return null;
		if (this.aabbLower == null || this.aabbUpper == null)
			computeVertices();
		return new Vector3f(this.aabbUpper.getX() - this.aabbLower.getX(), this.aabbUpper.getY() - this.aabbLower.getY(), this.aabbUpper.getZ() - this.aabbLower.getZ());
	}

	// --------------------------------------------------------
	// TranslatableBounds
	// --------------------------------------------------------

	@Override
	public void translate(Vector3f v) {
		if (isInit()) {
			this.center.add(v);
			if (this.aabbLower != null)
				this.aabbLower.add(v);
			if (this.aabbUpper != null)
				this.aabbUpper.add(v);
			if (this.vertices != null) {
				for (Vector3f vv : this.vertices) {
					if (vv != null) {
						vv.add(v);
					}
				}
			}
		}
	}

	/**
	 * Replies the height of this box.
	 * 
	 * @return the height of this box.
	 */
	private float getHeight(CoordinateSystem3D cs) {
		return (cs.getHeightCoordinateIndex() == 1) ? getSizeY() : getSizeZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position, boolean onGround) {
		if (isInit()) {
			if (onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				Point3f np = new Point3f(position);
				cs.addHeight(np, getHeight(cs) / 2.f);
				this.center.set(np);
			} else {
				this.center.set(position);
			}
			this.aabbLower = this.aabbUpper = null;
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position) {
		if (isInit()) {
			this.center.set(position);
			this.aabbLower = this.aabbUpper = null;
			this.vertices = null;
		}
	}

	// --------------------------------------------------------
	// RotatableBounds3D
	// --------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void rotate(AxisAngle4f rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		for (Vector3f v : this.axis) {
			m.transform(v);
		}
		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quaternion rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);
		for (Vector3f v : this.axis) {
			m.transform(v);
		}
		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(AxisAngle4f rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);

		// Internal algorithm uses an identity matrix as basis
		this.axis[0].set(1.f, 0.f, 0.f);
		this.axis[1].set(0.f, 1.f, 0.f);
		this.axis[2].set(0.f, 0.f, 1.f);

		for (Vector3f v : this.axis) {
			m.transform(v);
		}

		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quaternion rotation) {
		Matrix4f m = new Matrix4f();
		m.set(rotation);

		// Internal algorithm uses an identity matrix as basis
		this.axis[0].set(1.f, 0.f, 0.f);
		this.axis[1].set(0.f, 1.f, 0.f);
		this.axis[2].set(0.f, 0.f, 1.f);

		for (Vector3f v : this.axis) {
			m.transform(v);
		}

		this.vertices = null;
		this.aabbLower = this.aabbUpper = null;
	}

	// --------------------------------------------------------
	// IntersectionClassifier
	// --------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f p) {
		if (!isInit())
			return IntersectionType.OUTSIDE;
		Vector3f d = new Vector3f();
		d.sub(p, this.center); // d = p - center

		// For each OBB axis...
		float dist;
		for (int i = 0; i < this.axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			dist = d.dot(this.axis[i]);
			// If distance farther than the box extents, return OUTSIDE
			if (Math.abs(dist) > this.extent[i])
				return IntersectionType.OUTSIDE;
		}

		return IntersectionType.INSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f p) {
		if (!isInit())
			return false;
		Vector3f d = new Vector3f();
		d.sub(p, this.center); // d = p - center

		// For each OBB axis...
		float dist;
		for (int i = 0; i < this.axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			dist = d.dot(this.axis[i]);
			// If distance farther than the box extents, return OUTSIDE
			if (Math.abs(dist) > this.extent[i])
				return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f c, float r) {
		if (!isInit())
			return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidSphereOrientedBox(c, r, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f c, float r) {
		if (!isInit())
			return false;
		return IntersectionUtil.intersectsSolidSphereOrientedBox(c, r, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		if (!isInit())
			return IntersectionType.OUTSIDE;

		// Compute the effective radius of the obb and
		// compare it with the distance between the obb center
		// and the plane; source MGPCG pp.235
		Vector3f n = plane.getNormal();

		float dist = Math.abs(plane.distanceTo(this.center));

		float effectiveRadius = 0.f;

		for (int i = 0; i < this.axis.length; ++i) {
			effectiveRadius += Math.abs(MathUtil.dotProduct(this.axis[i].getX() * this.extent[i], this.axis[i].getY() * this.extent[i], this.axis[i].getZ() * this.extent[i], n.getX(), n.getY(), n.getZ()));
		}

		return (MathUtil.epsilonCompareTo(dist, effectiveRadius) > 0) ? IntersectionType.OUTSIDE : IntersectionType.SPANNING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		if (!isInit())
			return false;
		// Compute the effective radius of the obb and
		// compare it with the distance between the obb center
		// and the plane; source MGPCG pp.235
		Vector3f n = plane.getNormal();

		float dist = Math.abs(plane.distanceTo(this.center));

		float effectiveRadius = 0.f;

		for (int i = 0; i < this.axis.length; ++i) {
			effectiveRadius += Math.abs(MathUtil.dotProduct(this.axis[i].getX() * this.extent[i], this.axis[i].getY() * this.extent[i], this.axis[i].getZ() * this.extent[i], n.getX(), n.getY(), n.getZ()));
		}

		return MathUtil.epsilonCompareTo(dist, effectiveRadius) <= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		if (!isInit())
			return null;
		// Compute the effective radius of the obb and
		// compare it with the distance between the obb center
		// and the plane; source MGPCG pp.235
		Vector3f n = plane.getNormal();

		float dist = plane.distanceTo(this.center);

		float effectiveRadius = 0.f;

		for (int i = 0; i < this.axis.length; ++i) {
			effectiveRadius += Math.abs(MathUtil.dotProduct(this.axis[i].getX() * this.extent[i], this.axis[i].getY() * this.extent[i], this.axis[i].getZ() * this.extent[i], n.getX(), n.getY(), n.getZ()));
		}

		if (MathUtil.epsilonCompareTo(Math.abs(dist), effectiveRadius) <= 0)
			return PlanarClassificationType.COINCIDENT;

		return (dist > 0.) ? PlanarClassificationType.IN_FRONT_OF : PlanarClassificationType.BEHIND;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f l, Point3f u) {
		if (!isInit())
			return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesAlignedBoxOrientedBox(l, u, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f l, Point3f u) {
		if (!isInit())
			return false;
		return IntersectionUtil.intersectsAlignedBoxOrientedBox(l, u, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f obbCenter, Vector3f[] obbAxis, float[] obbExtent) {
		if (!isInit())
			return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesOrientedBoxes(obbCenter, obbAxis, obbExtent, this.center, this.axis, this.extent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f obbCenter, Vector3f[] obbAxis, float[] obbExtent) {
		if (!isInit())
			return false;
		return IntersectionUtil.intersectsOrientedBoxes(obbCenter, obbAxis, obbExtent, this.center, this.axis, this.extent);
	}

	/**
	 * An iterable on the OBB vertices and on a list of points.
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class MergedPointList implements Iterable<Tuple3f> {

		private final Collection<? extends Tuple3f> points;

		/**
		 * @param pointList
		 *            is a list of point to merge inside the iterable.
		 */
		public MergedPointList(Collection<? extends Tuple3f> pointList) {
			this.points = pointList;
		}

		@Override
		public Iterator<Tuple3f> iterator() {
			return CollectionUtil.mergeIterators(getGlobalOrientedBoundVertices(), CollectionUtil.sizedIterator(this.points));
		}

	}

	/**
	 * An iterable on vertices which is merging a collection of bounds and optionnally an oriented bounding box.
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class MergedBoundList implements Iterable<Tuple3f> {

		private final Collection<? extends Bounds3D> bounds;
		private final OrientedBoundingBox obb;

		/**
		 * @param bounds
		 *            are the list of bounds to merge inside
		 */
		public MergedBoundList(Collection<? extends Bounds3D> bounds) {
			this.bounds = bounds;
			this.obb = null;
		}

		/**
		 * @param bounds
		 *            are the list of bounds to merge inside
		 * @param obb
		 *            is the obb to put back in the merging list.
		 */
		public MergedBoundList(Collection<? extends Bounds3D> bounds, OrientedBoundingBox obb) {
			this.bounds = bounds;
			this.obb = obb;
		}

		@Override
		public Iterator<Tuple3f> iterator() {
			Iterator<Tuple3f> iterator = new ExtractionIterator(this.bounds.iterator());

			if (this.obb == null)
				return iterator;

			return CollectionUtil.mergeIterators(iterator, this.obb.getGlobalOrientedBoundVertices());
		}

		/**
		 * An iterable on vertices which is merging a collection of bounds and optionnally an oriented bounding box.
		 * 
		 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
		 * @version $Name$ $Revision$ @mavengroupid fr.utbm.set.sfc fr.utbm.set.sfc
		 * @mavenartifactid setmath
		 */
		private static class ExtractionIterator implements Iterator<Tuple3f> {

			private final Iterator<? extends Bounds3D> bounds;
			private LinkedList<Bounds3D> subBounds = null;
			private Iterator<? extends Point3f> vertices;
			private Tuple3f next;

			/**
			 * @param bounds
			 */
			public ExtractionIterator(Iterator<? extends Bounds3D> bounds) {
				this.bounds = bounds;
				searchNext();
			}

			private void searchNext() {
				this.next = null;
				while (this.vertices == null || !this.vertices.hasNext()) {
					Bounds3D cb;
					this.vertices = null;
					while ((this.subBounds != null && !this.subBounds.isEmpty()) || this.bounds.hasNext()) {
						cb = (this.subBounds != null) ? this.subBounds.removeFirst() : this.bounds.next();
						if (cb.isInit()) {
							if (cb instanceof OrientedBounds3D) {
								this.vertices = ((OrientedBounds3D) cb).getGlobalOrientedBoundVertices();
								break;
							} else if (cb instanceof ComposedBounds3D) {
								if (this.subBounds == null)
									this.subBounds = new LinkedList<Bounds3D>();
								this.subBounds.addAll((ComposedBounds3D) cb);
							}
						}
					}
					if (this.subBounds != null && this.subBounds.isEmpty())
						this.subBounds = null;
					if (this.vertices == null)
						return; // No more vertex
				}

				// One vertex exists!
				this.next = this.vertices.next();
			}

			@Override
			public boolean hasNext() {
				return this.next != null;
			}

			@Override
			public Tuple3f next() {
				if (this.next == null)
					throw new NoSuchElementException();
				Tuple3f n = this.next;
				searchNext();
				return n;
			}

			@Override
			public void remove() {
				//
			}

		}

	}

	/**
	 * An iterable on vertices which is merging a collection of bounds and optionnally an oriented bounding box.
	 * 
	 * @author $Author: sgalland$
	 * @author $Author: ngaud$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public static enum PlaneConstraint {
		/**
		 * Oriented bounding box may have one face coplanar with Oxy plane
		 */
		OXY,
		/**
		 * Oriented bounding box may have one face coplanar with Oxz plane
		 */
		OXZ,
		/**
		 * Oriented bounding box may have one face coplanar with Oyz plane
		 */
		OYZ,
	}

}
