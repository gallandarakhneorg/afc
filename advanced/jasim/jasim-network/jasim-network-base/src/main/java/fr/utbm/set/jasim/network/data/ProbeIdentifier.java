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
 * This class describes a probe identifier.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ProbeIdentifier implements Serializable, Comparable<ProbeIdentifier> {
	
	private static final long serialVersionUID = -8911087040774168112L;
	
	private final UUID placeId;
	private final String probeName;
	
	/**
	 * @param placeId is the identifier of the place where the probe must be located.
	 * @param probeName is the name of the probe.
	 */
	public ProbeIdentifier(UUID placeId, String probeName) {
		this.placeId = placeId;
		this.probeName = probeName;
	}
	
	/** Replies the identifier of the place where the probe is located.
	 * 
	 * @return the identifier of the place where the probe is located.
	 */
	public UUID getPlaceId() {
		return this.placeId;
	}
	
	/** Replies the name of the probe.
	 * 
	 * @return the name of the probe.
	 */
	public String getProbeName() {
		return this.probeName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.placeId+":"+this.probeName; //$NON-NLS-1$
	}

	@Override
	public int compareTo(ProbeIdentifier o) {
		if (o==null) return 1;
		int cmp = this.placeId.compareTo(o.placeId);
		if (cmp==0) {
			cmp = this.probeName.compareTo(o.probeName);
		}
		return cmp;
	}
	
}