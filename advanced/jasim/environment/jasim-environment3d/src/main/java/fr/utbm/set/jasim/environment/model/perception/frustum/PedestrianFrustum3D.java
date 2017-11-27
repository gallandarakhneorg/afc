/* 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.UUID;

import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.bounds.bounds2d.Bounds2D;
import fr.utbm.set.geom.bounds.bounds3d.AbstractBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.AlignedBoundingBox;
import fr.utbm.set.geom.bounds.bounds3d.BoundingPrimitiveType3D;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.plane.PlanarClassificationType;
import fr.utbm.set.geom.plane.Plane;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;

/**
 * A frustum usable by animats. It is composed of
 * a circular frustum around the animat and a triangular frustum
 * along its orientation.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PedestrianFrustum3D extends AbstractBounds3D implements Frustum3D {

	private static final long serialVersionUID = 8292822438316340167L;
	
	private final UUID id;
	private SphericalFrustum sphere;
	private PyramidalFrustum pyramid;
	private transient AlignedBoundingBox aabb;

	/**
	 * @param id is the identifier of the frustum.
	 * @param eye is the position of the eye
	 * @param radius is the radius of the spherical frustum around the animat
	 * @param orientation is the view direction
	 * @param farDistance is distance between eye and the far clipping plane
	 * @param horizontalAngle is the horizontal view angle (angle between left and right planes)
	 * @param verticalAngle is the vertical view angle (angle between bottom and top planes)
	 */
	public PedestrianFrustum3D(UUID id, Point3d eye, double radius, Direction3D orientation, double farDistance, double horizontalAngle, double verticalAngle) {
		this.id = id;
		this.sphere = new SphericalFrustum(id, eye, radius);
		this.pyramid = new PyramidalFrustum(id, eye, orientation, radius, farDistance, horizontalAngle, verticalAngle);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PedestrianFrustum3D clone() {
		PedestrianFrustum3D clone = (PedestrianFrustum3D)super.clone();
		clone.pyramid = this.pyramid.clone();
		clone.sphere = this.sphere.clone();
		return clone;
	}
	
	/** Replies the spherical frustum which is constiting this pedestrian frustum.
	 * 
	 * @return the inner spherical frustum.
	 */
	public SphericalFrustum getSphericalFrustum() {
		return this.sphere;
	}
	
	/** Replies the pyramidal frustum which is constiting this pedestrian frustum.
	 * 
	 * @return the inner pyramidal frustum.
	 */
	public PyramidalFrustum getPyramidalFrustum() {
		return this.pyramid;
	}

	private void clearBuffers() {
		this.aabb = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void rotate(Quat4d q) {
		this.pyramid.rotate(q);
		clearBuffers();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setRotation(Quat4d q) {
		this.pyramid.setRotation(q);
		clearBuffers();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void transform(Transform3D trans) {
		this.pyramid.transform(trans);
		this.sphere.transform(trans);
		clearBuffers();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(Vector3d v) {
		this.pyramid.translate(v);
		this.sphere.translate(v);
		clearBuffers();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getEye() {
		return this.sphere.getEye();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getFarDistance() {
		return Math.max(this.sphere.getFarDistance(), this.pyramid.getFarDistance());
	}

	/** {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getNearDistance() {
		return 0.;
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d p) {
		IntersectionType i1 = this.pyramid.classifies(p);
		if (i1==IntersectionType.INSIDE) return IntersectionType.INSIDE;
		IntersectionType i2 = this.sphere.classifies(p);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d l, Point3d u) {
		IntersectionType i1 = this.pyramid.classifies(l,u);
		if (i1==IntersectionType.INSIDE) return IntersectionType.INSIDE;
		IntersectionType i2 = this.sphere.classifies(l,u);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		IntersectionType i1 = this.pyramid.classifies(plane);
		if (i1==IntersectionType.INSIDE) return IntersectionType.INSIDE;
		IntersectionType i2 = this.sphere.classifies(plane);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d c, double r) {
		IntersectionType i1 = this.pyramid.classifies(c,r);
		if (i1==IntersectionType.INSIDE) return IntersectionType.INSIDE;
		IntersectionType i2 = this.sphere.classifies(c,r);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d center, Vector3d[] axis, double[] extent) {
		IntersectionType i1 = this.pyramid.classifies(center, axis, extent);
		if (i1==IntersectionType.INSIDE) return IntersectionType.INSIDE;
		IntersectionType i2 = this.sphere.classifies(center, axis, extent);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d p) {
		return this.pyramid.intersects(p)
		|| this.sphere.intersects(p);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d l, Point3d u) {
		return this.pyramid.intersects(l,u)
		|| this.sphere.intersects(l,u);
	}


	/** {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		PlanarClassificationType i1 = this.pyramid.classifiesAgainst(plane);
		if (i1==PlanarClassificationType.COINCIDENT) return PlanarClassificationType.COINCIDENT;
		PlanarClassificationType i2 = this.sphere.classifiesAgainst(plane);
		return i1.or(i2);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		return this.pyramid.intersects(plane)
		|| this.sphere.intersects(plane);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d c, double r) {
		return this.pyramid.intersects(c,r)
		|| this.sphere.intersects(c,r);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d center, Vector3d[] axis, double[] extent) {
		return this.pyramid.intersects(center, axis, extent)
		|| this.sphere.intersects(center, axis, extent);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D toBounds2D(CoordinateSystem3D system) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distance(Point3d reference) {
		return Math.min(this.sphere.distance(reference), this.pyramid.distance(reference));
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceSquared(Point3d reference) {
		return Math.min(this.sphere.distanceSquared(reference), this.pyramid.distanceSquared(reference));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3d reference) {
		EuclidianPoint3D p1 = this.sphere.nearestPoint(reference);
		double d = reference.distanceSquared(p1);
		EuclidianPoint3D p2 = this.pyramid.nearestPoint(reference);
		double d2 = reference.distanceSquared(p2);
		return (d<=d2) ? p1 : p2;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double distanceMax(Point3d reference) {
		return Math.max(this.sphere.distanceMax(reference), this.pyramid.distanceMax(reference));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double distanceMaxSquared(Point3d reference) {
		return Math.max(this.sphere.distanceMaxSquared(reference), this.pyramid.distanceMaxSquared(reference));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3d reference) {
		EuclidianPoint3D p1 = this.sphere.nearestPoint(reference);
		double d = reference.distanceSquared(p1);
		EuclidianPoint3D p2 = this.pyramid.nearestPoint(reference);
		double d2 = reference.distanceSquared(p2);
		return (d>=d2) ? p1 : p2;
	}

	/** Replies the aligned bounding box of this frustum.
	 * 
	 * @return the aligned bounding box of this frustum.
	 */
	public AlignedBoundingBox toBoundingBox() {
		if (this.aabb==null) {
			AlignedBoundingBox b1 = this.pyramid.toBoundingBox();
			this.aabb = new AlignedBoundingBox(b1,this.sphere);
		}
		return this.aabb;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getCenter();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getLower();
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return null;
		return box.getUpper();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3d lower, Point3d upper) {
		AlignedBoundingBox box = toBoundingBox();
		if (box!=null) {
			box.getLowerUpper(lower, upper);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeX() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return 0.;
		return box.getSizeX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeY() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return 0.;
		return box.getSizeY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public double getSizeZ() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return 0.;
		return box.getSizeZ();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3d getSize() {
		AlignedBoundingBox box = toBoundingBox();
		if (box==null) return new Vector3d();
		return box.getSize();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.pyramid.isEmpty() && this.sphere.isEmpty();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.pyramid.isInit() && this.sphere.isInit();
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingPrimitiveType3D getBoundType() {
		return null;
	}

}
