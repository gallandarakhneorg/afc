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
package fr.utbm.set.jasim.environment.interfaces.probes;

import java.io.File;

import org.arakhne.afc.util.ListenerCollection;

/** Abstract implements of an environmental probe.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractEnvironmentalProbe implements EnvironmentalProbe {
	
	private final String name;
	private final File logFilename;
	private ListenerCollection<EnvironmentalProbeListener> listeners = null;
	
	/**
	 * @param name is the name of the probe.
	 * @param logFile is the filename in which the probed values are logged,
	 * or <code>null</code> if the probe should be realtime.
	 */
	public AbstractEnvironmentalProbe(String name, File logFile) {
		this.name = name;
		this.logFilename = logFile;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isRealtimeProbe() {
		return this.logFilename!=null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public File getLogFile() {
		return this.logFilename;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final String getProbeName() {
		return this.name;
	}
	
	/** Collects the values.
	 */
	protected abstract void collect();
		
	/** {@inheritDoc}
	 */
	@Override
	public void addEnvironmentalProbeListener(EnvironmentalProbeListener listener) {
		if (this.listeners==null)
			this.listeners = new ListenerCollection<EnvironmentalProbeListener>();
		this.listeners.add(EnvironmentalProbeListener.class, listener);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeEnvironmentalProbeListener(EnvironmentalProbeListener listener) {
		if (this.listeners!=null) {
			this.listeners.remove(EnvironmentalProbeListener.class, listener);
			if (this.listeners.isEmpty()) {
				this.listeners = null;
			}
		}
	}

}