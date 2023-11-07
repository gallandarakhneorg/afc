/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d1.d;

import java.lang.ref.WeakReference;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Abstract shape with 2 double precision floating-point numbers.
 *
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractShape1d<T extends AbstractShape1d<?>> implements Shape1d<T> {
	private static final long serialVersionUID = 4686282147250686518L;

	private WeakReference<Segment1D<?, ?>> segment;

	/** Constructor.
	 */
	protected AbstractShape1d() {
		this.segment = new WeakReference<>(null);
	}

	/** Constructor.
	 *
	 * @param segment the segment.
	 */
	protected AbstractShape1d(Segment1D<?, ?> segment) {
		this.segment = new WeakReference<>(segment);
	}

	@Override
	public Segment1D<?, ?> getSegment() {
		return this.segment.get();
	}

	@Override
	public void setSegment(Segment1D<?, ?> segment) {
		this.segment = new WeakReference<>(segment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public final boolean equals(Object obj) {
		try {
			return equalsToShape((T) obj);
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
	@Override
    public abstract int hashCode();

	@Override
	public final GeomFactory1d getGeomFactory() {
		return GeomFactory1d.SINGLETON;
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		ReflectionUtil.toJson(this, buffer);
	}

}
