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

import javax.vecmath.Point3d;

import fr.utbm.set.geom.system.CoordinateSystem3D;

/** This class contains a cylinder mesh.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CylinderMesh3D extends Mesh3D {

	private static final long serialVersionUID = 5607492755639797225L;
	
	/** Default count of points on the base.
	 */
	public static final int DEFAULT_POINT_COUNT_ON_BASE = 10;

	/**
	 * Constructs a local cylinder mesh.
	 * 
	 * @param radius is the radius of the cylinder
	 * @param height is the height of the cylinder from the base.
	 * @param pointCountOnBase is the count of points on the base.
	 * @param onGround indicates if the box may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the box center will be at the origin.
	 */ 
	private static Point3d[] createCylinder(double radius, double height, int pointCountOnBase, boolean onGround) {
		double delta = 2.*Math.PI / pointCountOnBase;
		double px, angle = 0.;
		double z = onGround ? 0 : -height/2.;
		Point3d[] pts = new Point3d[pointCountOnBase*2];
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isZOnUp()) {
			double py;
			for(int i=0, j=0; i<pointCountOnBase; ++i, angle+=delta) {
				px = Math.cos(angle)*radius;
				py = Math.sin(angle)*radius;
				
				pts[j++] = new Point3d(px,py,z);
				pts[j++] = new Point3d(px,py,z+height);
			}
		}
		else {
			double pz;
			for(int i=0, j=0; i<pointCountOnBase; ++i, angle+=delta) {
				px = Math.cos(angle)*radius;
				pz = Math.sin(angle)*radius;
				
				pts[j++] = new Point3d(px,z,pz);
				pts[j++] = new Point3d(px,z+height,pz);
			}
		}
		return pts;
	}

	/**
	 * Constructs a local cylinder.
	 * 
	 * @param radius is the radius of the cylinder
	 * @param height is the height of the cylinder from the base.
	 * @param onGround indicates if the box may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the box center will be at the origin.
	 * @param pointCountOnBase is the count of points on the base.
	 */ 
	public CylinderMesh3D(double radius, double height, boolean onGround, int pointCountOnBase) {
		super(true, createCylinder(radius,height,pointCountOnBase, onGround));
	}

	/**
	 * Constructs local cylinder.
	 * 
	 * @param radius is the radius of the cylinder
	 * @param height is the height of the cylinder from the base.
	 * @param onGround indicates if the box may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the box center will be at the origin.
	 */ 
	public CylinderMesh3D(double radius, double height, boolean onGround) {
		this(radius,height,onGround,DEFAULT_POINT_COUNT_ON_BASE);
	}

	/** Clone this mesh.
	 * 
	 * @return a clone.
	 */
	@Override
	public CylinderMesh3D clone() {
			return (CylinderMesh3D)super.clone();
	}

}