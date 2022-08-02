/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1.dfx;

import java.lang.ref.WeakReference;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Segment1D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Abstract shape with 2 double precision floating-point FX properties.
 *
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractShape1dfx<T extends AbstractShape1dfx<?>> implements Shape1dfx<T> {

	private static final long serialVersionUID = 5975659766737300344L;

	/** Bounding box property.
	 */
	protected ObjectProperty<Rectangle1dfx> boundingBox;

	/** segment reference.
	 */
	protected ObjectProperty<WeakReference<Segment1D<?, ?>>> segment;

	/** Constructor.
	 */
	protected AbstractShape1dfx() {
		this.segment = null;
	}

	/** Constructor.
	 *
	 * @param segment the segment.
	 */
	protected AbstractShape1dfx(Segment1D<?, ?> segment) {
		segmentProperty().set(new WeakReference<>(segment));
	}

	/** Replies the segment property.
	 *
	 * @return the segment property.
	 */
	@Pure
	public ObjectProperty<WeakReference<Segment1D<?, ?>>> segmentProperty() {
		if (this.segment == null) {
			this.segment = new SimpleObjectProperty<>(this, MathFXAttributeNames.SEGMENT);
		}
		return this.segment;
	}

	@Override
	public Segment1D<?, ?> getSegment() {
		if (this.segment == null) {
			return null;
		}
		final WeakReference<Segment1D<?, ?>> ref = segmentProperty().get();
		if (ref == null) {
			return null;
		}
		return ref.get();
	}

	@Override
	public void setSegment(Segment1D<?, ?> segment) {
		segmentProperty().set(new WeakReference<>(segment));
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

	@Pure
	@Override
	@SuppressWarnings({ "checkstyle:equalshashcode", "unchecked" })
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
	public GeomFactory1dfx getGeomFactory() {
		return GeomFactory1dfx.SINGLETON;
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
