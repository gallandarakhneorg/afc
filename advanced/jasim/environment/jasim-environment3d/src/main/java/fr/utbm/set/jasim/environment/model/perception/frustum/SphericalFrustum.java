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
import javax.vecmath.Tuple3d;

import fr.utbm.set.geom.bounds.bounds3d.BoundingSphere;
import fr.utbm.set.geom.object.EuclidianPoint3D;
import fr.utbm.set.geom.transform.Transform3D;

/**
 * A frustum represented by a sphere.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SphericalFrustum extends BoundingSphere implements Frustum3D {

	private static final long serialVersionUID = 2158786473839782273L;
	
	private final UUID id;

	/**
	 * Uninitialized frustum.
	 * 
	 * @param id
	 */
	public SphericalFrustum(UUID id) {
    	this.id = id;
    }
    
	/**
	 * @param id
	 * @param x is center of the sphere
	 * @param y is center of the sphere
	 * @param z is center of the sphere
	 * @param radius is radius of the sphere
	 */
    public SphericalFrustum(UUID id, double x, double y, double z, double radius) {
        super(x,y,z,radius);
        this.id = id;
    }
    
	/**
	 * @param id
	 * @param p is center of the sphere
	 * @param radius is radius of the sphere
	 */
    public SphericalFrustum(UUID id, Tuple3d p, double radius) {
        super(p,radius);
        this.id = id;
    }

	/** Clone this frustum.
	 */
    @Override
	public SphericalFrustum clone() {
		return (SphericalFrustum)super.clone();
	}

	/** {@inheritDoc}
	 */
	@Override
    public double getFarDistance() {
    	return getRadius();
    }

	/** {@inheritDoc}
	 */
	@Override
    public double getNearDistance() {
    	return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
	public String toString() {
    	Point3d p = this.getCenter();
    	StringBuilder sb = new StringBuilder(512);
        sb.append("Frustum[X:"); //$NON-NLS-1$
        sb.append(p.x);
        sb.append(" Y:"); //$NON-NLS-1$
        sb.append(p.y);
        sb.append(" Z:"); //$NON-NLS-1$
        sb.append(p.z);
        sb.append(" radius:"); //$NON-NLS-1$
        sb.append(getRadius());
        sb.append(']');
        return sb.toString();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quat4d q) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quat4d q) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void transform(Transform3D trans) {
		translate(trans.getTranslationVector());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getEye() {
		return getPosition();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getIdentifier() {
		return this.id;
	}
    
}
