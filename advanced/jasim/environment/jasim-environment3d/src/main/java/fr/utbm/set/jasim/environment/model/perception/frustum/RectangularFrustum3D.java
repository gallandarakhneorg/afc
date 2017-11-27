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

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.Bounds3D;
import fr.utbm.set.geom.bounds.bounds3d.OrientedBoundingBox;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.plane.PlanarClassificationType;
import fr.utbm.set.geom.plane.Plane;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;


/**
 * A frustum represented by a frustum with planes which are
 * perpendicular/parallel to each others.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RectangularFrustum3D
implements Frustum3D {

	private static final long serialVersionUID = 8424716875967561782L;
	
	private final UUID id;
	
	private EuclidianPoint3D eye;
	private OrientedBoundingBox bounds;
	
	/**
	 * @param id is the identifier of the frustum
	 * @param eye is the position of the eye
	 * @param orientation is the view direction
	 * @param leftRightSize is distance between the left and right planes of box
	 * @param verticalSize is distance between the top and bottom planes of box
	 * @param farDistance is distance between the front and back planes of box
	 */
	public RectangularFrustum3D(UUID id, Point3d eye, Direction3D orientation, double leftRightSize, double verticalSize, double farDistance) {
		this.id = id;
		set(eye, orientation, leftRightSize, verticalSize, farDistance);
	}
	
	/** Set the frustum from given attributes.
	 * 
	 * @param eye is the position of the eye
	 * @param orientation is the view direction
	 * @param leftRightSize is distance between the left and right planes of box
	 * @param verticalSize is distance between the top and bottom planes of box
	 * @param farDistance is distance between the front and back planes of box
	 */
	private void set(Point3d eye, Direction3D orientation, double leftRightSize, double verticalSize, double farDistance) {
		double demiFarDistance = farDistance / 2.;
		
		Vector3d v1 = new Vector3d(orientation.getX(), orientation.getY(), orientation.getZ());
		v1.normalize();
		v1.scale(demiFarDistance);
		
		Point3d center = new Point3d();
		center.add(eye, v1);
		
		v1.normalize();
		
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		
		Vector3d v3 = new Vector3d();
		cs.getUpVector(v3);
		Matrix4d mat = GeometryUtil.lookAt(orientation);
		assert(mat!=null);
		mat.transform(v3);
		
		Vector3d v2 = new Vector3d();
		v2.cross(v3, v1);
		
		Vector3d[] axis = new Vector3d[] {
				v1,
				v2,
				v3
		};
		
		double[] extents = new double[] {
				demiFarDistance,
				leftRightSize / 2.,
				verticalSize / 2.
		};
		
		this.eye = new EuclidianPoint3D(eye);		
		this.bounds = new OrientedBoundingBox(
				center,
				axis,
				extents);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public RectangularFrustum3D clone() {
		try {
			return (RectangularFrustum3D)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getEye() {
		return this.eye;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFarDistance() {
		return 2.*this.bounds.getRExtent();
	}

	/**
	 * Replies the distance between the left clipping plane to the right clipping plane.
	 * 
	 * @return the distance between the left and right borders of the frustum.
	 */
	public double getLeftRightDistance() {
		return 2.*this.bounds.getSExtent();
	}

	/**
	 * Replies the distance between the bottom clipping plane to the top clipping plane.
	 * 
	 * @return the distance between the bottom and top borders of the frustum.
	 */
	public double getBottomUpDistance() {
		return 2.*this.bounds.getTExtent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getNearDistance() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quat4d q) {
		this.bounds.rotate(q);
		Vector3d v = new Vector3d(this.bounds.getR());
		v.scale(this.bounds.getRExtent());
		Point3d newCenter = new Point3d();
		newCenter.add(this.eye, v);
		this.bounds.setTranslation(newCenter, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quat4d q) {
		this.bounds.setRotation(q);
		Vector3d v = new Vector3d(this.bounds.getR());
		v.scale(this.bounds.getRExtent());
		Point3d newCenter = new Point3d();
		newCenter.add(this.eye, v);
		this.bounds.setTranslation(newCenter, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transform(Transform3D trans) {
		Quat4d rotation = new Quat4d();
		trans.get(rotation);

		this.bounds.rotate(rotation);
		
		this.eye.add(trans.getTranslationVector());
		
		Vector3d v = new Vector3d(this.bounds.getR());
		v.scale(this.bounds.getRExtent());
		Point3d newCenter = new Point3d();
		newCenter.add(this.eye, v);
		
		this.bounds.setTranslation(newCenter, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3d v) {
		this.eye.add(v);
		this.bounds.translate(v);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Bounds3D box) {
		return this.bounds.classifies(box);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d p) {
		return this.bounds.classifies(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d l, Point3d u) {
		return this.bounds.classifies(l, u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Bounds3D box) {
		return this.bounds.intersects(box);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d p) {
		return this.bounds.intersects(p);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d l, Point3d u) {
		return this.bounds.intersects(l, u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		return this.bounds.classifies(plane);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d center, double radius) {
		return this.bounds.classifies(center, radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3d center, Vector3d[] axis, double[] extents) {
		return this.bounds.classifies(center, axis, extents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		return this.bounds.classifiesAgainst(plane);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		return this.bounds.intersects(plane);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d center, double radius) {
		return this.bounds.intersects(center, radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3d center, Vector3d[] axis, double[] extents) {
		return this.bounds.intersects(center, axis, extents);
	}
	
	/** Replies the unit vector in the view direction.
	 * 
	 * @return the front vector.
	 */
	public Vector3d getFrontVector() {
		return this.bounds.getR();
	}

	/** Replies the unit vector of the side direction.
	 * 
	 * @return the side vector.
	 */
	public Vector3d getSideVector() {
		return this.bounds.getS();
	}

	/** Replies the unit vector in the up direction.
	 * 
	 * @return the up vector.
	 */
	public Vector3d getUpVector() {
		return this.bounds.getT();
	}
	
	/** Replies the oriented bounds of this frustum.
	 * 
	 * @return the oriented bounding box used by this frustum. 
	 */
	public OrientedBoundingBox getOrientedBoundingBox() {
		return this.bounds;
	}

}

