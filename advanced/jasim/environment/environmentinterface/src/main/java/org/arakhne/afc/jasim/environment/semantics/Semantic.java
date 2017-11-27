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
package org.arakhne.afc.jasim.environment.semantics;

import java.io.Serializable;

/** This interface describes a semantical tag associated to a world entity.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Semantic extends Serializable {
	
	/** Replies if the given semantic is the same as this semantic.
	 * 
	 * @param s
	 * @return <code>true</code> if they are the same, otherwise <code>false</code>
	 */
	public boolean equals(Semantic s);
	
	/** Replies if this semantic object is a subtype of the given semantic object
	 * 
	 * @param s
	 * @return <code>true</code> if this semantic is a subtype of the given semantic,
	 * otherwise <code>false</code>
	 * @see #isSubType(Semantic)
	 */
	public boolean isA(Semantic s);

	/** Replies if the given semantic is a subtype of this semantic object
	 * 
	 * @param s
	 * @return <code>true</code> if the given semantic is a subtype of this semantic,
	 * otherwise <code>false</code>
	 * @see #isA(Semantic)
	 */
	public boolean isSubType(Semantic s);
	
	/** Replies the negation of this type.
	 *
	 * @return the negation of this type, or <code>null</code>
	 */
	public Semantic negate();

}