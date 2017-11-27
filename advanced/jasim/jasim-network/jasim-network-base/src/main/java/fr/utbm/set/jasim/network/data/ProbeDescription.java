/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.network.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class describes a probe to add inside the JaSIM simulator.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProbeDescription implements Serializable {
	
	private static final long serialVersionUID = -2688452075912269130L;
	
	private final ProbeIdentifier probeId;
	private final String type;
	private final Serializable[] parameters;
	
	/**
	 * @param placeId is the identifier of the place where the probe must be located.
	 * @param probeName is the name of the probe.
	 * @param probeType is the type of the probe, ie basically the probe's classname.
	 * @param parameters are the parameters to pass to the probe constructor.
	 */
	public ProbeDescription(UUID placeId, String probeName, String probeType, Serializable... parameters) {
		this.probeId = new ProbeIdentifier(placeId, probeName);
		this.type = probeType;
		this.parameters = parameters;
	}
	
	/** Replies the identifier of the probe.
	 * 
	 * @return the identifier of the probe.
	 */
	public ProbeIdentifier getProbeId() {
		return this.probeId;
	}
	
	/** Replies the type of the probe. Basically the probe's classname is used.
	 * 
	 * @return the type of the probe.
	 */
	public String getProbeType() {
		return this.type;
	}

	/** Replies the count of parameters.
	 * 
	 * @return the count of parameters.
	 */
	public int getParameterCount() {
		return this.parameters==null ? 0 : this.parameters.length;
	}

	/** Replies the parameter at the given position.
	 * 
	 * @param position
	 * @return the parameter value.
	 */
	public Object getParameterAt(int position) {
		return this.parameters[position];
	}
	
	/** Replies all the parameters.
	 * 
	 * @return the parameter values.
	 */
	public Serializable[] getParameters() {
		return this.parameters==null ? new Serializable[0] : this.parameters;
	}
}