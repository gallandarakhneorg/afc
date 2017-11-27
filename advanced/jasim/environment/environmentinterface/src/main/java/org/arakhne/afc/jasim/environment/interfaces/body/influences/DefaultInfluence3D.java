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
package org.arakhne.afc.jasim.environment.interfaces.body.influences;

import java.util.concurrent.TimeUnit;

import javax.vecmath.AxisAngle4d;

import org.arakhne.afc.jasim.environment.JasimConstants;
import org.arakhne.afc.jasim.environment.time.Clock;

import fr.utbm.set.geom.transform.Transform3D;

/**
 * This class describes an influence inside a 2D situated environment.
 * <p>
 * <em>An influence is embedding a local transformation.</em>
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluence3D extends AbstractInfluence implements Influence3D {

	private final Transform3D transfom3D;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence3D(Influencable influencedObject, Transform3D transformation) {
		super(influencedObject);
		this.transfom3D = transformation;
	}

	/**
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence3D(Transform3D transformation) {
		this(null,transformation);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Transform3D getTransformation() {
		return this.transfom3D;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform3D getTransformation(Clock time) {
		Transform3D mat = new Transform3D();
		
		TimeUnit t1 = JasimConstants.DEFAULT_SPEED_UNIT.toTimeUnit();
		TimeUnit t2 = JasimConstants.DEFAULT_ROTATION_UNIT.toTimeUnit();
		
		double duration = time.getSimulationStepDuration(t1);
		mat.setTranslation(
				duration * this.transfom3D.getTranslationX(),
				duration * this.transfom3D.getTranslationY(),
				duration * this.transfom3D.getTranslationZ());
		
		if (t1!=t2) duration = time.getSimulationStepDuration(t2);
		AxisAngle4d initRotation = this.transfom3D.getRotation();
		mat.setRotation(new AxisAngle4d(
				initRotation.x,initRotation.y,initRotation.z,
				duration * initRotation.angle));
		
		return mat;
	}

}
