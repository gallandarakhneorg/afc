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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.arakhne.afc.util.ListenerCollection;

/** An implementation of a probe manager.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EnvironmentalProbeManager implements EnvironmentalProbes {

	private final Map<String,AbstractEnvironmentalProbe> probes = new TreeMap<String,AbstractEnvironmentalProbe>();
	
	private final ListenerCollection<EnvironmentalProbesListener> listeners = new ListenerCollection<EnvironmentalProbesListener>();
	
	/**
	 */
	public EnvironmentalProbeManager() {	
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addEnvironmentalProbesListener(EnvironmentalProbesListener listener) {
		this.listeners.add(EnvironmentalProbesListener.class, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeEnvironmentalProbesListener(EnvironmentalProbesListener listener) {
		this.listeners.remove(EnvironmentalProbesListener.class, listener);
	}
	
	/** Fire the probe addition event.
	 * 
	 * @param probe
	 */
	protected void fireProbeAddition(EnvironmentalProbe probe) {
		EnvironmentalProbesListener[] list = this.listeners.getListeners(EnvironmentalProbesListener.class);
		for(EnvironmentalProbesListener listener : list) {
			listener.onProbeAdded(probe);
		}
	}

	/** Fire the probe removal event.
	 * 
	 * @param probe
	 */
	protected void fireProbeRemoval(EnvironmentalProbe probe) {
		EnvironmentalProbesListener[] list = this.listeners.getListeners(EnvironmentalProbesListener.class);
		for(EnvironmentalProbesListener listener : list) {
			listener.onProbeRemoved(probe);
		}
	}

	/** Fire the probe change event.
	 */
	protected void fireProbeChange() {
		EnvironmentalProbesListener[] list = this.listeners.getListeners(EnvironmentalProbesListener.class);
		for(EnvironmentalProbesListener listener : list) {
			listener.onProbesChanged();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addProbe(EnvironmentalProbe probe) {
		if (probe instanceof AbstractEnvironmentalProbe) {
			this.probes.put(probe.getProbeName(),(AbstractEnvironmentalProbe)probe);
			fireProbeAddition(probe);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeProbe(EnvironmentalProbe probe) {
		if (probe instanceof AbstractEnvironmentalProbe
			&& this.probes.remove(probe.getProbeName())!=null) {
			fireProbeRemoval(probe);
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentalProbe removeProbe(String name) {
		AbstractEnvironmentalProbe probe = this.probes.remove(name);
		if (probe!=null) fireProbeRemoval(probe);
		return probe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentalProbe getProbe(String name) {
		return this.probes.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.probes.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<EnvironmentalProbe> iterator() {
		return new ProbeIterator(this.probes.values().iterator());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getProbeNames() {
		return this.probes.keySet();
	}
	
	/** Remove all the probes.
	 */
	public void clear() {
		Collection<EnvironmentalProbe> probesForListeners = new ArrayList<EnvironmentalProbe>(this.probes.size());
		probesForListeners.addAll(this.probes.values());
		this.probes.clear();
		for(EnvironmentalProbe probe : probesForListeners) {
			fireProbeRemoval(probe);
		}
	}

	/** Refresh the probe values
	 */
	public void refresh() {
		for(AbstractEnvironmentalProbe probe : this.probes.values()) {
			probe.collect();
		}
		fireProbeChange();
	}

	/**
	 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid fr.utbm.set.sfc.jasim
	 * @mavenartifactid jasim-environment-base
	 */
	private class ProbeIterator implements Iterator<EnvironmentalProbe> {

		private final Iterator<AbstractEnvironmentalProbe> iter;
		
		private EnvironmentalProbe probe = null;
		
		public ProbeIterator(Iterator<AbstractEnvironmentalProbe> i) {
			this.iter = i;
		}

		@Override
		public boolean hasNext() {
			return this.iter.hasNext();
		}

		@Override
		public EnvironmentalProbe next() {
			EnvironmentalProbe aProbe = this.iter.next();
			this.probe = (aProbe==null) ? null : aProbe;
			return aProbe;
		}

		@Override
		public void remove() {
			if (this.probe==null) throw new IllegalStateException();
			this.iter.remove();
			if (this.probe!=null) {
				fireProbeRemoval(this.probe);
				this.probe = null;
			}
		}
		
	}
	
}
