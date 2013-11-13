/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.afc.math.geometry;



/** This class representes the abstract implementation of bounds.
 *
 * @param <P> is the type of the points when replied by a function.
 * @param <P> is the type of the points when passed as parameter.
 * @param <V> is the type of the vectors when replied by a function.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBounds<P,V> implements Bounds<P,V> {

	private static final long serialVersionUID = -2249400747897441687L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distance(P p) {
		return (float) Math.sqrt(distanceSquared(p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceMax(P p) {
		return (float) Math.sqrt(distanceMaxSquared(p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public P getPosition() {
		return getCenter();
	}
	
	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Bounds<P,V> clone() {
		try {
			return (Bounds<P,V>)super.clone();
		}
		catch(Exception e) {
			throw new Error(e);
		}
		catch(AssertionError e) {
			throw e;
		}
		catch(Throwable e) {
			throw new Error(e);
		}
	}

}