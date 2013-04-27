/* 
 * $Id$
 * 
 * Copyright (C) 2005-2009 Stephane GALLAND This library is free software; you can redistribute it and/or
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

package org.arakhne.util.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * This class is a WeakReference that allows to be
 * compared on its pointed value.
 * 
 * @param <T> is the type of the referenced object. 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ComparableSoftReference<T> extends SoftReference<T> implements Comparable<Object> {

	/**
	 * @param referent is the referenced object.
	 */
	public ComparableSoftReference(T referent) {
		super(referent);
	}
	
	/**
	 * @param referent is the referenced object.
	 * @param queue is the object that will be notified of the memory released for the referenced object.
	 */
	public ComparableSoftReference(T referent, ReferenceQueue<? super T> queue) {
		super(referent,queue);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return compareTo(o)==0;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		T cur = get();
		return cur==null ? 0 : cur.hashCode();
	}

	/** Compare this reference to the specified object
	 * based on the {@link Object#hashCode()} if the
	 * references are not equals.
	 * 
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(Object o) {
		Object oth = (o instanceof Reference) ? ((Reference<?>)o).get() : o;
		T cur = get();
		
		if (oth==null && cur==null) return 0;
		if (cur==null) return 1;
		if (oth==null) return -1;
			
		if (cur instanceof Comparable) {
			try {
				return ((Comparable<Object>)cur).compareTo(oth);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable _) {
				//
			}
		}

		if (oth instanceof Comparable) {
			try {
				return -((Comparable<Object>)oth).compareTo(cur);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable _) {
				//
			}
		}

		return oth.hashCode() - cur.hashCode();			
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		T obj = get();
		if (obj==null) buffer.append("#null#"); //$NON-NLS-1$
		else buffer.append(obj.toString());
		buffer.append('}');
		return buffer.toString();
	}

}
