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


/**
 * This class describes an influence.
 * <p>
 * <em>An influence is embedding a local transformation.</em>
 *  
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractInfluence implements Influence {

	private Influencable influencedObject;
	private Influencer influencerObject;

	/**
	 * @param influencedObject is the object that must be influenced.
	 */
	public AbstractInfluence(Influencable influencedObject) {
		this.influencedObject = influencedObject;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Influencable getInfluencedObject() {
		return this.influencedObject;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInfluencedObject(Influencable influencedObject) {
		this.influencedObject = influencedObject;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Influencer getInfluencer() {
		return this.influencerObject;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setInfluencer(Influencer influencer) {
		this.influencerObject = influencer;
	}

}
