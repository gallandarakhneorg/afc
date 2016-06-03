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

package org.arakhne.afc.util;

import java.io.Serializable;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

/** This class implements triplet of values.
 *
 * @param <A> is the type of the first value.
 * @param <B> is the type of the second value.
 * @param <C> is the type of the third value.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Triplet<A, B, C> implements Serializable {

	private static final long serialVersionUID = -6979156649417633870L;

	private A avalue;

	private B bvalue;

	private C cvalue;

	/** Construct a triplet.
	 */
	public Triplet() {
		//
	}

	/** Construct a triplet.
	 *
	 * @param triplet the triplet.
	 */
	public Triplet(Triplet<? extends A, ? extends B, ? extends C> triplet) {
		this.avalue = triplet.getA();
		this.bvalue = triplet.getB();
		this.cvalue = triplet.getC();
	}

	/** Construct a triplet.
	 * @param firstValue first value.
	 * @param secondValue second value.
	 * @param thirdValue third value.
	 */
	public Triplet(A firstValue, B secondValue, C thirdValue) {
		this.avalue = firstValue;
		this.bvalue = secondValue;
		this.cvalue = thirdValue;
	}

	/** Replies the first value of the pair.
	 *
	 * @return the first value of the pair.
	 */
	@Pure
	public A getA() {
		return this.avalue;
	}

	/** Replies the second value of the pair.
	 *
	 * @return the second value of the pair.
	 */
	@Pure
	public B getB() {
		return this.bvalue;
	}

	/** Replies the third value of the pair.
	 *
	 * @return the third value of the pair.
	 */
	@Pure
	public C getC() {
		return this.cvalue;
	}

	/** Set the first value of the pair.
	 *
	 * @param firstValue is the first value of the pair.
	 */
	public void setA(A firstValue) {
		this.avalue = firstValue;
	}

	/** Set the second value of the pair.
	 *
	 * @param secondValue is the second value of the pair.
	 */
	public void setB(B secondValue) {
		this.bvalue = secondValue;
	}

	/** Set the third value of the pair.
	 *
	 * @param thirdValue is the third value of the pair.
	 */
	public void setC(C thirdValue) {
		this.cvalue = thirdValue;
	}

	@Pure
	@Override
	public String toString() {
		return "<" + this.avalue + ";" + this.bvalue //$NON-NLS-1$//$NON-NLS-2$
				+ ";" + this.cvalue + ">";  //$NON-NLS-1$//$NON-NLS-2$
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Triplet) {
			final Triplet<?, ?, ?> p = (Triplet<?, ?, ?>) obj;
			return innerEqual(this.avalue, p.avalue)
					&& innerEqual(this.bvalue, p.bvalue)
					&& innerEqual(this.cvalue, p.cvalue);
		}
		return false;
	}

	private static boolean innerEqual(Object value1, Object value2) {
		if (value1 == value2) {
			return true;
		}
		if (value1 == null || value2 == null) {
			return false;
		}
		return value1.equals(value2);
	}

	@Pure
	@Override
	public int hashCode() {
		int hash = HashCodeUtil.hash(this.avalue);
		hash = HashCodeUtil.hash(hash, this.bvalue);
		hash = HashCodeUtil.hash(hash, this.cvalue);
		return hash;
	}

}
