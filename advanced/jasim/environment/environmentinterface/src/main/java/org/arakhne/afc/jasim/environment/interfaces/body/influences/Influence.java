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
package org.arakhne.afc.jasim.environment.interfaces.body.influences;


/** This class describes an influence inside a situated environment.
 * An influence corresponds to an action of an agent which does not directly the environment.
 * An influence corresponds to the wish of an agent to modify the environment
 * The result of this modification tentative is then computed by the environment if
 * it doesn't have any conflict with other influence and if it doesn't violate environmental laws
 * <p>
 * Careful : An influence contains only one local transformation
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Influence {
	
	/** Replies the object which is influenced.
	 * 
	 * @return the object which is influenced.
	 */
	public Influencable getInfluencedObject();
	
	/** Set the object which is influenced.
	 * 
	 * @param influencedObject is the object which is influenced.
	 */
	public void setInfluencedObject(Influencable influencedObject);

	/** Replies the influencer.
	 * 
	 * @return the object which is influencing.
	 */
	public Influencer getInfluencer();
	
	/** Set the influencer.
	 * 
	 * @param influencer is the object which is influencing.
	 */
	public void setInfluencer(Influencer influencer);


}