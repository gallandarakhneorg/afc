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

/** This class is a 3d box.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoxMesh3D extends Mesh3D {

	private static final long serialVersionUID = -1163571734254097267L;

	/**
	 * Constructs a mesh. All the given points will be translated
	 * relatively to the center of the bounding box that encloses
	 * all the given points.
	 * 
	 * @param sizeFront is the size from back to front.
	 * @param sizeSide is the size from left to right.
	 * @param sizeVert is the size from bottom to top.
	 * @param onGround indicates if the box may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the box center will be at the origin.
	 */ 
	private static Point3d[] createBox(double sizeFront, double sizeSide, double sizeVert, boolean onGround) {
		double dx = sizeFront/2.;
		double z = onGround ? 0. : -sizeVert/2.;
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isZOnUp()) {
			double dy = sizeSide/2.;
			return new Point3d[] {
					new Point3d(-dx, -dy, z),
					new Point3d(-dx, +dy, z),
					new Point3d(+dx, -dy, z),
					new Point3d(+dx, +dy, z),
					new Point3d(-dx, -dy, z+sizeVert),
					new Point3d(-dx, +dy, z+sizeVert),
					new Point3d(+dx, -dy, z+sizeVert),
					new Point3d(+dx, +dy, z+sizeVert),
			};
		}

		double dz = sizeSide/2.;
		return new Point3d[] {
				new Point3d(-dx, z, -dz),
				new Point3d(-dx, z, +dz),
				new Point3d(+dx, z, -dz),
				new Point3d(+dx, z, +dz),
				new Point3d(-dx, z+sizeVert, -dz),
				new Point3d(-dx, z+sizeVert, +dz),
				new Point3d(+dx, z+sizeVert, -dz),
				new Point3d(+dx, z+sizeVert, +dz),
		};
	}

	/**
	 * Constructs a local box mesh.
	 * 
	 * @param sizeFront is the size from back to front.
	 * @param sizeSide is the size from left to right.
	 * @param sizeVert is the size from bottom to top.
	 * @param onGround indicates if the box may lies on ground or not.
	 * If <code>true</code>, the center of bottom side will be at the origin.
	 * if <code>false</code>, the box center will be at the origin.
	 */ 
	public BoxMesh3D(double sizeFront, double sizeSide, double sizeVert, boolean onGround) {
		super(true, createBox(sizeFront,sizeSide,sizeVert,onGround));
	}

	/** Clone this mesh.
	 * 
	 * @return a clone.
	 */
	@Override
	public BoxMesh3D clone() {
			return (BoxMesh3D)super.clone();
	}

}