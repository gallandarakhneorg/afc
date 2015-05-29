/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry3d.continuous;


/**
 * This interface describes an object that permits to classify intersection
 * between objects and planes.
 * 
 * @author $Author: cbohrhauer$
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
    public PlanarClassificationType classifies(Plane otherPlane);

    /**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param vec
	 * @return the classification
	 */
    public PlanarClassificationType classifies(Tuple3f vec);
    
	/**
	 * Classifies a point with respect to the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the classification
	 */
    public PlanarClassificationType classifies(float x, float y, float z);

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
    public PlanarClassificationType classifies(float lx, float ly, float lz, float ux, float uy, float uz);

	/**
	 * Classifies a sphere with respect to the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return the classification
	 */
    public PlanarClassificationType classifies(float x, float y, float z, float radius);

	/**
	 * Replies if the given plane is intersecting the plane.
	 *
	 * @param otherPlane is the plane to classify
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    public boolean intersects(Plane otherPlane);

    /**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param vec
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    public boolean intersects(Tuple3f vec);
    
	/**
	 * Replies if the given point is intersecting the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    public boolean intersects(float x, float y, float z);

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
    public boolean intersects(float lx, float ly, float lz, float ux, float uy, float uz);

	/**
	 * Replies if the given sphere is intersecting the plane.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 * @return <code>true</code> if intersection, otherwise <code>false</code>
	 */
    public boolean intersects(float x, float y, float z, float radius);

}