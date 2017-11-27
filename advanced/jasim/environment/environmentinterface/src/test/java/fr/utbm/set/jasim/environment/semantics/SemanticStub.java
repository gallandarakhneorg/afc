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
package fr.utbm.set.jasim.environment.semantics;

import org.arakhne.afc.jasim.environment.semantics.Semantic;

/**
 * Semantic usable for unit tests.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class SemanticStub implements Semantic {

	private static final long serialVersionUID = -1823018767307097445L;
	
	private SemanticStub negation;

	/**
	 */
	public SemanticStub() {
		this.negation = new SemanticStub(this);
	}

	private SemanticStub(SemanticStub negate) {
		this.negation = negate;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean equals(Semantic s) {
		if (s==null) return false;
		return getClass().equals(s.getClass());
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

	@Override
	public Semantic negate() {
		return this.negation;
	}

}
