/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.generation.builder;

import fr.utbm.set.jasim.environment.model.perception.frustum.Frustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.PedestrianFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.PyramidalFrustum;
import fr.utbm.set.jasim.environment.model.perception.frustum.RectangularFrustum3D;
import fr.utbm.set.jasim.environment.model.perception.frustum.SphericalFrustum;

/** Describe a frustum.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class FrustumDescription implements Cloneable {

	/** Type of the frustum.
	 */
	public final Class<? extends Frustum3D> fullName;
	
	/** Far distance
	 */
	public double farDistance = 0.;
	
	/** Near distance
	 */
	public double nearDistance = 0.;

	/** Lateral size of the frustum.
	 */
	public double lateralSize = 0.;

	/** Horizontal openness angle
	 */
	public double hOpennessAngle = 0.;

	/** Vertical openness angle
	 */
	public double vOpennessAngle = 0.;
	
	/** Eye position ratio according to the height of the body.
	 */
	public float eyeRatio = 0.95f;

	/**
	 * @param fn is the class of the frustum.
	 */
	public FrustumDescription(Class<? extends Frustum3D> fn) {
		this.fullName = fn;
		setToDefault();
	}

	@Override
	public final FrustumDescription clone() {
		return clone(this);
	}

	/** Clone this description but overwrite the parameter values with whose
	 * from the given description.
	 * 
	 * @param parametersToCopy
	 * @return the clone
	 */
	public FrustumDescription clone(FrustumDescription parametersToCopy) {
		FrustumDescription f = new FrustumDescription(this.fullName);
		f.eyeRatio = parametersToCopy.eyeRatio;
		f.farDistance = parametersToCopy.farDistance;
		f.hOpennessAngle = parametersToCopy.hOpennessAngle;
		f.lateralSize = parametersToCopy.lateralSize;
		f.nearDistance = parametersToCopy.nearDistance;
		f.vOpennessAngle = parametersToCopy.vOpennessAngle;
		return f;
	}

	@Override
	public String toString() {
		return this.fullName.getSimpleName();
	}

	@Override
	public int hashCode() {
		return this.fullName.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Class<?>) {
			return this.fullName.equals(o);
		}
		if (o instanceof FrustumDescription) {
			return this.fullName.equals(((FrustumDescription)o).fullName);
		}
		return super.equals(o);
	}
	
	/** Set the description to the default values according to the current frustum type.
	 */
	public void setToDefault() {
		if (isSphere()) {
			this.eyeRatio = 0.8f;
			this.farDistance = 150.;
			this.hOpennessAngle = 2.*Math.PI;
			this.lateralSize = 2.*this.farDistance;
			this.nearDistance = 0.;
			this.vOpennessAngle = 2.*Math.PI;
		}
		else if (isPyramid()) {
			this.eyeRatio = 0.8f;
			this.farDistance = 150.;
			this.hOpennessAngle = Math.PI/4.;
			this.lateralSize = Math.tan(this.hOpennessAngle) * this.farDistance;
			this.nearDistance = 2.;
			this.vOpennessAngle = Math.PI/4.;
		}
		else if (isPedestrian()) {
			this.eyeRatio = 0.8f;
			this.farDistance = 150.;
			this.hOpennessAngle = Math.PI/4.;
			this.lateralSize = Math.tan(this.hOpennessAngle) * this.farDistance;
			this.nearDistance = 2.;
			this.vOpennessAngle = Math.PI/4.;
		}
		else if (isRectangle()) {
			this.eyeRatio = 0.8f;
			this.farDistance = 150.;
			this.hOpennessAngle = Math.PI/2.;
			this.lateralSize = 10.;
			this.nearDistance = 0.;
			this.vOpennessAngle = Math.PI/2.;
		}
		else {
			this.eyeRatio = 0.8f;
			this.farDistance = 0.;
			this.hOpennessAngle = 0.;
			this.lateralSize = 0.;
			this.nearDistance = 0.;
			this.vOpennessAngle = 0.;
		}
	}
	
	/** Replies if the frustum described by this description is a spherical frustum.
	 * 
	 * @return <code>true</code> if the frustum is a spherical frustum, otherwise <code>false</code>
	 */
	public boolean isSphere() {
		return this.fullName!=null && SphericalFrustum.class.isAssignableFrom(this.fullName);
	}
	
	/** Replies if the frustum described by this description is a pyramidal frustum.
	 * 
	 * @return <code>true</code> if the frustum is a pyramidal frustum, otherwise <code>false</code>
	 */
	public boolean isPyramid() {
		return this.fullName!=null && PyramidalFrustum.class.isAssignableFrom(this.fullName);
	}

	/** Replies if the frustum described by this description is a pedestrian frustum.
	 * 
	 * @return <code>true</code> if the frustum is a pedestrian frustum, otherwise <code>false</code>
	 */
	public boolean isPedestrian() {
		return this.fullName!=null && PedestrianFrustum3D.class.isAssignableFrom(this.fullName);
	}

	/** Replies if the frustum described by this description is a rectangular frustum.
	 * 
	 * @return <code>true</code> if the frustum is a rectangular frustum, otherwise <code>false</code>
	 */
	public boolean isRectangle() {
		return this.fullName!=null && RectangularFrustum3D.class.isAssignableFrom(this.fullName);
	}

}
