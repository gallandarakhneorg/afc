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
import fr.utbm.set.geom.transform.Transform1D5;

/**
 * This class describes an influence inside a 1.5D situated environment.
 * <p>
 * <em>An influence is embedding a local transformation.</em>
 * 
 * @param <S> is the type of segment supported by the 1.5D space.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluence1D5<S extends Segment1D> extends AbstractInfluence implements Influence1D5<S> {

	private final Transform1D5<S> transform;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence1D5(Influencable influencedObject, Transform1D5<S> transformation) {
		super(influencedObject);
		this.transform = transformation;
	}

	/**
	 * @param transformation is the transformation embedded in this influence.
	 */
	public DefaultInfluence1D5(Transform1D5<S> transformation) {
		this(null,transformation);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Transform1D5<S> getTransformation() {
		return this.transform;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform1D5<S> getTransformation(Clock time) {
		Transform1D5<S> mat = new Transform1D5<S>(
				this.transform.getPath(),
				this.transform.getFirstSegmentPathDirection());
		
		TimeUnit t1 = JasimConstants.DEFAULT_SPEED_UNIT.toTimeUnit();
		
		double duration = time.getSimulationStepDuration(t1);
		mat.setTranslation(
				duration * this.transform.getCurvilineTransformation(),
				duration * this.transform.getJuttingTransformation());
		
		return mat;
	}

}
