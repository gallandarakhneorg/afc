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

import java.util.EventListener;

/** This interface representes a listener on environmental probe changes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalProbesListener extends EventListener {

	/** Invoked when a probe was added.
	 * 
	 * @param probe is the added probe.
	 */
	public void onProbeAdded(EnvironmentalProbe probe);

	/** Invoked when a probe was removed.
	 * 
	 * @param probe is the removed probe.
	 */
	public void onProbeRemoved(EnvironmentalProbe probe);

	/** Invoked when the values of the probes has been updated.
	 */
	public void onProbesChanged();

}