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

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * This class contains environmental probed value.
 * 
 * @author $Author: sgalland$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProbeInfo {
	
	private final ProbeIdentifier id;
	private final Map<String,Object> values;
	
	/**
	 * @param placeId
	 * @param probeName
	 * @param probeValues
	 */
	public ProbeInfo(UUID placeId, String probeName, Map<String, Object> probeValues) {
		this.id = new ProbeIdentifier(placeId, probeName);
		this.values = probeValues;
	}
	
	/** Replies the identifier of the probe.
	 * 
	 * @return an identifier, never <code>null</code>.
	 */
	public ProbeIdentifier getProbeIdentifier() {
		return this.id;
	}
	
	/** Replies the probed values.
	 * 
	 * @return a set of pair &lt;value_id,probed_value&gt;, never <code>null</code>,
	 * the semantic of the value identifier and the probed value depend on the
	 * type and the implementation of the probed in JaSIM. 
	 */
	public Map<String,Object> getProbedValues() {
		if (this.values==null) return Collections.emptyMap();
		return Collections.unmodifiableMap(this.values);
	}
		
	/** Replies the probed value.
	 * 
	 * @param valueId is the identifier of the value in the probe.
	 * @return a set of pair &lt;value_id,probed_value&gt;, or <code>null</code>,
	 * the semantic of the value identifier and the probed value depend on the
	 * type and the implementation of the probed in JaSIM. 
	 */
	public Object getProbedValue(String valueId) {
		if (this.values==null) return null;
		return this.values.get(valueId);
	}
	
}