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
package fr.utbm.set.jasim.environment.model.world;

import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.RotatableBounds3D;
import fr.utbm.set.geom.bounds.bounds3d.TranslatableBounds3D;
import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.transform.Transform3D;
import fr.utbm.set.geom.transform.Transformable3D;
import fr.utbm.set.jasim.environment.model.perception.tree.DynamicPerceptionTreeNode;
import fr.utbm.set.jasim.environment.model.place.Place;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.MeasureUnitUtil;
import fr.utbm.set.math.SpeedUnit;
import fr.utbm.set.physics.PhysicsUtil;

/** This interface representes an object in a 3D space.
 * <p>
 * The mobile is defined by:
 * <ul>
 * <li>its position, which is the local origin point;</li>
 * <li>a mesh, ie. a set of points for whose the coordinates are relative to the position of the object;<li>
 * <li>a pivot point around which all the rotation actions will be applied; the coordinate of the pivot are relative to theobject position.</li>
 * </ul>
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MobileEntity3D<B extends CombinableBounds3D> 
extends Entity3D<B>
implements TreeDataMobileEntity<B>,
           Transformable3D {
	
	private static final long serialVersionUID = -3200394825677838526L;

	private final Mesh3D convexHull;
	
	private Vector3d pivot = null;
	
	private final Quat4d rotation = new Quat4d();
	
	private final Vector3d linearVelocity = new Vector3d();

	private transient double linearAcceleration;

	private final AxisAngle4d angularVelocity = new AxisAngle4d();
	
	private transient double angularAcceleration;
	
	private transient WeakReference<DynamicPerceptionTreeNode<B,?,?,?>> treeNode;
	private transient WeakReference<Place<?,?,? extends MobileEntity<B>>> place;

	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param type is the type of this entity.
	 * @param bounds 
	 * @param pivot is the pivot point located according to the entity position.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. All the points are
	 * relative to the specified pivot
	 */
	public MobileEntity3D(UUID identifier, B bounds, Semantic type, Vector3d pivot, boolean onGround, Mesh3D convexHull) {
		super(identifier,bounds, type, onGround);
		this.treeNode = null;
		this.place = null;
		
		this.pivot = new Vector3d(pivot);
		
		this.rotation.set(0., 0., 0., 1.);
		
		this.convexHull = convexHull;
		if (this.convexHull.isGlobalMesh()) {
			Point3d p = new Point3d(getPosition3D());
			p.add(this.pivot);
			this.convexHull.makeLocal(p);
		}
		
		initAttributes();
	}

	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param pivot is the pivot point located according to the entity position.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. All the points are
	 * relative to the specified pivot
	 */
	public MobileEntity3D(B bounds, Semantic type, Vector3d pivot, boolean onGround, Mesh3D convexHull) {
		super(bounds, type, onGround);
		this.treeNode = null;
		
		this.pivot = new Vector3d(pivot);
		
		this.rotation.set(0., 0., 0., 1.);
		
		this.convexHull = convexHull;
		if (this.convexHull.isGlobalMesh()) {
			Point3d p = new Point3d(getPosition3D());
			p.add(this.pivot);
			this.convexHull.makeLocal(p);
		}

		initAttributes();
	}

	/**
	 * @param bounds 
	 * @param type is the type of this entity.
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public MobileEntity3D(B bounds, Semantic type, boolean onGround, Mesh3D convexHull) {
		super(bounds, type, onGround);
		this.treeNode = null;
		
		this.pivot = null;
		
		this.rotation.set(0., 0., 0., 1.);
		
		this.convexHull = convexHull;
		if (this.convexHull.isGlobalMesh()) {
			this.convexHull.makeLocal(getPosition3D());
		}
		
		initAttributes();
	}
	
	/**
	 * @param identifier is the identifier of this object. This value should be unique.
	 * @param type is the type of this entity.
	 * @param bounds 
	 * @param onGround is <code>true</code> if this entity is located on a ground,
	 * otherwhise <code>false</code>
	 * @param convexHull is the convex hull of the entity. All the points must be
	 * relative to the entity position.
	 */
	public MobileEntity3D(UUID identifier, B bounds, Semantic type, boolean onGround, Mesh3D convexHull) {
		super(identifier, bounds, type, onGround);
		this.treeNode = null;
		
		this.pivot = null;
		
		this.rotation.set(0., 0., 0., 1.);
		
		this.convexHull = convexHull;
		if (this.convexHull.isGlobalMesh()) {
			this.convexHull.makeLocal(getPosition3D());
		}
		
		initAttributes();
	}
	
	private void initAttributes() {
		this.linearVelocity.set(0, 0, 0);
		this.linearAcceleration = 0.;
		this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
		this.angularAcceleration = 0.;
	}
	
	/** Replies the points of the entity mesh.
	 *
	 * @return the list of points that composed the mesh.
	 */
	public Mesh3D getMesh() {
		return this.convexHull;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Place<?,?,? extends MobileEntity<B>> getPlace() {
		return this.place==null ? null : this.place.get();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setPlace(Place<?,?,? extends MobileEntity<B>> place) {
		this.place = (place==null) ? null : new WeakReference<Place<?,?,? extends MobileEntity<B>>>(place);
	}

	/** {@inheritDoc}
	 */
	@Override
	public DynamicPerceptionTreeNode<B,?,?,?> getNodeOwner() {
		return (this.treeNode==null) ? null : this.treeNode.get();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setNodeOwner(DynamicPerceptionTreeNode<B,?,?,?> owner) {
		if (owner==null) {
			this.treeNode = null;
		}
		else {
			this.treeNode = new WeakReference<DynamicPerceptionTreeNode<B,?,?,?>>(owner);
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
    public void setIdentityTransform() {
    	setTranslation(0, 0, 0);
    	
    	this.pivot = null;
    	this.rotation.set(0., 0., 0., 1.);
		this.linearVelocity.set(0, 0, 0);
    	this.linearAcceleration = 0.;
		this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
		this.angularAcceleration = 0.;

    	clearBuffers();
    	setRotationOnChildren(this.rotation);
    	assert(!(Double.isNaN(this.rotation.x)||Double.isNaN(this.rotation.y)||Double.isNaN(this.rotation.z)||Double.isNaN(this.rotation.w)));
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setTransform(Transform3D trans) {
    	Point3d previousPosition = getPosition3D();
    	
    	// Rotation
    	trans.get(this.rotation);
		if (this.bounds instanceof RotatableBounds3D) {
			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
			rb.setRotation(this.rotation);
		}
		else {
			this.convexHull.getBounds(this.bounds, this.rotation);
		}

		// Translation
    	Point3d expectedPosition = trans.getTranslation();
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).setTranslation(expectedPosition, isOnGround());

		// Kinematic and steering attributes
		this.linearVelocity.set(0, 0, 0);
		this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
		
		// Steering attributes
    	this.linearAcceleration = 0.;
		this.angularAcceleration = 0.;
		
		// Buffered values and attached children
    	clearBuffers();
    	Vector3d translation = new Vector3d(expectedPosition);    	
    	translation.sub(previousPosition);
    	translateChildren(translation);
    	setRotationOnChildren(this.rotation);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void transform(Transform3D trans) {
    	if (this.pivot!=null) {
    		transformWithPivot(trans);
    	}
    	else {
    		transformWithoutPivot(trans);
    	}
    }
    
    private void transformWithoutPivot(Transform3D trans) {
    	Quat4d rotationAmount = trans.getQuaternion();
		Vector3d expectedTranslation = trans.getTranslationVector();

    	// Rotation
		this.rotation.mul(rotationAmount, this.rotation);
		if (this.bounds instanceof RotatableBounds3D) {
			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
			rb.rotate(rotationAmount);
			// Translation
			if (this.bounds instanceof TranslatableBounds3D)
				((TranslatableBounds3D)this.bounds).translate(expectedTranslation);
		}
		else {
			Point3d p = new Point3d(getPosition3D());
			p.add(expectedTranslation);
			
			this.convexHull.getBounds(this.bounds, this.rotation);
			
			// Translation
			if (this.bounds instanceof TranslatableBounds3D)
				((TranslatableBounds3D)this.bounds).setTranslation(p, isOnGround());
		}

		// Kinematic and steering attributes
    	updateLinearAngularSpeeds(
    			expectedTranslation.x,expectedTranslation.y,expectedTranslation.z,
    			rotationAmount);
		
		// Buffered values and attached children
    	clearBuffers();
    	translateChildren(expectedTranslation);
    	setRotationOnChildren(this.rotation);
    }
    
    private void transformWithPivot(Transform3D trans) {
    	assert(this.pivot!=null);
    	
    	Quat4d rotationAmount = trans.getQuaternion();

    	Point3d previousPosition = new Point3d(getPosition3D());
		
		// Rotation
		this.rotation.mul(rotationAmount, this.rotation);
		if (this.bounds instanceof RotatableBounds3D) {
			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
			rb.rotate(rotationAmount);
		}
		else {
			this.convexHull.getBounds(this.bounds, this.rotation);			
		}

		// Translation
		Point3d newPosition = new Point3d(previousPosition);
		newPosition.add(this.pivot);
		Matrix4d m = GeometryUtil.rotateAround(rotationAmount, newPosition);
		newPosition.set(previousPosition);
		m.transform(newPosition);
		newPosition.add(trans.getTranslation());
		
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).setTranslation(newPosition, isOnGround());

		// Kinematic and steering attributes
		Vector3d translationAmount = new Vector3d();
		translationAmount.sub(newPosition, previousPosition);
    	updateLinearAngularSpeeds(
    			translationAmount.x,translationAmount.y,translationAmount.z,
    			rotationAmount);
		
		// Buffered values and attached children
    	clearBuffers();
    	translateChildren(translationAmount);
    	setRotationOnChildren(this.rotation);
    }

	/** {@inheritDoc}
	 */
	@Override
    public Transform3D getTransformMatrix() {
    	Transform3D tr = new Transform3D();
    	tr.set(this.rotation);
    	tr.setTranslation(getPosition3D());
    	return tr;
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setTranslation(double x, double y, double z) {
    	setTranslation(new Point3d(x,y,z));
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setTranslation(Point3d position) {
    	Point3d previousPosition = getPosition3D();
    	
		// Translation
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).setTranslation(position, isOnGround());

		// Kinematic and steering attributes
		this.linearVelocity.set(0, 0, 0);
    	this.linearAcceleration = 0.;
		this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
		this.angularAcceleration = 0.;
		
		// Buffered values and attached children
    	clearBuffers();
    	Vector3d translation = new Vector3d(position);    	
    	translation.sub(previousPosition);
    	translateChildren(translation);
    	setRotationOnChildren(this.rotation);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void translate(double dx, double dy, double dz) {
    	translate(new Vector3d(dx,dy,dz));
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void translate(Vector3d v) {
		// Translation
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).translate(v);

		// Kinematic and steering attributes
    	updateLinearSpeed(
    			v.x,v.y,v.z);
		
		// Buffered values and attached children
    	clearBuffers();
    	translateChildren(v);
    	setRotationOnChildren(this.rotation);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setScale(double sx, double sy, double sz) {
    	//
    }

	/** {@inheritDoc}
	 */
	@Override
    public void scale(double sx, double sy, double sz) {
    	//
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public Tuple3d getScale() {
    	return new Vector3d(1,1,1);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quat4d quaternion) {
    	if (this.pivot!=null) {
    		Point3d pivotPoint = new Point3d(getPosition3D());
    		pivotPoint.add(this.pivot);
    		setRotation(quaternion, pivotPoint);
    	}
    	else {
	    	// Rotation
	    	this.rotation.set(quaternion);
			if (this.bounds instanceof RotatableBounds3D) {
				RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
				rb.setRotation(this.rotation);
			}
			else {
				Point3d pos = getPosition3D();
				this.convexHull.getBounds(this.bounds, this.rotation);
				if (this.bounds instanceof TranslatableBounds3D)
					((TranslatableBounds3D)this.bounds).setTranslation(pos, isOnGround());
			}
	
			// Kinematic and steering attributes
			this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
			this.angularAcceleration = 0.;
			
			// Buffered values and attached children
	    	clearBuffers();
	    	setRotationOnChildren(this.rotation);
    	}
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4d quaternion) {
    	if (this.pivot!=null) {
    		Point3d pivotPoint = new Point3d(getPosition3D());
    		pivotPoint.add(this.pivot);
    		setRotation(quaternion, pivotPoint);
    	}
    	else {
	    	// Rotation
	    	this.rotation.set(quaternion);
			if (this.bounds instanceof RotatableBounds3D) {
				RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
				rb.setRotation(this.rotation);
			}
			else {
				Point3d pos = getPosition3D();
				this.convexHull.getBounds(this.bounds, this.rotation);
				if (this.bounds instanceof TranslatableBounds3D)
					((TranslatableBounds3D)this.bounds).setTranslation(pos, isOnGround());
			}
	
			// Kinematic and steering attributes
			this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
			this.angularAcceleration = 0.;
			
			// Buffered values and attached children
	    	clearBuffers();
	    	setRotationOnChildren(this.rotation);
    	}
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(AxisAngle4d quaternion, Point3d pivot) {
    	Quat4d q = new Quat4d();
    	q.set(quaternion);
    	setRotation(q, pivot);
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void setRotation(Quat4d quaternion, Point3d pivot) {
    	// Rotation
    	this.rotation.set(quaternion);
		if (this.bounds instanceof RotatableBounds3D) {
			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
			rb.setRotation(this.rotation);
		}
		else {
			this.convexHull.getBounds(this.bounds, this.rotation);
		}

		// Translation
    	Point3d previousPosition = getPosition3D();
    	Point3d position = new Point3d(previousPosition);
    	Matrix4d m = GeometryUtil.rotateAround(this.rotation, pivot);
    	m.transform(position);
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).setTranslation(position, isOnGround());

		// Kinematic and steering attributes
		this.linearVelocity.set(0, 0, 0);
    	this.linearAcceleration = 0.;
		this.angularVelocity.set(CoordinateSystem3D.getDefaultCoordinateSystem().getUpVector(), 0);
		this.angularAcceleration = 0.;
		
		// Buffered values and attached children
    	clearBuffers();
    	Vector3d translation = new Vector3d(position);    	
    	translation.sub(previousPosition);
    	translateChildren(translation);
    	setRotationOnChildren(this.rotation);
    }
    
    /** Set the rotation around the given pivot.
	 * 
	 * @param quaternion is the rotation
	 * @param px is the pivot with origin located at the entity position.
	 * @param py is the pivot with origin located at the entity position.
	 * @param pz is the pivot with origin located at the entity position.
	 */
    public final void setRotation(Quat4d quaternion, double px, double py, double pz) {    
    	setRotation(quaternion, new Point3d(px,py,pz));
    }

    /** Set the rotation around the given pivot.
	 * 
	 * @param quaternion is the rotation
	 * @param px is the pivot with origin located at the entity position.
	 * @param py is the pivot with origin located at the entity position.
	 * @param pz is the pivot with origin located at the entity position.
	 */
    public final void setRotation(AxisAngle4d quaternion, double px, double py, double pz) {
    	setRotation(quaternion, new Point3d(px,py,pz));
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(AxisAngle4d quaternion) {
    	Quat4d q = new Quat4d();
    	q.set(quaternion);
    	rotate(q);
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(Quat4d quaternion) {
    	if (this.pivot!=null) {
    		Point3d pivotPoint = new Point3d(getPosition3D());
    		pivotPoint.add(this.pivot);
    		rotate(quaternion, pivotPoint);
    	}
    	else {
        	// Rotation
    		this.rotation.mul(quaternion, this.rotation);
    		if (this.bounds instanceof RotatableBounds3D) {
    			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
    			rb.rotate(quaternion);
    		}
    		else {
    			Point3d p = getPosition3D();
    			
    			this.convexHull.getBounds(this.bounds, this.rotation);
    			
    			// Translation
    			if (this.bounds instanceof TranslatableBounds3D)
    				((TranslatableBounds3D)this.bounds).setTranslation(p, isOnGround());
    		}

    		// Kinematic and steering attributes
        	updateAngularSpeed(quaternion);
    		
    		// Buffered values and attached children
        	clearBuffers();
        	setRotationOnChildren(this.rotation);
    	}
    }

	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(AxisAngle4d quaternion, Point3d pivotPt) {
    	Quat4d q = new Quat4d();
    	q.set(quaternion);
    	rotate(q, pivotPt);
    }

    /** Rotate around the given pivot.
	 * 
	 * @param quaternion is the rotation
	 * @param px is the pivot with origin located at the entity position.
	 * @param py is the pivot with origin located at the entity position.
	 * @param pz is the pivot with origin located at the entity position.
	 */
    public final void rotate(AxisAngle4d quaternion, double px, double py, double pz) {
    	Quat4d q = new Quat4d();
    	q.set(quaternion);
    	rotate(q, new Point3d(px,py,pz));
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public final void rotate(Quat4d quaternion, Point3d pivotPt) {
    	assert(pivotPt!=null);
    	
    	Point3d previousPosition = getPosition3D();
		
		// Rotation
		this.rotation.mul(quaternion, this.rotation);
		if (this.bounds instanceof RotatableBounds3D) {
			RotatableBounds3D rb = (RotatableBounds3D)this.bounds;
			rb.rotate(quaternion);
		}
		else {
			this.convexHull.getBounds(this.bounds, this.rotation);			
		}

		// Translation
		Matrix4d m = GeometryUtil.rotateAround(quaternion, pivotPt);
		Point3d newPosition = new Point3d(previousPosition);
		m.transform(newPosition);
		
		if (this.bounds instanceof TranslatableBounds3D)
			((TranslatableBounds3D)this.bounds).setTranslation(newPosition, isOnGround());

		// Kinematic and steering attributes
		Vector3d translationAmount = new Vector3d();
		translationAmount.sub(newPosition, previousPosition);
    	updateLinearAngularSpeeds(
    			translationAmount.x,translationAmount.y,translationAmount.z,
    			quaternion);
		
		// Buffered values and attached children
    	clearBuffers();
    	translateChildren(translationAmount);
    	setRotationOnChildren(this.rotation);
    }

    /** Rotate around the given pivot.
	 * 
	 * @param quaternion is the rotation
	 * @param px is the pivot with origin located at the entity position.
	 * @param py is the pivot with origin located at the entity position.
	 * @param pz is the pivot with origin located at the entity position.
	 */
    public final void rotate(Quat4d quaternion, double px, double py, double pz) {
    	rotate(quaternion, new Point3d(px,py,pz));
    }

	/** {@inheritDoc}
	 */
	@Override
    public AxisAngle4d getAxisAngle() {
    	AxisAngle4d aa = new AxisAngle4d();
    	aa.set(this.rotation);
    	return aa;
    }
    
	/** Replies the rotation as a quaternion.
	 * 
	 * @return a quaternion
	 */
    public Quat4d getQuaternion() {
    	return new Quat4d(this.rotation);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setPivot(double x, double y, double z) {
    	this.pivot = new Vector3d(x,y,z);
    }

	/** {@inheritDoc}
	 */
	@Override
    public void setPivot(Point3d point) {
    	this.pivot = new Vector3d(point);
    }

	/** {@inheritDoc}
	 */
	@Override
    public Point3d getPivot() {
    	if (this.pivot==null) return new Point3d();
    	return new Point3d(this.pivot);
    }
    
	/** Replies the vector from the entity position that permits to
	 * compute the pivot point.
	 * 
	 * @return the pivot vector from the entioty position.
	 */
    public Vector3d getPivotVector() {
    	if (this.pivot==null) return new Vector3d();
    	return new Vector3d(this.pivot);
    }

    /** Invoked when child objects should be transformed with the same parameters as this entity.
     * 
     * @param translation
     */
    protected void translateChildren(Vector3d translation) {
    	//
    }

    /** Invoked when child objects should be transformed with the same parameters as this entity.
     * 
     * @param rotation
     */
    protected void setRotationOnChildren(Quat4d rotation) {
    	//
    }
    
    /** Update the linear speed attribute according to the current simulation clock
     * and the given movement.
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    protected void updateLinearSpeed(double dx, double dy, double dz) {
    	double previousSpeed = getLinearSpeed();
    	double speed = 0.;
    	double acceleration = 0.;
    	Place<?,?,? extends MobileEntity<B>> p = getPlace();
    	if (p!=null) {
    		Clock clock = p.getSimulationClock();
    		if (clock!=null) {
    			double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
    			double delta;
    			if (isOnGround()) {
    				delta = GeometryUtil.distance(dx,dy);
    			}
    			else {
    				delta = GeometryUtil.distance(dx,dy,dz);
    			}
    			speed = PhysicsUtil.speed(delta,dt);
    			acceleration = PhysicsUtil.acceleration(previousSpeed, speed, dt);
    		}
    	}
    	
    	this.linearVelocity.set(dx, dy, dz);
    	if (this.linearVelocity.lengthSquared()!=0) {
    		this.linearVelocity.normalize();
    		this.linearVelocity.scale(speed);
    	}
    	this.linearAcceleration = acceleration;
    }
	
    /** Update the angular speed attribute according to the current simulation clock
     * and the given movement.
     * 
     * @param dx
     * @param dy
     * @param dz
     * @param quaternion
     */
    protected void updateLinearAngularSpeeds(double dx, double dy, double dz, Quat4d quaternion) {
    	double previousAngularSpeed = getAngularSpeed();
    	double aSpeed = 0.;
    	double aAcceleration = 0.;
    	double previousLinearSpeed = getLinearSpeed();
    	double lSpeed = 0.;
    	double lAcceleration = 0.;
    	Place<?,?,? extends MobileEntity<B>> p = getPlace();
    	if (p!=null) {
    		Clock clock = p.getSimulationClock();
    		if (clock!=null) {
    			double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
    			double delta;
    			if (isOnGround()) {
    				delta = GeometryUtil.distance(dx,dy);
    			}
    			else {
    				delta = GeometryUtil.distance(dx,dy,dz);
    			}
    			lSpeed = PhysicsUtil.speed(delta,dt);
    			lAcceleration = PhysicsUtil.acceleration(previousLinearSpeed, lSpeed, dt);
    			double sin_a2 = Math.sqrt(quaternion.x*quaternion.x + quaternion.y*quaternion.y + quaternion.z*quaternion.z);  // |sin a/2|, w = cos a/2
    			double da = 2. * Math.atan2(sin_a2, quaternion.w);
    			aSpeed = PhysicsUtil.speed(da,dt);
    			aAcceleration = PhysicsUtil.acceleration(previousAngularSpeed, aSpeed, dt);
    		}
    	}
    	
    	this.linearVelocity.set(dx, dy, dz);
    	if (this.linearVelocity.lengthSquared()!=0) {
	    	this.linearVelocity.normalize();
	    	this.linearVelocity.scale(lSpeed);
    	}
    	this.linearAcceleration = lAcceleration;
    	this.angularVelocity.set(quaternion);
    	this.angularVelocity.setAngle(aSpeed);
    	this.angularAcceleration = aAcceleration;
    }

    /** Update the angular speed attribute according to the current simulation clock
     * and the given movement.
     * 
     * @param quaternion
     */
    protected void updateAngularSpeed(Quat4d quaternion) {
    	double previousAngularSpeed = getAngularSpeed();
    	double aSpeed = 0.;
    	double aAcceleration = 0.;
    	Place<?,?,? extends MobileEntity<B>> p = getPlace();
    	if (p!=null) {
    		Clock clock = p.getSimulationClock();
    		if (clock!=null) {
    			double dt = clock.getSimulationStepDuration(TimeUnit.SECONDS);
    			double da = GeometryUtil.angleQuat(quaternion.x, quaternion.y, quaternion.z, quaternion.w);
    			aSpeed = PhysicsUtil.speed(da,dt);
    			aAcceleration = PhysicsUtil.acceleration(previousAngularSpeed, aSpeed, dt);
    		}
    	}
    	
    	this.angularVelocity.set(quaternion);
    	this.angularVelocity.setAngle(aSpeed);
    	this.angularAcceleration = aAcceleration;
    }

    @Override
	public double getAngularAcceleration() {
		return this.angularAcceleration;
	}

    @Override
	public double getAngularAcceleration(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(this.angularAcceleration, unit);
	}

	@Override
	public double getAngularSpeed() {
		return this.angularVelocity.getAngle();
	}

	@Override
	public double getAngularSpeed(AngularUnit unit) {
		return MeasureUnitUtil.fromRadiansPerSecond(getAngularSpeed(), unit);
	}

	@Override
	public double getLinearAcceleration() {
		return this.linearAcceleration;
	}

	@Override
	public double getLinearAcceleration(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(this.linearAcceleration, unit);
	}

	@Override
	public double getLinearSpeed() {
		return this.linearVelocity.length();
	}

	@Override
	public double getLinearSpeed(SpeedUnit unit) {
		return MeasureUnitUtil.fromMetersPerSecond(getLinearSpeed(), unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getLinearVelocity3D() {
		return new Vector3d(this.linearVelocity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity2D() {
		return CoordinateSystem3D.getDefaultCoordinateSystem().toCoordinateSystem2D(getLinearVelocity3D());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2d getLinearVelocity1D5() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getLinearVelocity1D() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AxisAngle4d getAngularVelocity3D() {
		return new AxisAngle4d(this.angularVelocity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity2D() {
		return CoordinateSystem3D.getDefaultCoordinateSystem().toCoordinateSystem2D(getAngularVelocity3D());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity1D5() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getAngularVelocity1D() {
		throw new UnsupportedOperationException();
	}

}