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
package fr.utbm.set.jasim.environment.model.world;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.jasim.environment.semantics.Semantic;

/** This class representes an object in a 1D, 2D or 3D space.
 *
 * @param <B> is the type of the bounding box associated to this entity.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractWorldEntity<B extends Bounds<?,?,?>> implements WorldEntity<B> {

	private static final long serialVersionUID = 5858628685992465398L;

	private final UUID identifier;
	
	private Map<String,Object> userData = null;
	
	/**
	 * @param id is the identifier of this entity.
	 */
	public AbstractWorldEntity(UUID id) {
		this.identifier = id;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final void addSemantic(Semantic... semanticalTag) {
		addSemantics(Arrays.asList(semanticalTag));
	}

	/** {@inheritDoc}
	 */
	@Override
	@Deprecated
	public final void addSemantic(Collection<? extends Semantic> semanticalTag) {
		addSemantics(semanticalTag);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Object getUserData(String name) {
		return name==null || this.userData==null ? null : this.userData.get(name); 
	}

	/** {@inheritDoc}
	 */
	@Override
	public <T> T getUserData(String name, Class<T> type) {
		Object obj = name==null || this.userData==null ? null : this.userData.get(name);
		if (obj!=null && type.isInstance(obj)) return type.cast(obj);
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserData(String name, Object value) {
		assert(name!=null);
		if (value==null) {
			if (this.userData!=null) {
				this.userData.remove(name);
				if (this.userData!=null && this.userData.isEmpty()) {
					this.userData = null;
				}
			}
		}
		else {
			if (this.userData==null) {
				this.userData = new TreeMap<String,Object>();
			}
			this.userData.put(name, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserData(Map<String,Object> userData) {
		if (userData!=null && !userData.isEmpty()) {
			if (this.userData==null) {
				this.userData = new TreeMap<String,Object>(userData);
			}
			else {
				this.userData.putAll(userData);
			}
		}
		else {
			this.userData = null;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final UUID getIdentifier() {
		return this.identifier;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int compareTo(WorldEntity<? extends B> object) {
		if (object==null) return 1;
		UUID oId = object.getIdentifier();
		if (this.identifier==null && oId==null) return 0;
		if (oId==null) return 1;
		if (this.identifier==null) return -1;
		return this.identifier.compareTo(oId);
	}

	/** Replies if the given object is equal to this entity.
	 * <p>
	 * Two objects are equal if they have the same identifier and the same
	 * bounds.
	 * 
	 * @param obj is the object to compare to this entity.
	 * @return <code>true</code> if the given object is equal to this entity,
	 * otherwise <code>false</code>
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WorldEntity<?>) {
			WorldEntity<?> e = (WorldEntity<?>)obj;
			return (getIdentifier().equals(e.getIdentifier()))&&
				   (getBounds().equals(e.getBounds()));
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((this.identifier==null) ? 0 : this.identifier.hashCode());
        B b = getBounds();
        result = PRIME * result + ((b==null) ? 0 : b.hashCode());
        return result;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.identifier.toString();
	}
		
}