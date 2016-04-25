/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This interface describes an object that permits to classify intersection
 * between objects and planes.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PlaneClassifier {

	/**
	 * Classifies a plane with respect to the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return the classification
	 */
	@Pure
    public PlaneClassification classifies(Plane3D<?> otherPlane);

    /**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param vec
	 * @return the classification
	 */
	@Pure
    public PlaneClassification classifies(Tuple3D<?> vec);
    
	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the classification
	 */
	@Pure
    public PlaneClassification classifies(double x, double y, double z);

	/**
	 * Classifies a box with respect to the plane.
	 * 
	 * @param lx
	 * @param ly
	 * @param lz
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return the classification
	 */
	@Pure
    public PlaneClassification classifies(double lx, double ly, double lz, double ux, double uy, double uz);

	/**
	 * Classifies a sphere with respect to the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return the classification
	 */
	@Pure
    public PlaneClassification classifies(double x, double y, double z, double radius);

	/**
	 * Replies if the given plane is intersecting the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	@Pure
    public boolean intersects(Plane3D<?> otherPlane);

    /**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param vec
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	@Pure
    public boolean intersects(Tuple3D<?> vec);
    
	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	@Pure
    public boolean intersects(double x, double y, double z);

	/**
	 * Replies if the given box is intersecting the plane.
	 * 
	 * @param lx
	 * @param ly
	 * @param lz
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	@Pure
    public boolean intersects(double lx, double ly, double lz, double ux, double uy, double uz);

	/**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
	@Pure
    public boolean intersects(double x, double y, double z, double radius);

}
