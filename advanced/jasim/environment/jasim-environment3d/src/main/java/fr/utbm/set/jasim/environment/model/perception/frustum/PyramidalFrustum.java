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
import fr.utbm.set.geom.bounds.bounds3d.BoundingPrimitiveType3D;
import fr.utbm.set.geom.object.Direction3D;
import fr.utbm.set.geom.plane.Plane4d;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.jasim.JasimConstants;


/**
 * A frustum represented by a truncated pyramid.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PyramidalFrustum extends AbstractBoxedFrustum3D {

	private static final long serialVersionUID = 3794682563489565412L;
	
	/** Near distance.
	 */
	private final double nearDistance;

	/** Far distance.
	 */
	private final double farDistance;

	/** Horizontal angle.
	 */
	private final double hAngle;
	
	/** Vertical angle.
	 */
	private final double vAngle;

	/**
	 * @param id is the identifier of the frustum
	 * @param eye is the position of the eye
	 * @param orientation is the view direction
	 * @param nearDistance is distance between eye and the near clipping plane
	 * @param farDistance is distance between eye and the far clipping plane
	 * @param horizontalAngle is the horizontal view angle (angle between left and right planes)
	 * @param verticalAngle is the vertical view angle (angle between bottom and top planes)
	 */
	public PyramidalFrustum(UUID id, Point3d eye, Direction3D orientation, double nearDistance, double farDistance, double horizontalAngle, double verticalAngle) {
		super(id);
		this.nearDistance = nearDistance;
		this.farDistance = farDistance;
		this.hAngle = horizontalAngle;
		this.vAngle = verticalAngle;
		
		initialize(nearDistance, farDistance, horizontalAngle, verticalAngle);

		Matrix4d m = GeometryUtil.lookAt(orientation, CoordinateSystem3D.getDefaultCoordinateSystem());
		Transform3D faceToMatrix = new Transform3D(m);
		Point3d pivot;
		
		for(Plane4d p : this.planes) {
			// Compute the reference point
			pivot = p.getPivot();
			faceToMatrix.transform(pivot);
			
			// Change the orientation of the planes
			// Roll the planes
			p.transform(faceToMatrix, pivot);
			
			// Change the position
			p.translate(eye.x, eye.y, eye.z);
		}		
	}

	/** Clone this frustum.
	 */
    @Override
	public PyramidalFrustum clone() {
		return (PyramidalFrustum)super.clone();
	}

    /**
	 * Initialize the frustum.
	 * The coordinate system used is {@link CoordinateSystem3D#XYZ_RIGHT_HAND};
	 * 
	 * @param eyeNearDistance is distance between eye and the near clipping plane
	 * @param eyeFarDistance is distance between eye and the far clipping plane
	 * @param horizontalAngle is the horizontal view angle (angle between left and right planes)
	 * @param verticalAngle is the vertical view angle (angle between bottom and top planes)
	 */
	private void initialize(double eyeNearDistance, double eyeFarDistance, double horizontalAngle, double verticalAngle) {
		assert(CoordinateSystem3D.getDefaultCoordinateSystem()==CoordinateSystem3D.XYZ_RIGHT_HAND);
		
		this.planes[FRONT] = new Plane4d(JasimConstants.DEFAULT_VIEW_VECTOR_X,JasimConstants.DEFAULT_VIEW_VECTOR_Y,JasimConstants.DEFAULT_VIEW_VECTOR_Z,0);
		this.planes[FRONT].setPivot(eyeNearDistance, 0, 0);
		
		this.planes[BACK] = new Plane4d(JasimConstants.DEFAULT_VIEW_VECTOR_X,JasimConstants.DEFAULT_VIEW_VECTOR_Y,JasimConstants.DEFAULT_VIEW_VECTOR_Z,0);
		this.planes[BACK].setPivot(eyeFarDistance, 0, 0);
		this.planes[BACK].negate();
		
		double hTan = Math.tan(horizontalAngle/2.);
		double vTan = Math.tan(verticalAngle/2.);
		
		double farY = hTan * eyeFarDistance;
		double farZ = vTan * eyeFarDistance;
		
		this.planes[LEFT] = new Plane4d(
				0, 0, 0, // eye
				eyeFarDistance, farY, -farZ, // bottom left				
				eyeFarDistance, farY, farZ); // top left
		this.planes[RIGHT] = new Plane4d(
				0, 0, 0, // eye
				eyeFarDistance, -farY, farZ, // top right
				eyeFarDistance, -farY, -farZ); // bottom right				
		this.planes[TOP] = new Plane4d(
				0, 0, 0, // eye
				eyeFarDistance, farY, farZ, // top left
				eyeFarDistance, -farY, farZ); // top right				
		this.planes[BOTTOM] = new Plane4d(
				0, 0, 0, // eye
				eyeFarDistance, -farY, -farZ, // bottom right				
				eyeFarDistance, farY, -farZ); // bottom left
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getFarDistance() {
		return this.farDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getNearDistance() {
		return this.nearDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quat4d q) {
		Point3d eye = getEye();

		initialize(this.nearDistance, this.farDistance, this.hAngle, this.vAngle);

		Matrix4d mat = new Matrix4d();
		mat.set(q);
		Vector3d view = new Vector3d(JasimConstants.DEFAULT_VIEW_VECTOR_X, JasimConstants.DEFAULT_VIEW_VECTOR_Y, JasimConstants.DEFAULT_VIEW_VECTOR_Z);
		mat.transform(view);
		Vector3d up = new Vector3d(JasimConstants.DEFAULT_UP_VECTOR_X, JasimConstants.DEFAULT_UP_VECTOR_Y, JasimConstants.DEFAULT_UP_VECTOR_Z);
		mat.transform(up);
		
		Matrix4d m = GeometryUtil.lookAt(view,up, CoordinateSystem3D.getDefaultCoordinateSystem());
		Transform3D faceToMatrix = new Transform3D(m);
		
		Plane4d plane;
		
		// Left
		plane = this.planes[LEFT];
		plane.transform(faceToMatrix);
		plane.translate(eye.x, eye.y, eye.z);
		
		// Right
		plane = this.planes[RIGHT];
		plane.transform(faceToMatrix);
		plane.translate(eye.x, eye.y, eye.z);

		// Top
		plane = this.planes[TOP];
		plane.transform(faceToMatrix);
		plane.translate(eye.x, eye.y, eye.z);

		// Bottom
		plane = this.planes[BOTTOM];
		plane.transform(faceToMatrix);
		plane.translate(eye.x, eye.y, eye.z);

		// Front
		plane = this.planes[FRONT];
		view.normalize();
		plane.set(view.x, view.y, view.z, 0d);
		view.scale(this.nearDistance);
		plane.setPivot(eye.x+view.x, eye.y+view.y, eye.z+view.z);
				
		// Back
		plane = this.planes[BACK];
		view.normalize();
		plane.set(view.x, view.y, view.z, 0d);
		plane.negate();
		view.scale(this.farDistance);
		plane.setPivot(eye.x+view.x, eye.y+view.y, eye.z+view.z);

		clearCachedValues();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return true;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public BoundingPrimitiveType3D getBoundType() {
		return null;
	}

}
