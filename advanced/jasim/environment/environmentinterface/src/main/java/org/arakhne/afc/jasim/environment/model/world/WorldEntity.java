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
package org.arakhne.afc.jasim.environment.model.world;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import fr.utbm.set.geom.bounds.Bounds;

import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perceivable;
import org.arakhne.afc.jasim.environment.semantics.Semantic;
import org.arakhne.afc.jasim.environment.semantics.Semanticable;

/** This interface representes an object in a 1D, 2D or 3D space.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface WorldEntity<B extends Bounds<?,?,?>>
extends Semanticable, Perceivable, Serializable, Comparable<WorldEntity<? extends B>> {

	/** {@inheritDoc}
	 */
	@Override	
	public B getBounds();
	
	/** Associates a semantical tag to this entity.
	 * 
	 * @param semanticalTag is the tag to associate.
	 */
	public void addSemantic(Semantic... semanticalTag);

	/** Associates a semantical tag to this entity.
	 * 
	 * @param semanticalTag is the tag to associate.
	 * @deprecated see {@link #addSemantics(Collection)}
	 */
	@Deprecated
	public void addSemantic(Collection<? extends Semantic> semanticalTag);

	/** Associates a semantical tag to this entity.
	 * 
	 * @param semanticalTag is the tag to associate.
	 * @since 2.0
	 */
	public void addSemantics(Collection<? extends Semantic> semanticalTag);

	/** Unassociates a semantical tag from this entity.
	 * 
	 * @param semanticalTag is the tag to unassociate.
	 */
	public void removeSemantic(Semantic semanticalTag);

	/** Set a data associated to this entity.
	 * 
	 * @param name is the name of the data.
	 * @param value is the data or <code>null</code> to remove the data.
	 * @since 2.0
	 */
	public void setUserData(String name, Object value);

	/** Set a data associated to this entity.
	 * 
	 * @param userData is the data collection.
	 * @since 2.0
	 */
	public void setUserData(Map<String,Object> userData);

}