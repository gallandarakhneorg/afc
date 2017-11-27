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

/** This class if common to all semantics
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSemantic implements Semantic {
	
	private static final long serialVersionUID = -2700348378380643581L;

	/**
	 */
	protected AbstractSemantic() {
		//
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		return getClass().hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object o) {
		if (o instanceof Semantic) {
			return equals((Semantic)o);
		}
		return false;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean equals(Semantic s) {
		if (s==null) return false;
		return getClass().equals(s.getClass());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString() {
		return getClass().getName();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isA(Semantic s) {
		if (s==null) return false;
		return s.getClass().isAssignableFrom(getClass());
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isSubType(Semantic s) {
		if (s==null) return false;
		return getClass().isAssignableFrom(s.getClass());
	}

	/** {@inheritDoc}
	 */
	@Override
	public Semantic negate() {
		return null;
	}

}