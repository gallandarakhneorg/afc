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

import java.util.Collection;
import java.util.LinkedList;

import fr.utbm.set.collection.SizedIterable;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.model.actions.EnvironmentalActionCollector;

/**
 * This collector is storing the influences inside a collection.
 * 
 * @param <EA> is the type of the supported environmental actions.
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultEnvironmentalActionCollector<EA extends EnvironmentalAction>
implements EnvironmentalActionCollector<EA> {

	private SizedLinkedList actions = new SizedLinkedList();
	
	/**
	 */
	public DefaultEnvironmentalActionCollector() {
		//
	}
	
	@Override
	public void addAction(EA action) {
		this.actions.add(action);
	}

	@Override
	public void addActions(Collection<? extends EA> newActions) {
		this.actions.addAll(newActions);
	}

	@Override
	public SizedIterable<EA> consumeActions() {
		SizedLinkedList theActions = this.actions;
		this.actions = new SizedLinkedList();
		return theActions;
	}

	/**
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class SizedLinkedList
	extends LinkedList<EA>
	implements SizedIterable<EA> {
		
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
