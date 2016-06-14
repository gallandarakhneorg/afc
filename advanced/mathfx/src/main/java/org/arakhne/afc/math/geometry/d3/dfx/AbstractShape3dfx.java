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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.property.ObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.vmutil.ReflectionUtil;

/** Abstract shape with 3 double precision floating-point FX properties.
 *
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractShape3dfx<T extends AbstractShape3dfx<?>> implements Shape3dfx<T> {

	private static final long serialVersionUID = 5975659766737300344L;

	/** Bounding box property.
	 */
	protected ObjectProperty<RectangularPrism3dfx> boundingBox;

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
		if (this == obj) {
			return true;
		}
		try {
			try {
				return equalsToShape((T) obj);
			} catch (ClassCastException exception) {
				return equalsToPathIterator((PathIterator3D<?>) obj);
			}
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
	@Override
	public abstract int hashCode();

	@Override
	public final GeomFactory3dfx getGeomFactory() {
		return GeomFactory3dfx.SINGLETON;
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

}
