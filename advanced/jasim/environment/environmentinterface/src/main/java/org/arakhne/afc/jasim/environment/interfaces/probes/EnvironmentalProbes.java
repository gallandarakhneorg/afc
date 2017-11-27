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

/** This interface representes a manager of probes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalProbes extends Iterable<EnvironmentalProbe> {
		
	/** Add listener on probe changes.
	 * 
	 * @param listener
	 */
	public void addEnvironmentalProbesListener(EnvironmentalProbesListener listener);

	/** Add listener on probe changes.
	 * 
	 * @param listener
	 */
	public void removeEnvironmentalProbesListener(EnvironmentalProbesListener listener);

	/** Add a probe.
	 * 
	 * @param probe is the new probe.
	 * @return <code>true</code> if the probe was added, otherwise <code>false</code>
	 */
	public boolean addProbe(EnvironmentalProbe probe);
	
	/** Remove a prove.
	 * 
	 * @param probe is the probe to remove.
	 * @return <code>true</code> if the probe was removed, otherwise <code>false</code>
	 */
	public boolean removeProbe(EnvironmentalProbe probe);

	/** Remove a prove.
	 * 
	 * @param name is the name of the probe to remove.
	 * @return the removed probe or <code>null</code> if none.
	 */
	public EnvironmentalProbe removeProbe(String name);

	/** Replies the count of probes
	 * 
	 * @return the count of probes.
	 */
	public int size();

	/** Replies the probe with the given name.
	 * 
	 * @param name is the name of the probe to reply.
	 * @return the probe with the given index or <code>null</code> if no
	 * probe was found.
	 */
	public EnvironmentalProbe getProbe(String name);

	/** Replies the names of all the registered probes.
	 * 
	 * @return the probe names.
	 */
	public Iterable<String> getProbeNames();

}