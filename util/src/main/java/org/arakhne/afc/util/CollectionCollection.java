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
package org.arakhne.afc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.sizediterator.IteratorIterator;
import org.arakhne.afc.sizediterator.FilteredIteratorIterator;

/** Collection of collections.
 * This class merge several collections without memory consumption.
 * It maintains references to the original collections.
 * The collection of collections is read only.
 * 
 * @param <OBJ> is the type of the objects to iterator on.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CollectionCollection<OBJ> implements Collection<OBJ> {

	private final Class<OBJ> type;
	private final Collection<?> col1;
	private final Collection<?> col2;
	
	private int size = -1;
	
	/**
	 * @param col1 is the first collection.
	 * @param col2 is the second collection.
	 */
	public CollectionCollection(Collection<? extends OBJ> col1, Collection<? extends OBJ> col2) {
		this.type = null;
		this.col1 = col1;
		this.col2 = col2;
	}

	/**
	 * @param type is the type of the collection
	 * @param col1 is the first collection.
	 * @param col2 is the second collection.
	 */
	CollectionCollection(Class<OBJ> type, Collection<?> col1, Collection<?> col2) {
		this.type = type;
		this.col1 = col1;
		this.col2 = col2;
	}

	@Override
	public boolean add(OBJ e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends OBJ> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		return (this.type==null || this.type.isInstance(o))
				&& 
				(this.col1.contains(o) || this.col2.contains(o));
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		List<Object> tmp = new ArrayList<Object>(c);
		tmp.removeAll(this.col1);
		tmp.removeAll(this.col2);
		return tmp.isEmpty();
	}

	@Override
	public boolean isEmpty() {
		return this.col1.isEmpty() && this.col2.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<OBJ> iterator() {
		if (this.type==null)
			return new IteratorIterator<OBJ>(((Collection<? extends OBJ>)this.col1).iterator(), ((Collection<? extends OBJ>)this.col2).iterator());
		return new FilteredIteratorIterator<OBJ>(this.type, this.col1.iterator(), this.col2.iterator());
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		if (this.size==-1) {
			if (this.type==null) {
				this.size = this.col1.size()+this.col2.size();
			}
			else {
				Iterator<OBJ> i = iterator();
				this.size = 0;
				while (i.hasNext()) {
					i.next();
					++this.size;
				}
			}
		}
		return this.size;
	}

	@Override
	public Object[] toArray() {
		Object[] tab;
		if (this.type==null) {
			tab = new Object[this.col1.size()+this.col2.size()];
			this.col1.toArray(tab);
			System.arraycopy(this.col2.toArray(), 0, tab, this.col1.size(), this.col2.size());
		}
		else {
			tab = new Object[size()];
			Iterator<OBJ> i = iterator();
			for(int idx=0; (i.hasNext()); ++idx) {
				tab[idx] = i.next();
			}
		}
		return tab;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		int s = size();
		T[] b;
		if (a.length<s) {
			b = (T[])new Object[s];
		}
		else {
			b = a;
		}
		if (this.type==null) {
			this.col1.toArray(b);
			System.arraycopy(this.col2.toArray(), 0, b, this.col1.size(), this.col2.size());
		}
		else {
			Iterator<OBJ> i = iterator();
			for(int idx=0; (i.hasNext()); ++idx) {
				b[idx] = (T)i.next();
			}
		}
		return b;
	}

}
