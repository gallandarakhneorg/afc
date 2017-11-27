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
package fr.utbm.set.jasim.environment.model.influencereaction;

import java.util.LinkedList;

import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencable;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influencer;
import fr.utbm.set.jasim.environment.model.influences.InfluenceCollector;

/**
 * This collector is storing the influences inside a collection.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultInfluenceCollector
implements InfluenceCollector {

	private SizedLinkedList populationInfluences = new SizedLinkedList();
	private SizedLinkedList otherInfluences = new SizedLinkedList();
	
	/**
	 */
	public DefaultInfluenceCollector() {
		//
	}

	@Override
	public SizedIterable<? extends Influence> consumeStandardInfluences() {
		SizedIterable<Influence> theInfluences = this.otherInfluences;
		this.otherInfluences = new SizedLinkedList();
		return theInfluences;
	}

	@Override
	public SizedIterable<? extends Influence> consumePopulationInfluences() {
		SizedIterable<Influence> theInfluences = this.populationInfluences;
		this.populationInfluences = new SizedLinkedList();
		return theInfluences;
	}

	@Override
	public void registerInfluence(
			Influencer defaultInfluencer,
			Influencable defaultInfluencedObject, 
			Influence... influences) {
		for(Influence inf : influences) {
			if (inf.getInfluencer()!=defaultInfluencer) inf.setInfluencer(defaultInfluencer);
			if (InfluenceSolverUtil.isPopulationChangeInfluence(inf)) {
				inf.setInfluencedObject(defaultInfluencedObject);
				this.populationInfluences.add(inf);
			}
			else {
				if (inf.getInfluencedObject()==null) inf.setInfluencedObject(defaultInfluencedObject);
				this.otherInfluences.add(inf);
			}
		}
	}
	
	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SizedLinkedList
	extends LinkedList<Influence>
	implements SizedIterable<Influence> {
		
		/**
		 */
		private static final long serialVersionUID = -3511151636140713733L;

		/**
		 */
		public SizedLinkedList() {
			//
		}
		
	}
	
}
