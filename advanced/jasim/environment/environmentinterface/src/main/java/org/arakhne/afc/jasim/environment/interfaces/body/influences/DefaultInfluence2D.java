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

import org.arakhne.afc.jasim.environment.JasimConstants;
import org.arakhne.afc.jasim.environment.time.Clock;

import fr.utbm.set.geom.transform.Transform2D;

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
public class DefaultInfluence2D extends AbstractInfluence implements Influence2D {

	private final Transform2D transfom2D;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence2D(Influencable influencedObject, Transform2D transformation) {
		super(influencedObject);
		this.transfom2D = transformation;
	}

	/**
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence2D(Transform2D transformation) {
		this(null,transformation);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Transform2D getTransformation() {
		return this.transfom2D;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform2D getTransformation(Clock time) {
		Transform2D mat = new Transform2D();
		
		TimeUnit t1 = JasimConstants.DEFAULT_SPEED_UNIT.toTimeUnit();
		TimeUnit t2 = JasimConstants.DEFAULT_ROTATION_UNIT.toTimeUnit();
		
		double duration = time.getSimulationStepDuration(t1);
		mat.setTranslation(
				duration * this.transfom2D.getTranslationX(),
				duration * this.transfom2D.getTranslationY());
		
		if (t1!=t2) duration = time.getSimulationStepDuration(t2);
		mat.setRotation(duration * this.transfom2D.getRotation());
		
		return mat;
	}

}
