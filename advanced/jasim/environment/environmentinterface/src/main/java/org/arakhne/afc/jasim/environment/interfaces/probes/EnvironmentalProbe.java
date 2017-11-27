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

import java.io.File;
import java.util.Map;
import java.util.UUID;

/** This interface representes a probe in a environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface EnvironmentalProbe {
	
	/** Replies the name of the probe.
	 * 
	 * @return the probe name.
	 */
	public String getProbeName();
	
	/** Replies the location of the probe.
	 * 
	 * @return the probe location.
	 */
	public UUID getPlace();

	/** Replies the collected informations.
	 * 
	 * @return a map in which keys are the value names and values the collected values.
	 */
	public Map<String,Object> getCollectedValues();
		
	/** Replies if this probe is a realtime probe.
	 * <p>
	 * A realtime probe sent the probed values to the listeners at
	 * each step of the simulation. If a probe is not realtime, the
	 * probe values are logged into a predefined logfile.
	 * 
	 * @return <code>true</code> if the probe is realtime, otherwise <code>false</code>
	 */
	public boolean isRealtimeProbe();

	/** Replies the file in which this probe is supposed to log its values.
	 * The log file is used only if the valued returned by
	 * {@link #isRealtimeProbe()} is <code>false</code>.
	 * 
	 * @return the log file.
	 */
	public File getLogFile();
	
	/** Add listener on probe changes.
	 * 
	 * @param listener
	 */
	public void addEnvironmentalProbeListener(EnvironmentalProbeListener listener);

	/** Remove listener on probe changes.
	 * 
	 * @param listener
	 */
	public void removeEnvironmentalProbeListener(EnvironmentalProbeListener listener);

}