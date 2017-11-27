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

import fr.utbm.set.geom.object.Segment1D;
import fr.utbm.set.geom.transform.Transform1D;

/**
 * This class describes an influence inside a 1D situated environment.
 * <p>
 * <em>An influence is embedding a local transformation.</em>
 * 
 * @param <S> is the type of segment which is supported by this influence.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluence1D<S extends Segment1D> extends AbstractInfluence implements Influence1D<S> {

	private final Transform1D<S> transform;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence1D(Influencable influencedObject, Transform1D<S> transformation) {
		super(influencedObject);
		this.transform = transformation;
	}

	/**
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence1D(Transform1D<S> transformation) {
		this(null,transformation);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Transform1D<S> getTransformation() {
		return this.transform;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform1D<S> getTransformation(Clock time) {
		Transform1D<S> mat = new Transform1D<S>();
		
		TimeUnit t1 = JasimConstants.DEFAULT_SPEED_UNIT.toTimeUnit();
		
		double duration = time.getSimulationStepDuration(t1);
		mat.setTranslation(
				duration * this.transform.getCurvilineTransformation());
		
		return mat;
	}

}
