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

import java.lang.reflect.Constructor;
import java.util.List;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Tuple3d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.Mesh;

/** This class contains a mesh ie, a set of 3D points.
 * <p>
 * The origin from which ponit coordinates are computed is
 * outside the scope of this class.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Mesh3D implements Mesh<Point3d> {
	
	private static final long serialVersionUID = 7080629164323339648L;

	private Point3d[] points;
	
	private boolean localCoordinates;
	
	/**
	 * Constructs a mesh.
	 * 
	 * @param <T> is the type of the points inside the mesh.
	 * @param localCoordinates indicates if the given points are assumed to
	 * be local if <code>true</code> or globel if <code>false</code> to
	 * a reference point.
	 */ 
	<T extends Tuple3d> Mesh3D(boolean localCoordinates) {
		this.localCoordinates = localCoordinates;
		this.points = null;
	}

	/**
	 * Constructs a mesh.
	 * 
	 * @param <T> is the type of the points inside the mesh.
	 * @param localCoordinates indicates if the given points are assumed to
	 * be local if <code>true</code> or globel if <code>false</code> to
	 * a reference point.
	 * @param points is the set of points that compose this mesh.
	 */ 
	public <T extends Tuple3d> Mesh3D(boolean localCoordinates, T[] points) {
		this.localCoordinates = localCoordinates;
		this.points = new Point3d[points.length];
		for(int idx=0; idx<points.length; ++idx) {
			this.points[idx] = new Point3d(points[idx].getX(), points[idx].getY(), points[idx].getZ());
		}
	}

	/**
	 * Constructs a mesh.
	 * 
	 * @param localCoordinates indicates if the given points are assumed to
	 * be local if <code>true</code> or globel if <code>false</code> to
	 * a reference point.
	 * @param points is the set of points that compose this mesh.
	 */ 
	public Mesh3D(boolean localCoordinates, List<? extends Tuple3d> points) {
		this.localCoordinates = localCoordinates;
		this.points = new Point3d[points.size()];
		Tuple3d t;
		for(int idx=0; idx<points.size(); ++idx) {
			t = points.get(idx);
			this.points[idx] = new Point3d(t.getX(), t.getY(), t.getZ());
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3d[] getPoints() {
		return this.points;
	}
	
	/** Sets the points in this mesh relatively to the internal pivot.
	 * 
	 * @param points are the points in this mesh.
	 */
	protected void setPoints(Point3d[] points) {
		this.points = points;
	}

	/** Create the bounds for a set of points.
	 * 
	 * @param <B> is the type of the bounding box
	 * @param boundClass is the type of the bounding box to create
	 * @return the created bounding box.
	 */
	public <B extends CombinableBounds3D> B toBounds(Class<? extends B> boundClass) {
		assert(this.points!=null);
		try {
			Constructor<? extends B> cons = boundClass.getConstructor(Tuple3d[].class);
			return cons.newInstance((Object)(this.points));
		}
		catch(Exception _) {
			//
		}
		try {
			Constructor<? extends B> cons = boundClass.getConstructor();
			B bounds = cons.newInstance();
			bounds.set(this.points);
			return bounds;
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Create the bounds for a set of points.
	 * 
	 * @param bounds is the bounds to update.
	 * @param rotation is the rotation of the points around the pivot point (could be <code>null</code>).
	 * @param pivot is the pivot point.
	 */
	public void getBounds(CombinableBounds3D bounds, Quat4d rotation, Tuple3d pivot) {
		assert(this.points!=null);
		bounds.reset();

		Matrix4d m = GeometryUtil.rotateAround(rotation, pivot);
		
		Point3d r = new Point3d();		
		for(Point3d p : this.points) {
			m.transform(p,r);
			bounds.combine(r);
		}
	}

	/** Create the bounds for a set of points.
	 * 
	 * @param bounds is the bounds to update.
	 * @param rotation is the rotation of the points around the origin (could be <code>null</code>).
	 */
	public void getBounds(CombinableBounds3D bounds, Quat4d rotation) {
		assert(this.points!=null);
		bounds.reset();

		Matrix4d m = new Matrix4d();
		m.set(rotation);
		
		Point3d r = new Point3d();		
		for(Point3d p : this.points) {
			m.transform(p,r);
			bounds.combine(r);
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void makeLocal(Point3d localReference) {
		assert(localReference!=null);
		if (this.points!=null) {
			for(Point3d p : this.points) {
				p.sub(localReference);
			}
		}
		this.localCoordinates = true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void makeGlobal(Point3d localReference) {
		assert(localReference!=null);
		if (this.points!=null) {
			for(Point3d p : this.points) {
				p.add(localReference);
			}
		}
		this.localCoordinates = false;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isLocalMesh() {
		return this.localCoordinates;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isGlobalMesh() {
		return !this.localCoordinates;
	}
	
	/** Clone this mesh.
	 * 
	 * @return a clone.
	 */
	@Override
	public Mesh3D clone() {
		try {
			return (Mesh3D)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

}