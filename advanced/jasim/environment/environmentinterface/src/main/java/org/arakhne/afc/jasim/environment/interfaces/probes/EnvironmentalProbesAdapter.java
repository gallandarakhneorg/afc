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
package org.arakhne.afc.jasim.environment.interfaces.probes;

/** This interface representes a listener on environmental probe changes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EnvironmentalProbesAdapter implements EnvironmentalProbesListener {

	/** {@inheritDoc}
	 */
	@Override
	public void onProbeAdded(EnvironmentalProbe probe) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onProbeRemoved(EnvironmentalProbe probe) {
		//
	}

	/** {@inheritDoc}
	 */
	@Override
	public void onProbesChanged() {
		//
	}

}