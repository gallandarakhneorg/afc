/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.references;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class is a WeakReference that allows to be
 * compared on its pointed value.
 *
 * @param <T> is the type of the referenced object.
 * @author $Author: sgalland$
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
		super(referent, queue);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		return compareTo(obj) == 0;
	}

	@Pure
	@Override
	public int hashCode() {
		return Objects.hashCode(get());
	}

	/** Compare this reference to the specified object
	 * based on the {@link Object#hashCode()} if the
	 * references are not equals.
	 *
	 * @param obj {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Pure
	@Override
	@SuppressWarnings({"unchecked", "checkstyle:npathcomplexity"})
	public int compareTo(Object obj) {
		final Object oth = (obj instanceof Reference) ? ((Reference<?>) obj).get() : obj;
		final T cur = get();

		if (oth == null && cur == null) {
			return 0;
		}
		if (cur == null) {
			return 1;
		}
		if (oth == null) {
			return -1;
		}

		if (cur instanceof Comparable) {
			try {
				return ((Comparable<Object>) cur).compareTo(oth);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable exception) {
				//
			}
		}

		if (oth instanceof Comparable) {
			try {
				return -((Comparable<Object>) oth).compareTo(cur);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable exception) {
				//
			}
		}

		return oth.hashCode() - cur.hashCode();
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append('{');
		final T obj = get();
		if (obj == null) {
			buffer.append("#null#"); //$NON-NLS-1$
		} else {
			buffer.append(obj.toString());
		}
		buffer.append('}');
		return buffer.toString();
	}

}
