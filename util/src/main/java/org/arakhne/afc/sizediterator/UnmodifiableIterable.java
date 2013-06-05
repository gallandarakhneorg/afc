/*
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.sizediterator;

import java.util.Iterator;

import org.arakhne.afc.util.UnmodifiableIterator;

/** Iterator which is disabling the {@code remove()} function.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnmodifiableIterable<OBJ> implements Iterable<OBJ> {

	private final Iterable<OBJ> iterable;
	
	/**
	 * @param it
	 */
	public UnmodifiableIterable(Iterable<OBJ> it) {
		this.iterable = it;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<OBJ> iterator() {
		return new UnmodifiableIterator<OBJ>(this.iterable.iterator());
	}

}
